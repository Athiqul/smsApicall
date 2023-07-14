package com.example.smsbackgroundapicall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
public  static  final String EXTRA_SMS_SENDER="extra_sms_sender";
    public  static  final String EXTRA_SMS_BODY="extra_sms_body";
    public  static  final String EXTRA_SMS_CENTER="extra_sms_center";

    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.e("Textmsg", "onNewIntent: Intent triggerd");
        if(!intent.hasExtra(EXTRA_SMS_SENDER) && !intent.hasExtra(EXTRA_SMS_BODY))
        {
            return;
        }

        String senderNumber=intent.getExtras().getString(EXTRA_SMS_SENDER);
        String messageBody=intent.getExtras().getString(EXTRA_SMS_BODY);
        String messageCenterNumber=intent.getExtras().getString(EXTRA_SMS_CENTER);
        Log.e("Textmsg", "Sender Number: " +senderNumber+" body: "+ messageBody+ " center:" +messageCenterNumber);
          //Data fetching And api calling
        postDataUsingVolley(senderNumber,messageBody,messageCenterNumber);
    }


    //request send
    private void postDataUsingVolley(String senderNumber,String messageBody,String messageCenterNumber) {
        // url to post our data
        String url = "https://smstesting.abovedevteam.cyou//message";


        // creating a new variable for our request queue

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, response ->
                Log.d("ApiResponse", "postDataUsingVolley: Success"), error -> Log.d("ApiResponse", "postDataUsingVolley: Error") ){
            //Add Parameters
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("body",messageBody);
                params.put("center_number",messageCenterNumber);
                params.put("from_number",senderNumber);
                return params;
            }
        };
         requestQueue = Volley.newRequestQueue(MainActivity.this);


        requestQueue.add(request);

    }
}