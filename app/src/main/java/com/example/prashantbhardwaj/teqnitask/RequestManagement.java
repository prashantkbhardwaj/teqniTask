package com.example.prashantbhardwaj.teqnitask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by prashantbhardwaj on 11/08/17.
 */

public class RequestManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "TeqniHomeReqPref";
    public static final String LEVEL1IN = "level1in";

    public RequestManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void putData(String level1in){
        editor.putString(LEVEL1IN, level1in);
        editor.commit();
    }

    public HashMap<String, String> getStoredData(){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(LEVEL1IN, pref.getString(LEVEL1IN, null));
        return data;
    }
}
