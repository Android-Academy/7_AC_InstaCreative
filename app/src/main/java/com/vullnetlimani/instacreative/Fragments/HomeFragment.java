package com.vullnetlimani.instacreative.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vullnetlimani.instacreative.Adapters.PostAdapter;
import com.vullnetlimani.instacreative.Helper.Constants;
import com.vullnetlimani.instacreative.Model.Post;
import com.vullnetlimani.instacreative.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static String LOG_TAG = "HomeFragmentLog";
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerViewPosts.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);

        followingList = new ArrayList<>();

        checkFollowingUsers();

        return view;
    }

    private void checkFollowingUsers() {

        Log.d(LOG_TAG, "Current User - " + FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FOLLOW)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.FOLLOWING).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    followingList.add(snap.getKey());
                }

                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                Log.d(LOG_TAG, "followingList - " + followingList.size());

                readPosts();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child(Constants.POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Post post = snap.getValue(Post.class);

                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            postList.add(post);
                        }
                    }
                }

                Log.d(LOG_TAG, "postList - " + postList.size());

                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}