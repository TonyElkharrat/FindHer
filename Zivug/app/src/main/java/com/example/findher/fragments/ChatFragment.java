package com.example.findher.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findher.notifier.TextChangedNotifier;
import com.example.findher.R;
import com.example.findher.Adapter.MessageAdapter;
import com.example.findher.models.Message;
import com.example.findher.models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ObjectStreamException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment implements View.OnClickListener
{
    CircleImageView partnerPicture;
    TextView partnerName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<Message> allMessage;
    RecyclerView recyclerView;
    String userId;
    ValueEventListener listener;
    TextView inputUser;
    TextView statusUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  InitializeControllers(container);
        getIdPartner();
        setStatus();
        readMessages();
        return view;
    }

    private void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userSender",sender);
        hashMap.put("userReceiver",receiver);
        hashMap.put("dateCreated", getTime());
        hashMap.put("message",message);
        hashMap.put("isRead",false);
        reference.child("Chats").push().setValue(hashMap);
    }


    private View InitializeControllers(ViewGroup container)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.chat,container,false);

        partnerPicture = view.findViewById(R.id.pictureOfPartner_chat_layout);
        partnerName = view.findViewById(R.id.nameOfPartner_chat_layout);
        Button sendButton = view.findViewById(R.id.chat_send_button);

        ImageView backButton = view.findViewById(R.id.back_button_to_all_discussion);
        backButton.setOnClickListener(this);
        inputUser = view.findViewById(R.id.chat_message_edit_text);
        statusUser = view.findViewById(R.id.status_Of_user);

        TextChangedNotifier notifier = new TextChangedNotifier(sendButton);
        sendButton.setOnClickListener(this);
        inputUser.addTextChangedListener(notifier);

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private  void getIdPartner()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = getArguments().getString("userId");
    }

    private void setStatus ()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                partnerName.setText(user.getUserName());
                Picasso.get().load(user.getUrlPicture()).into(partnerPicture);
                if(user.getStatus().equals("online"))
                {
                    statusUser.setVisibility(View.VISIBLE);
                }
                else
                {
                    statusUser.setVisibility(View.GONE);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void readMessages()
    {
        allMessage = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                allMessage.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Message message = snapshot.getValue(Message.class);

                    if (message != null)
                    {
                        if (message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid()) && message.getUserSender().equals(userId) || message.getUserReceiver().equals(userId) && message.getUserSender().equals(FirebaseAuth.getInstance().getUid()))
                        {
                            allMessage.add(message);
                        }
                    }

                    MessageAdapter adapter = new MessageAdapter(getContext(), allMessage);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount());
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void makeMessageSeen()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        listener = databaseReference.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);

                    if (message != null)
                    {
                        String s = FirebaseAuth.getInstance().getUid();
                        String u = userId;

                        if (message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid()) && message.getUserSender().equals(userId))
                        {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("isRead",true);
                            snapshot.getRef().updateChildren(hashMap);
                            readMessages();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    private String getTime()
    {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        int minute = rightNow.get(Calendar.MINUTE);
        return  hour+":"+minute;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        makeMessageSeen();
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(listener);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.chat_send_button)
        {
            sendMessage(firebaseUser.getUid(),userId,inputUser.getText().toString());
            inputUser.setText("");
        }

        else if(view.getId() == R.id.back_button_to_all_discussion )
        {
            ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.central_layout, new DiscussionsFragment()).commit();
        }
    }
}
