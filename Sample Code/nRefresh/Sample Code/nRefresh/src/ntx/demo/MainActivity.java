
package ntx.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import ntx.refresh.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
	
	

	/**
	 * The waveform mode of init
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_INIT				= 0x00000000;

	/**
	 * The waveform mode of 2 gray levels(Grey->White/Grey->Black)
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_DU				= 0x00000001;

	/**
	 * The waveform mode of 16 gray levels
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_GC16				= 0x00000002;

	/**
	 * The waveform mode of 4 gray levels
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_GC4				= 0x00000003;

	/**
	 * The waveform mode for fast page turn
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_A2				= 0x00000005;

	/**
	 * The waveform mode for text with white background
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_GL16				= 0x00000006;

	/**
	 * The waveform mode for text with white background
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_GLR16			= 0x00000007;

	/**
	 * The waveform mode for text and graphics with white background
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_GLD16			= 0x00000008;

	/**
	 * The waveform mode of auto selected gray levels by painted data
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_AUTO				= 0x00000004;

	/**
	 * The Mask of waveform mode
	 * 
	 * @hide
	 */
	public static final int	EINK_WAVEFORM_MODE_MASK				= 0x0000000F;

	/**
	 * The regional update mode
	 * 
	 * @hide
	 */
	public static final int	EINK_AUTO_MODE_REGIONAL				= 0x00000000;

	/**
	 * The automatic update mode
	 * 
	 * @hide
	 */
	public static final int	EINK_AUTO_MODE_AUTOMATIC			= 0x00000010;

	/**
	 * The mask of the auto mode
	 * 
	 * @hide
	 */
	public static final int	EINK_AUTO_MODE_MASK					= 0x00000010;

	/**
	 * The partial update mode in region
	 * 
	 * @hide
	 */
	public static final int	EINK_UPDATE_MODE_PARTIAL			= 0x00000000;

	/**
	 * The full update mode in region
	 * 
	 * @hide
	 */
	public static final int	EINK_UPDATE_MODE_FULL				= 0x00000020;

	/**
	 * The Mask of update mode
	 * 
	 * @hide
	 */
	public static final int	EINK_UPDATE_MODE_MASK				= 0x00000020;

	/**
	 * The waveform mode of no wait update
	 * 
	 * @hide
	 */
	public static final int	EINK_WAIT_MODE_NOWAIT				= 0x00000000;

	/**
	 * The waveform mode of wait update
	 * 
	 * @hide
	 */
	public static final int	EINK_WAIT_MODE_WAIT					= 0x00000040;

	/**
	 * The Mask of waveform mode
	 * 
	 * @hide
	 */
	public static final int	EINK_WAIT_MODE_MASK					= 0x00000040;

	/**
	 * The waveform mode of nocombination
	 * 
	 * @hide
	 */
	public static final int	EINK_COMBINE_MODE_NOCOMBINE			= 0x00000000;

	/**
	 * The waveform mode of combine(inactive)
	 * 
	 * @hide
	 */
	public static final int	EINK_COMBINE_MODE_COMBINE			= 0x00000080;

	/**
	 * The Mask of combination mode
	 * 
	 * @hide
	 */
	public static final int	EINK_COMBINE_MODE_MASK				= 0x00000080;

	/**
	 * The waveform mode of no dithering
	 * 
	 * @hide
	 */
	public static final int	EINK_DITHER_MODE_NODITHER			= 0x00000000;

	/**
	 * The waveform mode of dithering
	 * 
	 * The dithering processing will be done to enhance the gray-tone of graphics. The target color depth is selected in
	 * dither_color ( Y4 or Y1 ). Since auto waveform is selected before dithering, the dithering flag will not affect
	 * the result of auto waveform selection.
	 * 
	 * @hide
	 */
	public static final int	EINK_DITHER_MODE_DITHER				= 0x00000100;

	/**
	 * The Mask of dithering mode
	 * 
	 * @hide
	 */
	public static final int	EINK_DITHER_MODE_MASK				= 0x00000100;

	/**
	 * The waveform mode of no invert
	 * 
	 * @hide
	 */
	public static final int	EINK_INVERT_MODE_NOINVERT			= 0x00000000;

	/**
	 * The waveform mode of invert
	 * 
	 * @hide
	 */
	public static final int	EINK_INVERT_MODE_INVERT				= 0x00000200;

	/**
	 * The Mask of invert mode
	 * 
	 * @hide
	 */
	public static final int	EINK_INVERT_MODE_MASK				= 0x00000200;

	/**
	 * The waveform mode of no converting
	 * 
	 * @hide
	 */
	public static final int	EINK_CONVERT_MODE_NOCONVERT			= 0x00000000;

	/**
	 * The waveform mode of convert
	 * 
	 * @hide
	 */
	public static final int	EINK_CONVERT_MODE_CONVERT			= 0x00000400;

	/**
	 * The Mask of converting mode
	 * 
	 * @hide
	 */
	public static final int	EINK_CONVERT_MODE_MASK				= 0x00000400;

	/**
	 * The waveform mode of using reagl algo
	 * 
	 * @hide
	 */
	public static final int	EINK_REAGL_MODE_REAGL				= 0x00000000;

	/**
	 * The waveform mode of using reagl-D algo
	 * 
	 * @hide
	 */
	public static final int	EINK_REAGL_MODE_REAGLD				= 0x00001000;

	/**
	 * The Mask of reagl algo
	 * 
	 * @hide
	 */
	public static final int	EINK_REAGL_MODE_MASK				= 0x00001000;

	/**
	 * The waveform mode of no monochrome
	 * 
	 * @hide
	 */
	public static final int	EINK_MONOCHROME_MODE_NOMONOCHROME	= 0x00000000;

	/**
	 * The waveform mode of monochrome
	 * 
	 * @hide
	 */
	public static final int	EINK_MONOCHROME_MODE_MONOCHROME		= 0x00000800;

	/**
	 * The Mask of monochrome mode
	 * 
	 * @hide
	 */
	public static final int	EINK_MONOCHROME_MODE_MASK			= 0x00000800;


	/** @hide */
	//public static final int UI_DEFAULT_MODE = EINK_CONVERT_MODE_NOCONVERT|EINK_INVERT_MODE_NOINVERT|EINK_DITHER_MODE_NODITHER|EINK_COMBINE_MODE_NOCOMBINE|
	//EINK_WAIT_MODE_NOWAIT|EINK_UPDATE_MODE_PARTIAL|EINK_AUTO_MODE_REGIONAL|EINK_WAVEFORM_MODE_AUTO;
	public static final int	UI_DEFAULT_MODE						= 0x02;

	/** @hide */
	public static final int	UI_FULLREFRESH_MODE					= EINK_CONVERT_MODE_NOCONVERT
																		| EINK_INVERT_MODE_NOINVERT
																		| EINK_DITHER_MODE_NODITHER
																		| EINK_COMBINE_MODE_NOCOMBINE
																		| EINK_WAIT_MODE_NOWAIT | EINK_UPDATE_MODE_FULL
																		| EINK_AUTO_MODE_REGIONAL
																		| EINK_WAVEFORM_MODE_GC16;

	/** @hide */
	public static final int	UI_MONOCHROME_MODE					= EINK_MONOCHROME_MODE_MONOCHROME
																		| EINK_CONVERT_MODE_NOCONVERT
																		| EINK_INVERT_MODE_NOINVERT
																		| EINK_DITHER_MODE_NODITHER
																		| EINK_COMBINE_MODE_NOCOMBINE
																		| EINK_WAIT_MODE_NOWAIT
																		| EINK_UPDATE_MODE_PARTIAL
																		| EINK_AUTO_MODE_REGIONAL
																		| EINK_WAVEFORM_MODE_GC16;

	private Spinner			spinner1, spinner2, spinner3, spinner4, spinner5;
	private Button			btnApply, btnFullRefresh;
	
	
    private static final String TAG = "MainActivity";

    private Activity mActivity = null;

    private Context mContext;

    private NtxImageViewClass iv;

    private FrameLayout mFrameLayout;

    PowerManager mPM;
    
    private int einkMode;
    

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mActivity = this;
        mContext = getApplicationContext();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        mFrameLayout = (FrameLayout) findViewById(R.id.show_img_framelayout);
        iv = new NtxImageViewClass(getApplicationContext());
        mFrameLayout.addView(iv);

        mPM = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);

        setResImages();
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResImages();
            }
        });
        
        
        initUI();
        
        
        
    }
    
    
    public void initUI() {

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);
		spinner4 = (Spinner) findViewById(R.id.spinner4);
		spinner5 = (Spinner) findViewById(R.id.spinner5);
		btnApply=(Button) findViewById(R.id.btnApply);
		btnFullRefresh=(Button) findViewById(R.id.btnFullRefresh);
		
		
		// set spinner default value
		String defaultitem1 = "GC16";
		String defaultitem2 = "FULL";
		ArrayAdapter myAdap = (ArrayAdapter) spinner1.getAdapter();
		int spinnerPosition = myAdap.getPosition(defaultitem1);
		spinner1.setSelection(spinnerPosition);
		
		myAdap = (ArrayAdapter) spinner2.getAdapter();
		spinnerPosition = myAdap.getPosition(defaultitem2);
		spinner2.setSelection(spinnerPosition);
		

		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				applyUpdateParams();
				
				Toast toast = Toast.makeText(getApplicationContext(), "Applied Successfully", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
				
				
				
			}

		});
		
		btnFullRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				doGC16FullRefresh();
				
			}
		});
		
		
		applyUpdateParams();
		
		
	}
    
    
    public void applyUpdateParams() {
    	


		String waveformStr = String.valueOf(spinner1.getSelectedItem());
		String updateStr = String.valueOf(spinner2.getSelectedItem());
		String waitStr = String.valueOf(spinner3.getSelectedItem());
		String monochromeStr = String.valueOf(spinner4.getSelectedItem());
		String hardwareDitherStr = String.valueOf(spinner5.getSelectedItem());
		

		int waveform = 0;
		if (waveformStr.equalsIgnoreCase("DU")) {
			waveform = EINK_WAVEFORM_MODE_DU;
		} else if (waveformStr.equalsIgnoreCase("GC16")) {
			waveform = EINK_WAVEFORM_MODE_GC16;
		} else if (waveformStr.equalsIgnoreCase("GC4")) {
			waveform = EINK_WAVEFORM_MODE_GC4;
		} else if (waveformStr.equalsIgnoreCase("A2")) {
			waveform = EINK_WAVEFORM_MODE_A2;
		} else if (waveformStr.equalsIgnoreCase("GL16")) {
			waveform = EINK_WAVEFORM_MODE_GL16;
		} else if (waveformStr.equalsIgnoreCase("GLR16")) {
			waveform = EINK_WAVEFORM_MODE_GLR16;
		} else if (waveformStr.equalsIgnoreCase("GLD16")) {
			waveform = EINK_WAVEFORM_MODE_GLD16;
		}

		int update = 0;
		if (updateStr.equalsIgnoreCase("Patrial")) {
			update = EINK_UPDATE_MODE_PARTIAL;
		} else if (updateStr.equalsIgnoreCase("FULL")) {
			update = EINK_UPDATE_MODE_FULL;
		}

		int wait = 0;
		if (waitStr.equalsIgnoreCase("NOWAIT")) {
			wait = EINK_WAIT_MODE_NOWAIT;
		} else if (waitStr.equalsIgnoreCase("WAIT")) {
			wait = EINK_WAIT_MODE_WAIT;
		}

		int monochrome = 0;
		if (monochromeStr.equalsIgnoreCase("NOMONOCHROME")) {
			monochrome = EINK_MONOCHROME_MODE_NOMONOCHROME;
		} else if (monochromeStr.equalsIgnoreCase("MONOCHROME")) {
			monochrome = EINK_MONOCHROME_MODE_MONOCHROME;
		}
		
		int hardware_dither = 0;
		if(hardwareDitherStr.equals("NONE")){
			hardware_dither = EINK_DITHER_MODE_NODITHER;
		} else if(hardwareDitherStr.equals("DITHER")){
			hardware_dither = EINK_DITHER_MODE_DITHER;
		}
		


		einkMode = waveform | update | wait | monochrome | hardware_dither;
    	
		iv.setUPDATE_MODE_SCREEN(einkMode);
    	
    	
    }
    
    
    public void doGC16FullRefresh(){

        Intent fullRefreshIntent = new Intent("ntx.eink_control.QUICK_REFRESH");
        fullRefreshIntent.putExtra("updatemode",NtxImageViewClass.UPDATE_MODE_FULL_GC16);

        sendBroadcast(fullRefreshIntent);


    }
    
    

    @Override
    public void onResume() {
        super.onResume();
        setWakeLock(mContext);
        PowerEnhanceSet(1);
    }

    private static List<Integer> imageValues = getImageValues();

    public static List<Integer> getImageValues()
    {
        try
        {
            Field[] drawableFields = R.drawable.class.getFields();
            List<Integer> resourceValues = new ArrayList<Integer>();
            for (Field field : drawableFields)
            {
                if (field.getName().indexOf("a_") != -1)
                {
                    resourceValues.add(field.getInt(R.drawable.class));
                }
            }
            return resourceValues;
        } catch (Exception e)
        {
            return null;
        }
    }

    private int page_index = -1;

    private void setResImages() {
        if (imageValues != null) {
            if (imageValues.size() != 0)
            {
                if (++page_index >= imageValues.size())
                {
                    page_index = 0;
                }
            }
        }
        if (page_index > -1)
        {
            iv.setImageDrawable(getResources().getDrawable(imageValues.get(page_index)));
        } else {
            iv.setImageDrawable(getResources().getDrawable(R.drawable.default_photo));
        }
    }

    private WakeLock mWakeLock = null;

    private void setWakeLock(Context context) {
        if ((mWakeLock != null) && (mWakeLock.isHeld())) {
            return;
        }
        mWakeLock = mPM.newWakeLock(
                PowerManager.FULL_WAKE_LOCK
                , this.getClass().getCanonicalName());
        mWakeLock.setReferenceCounted(false);
        mWakeLock.acquire();
    }

    public void cancelWakeLock() {
        final Handler handler = new Handler();
        if ((mWakeLock != null) && (mWakeLock.isHeld())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }).start();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                mWakeLock.release();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            mWakeLock = null;
        }
    };

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        cancelWakeLock();
        PowerEnhanceSet(0);
    }

    /**
     * Control the 2-Step-Suspend for Netronix eInk devices
     *
     * @param state 1 is enable. 0 is disable.
     */
    private void PowerEnhanceSet(int state) {
        Log.e("DANIEL", "PowerEnhanceSet(int state) = " + state);
        try {
            Settings.System.putInt(mContext.getContentResolver(), "power_enhance_enable", state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
