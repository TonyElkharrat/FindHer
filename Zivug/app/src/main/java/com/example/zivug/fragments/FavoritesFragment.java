package com.example.zivug.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.Adapter.UserAdapter;
import com.example.zivug.R;
import com.example.zivug.models.Message;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesFragment extends Fragment
{

        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private ArrayList<User> allUsers;
        private Set<String> allUsersDiscussion = new HashSet<String>();

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            final View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.discussion_list,container,false);
            recyclerView = view.findViewById(R.id.recyclerView_all_discussion);
            DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.list_divider));
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).margin(180,50).build());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            allUsers = new ArrayList<>();
            loadUsers();
            return view;
        }

        private void loadUsers()
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

            ValueEventListener listener = databaseReference.addValueEventListener(new ValueEventListener()
            {

                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {



                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Message message = snapshot.getValue(Message.class);

                        if (message != null)
                        {

                            if (!message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid())&& message.getUserSender().equals(FirebaseAuth.getInstance().getUid()))
                            {
                                allUsersDiscussion.add(message.getUserReceiver());
                            }
                            else if(message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid())&& !message.getUserSender().equals(FirebaseAuth.getInstance().getUid()))
                            {
                                allUsersDiscussion.add(message.getUserSender());

                            }
                        }
                    }
                    readUsers();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        private void readUsers()
        {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

            reference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    allUsers.clear();
                    for (DataSnapshot snapshot :dataSnapshot.getChildren())
                    {
                        User user = snapshot.getValue(User.class);

                        for (String userDiscussion: allUsersDiscussion )
                        {
                            if(!user.getuId().equals(firebaseUser.getUid())&&user.getuId().equals(userDiscussion))
                            {
                                allUsers.add(user);
                            }
                        }

                    }

                    userAdapter = new UserAdapter(getContext(),allUsers);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }

            });
        }
    }


