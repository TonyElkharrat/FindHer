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

public class LevelReligionFragment extends Fragment implements View.OnClickListener
{
    ImageView nextbutton;
    Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.intro_level_religion, container, false);
        nextbutton = view.findViewById(R.id.next_button);
        nextbutton.setOnClickListener(this);
        Button manLevel1 = view.findViewById(R.id.level1);
        Button manLevel2 = view.findViewById(R.id.level2);
        Button manLevel3 = view.findViewById(R.id.level3);
        bundle = getArguments();
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
            Intent intent = new Intent(getActivity(), ZivugActivity.class);

            startActivity(intent );
            getActivity().finishAffinity();

        }

        else if(view.getId() == R.id.level1)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
        }

        else if(view.getId() == R.id.level2)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
        }

        else if(view.getId() == R.id.level3)
        {
            AnimationMaker.inflateAnimation(getContext(),nextbutton);
            nextbutton.setVisibility(View.VISIBLE);
        }

    }
}
