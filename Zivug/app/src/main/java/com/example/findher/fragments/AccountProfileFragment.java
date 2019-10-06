package com.example.findher.fragments;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AccountProfileFragment extends Fragment implements View.OnClickListener
{
    private TextView userName;
    private ImageView photoOfUser;
    private Button logOutButton;
    private final int GALLERY_PIC=1;
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
        photoOfUser.setOnClickListener(this);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                setPhotoProfile(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());

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


    private void retreivePictureFromGallery()
    {
//        Intent galleryIntent = new Intent();
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent,GALLERY_PIC);
        CropImage.activity()
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PIC && resultCode ==RESULT_OK && data!=null)
        {
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());
        }

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(resultUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    setPhotoProfile(selectedImage);

                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    private void setPhotoProfile(Object photoSelected)
    {
        Glide.with(AccountProfileFragment.this).load(photoSelected)
                .apply(RequestOptions.circleCropTransform().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL)).into(photoOfUser);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()== R.id.log_out_btn)
        {
            onClickLogoutButton();
        }

        else if(view.getId() == R.id.user_photo)
        {
            retreivePictureFromGallery();
        }
    }

}
