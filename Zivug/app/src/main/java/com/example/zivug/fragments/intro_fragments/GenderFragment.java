package com.example.zivug.fragments.intro_fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GenderFragment extends Fragment implements View.OnClickListener
{
    ImageView nextbutton;
    String gender;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.intro_gender, container, false);
        nextbutton = view.findViewById(R.id.next_button);
        nextbutton.setOnClickListener(this);
        Button manButton = view.findViewById(R.id.man_Btn);
        Button womanButton = view.findViewById(R.id.woman_Btn);
        manButton.setOnClickListener(this);
        womanButton.setOnClickListener(this);
        nextbutton.setVisibility(View.INVISIBLE);
        return view;
    }


    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.next_button)
        {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            updateDatabase();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
            LevelReligionFragment levelReligionFragment = new LevelReligionFragment();
            transaction.replace(R.id.central_layout_intro,levelReligionFragment);
            transaction.commit();
        }

        else if(view.getId() == R.id.man_Btn)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
            gender = "man";
        }

        else if(view.getId() == R.id.woman_Btn)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
            gender = "woman";
        }
    }

    private  void updateDatabase()
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap genderOfUser = new HashMap();
        genderOfUser.put("gender",gender);
        reference.updateChildren(genderOfUser);
    }
}
