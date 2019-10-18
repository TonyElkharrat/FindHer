package com.example.zivug.fragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zivug.Api.FileHelper;
import com.example.zivug.Api.ImageHelper;
import com.example.zivug.Api.TimeHelper;
import com.example.zivug.notifier.TextChangedNotifier;
import com.example.zivug.R;
import com.example.zivug.Adapter.MessageAdapter;
import com.example.zivug.models.Message;
import com.example.zivug.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ChatFragment extends Fragment implements View.OnClickListener
{
    private CircleImageView partnerPicture;
    TextView partnerName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<Message> allMessage;
    RecyclerView recyclerView;
    String userId;
    ValueEventListener listener;
    TextView inputUser;
    TextView statusUser;
    ImageView addFileButton;
    int GALLERY_REQUEST=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  InitializeControllers(container);

        getIdPartner();
        setStatus();
        makeMessageSeen();
        readMessages();
        return view;
    }

    private void sendMessage(String sender,String receiver,String message, String type,String child)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userSender",sender);
        hashMap.put("userReceiver",receiver);
        hashMap.put("dateCreated", TimeHelper.getTime());
        hashMap.put("message",message);
        hashMap.put("isRead",false);
        hashMap.put("type",type);
        reference.child("Chats").child(child).setValue(hashMap);
    }


    private View InitializeControllers(ViewGroup container)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.chat,container,false);

        partnerPicture = view.findViewById(R.id.pictureOfPartner_chat_layout);
        partnerName = view.findViewById(R.id.nameOfPartner_chat_layout);
        Button sendButton = view.findViewById(R.id.chat_send_button);

        addFileButton = view.findViewById(R.id.chat_add_file_button);
        addFileButton.setOnClickListener(this);

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

                    statusUser.setText(user.getStatus());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void readMessages()
    {

               MessageAdapter adapter = new MessageAdapter(getContext(), allMessage);
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                recyclerView.setAdapter(adapter);

    }

    private void makeMessageSeen()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        allMessage = new ArrayList<>();
        listener = databaseReference.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                        allMessage.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);

                            if (message != null) {

                                if (message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid()) && message.getUserSender().equals(userId)) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    if(message.getIsRead()==false&& !message.getUserSender().equals(FirebaseAuth.getInstance().getUid()))
                                    {
                                        hashMap.put("isRead", true);
                                        snapshot.getRef().updateChildren(hashMap);
                                    }


                                }
                                allMessage.add(message);
                                readMessages();
                            }
                        }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK&& data!=null && data.getData()!=null)
        {
           byte[] fileInBytes = ImageHelper.getCompressImage(getContext(),data.getData());
           uploadImage(fileInBytes);

        }
    }


    private  void uploadImage(byte[] fileInBytes)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final String messageId = databaseReference.push().getKey();
        final StorageReference filePath = storageReference.child(messageId+".jpg");

        StorageTask uploadTask = filePath.putBytes(fileInBytes);

        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception
            {
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Uri downloadUrl =  task.getResult();
                    sendMessage(FirebaseAuth.getInstance().getUid(),userId,downloadUrl.toString(),"image",messageId);
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.chat_send_button)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            sendMessage(firebaseUser.getUid(),userId,inputUser.getText().toString(),"text",databaseReference.push().getKey());
            inputUser.setText("");
        }

        else if(view.getId() == R.id.back_button_to_all_discussion )
        {
            ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.central_layout, new DiscussionsFragment()).commit();
        }

        else if(view.getId() == R.id.chat_add_file_button)
        {
            Intent intent = FileHelper.getFilefromMemory();
            startActivityForResult(Intent.createChooser(intent,"Select Image"),GALLERY_REQUEST);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        makeMessageSeen();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        databaseReference.removeEventListener(listener);
    }

}
