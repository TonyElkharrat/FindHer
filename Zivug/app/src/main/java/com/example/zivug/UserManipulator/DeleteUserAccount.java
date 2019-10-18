package com.example.zivug.UserManipulator;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.zivug.Api.SnackBarMessage;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteUserAccount implements onDelete
{
    @Override
    public void deleteData(final Activity activity)
    {
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity.getApplicationContext());
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = FirebaseAuth.getInstance().getUid();
        AuthCredential credential = GoogleAuthProvider
                .getCredential(acct.getIdToken(), null);
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {

                                            FirebaseDatabase.getInstance().getReference("Users").child(userId).removeValue();
                                            FirebaseAuth.getInstance().signOut();
                                            activity.finish();
                                        }

                                        else
                                        {
                                            SnackBarMessage.showSnackBar(activity,"Fore your security you need to reauthentificate for making this operation ");

                                        }
                                    }
                                });

                    }
                });
    }
}
