package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 23/08/17.
 */

public class SessionLoadRequest extends StringRequest {
    private Map<String, String> params;

    public SessionLoadRequest(String username, Response.Listener<String> listener){
        super(Method.POST, Config.SESSION_LOAD_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
