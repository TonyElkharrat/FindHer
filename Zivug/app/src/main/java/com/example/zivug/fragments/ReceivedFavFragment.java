package com.example.zivug.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.Adapter.ContactAdapter;
import com.example.zivug.R;
import com.example.zivug.models.Favorites;
import com.example.zivug.models.User;
import com.example.zivug.notifier.loadDataNotifier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReceivedFavFragment extends Fragment implements loadDataNotifier
{
 RecyclerView recyclerView= null;
 private View RequestFragmentView;
 private RecyclerView myRequestList;
 private DatabaseReference ChatreqRed;
 private FirebaseAuth mAuth;
    private String currentUserID;
    private static String SenderUserID,ReciverUserID;


    public ReceivedFavFragment(){


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_request, container, false);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ChatreqRed= FirebaseDatabase.getInstance().getReference().child("Favorites");
        SenderUserID = mAuth.getCurrentUser().getUid();

        Initialize(view);
        loadData();
        return view;
    }
    private  void Initialize(View view)
    {
        recyclerView = view.findViewById(R.id.chat_request_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


    }

    public void loadData() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        final ArrayList<User>  ReceiveArr = new ArrayList<>();

        final ArrayList<String>  list2 = new ArrayList<>();


        databaseReference.child("Favorites").addValueEventListener(new ValueEventListener(){

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {


            ReceiveArr.clear();
            for (DataSnapshot snapshot: dataSnapshot.getChildren() )
            {
                list2.add(snapshot.getValue(Favorites.class).getSenderUserID());
            }
            Log.d("MyApp", list2+"");

            databaseReference.child("Users").addValueEventListener(new ValueEventListener()
            {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    ReceiveArr.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren() )
                    {
                        User user = snapshot.getValue(User.class);
                        ReciverUserID = user.getuId();
                        list2.add(snapshot.getValue(Favorites.class).toString());
                        if (list2.contains(ReciverUserID)&& (!ReciverUserID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){
                            ReceiveArr.add(user);

                        }

                    }

                    ContactAdapter contactAdapter = new ContactAdapter(ReceiveArr, getContext());
                    recyclerView.setAdapter(contactAdapter);
                }


                    @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }




//    public static class RequestViewHolder extends RecyclerView.ViewHolder {
//
//
//        public RequestViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }

}


