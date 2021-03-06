package com.imagenproactiva.irplugin;

// kludgy imports to support 2.9 and 3.0 due to package changes
import org.apache.cordova.*;
// import org.apache.cordova.CordovaArgs;
// import org.apache.cordova.CordovaPlugin;
// import org.apache.cordova.CallbackContext;
// import org.apache.cordova.PluginResult;
// import org.apache.cordova.LOG;

import android.hardware.ConsumerIrManager;

import org.apache.cordova.CallbackContext;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.String;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Runnable;

public class Ir extends CordovaPlugin {
    public static final String ACTION_TRANSMIT_IR_CODE = "transmit";
    
    private static final String TAG = "IRtransmit";


    @Override
    public boolean execute(String action, JSONArray jsonArgs, final CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "action = " + action);
        try {
            if (ACTION_TRANSMIT_IR_CODE.equals(action)) {
                JSONObject args = jsonArgs.getJSONObject(0);
                final Integer frequency = args.getInt("frequency");
                JSONArray signalJson = args.getJSONArray("signal");
                final int[] signal = new int[signalJson.length()];
                for (int i = 0; i < signalJson.length(); ++i) {
                    signal[i] = signalJson.optInt(i);
                }
                final Context context = this.cordova.getActivity().getApplicationContext();
                this.cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                        ConsumerIrManager irService = (ConsumerIrManager)context.getSystemService(context.CONSUMER_IR_SERVICE);
                        // transmit the pattern at 38.4KHz
                        Log.d(TAG, "ir blink");
                        irService.transmit(frequency, signal);
                        callbackContext.success("IR Code Transmitted");
                        
                    }
                });
            }
            return true;

        } catch (
                Exception e
                
                )

        {
            callbackContext.error("ivalid action");
            
            return false;
        }

    }
}


/*
 if (ACTION_TRANSMIT_IR_CODE.equals(action)) {

                JSONObject args = jsonArgs.getJSONObject(0);
                final Integer frequency = args.getInt("frequency");
                String strSignal = args.getString("signal");
                final int[] signal;

                String[] items = strSignal.replaceAll("\\[", "").replaceAll("\\]", "").split(",");

                signal = new int[items.length];

                for (int i = 0; i < items.length; i++) {
                    try {
                        signal[i] = Integer.parseInt(items[i]);
                    } catch (NumberFormatException nfe) {
                    }
                    ;
                }


                final Context context = this.cordova.getActivity().getApplicationContext();
                this.cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                        callbackContext.success("0");
                        ConsumerIrManager irService = (ConsumerIrManager) context.getSystemService(context.CONSUMER_IR_SERVICE);

                        Log.d("transmit", "lets go");

                        /*if (Build.VERSION.SDK_INT == ANDROID_KITKAT_SDK) {
                            intlastIdx = Build.VERSION.RELEASE.lastIndexOf(".");
                            int VERSION_MR = Integer.valueOf(Build.VERSION.RELEASE.substring(lastIdx + 1));
                            if (VERSION_MR < 3) {
                                // Before version of Android 4.4.2
                                irService.transmit(frequency, signal);
                            } else {
                                // Later version of Android 4.4.3

        irService.transmit(frequency, signal);
        //   }
        callbackContext.success("1");
        }

        });
 }else{
                callbackContext.error("ivalid action");
                return false;
            }
 */
