package com.example.zivug.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.zivug.R;
import com.spark.submitbutton.SubmitButton;

public class SearchFragment extends Fragment implements View.OnClickListener
{
    TextView minimumAgeTV;
    TextView maximumAgeTV;
    TextView radiusResearchTV;
    BubbleThumbRangeSeekbar ageSeekbar;
    BubbleThumbSeekbar radiusSearchSeekBar;

    private  int minimumValueAge , maximumValueAge,radiusResearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_fragment, container, false);

        ageSeekbar = (BubbleThumbRangeSeekbar) view.findViewById(R.id.rangeSeekbar3);
        SubmitButton researchButton = view.findViewById(R.id.research_button);
        ImageView backButton = view.findViewById(R.id.back_button);
        minimumAgeTV = (TextView) view.findViewById(R.id.tvMin);
        maximumAgeTV = (TextView) view.findViewById(R.id.tvMaxin);
        radiusResearchTV = view.findViewById(R.id.tvMaxdistance);
        radiusSearchSeekBar = view.findViewById(R.id.rangeSeekbar4);

        researchButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        ageSeekbar.setVerticalScrollbarPosition(CrystalSeekbar.Position.LEFT);

        ageSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener()
        {
            @Override
            public void valueChanged(Number minValue, Number maxValue)
            {

                maximumAgeTV.setText(String.valueOf(maxValue));
                minimumAgeTV.setText(String.valueOf(minValue));
                makeSeekBarPicture(minValue.intValue(),maxValue.intValue());

            }
        });

        radiusSearchSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value)
            {
                radiusResearchTV.setText(String.valueOf(value));
                radiusResearch = value.intValue();
            }
        });




        ageSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener()
        {
            @Override
            public void finalValue(Number minValue, Number maxValue)
            {
                makeSeekBarPicture(minValue.intValue(),maxValue.intValue());
                minimumValueAge = minValue.intValue();
                maximumValueAge = maxValue.intValue();
            }
        });

        return view;

    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.research_button)
        {
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(1000);
                        Bundle bundle = new Bundle();
                        bundle.putInt("minimumAge",minimumValueAge);
                        bundle  .putInt("maximumAge",maximumValueAge);
                        bundle .putInt("radiusResearch",radiusResearch);
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(bundle);

                        ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.central_layout, homeFragment).commit();


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }

        else if( view.getId() == R.id.back_button)
        {
            ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.central_layout, new HomeFragment()).commit();
        }
    }

    private void makeSeekBarPicture(int minValue, int maxValue)
    {

        if(minValue>30&& minValue<60)
        {

            ageSeekbar.setLeftThumbHighlightBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.middle_women));

        }
        else if(maxValue>30&& maxValue<60)
        {

            ageSeekbar.setRightThumbHighlightBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.middle_women));

        }

        else if(minValue>60)
        {
            ageSeekbar.setLeftThumbHighlightBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.old_women));

        }
        else if(maxValue>60)
        {
            ageSeekbar.setRightThumbHighlightBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.old_women));

        }
        else if(minValue<30)
        {
            ageSeekbar.setLeftThumbHighlightBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.teenager_women));


        }
        else if(maxValue<30)
        {
            ageSeekbar.setRightThumbHighlightBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.teenager_women));
        }
    }
}
