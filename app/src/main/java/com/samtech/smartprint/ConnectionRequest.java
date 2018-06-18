package com.samtech.smartprint;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * /**
 * THIS CODE WAS DEVELOP BY SAMUEL ADAKOLE
 * Created by CyberTech on 6/4/2017.
 * CALL THIS Phone NO: 07038620440 FOR CONSULTANCY.
 * <p/>
 * , url = "gosmart.comze.com/kingsbank/register.php"
 * "http://192.168.43.86/kingsbank/register.php"
 * "https://cybersmart.000webhostapp.com/kingsbank/"
 */
public class ConnectionRequest extends StringRequest {
    private static final String REG_URL = "https://dmangsok.000webhostapp.com/register.php";
    private static final String OTP_URL = "http://api.smartsmssolutions.com/smsapi.php";
    private static final String TRANSFER_URL = "https://dmangsok.000webhostapp.com/transfer.php";
    private static final String Balance_URL = "https://dmangsok.000webhostapp.com/balance.php";
    //private static final String Balance_URL = "http://192.168.43.86/test/balance.php";
    Map params;

    // Transfer constructor.
    public ConnectionRequest(String accountNO, String raccountno, String amount, String pin, Response.Listener<String> listener) {
        super(Method.POST, TRANSFER_URL, listener, null);
        params = new HashMap();
        params.put("accountno", accountNO);
        params.put("raccountno", raccountno);
        params.put("amount", amount);
        params.put("pin", pin);

    }

    // send otp to API server  constructor.
    public ConnectionRequest(String otp, String phoneNO, Response.Listener<String> listener) {
        super(Method.POST, OTP_URL, listener, null);
        String username = "dcgnifes";
        String password = "dcgnifes";
        String sender = "KingsMobile";
        params = new HashMap();
        params.put("username", username);
        params.put("password", password);
        params.put("message", otp);
        params.put("sender", sender);
        params.put("recipient", phoneNO);

    }

    //  constructor for Registration.
    public ConnectionRequest(String accountNo, String username, String email, String phoneNo, String pin, String loginPass, String deviceSerial, Response.Listener<String> listener) {
        super(Method.POST, REG_URL, listener, null);
        params = new HashMap();
        params.put("accountno", accountNo);
        params.put("phoneno", phoneNo);
        params.put("email", email);
        params.put("username", username);
        params.put("pin", pin);
        params.put("loginpass", loginPass);
        params.put("deviceserial", deviceSerial);
    }

    // for checking Balance.
    public ConnectionRequest(String accountNo, Response.Listener<String> listener) {
        super(Method.POST, Balance_URL, listener, null);
        params = new HashMap();
        params.put("accountno", accountNo);
    }

    public Map getParams() {
        return params;
    }
}
