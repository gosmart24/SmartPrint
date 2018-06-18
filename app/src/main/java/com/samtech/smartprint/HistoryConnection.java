package com.samtech.smartprint;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * THIS CODE WAS DEVELOP BY SAMUEL ADAKOLE
 * Created by CyberTech on 6/4/2017.
 * CALL THIS Phone NO: 07038620440 FOR CONSULTANCY.
 */
public class HistoryConnection extends StringRequest {
    private static final String History_URL = "https://dmangsok.000webhostapp.com/history.php";
    Map<String, String> params;

    public HistoryConnection(String accountno, Response.Listener<String> listener) {
        super(Method.POST, History_URL, listener, null);
        params = new HashMap<>();
        params.put("accountno", accountno);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
