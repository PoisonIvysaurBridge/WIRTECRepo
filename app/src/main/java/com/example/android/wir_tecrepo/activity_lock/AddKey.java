package com.example.android.wir_tecrepo.activity_lock;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

public class AddKey extends AppCompatActivity{
    public final static String LOCK_ONE_NUM = "LOCK_ONE_NUM";
    public final static String LOCK_TWO_NUM = "LOCK_TWO_NUM";
    public final static String LOCK_THREE_NUM = "LOCK_THREE_NUM";
    private EditText lock1,
            lock2,
            lock3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);

        lock1 = (EditText) findViewById(R.id.lock_one);
        lock2 = (EditText) findViewById(R.id.lock_two);
        lock3 = (EditText) findViewById(R.id.lock_three);


        Button submit = (Button) findViewById(R.id.submit_action);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputs()){
                    prepareData();
                    finish();
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean checkInputs(){
        int lockNum1 = -1, lockNum2 = -1, lockNum3 = -1;

        if(lock1.getText().toString().length() == 0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.lock1.getWindowToken(), 0);
            displayFeedback("Please input a number for the lock 1.");
            return false;
        }
        if(lock2.getText().toString().length() == 0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.lock2.getWindowToken(), 0);
            displayFeedback("Please input a number for the lock 2.");
            return false;
        }

        if(lock3.getText().toString().length() == 0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.lock3.getWindowToken(), 0);
            displayFeedback("Please input a number for the lock 3.");
            return false;
        }

        if(lock1.getText().toString().length() > 0 &&
                lock2.getText().toString().length() > 0 &&
                lock3.getText().toString().length() > 0) {
            lockNum1 = Integer.parseInt(lock1.getText().toString());
            lockNum2 = Integer.parseInt(lock2.getText().toString());
            lockNum3 = Integer.parseInt(lock3.getText().toString());
        }

        // check also if the weight provided is within the proper range
        if(lockNum1 < 0 || lockNum1 > 9 ||
                lockNum2 < 0 || lockNum2 > 9 ||
                lockNum3 < 0 || lockNum3 > 9 ) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.lock3.getWindowToken(), 0);
            displayFeedback("Please enter a digit between 0 and 9 inclusive.");
            return false;
        }
        return true;
    }

    public void prepareData(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(LOCK_ONE_NUM, Integer.parseInt(lock1.getText().toString()));
        bundle.putInt(LOCK_TWO_NUM, Integer.parseInt(lock2.getText().toString()));
        bundle.putInt(LOCK_THREE_NUM, Integer.parseInt(lock3.getText().toString()));

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    public void displayFeedback(String prompt){
        //Toast.makeText(AddRestaurant.this, prompt, Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar.make(AddKey.this.findViewById(R.id.add_resto_layout), prompt,3000);

        TextView snackbarActionTextView =  snackbar.getView().findViewById( android.support.design.R.id.snackbar_text );
        snackbarActionTextView.setTextSize(20);
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

        snackbar.show();
    }
}
