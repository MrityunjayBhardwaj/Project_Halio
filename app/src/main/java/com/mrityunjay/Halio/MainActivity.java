package com.mrityunjay.Halio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import mrityunjay.com.Halio.R;

public class MainActivity extends AppCompatActivity implements IMainActivity{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1234;
    public static String CAMERA_POSITION_FRONT;
    public static String CAMERA_POSITION_BACK;
    public static String MAX_ASPECT_RATIO;

    //widgets

    //vars
    private boolean mPermissions;
    public String mCameraOrientation = "none"; // Front-facing or back-facing

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio();
        }

        ViewPager viewPager = findViewById(R.id.viewPager);

        adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.setCurrentItem(1);


//        init();
    }


    public static final int ACTIVITY_RECORD_SOUND = 0;

    public void OnClickRecordAudio(View view) {

        try{
            Log.i("recorder","inside onClickRecordAudio .... starting activity intent");
            Intent audioRecorder = new Intent(this,RecorderAudioActivity.class);
            startActivity(audioRecorder);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
private boolean checkPermissionFroDevice(){
        int write_ext_storage_result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_ext_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
}

private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_CODE);
}



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RecorderAudioActivity.RECORD_AUDIO_REQUEST_CODE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionForAudioAndImages() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RecorderAudioActivity.RECORD_AUDIO_REQUEST_CODE);

        }
    }

    public void OnClickReloadRListFrag(View view) {
    }

    public void ReloadRListFrag() {
        // Reload current fragment
        Fragment frg = null;
        frg = getSupportFragmentManager().getFragments().get(1);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();

//        Snackbar.make(view,"Reload AudioPlaylistFragment",Snackbar.LENGTH_SHORT).show();
    }

    public class PagerAdapter extends FragmentPagerAdapter{
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.i("frag","inside getItem");
            switch (i){
                case 0:
                    // return to audio Playlist

                    return AudioPlaylistFragment.newInstance();
                case 1:
                    // return to camera.
//                    showSnackBar("You need a camera to use this application", Snackbar.LENGTH_SHORT);
                    Log.i("frag","this works");
                    startCamera2();
                    return Camera2Fragment.newInstance();
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() { return 2; }
    }

//    @Override
//    protected void onSwipeRight() {
//        Log.i("MyLog","inside onSwipeLeft!");
//
//        try{
//
//            Intent intent = new Intent(this,AudioPlaylist.class);
//            startActivity(intent);
//
//        }catch (Exception e){
//            Log.i("MyLog","can't open Audio Activity : "+e);
//        }
//    }
//
//    @Override
//    protected void onSwipeLeft() {
//        return ;
//    }

    private void startCamera2(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.camera_container, Camera2Fragment.newInstance(), getString(R.string.fragment_camera2));
        transaction.commit();
    }

    private void init(){
        if(mPermissions){
            if(checkCameraHardware(this)){

                // Open the Camera
                startCamera2();
            }
            else{
                showSnackBar("You need a camera to use this application", Snackbar.LENGTH_INDEFINITE);
            }
        }
        else{
            verifyPermissions();
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        String[] permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0] ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1] ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2] ) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true;
            init();
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(mPermissions){
                init();
            }
            else{
                verifyPermissions();
            }
        }
    }


    private void showSnackBar(final String text, final int length) {
        View view = this.findViewById(android.R.id.content).getRootView();
        Snackbar.make(view, text, length).show();
    }

    @Override
    public void setCameraFrontFacing() {
        Log.d(TAG, "setCameraFrontFacing: setting camera to front facing.");
        mCameraOrientation = CAMERA_POSITION_FRONT;
    }

    @Override
    public void setCameraBackFacing() {
        Log.d(TAG, "setCameraBackFacing: setting camera to back facing.");
        mCameraOrientation = CAMERA_POSITION_BACK;
    }

    @Override
    public void setFrontCameraId(String cameraId){
        CAMERA_POSITION_FRONT = cameraId;
    }


    @Override
    public void setBackCameraId(String cameraId){
        CAMERA_POSITION_BACK = cameraId;
    }

    @Override
    public boolean isCameraFrontFacing() {
        if(mCameraOrientation.equals(CAMERA_POSITION_FRONT)){
            return true;
        }
        return false;
    }

    @Override
    public boolean isCameraBackFacing() {
        if(mCameraOrientation.equals(CAMERA_POSITION_BACK)){
            return true;
        }
        return false;
    }

    @Override
    public String getBackCameraId(){
        return CAMERA_POSITION_BACK;
    }

    @Override
    public String getFrontCameraId(){
        return CAMERA_POSITION_FRONT;
    }

    @Override
    public void hideStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void showStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void hideStillshotWidgets() {
        Camera2Fragment camera2Fragment = (Camera2Fragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_camera2));
        if (camera2Fragment != null) {
            if(camera2Fragment.isVisible()){
                camera2Fragment.drawingStarted();
            }
        }
    }

    @Override
    public void showStillshotWidgets() {
        Camera2Fragment camera2Fragment = (Camera2Fragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_camera2));
        if (camera2Fragment != null) {
            if(camera2Fragment.isVisible()){
                camera2Fragment.drawingStopped();
            }
        }
    }

    @Override
    public void toggleViewStickersFragment(){

        ViewStickersFragment viewStickersFragment
                = (ViewStickersFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_stickers));
        if (viewStickersFragment != null) {
            if(viewStickersFragment.isVisible()){
                hideViewStickersFragment(viewStickersFragment);
            }
            else{
                showViewStickersFragment(viewStickersFragment);
            }
        }
        else{
            inflateViewStickersFragment();
        }
    }

    private void hideViewStickersFragment(ViewStickersFragment fragment){

        showStillshotWidgets();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.hide(fragment);
        transaction.commit();
    }

    private void showViewStickersFragment(ViewStickersFragment fragment){

        hideStillshotWidgets();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.show(fragment);
        transaction.commit();
    }

    private void inflateViewStickersFragment(){

        hideStillshotWidgets();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.camera_container, ViewStickersFragment.newInstance(), getString(R.string.fragment_view_stickers));
        transaction.commit();
    }

    @Override
    public void addSticker(Drawable sticker){
        Camera2Fragment camera2Fragment = (Camera2Fragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_camera2));
        if (camera2Fragment != null) {
            if(camera2Fragment.isVisible()){
                camera2Fragment.addSticker(sticker);
            }
        }
    }

    @Override
    public void setTrashIconSize(int width, int height){
        Camera2Fragment camera2Fragment = (Camera2Fragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_camera2));
        if (camera2Fragment != null) {
            if(camera2Fragment.isVisible()){
                camera2Fragment.setTrashIconSize(width, height);
            }
        }
    }

    public void dragStickerStarted(){
        Camera2Fragment camera2Fragment = (Camera2Fragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_camera2));
        if (camera2Fragment != null) {
            if(camera2Fragment.isVisible()){
                camera2Fragment.dragStickerStarted();
            }
        }
    }

    public void dragStickerStopped(){
        Camera2Fragment camera2Fragment = (Camera2Fragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_camera2));
        if (camera2Fragment != null) {
            if(camera2Fragment.isVisible()){
                camera2Fragment.dragStickerStopped();
            }
        }
    }
}








