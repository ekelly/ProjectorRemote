package net.erickelly.projectorremote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class PowerButtonDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.on, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        triggerPower(ProjectorCommand.Power.ON);
                    }
                })
                .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.off, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        triggerPower(ProjectorCommand.Power.OFF);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void triggerPower(ProjectorCommand.Command c) {
        Intent i = new Intent(getContext(), PowerIntentService.class);
        i.setAction(PowerIntentService.POWER);
        i.putExtra(PowerIntentService.POWER, c.toString());
        getContext().startService(i);
    }
}
