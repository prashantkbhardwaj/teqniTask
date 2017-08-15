package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 14/08/17.
 */

public class ProjectionStopRequest extends StringRequest {
    private static final String PROJECTION_REQUEST_URL = "http://192.168.1.101/fileTransfers/teqniHome/stopState.php";
    private Map<String, String> params;

    public ProjectionStopRequest(String data, Response.Listener<String> listener){
        super(Method.POST, PROJECTION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("data", data);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
