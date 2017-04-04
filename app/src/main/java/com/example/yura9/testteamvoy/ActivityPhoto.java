package com.example.yura9.testteamvoy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by yura9 on 4/4/2017.
 */

public class ActivityPhoto extends AppCompatActivity{


    public static final String PhotoID = "com.example.yura9.criminalintent.photo_id";
    public static final String RANDOM = "com.example.yura9.criminalintent.random";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment ==null){
            fragment = new PhotoFragment();
            fm.beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    public static Intent newIntent(Context context, String id, boolean random){
        Intent intent = new Intent(context, ActivityPhoto.class);
        intent.putExtra(PhotoID, id);
        intent.putExtra(RANDOM, random);
        return intent;
    }
}
