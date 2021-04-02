package com.vullnetlimani.instacreative.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.vullnetlimani.instacreative.Fragments.PostDetailFragment;
import com.vullnetlimani.instacreative.Helper.Constants;
import com.vullnetlimani.instacreative.Helper.GlideEngine;
import com.vullnetlimani.instacreative.Model.Post;
import com.vullnetlimani.instacreative.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<Post> mPosts;

    public PhotoAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = mPosts.get(position);

        GlideEngine.createGlideEngine().loadImage(context, post.getImageurl(), R.drawable.ic_placeholder, holder.postImage);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).edit().putString(Constants.POSTID, post.getPostid()).apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment())
                        .addToBackStack(PostDetailFragment.class.getSimpleName()).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView postImage;

        public ViewHolder(View view) {
            super(view);

            postImage = view.findViewById(R.id.post_image);

        }
    }
}
