package com.example.zivug.Api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.request.RequestOptions;

public class FileHelper
{
    public static Intent getFilefromMemory()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return intent;
    }
}
