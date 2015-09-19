package net.erickelly.projectorremote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class RemoteFragment extends Fragment {
    private ProjectorRemote mRemote;

    // Views
    Button mPowerButton;
    Button mMuteButton;

    public RemoteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            mRemote = ProjectorRemote.getInstance(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_remote, container, false);

        // Set onClickListeners
        mPowerButton = (Button) v.findViewById(R.id.power);
        mMuteButton = (Button) v.findViewById(R.id.mute);
        mPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(ProjectorCommand.Power.TOGGLE);
            }
        });
        mMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(ProjectorCommand.Mute.TOGGLE);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRemote.onStop();
    }

    private void sendCommand(ProjectorCommand.Power command) {
        mRemote.sendCommand(command, new ProjectorRemote.ResultListener() {
            @Override
            public void onSuccess(JSONObject response) {
                showToast("Success!");
            }

            @Override
            public void onError(Exception error) {
                error.printStackTrace();
                showToast("Error: " + error.getMessage());
            }
        });
    }

    private void sendCommand(ProjectorCommand.Mute command) {
        mRemote.sendCommand(command, new ProjectorRemote.ResultListener() {
            @Override
            public void onSuccess(JSONObject response) {
                showToast("Success!");
            }

            @Override
            public void onError(Exception error) {
                error.printStackTrace();
                showToast("Error: " + error.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
