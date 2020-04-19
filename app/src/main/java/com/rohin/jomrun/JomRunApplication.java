package com.rohin.jomrun;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.rohin.jomrun.model.data.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


public class JomRunApplication extends Application {
    private static JomRunApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Consts.API_KEY = getManifistMetaData(BuildConfig.APPLICATION_ID + ".API_KEY");

    }

    public String getManifistMetaData(String key) {
        try {
            ApplicationInfo app;
            app = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            return bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JomRunApplication getInstance() {
        return INSTANCE;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void shareMovie(Fragment fragment, Movie movie) {
        if (!TextUtils.isEmpty(movie.getPoster())) {
            if (!canWriteStorage(fragment, "We need to write the image to the external storage inorder to share this movie ... "))
                return;
            Picasso.get().load(movie.getPoster()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Share.jpg";
                    OutputStream out = null;
                    File file = null;
                    try {
                        file = File.createTempFile("sharedImage", ".jpg", getExternalCacheDir());
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(file == null)
                        return;
                    path = file.getPath();
                    Uri bmpUri =  FileProvider.getUriForFile(
                            getApplicationContext(),
                            getApplicationContext()
                                    .getPackageName() + ".provider", file);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this movie: "+movie.getTitle()+" \nyear:"+movie.getYear()+" \ntype:"+movie.getType());
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    fragment.startActivity(Intent.createChooser(intent, "Share Movie"));

                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plan");
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, check this movie "+movie.getTitle()+" \nyear:"+movie.getYear()+" \ntype:"+movie.getType());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fragment.startActivity(Intent.createChooser(intent, "Share Movie"));
        }
    }

    public static boolean canWriteStorage(final Fragment fragment, String message) {
        if (ContextCompat.checkSelfPermission(fragment.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(fragment.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(fragment.getContext())
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> fragment.requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1)).setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).setTitle("Attention!")
                        .setIcon(android.R.drawable.ic_dialog_info).show();


            } else {
                fragment.requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
            return false;
        }
        return true;
    }


}
