package dell.example.jbplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import dell.example.jbplayer.adapter.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    //ListView songListView;
    RecyclerView recyclerView;
    String[] items;
    Button playerButton;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerButton = findViewById(R.id.playerButtonId);

//        playerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(count == 1) {
//                    Intent intent = new Intent(MainActivity.this, palyerActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        //songListView = findViewById(R.id.SongListId);


        recyclerView = findViewById(R.id.RecyclerViewId);
        runTimePermission();
    }

    public void runTimePermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        assert files != null;
        for(File singleFile:files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    public void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++)
        {

            items[i] = mySongs.get(i).getName().replace(".mp3","").replace(".wav","");
        }

//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,R.layout.list_design_view,R.id.textViewId,items);
//        songListView.setAdapter(myAdapter);
//
//        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String songName = songListView.getItemAtPosition(i).toString();
//                startActivity(new Intent(getApplicationContext(),palyerActivity.class).putExtra("songs",mySongs).putExtra("songname",songName).putExtra("position",i));
//                count=1;
//            }
//        });

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter.setItemClickListener(new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClickListener(int position, View v) {
                String songName = items[position];
                startActivity(new Intent(getApplicationContext(),palyerActivity.class).putExtra("songs",mySongs).putExtra("songname",songName).putExtra("position",position));
                count=1;
            }

            @Override
            public void onItemLongClickListener(int position, View v) {

            }
        });

    }
}
