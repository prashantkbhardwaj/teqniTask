package com.example.prashantbhardwaj.teqnitask;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPostActivity extends AppCompatActivity {

    private String postid;
    private EditText etName;
    private EditText etTimeDuration;
    private Button bUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postid = getIntent().getExtras().get("postid").toString();
        etName = (EditText) findViewById(R.id.etNameU);
        etTimeDuration = (EditText) findViewById(R.id.etTimeDurationU);

        if ((getIntent().getExtras().get("name").toString()).equals(null)||(getIntent().getExtras().get("name").toString()).equals("")){
            etName.setText("name");
            etTimeDuration.setText("0");
        } else {
            etName.setText(getIntent().getExtras().get("name").toString());
            etTimeDuration.setText(getIntent().getExtras().get("timeDuration").toString());
        }

        bUpdate = (Button) findViewById(R.id.bUpdate);

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success){
                                Intent intent = new Intent(EditPostActivity.this, TeacherActivity.class);
                                EditPostActivity.this.startActivity(intent);
                                Toast toast = Toast.makeText(EditPostActivity.this, "Successfully updated", Toast.LENGTH_LONG);
                                toast.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                                builder.setMessage("Update Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                EditPostRequest editPostRequest = new EditPostRequest(postid, etName.getText().toString(), etTimeDuration.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(EditPostActivity.this);
                queue.add(editPostRequest);
            }
        });

    }
}
