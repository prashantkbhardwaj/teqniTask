package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 13/08/17.
 */

public class ProjectionStartRequest extends StringRequest {
    private static final String PROJECTION_REQUEST_URL = "http://www.vit5icnn2018.com/teqniHome/updateState.php";
    private Map<String, String> params;

    public ProjectionStartRequest(String data, Response.Listener<String> listener){
        super(Method.POST, PROJECTION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("data", data);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
