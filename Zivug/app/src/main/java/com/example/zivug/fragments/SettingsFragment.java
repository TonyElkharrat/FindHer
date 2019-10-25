package com.example.zivug.fragments;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.zivug.Api.SnackBarMessage;
import com.example.zivug.R;
import com.example.zivug.UserManipulator.DeleteUserAccount;
import com.example.zivug.UserManipulator.onDelete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener
{

    FirebaseAuth firebaseAuth;
    private ListPreference listPreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
        setPreferencesFromResource(R.xml.preferences,s);
        Preference changePasswordPreferenceButton = findPreference("change_password_preference");
        changePasswordPreferenceButton.setOnPreferenceClickListener(this);
        Preference removeAccountPreferenceButton = findPreference("remove_user_account");
        removeAccountPreferenceButton.setOnPreferenceClickListener(this);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.central_layout,new HomeFragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        final Dialog infoDialog = new Dialog(getContext());
        infoDialog.setContentView(R.layout.delete_dialog);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton yesbutton = infoDialog.findViewById(R.id.YesButton);

        if(preference.getKey().equals("change_password_preference"))
        {
            resetPassword();
        }

        else  if(preference.getKey().equals("remove_user_account"))
        {
          showDialog(new DeleteUserAccount(),"Are you Sure you want to delete your account? This action is irreversible");
        }

        return false;
    }

    private void resetPassword()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                SnackBarMessage.showSnackBar( getActivity(),"We send you an email to reset your password");
            }
        });
    }

    private void showDialog(final onDelete deleter,String textToShow)
    {
        final Dialog infoDialog = new Dialog(getContext());
        infoDialog.setContentView(R.layout.delete_dialog);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton yesButton = infoDialog.findViewById(R.id.YesButton);
        ImageButton noButton = infoDialog.findViewById(R.id.CancelButton);
        TextView text = infoDialog.findViewById(R.id.text_dialog);
        text.setText(textToShow);
        infoDialog.show();

        yesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleter.deleteData(getActivity());
            }
        });

        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
    }


}
