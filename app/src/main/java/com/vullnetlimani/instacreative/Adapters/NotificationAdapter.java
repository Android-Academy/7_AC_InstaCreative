package com.vullnetlimani.instacreative.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vullnetlimani.instacreative.Fragments.PostDetailFragment;
import com.vullnetlimani.instacreative.Fragments.ProfileFragment;
import com.vullnetlimani.instacreative.Helper.Constants;
import com.vullnetlimani.instacreative.Helper.GlideEngine;
import com.vullnetlimani.instacreative.Model.Notification;
import com.vullnetlimani.instacreative.Model.Post;
import com.vullnetlimani.instacreative.Model.User;
import com.vullnetlimani.instacreative.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context context, List<Notification> mNotifications) {
        this.context = context;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification notification = mNotifications.get(position);

        getUser(holder.imageProfile, holder.username, notification.getUserid());

        holder.comment.setText(notification.getText());

        if (notification.isIs_post()) {
            holder.postImage.setVisibility(View.VISIBLE);
            getPostImage(holder.postImage, notification.getPostid());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isIs_post()) {

                    context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).edit().putString(Constants.POSTID, notification.getPostid()).apply();

                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).addToBackStack(PostDetailFragment.class.getSimpleName()).commit();

                } else {
                    context.getSharedPreferences(Constants.PROFILE, Context.MODE_PRIVATE).edit().putString(Constants.PROFILE_ID, notification.getUserid()).apply();

                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).addToBackStack(ProfileFragment.class.getSimpleName()).commit();
                }
            }
        });

    }

    private void getPostImage(ImageView postImage, String postid) {

        FirebaseDatabase.getInstance().getReference().child(Constants.POSTS).child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Post post = snapshot.getValue(Post.class);

                GlideEngine.createGlideEngine().loadImage(context, post.getImageurl(), R.drawable.ic_placeholder, postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUser(ImageView imageProfile, TextView username, String userid) {

        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if (!user.getImageurl().equals(Constants.IMAGE_DEFAULT_URL)) {
                    GlideEngine.createGlideEngine().loadImage(context, user.getImageurl(), imageProfile);
                }

                username.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;
        public ImageView postImage;
        public TextView username;
        public TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);

        }
    }
}
