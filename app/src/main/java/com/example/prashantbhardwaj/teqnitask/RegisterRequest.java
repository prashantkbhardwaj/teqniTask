package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 08/08/17.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://192.168.1.101/fileTransfers/teqniHome/register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username, String password, String usertype, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("usertype", usertype);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
