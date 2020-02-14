package com.palmbell.iworship;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NowPlaying extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private SeekBar mSeekBar;

    private MediaPlayer mPlayer;
    private Handler mHandler;
    private Runnable mRunnable;
    private boolean currentlyPlaying = true;
    private boolean currentlyRepeating = true;

    Animation plateAnimation;

    boolean repeatA = false;
    boolean repeatB = false;
    int positionA = 0;
    int positionB = 0;


    String songLocation, songDuration, songArtist, songTitle;
    private boolean currentlyShuffled = true;
    private boolean left_while_playing;
    private String songHost;
    private NelsonComponents nelsonComponents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_playing);

        nelsonComponents = new NelsonComponents();

        // Get the application context
        mContext = getApplicationContext();
        mActivity = NowPlaying.this;

        mHandler = new Handler();

        mSeekBar = (SeekBar) findViewById(R.id.now_playingSeekBar);



        songTitle = getIntent().getExtras().getString("song_title");
        songArtist = getIntent().getExtras().getString("song_artist");

        songHost = getIntent().getExtras().getString("song_host"); // i.e. online or offline


        TextView songTitleTV = (TextView) findViewById(R.id.now_playingSongTitleTV);
        songTitleTV.setText(songTitle);
        TextView songArtistTV = (TextView) findViewById(R.id.now_playingSongArtistTV);
        songArtistTV.setText(songArtist);

        TextView fontTextview = (TextView) findViewById(R.id.font_iconTV);
        fontTextview.setTypeface(NelsonFontManager.getTypeface(NowPlaying.this, NelsonFontManager.FONTAWESOME));


        final ImageView repeatIV = (ImageView) findViewById(R.id.now_playingRepeatIV);
        ImageView previousIV = (ImageView) findViewById(R.id.now_playingPreviousIV);
        final ImageView playIV = (ImageView) findViewById(R.id.now_playingPlayIV);
        ImageView nextIV = (ImageView) findViewById(R.id.now_playingNextIV);
        final ImageView shuffleIV = (ImageView) findViewById(R.id.now_playingShuffleIV);
        final ImageView largePlate = (ImageView) findViewById(R.id.now_playingLargePlate);
        final ImageView repeatAB_IV = (ImageView) findViewById(R.id.now_playingRepeatAB_IV);

        plateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_disk);


      //  File file = new File(songLocation);
//        if(file.exists()){
//            Uri songUri = Uri.fromFile(file);
//        }


        Uri songUri=Uri.EMPTY;
        if(songHost.equalsIgnoreCase("online")) { // if the song is coming from internet
            songLocation = getIntent().getExtras().getString("song_location");
            songLocation = nelsonComponents.songsBaseUrl+songLocation;
            mPlayer = new MediaPlayer();
            // load the song into the media player again
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try{
                mPlayer.setDataSource(songLocation);
            }catch (IllegalArgumentException e){
            }
            catch (SecurityException e){
            }catch (IllegalStateException e){
            }catch (IOException e){
            }

            try{
                mPlayer.prepare();
            }catch (IllegalStateException e){
            }catch (IOException e){
            }

            mPlayer.start();

            startTheAnimation(largePlate);


            //            songUri = get the song from the internet
        }
        else{
            songLocation = getIntent().getExtras().getString("song_location");
            File file = new File(songLocation);
            songUri = Uri.fromFile(file);
            mPlayer = new MediaPlayer();
            // load the song into the media player again
            mPlayer = MediaPlayer.create(mContext, songUri);
            mPlayer.start();
            startTheAnimation(largePlate);

        }



        // Initialize the media player
