package com.example.zivug.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zivug.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AccountProfilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context ;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        switch (holder.getItemViewType())
        {
            case 0:
                AccountViewHolder acccountholder1 = (AccountViewHolder) holder;
                acccountholder1.email.setText(acccountholder1.email.getText()+FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
        }
    }

    public class AccountViewHolder2 extends RecyclerView.ViewHolder
    {

        public AccountViewHolder2(@NonNull View itemView)
        {
            super(itemView);

        }
    }


}
