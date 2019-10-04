package com.example.findher.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findher.R;
import com.example.findher.fragments.ChatFragment;
import com.example.findher.models.User;
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

        public ViwHolderUser(@NonNull View itemView)
        {
            super(itemView);
           partnerPictureUrl= itemView.findViewById(R.id.photo_partner_circleImageView);
           partnerName= itemView.findViewById(R.id.name_partner_Textview);
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
       Picasso.get().load(allPartnerDiscussion.get(position).getUrlPicture()).into(holder.partnerPictureUrl);
    }

    @Override
    public int getItemCount()
    {
       return allPartnerDiscussion.size();
    }
}
