package com.example.findher;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.findher.fragments.AccountProfileFragment;
import com.example.findher.fragments.MessageFragment;
import com.example.findher.fragments.PotentialPartnersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                    selectedFragment = new PotentialPartnersFragment();
                    case R.id.messages_nav:
                    selectedFragment = new MessageFragment();
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

}
