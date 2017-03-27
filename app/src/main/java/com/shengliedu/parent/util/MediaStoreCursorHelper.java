package com.shengliedu.parent.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 */
public class MediaStoreCursorHelper {
    public static final String COL_COUNT = "count(0)";
    public static final String[] PHOTOS_PROJECTION = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
    public static final String PHOTOS_ORDER_BY = MediaStore.Images.Media.DATE_ADDED + " desc";

    public static final Uri MEDIA_STORE_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    // 所有相册目录信息
    public static Cursor openAlbumsCursor(Context context) {
        return context.getContentResolver().query(
                MEDIA_STORE_CONTENT_URI,
                new String[]{COL_COUNT, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.BUCKET_ID
                },
                "1=1) group by(" + MediaStore.Images.ImageColumns.BUCKET_ID,
                null, PHOTOS_ORDER_BY);
    }

    public static Cursor openUsersPhotosCursor(Context context, Uri contentUri,
                                               String bucketId) {
        String selection = null;
        String[] selectionArgs = null;

        if (!TextUtils.isEmpty(bucketId)) {
            selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
            selectionArgs = new String[]{bucketId};
        }
        return context.getContentResolver().query(contentUri,
                PHOTOS_PROJECTION, selection, selectionArgs, PHOTOS_ORDER_BY);
    }

    public static Cursor openPhotosCursor(Context context, Uri contentUri) {
        return context.getContentResolver()
                .query(contentUri, PHOTOS_PROJECTION, null, null, PHOTOS_ORDER_BY);
    }


    public static Bitmap getLocalImgThumbnail(ContentResolver resolver, long id) {
        Bitmap bm = null;
        bm = MediaStore.Images.Thumbnails.getThumbnail(resolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
        if (bm == null) {
            String[] PROJIMG = {
                    MediaStore.Images.ImageColumns.DATA
            };
            String where = MediaStore.Images.ImageColumns._ID + "=?";
            String[] args = {String.valueOf(id)};
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    PROJIMG, where, args, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    bm = generateLocalImgThumbnail(fullpath);
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return bm;
    }

    private static Bitmap generateLocalImgThumbnail(String path) {
        String lowPath = path.toLowerCase();
        if (!lowPath.endsWith(".jpg") && !lowPath.endsWith(".png")
                && !lowPath.endsWith(".gif")
                && !lowPath.endsWith(".jpeg") && !lowPath.endsWith(".bmp")) {
            return null;
        }
        Bitmap mBitmap;//
        mBitmap = loadLocalBitmap(path);
        Bitmap small = null;
        if (mBitmap != null) {
            small = Bitmap.createScaledBitmap(mBitmap, 90, 90, false);
        }

        return small;
    }

    public static Bitmap loadLocalBitmap(String path) {
        Bitmap mBitmap = null;//

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Set height and width in options, does not return an image and no resource taken
        BitmapFactory.decodeFile(path, options);
        int pow = 0;
        while (options.outHeight >> pow > Config.screenheight || options.outWidth >> pow > Config.screenwidth)
            pow += 1;
        options.inSampleSize = 1 << pow;
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(path, options);
        return mBitmap;
    }
}
