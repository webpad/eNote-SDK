package ntx.demo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

    public class NtxImageViewClass extends ImageView {

        private static final String TAG = "ntxImageView";
        // private final Bitmap mScreen;
        private boolean mfirsttimedraw;

        public static int FULL_REFRESH_COUNT = 600;


        /* update mode for handwriting in eink */
        public static final int UPDATE_MODE_PARTIAL_DU =
                android.view.View.EINK_WAVEFORM_MODE_DU
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL;

        public static final int UPDATE_MODE_PARTIAL_A2 =
                          android.view.View.EINK_WAVEFORM_MODE_A2
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL;

        public static final int UPDATE_MODE_PARTIAL_GC4 =
                android.view.View.EINK_WAVEFORM_MODE_GC4
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL;

        public static final int UPDATE_MODE_PARTIAL_DU_WITH_DITHER =
                          android.view.View.EINK_WAVEFORM_MODE_DU
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL
                        | android.view.View.EINK_DITHER_MODE_DITHER;

        public static final int UPDATE_MODE_PARTIAL_A2_WITH_DITHER =
                          android.view.View.EINK_WAVEFORM_MODE_A2
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL
                        | android.view.View.EINK_DITHER_MODE_DITHER;

        public static final int UPDATE_MODE_PARTIAL_GC4_WITH_DITHER =
                android.view.View.EINK_WAVEFORM_MODE_GC4
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL
                        | android.view.View.EINK_DITHER_MODE_DITHER;

        public static final int UPDATE_MODE_PARTIAL_DU_WITH_MONO =
                android.view.View.EINK_WAVEFORM_MODE_DU
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL
                        | android.view.View.EINK_DITHER_MODE_DITHER
                        | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

        public static final int UPDATE_MODE_PARTIAL_A2_WITH_MONO =
                android.view.View.EINK_WAVEFORM_MODE_A2
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL
                        | android.view.View.EINK_DITHER_MODE_DITHER
                        | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

        public static final int UPDATE_MODE_PARTIAL_GC4_WITH_MONO =
                android.view.View.EINK_WAVEFORM_MODE_GC4
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL
                        | android.view.View.EINK_DITHER_MODE_DITHER
                        | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

        public static final int UPDATE_MODE_PARTIAL_GC16 =
                          android.view.View.EINK_WAVEFORM_MODE_GC16
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL;

        public static final int UPDATE_MODE_PARTIAL_AUTO =
                          android.view.View.EINK_WAVEFORM_MODE_AUTO
                        | android.view.View.EINK_UPDATE_MODE_PARTIAL;

        public static final int UPDATE_MODE_FULL_GC16 =
                          android.view.View.EINK_WAVEFORM_MODE_GC16
                        | android.view.View.EINK_UPDATE_MODE_FULL;

        public static final int UPDATE_MODE_FULL_A2 =
                          android.view.View.EINK_WAVEFORM_MODE_A2
                        | android.view.View.EINK_UPDATE_MODE_FULL
                        | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

        public static final int UPDATE_MODE_FULL_DU =
                          android.view.View.EINK_WAVEFORM_MODE_DU
                        | android.view.View.EINK_UPDATE_MODE_FULL
                        | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

        public static  int UPDATE_MODE_SCREEN = UPDATE_MODE_FULL_GC16;

        public NtxImageViewClass(Context context) {
            super(context);
            mContext = context;
            mfirsttimedraw = false;
        }

        public Bitmap loadBitmap(Drawable sprite) {
            return loadBitmap(sprite, Bitmap.Config.RGB_565);
        }

        public Bitmap loadBitmap(Drawable sprite, Bitmap.Config bitmapConfig) {
            Display d = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            int width = d.getWidth();
            int height = d.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);
            Canvas canvas = new Canvas(bitmap);
            sprite.setBounds(0, 0, width, height);
            sprite.draw(canvas);
            return bitmap;
        }

        @Override
        public void setImageDrawable(Drawable drawable) {
            invalidate(UPDATE_MODE_SCREEN);
            super.setImageDrawable(drawable);
        }

        @Override
        public void setImageBitmap(Bitmap bm)
        {
            invalidate(UPDATE_MODE_SCREEN);
            super.setImageBitmap(bm);
        }

        @Override
        public void draw(Canvas canvas) {
            if (mfirsttimedraw == false) {
                invalidate(UPDATE_MODE_SCREEN);
                mfirsttimedraw = true;
            } else {
                super.draw(canvas);
            }
        }
        
        
        
        public void setUPDATE_MODE_SCREEN(int updateMode) {
        	
        	
        	UPDATE_MODE_SCREEN=updateMode;
        	
        	
        }
        
        
        
    }