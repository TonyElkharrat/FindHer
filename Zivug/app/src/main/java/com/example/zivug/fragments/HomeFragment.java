package com.example.zivug.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.zivug.Adapter.ContactAdapter;
import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.Api.JsonParser;
import com.example.zivug.Api.LocationHelper;
import com.example.zivug.R;
import com.example.zivug.models.User;
import com.example.zivug.notifier.cityListener;
import com.example.zivug.notifier.loadDataNotifier;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements loadDataNotifier
{
     TextView cityTV;
     RecyclerView recyclerView= null;
     SparkButton finalButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_fragment, container, false);

        Initialize(view);
        LocationHelper.getCityUser(getContext());
        loadData();
        AnimationMaker.makeLoadingAnimation(getActivity(),view,R.id.swipeContainer);
        AnimationMaker.SetListener(this);

        boolean isWithin = LocationHelper.isCityInRadius(50*1000,34.781769,32.085300);

        JsonParser.SetListener(new cityListener() {
            @Override
            public void getCity(String city) {
                cityTV.setText(city);
            }
        });


        finalButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               finalButton.setInactiveImage(R.drawable.ic_loupe);
               finalButton.playAnimation();
           }
       });

        return view;
    }

    private  void Initialize(View view)
    {
        recyclerView = view.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        cityTV = view.findViewById(R.id.city_user_esai);
        finalButton = view.findViewById(R.id.spark_button);
        finalButton = new SparkButtonBuilder(getContext())
                .setActiveImage(R.drawable.ic_loupe)
                .setPrimaryColor(ContextCompat.getColor(getContext(), R.color.salmon))
                .setSecondaryColor(ContextCompat.getColor(getContext(), R.color.blueClear))
                .build();
    }





    @Override
    public void loadData()
    {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayList<User>  allUsers = new ArrayList<>();

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                allUsers.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren() )
                {
                    User user = snapshot.getValue(User.class);
                    if(!user.getuId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        allUsers.add(user);
                    }
                }

                ContactAdapter contactAdapter = new ContactAdapter(allUsers,getContext());
                recyclerView.setAdapter(contactAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }

