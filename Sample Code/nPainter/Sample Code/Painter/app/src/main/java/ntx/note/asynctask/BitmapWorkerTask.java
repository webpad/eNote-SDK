package ntx.note.asynctask;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private int reqWidth = 0;
	private int reqHeight = 0;

	public BitmapWorkerTask(ImageView imageView, int reqWidth, int reqHeight) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.reqWidth = reqWidth;
		this.reqHeight = reqHeight;
	}

	@Override
	protected Bitmap doInBackground(String... params) {

		if (!params[0].equals("")) {
			return resizeBitmapFromFile(params[0], reqWidth, reqHeight);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		final ImageView imageView = imageViewReference.get();
		if (imageView != null && bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	public Bitmap resizeBitmapFromFile(String path, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}

		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}
}
