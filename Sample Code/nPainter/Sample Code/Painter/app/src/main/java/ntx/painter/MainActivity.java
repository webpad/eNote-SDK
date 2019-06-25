
package ntx.painter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.SensorManager;

import junit.framework.Assert;

import ntx.draw.nDrawHelper;
import ntx.painter.NtxView.PenType;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";

    private Context mContext;
    private NtxView myNewView;
    private LinearLayout mainLayout, painterViewLayout;

    Timer mTimer;
    private long fixed_full_refresh_time = 3 * 60 * 1000;

    private String strSaveImgPath;

    private Handler mHandler = new Handler();

    protected ReselectSpinner thicknessSpinner;
    protected ReselectSpinner mPenTypeSpinner;
    private ImageButton refreshAButton, refreshButton;
    private ImageButton thicknessButton, calibrationButton, cleanButton, rotateButton, backgroundButton, saveButton, infoButton;
    private ImageButton mPenTypeButton;
    protected TextView txtvThicknessNum;
    protected TextView mTxtPenStyle;
    protected Spinner mHorizontalSpinner, mVerticalSpinner, refreshModeSpinner;
    private boolean isRefreshModeSpinnerInitRun, isThicknessSpinnerInitRun, isPenTypeSpinnerInitRun;

    private Button backgroundBlackButton, backgroundWhiteButton;
    private Button autoDrawButton;
    private CheckBox chkbox_finger_touch;
    Dialog mDialog;

    AlertDialog.Builder mBuilder;
    AlertDialog dialog;

    private int calHorizontalPosition, calVerticalPosition;

    private int initHorizontalPosition, initVerticalPosition;
    private int isSavedInteger=0;


    static int pen_offset_x, pen_offset_y;


    private int[] thicknessChoices;
    private String[] mPentypeChoices;

    private boolean refresh_flag = false;

    private WindowManager mWindowManager;
    private Display mDisplay;

    private int getNavigationBarHeight() {
          	Resources resources =  mContext.getResources();
           	int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
         	int height = resources.getDimensionPixelSize(resourceId);

                		return height;
        	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setContentView(R.layout.main);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        thicknessSpinner = (ReselectSpinner) findViewById(R.id.toolbox_thickness_spinner);
        mPenTypeSpinner = (ReselectSpinner) findViewById(R.id.pen_type_spinner);
        thicknessButton = (ImageButton) findViewById(R.id.toolbox_thickness_button);
        mPenTypeButton  = (ImageButton) findViewById(R.id.pen_type_button);
        txtvThicknessNum = (TextView) findViewById(R.id.toolbox_thickness_number);
        mTxtPenStyle  = (TextView) findViewById(R.id.pen_type_string);
        calibrationButton = (ImageButton) findViewById(R.id.toolbox_calibration);

        cleanButton = (ImageButton) findViewById(R.id.toolbox_clean);
        rotateButton = (ImageButton) findViewById(R.id.toolbox_rotate);
        refreshModeSpinner = (Spinner) findViewById(R.id.toolbox_refresh_mode_spinner);
        backgroundButton = (ImageButton) findViewById(R.id.toolbox_background);
        saveButton = (ImageButton) findViewById(R.id.toolbox_save);
        infoButton = (ImageButton) findViewById(R.id.toolbox_info);
        refreshAButton = (ImageButton) findViewById(R.id.toolbox_full_refreshA);
        refreshButton = (ImageButton) findViewById(R.id.toolbox_full_refresh);

        backgroundWhiteButton = (Button) findViewById(R.id.btn_bg_white);
        backgroundBlackButton = (Button) findViewById(R.id.btn_bg_black);
        chkbox_finger_touch = (CheckBox) findViewById(R.id.chkbox_finger_touch);
        autoDrawButton = (Button) findViewById(R.id.auto_draw);

        painterViewLayout = (LinearLayout) findViewById(R.id.painter_view);

        myNewView = new NtxView(this);
        painterViewLayout.addView(myNewView);

        myNewView.getBottomHeight(getNavigationBarHeight());
        myNewView.setCurrentRotation(getRequestedOrientation());

        thicknessButton.setOnClickListener(mOnClickListener);
        mPenTypeButton.setOnClickListener(mOnClickListener);
        calibrationButton.setOnClickListener(mOnClickListener);
        cleanButton.setOnClickListener(mOnClickListener);
        rotateButton.setOnClickListener(mOnClickListener);
        backgroundButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        infoButton.setOnClickListener(mOnClickListener);
        refreshAButton.setOnClickListener(mOnClickListener);
        refreshButton.setOnClickListener(mOnClickListener);

        thicknessChoices = getResources().getIntArray(R.array.dlg_thickness_values);
        String[] thickness_spinner_string_table = new String[thicknessChoices.length];
        for (int i = 0; i < thicknessChoices.length; i++) {
            if (i == thicknessChoices.length - 1)
                thickness_spinner_string_table[i] = (String) "Pressure";
            else
                thickness_spinner_string_table[i] = (String) String.valueOf(thicknessChoices[i]);
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                mContext,
                R.layout.toolbox_spinner_item,
                thickness_spinner_string_table);
        mAdapter.setDropDownViewResource(R.layout.toolbox_spinner_item);
        thicknessSpinner.setAdapter(mAdapter);
        isThicknessSpinnerInitRun = true;
        thicknessSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        setToolboxThicknessValue(myNewView.getPenThickness());

        //pen type
        mPentypeChoices = getResources().getStringArray(R.array.pen_type_values);

        mAdapter = new ArrayAdapter<String>(
                mContext,
                R.layout.toolbox_spinner_item,
                mPentypeChoices);
        mAdapter.setDropDownViewResource(R.layout.toolbox_spinner_item);
        mPenTypeSpinner.setAdapter(mAdapter);
        isPenTypeSpinnerInitRun = true;
        mPenTypeSpinner.setOnItemSelectedListener(mOnItemSelectedListener);

        String[] refresh_mode_spinner_string_table = getResources().getStringArray(R.array.toolbox_refresh_mode);
        mAdapter = new ArrayAdapter<String>(
                mContext,
                R.layout.toolbox_spinner_item,
                refresh_mode_spinner_string_table);
        mAdapter.setDropDownViewResource(R.layout.toolbox_spinner_item);
        refreshModeSpinner.setAdapter(mAdapter);
        isRefreshModeSpinnerInitRun = true;
        refreshModeSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        refreshModeSpinner.setOnTouchListener(mSpinnerOnTouchListener);
        setToolBoxRefreshModeCurrentValue();

        backgroundWhiteButton.setOnClickListener(mOnClickListener);
        backgroundBlackButton.setOnClickListener(mOnClickListener);
        autoDrawButton.setOnClickListener(mOnClickListener);

        chkbox_finger_touch.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNewView.setFingerTouch(chkbox_finger_touch.isChecked());
            }
        });


        refreshAButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    refreshAButton.setImageResource(R.color.black);
                }

                return false;
            }
        });



        mBuilder=new AlertDialog.Builder(MainActivity.this);
        View mView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_calibration, null);
        mBuilder.setTitle("Calibration");

        mHorizontalSpinner = (Spinner) mView.findViewById(R.id.horizontal_offset_spinner);
        mVerticalSpinner = (Spinner) mView.findViewById(R.id.vertical_offset_spinner);

        ArrayAdapter<String> adapterHorizontal = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.horizontal_offset_list));
        ArrayAdapter<String> adapterVertical = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.vertical_offset_list));


        adapterHorizontal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterVertical.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mHorizontalSpinner.setAdapter(adapterHorizontal);
        mVerticalSpinner.setAdapter(adapterVertical);

        mHorizontalSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mVerticalSpinner.setOnItemSelectedListener(mOnItemSelectedListener);



        initHorizontalPosition=(adapterHorizontal.getCount()-1)/2;
        initVerticalPosition=(adapterHorizontal.getCount()-1)/2;

        isSavedInteger=Settings.System.getInt(getContentResolver(),"isSavedInteger",999);

        if(isSavedInteger==1){

            calHorizontalPosition=Settings.System.getInt(getContentResolver(),"calHorizontalPosition",0);
            calVerticalPosition=Settings.System.getInt(getContentResolver(),"calVerticalPosition",0);


        }else{

            calHorizontalPosition=initHorizontalPosition;
            calVerticalPosition=initVerticalPosition;

        }

            mHorizontalSpinner.setSelection(calHorizontalPosition);
            mVerticalSpinner.setSelection(calVerticalPosition);


        final int[] horizontal_offset_int_table=getResources().getIntArray(R.array.horizontal_offset);
        final int[] vertical_offset_int_table=getResources().getIntArray(R.array.vertical_offset);


        pen_offset_x=horizontal_offset_int_table[calHorizontalPosition];
        pen_offset_y=vertical_offset_int_table[calVerticalPosition];


        myNewView.setInputOffset(pen_offset_x, pen_offset_y);


        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                calHorizontalPosition=mHorizontalSpinner.getSelectedItemPosition();
                calVerticalPosition=mVerticalSpinner.getSelectedItemPosition();

                  pen_offset_x=horizontal_offset_int_table[calHorizontalPosition];
                  pen_offset_y=vertical_offset_int_table[calVerticalPosition];


                myNewView.setInputOffset(pen_offset_x, pen_offset_y);



                Settings.System.putInt(getContentResolver(),"calHorizontalPosition", calHorizontalPosition);
                Settings.System.putInt(getContentResolver(),"calVerticalPosition", calVerticalPosition);
                Settings.System.putInt(getContentResolver(),"isSavedInteger",1);


                dialogInterface.dismiss();


            }
        });


        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                mHorizontalSpinner.setSelection(calHorizontalPosition);
                mVerticalSpinner.setSelection(calVerticalPosition);
                dialogInterface.dismiss();




            }
        });




        mBuilder.setView(mView);

        dialog = mBuilder.create();
        dialog.setCancelable(false);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        nDrawHelper.NDrawInit(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        changeEinkControlPermission(false);
        Log.d(TAG,"onBackPressed");
        nDrawHelper.NDrawSkia2FBUnInit();
        //myNewView.onBackPressed();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeEinkControlPermission(true);
        myNewView.onResume();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                while (!myNewView.getFullRefreshable()) {
                    SystemClock.sleep(1000);
                }
                //RefreshHandler.sendEmptyMessage(1);//Bugfix: show white region when resume from suspend
            }
        }, 0, fixed_full_refresh_time);
        PowerEnhanceSet(1);



        if (refresh_flag){

            changeToA2DitheringMode();

            mHandler.postDelayed(runInvalidate,2000);

        }else{

            changeToGlobalReset();

        }


    }

    private void changeEinkControlPermission(boolean isForNtxAppsOnly) {
        Intent changePermissionIntent = new Intent("ntx.eink_control.CHANGE_PERMISSON");
        changePermissionIntent.putExtra("isPermissionNtxApp", isForNtxAppsOnly);
        sendBroadcast(changePermissionIntent);
    }

    private Handler RefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (myNewView.isEinkHardwareType())
                        myNewView.fullRefresh(mainLayout);
                    else
                        mainLayout.invalidate();
                    break;
            }
        }
    };

    private Spinner.OnTouchListener mSpinnerOnTouchListener = new Spinner.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                nDrawHelper.NDrawSwitch(false);
            }

            return false;
        }
    };

    private Button.OnClickListener mOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
            nDrawHelper.NDrawSwitch(false);
            switch (v.getId()) {
                case R.id.toolbox_thickness_button:
                    //trigger thickness spinner
                    ((ReselectSpinner) findViewById(R.id.toolbox_thickness_spinner)).performClick();
                    break;
                case R.id.pen_type_button:
                    //trigger thickness spinner
                    ((ReselectSpinner) findViewById(R.id.pen_type_spinner)).performClick();
                    break;
                case R.id.toolbox_calibration:


                    dialog.show();

                    break;
                case R.id.toolbox_clean:
                    myNewView.clear();
                    break;
                case R.id.toolbox_rotate:
                    // set Landscape/ Portrait
                    startRotate();

                    break;
                case R.id.toolbox_background:

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.wait));
                    mDialog = builder.create();
                    mDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                        }
                    }).start();
                    break;
                case R.id.toolbox_save:
                    Time now = new Time();
                    now.setToNow();
                    strSaveImgPath = Environment.getExternalStorageDirectory().getPath() + "/painter" + now.format("_%Y%m%d-%H%M%S.png");
                    if (myNewView.FileOutput(strSaveImgPath) == 0) {
                        mDialog = createDialog(
                                MainActivity.this,
                                String.format(getString(R.string.toolbox_save_msg_success), strSaveImgPath),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });
                        mDialog.show();
                    } else {
                        mDialog = createDialog(
                                MainActivity.this,
                                String.format(getString(R.string.toolbox_save_msg_fail), strSaveImgPath),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDialog.dismiss();
                                    }
                                });
                        mDialog.show();
                    }
                    break;
                case R.id.toolbox_info:
                    mDialog = createDialog(
                            MainActivity.this,
                            getString(R.string.msg_info),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });
                    mDialog.show();
                    break;
                case R.id.btn_bg_white:
                    myNewView.setCanvasStyle(1);
                    myNewView.setBackgroundPath("");
                    myNewView.clear();
                    break;
                case R.id.btn_bg_black:
                    myNewView.setCanvasStyle(2);
                    myNewView.setBackgroundPath("");
                    myNewView.clear();
                    break;
                case R.id.toolbox_full_refreshA:
                 //   refreshButton.setImageResource(R.drawable.ic_menu_reset);
                     if (refresh_flag == false){

                        refresh_flag = true;

                     }else if (refresh_flag == true){

                         refresh_flag = false;
                     }



                     if (refresh_flag){

                        refreshAButton.setImageResource(R.drawable.ic_menu_admode);

                        changeToA2DitheringMode();

                        myNewView.invalidate();

                     }else{

                        refreshAButton.setImageResource(R.drawable.ic_menu_reset);

                        changeToGlobalReset();

                        myNewView.invalidate();

                     }


                     break;

                case R.id.toolbox_full_refresh:

                    myNewView.invalidate();

                    Intent ac = new Intent("ntx.eink_control.QUICK_REFRESH");
                    ac.putExtra("updatemode",NtxView.UPDATE_MODE_FULL_GC16);
                    ac.putExtra("commandFromNtxApp", true);
                    sendBroadcast(ac);


                    break;


                default:
                    break;
            }
        }
    };


    public void changeToA2DitheringMode(){

        doDuRefresh();

        Intent a = new Intent("ntx.eink_control.GLOBAL_REFRESH");
        a.putExtra("updatemode",NtxView.UPDATE_MODE_GLOBAL_FULL_A2_WITH_DITHER);
        a.putExtra("commandFromNtxApp", true);
        sendBroadcast(a);

    }

    private void doDuRefresh(){

        Intent quickDuRefreshIntent = new Intent("ntx.eink_control.QUICK_REFRESH");
        quickDuRefreshIntent.putExtra("updatemode",NtxView.UPDATE_MODE_FULL_DU_WITH_DITHER);
        quickDuRefreshIntent.putExtra("commandFromNtxApp", true);
        sendBroadcast(quickDuRefreshIntent);

    }


    public void changeToGlobalReset(){

        Intent al = new Intent("ntx.eink_control.GLOBAL_REFRESH");
        al.putExtra("updatemode",NtxView.UPDATE_MODE_GLOBAL_RESET);
        al.putExtra("commandFromNtxApp", true);
        sendBroadcast(al);

    }




    public static Dialog createDialog(Context context, String msg, View.OnClickListener callbackOk) {
        Dialog dialog = new Dialog(context, R.style.dialog_custom);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int vWidth = dm.widthPixels;
        int vHeight = dm.heightPixels;
        LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.lldialog);
        ll.getLayoutParams().width = (int) (vWidth * 0.5);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        lp.y = (int) (vWidth * 0.4);

        Button btnOk = (Button) dialog.findViewById(R.id.dialog_button_ok);
        btnOk.setOnClickListener(callbackOk);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.message);
        txtMessage.setText(msg);
        return dialog;
    }

    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View item, int position, long id) {
            if (parent.getId() == R.id.toolbox_thickness_spinner) {
                if (isThicknessSpinnerInitRun) {
                    isThicknessSpinnerInitRun = false;
                    return;
                }
                if (position == thicknessChoices.length - 1) {
                    myNewView.setPenThicknessWithPressure(true);
                    setToolboxThicknessValue();
                } else {
                    int thickness = thicknessChoices[position];
                    //save current setting to the customized
                    thicknessChoices[thicknessChoices.length - 1] = thickness;
                    if (thickness == myNewView.getPenThickness() && (!myNewView.getPenThicknessWithPressure()))
                        return;
                    myNewView.setPenThickness(thickness);
                    setToolboxThicknessValue(thickness);
                }
            } else if (parent.getId() == R.id.toolbox_refresh_mode_spinner) {
                if (isRefreshModeSpinnerInitRun) {
                    isRefreshModeSpinnerInitRun = false;
                    return;
                }
                int refresh_mode_selected = NtxView.refresh_mode_table[position];
                if (refresh_mode_selected == myNewView.getRefreshMode()) return;
                myNewView.setRefreshMode(refresh_mode_selected);
            } else if (parent.getId() == R.id.pen_type_spinner) {
                if (isPenTypeSpinnerInitRun) {
                    isPenTypeSpinnerInitRun = false;
                    return;
                }

                int pen_type_selected = NtxView.pen_type_table[position];
                if (pen_type_selected == myNewView.getPenType()) return;
                myNewView.setPenType(pen_type_selected);
                updateIcon();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    };

    private void updateIcon() {
        //Log.d(TAG, "updateIcon:" +  mPentypeChoices[myNewView.getPenType()]);
        if ( myNewView.getPenType() == NtxView.PenType.PENCIL.ordinal() ) {
            mPenTypeButton.setImageResource(R.drawable.writing_pen_pencil);

        } else if (myNewView.getPenType() == PenType.PEN.ordinal()) {
            mPenTypeButton.setImageResource(R.drawable.writing_pen_pen);

        } else if (myNewView.getPenType() == PenType.CHINESE_BRUSH.ordinal()) {
            mPenTypeButton.setImageResource(R.drawable.writing_pen_brush);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        changeEinkControlPermission(false);
        myNewView.onPause();
        mTimer.cancel();
        PowerEnhanceSet(0);


        changeToGlobalReset();


    }

    private void startRotate() {
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        } else if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else{
            //default
            //    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        myNewView.setCurrentRotation(getRequestedOrientation());
        myNewView.setBackgroundPath("");


    }

    public void setToolBoxRefreshModeCurrentValue() {
        mHandler.removeCallbacks(runbSetRefreshMode);
        mHandler.postDelayed(runbSetRefreshMode, 500);
    }

    private Runnable runbSetRefreshMode = new Runnable() {
        @Override
        public void run() {
            setRefreshModeValue(myNewView.getRefreshMode());
        }
    };


    private Runnable runInvalidate = new Runnable() {
        @Override
        public void run() {


            myNewView.invalidate();

        }
    };



    public void setRefreshModeValue(int refresh_mode) {
        for (int i = 0; i < NtxView.refresh_mode_table.length; i++) {
            if (refresh_mode == NtxView.refresh_mode_table[i]) {
                refreshModeSpinner.setSelection(i);
            }
        }
    }

    public void setToolboxThicknessValue() {
        setToolboxThicknessValue(0);
    }

    public void setToolboxThicknessValue(int thickness) {
        for (int i = 0; i < thicknessChoices.length; i++) {
            if (thickness == thicknessChoices[i]) {
                thicknessSpinner.setSelection(i);
                break;
            }
        }

        if (myNewView.getPenThicknessWithPressure()) {
            thicknessButton.setImageResource(R.drawable.ic_menu_thickness_pressure);
            txtvThicknessNum.setText("P");
            return;
        }

        //update thickness button
        if ((thickness >= 9) && (thickness <= 14)) {
            thicknessButton.setImageResource(R.drawable.ic_menu_thickness_medium);
        } else if (thickness >= 15) {
            thicknessButton.setImageResource(R.drawable.ic_menu_thickness_large);
        } else {
            thicknessButton.setImageResource(R.drawable.ic_menu_thickness_small);
        }

        //update thickness number
        setThicknessNumText(String.valueOf(thickness));

        //sync to customized thickness
        thicknessChoices[thicknessChoices.length - 1] = thickness;

    }

    public void setThicknessNumText(String mString) {
        txtvThicknessNum.setText(mString);
    }

    private final static int REQUEST_CODE_PICK_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (mDialog.isShowing())
            mDialog.dismiss();

        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode != RESULT_OK)
                    break;
                Uri selectedImage = intent.getData();
                if (selectedImage != null) {
                    File mFile = new File(selectedImage.getPath());
                    myNewView.setBackgroundPath(mFile.getPath());
                }
                break;
            default:
                Assert.fail("Unknown request code");
        }
    }


    /**
     * Control the 2-Step-Suspend for Netronix eInk devices
     *
     * @param state
     *            1 is enable.
     *            0 is disable.
     */
    private void PowerEnhanceSet(int state) {
        Log.d(TAG, "PowerEnhanceSet = " + state);
        try {
            Settings.System.putInt(mContext.getContentResolver(), "power_enhance_enable", state);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "set POWER_ENHANCE_ENABLE error!!");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "Rotation = " + mDisplay.getRotation());
    }

}