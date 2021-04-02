package com.vullnetlimani.instacreative.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.vullnetlimani.instacreative.Activities.CommentActivity;
import com.vullnetlimani.instacreative.Activities.FollowersActivity;
import com.vullnetlimani.instacreative.Fragments.ProfileFragment;
import com.vullnetlimani.instacreative.Helper.Constants;
import com.vullnetlimani.instacreative.Helper.DoubleClickListener;
import com.vullnetlimani.instacreative.Helper.GlideEngine;
import com.vullnetlimani.instacreative.Model.Notification;
import com.vullnetlimani.instacreative.Model.Post;
import com.vullnetlimani.instacreative.Model.User;
import com.vullnetlimani.instacreative.R;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private static String LOG_TAG = "HomeFragmentLog";
    private Context context;
    private List<Post> mPost;
    private FirebaseUser firebaseUser;

    private AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    private AnimatedVectorDrawable animatedVectorDrawable;

    public PostAdapter(Context context, List<Post> mPost) {
        this.context = context;
        this.mPost = mPost;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = mPost.get(position);

        GlideEngine.createGlideEngine().loadImage(context, post.getImageurl(), holder.postImage);

        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if (user.getImageurl().equals(Constants.IMAGE_DEFAULT_URL)) {
                    holder.imageProfile.setImageResource(R.drawable.ic_profile_placeholder);
                } else {
                    GlideEngine.createGlideEngine().loadImage(context, user.getImageurl(), holder.imageProfile);
                }


                holder.username.setText(user.getUsername());
                holder.author.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.noOfLikes);
        getComments(post.getPostid(), holder.noOfComments);
        isSaved(post.getPostid(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.like.getTag().equals(Constants.LIKE)) {

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(Constants.LIKES)
                            .child(post.getPostid())
                            .child(firebaseUser.getUid())
                            .setValue(true);

                    addNotification(post.getPostid(), post.getPublisher(), true);

                } else {
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(Constants.LIKES)
                            .child(post.getPostid())
                            .child(firebaseUser.getUid())
                            .removeValue();

                    addNotification(post.getPostid(), post.getPublisher(), false);
                }

            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra(Constants.POSTID, post.getPostid());
                intent.putExtra(Constants.AUTHOR_ID, post.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra(Constants.POSTID, post.getPostid());
                intent.putExtra(Constants.AUTHOR_ID, post.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.save.getTag().equals(Constants.SAVE)) {
                    FirebaseDatabase.getInstance().getReference().child(Constants.SAVES)
                            .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child(Constants.SAVES)
                            .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                }

            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.getSharedPreferences(Constants.PROFILE, Context.MODE_PRIVATE)
                        .edit().putString(Constants.PROFILE_ID, post.getPublisher()).apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                        .addToBackStack(ProfileFragment.class.getSimpleName()).commit();

            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.getSharedPreferences(Constants.PROFILE, Context.MODE_PRIVATE)
                        .edit().putString(Constants.PROFILE_ID, post.getPublisher()).apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                        .addToBackStack(ProfileFragment.class.getSimpleName()).commit();

            }
        });

        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.getSharedPreferences(Constants.PROFILE, Context.MODE_PRIVATE)
                        .edit().putString(Constants.PROFILE_ID, post.getPublisher()).apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                        .addToBackStack(ProfileFragment.class.getSimpleName()).commit();

            }
        });

        Drawable drawable = holder.insta_heart.getDrawable();

        holder.postImage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {

                context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).edit().putString(Constants.POSTID, post.getPostid()).apply();

                holder.insta_heart.setAlpha(0.70f);

                if (drawable instanceof AnimatedVectorDrawableCompat) {

                    animatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    animatedVectorDrawableCompat.start();

                } else if (drawable instanceof AnimatedVectorDrawable) {

                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }


                if (holder.like.getTag().equals(Constants.LIKE)) {

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(Constants.LIKES)
                            .child(post.getPostid())
                            .child(firebaseUser.getUid())
                            .setValue(true);

                    addNotification(post.getPostid(), post.getPublisher(), true);

                } else {
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(Constants.LIKES)
                            .child(post.getPostid())
                            .child(firebaseUser.getUid())
                            .removeValue();

                    addNotification(post.getPostid(), post.getPublisher(), false);
                }


            }
        });

        holder.noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FollowersActivity.class);
                intent.putExtra(Constants.ID, post.getPostid());
                intent.putExtra(Constants.TITLE, Constants.LIKES_HELPER);
                context.startActivity(intent);
            }
        });


    }

    private void isSaved(String postId, ImageView imageView) {

        FirebaseDatabase.getInstance().getReference().child(Constants.SAVES).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(postId).exists()) {
                            imageView.setImageResource(R.drawable.ic_saved_icon);
                            imageView.setTag(Constants.SAVED);
                        } else {
                            imageView.setImageResource(R.drawable.ic_save_icon);
                            imageView.setTag(Constants.SAVE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getComments(String postId, TextView noOfComments) {
        FirebaseDatabase.getInstance().getReference().child(Constants.COMMENTS).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                noOfComments.setText(context.getString(R.string.no_of_comments, snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void noOfLikes(String postId, TextView textView) {

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKES).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                textView.setText(context.getString(R.string.no_of_likes, snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isLiked(String postId, ImageView imageView) {

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKES).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setColorFilter(Color.RED);
                    imageView.setTag(Constants.LIKED);
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setColorFilter(context.getResources().getColor(android.R.color.darker_gray));
                    imageView.setTag(Constants.LIKE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    private void addNotification(String postId, String publisherId, boolean like) {

        Log.d("addNotification", "postId - " + postId);
        Log.d("addNotification", "publisherId - " + publisherId);

        if (like) {

            HashMap<String, Object> map = new HashMap<>();

            map.put(Notification.USERID, firebaseUser.getUid());
            map.put(Notification.TEXT, "liked your post.");
            map.put(Notification.POSTID, postId);
            map.put(Notification.IS_POST, true);

            FirebaseDatabase.getInstance().getReference().child(Constants.NOTIFICATIONS).child(publisherId).push().setValue(map);

        } else {
            FirebaseDatabase.getInstance().getReference().child(Constants.NOTIFICATIONS).child(publisherId).removeValue();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView insta_heart;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;

        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        public SocialTextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            insta_heart = itemView.findViewById(R.id.insta_heart);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);
            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            author = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            description = itemView.findViewById(R.id.description);

        }
    }

}
