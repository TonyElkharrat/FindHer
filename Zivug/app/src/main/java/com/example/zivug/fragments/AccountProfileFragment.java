package com.example.zivug.fragments;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.zivug.Adapter.AccountProfilAdapter;
import com.example.zivug.R;
import com.example.zivug.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AccountProfileFragment extends Fragment implements View.OnClickListener
{
    private TextView userName;
    private ImageView photoOfUser;
    private Button logOutButton;
    RecyclerView recyclerView;
    private final int GALLERY_PIC=1;
    private static final int UPDATE_USERNAME = 30;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.account_profile_fragment,container,false);

        userName = view.findViewById(R.id.nameOfTheUser);
        photoOfUser = view.findViewById(R.id.user_photo);
        logOutButton = view.findViewById(R.id.log_out_btn);
        recyclerView = view.findViewById(R.id.recycler_view_account_profil);
        AccountProfilAdapter accountProfilAdapter = new AccountProfilAdapter(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(accountProfilAdapter);
        logOutButton.setOnClickListener(this);
        photoOfUser.setOnClickListener(this);

        changeUserProfilListener();


        return view;
    }

    public void onClickLogoutButton()
    {
        FirebaseAuth.getInstance().signOut();
    }


    private void retreivePictureFromGallery()
    {
        CropImage.activity()
                .start(getContext(), this);
    }

    private  void changeUserProfilListener()
    {
        rootReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                setPhotoProfile(user.getUrlPicture());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {

                try {
                    final Uri imageUri = result.getUri();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    setPhotoProfile(selectedImage);
                    final StorageReference filePath = storageReference.child(FirebaseAuth.getInstance().getUid()+".jpg");
                    filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri)
                                    {
                                        String downloadUrl = uri.toString();
                                        rootReference.child("urlPicture")
                                                .setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if(task.isSuccessful())
                                                {
                                                    int a =0;
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                            else
                            {
                              int c =0;
                            }
                        }
                    });
                }

                catch (FileNotFoundException e)
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
