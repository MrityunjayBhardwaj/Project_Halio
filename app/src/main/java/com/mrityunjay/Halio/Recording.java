//package codingwithmitch.com.tabiancustomcamera;
package com.mrityunjay.Halio;

import android.util.Log;
import static com.mrityunjay.Halio.Camera2Fragment.audioUri;
import static com.mrityunjay.Halio.Camera2Fragment.is_Start;

public class Recording {

    String Uri, fileName;
    boolean isPlaying = false;


    public Recording(String uri, String fileName, boolean isPlaying) {
        Uri = uri;
        this.fileName = fileName;
        this.isPlaying = isPlaying;
    }

    public String getUri() {
        return Uri;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing){
        this.isPlaying = playing;
        Log.d("Recording","start playing"+ this.fileName);

        /* Selecting this audio file*/
        is_Start = true;
        audioUri = this.Uri;
    }
}