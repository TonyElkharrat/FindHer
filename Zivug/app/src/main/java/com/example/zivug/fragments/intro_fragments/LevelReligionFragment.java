package com.example.zivug.fragments.intro_fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.zivug.Activities.ZivugActivity;
import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LevelReligionFragment extends Fragment implements View.OnClickListener
{
    ImageView nextbutton;
    String levelOfReligion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.intro_level_religion, container, false);
        nextbutton = view.findViewById(R.id.next_button);
        nextbutton.setOnClickListener(this);
        Button manLevel1 = view.findViewById(R.id.level1);
        Button manLevel2 = view.findViewById(R.id.level2);
        Button manLevel3 = view.findViewById(R.id.level3);
        manLevel1.setOnClickListener(this);
        manLevel2.setOnClickListener(this);
        manLevel3.setOnClickListener(this);
        nextbutton.setVisibility(View.INVISIBLE);
        return view;
    }


    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.next_button)
        {
            updateDatabase();
            Intent intent = new Intent(getActivity(), ZivugActivity.class);
            startActivity(intent );
            getActivity().finishAffinity();

        }

        else if(view.getId() == R.id.level1)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
            levelOfReligion = "Normal";
        }

        else if(view.getId() == R.id.level2)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
            levelOfReligion = "Traditionalist";

        }

        else if(view.getId() == R.id.level3)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
            levelOfReligion = "Advanced";

        }

    }

    private  void updateDatabase()
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap genderOfUser = new HashMap();
        genderOfUser.put("levelOfReligion",levelOfReligion);
        reference.updateChildren(genderOfUser);
    }
}
