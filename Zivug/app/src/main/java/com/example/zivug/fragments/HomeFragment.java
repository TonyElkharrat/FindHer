package com.example.zivug.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zivug.Adapter.ContactAdapter;
import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.Api.LocationHelper;
import com.example.zivug.R;
import com.example.zivug.models.Location;
import com.example.zivug.models.User;
import com.example.zivug.notifier.loadDataNotifier;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;
import java.util.EventListener;


public class HomeFragment extends Fragment implements loadDataNotifier
{
    TextView cityTV;
    RecyclerView recyclerView= null;
    SparkButton searchButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_fragment, container, false);

        Initialize(view);
        loadData();
        AnimationMaker.makeLoadingAnimation(getActivity(),view,R.id.swipeContainer);
        AnimationMaker.SetListener(this);
        return view;
    }

    private  void Initialize(View view)
    {
        recyclerView = view.findViewById(R.id.recycler_view_contacts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        searchButton = (SparkButton) view.findViewById(R.id.spark_button);

        searchButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState)
            {

            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState)
            {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.central_layout, new SearchFragment()).commit();
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }

    @Override
    public void loadData()
    {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayList<User>  allUsers = new ArrayList<>();
        final User[] Firebaseuser = new User[1];
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Firebaseuser[0] = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Users").addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                allUsers.clear();


                for (DataSnapshot snapshot: dataSnapshot.getChildren() )
                {
                    User user = snapshot.getValue(User.class);

                    if(!user.getuId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())&&!Firebaseuser[0].getGender().equals(user.getGender()))
                    {


                        if (getArguments() != null)
                        {
                            int minimumAge = getArguments().getInt("minimumAge");
                            int maximumAge = getArguments().getInt("maximumAge");
                            int radiusResearch = getArguments().getInt("radiusResearch");
                            ArrayList<String> params = getArguments().getStringArrayList("levelOfReligion");
                            Location location = user.getLocation();

                            for(int i=0;i<params.size();i++)
                            {
                                String s = params.get(i);
                                if (user.getLevelOfReligion().equals(s)&&Integer.valueOf(user.getAgeUser()) >= minimumAge && Integer.valueOf(user.getAgeUser()) <= maximumAge && LocationHelper.isCityInRadius(radiusResearch * 1000, Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()))) {
                                    allUsers.add(user);
                                }
                            }
                        }

                        else
                        {
                            allUsers.add(user);
                        }
                    }
                }

                ContactAdapter contactAdapter = new ContactAdapter(allUsers,getContext());
                recyclerView.setAdapter(contactAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }



}

