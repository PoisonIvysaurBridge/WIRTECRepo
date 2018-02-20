package com.example.android.wir_tecrepo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity implements TextWatcher{

    private final String displayFormat = "Hello %s! Welcome to the WIR-TEC Repo of Ivy!!!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final Button submit = (Button) findViewById(R.id.submit);
        submit.setTextColor(Color.WHITE);
        final EditText editText = (EditText) findViewById(R.id.name_edit_text);
        final TextView welcome = (TextView) findViewById(R.id.welcome);
        CheckBox autoRefresh = (CheckBox) findViewById(R.id.auto_refresh);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get layout set up
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.root_view);

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                // Convert it to readable string text
                String name = editText.getText().toString();

                // Set it in welcome text view
                welcome.setText(String.format(displayFormat, name));
            }
        });

        autoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editText.addTextChangedListener(FirstActivity.this);
                    submit.setBackgroundColor(Color.GRAY);
                    submit.setEnabled(false);
                } else {
                    editText.removeTextChangedListener(FirstActivity.this);
                    submit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    submit.setEnabled(true);
                }
            }
        });
        /*
        // The button that leads to the Lifecycle Activity
        Button lifecycle = (Button) findViewById(R.id.lifecycle);
        lifecycle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, LifecycleActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable name) {
        final TextView welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText(String.format(displayFormat, name));
    }
}
