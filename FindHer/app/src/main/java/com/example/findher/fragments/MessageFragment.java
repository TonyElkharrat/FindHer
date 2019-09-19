package com.example.findher.fragments;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findher.R;
import com.example.findher.RecyclerViewChat.MessageAdapter;
import com.example.findher.api.MessageHelper;
import com.example.findher.api.UserHelper;
import com.example.findher.models.Message;
import com.example.findher.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageFragment extends Fragment implements MessageAdapter.Listener, View.OnClickListener
{
    User modelCurrentUser;
    EditText inputUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.getCurrentUserFromFirestore();
        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.activity_chat,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.activity_mentor_chat_recycler_view);
        MessageAdapter messageAdapter =  new MessageAdapter(generateOptionsForAdapter(MessageHelper.getAllMessageForChat(FirebaseAuth.getInstance().getUid(),"RXp6VxXTx4UnXOxPQBvr")), Glide.with(this), this, FirebaseAuth.getInstance().getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);
        Button imageButton = view.findViewById(R.id.chat_send_button);
        inputUser = view.findViewById(R.id.chat_message_edit_text);
        imageButton.setOnClickListener(this);
        return view;
    }

    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query)
    {
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataChanged()
    {

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.chat_send_button)
        {
            MessageHelper.createMessageForChat(inputUser.getText().toString(),modelCurrentUser,
                            "RXp6VxXTx4UnXOxPQBvr");
        }
    }

    private void getCurrentUserFromFirestore()
    {
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                 modelCurrentUser = documentSnapshot.toObject(User.class);
            }
        });
    }
}
