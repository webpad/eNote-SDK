package ntx.painter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import ntx.draw.nDrawHelper;

public class NtxView extends View {

    private final static String TAG = "NtxView";

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private Paint mPaint;
    private Path mPath;

    private Rect mCurrentUpdateEndPointRegion;
    private Rect mCurrentUpdateRect;

    boolean mStartFlag = false;
    boolean mFirstPointFlag = true;
    boolean mFinishUpdateFlag = false;
    boolean isFingerTouch = false;

    private int mCurX = -1;
    private int mCurY = -1;
    private int mOldX = -1;
    private int mOldY = -1;
    private int mCurWidth;

    private int intCanvasHeight = 0;
    private int intCanvasWidth = 0;

    private int fLinewidth = 6;
    private int refreshMode;
    private int mPenType = PenType.PENCIL.ordinal();
    private int max_pressure = CHINESE_BRUSH_STROKEWIDTH;
    private String strBackgroundPath = "";

    private static final int CHINESE_BRUSH_STROKEWIDTH = 100;

    private boolean isFullRefreshable = true;
    private boolean isEnablePenPressure = false;

    private Object mDrawLock;
    public final int NOTSET = -1;

    boolean isEraser = false;
    private ArrayList<TouchPath> tPath;
    private Rect mCurrDrawRect;
    private Paint mCurrDrawPaint;

    public static int mCurrentRotation;
    public static int mNavigationBarHeight;

    static private int intCanvasOffsetX;
    static private int intCanvasOffsetY;
    static private boolean isOutSide = false;

    public static final int UPDATE_MODE_PARTIAL_DU =
            android.view.View.EINK_WAVEFORM_MODE_DU
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL;

    public static final int UPDATE_MODE_PARTIAL_DU_WITH_DITHER =
            android.view.View.EINK_WAVEFORM_MODE_DU
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_DITHER_MODE_DITHER;

    public static final int UPDATE_MODE_PARTIAL_DU_WITH_MONO =
            android.view.View.EINK_WAVEFORM_MODE_DU
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_DITHER_MODE_DITHER
                    | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

    public static final int UPDATE_MODE_PARTIAL_A2 =
            android.view.View.EINK_WAVEFORM_MODE_A2
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL;

    public static final int UPDATE_MODE_PARTIAL_A2_WITH_DITHER =
            android.view.View.EINK_WAVEFORM_MODE_A2
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_DITHER_MODE_DITHER;

    public static final int UPDATE_MODE_PARTIAL_A2_WITH_MONO =
            android.view.View.EINK_WAVEFORM_MODE_A2
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_DITHER_MODE_DITHER
                    | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

    public static final int UPDATE_MODE_PARTIAL_GC4 =
            android.view.View.EINK_WAVEFORM_MODE_GC4
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL;

    public static final int UPDATE_MODE_PARTIAL_GC4_WITH_DITHER =
            android.view.View.EINK_WAVEFORM_MODE_GC4
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_DITHER_MODE_DITHER;

    public static final int UPDATE_MODE_PARTIAL_GC4_WITH_MONO =
            android.view.View.EINK_WAVEFORM_MODE_GC4
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_DITHER_MODE_DITHER
                    | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

    public static final int UPDATE_MODE_FULL_GC16 =
            android.view.View.EINK_WAVEFORM_MODE_GC16
                    | android.view.View.EINK_UPDATE_MODE_FULL;

    public static final int UPDATE_MODE_PARTIAL_GC16 =
            android.view.View.EINK_WAVEFORM_MODE_GC16
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL;

    public static final int UPDATE_MODE_PARTIAL_AUTO =
            android.view.View.EINK_WAVEFORM_MODE_AUTO
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL;

    public static final int UPDATE_MODE_FULL_A2 =
            android.view.View.EINK_WAVEFORM_MODE_A2
                    | android.view.View.EINK_UPDATE_MODE_FULL
                    | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

    public static final int UPDATE_MODE_FULL_DU =
            android.view.View.EINK_WAVEFORM_MODE_DU
                    | android.view.View.EINK_UPDATE_MODE_FULL
                    | android.view.View.EINK_MONOCHROME_MODE_MONOCHROME;

