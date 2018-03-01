package com.example.android.wir_tecrepo.activity_lock;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

import java.util.ArrayList;
import java.util.Random;

public class LockerActivity extends AppCompatActivity {
    static final int ADD_KEY_REQUEST = 0;

    private ArrayList<String> acceptedCombinations = new ArrayList<>();
    private TextView combinationView;
    private ArrayList<LockerDataModel> locks = new ArrayList<>();

    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;

    private LockerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager1;
    private LinearLayoutManager mLayoutManager2;
    private LinearLayoutManager mLayoutManager3;

    private boolean isVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);


        // setup recycler views
        mRecyclerView1  = (RecyclerView) findViewById(R.id.lock_view_1);
        mRecyclerView2  = (RecyclerView) findViewById(R.id.lock_view_2);
        mRecyclerView3  = (RecyclerView) findViewById(R.id.lock_view_3);

        // use a linear layout manager for the recycler view
        mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager3 = new LinearLayoutManager(this);
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView3.setLayoutManager(mLayoutManager3);

        mAdapter = new LockerAdapter(locks);
        mRecyclerView1.setAdapter(mAdapter);
        mRecyclerView2.setAdapter(mAdapter);
        mRecyclerView3.setAdapter(mAdapter);

        this.setupLocks();
        this.setupButtons();
        this.setDefaultCombinations();
    }

    private void setupLocks() {
        this.combinationView = this.findViewById(R.id.combination_txt_view);
        //setup your three "locks" here
        locks.add(new LockerDataModel(0));
        locks.add(new LockerDataModel(0));
        locks.add(new LockerDataModel(1));
        locks.add(new LockerDataModel(2));
        locks.add(new LockerDataModel(3));
        locks.add(new LockerDataModel(4));
        locks.add(new LockerDataModel(5));
        locks.add(new LockerDataModel(6));
        locks.add(new LockerDataModel(7));
        locks.add(new LockerDataModel(8));
        locks.add(new LockerDataModel(9));
        locks.add(new LockerDataModel(9));

        // for circular
        // layoutManager.scrollToPosition(Integer.MAX_VALUE/2-3);
    }

    private void setupButtons() {
        /*TODO: Add button listeners! If you already have the button functionalities, then remove 'setEnabled' function call and replace
         *TODO: with an onClick listener!
         */
        Button addBtn = this.findViewById(R.id.add_btn);
        addBtn.setEnabled(false);
        addBtn.setBackgroundColor(Color.GRAY);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LockerActivity.this, AddKey.class);
                startActivityForResult(intent, ADD_KEY_REQUEST);
            }
        });

        Button shuffleBtn = this.findViewById(R.id.shuffle_btn);
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView1.smoothScrollToPosition(new Random().nextInt(locks.size() % 9));
                mRecyclerView2.smoothScrollToPosition(new Random().nextInt(locks.size() % 9));
                mRecyclerView3.smoothScrollToPosition(new Random().nextInt(locks.size() % 9));

                int randomNum = new Random().nextInt(locks.size());
            }
        });

        Button enterBtn = this.findViewById(R.id.enter_btn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lock1Index = mLayoutManager1.findFirstVisibleItemPosition() + 1;
                int lock2Index = mLayoutManager2.findFirstVisibleItemPosition() + 1;
                int lock3Index = mLayoutManager3.findFirstVisibleItemPosition() + 1;

                LockerDataModel lock1 = locks.get(lock1Index);
                LockerDataModel lock2 = locks.get(lock2Index);
                LockerDataModel lock3 = locks.get(lock3Index);
// THANKK YOU LORD
//                Toast.makeText(LockerActivity.this, "ONE: " + lock1.getNumberString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(LockerActivity.this, "TWO: " + lock2.getNumberString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(LockerActivity.this, "THREE: " + lock3.getNumberString(), Toast.LENGTH_SHORT).show();

                verifyAndUnlock(lock1.getNumberString(), lock2.getNumberString(), lock3.getNumberString());
            }
        });
    }

    private void verifyAndUnlock(String a, String b, String c) {
        String combination = a + b + c;
        boolean unlocked = false;

        for(int i = 0; i < this.acceptedCombinations.size(); i++) {
            if(combination.compareTo(this.acceptedCombinations.get(i)) ==  0) {
                unlocked = true;
                break;
            }
        }

        if(unlocked) {
            isVerified = true;
            Button addBtn = this.findViewById(R.id.add_btn);
            addBtn.setEnabled(true);
            addBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            combinationView.setText("ACCESS GRANTED!");
            Intent unlockIntent = new Intent(LockerActivity.this, UnlockActivity.class);
            this.startActivity(unlockIntent);
        }
        else {
            combinationView.setText("Combination " +combination+ " is invalid! ACCESS DENIED!");
        }
    }

    private void setDefaultCombinations() {
        this.acceptedCombinations.add("123");
        this.acceptedCombinations.add("567");
        this.acceptedCombinations.add("444");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_KEY_REQUEST) { // if the user presses the ADD button
            if (resultCode == RESULT_OK) {  // if the user presses submit instead of the cancel button
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    int lock1 = bundle.getInt(AddKey.LOCK_ONE_NUM, 0);
                    int lock2 = bundle.getInt(AddKey.LOCK_TWO_NUM, 0);
                    int lock3 = bundle.getInt(AddKey.LOCK_THREE_NUM, 0);

                    this.acceptedCombinations.add("" + lock1 + "" + lock2 + "" + lock3);
                }
            }
        }
    }
}
