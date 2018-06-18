/**
 * THIS CODE WAS DEVELOP BY SAMUEL ADAKOLE
 * Created by CyberTech on 6/4/2017.
 * CALL THIS Phone NO: 07038620440 FOR CONSULTANCY.
 */
package com.samtech.smartprint;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class functions {

    // static Context context;
    private static SharedPreferences sp;
    private static ConnectionRequest request;
    private static String phoneNO;
    // private static String msg = null;
    private static Dialog progressDialog;
    //  private static String raccountno;
    // private static String amount;
    private static boolean status = false;
    private static String msg;

    public static void showmsg(Context context, String title, String Msg, DialogInterface.OnCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setOnCancelListener(listener);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.show();

    }

    public static void showClose(final Context context, String title, String Msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                context.startActivity(new Intent(context, Home.class));
            }
        });
        builder.setNegativeButton("Close", listener);
        builder.setMessage(Msg);
        builder.show();

    }

    public static void NetworkAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setNegativeButton("No", null);
        DialogInterface.OnClickListener onyes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        };
        builder.setPositiveButton("Yes", onyes);
        builder.setTitle("Connectivity Alert");
        builder.setMessage("Network Connectivity is Off\n do you want to switch to data? cost may Apply");
        builder.show();

    }

    public static void startConnection(final Context context, Bundle data) {
        boolean isNetworkOn = functions.checkConnection(context);
        if (isNetworkOn) {
            String raccountno = data.getString("raccountno");
            String amount = data.getString("amount");
            String pin = functions.getPref(context, "pin");
            String account = functions.getPref(context, "accountno");
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    // queue.cancelAll("transfer");
                    try {
                        JSONObject object = new JSONObject(response);
                        boolean success = object.getBoolean("success");
                        msg = object.getString("message");
                        DialogInterface.OnClickListener Success = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, Home.class));
                            }
                        };
                        if (success) {

                            functions.showClose(context, "Transaction Status", msg, Success);

                        } else {
                            functions.showClose(context, "Transaction Status", msg, Success);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            DialogInterface.OnCancelListener oncancel = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    DialogInterface.OnCancelListener close = new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            context.startActivity(new Intent(context, MakeTransfer.class));
                        }
                    };
                    functions.showmsg(context, "Transaction Status", "Transaction Interupted!", close);
                }
            };
            progressDialog = ProgressDialog.show(context, null, "Transaction in Progress...", true, true, oncancel);
            ConnectionRequest request = new ConnectionRequest(account, raccountno, amount, pin, listener);
            request.setTag("transfer");
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        } else {
            functions.NetworkAlert(context);
        }

    }

    public static void showmsg(Context context, String Msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Registration Status");
        builder.setMessage(Msg);
        builder.setNegativeButton("Ok", listener);
        builder.show();

    }

    public static boolean insert(Context context, String key, String object) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, object);
        return editor.commit();
    }

    public static String getPref(Context context, String key) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }

    public static void generateOTP(Context context) {
        // Java code to explain how to generate OTP
        // Using numeric values
        String numbers = "0123456789";
        int len = 6;
        // Using random method
        Random rndm_method = new SecureRandom();
        char[] otp = new char[len];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
            stringBuilder.append(otp[i]);
        }

        insert(context, "otp", stringBuilder.toString());
    }

    public static String codeMD5(String str) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //
    public static void sendOTP(final Context context, final Bundle data) {
        functions.generateOTP(context);
        String otp = getPref(context, "otp");
        phoneNO = data.getString("phoneNO");
        Response.Listener<String> otplistener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.contains("OK")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    DialogInterface.OnClickListener diaListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, Activate.class);
                            intent.putExtra("data", data);
                            context.startActivity(intent);

                        }
                    };
                    builder.setNegativeButton("OK", diaListener);
                    builder.setCancelable(false);
                    builder.setTitle("Registration Status");
                    builder.setMessage("Please use the OTP sent to your Phone for activation.");
                    builder.show();
                } else {
                    functions.showmsg(context, response, null);
                }
            }
        };
        DialogInterface.OnCancelListener oncansel = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                functions.showmsg(context, null, "Process canceled please again!", null);
            }
        };
        progressDialog = ProgressDialog.show(context, null, "Sending Activation Code Please wait...", true, true, oncansel);
        request = new ConnectionRequest(otp, phoneNO, otplistener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static boolean insert(String accountNO, String username, String phoneNO, String fname, String surname) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("accountno", accountNO);
        editor.putString("username", username);
        editor.putString("phoneno", phoneNO);
        editor.putString("fname", fname);
        editor.putString("surname", surname);
        return editor.commit();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deletDIR(dir);
        } catch (Exception e) {
            Log.i("MYTAG", "Exception on deleteCache : " + e.getMessage());
        }

    }

    private static boolean deletDIR(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deletDIR(new File(dir, children[i]));
                if (success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void buyAirtime(final Context context, Bundle data) {
        boolean isNetworkOn = functions.checkConnection(context);
        if (isNetworkOn) {
            String accountNo = data.getString("accountNo", "");
            final String amount = data.getString("amount", "");
            String pin = data.getString("pin", "");
            final String phoneNo = data.getString("phoneNo", "");
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String msg = jsonObject.getString("message");
                        if (success) {
                            DialogInterface.OnCancelListener onClose = new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    context.startActivity(new Intent(context, Home.class));
                                }
                            };
                            functions.showmsg(context, "Transaction Status", msg + " \n" + phoneNo + " has been loaded with " + amount, onClose);
                        } else {
                            msg = jsonObject.getString("error");
                            functions.showmsg(context, "Transaction Status", msg, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            DialogInterface.OnCancelListener onCancel = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    functions.showmsg(context, "Transaction Status", "Transaction interrupted", null);
                }
            };
            progressDialog = ProgressDialog.show(context, null, "Transaction in progress...", true, true, onCancel);
            AirtimeConnection request = new AirtimeConnection(accountNo, phoneNo, amount, pin, listener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        } else {
            functions.NetworkAlert(context);
        }

    }

    public static boolean sendOTPGen(final Context context, final Bundle data, final String key) {
        boolean isNetworkOn = functions.checkConnection(context);
        if (isNetworkOn) {
            functions.generateOTP(context);
            String otp = getPref(context, "otp");
            phoneNO = getPref(context, "phoneno");
            Response.Listener<String> otplistener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if (response.contains("OK")) {
                        status = true;
                        DialogInterface.OnCancelListener openOTP = new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Intent intent = new Intent(context, Otp.class);
                                intent.putExtra("key", key);
                                intent.putExtra("data", data);
                                context.startActivity(intent);
                            }
                        };
                        functions.showmsg(context, "Status", "Please use the OTP sent to your Registered Phone to confirm Your Transaction.", openOTP);
                    } else {
                        functions.showmsg(context, "Status", response, null);
                    }
                }

            };
            DialogInterface.OnCancelListener oncancel = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    functions.showmsg(context, "Transaction Status", "Transaction Interupted!", null);
                }
            };
            progressDialog = ProgressDialog.show(context, "Transaction Status", "Transaction in Progress...", true, true, oncancel);
            //request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request = new ConnectionRequest(otp, phoneNO, otplistener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        } else {
            functions.NetworkAlert(context);
        }
        return status;
    }

    public static void checkFingerPrint(Context context, Intent pinIntent, Intent FingerPrint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fm = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            } else {
                if (!fm.isHardwareDetected()) {
                    context.startActivity(pinIntent);
                } else {
                    context.startActivity(FingerPrint);
                }
            }
        } else {
            context.startActivity(pinIntent);
        }
    }

    public static void payments(final Context context, Bundle data) {
        String account = data.getString("account");
        final String amount = data.getString("amount");
        String pin = data.getString("pin");
        final String item = data.getString("item");
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    String msg = object.getString("message");
                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Transaction Status");
                        builder.setMessage("Transaction successful \n " + amount + " has been paid for " + item + "\n Thank you.");
                        builder.setCancelable(false);
                        DialogInterface.OnClickListener loadHome = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, Home.class));
                            }
                        };
                        builder.setNegativeButton("OK", loadHome);
                        builder.show();

                    } else {
                        DialogInterface.OnCancelListener onCancel = new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                context.startActivity(new Intent(context, Home.class));
                            }
                        };
                        functions.showmsg(context, "Transaction Status", msg, onCancel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DialogInterface.OnCancelListener onCancelpro = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                context.startActivity(new Intent(context, Home.class));
            }
        };
        progressDialog = ProgressDialog.show(context, "Transaction Status", "Transaction in Progress...", true, true, onCancelpro);
        AirtimeConnection connection = new AirtimeConnection(account, amount, pin, listener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(connection);
    }

    public static void removePrefs(Context context, String key) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }

    public boolean removeData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("accountno");
        editor.remove("username");
        editor.remove("phoneno");
        editor.remove("fname");
        editor.remove("surname");
        return editor.commit();
    }


}