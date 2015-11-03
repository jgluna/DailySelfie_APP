package com.github.jgluna.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.github.jgluna.dailyselfie.model.Selfie;
import com.github.jgluna.dailyselfie.provider.DBHelper;
import com.github.jgluna.dailyselfie.provider.SelfiesProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfieHelper {

    private static final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String currentPhotoPath;

    public static Intent addOneSelfie(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                currentPhotoPath = photoFile.getPath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }
        }
        return takePictureIntent;
    }

    public static Selfie storeSelfie(Context context) {
        Selfie s = new Selfie();
        s.setImagePath(currentPhotoPath);
        s.setSelfieDate(new Date());
        //TODO mover a una fachada, ahorita es pa que funcione
        ContentValues values = new ContentValues();
        values.put(DBHelper.SELFIES_CREATION_DATE_COLUMN, iso8601Format.format(s.getSelfieDate()));
        values.put(DBHelper.SELFIES_ORIGINAL_IMAGE_PATH_COLUMN, s.getImagePath());
        context.getContentResolver().insert(SelfiesProvider.CONTENT_URI, values);
        return s;
    }

    private static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(Environment.getExternalStorageDirectory(), timeStamp + ".jpg");
    }
}
