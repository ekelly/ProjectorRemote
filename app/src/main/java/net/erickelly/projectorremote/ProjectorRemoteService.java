package net.erickelly.projectorremote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

public class ProjectorRemoteService extends Service {
    private ProjectorRemote mRemote;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mRemote = ProjectorRemote.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "started");
        mRemote.sendCommand(ProjectorCommand.Power.TOGGLE, new ProjectorRemote.ResultListener() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public void onError(Exception error) {
                Toast t =Toast.makeText(ProjectorRemoteService.this,
                        "Error: " + error.getMessage(), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM, 0, 0);
                t.show();
            }
        });
        return START_NOT_STICKY;
    }
}
