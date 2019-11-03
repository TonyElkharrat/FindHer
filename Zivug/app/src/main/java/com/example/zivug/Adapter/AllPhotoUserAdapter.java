package com.example.zivug.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zivug.R;
import com.example.zivug.fragments.ChatFragment;
import com.example.zivug.models.Message;
import com.example.zivug.models.User;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllPhotoUserAdapter extends RecyclerView.Adapter<AllPhotoUserAdapter.ViewHolderAllPhotoOFUser>
{

    private final Context context;
    private ArrayList<String> allPhotos;
    ProgressBar progressBar;
    SpinKitView animationLoading;
    public AllPhotoUserAdapter(Context context, ArrayList<String> allPhotos )
    {
        this.context=context;
        this.allPhotos = allPhotos;
    }


    public class ViewHolderAllPhotoOFUser extends RecyclerView.ViewHolder
    {
        ImageView photo;

        public ViewHolderAllPhotoOFUser(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo_item_user);
            animationLoading = itemView.findViewById(R.id.spin_kit);
        }
    }

    @NonNull
    @Override
    public AllPhotoUserAdapter.ViewHolderAllPhotoOFUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item,parent,false);
        return new AllPhotoUserAdapter.ViewHolderAllPhotoOFUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAllPhotoOFUser holder, int position)
    {

        Picasso.get().load(Uri.parse(allPhotos.get(position))).into(holder.photo);
    }



    @Override
    public int getItemCount()
    {
        return allPhotos.size();
    }
}
