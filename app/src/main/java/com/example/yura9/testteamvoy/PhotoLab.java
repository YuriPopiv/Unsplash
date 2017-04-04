package com.example.yura9.testteamvoy;

import com.example.yura9.testteamvoy.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura9 on 4/4/2017.
 */

public class PhotoLab {
    private static PhotoLab sPhotoLab;
    private List<Photo> mPhotos;
    private Photo randomPhoto;

    public static PhotoLab get(){
        if (sPhotoLab ==null){
            sPhotoLab = new PhotoLab();
        }
        return sPhotoLab;
    }

    private PhotoLab(){
        mPhotos = new ArrayList<>();
    }

    public List<Photo> getPhotos(){
        return mPhotos;
    }

    public Photo getPhoto(String id){

        for (Photo photo : mPhotos){

            if (photo.getId().equals(id)){
                return photo;
            }
        }
        return null;
    }

    public void setPhotos(List<Photo> photos){
        mPhotos.addAll(photos);
    }

    public void clearAll(){
        mPhotos.clear();
    }

    public void setRandomPhoto(Photo photo){
        randomPhoto = photo;
    }

    public Photo getRandom(){
        if (randomPhoto!= null){
            return randomPhoto;
        }
        return null;
    }
}
