package sjsu.android.alarmclockplusplus;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class SoundSelectorAdapter extends RecyclerView.Adapter<SoundSelectorAdapter.MyViewHolder>{
    private ArrayList<String> mDataset;
    private ArrayList<String> names;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private View view;
        private TextView textview;


        public MyViewHolder(View v) {
            super(v);
            view = v;
            textview = (TextView) v.findViewById(R.id.first_line);
        }
    }

    public SoundSelectorAdapter(ArrayList<String> myDataset, ArrayList<String> names) {
        this.mDataset = myDataset;
        this.names = names;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public SoundSelectorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sound_selector_row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textview.setText(names.get(position));
        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent();
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, mDataset.get(position));
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_NAME, names.get(position));
                Activity activity = (Activity) view.getContext();
                activity.setResult(RESULT_OK, updateIntent);
                activity.finish();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
