package com.example.findher.notifier;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

public class TextChangedNotifier implements TextWatcher
{
    private Button sendbutton;

    public TextChangedNotifier(Button sendbutton)
    {
        this.sendbutton = sendbutton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        sendbutton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        if(charSequence.length()>0) {
            sendbutton.setVisibility(View.VISIBLE);
        }
    }
}
