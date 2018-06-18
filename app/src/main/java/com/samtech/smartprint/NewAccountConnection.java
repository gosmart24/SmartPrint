package com.samtech.smartprint;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CyberTech on 6/4/2013.
 * stagent24@gmail.com
 * https://dmangsok.000webhostapp.com/createaccount.php"
 * http://192.168.43.86/kingsbank/
 */
public class NewAccountConnection extends StringRequest {
    private static final String NewAccount_URL = "https://dmangsok.000webhostapp.com/createaccount.php";
    private static final String Login_URL = "https://dmangsok.000webhostapp.com/login.php";
    Map<String, String> params;

    public NewAccountConnection(String surname, String fname, String othername, String DOB, String Address, String phone, Response.Listener<String> listener) {
        super(Method.POST, NewAccount_URL, listener, null);
        params = new HashMap<>();
        params.put("surname", surname);
        params.put("fname", fname);
        params.put("othername", othername);
        params.put("dob", DOB);
        params.put("address", Address);
        params.put("phoneno", phone);
    }


    public NewAccountConnection(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, Login_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
