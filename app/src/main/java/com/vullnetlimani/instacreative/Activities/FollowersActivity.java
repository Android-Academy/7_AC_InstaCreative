package com.vullnetlimani.instacreative.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vullnetlimani.instacreative.Adapters.UserAdapter;
import com.vullnetlimani.instacreative.Helper.Constants;
import com.vullnetlimani.instacreative.Model.User;
import com.vullnetlimani.instacreative.R;

import java.util.ArrayList;
import java.util.List;

public class FollowersActivity extends AppCompatActivity {

    private static final String LOG_TAG = "FollowerAcLog";
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    private String id;
    private String title;
    private List<String> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);


        Intent intent = getIntent();
        id = intent.getStringExtra(Constants.ID);
        title = intent.getStringExtra(Constants.TITLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(this, mUsers, false);

        recyclerView.setAdapter(userAdapter);

        idList = new ArrayList<>();

        Log.d(LOG_TAG, "title - " + title);

        switch (title) {
            case Constants.FOLLOWERS_HELPER:
                getFollowers();
                break;
            case Constants.FOLLOWINGS_HELPER:
                getFollowings();
                break;
            case Constants.LIKES_HELPER:
                getLikes();
                break;
        }

    }

    private void getLikes() {

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKES).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                idList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    idList.add(snap.getKey());
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getFollowings() {

        FirebaseDatabase.getInstance().getReference().child(Constants.FOLLOW).child(id).child(Constants.FOLLOWING).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    idList.add(snap.getKey());
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getFollowers() {

        FirebaseDatabase.getInstance().getReference().child(Constants.FOLLOW).child(id).child(Constants.FOLLOWERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    idList.add(snap.getKey());
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showUsers() {

        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {

                    User user = snap.getValue(User.class);

                    for (String id : idList) {
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }

                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}