package com.example.zivug.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.zivug.notifier.ChatNotification;
import com.example.zivug.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
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
    protected void onCreate(@Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null)
                {
                    startSignInActivity();
                }

                else
                {
                    Intent intent = new Intent(AuthentificationActivity.this, ZivugActivity.class);
                    storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                    final Intent newIntent = new Intent(AuthentificationActivity.this, ChatNotification.class);
                    startService(newIntent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            createUser();
        }

    }

    private void startSignInActivity()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build())) // SUPPORT GOOGLE
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.hello)
                        .build(),
                RC_SIGN_IN);
    }

    private void createUser()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","online");
        hashMap.put("uId",FirebaseAuth.getInstance().getUid());
        hashMap.put("urlPicture",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        hashMap.put("userName",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(AuthentificationActivity.this, IntroActivity.class);
                    storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