    public static final int UPDATE_MODE_FULL_DU_WITH_DITHER =
            android.view.View.EINK_WAVEFORM_MODE_DU
                    | android.view.View.EINK_UPDATE_MODE_FULL
                    | android.view.View.EINK_DITHER_MODE_DITHER;

    public static final int EINK_STATIC_MODE_SET = 0x10000000;
    public static final int EINK_STATIC_MODE_CLEAR = 0x20000000;
    public static final int UPDATE_MODE_GLOBAL_PARTIAL_A2_WITH_DITHER =
            android.view.View.EINK_AUTO_MODE_REGIONAL
                    | android.view.View.EINK_WAIT_MODE_NOWAIT
                    | android.view.View.EINK_UPDATE_MODE_PARTIAL
                    | android.view.View.EINK_WAVEFORM_MODE_A2
                    | android.view.View.EINK_DITHER_MODE_DITHER
                    | EINK_STATIC_MODE_SET;
    public static final int UPDATE_MODE_GLOBAL_RESET = EINK_STATIC_MODE_CLEAR;

    public static final int UPDATE_MODE_GLOBAL_FULL_GC16 =
            android.view.View.EINK_UPDATE_MODE_FULL
                    | android.view.View.EINK_WAVEFORM_MODE_GC16
                    | EINK_STATIC_MODE_SET;

    public static final int UPDATE_MODE_GLOBAL_FULL_A2_WITH_DITHER =
            android.view.View.EINK_AUTO_MODE_REGIONAL
                    | android.view.View.EINK_WAIT_MODE_WAIT
                    | android.view.View.EINK_UPDATE_MODE_FULL
                    | android.view.View.EINK_WAVEFORM_MODE_A2
                    | android.view.View.EINK_DITHER_MODE_DITHER
                    | EINK_STATIC_MODE_SET;

    public static final int MODE_APPNDRAWSTROKESYNC = 0x01000000;

    public static int[] refresh_mode_table = {
            UPDATE_MODE_PARTIAL_DU,
            UPDATE_MODE_PARTIAL_DU_WITH_DITHER,
            UPDATE_MODE_PARTIAL_DU_WITH_MONO,
            UPDATE_MODE_PARTIAL_A2,
            UPDATE_MODE_PARTIAL_A2_WITH_DITHER,
            UPDATE_MODE_PARTIAL_A2_WITH_MONO,
            UPDATE_MODE_PARTIAL_GC4,
            UPDATE_MODE_PARTIAL_GC4_WITH_DITHER,
            UPDATE_MODE_PARTIAL_GC4_WITH_MONO
    };

    public enum PenType {
            PENCIL,
            PEN,
            CHINESE_BRUSH,
    }

    public static int[] pen_type_table = {
            PenType.PENCIL.ordinal(),
            PenType.PEN.ordinal(),
            PenType.CHINESE_BRUSH.ordinal(),
    };

