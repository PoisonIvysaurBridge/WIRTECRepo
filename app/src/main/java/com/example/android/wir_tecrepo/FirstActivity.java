package com.example.android.wir_tecrepo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // SUBMIT BUTTON
        Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get layout set up
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.root_view);

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                // Get the input in the edit field
                EditText nameField = (EditText) findViewById(R.id.name_edit_text);

                // Convert it to readable string text
                String name = nameField.getText().toString();

                // Set it in welcome text view
                TextView welcome = (TextView) findViewById(R.id.welcome);
                welcome.setText("Hello " + name + "! Welcome to Chopin world!");
            }
        });

        final EditText editText = (EditText) findViewById(R.id.name_edit_text);

        final TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable name) {
                TextView welcome = (TextView) findViewById(R.id.welcome);
                welcome.setText(name.toString());
                welcome.setText("Hello " + name.toString() + "! Welcome to the WIR-TEC Repo of Ivy!!!");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        final CheckBox autoRefresh = (CheckBox) findViewById(R.id.auto_refresh);

        autoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (autoRefresh.isChecked()) {
                    editText.addTextChangedListener(inputTextWatcher);
                } else {
                    editText.removeTextChangedListener(inputTextWatcher);
                }
            }
        });

        // The button that leads to the Lifecycle Activity
        Button lifecycle = (Button) findViewById(R.id.lifecycle);
        lifecycle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, LifecycleActivity.class);
                startActivity(intent);
            }
        });
    }
}
