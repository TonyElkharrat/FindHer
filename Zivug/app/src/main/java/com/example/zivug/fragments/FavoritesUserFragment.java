package com.example.zivug.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.Adapter.FavoritesAdapter;
import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.R;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesUserFragment extends Fragment
{
    private ArrayList<User> allfavorites = new ArrayList<>();
    RecyclerView recyclerView ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_contacts);
        getAllFavorites();
        return view;
    }

    public void getAllFavorites()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("FavoritesUsers").child(FirebaseAuth.getInstance().getUid());
                database.addValueEventListener(new ValueEventListener()
                {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren() )
                {
                    User user = dataSnapshot1.getValue(User.class);
                    allfavorites.add(user);
                }
                FavoritesAdapter favoritesAdapter = new FavoritesAdapter(allfavorites,getContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(favoritesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
