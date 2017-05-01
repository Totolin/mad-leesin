package com.ace.ucv.mad.leesin;

import android.content.Context;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import java.text.DecimalFormat;

public class MainActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, IRequester {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer mPlayer;

    // Authentication lock
    private Lock lock;
    private String currentVideo;

    // Spotify api manager
    private SpotifyManager spotifyManager;

    // Ui Elements
    private EditText songSearchField;
    private TextView songInfo;
    private TextView songValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initializeYoutubeAPI();

        showAuthenticator();

        // Spotify Manager
        spotifyManager = new SpotifyManager();

        // Initialize UI components
        songSearchField = (EditText)findViewById(R.id.songsearch);
        songInfo = (TextView)findViewById(R.id.songinfo);
        songValues = (TextView)findViewById(R.id.songvalues);

        // Initial reset
        resetSongInfo();
    }

    private void showAuthenticator() {
        Auth0 auth0 = new Auth0("jKFbNazvbDgUEmfE9PAlWOtYbD2TIBUl", "ctotolin.eu.auth0.com");
        lock = Lock.newBuilder(auth0, callback)
                .loginAfterSignUp(true)
                .build(this);

        startActivity(lock.newIntent(this));
    }

    private void initializeYoutubeAPI() {
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);

    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            //Authenticated
            Log.d("AUTH0", "onAuthentication");
        }

        @Override
        public void onCanceled() {
            //User pressed back
            Log.d("AUTH0", "onCanceled");
        }

        @Override
        public void onError(LockException error) {
            //Exception occurred
            Log.d("AUTH0", "onError");
        }
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

            // Save player for class
            this.mPlayer = player;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private void resetSongInfo() {
        songInfo.setText("Name:\nMusicalness:\nInstrumentalness:\nEnergy:\nSpeechiness\nLoudness");
        songValues.setText("....\n....\n....\n....\n....\n....");
    }

    private void setSongInfo(SongInfo info) {
        DecimalFormat numberFormat = new DecimalFormat("0.00");

        songValues.setText (info.getName() + '\n'
                        + numberFormat.format(info.getMusicalness()) + '\n'
                        + numberFormat.format(info.getInstrumentalness()) + '\n'
                        + numberFormat.format(info.getEnergy()) + '\n'
                        + numberFormat.format(info.getSpeechiness()) + '\n'
                        + numberFormat.format(info.getLoudness())
        );
    }
    public void searchClick(View view) {
        // Hide keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Reset info displayed
        resetSongInfo();

        // Request song
        String songName = songSearchField.getText().toString();
        SongInfo info = this.spotifyManager.getSongInfo(songName);
        if (info != null) {
            setSongInfo(info);
            new Requester(this, songName).execute();
        }
    }

    public void mediaPlay(View view) {
        mPlayer.play();
    }

    public void mediaPause(View view) {
        mPlayer.pause();
    }

    public void mediaStop(View view) {
        mPlayer.loadVideo(currentVideo);
        mPlayer.pause();
    }

    @Override
    public void requestCallback(String videoID) {
        if (videoID != null) {
            // Load song in player
            currentVideo = videoID;
            mPlayer.loadVideo(videoID);
        }
    }
}
