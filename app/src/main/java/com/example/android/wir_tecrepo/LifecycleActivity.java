package com.example.android.wir_tecrepo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import java.util.Calendar;

public class LifecycleActivity extends AppCompatActivity {
    private final String LOG_TAG = "Lifecycle Activity";

    /** the log string to be displayed on screen */             private String logcat = "";
    /** lifecycle counter for the onCreate callback method */   private int ctrCreate = 0;
    /** lifecycle counter for the onStart callback method */    private int ctrStart = 0;
    /** lifecycle counter for the onResume callback method */   private int ctrResume = 0;
    /** lifecycle counter for the onPause callback method */    private int ctrPause = 0;
    /** lifecycle counter for the onStop callback method */     private int ctrStop = 0;
    /** lifecycle counter for the onDestroy callback method */  private int ctrDestroy = 0;

    /** the shared preference object for persistent storage */  private SharedPreferences mPrefs;

    /** the activity tag to be used for debugging */            private static final String TAG = "LifecycleActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);

/* ################################## RESTORE SAVED STATE DATA ##################################
        // OPTION #1: Using Bundle saveInstanceState to temporarily store current activity states
        // Bundle is only appropriate for saving orientation changes
        // IT ONLY STORES IN RAM MEMORY AND IS VOLATILE
        if(savedInstanceState != null){
            ctrCreate = savedInstanceState.getInt("ctrCreate");
            ctrStart = savedInstanceState.getInt("ctrStart");
            ctrResume = savedInstanceState.getInt("ctrResume");
            ctrPause = savedInstanceState.getInt("ctrPause");
            ctrStop = savedInstanceState.getInt("ctrStop");
            ctrDestroy = savedInstanceState.getInt("ctrDestroy");

            logcat = savedInstanceState.getString("logcat");
        }
*/
        // OPTION #2: Using SharedPreferences to permanently store activity states
        // SharedPreference is appropriate for long-term use (e.g. user preferences)
        mPrefs = this.getSharedPreferences("com.example.android",MODE_PRIVATE);

        // if there are data stored previously in shared preferences, restore them
        // set the current counters to the last saved data state
        ctrCreate = mPrefs.getInt("ctrCreate", ctrCreate);
        ctrStart = mPrefs.getInt("ctrStart", ctrStart);
        ctrResume = mPrefs.getInt("ctrResume", ctrResume);
        ctrPause = mPrefs.getInt("ctrPause", ctrPause);
        ctrStop = mPrefs.getInt("ctrStop", ctrStop);
        ctrDestroy = mPrefs.getInt("ctrDestroy", ctrDestroy);
        logcat = mPrefs.getString("logcat", logcat);
// ######################################################################################################

        ctrCreate++;
        updateUI(R.id.ctr_create, R.id.time_create, ctrCreate);
        displayLog("onCreate");

        // onCreate only calls onStart and onResume
        // hence the very FIRST TIME the app opens, the app needs to update the UI screen
        // for onPause, onStop, and onDestroy text views
        updateUI(R.id.ctr_pause, R.id.time_pause, ctrPause);
        updateUI(R.id.ctr_stop, R.id.time_stop, ctrStop);
        updateUI(R.id.ctr_destroy, R.id.time_destroy, ctrDestroy);

