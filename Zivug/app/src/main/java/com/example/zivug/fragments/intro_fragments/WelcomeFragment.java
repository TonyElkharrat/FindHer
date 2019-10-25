package com.example.zivug.fragments.intro_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.zivug.R;

    public class WelcomeFragment extends Fragment
    {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.intro_welcome, container, false);
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.animation_inflate);
            Button nextButton = view.findViewById(R.id.start_btn);
            nextButton.setAnimation(scaleUp);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
                    transaction.replace(R.id.central_layout_intro, new BirthdayFragment());
                    transaction.commit();
                }
            });
            return view;
        }

}
