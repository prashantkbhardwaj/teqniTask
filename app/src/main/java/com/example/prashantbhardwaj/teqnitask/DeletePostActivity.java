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

public class DeletePostActivity extends AppCompatActivity {

    private String postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post);

        postid = getIntent().getExtras().get("postid").toString();

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success){
                        Intent intent = new Intent(DeletePostActivity.this, TeacherActivity.class);
                        DeletePostActivity.this.startActivity(intent);
                        Toast toast = Toast.makeText(DeletePostActivity.this, "Successfully updated", Toast.LENGTH_LONG);
                        toast.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DeletePostActivity.this);
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

        DeletePostRequest deletePostRequest = new DeletePostRequest(postid, responseListener);
        RequestQueue queue = Volley.newRequestQueue(DeletePostActivity.this);
        queue.add(deletePostRequest);

    }
}
