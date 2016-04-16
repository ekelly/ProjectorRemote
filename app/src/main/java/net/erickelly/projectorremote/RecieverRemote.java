package net.erickelly.projectorremote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class RecieverRemote {
    RequestQueue mRequestQueue;
    private static final String mUrl = "http://192.168.1.99/YamahaRemoteControl/ctrl";
    private static final String mReceiverData = "<YAMAHA_AV cmd=\"PUT\"><Main_Zone>" +
            "<Power_Control><Power>%s</Power></Power_Control></Main_Zone></YAMAHA_AV>\nName\n";

    private static RecieverRemote sInstance;

    public static RecieverRemote getInstance(Context c) throws IOException {
        if (sInstance == null) {
            sInstance = new RecieverRemote(c);
        }
        return sInstance;
    }

    private RecieverRemote(Context c) throws IOException {
        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(c);
        mRequestQueue.start();
    }

    public void onStop() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                // do I have to cancel this?
                return true; // -> always yes
            }
        });
    }

    public void sendCommand(final ReceiverAction command, final ResultListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }) {

            @Override
            public byte[] getBody() {
                return getReceiverData(command).getBytes();
            }
        };
        mRequestQueue.add(request);
    }

    public interface ResultListener {
        void onSuccess();
        void onError(Exception error);
    }

    enum ReceiverAction {
        ON("On"), OFF("Standby");

        private String action;

        ReceiverAction(String action) {
            this.action = action;
        }

        @Override
        public String toString() {
            return action;
        }
    }

    private String getReceiverData(ReceiverAction action) {
        return String.format(mReceiverData, action.toString());
    }
}