package com.example.android.wir_tecrepo;

import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public boolean forCleaning = false;
    public MusicController(Context context) {
        super(context);
    }

    public void markForCleaning(boolean flag) {
        this.forCleaning = flag;
    }
    @Override
    public void hide() {
        if(this.forCleaning) {
            super.hide();
        }

    }
}
