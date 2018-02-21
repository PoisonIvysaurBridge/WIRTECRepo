package com.example.android.wir_tecrepo.activity_restaurant;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wir_tecrepo.R;

public class AddRestaurant extends AppCompatActivity {
    public final static String RESTAURANT_NAME_KEY = "RESTAURANT_NAME_KEY";
    public final static String RESTAURANT_DESC_KEY = "RESTAURANT_DESC_Key";
    public final static String WEIGHT_KEY = "WEIGHT_KEY";
    public final static String EDIT_MODEL_INDEX_KEY = "EDIT_MODEL_INDEX_KEY";

    private EditText editName,
                     editDesc,
                     editWeight;
    private int position = -1;
    private boolean isCanceled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        editName = (EditText) findViewById(R.id.edit_resto_name);
        editDesc = (EditText) findViewById(R.id.edit_resto_desc);
        editWeight = (EditText) findViewById(R.id.edit_resto_weight);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){ // there is a bundle
            position = bundle.getInt(EDIT_MODEL_INDEX_KEY, -1);
            String restoName = bundle.getString(RESTAURANT_NAME_KEY);
            String restoDesc = bundle.getString(RESTAURANT_DESC_KEY);
            Double restoWeight = bundle.getDouble(WEIGHT_KEY, 0.0);

            editName.setText("" + restoName);
            editDesc.setText("" + restoDesc);
            editWeight.setText("" + restoWeight);
        }

        Button submit = (Button) findViewById(R.id.add_resto);
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
                //Toast.makeText(AddRestaurant.this, "Cancel", Toast.LENGTH_SHORT).show();
                isCanceled = true;
                finish();
            }
        });
    }

    public boolean checkInputs(){
        if(editName.getText().toString().length() == 0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.editName.getWindowToken(), 0);
            displayFeedback("Please input a name for the new restaurant.");
            return false;
        }
        if(editDesc.getText().toString().length() == 0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.editDesc.getWindowToken(), 0);
            displayFeedback("Please input a description for the new restaurant.");
            return false;
        }

        double weight = -1;
        // if restaurant weight edit text field not empty
        if(editWeight.getText().toString().length() > 0) {
            weight = Double.parseDouble(editWeight.getText().toString());
        }

        // check also if the weight provided is within the proper range
        if(weight <= 0 || weight > 10) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
            displayFeedback("Please enter a restaurant weight between 1 and 10.");
            return false;
        }
        return true;
    }

    public void prepareData(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(RESTAURANT_NAME_KEY, editName.getText().toString());
        bundle.putString(RESTAURANT_DESC_KEY, editDesc.getText().toString());
        bundle.putDouble(WEIGHT_KEY, Double.parseDouble(editWeight.getText().toString()));
        bundle.putInt(EDIT_MODEL_INDEX_KEY, position);


        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    public void displayFeedback(String prompt){
        //Toast.makeText(AddRestaurant.this, prompt, Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar.make(AddRestaurant.this.findViewById(R.id.add_resto_layout), prompt,3000);

        TextView snackbarActionTextView =  snackbar.getView().findViewById( android.support.design.R.id.snackbar_text );
        snackbarActionTextView.setTextSize(20);
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

        snackbar.show();
    }
}
