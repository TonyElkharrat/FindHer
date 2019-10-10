package com.example.zivug.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.zivug.ChatNotification;
import com.example.zivug.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;


public class AuthentificationActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;

    DatabaseReference reference;
    private CoordinatorLayout coordinatorLayout;
    StorageReference storageReference;

    private FirebaseAuth.AuthStateListener mAuthListener;
    public  static final int RC_SIGN_IN = 1;
    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build() );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       mAuth = FirebaseAuth.getInstance();

       mAuthListener = new FirebaseAuth.AuthStateListener()
       {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    Intent intent = new Intent(AuthentificationActivity.this, FindHerActivity.class);

                   storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                    final Intent newIntent = new Intent(AuthentificationActivity.this, ChatNotification.class);
                    startService(newIntent);
                    startActivity(intent);
                }
                else
                {
                    startSignInActivity();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignInActivity()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build())) // SUPPORT GOOGLE
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_launcher_background)
                        .build(),
                RC_SIGN_IN);
    }



}
