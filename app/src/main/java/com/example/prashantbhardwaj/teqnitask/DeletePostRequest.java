package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 18/08/17.
 */

public class DeletePostRequest extends StringRequest {
    private Map<String, String> params;

    public DeletePostRequest(String postid, Response.Listener<String> listener){
        super(Method.POST, Config.DELETE_POST_URL, listener, null);
        params = new HashMap<>();
        params.put("postid", postid);
    }

    @Override
    public java.util.Map<String, String> getParams() {
        return params;
    }
}
