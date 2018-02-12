package com.example.android.wir_tecrepo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CookieActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie);
    }

    /**
     * Called when the cookie should be eaten.
     */
    public void eatCookie(View view) {
        // Find a reference to the ImageView in the layout. Change the image.
        ImageView imgView = (ImageView) findViewById(R.id.android_cookie_image_view);
        imgView.setImageResource(R.drawable.after_cookie);

        // Find a reference to the TextView in the layout. Change the text.
        TextView txtView = (TextView) findViewById(R.id.status_text_view);
        txtView.setText("I'm so full");
    }
}
