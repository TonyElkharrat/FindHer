package com.example.zivug.fragments.intro_fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.zivug.Api.JsonParser;
import com.example.zivug.Api.LocationHelper;
import com.example.zivug.R;
import com.example.zivug.notifier.cityListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LocationFragment extends Fragment implements cityListener
{
    TextView cityUser;
    Button locationButton;
    ImageView nextbutton;
    private static final int REQUEST_LOCATION_CODE = 1;
    String city;
    String longitude;
    String latitude;
    ProgressBar progressBar;
    SpinKitView animationLoading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.intro_location, container, false);

         nextbutton = view.findViewById(R.id.next_button);
         cityUser = view.findViewById(R.id.city_user);
         nextbutton.setVisibility(View.INVISIBLE);

         progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
         animationLoading = view.findViewById(R.id.spin_kit);

        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        animationLoading.setVisibility(View.INVISIBLE);

        JsonParser.SetListener(LocationFragment.this);

        nextbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                updateDatabase();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                GenderFragment genderFragment = new GenderFragment();

                transaction.replace(R.id.central_layout_intro, genderFragment);
                transaction.commit();
            }
        });

        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.animation_inflate);
        locationButton = view.findViewById(R.id.loacte_btn);
        locationButton.setAnimation(scaleUp);

        locationButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view)
            {
                makeLocationRequest();

                int haspermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

                if (haspermission == PackageManager.PERMISSION_GRANTED)
                {
                    animationLoading.setVisibility(View.VISIBLE);
                    LocationHelper.getLocationUser(getContext());

                }
            }
        });

        return view;
    }

    public  void makeLocationRequest()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            int haspermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (haspermission != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }

            else
            {

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if(requestCode==1&& grantResults[0]!= PackageManager.PERMISSION_GRANTED)
        {

        }

        else
        {
            animationLoading.setVisibility(View.VISIBLE);
            LocationHelper.getLocationUser(getContext());

        }

    }

    @Override
    public void getLocation(String city, String latitude, String longitude)
    {
        cityUser.setVisibility(View.VISIBLE);
        nextbutton.setVisibility(View.VISIBLE);
        locationButton.clearAnimation();
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.animation_inflate);
        nextbutton.setAnimation(scaleUp);
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        cityUser.setText(city);
        animationLoading.setVisibility(View.GONE);
    }

    private void updateDatabase(){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Location");
                HashMap locationUser = new HashMap();
                locationUser.put("cityUser",city);
                locationUser.put("latitude",latitude);
                locationUser.put("longitude",longitude);
                reference.updateChildren(locationUser);

    }
}
