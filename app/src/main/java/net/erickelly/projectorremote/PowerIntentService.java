package net.erickelly.projectorremote;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

public class PowerIntentService extends IntentService {
    private ProjectorRemote mRemote;

    public PowerIntentService() {
        super(PowerIntentService.class.getSimpleName());
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

    private void toast(String message) {
        Toast t = Toast.makeText(PowerIntentService.this,
                message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mRemote.sendCommand(ProjectorCommand.Power.TOGGLE, new ProjectorRemote.ResultListener() {
            @Override
            public void onSuccess(JSONObject response) {
                toast("Success!");
            }

            @Override
            public void onError(Exception error) {
                toast("Error: " + error.getMessage());
            }
        });
    }
}
