package com.vullnetlimani.instacreative.Helper;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.google.android.material.snackbar.Snackbar;
import com.vullnetlimani.instacreative.R;

import java.io.File;

public class Utils {

    public static boolean isEmailValid(CharSequence email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showMessageSnackBar(Context context, View view, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(context.getResources().getColor(R.color.primary))
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .setActionTextColor(context.getResources().getColor(R.color.white))
                .setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();

    }

    public static String getFileExtension(Context context, Uri uri) {

        String extension;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }

       // Log.d(LOG_TAG, "getFileExtension - " + extension);

        return extension;
    }

}
