package com.example.prashantbhardwaj.teqnitask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StopProjectionActivity extends AppCompatActivity {

    private Button bStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_projection);

        bStop = (Button) findViewById(R.id.bStop);

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success){
                                Intent intent = new Intent(StopProjectionActivity.this, StudentActivity.class);
                                StopProjectionActivity.this.startActivity(intent);
                                Toast.makeText(StopProjectionActivity.this, "Projection Stopped", Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                ProjectionStopRequest projectionStoptRequest = new ProjectionStopRequest("2", responseListener);
                RequestQueue queue = Volley.newRequestQueue(StopProjectionActivity.this);
                queue.add(projectionStoptRequest);
            }
        });
    }
}
