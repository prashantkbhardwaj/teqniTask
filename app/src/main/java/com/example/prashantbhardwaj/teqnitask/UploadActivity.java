package com.example.prashantbhardwaj.teqnitask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bSelect;
    private ImageView ivPicture;
    private EditText etTimeDuration;
    private Button bUpload;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "image";
    private String KEY_UPLOADER = "uploader";
    private String KEY_SESSIONNAME = "sessionname";
    private String KEY_TIMEDURATION = "timeduration";
    private String KEY_LEVEL1 = "level1";
    private String KEY_LEVEL2 = "level2";
    private String KEY_LEVEL3 = "level3";
    private String KEY_QRCODE = "qrcode";

    private int REQUEST_CAMERA = 0;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        bSelect = (Button) findViewById(R.id.bSelect);
        bUpload = (Button) findViewById(R.id.bUpload);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        etTimeDuration = (EditText) findViewById(R.id.etTimeDuration);

        bSelect.setOnClickListener(this);
        bUpload.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo")) {
                        cameraIntent();
                    }
                    else {
                        //code for deny
                    }
                }
                break;
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loadingnow = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_PICTURE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loadingnow.dismiss();
                        //Showing toast message of the response
                        Intent intent = new Intent(UploadActivity.this, TeacherActivity.class);
                        UploadActivity.this.startActivity(intent);
                        Toast.makeText(UploadActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loadingnow.dismiss();

                        //Showing toast
                        Toast.makeText(UploadActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name

                final SessionManagement session;
                session = new SessionManagement(getApplicationContext());
                session.checkLogin();

                HashMap<String, String> user = session.getUserDetails();
                String uploader = user.get(SessionManagement.KEY_USERNAME);
                String qrurl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+getIntent().getExtras().get("level1").toString()+"_"+getIntent().getExtras().get("level2").toString()+"_"+getIntent().getExtras().get("level3").toString()+"_"+getIntent().getExtras().get("session").toString();

                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_UPLOADER, uploader);
                params.put(KEY_LEVEL1, getIntent().getExtras().get("level1").toString());
                params.put(KEY_LEVEL2, getIntent().getExtras().get("level2").toString());
                params.put(KEY_LEVEL3, getIntent().getExtras().get("level3").toString());
                params.put(KEY_SESSIONNAME, getIntent().getExtras().get("session").toString());
                params.put(KEY_TIMEDURATION, etTimeDuration.getText().toString());
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void optionChooser(){
        final CharSequence[] items = {
                "Camera", "Gallery"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
               // bSelect.setText(items[item]);
                boolean result=Utility.checkPermission(UploadActivity.this);
                if (items[item].equals("Gallery")){
                    showFileChooser();
                } else if (items[item].equals("Camera")){
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                ivPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivPicture.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onClick(View v) {

        if(v == bSelect){
            optionChooser();
            //showFileChooser();
        }

        if(v == bUpload){
            uploadImage();
        }
    }

}
