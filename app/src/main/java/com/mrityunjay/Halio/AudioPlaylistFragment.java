package com.mrityunjay.Halio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import mrityunjay.com.Halio.R;

public class AudioPlaylistFragment extends Fragment {
    public static AudioPlaylistFragment newInstance(){
        AudioPlaylistFragment fragment = new AudioPlaylistFragment();
        return fragment;
    }

    public RecyclerView recyclerViewRecordings;
    public ArrayList<Recording> recordingArraylist;
    public RecordingAdapter recordingAdapter;
    public TextView textViewNoRecordings;
    public int permissiongranted = 0;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.music_list,container,false);

        /* Init View */
        recordingArraylist = new ArrayList<Recording>();
        recyclerViewRecordings = (RecyclerView) view.findViewById(R.id.recyclerViewRecordings);
        recyclerViewRecordings.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewRecordings.setHasFixedSize(true);

        textViewNoRecordings = (TextView) view.findViewById(R.id.textViewNoRecordings);

        Log.d("RListAct","inside onCreateView");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("RListAct","calling getpermissionTORecordAudio()");
                getPermissionToRecordAudio();

            }

        if(permissiongranted ==0){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();


        }

        fetchRecordings();

//        if(RecorderAudioActivity.RECORD_AUDIO_REQUEST_CODE != 123){
//            fetchRecordings();
//        }
//        else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                getPermissionToRecordAudio();
//            }
//        }


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RecorderAudioActivity.RECORD_AUDIO_REQUEST_CODE);

        }
        else{

//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                ft.setReorderingAllowed(false);
//            }
//            ft.detach(this).attach(this).commit();

        }
    }


    public void fetchRecordings() {
        Log.i("RListAct","inside fetchRecordings");

        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/Halio/Audios";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();

        Log.d("Files", "file obj: "+ files);

        if( files!=null ){
            permissiongranted =1;
            for (int i = 0; i < files.length; i++) {

                Log.d("Files", "FileName:" + files[i].getName());
                String fileName = files[i].getName();
                String recordingUri = root.getAbsolutePath() + "/Halio/Audios/" + fileName;

                Recording recording = new Recording(recordingUri,fileName,false);
                Log.d("Files", "recording: "+ recording);

                try{
                    recordingArraylist.add(recording);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
            setAdaptertoRecyclerView();

        }else{
            textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
        }

    }

    private void setAdaptertoRecyclerView() {
        recordingAdapter = new RecordingAdapter(getContext(),recordingArraylist);
        recyclerViewRecordings.setAdapter(recordingAdapter);
    }

}
