package com.example.findher.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findher.notifier.TextChangedNotifier;
import com.example.findher.R;
import com.example.findher.RecyclerViewChat.MessageAdapter;
import com.example.findher.models.Message;
import com.example.findher.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment
{
    CircleImageView partnerPicture;
    TextView partnerName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<Message> allMessage;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.chat,container,false);

        partnerPicture = view.findViewById(R.id.pictureOfPartner_chat_layout);
        partnerName = view.findViewById(R.id.nameOfPartner_chat_layout);
        Button sendButton = view.findViewById(R.id.chat_send_button);
        final TextView inputUser = view.findViewById(R.id.chat_message_edit_text);
        final TextView statusUser = view.findViewById(R.id.status_Of_user);

        TextChangedNotifier notifier = new TextChangedNotifier(sendButton);
        inputUser.addTextChangedListener(notifier);

         recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = getArguments().getString("userId");
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessage(firebaseUser.getUid(),userId,inputUser.getText().toString());
                inputUser.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                partnerName.setText(user.getUserName());
                Picasso.get().load(user.getUrlPicture()).into(partnerPicture);
                readMessages(FirebaseAuth.getInstance().getUid(),userId,user.getUrlPicture());
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

        return view;
    }

    private void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userSender",sender);
        hashMap.put("userReceiver",receiver);
        hashMap.put("dateCreated", ServerValue.TIMESTAMP);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String myid, final String partnerId, String imageUrl)
    {
        allMessage = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                allMessage.clear();
                for (DataSnapshot snapchot: dataSnapshot.getChildren()) {
                    Message message = snapchot.getValue(Message.class);

                    if (message != null)
                    {
                        if (message.getUserReceiver().equals(myid) && message.getUserSender().equals(partnerId) || message.getUserReceiver().equals(partnerId) && message.getUserSender().equals(myid))
                        {
                            allMessage.add(message);
                        }
                    }

                    MessageAdapter adapter = new MessageAdapter(getContext(), allMessage);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
