package com.example.zivug.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.Adapter.AccountProfilAdapter;
import com.example.zivug.Adapter.ContactAdapter;
import com.example.zivug.R;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;

import java.util.ArrayList;


public class HomeFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_fragment,container,false);


        SparkButton button = new SparkButtonBuilder(getContext())
                .setActiveImage(R.drawable.ic_loupe)
                .setPrimaryColor(ContextCompat.getColor(getContext(), R.color.salmon))
                .setSecondaryColor(ContextCompat.getColor(getContext(), R.color.blueClear))
                .build();

        final RecyclerView recyclerView =  view.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

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

        button = view.findViewById(R.id.spark_button);
        final SparkButton finalButton = button;
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               finalButton.setInactiveImage(R.drawable.ic_loupe);
               finalButton.playAnimation();
           }
       });



        return view;
    }
}
