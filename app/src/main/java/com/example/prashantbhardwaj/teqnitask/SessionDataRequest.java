package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 24/08/17.
 */

public class SessionDataRequest extends StringRequest {
    private Map<String, String> params;

    public SessionDataRequest(String username, String sessionName,Response.Listener<String> listener){
        super(Method.POST, Config.SESSION_DATA_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("sessionName", sessionName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
