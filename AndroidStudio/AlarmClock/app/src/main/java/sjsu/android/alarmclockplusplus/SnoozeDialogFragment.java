package sjsu.android.alarmclockplusplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class SnoozeDialogFragment extends DialogFragment {
    private View view;
    private Toast toast;
    private final int SEEK_MAX = 29;
    private final int SEEK_START = 4;
    private final int SEEK_MIN = 1;

    public static interface OnCompleteListener {
        public abstract void onComplete(int minutes);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public SnoozeDialogFragment(View view){
        super();
        this.view = view;
        toast = new Toast(view.getContext());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedinstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Set Snooze Duration");
        final SeekBar seekBar = new SeekBar(view.getContext());
        seekBar.setMax(SEEK_MAX);
        seekBar.setProgress(SEEK_START);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        seekBar.setLayoutParams(lp);
        int progress = seekBar.getProgress() + SEEK_MIN;
        toast = Toast.makeText(view.getContext(),progress + " minutes", Toast.LENGTH_SHORT);
        toast.show();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                toast.cancel();
                int progress = seekBar.getProgress() + SEEK_MIN;
                toast = Toast.makeText(view.getContext(),progress + " minutes", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onTimeSet(seekBar);
            }
        });
        builder.setView(seekBar);
        return builder.create();
    }

    public void onTimeSet(SeekBar seekBar){
        int progress = seekBar.getProgress() + SEEK_MIN;
        this.mListener.onComplete(progress);
    }
}
