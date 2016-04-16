package net.erickelly.projectorremote;

import android.app.Activity;
import android.os.Bundle;

public class PowerButtonDialogActivity extends Activity {
    PowerButtonDialogFragment dialogFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(true);
        dialogFragment = new PowerButtonDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogFragment.show(getFragmentManager(), "PowerDialog");
    }
}
