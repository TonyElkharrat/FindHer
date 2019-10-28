package com.example.zivug.fragments;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.Adapter.AccountProfilAdapter;
import com.example.zivug.Adapter.AllPhotoUserAdapter;
import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.Api.BitmapCompresser;
import com.example.zivug.Api.FileHelper;
import com.example.zivug.Api.SnackBarMessage;
import com.example.zivug.Api.UploadManager;
import com.example.zivug.Api.WifiManager;
import com.example.zivug.R;
import com.example.zivug.UserManipulator.onDelete;
import com.example.zivug.models.NumberOfPhotos;
import com.example.zivug.models.User;

import com.example.zivug.notifier.WifiBroadCastReceiver;
import com.example.zivug.notifier.onUploadPhotoNotifier;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AccountProfileFragment extends Fragment implements View.OnClickListener, onUploadPhotoNotifier
{
    private TextView userName;
    private ImageButton uploadButton;
    private CircleImageView photoOfUser;
    private Button logOutButton;
    RecyclerView recyclerView;
    ImageView settingsButton;
    private final int GALLERY_PIC=1;
    private final int UPLOAD_PHOTO=1;
    ProgressBar progressBar;
    SpinKitView animationLoading;
    private static final int UPDATE_USERNAME = 30;
    ArrayList<String> allPicturesPhotoOfUser = new ArrayList<>();
    RecyclerView photoRecyclerview;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    Uri photoToUpload;
    public static int m_numberOfPhotos;


    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.account_profile_fragment,container,false);
        UploadManager.setListener(this);
        WifiBroadCastReceiver.setListener(this);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        animationLoading = view.findViewById(R.id.spin_kit);
        photoRecyclerview = view.findViewById(R.id.recycler_photos);
        Sprite doubleBounce = new Circle();
        pref = getContext().getSharedPreferences("photoToSend", Context.MODE_PRIVATE);;
        editor = pref.edit();
        WifiBroadCastReceiver wifiBroadCastReceiver = new WifiBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(android.net.wifi.WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        getContext().registerReceiver(wifiBroadCastReceiver, intentFilter);
        progressBar.setIndeterminateDrawable(doubleBounce);
        uploadButton = view.findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(this);
        userName = view.findViewById(R.id.nameOfTheUser);
        photoOfUser = view.findViewById(R.id.user_photo);
        logOutButton = view.findViewById(R.id.log_out_btn);
        settingsButton = view.findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_view_account_profil);
        AccountProfilAdapter accountProfilAdapter = new AccountProfilAdapter(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(accountProfilAdapter);
        logOutButton.setOnClickListener(this);
        photoOfUser.setOnClickListener(this);
        changeUserProfilListener();
        retreiveAllPhotos();


        return view;
    }



    public void onClickLogoutButton()
    {
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }


    private void retreivePictureFromGalleryWithCrop()
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
                setPhotoProfile(Uri.parse(user.getUrlPicture()));

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
            final Uri imageUri = result.getUri();
            byte[] imagecompressed = BitmapCompresser.getCompressImage(getContext(),imageUri);

            if (resultCode == RESULT_OK)
            {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                StorageReference filePath = storageReference.child(FirebaseAuth.getInstance().getUid()+"jpg");

                     uploadPhotoProfil(imageUri,filePath);

                }

        }

        else if (requestCode==UPLOAD_PHOTO)
        {
            StorageReference storageUserphotoReference = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid());
            m_numberOfPhotos++;
            StorageReference filePath = storageUserphotoReference.child(m_numberOfPhotos + ".jpg");
            if(data.getData()!=null)
            photoToUpload = data.getData();
            boolean connectedToWifi = WifiManager.checkWifiOnAndConnected(getContext());

            if(connectedToWifi)
            {
                if(data!=null)
                {
                    UploadManager.uploadPhoto(getActivity(),photoToUpload, filePath);
                    UploadManager.updateNumberOfPhoto(m_numberOfPhotos);

                }
            }

            else
            {
                showDialog("You are not connected to Wi-Fi,  Do you want to upload the image now or that we will send for you when you will be connected ?",filePath);
            }

        }
    }

    private void setPhotoProfile(Uri uri)
    {
        animationLoading.setVisibility(View.VISIBLE);
        Picasso.get().load(uri)
                .into(photoOfUser, new Callback() {
                    @Override
                    public void onSuccess() {
                        animationLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    private void setProfilePictureOnServer(StorageReference finalFilePath)
    {
        animationLoading.setVisibility(View.VISIBLE);
        finalFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri)
            {
                String downloadUrl = uri.toString();
                rootReference.child("urlPicture").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                         AnimationMaker.updateUI(getActivity(),task);
                        }
                    }
                });
            }
        });
    }

    private void uploadPhotoProfil(final Uri uri, final StorageReference filePath)
    {

        animationLoading.setVisibility(View.VISIBLE);
        filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                setProfilePictureOnServer(filePath);
                AnimationMaker.updateUI(getActivity(),task);
            }
        });
    }



    private void retreiveAllPhotos()
    {


        final StorageReference reference = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid());
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("numberOfPhotoUser").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    NumberOfPhotos numberOfPhotos = dataSnapshot1.getValue(NumberOfPhotos.class);
                    m_numberOfPhotos = Integer.parseInt(numberOfPhotos.getNumberOfPhotos());
                    allPicturesPhotoOfUser.clear();
                    recyclerView.getRecycledViewPool().clear();

                    for (int i = 1; i <= m_numberOfPhotos; i++) {
                        StorageReference finalFilePath = reference.child(i + ".jpg");

                        finalFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri)
                            {
                                String downloadUrl = uri.toString();
                                allPicturesPhotoOfUser.add(downloadUrl);
                                displayPhotos();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void displayPhotos()
    {
        AllPhotoUserAdapter allPhotoUserAdapter = new AllPhotoUserAdapter(getContext(),allPicturesPhotoOfUser);
        photoRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        photoRecyclerview.setAdapter(allPhotoUserAdapter);
        allPhotoUserAdapter.notifyDataSetChanged();
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
            retreivePictureFromGalleryWithCrop();
        }

        else if(view.getId() == R.id.settings_btn)
        {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.central_layout,new SettingsFragment()).addToBackStack(null).commit();
        }

        else if(view.getId() == R.id.uploadButton)
        {

               Intent intent = FileHelper.getFilefromMemory();
               startActivityForResult(intent, UPLOAD_PHOTO);


        }
    }

    private void showDialog(String textToShow , final StorageReference filePath)
    {
        final Dialog infoDialog = new Dialog(getContext());
        infoDialog.setContentView(R.layout.upload_image_network_status_dialog);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView yesButton = infoDialog.findViewById(R.id.YesButton);
        TextView noButton = infoDialog.findViewById(R.id.CancelButton);
        TextView text = infoDialog.findViewById(R.id.text_dialog);
        text.setText(textToShow);
        infoDialog.show();

        yesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                UploadManager.uploadPhoto(getActivity(),photoToUpload, filePath);
                infoDialog.dismiss();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editor.putString("photoSaved",photoToUpload.toString());
                editor.commit();
                infoDialog.dismiss();
            }
        });
    }


    @Override
    public void onUploadPhoto()
    {
        retreiveAllPhotos();
    }
}
