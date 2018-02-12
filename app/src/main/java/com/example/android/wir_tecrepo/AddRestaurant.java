package com.example.android.wir_tecrepo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;

public class AddRestaurant extends AppCompatActivity {
    private EditText editName,
                     editDesc,
                     editWeight;
    private Integer position = -1;

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
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddRestaurant.this, "Cancel", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        // Make a bundle containing the current restaurant details
        Bundle bundle = new Bundle();
        bundle.putString("restoName", editName.getText().toString());
        bundle.putString("restoDesc", editDesc.getText().toString());
        bundle.putDouble("restoWeight", Double.parseDouble(editWeight.getText().toString()));
        if(position != -1){
            bundle.putInt("position", position);
        }

        intent.putExtras(bundle);
        // TODO replace with real value
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
