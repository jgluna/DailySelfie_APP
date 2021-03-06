package com.github.jgluna.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.github.jgluna.dailyselfie.model.Selfie;
import com.github.jgluna.dailyselfie.model.SelfiesOrder;
import com.github.jgluna.dailyselfie.provider.DBHelper;
import com.github.jgluna.dailyselfie.provider.SelfiesProvider;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static List<Selfie> loadSelfiesFromProvider(SelfiesOrder order, boolean isModified, Context context) {
        Uri friends = SelfiesProvider.CONTENT_URI;
        String where = null;
        String[] whereValues = null;
        if (isModified) {
            where = DBHelper.SELFIES_IS_MODIFIED_COLUMN + "=?";
            whereValues = new String[]{String.valueOf(isModified)};
        }
        Cursor c = context.getContentResolver().query(friends, null, where, whereValues, order.getDescription());
        List<Selfie> selfies = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Selfie s;
                try {
                    s = new Selfie();
                    s.setSelfieDate(iso8601Format.parse(c.getString(c.getColumnIndex(DBHelper.SELFIES_CREATION_DATE_COLUMN))));
                    s.setImagePath(c.getString(c.getColumnIndex(DBHelper.SELFIES_ORIGINAL_IMAGE_PATH_COLUMN)));
                } catch (ParseException e) {
                    s = null;
                    e.printStackTrace();
                }
                if (s != null) {
                    selfies.add(s);
                }
            } while (c.moveToNext());
        }
        c.close();
        return selfies;
    }
}
