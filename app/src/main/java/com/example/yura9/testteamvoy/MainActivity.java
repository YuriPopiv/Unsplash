package com.example.yura9.testteamvoy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yura9.testteamvoy.model.Photo;
import com.example.yura9.testteamvoy.model.PhotoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API = "44ee9ae4dedf5a3cbb94a60e8ad8b34769f1f5554547746a9e3cd6c5d2f965f0";
    private static final String code = "5ea11570199c9675df387bd9c78fd81b99399c6a56ff77fdfc23b9278da3a837";
    private static final String secret = "04d4b622db4e40408be8cea72fca6725a6c37f0c272593165e5cd1fcd9535ae0";
    private static final String redirect_uri ="urn:ietf:wg:oauth:2.0:oob";



    public static String access_token = "b3dd446fc660112799c5c48fd1925d5a4c1349f006d346fba282c7d90a945e8b";
    private Retrofit retrofit;
    private PhotoService service;
    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Photo> mPhotos;
    private PhotoLab mPhotoLab;
    private int curPage;
    private String curOrder;
    private boolean loading = true;
    private boolean isOrderChanged;
    int visibleItemCount, pastVisibleItems, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        curPage = 1;
        mPhotoLab = PhotoLab.get();

        //access token do not expire, so I do not need anymore a code below

        /*retrofit = new Retrofit.Builder()
                .baseUrl("https://unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PhotoService.class);

        final Call<TokenResponce> tr = service.getToken(API, secret, redirect_uri, code, "authorization_code");
        tr.enqueue(new Callback<TokenResponce>() {
            @Override
            public void onResponse(Call<TokenResponce> call, Response<TokenResponce> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<TokenResponce> call, Throwable t) {

            }
        });*/

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PhotoService.class);

        final Call<Photo> ph = service.getMe("Bearer "+ access_token);
        ph.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });

        mPhotos = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.photoList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPhotoAdapter = new PhotoAdapter();
        mRecyclerView.setAdapter(mPhotoAdapter);


        curOrder = "latest";
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy >0){
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading){
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount-5){
                            loading = false;
                            loadPhotos(curOrder);
                        }
                    }
                }
            }
        });
        loadPhotos(curOrder);
    }


    public void loadPhotos(String orderBy){
        if (curOrder != orderBy) {
            curOrder = orderBy;
            curPage = 1;
            isOrderChanged = true;
        }
        final Call<List<Photo>> ph = service.getPhotos("Bearer "+ access_token, curPage, curOrder);

        ph.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()){
                    if (isOrderChanged){
                        mPhotoLab.clearAll();
                        mPhotos.clear();
                        isOrderChanged = false;
                        mPhotoAdapter.notifyDataSetChanged();
                    }
                    mPhotoLab.setPhotos(response.body());

                    mPhotos = mPhotoLab.getPhotos();
                    curPage++;
                    loading = true;
                    mPhotoAdapter.notifyItemInserted(0);

                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
            }
        });
    }

    public void loadRandom(){
        final Call<Photo> ph = service.getRandom("Bearer "+ access_token);

        ph.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                Photo photo = response.body();
                mPhotoLab.setRandomPhoto(photo);
                Intent intent = ActivityPhoto.newIntent(MainActivity.this, photo.getId(), true);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.latest:
                loadPhotos("latest");
                return true;
            case R.id.oldest:
                loadPhotos("oldest");
                return true;
            case R.id.popular:
                loadPhotos("popular");
                return true;
            case R.id.random:
                loadRandom();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageView;
        public PhotoHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        private Photo mPhoto;
        public void setPhoto(Photo photo){
            mPhoto = photo;
            Glide.with(MainActivity.this).load(mPhoto.getUrls().getRegular()).into(mImageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ActivityPhoto.newIntent(MainActivity.this, mPhoto.getId(), false);
            startActivity(intent);
        }
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.item_view, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo p = mPhotos.get(position);
            holder.setPhoto(p);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
    }
}
