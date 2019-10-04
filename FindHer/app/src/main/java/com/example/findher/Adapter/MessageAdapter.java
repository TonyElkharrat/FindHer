package com.example.findher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findher.R;
import com.example.findher.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolderMessage>
{
    public static  final int MSG_TYPE_LEFT=0;
    public static  final int MSG_TYPE_RIGHT=1;

    private final Context context;
    private ArrayList<Message> allMessages;

    public MessageAdapter(Context context, ArrayList<Message> allMessages )
    {
        this.context=context;
        this.allMessages = allMessages;
    }


    public class ViewHolderMessage extends RecyclerView.ViewHolder
    {
        private TextView message ;
        private CircleImageView profilPicture;

        public ViewHolderMessage(@NonNull View itemView)
        {
            super(itemView);
            profilPicture = itemView.findViewById(R.id.chat_item_image_profil);
            message = itemView.findViewById(R.id.input_user_textview_chat_item);
        }

    }

    @NonNull
    @Override
    public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;

        if(viewType == MSG_TYPE_RIGHT)
        {
             view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        }

        else
        {
             view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
        }

        return new ViewHolderMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessage holder, int position)
    {
        Message message = allMessages.get(position);
        holder.message.setText(message.getMessage());
    }

    @Override
    public int getItemCount()
    {
        return allMessages.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(allMessages.get(position).getUserSender().equals(user.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}
