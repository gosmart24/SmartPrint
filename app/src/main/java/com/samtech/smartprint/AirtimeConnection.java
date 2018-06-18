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
public class AirtimeConnection extends StringRequest {
    private static final String Airtime_URL = "https://dmangsok.000webhostapp.com/buyairtime.php";
    private static final String BILL_URL ="https://dmangsok.000webhostapp.com/billpayment.php";

    Map<String, String> params;

    // for AirtimeConnection Topup.
    public AirtimeConnection(String accountNo, String phoneNo, String amount, String pin, Response.Listener<String> listener) {
        super(Method.POST, Airtime_URL, listener, null);
        params = new HashMap();
        params.put("accountno", accountNo);
        params.put("phoneno", phoneNo);
        params.put("amount", amount);
        params.put("pin", pin);
    }

    public AirtimeConnection(String accountNo, String amount, String pin, Response.Listener<String> listener) {
        super(Method.POST, BILL_URL, listener, null);
        params = new HashMap();
        params.put("accountno", accountNo);
        params.put("amount", amount);
        params.put("pin", pin);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
