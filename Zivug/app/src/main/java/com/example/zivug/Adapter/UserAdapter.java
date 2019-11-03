package com.example.zivug.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.R;
import com.example.zivug.fragments.ChatFragment;
import com.example.zivug.models.Message;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViwHolderUser>
{
    private final Context context;
    private ArrayList<User> allPartnerDiscussion;

    public UserAdapter(Context context, ArrayList<User> allPartnerDiscussion )
    {
        this.context=context;
        this.allPartnerDiscussion = allPartnerDiscussion;
    }


    public class ViwHolderUser extends RecyclerView.ViewHolder
    {
        private TextView partnerName;
        private CircleImageView partnerPictureUrl;
        private TextView lastMessage;
        private  TextView timeLastMessage;
        private  ImageView checkLastMessage;
        private  ImageView photoContent;

        public ViwHolderUser(@NonNull View itemView)
        {
            super(itemView);
            partnerPictureUrl= itemView.findViewById(R.id.photo_partner_circleImageView);
            partnerName= itemView.findViewById(R.id.name_partner_Textview);
            lastMessage = itemView.findViewById(R.id.last_message_Textview);
            timeLastMessage = itemView.findViewById(R.id.time_last_message_Textview);
            checkLastMessage = itemView.findViewById(R.id.double_check_discussion_item);
            photoContent = itemView.findViewById(R.id.photo_content);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId",allPartnerDiscussion.get(getAdapterPosition()).getuId());
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    ChatFragment chatFragment = new ChatFragment();
                    chatFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.central_layout,chatFragment).addToBackStack(null).commit();
                }
            });

        }
    }

    @NonNull
    @Override
    public ViwHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.discussion_item,parent,false);
        return new ViwHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViwHolderUser holder, int position)
    {
        holder.partnerName.setText(allPartnerDiscussion.get(position).getUserName());
        setLastMessageInfo(allPartnerDiscussion.get(position).getuId(),holder.lastMessage,holder.timeLastMessage,holder.checkLastMessage,holder.photoContent);
        if(!allPartnerDiscussion.get(position).getUrlPicture().isEmpty())
            Picasso.get().load(allPartnerDiscussion.get(position).getUrlPicture()).into(holder.partnerPictureUrl);
    }

    private void setLastMessageInfo(final String userId, final TextView lastMessage, final TextView timeLastMessage, final ImageView doubleCheckLastMessage,final ImageView photoContent)
    {
        timeLastMessage.setText("");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Message message = snapshot.getValue(Message.class);

                    if (message != null)
                    {
                        if (message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid()) && message.getUserSender().equals(userId) || message.getUserReceiver().equals(userId) && message.getUserSender().equals(FirebaseAuth.getInstance().getUid()))
                        {
                            setMessageInfo(message,lastMessage,doubleCheckLastMessage,photoContent);
                            setTimeInfo(message,timeLastMessage);
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

    private void setMessageInfo(Message message, TextView lastMessage,ImageView doubleCheckLastMessage,ImageView photoContent)
    {

        if(message.getUserSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            if (message.getIsRead())
            {
                doubleCheckLastMessage.setVisibility(View.VISIBLE);
                doubleCheckLastMessage.setImageResource(R.drawable.double_check_read);
            }
            else
            {
                doubleCheckLastMessage.setVisibility(View.VISIBLE);
                doubleCheckLastMessage.setImageResource(R.drawable.double_check);
            }
        }

        else
        {
            doubleCheckLastMessage.setVisibility(View.GONE);
        }

        if(message.getType().equals("text"))
        {
            lastMessage.setText(message.getMessage());
            photoContent.setVisibility(View.GONE);

        }

        else
        {
            lastMessage.setText("Photo");
            photoContent.setVisibility(View.VISIBLE);
        }
    }

    private void setTimeInfo( Message message,TextView lastMessage)
    {
        lastMessage.setText(message.getDateCreated());
    }


    @Override
    public int getItemCount()
    {
        return allPartnerDiscussion.size();
    }
}
