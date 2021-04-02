package com.vullnetlimani.instacreative.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vullnetlimani.instacreative.Fragments.ProfileFragment;
import com.vullnetlimani.instacreative.Helper.Constants;
import com.vullnetlimani.instacreative.Helper.GlideEngine;
import com.vullnetlimani.instacreative.MainActivity;
import com.vullnetlimani.instacreative.Model.Notification;
import com.vullnetlimani.instacreative.Model.User;
import com.vullnetlimani.instacreative.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> mUsers;
    private boolean isFragemnt;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> mUsers, boolean isFragemnt) {
        this.context = context;
        this.mUsers = mUsers;
        this.isFragemnt = isFragemnt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        User user = mUsers.get(position);

        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());

        if (!user.getImageurl().equals(Constants.IMAGE_DEFAULT_URL)) {
            GlideEngine.createGlideEngine().loadImage(context, user.getImageurl(), holder.imageProfile);
        }

        isFollowed(user.getId(), holder.btnFollow);

        if (user.getId().equals(firebaseUser.getUid()))
            holder.btnFollow.setVisibility(View.GONE);


        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.btnFollow.getText().toString().equals("follow")) {

                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.FOLLOW)
                            .child(firebaseUser.getUid())
                            .child(Constants.FOLLOWING)
                            .child(user.getId())
                            .setValue(true);

                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.FOLLOW)
                            .child(user.getId())
                            .child(Constants.FOLLOWERS)
                            .child(firebaseUser.getUid())
                            .setValue(true);

                    addNotification(user.getId(), true);

                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.FOLLOW)
                            .child(firebaseUser.getUid())
                            .child(Constants.FOLLOWING)
                            .child(user.getId())
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.FOLLOW)
                            .child(user.getId())
                            .child(Constants.FOLLOWERS)
                            .child(firebaseUser.getUid())
                            .removeValue();

                    addNotification(user.getId(), false);

                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFragemnt) {

                    context.getSharedPreferences(Constants.PROFILE, Context.MODE_PRIVATE).edit().putString(Constants.PROFILE_ID, user.getId()).apply();

                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).addToBackStack(ProfileFragment.class.getSimpleName()).commit();

                } else {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(Constants.PUBLISHER_ID, user.getId());
                    context.startActivity(intent);
                }

            }
        });

    }

    private void addNotification(String id, boolean follow) {

        if (follow) {

            HashMap<String, Object> map = new HashMap<>();

            map.put(Notification.USERID, firebaseUser.getUid());
            map.put(Notification.TEXT, "started following you.");
            map.put(Notification.POSTID, "");
            map.put(Notification.IS_POST, false);

            FirebaseDatabase.getInstance().getReference().child(Constants.NOTIFICATIONS).child(id).push().setValue(map);

        } else {
            FirebaseDatabase.getInstance().getReference().child(Constants.NOTIFICATIONS).child(id).removeValue();
        }

    }

    private void isFollowed(String id, Button btnFollow) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.FOLLOW).child(firebaseUser.getUid())
                .child(Constants.FOLLOWING);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists())
                    btnFollow.setText("following");
                else
                    btnFollow.setText("follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageProfile;
        public TextView username;
        public TextView fullname;
        public Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            btnFollow = itemView.findViewById(R.id.btn_follow);

        }
    }
}
