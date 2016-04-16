package net.erickelly.projectorremote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ProjectorRemote {
    RequestQueue mRequestQueue;
    private static final String mUrl = "http://eagle-5:8080";
    private static final String mRecieverUrl = "http://192.168.1.99/YamahaRemoteControl/ctrl";
    private static final String mReceiverData = "<YAMAHA_AV cmd=\"PUT\"><Main_Zone>" +
            "<Power_Control><Power>%s</Power></Power_Control></Main_Zone></YAMAHA_AV>\nName\n";

    private static ProjectorRemote sInstance;

    public static ProjectorRemote getInstance(Context c) throws IOException {
        if (sInstance == null) {
            sInstance = new ProjectorRemote(c);
        }
        return sInstance;
    }

    private ProjectorRemote(Context c) throws IOException {
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

    public void sendCommand(final ProjectorCommand.Command command, final ResultListener listener) {
        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    mUrl + command.endpoint(), createCommandRequest(command),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError(error);
                }
            });
            mRequestQueue.add(jsonRequest);
        } catch (JSONException e) {
            listener.onError(e);
        }
    }

    private JSONObject createCommandRequest(ProjectorCommand.Command command) throws JSONException {
        JSONObject request = new JSONObject();
        request.put("payload", command.toString());
        return request;
    }

    public interface ResultListener {
        void onSuccess(JSONObject response);
        void onError(Exception error);
    }

}