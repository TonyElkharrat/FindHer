package com.example.zivug.Animations;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.zivug.R;
import com.example.zivug.notifier.loadDataNotifier;

public class AnimationMaker {
    private static loadDataNotifier notifier;

    public static void SetListener(loadDataNotifier i_notifier) {
        notifier = i_notifier;
    }

    public static void makeLoadingAnimation(Activity activity, View view, int resource) {
        final SwipeRefreshLayout swipeLayout = view.findViewById(resource);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        notifier.loadData();

                    }
                }, 2000); // Delay in millis
            }
        });

        swipeLayout.setColorSchemeColors(
                activity.getResources().getColor(android.R.color.holo_blue_bright),
                activity.getResources().getColor(android.R.color.holo_green_light),
                activity.getResources().getColor(android.R.color.holo_orange_light),
                activity.getResources().getColor(android.R.color.holo_red_light)
        );
    }

    public static void inflateAnimation(Context context, View view)
    {
        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.animation_inflate);
        view.setAnimation(scaleUp);
    }
}
