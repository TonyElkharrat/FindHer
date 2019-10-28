package com.example.zivug.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.R;
import com.example.zivug.fragments.ChatFragment;
import com.example.zivug.models.User;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.zivug.Activities.ZivugActivity.bottomNavigationView;


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
        ImageView message;
        TextView nameOfContact;
        TextView ageOfContact;
        TextView cityOfContact;
        RecyclerView recyclerView;
        CardView userCardView;
        TextView levelofReligion;
        ProgressBar progressBar;
        SpinKitView animationLoading;

        public ViewHolderContact(@NonNull View itemView)
        {
            super(itemView);
            imageOfContact = itemView.findViewById(R.id.profilePic);
            nameOfContact = itemView.findViewById(R.id.nameOfContact);
            ageOfContact = itemView.findViewById(R.id.age_contact_fragment);
            userCardView = itemView.findViewById(R.id.contact_cardview);
            cityOfContact = itemView.findViewById(R.id.city_user_contact_fragment);
            levelofReligion = itemView.findViewById(R.id.levelOfReligion);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progress_bar);
            animationLoading = itemView.findViewById(R.id.spin_kit);
            message = itemView.findViewById(R.id.message_contact);

            message.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId",allUsers.get(getAdapterPosition()).getuId());
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    bottomNavigationView.setSelectedItemId(R.id.messages_nav);
                    ChatFragment chatFragment = new ChatFragment();
                    chatFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.central_layout,chatFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderContact holder, int position)
    {

        if(!allUsers.get(position).getUrlPicture().isEmpty())
        {holder.animationLoading.setVisibility(View.VISIBLE);
            Picasso.get().load(allUsers.get(position).getUrlPicture()).into(holder.imageOfContact, new Callback() {
                @Override
                public void onSuccess() {
                    holder.animationLoading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        holder.nameOfContact.setText(allUsers.get(position).getUserName());
        holder.ageOfContact.setText(allUsers.get(position).getAgeUser());
        holder.cityOfContact.setText(allUsers.get(position).getLocation().getCityUser());
        holder.levelofReligion.setText(allUsers.get(position).getLevelOfReligion());
    }

    @Override
    public int getItemCount()
    {
        return allUsers.size();
    }
}
