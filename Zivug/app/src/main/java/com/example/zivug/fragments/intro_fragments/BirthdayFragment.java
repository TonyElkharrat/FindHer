package com.example.zivug.fragments.intro_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.Api.SnackBarMessage;
import com.example.zivug.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BirthdayFragment extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        String dateOfBirthday;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.intro_age, container, false);
        ImageView nextbutton = view.findViewById(R.id.next_button);
        final EditText monthInput = view.findViewById(R.id.month_input);
        final EditText dayInput = view.findViewById(R.id.day_input);
        final EditText yearInput=view.findViewById(R.id.Year_input);

        disablePasswordMode(monthInput,dayInput,yearInput);


        nextbutton.setVisibility(View.INVISIBLE);

        nextbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean isMonthCorrect,isDayCorrect,isYearCorrect;
                isMonthCorrect = checkInputDate(monthInput,1,12,"month");
                isDayCorrect = checkInputDate(dayInput,1,31,"day");
                isYearCorrect= checkInputDate(yearInput,1939,2001,"year");
                if(isDayCorrect&&isMonthCorrect&&isYearCorrect)
                {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    LocationFragment locationFragment =  new LocationFragment();
                    int ageOfUser = 2019-Integer.valueOf(yearInput.getText().toString());
                    updateDatabase(ageOfUser);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    transaction.replace(R.id.central_layout_intro,locationFragment);
                    transaction.commit();
                }
            }
        });

        AnimationMaker.inflateAnimation(getContext(),nextbutton);
        monthInput.requestFocus();

        return view;
    }

    private  void disablePasswordMode(TextView month,TextView day,TextView year)
    {
        month.setTransformationMethod(null);
        day.setTransformationMethod(null);
        year.setTransformationMethod(null);
    }

    public boolean checkInputDate(TextView date,int minValue,int maxValue,String kindOfDate)
    {
        if(!date.getText().toString().isEmpty()) {
            if (Integer.valueOf(date.getText().toString()) > maxValue || Integer.valueOf(date.getText().toString()) < minValue) {
                SnackBarMessage.showSnackBar(getActivity(), "The " + kindOfDate + " is incorrect");
                date.setText("");
                return false;
            }
        }

        else
        {
            return false;
        }

        return true;
    }

    private  void updateDatabase(int ageOfUser)
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap genderOfUser = new HashMap();
        genderOfUser.put("ageUser",ageOfUser+"");
        reference.updateChildren(genderOfUser);
    }

}
