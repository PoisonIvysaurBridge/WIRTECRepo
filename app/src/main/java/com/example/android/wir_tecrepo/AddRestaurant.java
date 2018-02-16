package com.example.android.wir_tecrepo;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;

public class AddRestaurant extends AppCompatActivity {
    private EditText editName,
                     editDesc,
                     editWeight;
    private Integer position = -1;
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
            position = bundle.getInt("position");
            String restoName = bundle.getString("restoName");
            String restoDesc = bundle.getString("restoDesc");
            Double restoWeight = bundle.getDouble("restoWeight", 0);


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
            this.displayFeedback("Please input a name for the new restaurant.");
            return false;
        }
        if(editDesc.getText().toString().length() == 0){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.editDesc.getWindowToken(), 0);
            this.displayFeedback("Please input a description for the new restaurant.");
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
            this.displayFeedback("Please enter a restaurant weight between 1 and 10.");
            return false;
        }
        return true;
    }

    public void prepareData(){
        Intent intent = new Intent();

        if(isCanceled == false){    // if not canceled, put in values
            // Make a bundle containing the current restaurant details
            //Toast.makeText(this,"isCanceled is FALSE", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("restoName", editName.getText().toString());
            bundle.putString("restoDesc", editDesc.getText().toString());
            bundle.putDouble("restoWeight", Double.parseDouble(editWeight.getText().toString()));
            if(position != -1){
                bundle.putInt("position", position);
            }

            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        else {  // if cancel button is pressed
            setResult(RESULT_CANCELED, intent);
        }
    }

    public void displayFeedback(String prompt){
        Toast.makeText(AddRestaurant.this, prompt, Toast.LENGTH_SHORT).show();
    }
}
