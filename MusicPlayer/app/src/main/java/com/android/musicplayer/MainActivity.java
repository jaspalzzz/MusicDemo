package com.android.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.android.musicplayer.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AudioListAdapter.AudioSelectListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private ArrayList<Audio> audioList = new ArrayList();
    private ActivityMainBinding binding;
    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer;
    //Used to pause/resume MediaPlayer
    private int resumePosition;
    private boolean isPlayingSong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Data Binding Implementation*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /*init media player first so that we can play songs on list read*/
        initMediaPlayer();

        /*Permission checking for the external storage*/
        if (PermissionUtils.hasPermission(this, PermissionUtils.EX_STORAGE_PERMISSION)) {
            /*Loading songs from the external storage*/
            loadAudioFromStorage();
        } else {
            PermissionUtils.requestPermissions(this, PermissionUtils.EX_STORAGE_PERMISSION, PermissionUtils.EXTERNAL_STORAGE_REQUEST_CODE);
        }

        setListener();
    }

    /*
     * Generating list from the songs has been collected from external storage
     * */
    private void loadAudioFromStorage() {
        audioList = AudioUtils.loadAudio(this);
        createAudioList();
    }

    private void createAudioList() {
        audioListAdapter = new AudioListAdapter();
        audioListAdapter.setData(audioList);
        audioListAdapter.setAudioSelectListener(this);
        binding.audioList.setAdapter(audioListAdapter);
    }


    /***
     * When ever user tap on the any song
     * @param position index of the song in the arraylist
     * @param path path of th song on external storage that we will pass to media player class
     */
    @Override
    public void audioSelected(int position, String path) {
        playSong(path);
        binding.songTitle.setText(audioList.get(position).getTitle());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAudioFromStorage();
            } else {
                Toast.makeText(this, "Please allow external storage permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void setListener() {
        binding.plyaPauseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayingSong) {
                    pauseMedia();
                    isPlayingSong =false;
                    binding.setIsPlaying(false);
                } else {
                    playMedia();
                    isPlayingSong =true;
                    binding.setIsPlaying(true);
                }
            }
        });
    }

    private void playSong(String songPath) {
        mediaPlayer.reset();
        try {
            // Set the data source to the mediaFile location
            mediaPlayer.setDataSource(songPath);
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.stop();
        }
        mediaPlayer.prepareAsync();
    }

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }


    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        isPlayingSong = true;
        binding.setIsPlaying(true);
    }
}
