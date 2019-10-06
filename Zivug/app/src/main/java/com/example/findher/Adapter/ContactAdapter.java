package com.example.findher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findher.R;
import com.example.findher.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ViewHolderContact>
{
    ArrayList<User> allUsers;
    Context context;
    public interface onuserClickListener
    {
        void OnUserClick(String userID);
    }

    static onuserClickListener listener;

    public static void SetListener(onuserClickListener i_listener)
    {
        listener = i_listener;
    }

    public ContactAdapter(ArrayList<User> allUsers, Context context)
    {
        this.allUsers = allUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);
        return new ViewHolderContact(view);
    }

    public class ViewHolderContact extends RecyclerView.ViewHolder
    {
        ImageView imageOfContact;
        TextView nameOfContact;
        RecyclerView recyclerView;
        CardView userCardView;

        public ViewHolderContact(@NonNull View itemView)
        {
            super(itemView);
            imageOfContact = itemView.findViewById(R.id.profilePic);
            nameOfContact = itemView.findViewById(R.id.nameOfContact);
            userCardView = itemView.findViewById(R.id.contact_cardview);

            userCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    listener.OnUserClick(allUsers.get(getAdapterPosition()).getuId());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContact holder, int position)
    {
        Picasso.get().load(allUsers.get(position).getUrlPicture()).into(holder.imageOfContact);
        holder.nameOfContact.setText(allUsers.get(position).getUserName());
    }

    @Override
    public int getItemCount()
    {
        return allUsers.size();
    }
}
