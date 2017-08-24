package com.example.prashantbhardwaj.teqnitask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class UploadVideoActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bSelectVideo;
    private Button bUploadVideo;
    private TextView tvSource;
    private EditText etVideoName;
    private static final int SELECT_VIDEO = 3;
    private String selectedPath;
    private String KEY_UPLOADER = "uploader";
    private String KEY_SESSIONNAME = "sessionname";
    private String KEY_VIDEONAME = "picturename";
    private String KEY_LEVEL1 = "level1";
    private String KEY_LEVEL2 = "level2";
    private String KEY_LEVEL3 = "level3";
    private String KEY_QRCODE = "qrcode";
    private String KEY_VIDEO = "video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        bSelectVideo = (Button) findViewById(R.id.bSelectVideo);
        bUploadVideo = (Button) findViewById(R.id.bUploadVideo);
        tvSource = (TextView) findViewById(R.id.tvSource);

        etVideoName = (EditText) findViewById(R.id.etVideoName);

        bSelectVideo.setOnClickListener(this);
        bUploadVideo.setOnClickListener(this);

    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                tvSource.setText(selectedPath);
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadVideoHere() {
        class UploadVideohere extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(UploadVideoActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                //Toast.makeText(UploadVideoActivity.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                UploadVideo u = new UploadVideo();
                String msg = u.uploadVideo(selectedPath);
                return msg;
            }
        }
        UploadVideohere uv = new UploadVideohere();
        uv.execute();

        final ProgressDialog loadingNow = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_VIDEO_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loadingNow.dismiss();
                        //Showing toast message of the response
                        Intent intent = new Intent(UploadVideoActivity.this, TeacherActivity.class);
                        UploadVideoActivity.this.startActivity(intent);
                        Toast.makeText(UploadVideoActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loadingNow.dismiss();

                        //Showing toast
                        Toast.makeText(UploadVideoActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                final SessionManagement session;
                session = new SessionManagement(getApplicationContext());
                session.checkLogin();

                HashMap<String, String> user = session.getUserDetails();
                String uploader = user.get(SessionManagement.KEY_USERNAME);
                String qrurl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+getIntent().getExtras().get("level1").toString()+"_"+getIntent().getExtras().get("level2").toString()+"_"+getIntent().getExtras().get("level3").toString()+"_"+getIntent().getExtras().get("session").toString();

                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put(KEY_VIDEO, selectedPath);
                params.put(KEY_UPLOADER, uploader);
                params.put(KEY_LEVEL1, getIntent().getExtras().get("level1").toString());
                params.put(KEY_LEVEL2, getIntent().getExtras().get("level2").toString());
                params.put(KEY_LEVEL3, getIntent().getExtras().get("level3").toString());
                params.put(KEY_VIDEONAME, etVideoName.getText().toString());
                params.put(KEY_SESSIONNAME, getIntent().getExtras().get("session").toString());
                params.put(KEY_QRCODE, qrurl);
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        if (v == bSelectVideo) {
            chooseVideo();
        }
        if (v == bUploadVideo) {
            uploadVideoHere();
        }
    }
}
