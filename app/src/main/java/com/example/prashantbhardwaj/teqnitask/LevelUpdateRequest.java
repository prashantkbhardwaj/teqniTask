package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 08/08/17.
 */

public class LevelUpdateRequest extends StringRequest {
    private static final String LEVEL_UPDATE_REQUEST_URL = "http://www.vit5icnn2018.com/teqniHome/levelUpdate.php";
    private Map<String, String> params;

    public LevelUpdateRequest(String level1, String level2, String level3, String level1opt, String level2opt, String level3opt, Response.Listener<String> listener){
        super(Method.POST, LEVEL_UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("level1", level1);
        params.put("level2", level2);
        params.put("level3", level3);
        params.put("level1opt", level1opt);
        params.put("level2opt", level2opt);
        params.put("level3opt", level3opt);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
