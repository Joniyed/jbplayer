
package dell.example.jbplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class palyerActivity extends AppCompatActivity {

    Button btn_next,btn_pause,btn_prev,songList;
    TextView songName;
    SeekBar songSeekBar;

    static MediaPlayer mediaPlayer;
    int position,currentPosition=0;
    ArrayList<File> mySongs;
    Thread updadeSeekBar;
    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palyer);

        btn_next = findViewById(R.id.nextId);
        btn_pause = findViewById(R.id.pauseId);
        btn_prev = findViewById(R.id.prevId);
        songList = findViewById(R.id.ListButtonId);
        songName = findViewById(R.id.songNameId);
        songSeekBar = findViewById(R.id.seekBarId);


        songList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updadeSeekBar = new Thread(){
            @Override
            public void run() {

                int totalDuration = mediaPlayer.getDuration();

                while(currentPosition<totalDuration)
                {
                    try{
                        Thread.sleep(100);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        };

        try {

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            Intent i = getIntent();
            Bundle bundle = i.getExtras();

            mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
            sname = mySongs.get(position).getName().toString();

            final String songname = i.getStringExtra("songname");

            songName.setText(songname);
            songName.setSelected(true);

            position = bundle.getInt("position", 0);

            Uri u = Uri.parse(mySongs.get(position).toString());

            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);

            mediaPlayer.start();
            songSeekBar.setMax(mediaPlayer.getDuration());

            updadeSeekBar.start();

            songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            });

            btn_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        songSeekBar.setMax(mediaPlayer.getDuration());
                        if (mediaPlayer.isPlaying()) {
                            btn_pause.setBackgroundResource(R.drawable.play_icon);
                            mediaPlayer.pause();

                        } else {
                            btn_pause.setBackgroundResource(R.drawable.pasue_icon);
                            mediaPlayer.start();
                        }
                    }catch (Exception e){
                        songName.setText(R.string.Error);
                        btn_pause.setBackgroundResource(R.drawable.play_icon);
                        Toast.makeText(palyerActivity.this, "Select a song first..", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();

                        position = ((position + 1) % mySongs.size());

                        Uri u = Uri.parse(mySongs.get(position).toString());

                        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                        sname = mySongs.get(position).getName();
                        songName.setText(sname);
                        btn_pause.setBackgroundResource(R.drawable.pasue_icon);
                        mediaPlayer.start();
                    }catch (Exception e){
                        songName.setText(R.string.Error);
                        btn_pause.setBackgroundResource(R.drawable.play_icon);
                        Toast.makeText(palyerActivity.this, "Error select from list..", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        mediaPlayer.stop();
                        mediaPlayer.release();

                        position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);

                        Uri u = Uri.parse(mySongs.get(position).toString());

                        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                        sname = mySongs.get(position).getName();
                        songName.setText(sname);

                        btn_pause.setBackgroundResource(R.drawable.pasue_icon);
                        mediaPlayer.start();
                    }catch(Exception e){
                        songName.setText(R.string.Error);
                        btn_pause.setBackgroundResource(R.drawable.play_icon);
                        Toast.makeText(palyerActivity.this, "Error select from list", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error ,,,, File is not playing try another one", Toast.LENGTH_SHORT).show();
        }
    }
}
