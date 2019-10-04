package com.example.findher.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.findher.R;
import com.example.findher.models.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountProfileFragment extends Fragment implements View.OnClickListener
{
   private TextView userName;
   private ImageView photoOfUser;
   private Button logOutButton;
   private static final int UPDATE_USERNAME = 30;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.account_profile_fragment,container,false);

        userName = view.findViewById(R.id.nameOfTheUser);
        photoOfUser = view.findViewById(R.id.user_photo);
        logOutButton = view.findViewById(R.id.log_out_btn);        Button button = view.findViewById(R.id.btn_change_name);

        logOutButton.setOnClickListener(this);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                Glide.with(AccountProfileFragment.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL)).into(photoOfUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userName.setText("YIUUU");
            }
        });

        return view;
    }

    public void onClickLogoutButton()
    {
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onClick(View view)
    {
        if(view.getId()== R.id.log_out_btn)
        {
            onClickLogoutButton();
        }
    }

}