        // Save the data in the shared preference editor
        saveData("ctrCreate", ctrCreate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ctrStart++;
        updateUI(R.id.ctr_start, R.id.time_start, ctrStart);
        displayLog("onStart");

        // Save the data in the shared preference editor
        saveData("ctrStart", ctrStart);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ctrResume++;
        updateUI(R.id.ctr_resume, R.id.time_resume, ctrResume);
        displayLog("onResume");

        // Save the data in the shared preference editor
        saveData("ctrResume", ctrResume);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ctrPause++;
        updateUI(R.id.ctr_pause, R.id.time_pause, ctrPause);
        displayLog("onPause");

        // Save the data in the shared preference editor
        saveData("ctrPause", ctrPause);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ctrStop++;
        updateUI(R.id.ctr_stop, R.id.time_stop, ctrStop);
        displayLog("onStop");

        // Save the data in the shared preference editor
        saveData("ctrStop", ctrStop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ctrDestroy++;
        updateUI(R.id.ctr_destroy, R.id.time_destroy, ctrDestroy);
        displayLog("onDestroy");

        // Save the data in the shared preference editor
        saveData("ctrDestroy", ctrDestroy);
    }

    /**
     * This method updates the values of the views in the UI
     * @param lifeCycleView is the resource ID of the text view that displays the lifecycle counter value
     * @param lifeCycleTimeStamp is the resource ID of the text view that displays the timestamp
     * @param lifeCycleCtr is the lifecycle counter
     */
    public void updateUI(Integer lifeCycleView, Integer lifeCycleTimeStamp, Integer lifeCycleCtr){
        // get the current time
        String timestamp = "" + java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

        // update the lifecycle counter in UI
        TextView textView = (TextView) findViewById(lifeCycleView);
        textView.setText(""+lifeCycleCtr);

        // update the lifecycle timestamp in UI
        textView = (TextView) findViewById(lifeCycleTimeStamp);
        textView.setText(timestamp);
    }

    /**
     * Displays the log of the triggered lifecycle callback together with the timestamp
     * @param strLog is the callback method that was triggered
     */
    public void displayLog(String strLog){
        // get the current time
        String timestamp = "" + java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

        // append the new log to the existing logs
        // log the timestamp when the lifecycle method is called in the scroll view
        logcat += timestamp + " - Triggered " + strLog + "()\n";
        TextView logs = (TextView) findViewById(R.id.log_text_view);
        logs.setText(logcat);
    }

    /**
    * This method saves the current activity state data into the SharedPreference editor.
     * @param ctrKey is the key name of the lifecycle counter
     * @param ctrValue is the value of the lifecycle counter
     */
    /**
     * The saved data is then stored into persistent storage of the device.
     * editor.apply() was used instead of editor.commit() since the boolean return value
     * of commit() will not be used, so using apply() here would be more efficient as the
     * method can execute asynchronously, not needing to wait for the return value as with
     * the commit() method.
    */
    public void saveData(String ctrKey, Integer ctrValue){
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt(ctrKey , ctrValue);
        ed.putString("logcat", logcat);
        ed.apply();
    }

    // OPTION #1: Using Bundle saveInstanceState to temporarily store current activity states
    // Bundle is only appropriate for saving orientation changes
    // IT ONLY STORES IN RAM MEMORY AND IS VOLATILE
    // This is called after onPause and before onStop,
    // hence it would not be able to save the counters in onStop and onDestroy
    // Option #1 can be replaced with Option #2 which is using SharedPreferences editor
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Toast.makeText(this, "called onSaveInstanceState!", Toast.LENGTH_SHORT).show();

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("ctrCreate", ctrCreate);
        savedInstanceState.putInt("ctrStart", ctrStart);
        savedInstanceState.putInt("ctrResume", ctrResume);
        savedInstanceState.putInt("ctrPause", ctrPause);
        savedInstanceState.putInt("ctrStop", ctrStop);
        savedInstanceState.putInt("ctrDestroy", ctrDestroy);
        savedInstanceState.putString("logcat", logcat);

//        savedInstanceState.putString("timeCreated", (String) ((TextView) findViewById(R.id.time_create)).getText());
//        savedInstanceState.putString("timeStarted", (String) ((TextView) findViewById(R.id.time_start)).getText());
//        savedInstanceState.putString("timeResumed", (String) ((TextView) findViewById(R.id.time_resume)).getText());
//        savedInstanceState.putString("timePaused", (String) ((TextView) findViewById(R.id.time_pause)).getText());
//        savedInstanceState.putString("timeStopped", (String) ((TextView) findViewById(R.id.time_stop)).getText());
//        savedInstanceState.putString("timeDestroyed", (String) ((TextView) findViewById(R.id.time_destroy)).getText());

        super.onSaveInstanceState(savedInstanceState);
    }
}

/*
    // OPTION #1: Using Bundle saveInstanceState to temporarily store current activity states
    // Bundle is only appropriate for saving orientation changes
    // onRestoreInstanceState is only called when onStart is called,
    // hence onCreate would not update unless u restore it in onCreate
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        ctrCreate = savedInstanceState.getInt("ctrCreate");
        ctrStart = savedInstanceState.getInt("ctrStart");
        ctrResume = savedInstanceState.getInt("ctrResume");
        ctrPause = savedInstanceState.getInt("ctrPause");
        ctrStop = savedInstanceState.getInt("ctrStop");
        ctrDestroy = savedInstanceState.getInt("ctrDestroy");
        //logcat = savedInstanceState.getString("logcat");
        Toast.makeText(this, "called on restore instance state!", Toast.LENGTH_SHORT).show();
    }
    */