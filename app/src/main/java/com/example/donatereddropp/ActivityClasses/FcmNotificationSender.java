package com.example.donatereddropp.ActivityClasses;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {
    String userFcToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;

    // this is post url and 2nd is firebase key
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "key=AAAAisdC7i0:APA91bG2ewGemWRSxDkh9-kFCTiE3d89ZS2Gc-lQU4sSeExL-tUwZMoTiqI5KBlzV1TWgI_e1t0jprWn9XycX4GMKW1lOaMf5wFqCV1Zx-464bHj21Fu3Ih5p9fOAwTjoqNeNXG_MLbs";

    // this is constructor for variables
    public FcmNotificationSender(String userFcToken, String title, String body, Context mContext) {
        this.userFcToken = userFcToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
    }

    public void SendNotifications(String senderId) {
        // this code is help to send the notifications
        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            Log.d("notirespo",userFcToken);
            mainObj.put("to", userFcToken);
            JSONObject notiObj = new JSONObject();
            notiObj.put("title", title);
            notiObj.put("body", body);
            notiObj.put("senderId", senderId); // Include the sender's ID in the notification payload
            notiObj.put("click_action", "OPEN_CHAT");

            mainObj.put("notification", notiObj);
            Log.d("notirespo", mainObj.toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("notirespoo", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("notirespo", error.getMessage()+error.getLocalizedMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", fcmServerKey);
                return header;
            }
        };

        requestQueue.add(request);
    }

    public void sendChatNotificationWithPendingIntent(String uid, String name) {
        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcToken);
            JSONObject notiObj = new JSONObject();

            notiObj.put("title", title);
            notiObj.put("body", body);
            notiObj.put("uid", uid);
            notiObj.put("name", name); // Include the name of the sender or receiver, as needed
            notiObj.put("type", 1);

            mainObj.put("data", notiObj);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("notirespo", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("notirespo", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", fcmServerKey);
                return header;
            }
        };

        requestQueue.add(request);



    }
    public void sendLikeNotificationWithPendingIntent(String uid,String name) {
        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcToken);
            JSONObject notiObj = new JSONObject();

            notiObj.put("title", title);
            notiObj.put("body", body);
            notiObj.put("uid", uid);
            notiObj.put("name", name);
            notiObj.put("type", 2);

            mainObj.put("data", notiObj);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("notirespo", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("notirespo", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", fcmServerKey);
                return header;
            }
        };

        requestQueue.add(request);


    }
    }
