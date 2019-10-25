package com.example.zivug.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.zivug.R;
import com.example.zivug.fragments.intro_fragments.WelcomeFragment;
import com.google.firebase.database.FirebaseDatabase;

public class IntroActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_left,R.anim.exit_to_left);
        transaction.replace(R.id.central_layout_intro, new WelcomeFragment());
        transaction.commit();
    }

}
