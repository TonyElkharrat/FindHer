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
import com.example.findher.api.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;


public class AccountProfileFragment extends Fragment implements View.OnClickListener, OnFailureListener
{
   private TextView Name;
   private ImageView photo_of_user;
   private Button logOutButton;
   private static final int UPDATE_USERNAME = 30;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.account_profile_fragment,container,false);
        Name = view.findViewById(R.id.nameOfTheUser);
        photo_of_user = view.findViewById(R.id.user_photo);
        logOutButton = view.findViewById(R.id.log_out_btn);
        logOutButton.setOnClickListener(this);
        Button button = view.findViewById(R.id.btn_change_name);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UserHelper.updateUsername("YIUUU",FirebaseAuth.getInstance().getCurrentUser().getUid());
                Name.setText("YIUUU");
            }
        });

        upDateUiWhenCreating();
        return view;

    }

    public void upDateUiWhenCreating()
    {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).apply(RequestOptions.circleCropTransform().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL)).into(photo_of_user);
            }
           String n = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //Name.setText((DocumentSnapshot)UserHelper.getUsersCollection().ge;
        }
    }

    private void updateUsernameInFirebase()
    {


        String username = this.Name.getText().toString();

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            if (!username.isEmpty() &&  !username.equals("no username found"))
            {
                UserHelper.updateUsername(username, FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnFailureListener
                        (this)
                        .addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_USERNAME));
            }
        }
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

    @Override
    public void onFailure(@NonNull Exception e)
    {

    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin)
    {
        return new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                switch (origin)
                {
                    // 8 - Hiding Progress bar after request completed
                    case UPDATE_USERNAME:
                        break;
                }
            }
        };
    }
}
