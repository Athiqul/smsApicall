package com.example.smsbackgroundapicall;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                String format = bundle.getString("format");
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = createSmsMessage(pdu, format);
                    if (smsMessage != null) {
                        String senderNumber = smsMessage.getDisplayOriginatingAddress();
                        String messageBody = smsMessage.getDisplayMessageBody();
                        String messageCenterNumber = smsMessage.getServiceCenterAddress();
                        //Api request post method

                        String url = "https://smstesting.abovedevteam.cyou//message";
                        Log.e("Textmsg", "onReceive: "+messageBody+senderNumber+messageCenterNumber);
                         Intent directIntent=new Intent(context,MainActivity.class);
                         directIntent.putExtra(MainActivity.EXTRA_SMS_BODY,messageBody);
                         directIntent.putExtra(MainActivity.EXTRA_SMS_CENTER,messageCenterNumber);
                         directIntent.putExtra(MainActivity.EXTRA_SMS_SENDER,senderNumber);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, directIntent, PendingIntent.FLAG_MUTABLE);

                        try {
                            pendingIntent.send();
                            Log.e("Textmsg","Intent send");
                        } catch (PendingIntent.CanceledException e) {
                            Log.d("SMS", "onReceive: failed to catch sms" );
                            throw new RuntimeException(e);
                        }
                        // creating a new variable for our request queue

                        // on below line we are calling a string
                        // request method to post the data to our API
                        // in this we are calling a post method.


                    }
                }
            }
        }
    }

    private SmsMessage createSmsMessage(Object pdu, String format) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return SmsMessage.createFromPdu((byte[]) pdu, format);
        } else {
            return SmsMessage.createFromPdu((byte[]) pdu);
        }
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
