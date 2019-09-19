package com.example.findher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.findher.api.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity //implements OnFailureListener
{
    private FirebaseAuth mAuth;

    private CoordinatorLayout coordinatorLayout;

    private FirebaseAuth.AuthStateListener mAuthListener;
    public  static final int RC_SIGN_IN = 1;
    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build() );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mAuth = FirebaseAuth.getInstance();
       mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    Intent intent = new Intent(MainActivity.this,FindHerActivity.class);
                    startActivity(intent);
                }
                else
                {
                    createUserInFirestore();
                    startSignInActivity();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
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

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data)
    {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN)
        {
            if (resultCode == RESULT_OK)
            { // SUCCESS
               Intent intent = new Intent(MainActivity.this,FindHerActivity.class);
               startActivity(intent);
            }
        }
    }

    private void createUserInFirestore()
    {

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {

            String urlPicture = (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) ? FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString() : null;
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserHelper.createUser(username, uid, urlPicture);
        }
    }

//    @Override
//    public void onFailure(@NonNull Exception e)
//    {
//
//    }
}
