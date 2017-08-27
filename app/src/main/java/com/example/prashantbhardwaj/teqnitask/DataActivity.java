package com.example.prashantbhardwaj.teqnitask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DataActivity extends AppCompatActivity {

    private List<SuperHeroes> listSuperHeroes;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private TextView tvSessionView;
    private TextView tvTree;
    private TextView tvDesc;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2, fab3, fab4;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private String KEY_USER = "user";
    private String KEY_SESSION = "session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tvSessionView = (TextView) findViewById(R.id.tvSessionView);
        tvTree = (TextView) findViewById(R.id.tvTree);
        tvDesc = (TextView) findViewById(R.id.tvDesc);

        tvSessionView.setText(getIntent().getExtras().get("sess").toString());

       // System.out.println(getIntent().getExtras().get("sess").toString());

        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();

        //Calling method to get data
        getData();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        final String level1 = jsonResponse.getString("level1");
                        final String level2 = jsonResponse.getString("level2");
                        final String level3 = jsonResponse.getString("level3");
                        final String description = jsonResponse.getString("description");

                        tvTree.setText(level1+"-"+level2+"-"+level3);
                        tvDesc.setText(description);

                        fab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DataActivity.this, UploadVideoActivity.class);
                                intent.putExtra("session", getIntent().getExtras().get("sess").toString());
                                intent.putExtra("level1", level1);
                                intent.putExtra("level2", level2);
                                intent.putExtra("level3", level3);
                                DataActivity.this.startActivity(intent);
                            }
                        });

                        fab2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DataActivity.this, UploadActivity.class);
                                intent.putExtra("session", getIntent().getExtras().get("sess").toString());
                                intent.putExtra("level1", level1);
                                intent.putExtra("level2", level2);
                                intent.putExtra("level3", level3);
                                DataActivity.this.startActivity(intent);
                            }
                        });

                        fab3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DataActivity.this, DeleteSessionActivity.class);
                                intent.putExtra("session", getIntent().getExtras().get("sess").toString());
                                DataActivity.this.startActivity(intent);
                            }
                        });

                        fab4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DataActivity.this, EditSessionActivity.class);
                                intent.putExtra("session", getIntent().getExtras().get("sess").toString());
                                intent.putExtra("level1", level1);
                                intent.putExtra("level2", level2);
                                intent.putExtra("level3", level3);
                                DataActivity.this.startActivity(intent);
                            }
                        });

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);
                        builder.setMessage("Load Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        final SessionManagement session;
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManagement.KEY_USERNAME);

        SessionDataRequest sessionDataRequest = new SessionDataRequest(username, getIntent().getExtras().get("sess").toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(DataActivity.this);
        queue.add(sessionDataRequest);


    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            isFabOpen = false;
            Log.d("PKB", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
            Log.d("PKB","open");

        }
    }

    private void getData(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        StringRequest stringRequestDown = new StringRequest(Request.Method.POST, Config.DOWNLOAD_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();

                        try {
                            JSONArray array = new JSONArray(response);

                            for(int i = 0; i<array.length(); i++) {
                                SuperHeroes superHero = new SuperHeroes();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    superHero.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                                    superHero.setDate(json.getString(Config.TAG_DATE));
                                    superHero.setTimeDuration(json.getString(Config.TAG_TIME_DURATION));
                                    superHero.setPostid(json.getString(Config.TAG_POSTID));
                                    superHero.setPos(json.getString(Config.TAG_POS));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                listSuperHeroes.add(superHero);
                            }

                            //Finally initializing our adapter
                            adapter = new CardAdapter(listSuperHeroes, DataActivity.this);

                            //Adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                final SessionManagement session;
                session = new SessionManagement(getApplicationContext());
                session.checkLogin();
                HashMap<String, String> user = session.getUserDetails();
                String name = user.get(SessionManagement.KEY_NAME);
                String username = user.get(SessionManagement.KEY_USERNAME);

                //Creating parameters
                Map<String, String> params = new Hashtable<>();

                //Adding parameters
                params.put(KEY_USER, username);
                params.put(KEY_SESSION, getIntent().getExtras().get("sess").toString());
                //returning parameters
                return params;
            }
        };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequestDown);
    }
}
