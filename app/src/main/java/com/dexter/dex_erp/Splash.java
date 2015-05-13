package com.dexter.dex_erp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Splash extends Activity {
    private static final long SPLASHTIME = 5000;
    SessionManager session;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    private ProgressBar progressBar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        session = new SessionManager(getApplicationContext());
        // StartAnimations();
        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());

        SplashHandler handlerSplash = new SplashHandler();
        progressBar.setMax((int) ((SPLASHTIME) / 1000));
        progressBar.setProgress(0);
        Message msg = new Message();
        msg.what = 0;
        handlerSplash.sendMessageDelayed(msg, SPLASHTIME);

        ThreadProgressBar threadProgress = new ThreadProgressBar();
        threadProgress.start();

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private class ThreadProgressBar extends Thread {
        ProgressBarHandler handlerProgress = new ProgressBarHandler();

        public void run() {
            try {

                while (progressBar.getProgress() <= progressBar.getMax()) {
                    Thread.sleep(1000);

                    handlerProgress
                            .sendMessage(handlerProgress.obtainMessage());
                }
            } catch (java.lang.InterruptedException e) {

            }
        }
    }

    private class ProgressBarHandler extends Handler {

        public void handleMessage(Message msg) {
            progressBar.incrementProgressBy((int) (SPLASHTIME / SPLASHTIME));
        }
    }

    private class SplashHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                case 0:
                    super.handleMessage(msg);
                    // new ProgressBarIncrease().execute();
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    // check for Internet status
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        if (session.isLoggedIn()) {
                            Intent intentMain = new Intent(getApplicationContext(),
                                    Home_Screen.class);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intentMain);
                            finish();
                        } else {

                            Intent intentMain = new Intent(getApplicationContext(),
                                    LoginActivity.class);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intentMain);
                            finish();
                        }

                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        showAlertDialog(Splash.this, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }

            }
        }
    }

}