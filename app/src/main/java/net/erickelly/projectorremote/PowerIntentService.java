package net.erickelly.projectorremote;

import android.app.IntentService;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class PowerIntentService extends IntentService {
    private ProjectorRemote mProjectorRemote;
    private RecieverRemote mReceiverRemote;

    public static final String POWER = "power";

    public PowerIntentService() {
        super(PowerIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mProjectorRemote = ProjectorRemote.getInstance(this);
            mReceiverRemote = RecieverRemote.getInstance(this);
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
        ProjectorCommand.Command c = null;
        RecieverRemote.ReceiverAction receiverAction = RecieverRemote.ReceiverAction.ON;
        if (intent.getAction().equals(POWER)) {
            String action = intent.getStringExtra(POWER);
            c = ProjectorCommand.Power.valueOf(action);
            if (c == ProjectorCommand.Power.OFF) {
                receiverAction = RecieverRemote.ReceiverAction.OFF;
            }
        }

        final AtomicBoolean noErrors = new AtomicBoolean(true);
        final CountDownLatch latch = new CountDownLatch(2);
        mProjectorRemote.sendCommand(c, new ProjectorRemote.ResultListener() {
            @Override
            public void onSuccess(JSONObject response) {
                latch.countDown();
            }

            @Override
            public void onError(Exception error) {
                if (error instanceof UnknownHostException) {
                    toast("Error: Could not find server");
                } else {
                    toast("Error: " + error.getMessage());
                }
                noErrors.set(false);
                latch.countDown();
            }
        });
        mReceiverRemote.sendCommand(receiverAction, new RecieverRemote.ResultListener() {
            @Override
            public void onSuccess() {
                latch.countDown();
            }

            @Override
            public void onError(Exception error) {
                if (error instanceof UnknownHostException) {
                    toast("Error: Could not find server");
                } else {
                    toast("Error: " + error.getMessage());
                }
                noErrors.set(false);
                latch.countDown();
            }
        });

        try {
            latch.await();
            if (noErrors.get()) {
                toast("Success!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
