package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Vladimira Hezelova on 6. 10. 2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private int inSampleSize = 0;

    private String imageUrl;

    private BaseAdapter adapter;

    private ImagesCache cache;

    private int desiredWidth, desiredHeight;

    private Bitmap image = null;

    private ImageView ivImageView;


    private LruCache<String, Bitmap> mMemoryCache;

    public DownloadImageTask(BaseAdapter adapter, int desiredWidth, int desiredHeight) {
        this.adapter = adapter;

        this.cache = ImagesCache.getInstance();

        this.desiredWidth = desiredWidth;

        this.desiredHeight = desiredHeight;
    }

    public DownloadImageTask(ImagesCache cache, ImageView ivImageView, int desireWidth, int desireHeight) {
        this.cache = cache;

        this.ivImageView = ivImageView;

        this.desiredHeight = desireHeight;

        this.desiredWidth = desireWidth;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];

        return getImage(imageUrl);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (result != null) {
            cache.addImageToWarehouse(imageUrl, result);

            if (ivImageView != null) {
                ivImageView.setImageBitmap(result);
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private Bitmap getImage(String imageUrl) {
        if (cache.getImageFromWarehouse(imageUrl) == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            options.inSampleSize = inSampleSize;

            try {
             //   URL url = new URL(imageUrl);

              //  HttpURLConnection connection = (HttpURLConnection) url.openConnection();

             //   InputStream stream = connection.getInputStream();

              //  image = BitmapFactory.decodeFile(imageUrl);

                int imageWidth = options.outWidth;

                int imageHeight = options.outHeight;

                if (imageWidth > desiredWidth || imageHeight > desiredHeight) {
                    System.out.println("imageWidth:" + imageWidth + ", imageHeight:" + imageHeight);

                    inSampleSize = inSampleSize + 2;

                    getImage(imageUrl);
                } else {
                    options.inJustDecodeBounds = false;

               //     connection = (HttpURLConnection) url.openConnection();

                 //   stream = connection.getInputStream();

                 //   image = BitmapFactory.decodeStream(stream, null, options);
                    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                    final int cacheSize = maxMemory / 8;

                    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                        @Override
                        protected int sizeOf(String key, Bitmap bitmap) {
                            // The cache size will be measured in kilobytes rather than
                            // number of items.
                            return bitmap.getByteCount() / 1024;
                        }
                    };


                    addBitmapToMemoryCache("key1", BitmapFactory.decodeFile(imageUrl));
                    if(getBitmapFromMemCache("key1") == null) {
                        Log.e("bitmapFromCache" , "is snull");
                    }else {
                        Log.e("bitmapFromCache" , getBitmapFromMemCache("key1").toString());
                    }
                    image = getBitmapFromMemCache("key1");

                    return image;
                }
            } catch (Exception e) {
                Log.e("getImage", e.toString());
            }
        }

        return image;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            if(bitmap == null) {
                Log.e("bitmap" , "is snull");
            }else {
                Log.e("bitmap" , bitmap.toString());
            }
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


}
