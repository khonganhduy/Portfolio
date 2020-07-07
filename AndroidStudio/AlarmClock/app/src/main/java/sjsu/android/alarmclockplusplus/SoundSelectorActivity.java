package sjsu.android.alarmclockplusplus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SoundSelectorActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> myDataset;
    private ArrayList<String> names;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkTheme);
        setContentView(R.layout.activity_sound_selector);

        if (!checkPermission()){
            requestPermission();
        }
        else {
            fillMusicList();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(
                SoundSelectorActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (checkPermission()){
            fillMusicList();
        }
        else {
            Toast.makeText(SoundSelectorActivity.this, "You do not have permission to access storage.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void fillMusicList(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = this.getContentResolver().query(uri, null, selection, null, sortOrder);
        int count = 0;

        if(cur != null) {
            myDataset = new ArrayList<String>();
            names = new ArrayList<String>();
            count = cur.getCount();
            if(count > 0) {
                while(cur.moveToNext()) {
                    myDataset.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    // Add code to get more column here
                    names.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    // Save to your list here
                }
            }
            cur.close();

            recyclerView = (RecyclerView) findViewById(R.id.sound_selector_recycler);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            // specify an adapter (see also next example
            mAdapter = new SoundSelectorAdapter(myDataset, names);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(mAdapter);
        }
    }
}
