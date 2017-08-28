package com.example.prashantbhardwaj.teqnitask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CreateSessionActivity extends AppCompatActivity {

    private EditText etSessionName;
    private Spinner spLevel1Opt;
    private Spinner spLevel2Opt;
    private Spinner spLevel3Opt;
    private TextView tvLevel1;
    private TextView tvLevel2;
    private TextView tvLevel3;
    private EditText etDesc;
    private Button bCreateSession;
    private ProgressDialog loading;
    private String level1 ="";
    private String level1opt ="";
    private String level2 = "";
    private String level2opt = "";
    private String level3 = "";
    private String level3opt = "";
    private String KEY_UPLOADER = "uploader";
    private String KEY_LEVEL1 = "level1";
    private String KEY_LEVEL2 = "level2";
    private String KEY_LEVEL3 = "level3";
    private String KEY_SESSIONNAME = "sessionName";
    private String KEY_DESCRIPTION = "description";
    private String level2Arr;
    private String[] level2ListSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        etSessionName = (EditText) findViewById(R.id.etSessionNameC);
        etDesc = (EditText) findViewById(R.id.etDesc);
        spLevel1Opt = (Spinner) findViewById(R.id.spLevel1OptC);
        spLevel2Opt = (Spinner) findViewById(R.id.spLevel2OptC);
        spLevel3Opt = (Spinner) findViewById(R.id.spLevel3OptC);
        tvLevel1 = (TextView) findViewById(R.id.tvLevel1C);
        tvLevel2 = (TextView) findViewById(R.id.tvLevel2C);
        tvLevel3 = (TextView) findViewById(R.id.tvLevel3C);
        bCreateSession = (Button) findViewById(R.id.bCreateSession);

        getData();

        bCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSession();
            }
        });

    }

    private void getData() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.SPINNER_DATA_URL.toString().trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
                final RequestManagement requestManagement;
                requestManagement = new RequestManagement(getApplicationContext());
                requestManagement.putData(level1);
                //System.out.println("inside: "+level1);
            }
        },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateSessionActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {

            JSONObject jsonObject = new JSONObject(response);
            level1 = jsonObject.getString(Config.KEY_LEVEL1);
            level1opt = jsonObject.getString(Config.KEY_LEVEL1OPT);
            level2 = jsonObject.getString(Config.KEY_LEVEL2);
            level2opt = jsonObject.getString(Config.KEY_LEVEL2OPT);
            level3 = jsonObject.getString(Config.KEY_LEVEL3);
            level3opt = jsonObject.getString(Config.KEY_LEVEL3OPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String[] level1List = level1opt.toString().split(",");
        final String[] level2List = level2opt.toString().split("_");
        final String[] level3List = level3opt.toString().split("/");

        tvLevel1.setText(level1);
        tvLevel2.setText(level2);
        tvLevel3.setText(level3);

        ArrayAdapter<String> spLevel1ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, level1List);
        spLevel1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spLevel1Opt.setAdapter(spLevel1ArrayAdapter);

        spLevel1Opt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < level1List.length; i++){
                    if (level1List[i].equals(String.valueOf(spLevel1Opt.getSelectedItem()))){
                        level2Arr = level2List[i];
                        level2ListSelect = level2Arr.toString().split(",");
                        ArrayAdapter<String> spLevel2ArrayAdapter = new ArrayAdapter<String>(CreateSessionActivity.this,   android.R.layout.simple_spinner_item, level2ListSelect);
                        spLevel2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        spLevel2Opt.setAdapter(spLevel2ArrayAdapter);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spLevel2Opt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int j = 0; j < level1List.length; j++){
                    if (level1List[j].equals(String.valueOf(spLevel1Opt.getSelectedItem()))){
                        String level3Arr = level3List[j];
                        String[] level3Group = level3Arr.toString().split("_");
                        for (int i = 0; i < level2ListSelect.length; i++){
                            if (level2ListSelect[i].equals(String.valueOf(spLevel2Opt.getSelectedItem()))){
                                String level3Final = level3Group[i];
                                String[] level3ListSelect = level3Final.toString().split(",");
                                ArrayAdapter<String> spLevel3ArrayAdapter = new ArrayAdapter<String>(CreateSessionActivity.this,   android.R.layout.simple_spinner_item, level3ListSelect);
                                spLevel3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spLevel3Opt.setAdapter(spLevel3ArrayAdapter);
                            }
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void createSession(){
        //Showing the progress dialog
        final ProgressDialog loadingnow = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREATE_SESSION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loadingnow.dismiss();
                        //Showing toast message of the response
                        Intent intent = new Intent(CreateSessionActivity.this, TeacherActivity.class);
                        CreateSessionActivity.this.startActivity(intent);
                        Toast.makeText(CreateSessionActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loadingnow.dismiss();

                        //Showing toast
                        Toast.makeText(CreateSessionActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                final SessionManagement session;
                session = new SessionManagement(getApplicationContext());
                session.checkLogin();

                HashMap<String, String> user = session.getUserDetails();
                String uploader = user.get(SessionManagement.KEY_USERNAME);

                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put(KEY_UPLOADER, uploader);
                params.put(KEY_LEVEL1, String.valueOf(spLevel1Opt.getSelectedItem()));
                params.put(KEY_LEVEL2, String.valueOf(spLevel2Opt.getSelectedItem()));
                params.put(KEY_LEVEL3, String.valueOf(spLevel3Opt.getSelectedItem()));
                params.put(KEY_SESSIONNAME, etSessionName.getText().toString());
                params.put(KEY_DESCRIPTION, etDesc.getText().toString());

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
