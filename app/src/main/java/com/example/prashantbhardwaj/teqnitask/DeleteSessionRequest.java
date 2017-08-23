package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 23/08/17.
 */

public class DeleteSessionRequest extends StringRequest{
    private Map<String, String> params;

    public DeleteSessionRequest(String sessionName, Response.Listener<String> listener){
        super(Method.POST, Config.DELETE_SESSION_URL, listener, null);
        params = new HashMap<>();
        params.put("sessionName", sessionName);
    }

    @Override
    public java.util.Map<String, String> getParams() {
        return params;
    }
}
