package com.example.prashantbhardwaj.teqnitask;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteSessionActivity extends AppCompatActivity {

    private String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_session);

        sessionName = getIntent().getExtras().get("session").toString();

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success){
                        Intent intent = new Intent(DeleteSessionActivity.this, TeacherActivity.class);
                        DeleteSessionActivity.this.startActivity(intent);
                        Toast toast = Toast.makeText(DeleteSessionActivity.this, "Successfully updated", Toast.LENGTH_LONG);
                        toast.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteSessionActivity.this);
                        builder.setMessage("Delete Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DeleteSessionRequest deleteSessionRequest = new DeleteSessionRequest(sessionName, responseListener);
        RequestQueue queue = Volley.newRequestQueue(DeleteSessionActivity.this);
        queue.add(deleteSessionRequest);
    }
}