//        mPlayer = MediaPlayer.create(mContext, songUri);


        initializeSeekBar();

        repeatAB_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(repeatA)) {
                    positionA = mPlayer.getCurrentPosition();
                    repeatA = true;
                    repeatAB_IV.setImageResource(R.mipmap.repeat_a_on);
                } else if ((repeatA) && (!repeatB)) {
                    positionB = mPlayer.getCurrentPosition();
                    repeatB = true;
                    repeatAB_IV.setImageResource(R.mipmap.repeat_ab_on);
                    repeatSongFromPositionA(positionA);

                } else if ((repeatA) && (repeatB)) {
                    repeatA = false;
                    repeatB = false;
                    positionA = 0;
                    positionB = 0;
                    repeatAB_IV.setImageResource(R.mipmap.repeat_ab_off);
                }

            }
        });


        playIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPlayer = MediaPlayer.create(mContext, R.raw.nani_gi_buchi);

                if (currentlyPlaying) {
                    playIV.setImageResource(R.mipmap.play_btn);
                    mPlayer.pause();
                    currentlyPlaying = false;
                    stopTheAnimation();

                } else {
                    playIV.setImageResource(R.mipmap.pause);
                    mPlayer.start();
                    currentlyPlaying = true;
                    startTheAnimation(largePlate);

                }

            }
        });

        repeatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyRepeating) {
                    currentlyRepeating = false;
                    repeatIV.setImageResource(R.mipmap.repeat_none);
                } else {
                    currentlyRepeating = true;
                    repeatIV.setImageResource(R.mipmap.repeat_all);
                }
            }
        });

        shuffleIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyShuffled) {
                    currentlyShuffled = false;
                    shuffleIV.setImageResource(R.mipmap.shuffle_off);
                } else {
                    currentlyShuffled = true;
                    shuffleIV.setImageResource(R.mipmap.shuffle);
                }
            }
        });


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                stopTheAnimation();
                playIV.setImageResource(R.mipmap.play_btn);
                currentlyPlaying = false;

                // if the user sets point A and did not set point be before the song completes playing
                if (repeatA) {
                    repeatSongFromPositionA(positionA);
                    mPlayer.start();
                    startTheAnimation(largePlate);
                    playIV.setImageResource(R.mipmap.pause);
                    currentlyPlaying = true;
                    repeatB = true;
                    repeatAB_IV.setImageResource(R.mipmap.repeat_ab_on);
                    positionB = mPlayer.getDuration();

                }
            }
        });


        // Set a change listener for seek bar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mPlayer != null && b) {

                    mPlayer.seekTo(i * 1000);

                    // Get the duration
                    int totalDuration = mPlayer.getDuration();
                    int currentDuration = i * 1000;

                    updateCurrenetAndRemainingDuration(totalDuration, currentDuration);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    // This method updates the duration textviews
    public void updateCurrenetAndRemainingDuration(int totalDuration, int currentDuration) {
        TextView currentDur = (TextView) findViewById(R.id.currentDurationTV);
        TextView remainingDur = (TextView) findViewById(R.id.remainingDurationTV);

        NelsonComponents nelsonComponents = new NelsonComponents();
        currentDur.setText(nelsonComponents.convertSecondsToMinsAndSeconds(currentDuration / 1000) + "");
        remainingDur.setText("-" + nelsonComponents.convertSecondsToMinsAndSeconds((totalDuration - currentDuration) / 1000) + "");

    }


    public void startTheAnimation(View viewnameToApplyAnimation) {
        // we start the animation
        plateAnimation.setDuration(8000);
        plateAnimation.setRepeatCount(calculateAnimationRepeats((mPlayer.getDuration() / 1000) - (mPlayer.getCurrentPosition())));
        viewnameToApplyAnimation.startAnimation(plateAnimation);
    }

    public void stopTheAnimation() {
        // we start the animation
        plateAnimation.cancel();
        plateAnimation.reset();
        plateAnimation.setStartOffset(0);
    }

    protected void stopPlaying() {
        // If media player is not null then try to stop it
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    int currentSongPosition = 0;

    protected void initializeSeekBar() {
        mSeekBar.setMax(mPlayer.getDuration() / 1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mPlayer != null) {
                    int mCurrentPosition = mPlayer.getCurrentPosition() / 1000; // In milliseconds
                    mSeekBar.setProgress(mCurrentPosition);
//                    getAudioStats();

                    int totalDuration = mPlayer.getDuration();
                    int currentDuration = mPlayer.getCurrentPosition();

                    updateCurrenetAndRemainingDuration(totalDuration, currentDuration);


                    // for repeat AB
                    currentSongPosition = mPlayer.getCurrentPosition();
                    if ((repeatB) && (currentSongPosition >= positionB)) {
                        repeatSongFromPositionA(positionA);
                    }

                }
                mHandler.postDelayed(mRunnable, 100);
            }
        };
        mHandler.postDelayed(mRunnable, 100);
    }


    public int calculateAnimationRepeats(int totalSeconds) {

//        it takes 4 seconds of the song time to complete an animation of duration 5 sec
        int time = totalSeconds / 4;
        int finalRepeat = time * 8;

        return finalRepeat;
    }

    public void repeatSongFromPositionA(int positionA) {
        mPlayer.seekTo(positionA);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopPlaying();
        mPlayer.stop();
//        mPlayer.release();
        left_while_playing = true;
    }
}
