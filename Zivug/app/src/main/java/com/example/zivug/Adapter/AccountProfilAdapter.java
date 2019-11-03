package com.example.zivug.Adapter;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zivug.R;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.provider.Settings.System.getString;

public class AccountProfilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static String byIdName(Context context, String name) {
        Resources res = context.getResources();
        return res.getString(res.getIdentifier(name, "string", context.getPackageName()));
    }
    @Override
    public int getItemViewType(int position)
    {
        if(position ==0)
            return 0;
        else return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        RecyclerView.ViewHolder accountViewHolder = null;
        View view;

        switch (viewType)
        {
            case 0:  view = LayoutInflater.from(context).inflate(R.layout.cardview_info_user, parent, false);
                accountViewHolder = new AccountViewHolder(view);
                break;

            case 1 :view = LayoutInflater.from(context).inflate(R.layout.cardview_info_account, parent, false);
                accountViewHolder = new AccountViewHolder2(view);
                break;
        }
        return accountViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position)
    {
        switch (holder.getItemViewType())
        {
            case 0:
                final AccountViewHolder acccountholder1 = (AccountViewHolder) holder;
                //String s =FirebaseAuth.getInstance().getCurrentUser().getUid();

                acccountholder1.email.setText(acccountholder1.email.getText()+FirebaseAuth.getInstance().getCurrentUser().getEmail());


                databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        acccountholder1.levelReligion.setText("Level of Religion :"+dataSnapshot.child("levelOfReligion").getValue().toString());

                        acccountholder1.age.setText("Age : "+dataSnapshot.child("ageUser").getValue().toString());




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;
            case 1:
                AccountViewHolder2 acccountholder2 = (AccountViewHolder2) holder;
                break;

        }
    }

    public AccountProfilAdapter(Context context)
    {
        this.context = context;
    }


    @Override
    public int getItemCount()
    {
        return 2;
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder
    {
        private TextView email;
        private TextView age;
        private TextView levelReligion;



        public AccountViewHolder(@NonNull View itemView)
        {
            super(itemView);

            email = itemView.findViewById(R.id.email_cardView);
            levelReligion = itemView.findViewById(R.id.level_Of_Religion_cardview);
            age = itemView.findViewById(R.id.age_cardView);
        }
    }

    public class AccountViewHolder2 extends RecyclerView.ViewHolder
    {

        private TextView since;
        private TextView account;

        public AccountViewHolder2(@NonNull View itemView)
        {
            super(itemView);
            since = itemView.findViewById(R.id.memberSince_cardview);
        }
    }


}