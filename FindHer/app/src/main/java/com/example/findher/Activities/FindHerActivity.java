package com.example.findher.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.findher.R;
import com.example.findher.Adapter.ContactAdapter;
import com.example.findher.fragments.AccountProfileFragment;
import com.example.findher.fragments.DiscussionsFragment;
import com.example.findher.fragments.ChatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FindHerActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
        {
            Fragment selectedFragment = null;


            switch (menuItem.getItemId())
            {
                case R.id.profile_nav :

                    selectedFragment = new AccountProfileFragment();
                    break;
                case R.id.home_nav:
                    break;
                    case R.id.messages_nav:
                        selectedFragment = new DiscussionsFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.central_layout,selectedFragment).addToBackStack(null).commit();

            return  true;
        }
    };

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount()>1)
        {
            for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount()+1; ++i)
                getSupportFragmentManager().popBackStack();
        }

        else
        {
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.home_nav);

    }


    @Override
    public void onResume() {
        super.onResume();
        setStatus("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        setStatus("offline");
    }

    private void setStatus(String status)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);
    }

}
