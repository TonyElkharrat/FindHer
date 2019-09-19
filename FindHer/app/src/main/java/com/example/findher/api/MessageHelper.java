package com.example.findher.api;

import androidx.annotation.NonNull;

import com.example.findher.models.Message;
import com.example.findher.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageHelper
{
    public static Query getAllMessageForChat(String userSenderId,String userReceiverId)
    {
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(userSenderId)
                .collection("messages").document(
                        "RXp6VxXTx4UnXOxPQBvr"
                ).collection("collections_of_message")
                .orderBy("dateCreated")
                .limit(50);

    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, User userSender, String userReceiverID)
    {
        // 1 - Create the Message object
        Message message = new Message(textMessage,userSender);

        // 2 - Store Message to Firestore
        return getChatCollection(FirebaseAuth.getInstance().getUid(),userReceiverID)
                .add(message).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        int a =0;
                    }
                });
    }

    public static CollectionReference getChatCollection(String userID,String userReceiverID)
    {
        return FirebaseFirestore.getInstance().collection("users").document(userID)
                .collection("messages").document(userReceiverID).collection("collections_of_message");
    }
}