    public static boolean isEinkHardwareType() {
        return SystemProperties.get("ro.product.hardwareType", "").equals("E60Q20")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60Q30")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60Q50")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60Q60")
                || SystemProperties.get("ro.product.hardwareType", "").equals("ED0Q00")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60QD0")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E70Q30")
                || SystemProperties.get("ro.product.hardwareType", "").equals("EA0Q00")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60QR2");
    }

    public static boolean isEinkHandWritingHardwareType() {
        return SystemProperties.get("ro.product.hardwareType", "").equals("E60Q60")
                || SystemProperties.get("ro.product.hardwareType", "").equals("ED0Q00")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E70Q30")
                || SystemProperties.get("ro.product.hardwareType", "").equals("EA0Q00")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60QR2");
    }

    public static boolean isEink6InchHardwareType() {
        return SystemProperties.get("ro.product.hardwareType", "").equals("E60Q60")
                || SystemProperties.get("ro.product.hardwareType", "").equals("E60QR2")
                || SystemProperties.get("ro.product.hardwareType", "").equals("EA0Q00");
    }

    public static boolean isPenUpdateModeDU() {
        return SystemProperties.get("ro.product.hardwareType", "").equals("ED0Q00");
    }

    public void setCurrentRotation(int mCurrent_Rotation) {

        this.mCurrentRotation = mCurrent_Rotation;
        nDrawHelper.NDrawSetInputRotation(mCurrentRotation);


    }

    public NtxView(Context context) {
        this(context, null);
    }

    public NtxView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NtxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(false);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        tPath = new ArrayList();

        mCurrentUpdateEndPointRegion = new Rect(0, 0, 0, 0);
        mCurrentUpdateRect = new Rect(NOTSET, NOTSET, NOTSET, NOTSET);

        mCurrDrawPaint = new Paint();
        mCurrDrawPaint.setAntiAlias(false);
        mCurrDrawPaint.setStyle(Paint.Style.STROKE);
        mCurrDrawPaint.setColor(Color.BLACK);
        mCurrDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mCurrDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        mFirstPointFlag = true;

        mDrawLock = new Object();

        // update_mode initial
        if (isPenUpdateModeDU()) {
            setRefreshMode(UPDATE_MODE_PARTIAL_DU);
        } else {
            setRefreshMode(UPDATE_MODE_PARTIAL_A2);
        }
        setPenThickness(fLinewidth);
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public void getBottomHeight(int mNavigationBar_Height) {

        this.mNavigationBarHeight = mNavigationBar_Height;

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //Log.d(TAG, "onSizeChanged w h oldw oldh = " + "( " + w + ", " + h + ", " + oldw + ", " + oldh + ")");
        int curW = 0;
        int curH = 0;

        if (curW < w)
            curW = w;
        if (curH < h)
            curH = h;

        intCanvasHeight = curH;
        intCanvasWidth = curW;

        Canvas newCanvas = new Canvas();
        Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
        newCanvas.setBitmap(newBitmap);
        newCanvas.drawColor(Color.WHITE);
        if (mBitmap != null) {
            newCanvas.drawBitmap(mBitmap, 0, 0, null);
        }
        mBitmap = newBitmap;
        mCanvas = newCanvas;

        synchronized (mDrawLock) {
            mDrawLock.notifyAll();
        }

        int Ndraw_region_left = 0;
        int Ndraw_region_top = getRelativeTop(NtxView.this);
        int Ndraw_region_right = Ndraw_region_left + w;
        int Ndraw_region_bottom = Ndraw_region_top + h;
        int[] packet1 = {Ndraw_region_left, Ndraw_region_top, Ndraw_region_right, Ndraw_region_bottom};

        //Log.d(TAG, "packet1: " + Arrays.toString(packet1));
        //nDrawHelper.NDrawInit();
        nDrawHelper.NDrawSetDrawRegion(packet1);

        //test_setNDrawMultiActiveRegion(w, h);
        
    }

    private void test_NDrawDropFrames() {
        nDrawHelper.NDrawDropFrames(400000000); //400 ms
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * Return a String describing the calling method and location at a particular stack depth.
     *
     * @param callStack the Thread stack
     * @param depth     the depth of stack to return information for.
     * @return the String describing the caller at that depth.
     */
    private static String getCaller(StackTraceElement callStack[], int depth) {
        // callStack[4] is the caller of the method that called getCallers()
        if (4 + depth >= callStack.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement caller = callStack[4 + depth];
        return caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber();
    }

    /**
     * Return a string consisting of methods and locations at multiple call stack levels.
     *
     * @param depth the number of levels to return, starting with the immediate caller.
     * @return a string describing the call stack.
     * {@hide}
     */
    public static String getCallers(final int depth) {
        final StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            sb.append(getCaller(callStack, i)).append(" ");
        }
        return sb.toString();
    }

    /**
     * Return a string consisting of methods and locations at multiple call stack levels.
     *
     * @param depth the number of levels to return, starting with the immediate caller.
     * @return a string describing the call stack.
     * {@hide}
     */
    public static String getCallers(final int start, int depth) {
        final StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        depth += start;
        for (int i = start; i < depth; i++) {
            sb.append(getCaller(callStack, i)).append(" ");
        }
        return sb.toString();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        processOnTouchEventNtx(event);
        return true;
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {

        super.onHoverEvent(event);
        if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
            mHandler.removeCallbacks(runPostInvalidate);
            mHandler.postDelayed(runPostInvalidate, 3000);
        } else if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            mHandler.removeCallbacks(runPostInvalidate);
            nDrawHelper.NDrawSwitch(true);
        }

        return true;
    }

    boolean processOnTouchEventNtx(MotionEvent event) {
        // Check Finger Touch or Pen
        switch (getToolType(event)) {
            case MotionEvent.TOOL_TYPE_STYLUS: // set Pan touch
                if (isFingerTouch) return true;

                break;
            case MotionEvent.TOOL_TYPE_FINGER: // set Hand touch
                if (false == isFingerTouch) return true;

                break;

        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            // Check Pen ERASER and HIGHLIGHT button pressed
            if ((event.getButtonState() & MotionEvent.BUTTON_SECONDARY) > 0 ? true : false) { // NTX Eraser
                isEraser = true;
                nDrawHelper.NDrawSwitch(false);
            } else if ((event.getButtonState() & MotionEvent.BUTTON_TERTIARY) > 0 ? true : false) { // Lin Eraser
                isEraser = true;
                nDrawHelper.NDrawSwitch(false);
            } else if ((event.getToolType(0) == MotionEvent.TOOL_TYPE_ERASER)) {
                isEraser = true;
                nDrawHelper.NDrawSwitch(false);
            } else {
                isEraser = false;
                nDrawHelper.NDrawSwitch(true);

                //nDrawHelper.NDrawSetInputOffset(intCanvasOffsetX,intCanvasOffsetY);

            }

            nDrawHelper.NDrawSetStrokeWidth(fLinewidth);

            startUpdating();
        }

        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            drawPointNtx(event.getHistoricalX(i) + intCanvasOffsetX,
                    event.getHistoricalY(i) + intCanvasOffsetY + 1,
                    event.getHistoricalPressure(i),
                    event.getHistoricalSize(i));
        }

        drawPointNtx(event.getX() + intCanvasOffsetX,
                event.getY() + intCanvasOffsetY + 1,
                event.getPressure(),
                event.getSize());

        if (event.getAction() == MotionEvent.ACTION_UP) {
            stopUpdating();
            //nDrawHelper.NDrawSwitch(false);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isEraser)
                eraseDraw();
        }

        return true;
    }

    void startUpdating() {
        setFullRefreshable(false);
        mHandler.removeCallbacks(runPostInvalidate);
        mStartFlag = true;
        mCurrDrawRect = new Rect(0, 0, 0, 0);

    }

    void stopUpdating() {
        setFullRefreshable(true);
        mStartFlag = false;

        mFirstPointFlag = true;
        synchronized (mDrawLock) {
            mCurrentUpdateRect.set(NOTSET, NOTSET, NOTSET, NOTSET);
        }
        int mode = MODE_APPNDRAWSTROKESYNC | android.view.View.EINK_WAVEFORM_MODE_GC16;
        Log.d(TAG, "app invalidate() " + Integer.toHexString(mode));
        invalidate(mode);
    }

    private void drawPointNtx(float x, float y, float pressure, float size) {

        int x_int = 0;
        int y_int = 0;

        x_int = (int) x;
        y_int = (int) y;

        currDrawRectMove((int) x, (int) y);

        if (mCurX != x_int || mCurY != y_int) {
            if (mFinishUpdateFlag) {
                mFinishUpdateFlag = false;
            }
            /*if (isEnablePenPressure) {
                mPaint.setStrokeWidth(((float) max_pressure) * pressure);
                //nDrawHelper.NDrawSetStrokeWidth((int) (((float) max_pressure) * pressure));
            }*/
            updatePenStroke(pressure);
            mCurX = x_int;
            mCurY = y_int;
            if (mCurY > intCanvasHeight) {
                mCurY = intCanvasHeight;
            }
            if (mCurY < 0) {
                mCurY = 0;
                isOutSide = true;
                return;
            }
            if (isOutSide == true) {
                isOutSide = false;
                stopUpdating();
            }
            if (mCurX > intCanvasWidth) {
                mCurX = intCanvasWidth;
            }
            if (mCurX < 0) {
                mCurX = 0;
            }
            if (isEnablePenPressure) {
                mCurWidth = (int) ((((float) max_pressure) * pressure) / 2.0f);
            } else {
                mCurWidth = getPenThickness() / 2;
            }
            if (mCurWidth < 1) {
                mCurWidth = 1;
            }

            if (mCanvas != null) {
                int newleft = mCurX - mCurWidth;
                int newtop = mCurY - mCurWidth;
                int newright = mCurX + mCurWidth;
                int newbottom = mCurY + mCurWidth;
                synchronized (mDrawLock) {
                    if (mFirstPointFlag) {
                        mFirstPointFlag = false;
                        mCurrentUpdateRect.set(newleft, newtop, newright, newbottom);
                    } else {
                        mCurrentUpdateRect.union(newleft, newtop, newright, newbottom);

                        if (false == isEraser) {
                            mCanvas.drawLine((float) mOldX, (float) mOldY, (float) mCurX, (float) mCurY, mPaint);


                            if (getPenType() == PenType.PEN.ordinal()){
                                tPath.add(new TouchPath( (int)(fLinewidth * pressure), mPaint.getColor(), mOldX, mOldY, mCurX, mCurY, mCurrDrawRect));

                            }else if (getPenType() == PenType.CHINESE_BRUSH.ordinal()) {
                                tPath.add(new TouchPath((int)(float)(max_pressure * Math.pow(pressure,3)), mPaint.getColor(), mOldX, mOldY, mCurX, mCurY, mCurrDrawRect));

                            }else{
                                // PenType.PENCIL.ordinal()
                                tPath.add(new TouchPath(fLinewidth, mPaint.getColor(), mOldX, mOldY, mCurX, mCurY, mCurrDrawRect));

                            }
                        }
                    }
                    mOldX = mCurX;
                    mOldY = mCurY;
                    mCurrentUpdateEndPointRegion.set(newleft, newtop, newright, newbottom);
                }
            }
        }
    }

    private Handler mHandler = new Handler();

    public synchronized void clear() {
        if (mCanvas != null) {
            tPath.clear();

            mPath.reset();
            drawBackground();
            mHandler.removeCallbacks(runClear);
            mHandler.postDelayed(runClear, 1000);
        }
    }

    private Runnable runPostInvalidate = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    private Runnable runClear = new Runnable() {
        @Override
        public void run() {

            mFirstPointFlag = true;
            fullRefresh(NtxView.this);
        }
    };

    public int FileOutput(String output_path) {

        Bitmap bmpBackground = null;

        if (strBackgroundPath.equals("")) {
            if (mBitmap == null)
                return -1; // retrun -1 ==> fail; return 0 ==> success;

            bmpBackground = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            bmpBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(strBackgroundPath), mBitmap.getWidth(),
                    mBitmap.getHeight(), true);
        }

        if (mBitmap != null) {
            // Create new canvas to merge paint and background
            bmpBackground = combineBitmap(bmpBackground, mBitmap);
        }

        try {
            FileOutputStream fos = new FileOutputStream(output_path);
            bmpBackground.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * @param background
     * @param foreground
     * @return Bitmap
     */
    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
                (bgHeight - fgHeight) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }

    public int getPenThickness() {
        return fLinewidth;
    }

    public void setPenThickness(int i) {
        fLinewidth = i;
        mPaint.setStrokeWidth(fLinewidth);
        nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure(NOTSET);
        nDrawHelper.NDrawSetStrokeWidth(fLinewidth);
        isEnablePenPressure = false;
        return;
    }

    private void updatePenStroke( float pressure ) {

        if ( mPenType == PenType.PENCIL.ordinal() ) {
            mPaint.setStrokeWidth(fLinewidth);
            nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure(NOTSET);
            nDrawHelper.NDrawSetStrokeWidth(fLinewidth);
        } else if ( mPenType == PenType.PEN.ordinal() ) {
            isEnablePenPressure = true;
            mPaint.setStrokeWidth(fLinewidth * pressure);
            nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure(fLinewidth);

        } else if ( mPenType == PenType.CHINESE_BRUSH.ordinal() ) {
            isEnablePenPressure = true;
            mPaint.setStrokeWidth((float)(max_pressure * Math.pow(pressure,3)));
            nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure(max_pressure);

        }
    }



    public boolean getPenThicknessWithPressure() {
        return isEnablePenPressure;
    }

    public void setPenThicknessWithPressure(boolean enable_pen_pressure) {
        nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure(max_pressure);
        isEnablePenPressure = enable_pen_pressure;
        return;
    }

    public void setFullRefreshable(boolean fullRefreshable) {
        isFullRefreshable = fullRefreshable;
    }

    public boolean getFullRefreshable() {
        return isFullRefreshable;
    }

    public void setFingerTouch(boolean checked) {
        isFingerTouch = checked;
    }

    private int intCanvasStyle = 1;

    public void setCanvasStyle(int i) {
        if (i == 1) {
            intCanvasStyle = 1;
            mPaint.setColor(Color.BLACK);
            mCanvas.drawColor(Color.WHITE);
            nDrawHelper.NDrawSetPaintColor(Color.BLACK);
        } else if (i == 2) {
            intCanvasStyle = 2;
            mPaint.setColor(Color.WHITE);
            mCanvas.drawColor(Color.BLACK);
            nDrawHelper.NDrawSetPaintColor(Color.WHITE);
        }
        mPaint.setXfermode(null);
        setPenThickness(fLinewidth);
    }

    public void setRefreshMode(int refresh_mode) {
        refreshMode = refresh_mode;
        nDrawHelper.NDrawSetUpdateMode(refresh_mode);
    }

    public int getRefreshMode() {
        return refreshMode;
    }

    public void setPenType(int type) {
        mPenType = type;
        nDrawHelper.NDrawSetPenType(type);
    }

    public int getPenType() {
        //Log.d(TAG, "getPenType: " + mPenType);
        return mPenType;
    }

    public void setBackgroundPath(String background_path) {
        strBackgroundPath = background_path;

        if (new File(background_path).exists()) {
            tPath.clear();
            drawBackground();
            invalidate();
        }
    }

    public void onPause() {
        nDrawHelper.NDrawSwitch(false);
    }

    public void onResume() {
        nDrawHelper.NDrawSwitch(true);
    }

    public void drawBackground() {
        if (intCanvasStyle == 1) {
            mBitmap.eraseColor(Color.WHITE);
        } else if (intCanvasStyle == 2) {
            mBitmap.eraseColor(Color.BLACK);
        }

        if (new File(strBackgroundPath).exists()) {
            mBitmap.eraseColor(Color.TRANSPARENT);
            setBackgroundDrawable(new BitmapDrawable(strBackgroundPath));
        }
    }

    private int getToolType(MotionEvent event) {
        return hasToolStylus(event) ? MotionEvent.TOOL_TYPE_STYLUS : event.getToolType(event.getActionIndex());
    }

    private boolean hasToolStylus(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (event.getToolType(i) == MotionEvent.TOOL_TYPE_STYLUS) {
                return true;
            }
        }
        return false;
    }

    private void eraseDraw() {
        if (isEraser && tPath.size() > 0 && isRectEmpty(mCurrDrawRect)) {
            TouchPath tp;
            boolean eraseStatus = false;
            for (int i2 = 0; i2 < 2; i2++) {
                for (int i1 = tPath.size() - 1; i1 >= 0; i1--) {
                    tp = (TouchPath) tPath.get(i1);
                    Rect rect = tp.mRect;
                    if (isRectEmpty(rect) && Rect.intersects(mCurrDrawRect, rect)) {
                        if (i2 == 1) {
                            tPath.remove(tp);
                        } else {
                            eraseRect(rect);
                        }
                        eraseStatus = true;
                    }
                }
                if (!eraseStatus) {
                    break;
                }
            }
            if (eraseStatus) {
                int currStrokeWidth = fLinewidth;
                int currColor = mPaint.getColor();
                drawBackground();

                Iterator it = tPath.iterator();
                while (it.hasNext()) {
                    tp = (TouchPath) it.next();
                    mCurrDrawPaint.setStrokeWidth((float) tp.mStrokeWidth);
                    mCurrDrawPaint.setColor(tp.mColor);
                    nDrawHelper.NDrawSetStrokeWidth(tp.mStrokeWidth);
                    nDrawHelper.NDrawSetPaintColor(tp.mColor);
                    mCanvas.drawLine((float) tp.mOldX, (float) tp.mOldY, (float) tp.mCurX, (float) tp.mCurY, mCurrDrawPaint);
                }
                nDrawHelper.NDrawSetStrokeWidth(currStrokeWidth);
                nDrawHelper.NDrawSetPaintColor(currColor);

                mCurrDrawPaint.setStrokeWidth((float) currStrokeWidth);
                mCurrDrawPaint.setColor(currColor);
                invalidate();
            }
        }
    }

    private boolean isRectEmpty(Rect rect) {
        if ((rect.right - rect.left) + 1 <= 0 || (rect.bottom - rect.top) + 1 <= 0) {
            return false;
        }
        return true;
    }

    private void eraseRect(Rect rect) {
        if (mCurrDrawRect.left > rect.left) {
            mCurrDrawRect.left = rect.left;
        }
        if (mCurrDrawRect.top > rect.top) {
            mCurrDrawRect.top = rect.top;
        }
        if (mCurrDrawRect.right < rect.right) {
            mCurrDrawRect.right = rect.right;
        }
        if (mCurrDrawRect.bottom < rect.bottom) {
            mCurrDrawRect.bottom = rect.bottom;
        }
    }

    private void currDrawRectMove(int mCurX, int mCurY) {
        if (mCurrDrawRect.left == 0 || mCurrDrawRect.left > mCurX) {
            mCurrDrawRect.left = mCurX;
        }
        if (mCurrDrawRect.top == 0 || mCurrDrawRect.top > mCurY) {
            mCurrDrawRect.top = mCurY;
        }
        if (mCurrDrawRect.right == 0 || mCurrDrawRect.right < mCurX) {
            mCurrDrawRect.right = mCurX;
        }
        if (mCurrDrawRect.bottom == 0 || mCurrDrawRect.bottom < mCurY) {
            mCurrDrawRect.bottom = mCurY;
        }
    }

    public void fullRefresh(View view) {
        view.invalidate(UPDATE_MODE_FULL_GC16);
    }


    public void setInputOffset(int offset_x, int offset_y) {

        nDrawHelper.NDrawSetInputOffset(offset_x, offset_y);
        intCanvasOffsetX = offset_x;
        intCanvasOffsetY = offset_y;

    }


    class TouchPath {
        public int mColor;
        public int mCurX;
        public int mCurY;
        public int mOldX;
        public int mOldY;
        public Rect mRect;
        public int mStrokeWidth;

        public TouchPath(int strokeWidth, int currColor, int currOldX, int currOldY, int currCurX, int currCurY, Rect currmRect) {
            this.mStrokeWidth = strokeWidth;
            this.mColor = currColor;
            this.mRect = currmRect;
            this.mOldX = currOldX;
            this.mOldY = currOldY;
            this.mCurX = currCurX;
            this.mCurY = currCurY;
        }
    }

    public void test_setNDrawMultiActiveRegion(int w, int h) {
        int Ndraw_region_left = 0;
        int Ndraw_region_top = getRelativeTop(NtxView.this);
        int Ndraw_region_right = Ndraw_region_left + w/3;
        int Ndraw_region_bottom = Ndraw_region_top + h/3;

        int Ndraw_region_left_2 = (w * 2)/3 ;
        int Ndraw_region_top_2 = getRelativeTop(NtxView.this);
        int Ndraw_region_right_2 = w;
        int Ndraw_region_bottom_2 = Ndraw_region_top_2 + h/3;

        int Ndraw_region_left_3 = w/3 ;
        int Ndraw_region_top_3 = getRelativeTop(NtxView.this) + h/3;
        int Ndraw_region_right_3 = Ndraw_region_left_3 + w/3;
        int Ndraw_region_bottom_3 = Ndraw_region_top_3 + h/3;


        int[] multi_active_region = {Ndraw_region_left, Ndraw_region_top, Ndraw_region_right, Ndraw_region_bottom,
                        Ndraw_region_left_2, Ndraw_region_top_2, Ndraw_region_right_2, Ndraw_region_bottom_2,
                        Ndraw_region_left_3, Ndraw_region_top_3, Ndraw_region_right_3, Ndraw_region_bottom_3};



        Log.d(TAG, "multi_active_region: " + Arrays.toString(multi_active_region));
        //nDrawHelper.NDrawInit(multi_active_region);
        nDrawHelper.NDrawSetDrawRegion(multi_active_region);

    }
}
