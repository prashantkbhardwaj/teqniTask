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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadVideoActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bSelectVideo;
    private Button bUploadVideo;
    private TextView tvSource;

    private static final int SELECT_VIDEO = 3;
    private String selectedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        bSelectVideo = (Button) findViewById(R.id.bSelectVideo);
        bUploadVideo = (Button) findViewById(R.id.bUploadVideo);
        tvSource = (TextView) findViewById(R.id.tvSource);

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
                Toast.makeText(UploadVideoActivity.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
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
