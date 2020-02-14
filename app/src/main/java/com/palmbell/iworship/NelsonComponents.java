package com.palmbell.iworship;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Nelson on 4/5/2018.
 */

public class NelsonComponents extends AppCompatActivity {

    public static String baseUrl = "http://192.168.43.235/iworship_app_php/";
    public static String songsBaseUrl = "http://192.168.43.235/iworship_app_php/";

    // This method counts all the music in the device
    public int getMusicCount(Context whichContext) {
        ContentResolver contentResolver = whichContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        int count = 0;
        if (songCursor != null && songCursor.moveToFirst()) {
            do {
                count += 1;
            } while (songCursor.moveToNext());
        }
        return count;
    }


    // getting the songs title from the external storage
    public ArrayList<String> getSongsTitle(Context whichContext) {

        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();

        ContentResolver contentResolver = whichContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                String currentTitle = songCursor.getString(songTitle);
                arrayList.add(currentTitle);
            } while (songCursor.moveToNext());
        }
        return arrayList;
    }

    // getting the songs from the external storage
    public ArrayList<String> getSongsLocationLocal(Context whichContext) {
        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();
        ContentResolver contentResolver = whichContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentLocation = songCursor.getString(songLocation);
                arrayList.add(currentLocation);
            } while (songCursor.moveToNext());
        }
        return arrayList;
    }



    // getting the songs from the external storage
//    public ArrayList<String> getSongsLocationLocal(Context whichContext) {
//        ArrayList<String> arrayList;
//        arrayList = new ArrayList<>();
//        ContentResolver contentResolver = whichContext.getContentResolver();
//        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
//
//        if (songCursor != null && songCursor.moveToFirst()) {
//
//            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//
//            do {
//                String currentLocation = songCursor.getString(songLocation);
//                arrayList.add(currentLocation);
//            } while (songCursor.moveToNext());
//        }
//        return arrayList;
//    }



    // getting the songs from the external storage
    public ArrayList<String> getSongsDuration(Context whichContext) {
        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();
        ContentResolver contentResolver = whichContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {
                String currentDuration = songCursor.getString(songDuration);
                arrayList.add(currentDuration);
            } while (songCursor.moveToNext());
        }
        return arrayList;
    }


    // getting the songs from the external storage
    public ArrayList<String> getSongsArtist(Context whichContext) {
        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();
        ContentResolver contentResolver = whichContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String currentArtist = songCursor.getString(songArtist);
                arrayList.add(currentArtist);
            } while (songCursor.moveToNext());
        }
        return arrayList;
    }


    // getting the songs from the external storage
    public ArrayList<String> getSongInformation(Context whichContext) {

        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();

        ContentResolver contentResolver = whichContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {


            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentDuration = songCursor.getString(songDuration);

                int finalDuration = Integer.parseInt(currentDuration);
                finalDuration = finalDuration/1000;


                arrayList.add(currentTitle + "\n" + "Artist: " + currentArtist + "\n" + "Duration: " + convertSecondsToMinsAndSeconds(finalDuration) + "");
            } while (songCursor.moveToNext());
        }
        return arrayList;
    }


    // This method converts songs seconds to its equivalent in mins and seconds
    // You will first divide the milliseconds with 1000 before sending it to this method
    // mPlayer.getDuration() gives the duration in milliseconds
    public String convertSecondsToMinsAndSeconds(int totalSeconds){
        int minsInWhone = (totalSeconds/60);
        int secondsOfMinsInWhole = (minsInWhone*60);
        int remainingSeconds = totalSeconds-secondsOfMinsInWhole;
        String finalOutput = (minsInWhone>0)?minsInWhone+":"+remainingSeconds:""+remainingSeconds;
        return finalOutput;
    }



}

