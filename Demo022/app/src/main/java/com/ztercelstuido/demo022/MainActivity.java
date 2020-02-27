package com.ztercelstuido.demo022;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private boolean mPrepared = false;
    private MediaPlayer mMediaPlayer = null;
    private AudioManager mAudioManager = null;
    private LoudnessEnhancer mLoudnessEnhancer = null;
    private AcousticEchoCanceler mAcousticEchoCanceler = null;

    private Handler mCalHandler = new Handler(Looper.getMainLooper());
    private final Runnable mTicker = new Runnable() {
        public void run() {
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            mCalHandler.postAtTime(mTicker, next);

            if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
                SeekBar process = (SeekBar)findViewById(R.id.seekbar_process);
                process.setMax(mMediaPlayer.getDuration());
                process.setProgress(mMediaPlayer.getCurrentPosition());

                int duration = mMediaPlayer.getDuration() / 1000;  // in second
                int current  = mMediaPlayer.getCurrentPosition() / 1000;  // in second
                TextView audioTime = (TextView)findViewById(R.id.textView_time);
                audioTime.setText(String.format("%02d:%02d/%02d:%02d",
                        current / 60, current % 60, duration / 60, duration % 60));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaPlayer = new MediaPlayer();
       // NoiseSuppressor.create(mMediaPlayer.getAudioSessionId()).setEnabled(true);
        mLoudnessEnhancer = new LoudnessEnhancer(mMediaPlayer.getAudioSessionId());
       // Log.d("zTercel", "is " + String.valueOf(AcousticEchoCanceler.isAvailable()));

       // mAcousticEchoCanceler = AcousticEchoCanceler.create(mMediaPlayer.getAudioSessionId());
       // if (null != mAcousticEchoCanceler) {
       //     Log.d("zTercel", "dddddddddd");
       // }

        Equalizer mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId()); //启用、获取/设置参数
         mEqualizer.setEnabled(true);
        //short bands = mEqualizer.getNumberOfBands();
       // Log.d("zTercel", "" + bands);
        // mEqualizer.getBandLevelRange()
        short band = 1;
        mEqualizer.setBandLevel(band, (short) 100);

        // record20200226110453_quzao.3gpp  // 人声
        // "/sdcard/test/test2.3gpp");  // 人声
        // "/sdcard/test/test1.3gpp");  // 人声
        // "/sdcard/test/test.amr");  // 人声
        // "/sdcard/test/test.3gp");  // 人声
        // "/sdcard/test/test.mp3");  // 许魏
        // "/sdcard/test/test1.mp3");  // 许魏

        openAudioFile("/sdcard/test/record20200226110453_quzao.3gpp");

        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);

        ToggleButton toggleButton = findViewById(R.id.loop);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null != mMediaPlayer) {
                    mMediaPlayer.setLooping(isChecked);
                }
            }
        });

        SeekBar seekbar = (SeekBar)findViewById(R.id.seekbar_grain);
        seekbar.setMax(10000);
        seekbar.setProgress(2000);
        seekbar.setOnSeekBarChangeListener(this);
        TextView textView = findViewById(R.id.textView_grain);
        textView.setText(String.valueOf(seekbar.getProgress()));

        SeekBar playSeekbar = (SeekBar)findViewById(R.id.seekbar_process);
        playSeekbar.setOnSeekBarChangeListener(this);

        mLoudnessEnhancer.setTargetGain(seekbar.getProgress());
        mLoudnessEnhancer.setEnabled(true);
       // mAcousticEchoCanceler.setEnabled(true);

        mCalHandler.post(mTicker);
    }

    private void openAudioFile(final String musicPath) {
        if (null == mMediaPlayer) return;
        try {
            mMediaPlayer.setDataSource(musicPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ACCESSIBILITY);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPrepared = true;

                SeekBar process = (SeekBar)findViewById(R.id.seekbar_process);
                process.setMax(mp.getDuration());
                process.setProgress(0);

                int duration = mp.getDuration() / 1000;  // in second
                int current  = mp.getCurrentPosition() / 1000;  // in second
                TextView audioTime = (TextView)findViewById(R.id.textView_time);
                audioTime.setText(String.format("%02d:%02d/%02d:%02d",
                        current / 60, current % 60, duration / 60, duration % 60));
                Log.d("zTercel", "duration = " + mp.getDuration());
                Log.d("zTercel", "getCurrentPosition = " + mp.getCurrentPosition());
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("zTercel", "finish...");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play: {
                if (mPrepared && !mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                break;
            }
            case R.id.pause: {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            }
            case R.id.stop: {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                break;
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.seekbar_grain) {
            TextView textView = findViewById(R.id.textView_grain);
            textView.setText(String.valueOf(progress));

            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//得到听筒模式的当前值
            mAudioManager.setSpeakerphoneOn(false);
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, AudioManager.STREAM_VOICE_CALL);
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);

            mLoudnessEnhancer.setTargetGain(progress);
            mLoudnessEnhancer.setEnabled(true);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(seekBar.getProgress()); //, mMediaPlayer.SEEK_CLOSEST);
        }
    }
}
