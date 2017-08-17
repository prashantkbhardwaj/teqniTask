package com.example.prashantbhardwaj.teqnitask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashantbhardwaj on 17/08/17.
 */

public class EditPostRequest extends StringRequest {
    private Map<String, String> params;

    public EditPostRequest(String postid, String name, String timeDuration, Response.Listener<String> listener){
        super(Method.POST, Config.EDIT_POST_URL, listener, null);
        params = new HashMap<>();
        params.put("postid", postid);
        params.put("name", name);
        params.put("timeDuration", timeDuration);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
