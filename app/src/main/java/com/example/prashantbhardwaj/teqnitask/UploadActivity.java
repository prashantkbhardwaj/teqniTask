package com.example.prashantbhardwaj.teqnitask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadActivity extends AppCompatActivity {

    private ProgressDialog loading;
    private Button bSelect;
    private ImageView ivPicture;
    private TextView tvLevel1;
    private TextView tvLevel2;
    private TextView tvLevel3;
    private Spinner spLevel1;
    private Spinner spLevel2;
    private Spinner spLevel3;
    private EditText etSessionName;
    private EditText etPictureName;
    private EditText etTimeDuration;
    private Button bUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        getData();
    }

    private void getData() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.SPINNER_DATA_URL.toString().trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        String level1 ="";
        String level1opt ="";
        String level2 = "";
        String level2opt = "";
        String level3 = "";
        String level3opt = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            level1 = jsonObject.getString(Config.KEY_LEVEL1);
            System.out.println(level1);
            level1opt = jsonObject.getString(Config.KEY_LEVEL1OPT);
            level2 = jsonObject.getString(Config.KEY_LEVEL2);
            level2opt = jsonObject.getString(Config.KEY_LEVEL2OPT);
            level3 = jsonObject.getString(Config.KEY_LEVEL3);
            level3opt = jsonObject.getString(Config.KEY_LEVEL3OPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] level1List = level1opt.toString().split(",");
        String[] level2List = level2opt.toString().split(",");
        String[] level3List = level3opt.toString().split(",");

        spLevel1 = (Spinner) findViewById(R.id.spLevel1);
        spLevel2 = (Spinner) findViewById(R.id.spLevel2);
        spLevel3 = (Spinner) findViewById(R.id.spLevel3);

        tvLevel1 = (TextView) findViewById(R.id.tvLevel1);
        tvLevel2 = (TextView) findViewById(R.id.tvLevel2);
        tvLevel3 = (TextView) findViewById(R.id.tvLevel3);

        tvLevel1.setText(level1);
        tvLevel2.setText(level2);
        tvLevel3.setText(level3);

        ArrayAdapter<String> spLevel1ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, level1List);
        spLevel1ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spLevel1.setAdapter(spLevel1ArrayAdapter);

        ArrayAdapter<String> spLevel2ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, level2List);
        spLevel2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spLevel2.setAdapter(spLevel2ArrayAdapter);

        ArrayAdapter<String> spLevel3ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, level3List);
        spLevel3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spLevel3.setAdapter(spLevel3ArrayAdapter);


    }

}
