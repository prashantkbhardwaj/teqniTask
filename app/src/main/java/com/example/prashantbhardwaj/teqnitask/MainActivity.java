package com.example.prashantbhardwaj.teqnitask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SessionManagement session;
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String usertype = user.get(SessionManagement.KEY_USERTYPE);
        System.out.println(usertype);
        if (usertype==null||usertype==""){
            usertype = "usertype";
        } else {
            session.checkUserType(usertype);
        }
    }
}
