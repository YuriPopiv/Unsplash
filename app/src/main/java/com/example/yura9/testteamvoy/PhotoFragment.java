package com.example.yura9.testteamvoy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yura9.testteamvoy.model.Photo;
import com.example.yura9.testteamvoy.model.PhotoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yura9 on 4/4/2017.
 */

public class PhotoFragment  extends Fragment{

    //Here i can't avoid violating «Don’t Repeat Yourself» principle. Sorry.

    private Retrofit retrofit;
    private PhotoService service;

    public ImageView photoFull;
    public ImageButton buttonUp;
    public ImageButton buttonDown;
    private Photo mPhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = (String) getActivity().getIntent().getSerializableExtra(ActivityPhoto.PhotoID);
        boolean random = getActivity().getIntent().getBooleanExtra(ActivityPhoto.RANDOM, false);
        if (random){
            mPhoto = PhotoLab.get().getRandom();
        }else {
            mPhoto = PhotoLab.get().getPhoto(id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        photoFull = (ImageView) v.findViewById(R.id.photoFull);
        buttonUp = (ImageButton) v.findViewById(R.id.buttonUp);
        buttonDown = (ImageButton) v.findViewById(R.id.buttondown);

        Glide.with(getActivity()).load(mPhoto.getUrls().getRegular()).into(photoFull);


        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PhotoService.class);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPhoto.getLikedByUser() != false){
                    return;
                }
                likePhoto();
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPhoto.getLikedByUser()!=true){
                    return;
                }
                dislikePhoto();
            }
        });

        return v;
    }

    public void likePhoto(){
        final Call<Photo> ph = service.likePhoto("Bearer "+ MainActivity.access_token, mPhoto.getId());

        ph.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Thank you!", Toast.LENGTH_SHORT).show();
                    mPhoto.setLikedByUser(true);
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });
    }

    public void dislikePhoto(){
        final Call<Photo> ph = service.dislikePhoto("Bearer "+ MainActivity.access_token, mPhoto.getId());

        ph.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Disliked!", Toast.LENGTH_SHORT).show();
                    mPhoto.setLikedByUser(false);
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });
    }
}
