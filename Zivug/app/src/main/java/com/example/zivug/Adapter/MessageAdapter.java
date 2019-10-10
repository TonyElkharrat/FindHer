package com.example.zivug.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zivug.R;
import com.example.zivug.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

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
        private TextView timeMessage ;
        private CircleImageView profilPicture;
        private ImageView isSeen;
        private ImageView imageSent;
        LinearLayout messageLayout;


        public ViewHolderMessage(@NonNull View itemView)
        {
            super(itemView);
            profilPicture = itemView.findViewById(R.id.chat_item_image_profil);
            message = itemView.findViewById(R.id.input_user_textview_chat_item);
            timeMessage = itemView.findViewById(R.id.time_message_chat_item);
            isSeen = itemView.findViewById(R.id.view_message);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            imageSent = itemView.findViewById(R.id.image_sent);
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
        holder.timeMessage.setText(message.getDateCreated()+"");

        if(!allMessages.get(position).getUserReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            if (!allMessages.get(position).getIsRead())
            {
                holder.isSeen.setImageResource(R.drawable.double_check);
            }

            else
            {
                holder.isSeen.setImageResource(R.drawable.double_check_read);
            }
        }

        else
        {
            holder.isSeen.setVisibility(View.INVISIBLE);
        }

        if(message.getType().equals("image"))
        {
            holder.message.setVisibility(View.INVISIBLE);
            Glide.with(context).load(message.getMessage()).into(holder.imageSent);


//            if(!message.getUserSender().equals(FirebaseAuth.getInstance().getCurrentUser()))
//            {
//                context.getResources().getDrawable(R.drawable.ic_message_background_image_receiver);
//                holder.messageLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_message_background_image_receiver));
//            }
//
//            else
//            {
//                holder.messageLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_message_background_image_sender));
//
//            }

        }
        else
        {
            holder.message.setVisibility(View.VISIBLE);
            holder.imageSent.setVisibility(View.GONE);

        }

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
