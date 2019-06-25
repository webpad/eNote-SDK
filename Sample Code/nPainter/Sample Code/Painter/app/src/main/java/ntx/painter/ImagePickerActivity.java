package ntx.painter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntx.note.asynctask.BitmapWorkerTask;

@SuppressLint("DefaultLocale")
public class ImagePickerActivity extends Activity implements OnItemClickListener {
    private final static String TAG = "ImagePickerActivity";

    public String[] searchImageType = new String[]{".png", ".jpg", ".jpeg", ".bmp"}; //auto list files

    private final static int NumPerPage_Portrait = 9; //this should be fit real status
    private final static int NumPerPage_Landscape = 8; //this should be fit real status

    private final static int GridView_Landscape_height_6Inch = 830;
    private final static int GridView_Landscape_height_13Inch = 930;

    private int numPerPage = 0; //this should be fit real status

    protected File sdcardDir, externalCardDir = null;

    private GridView fileGrid;
    private SimpleAdapter simpleAdapter;

    private List<String> resultPath = new ArrayList<String>();
    private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

    private TextView txtv_pageinfo;
    private ImageView imgv_pre_page, imgv_next_page;
    private int totalPage, currentPage = 0;
    public BitmapWorkerTask[] asyncTask = new BitmapWorkerTask[NumPerPage_Portrait];

    public final String PATH_SDCARD = Environment.getExternalStorageDirectory().getPath();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker);

        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            numPerPage = NumPerPage_Portrait;
        } else {
            numPerPage = NumPerPage_Landscape;
        }

        sdcardDir = new File(PATH_SDCARD);
        searchFiles(sdcardDir);

        Collections.sort(resultPath, new SortIgnoreCase());

        totalPage = getTotalPage();
        if (currentPage == 0)
            currentPage = 1;

        //add into items for showing
        updateItems();

        fileGrid = (GridView) findViewById(R.id.filepicker_file_grid);


        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {

        } else {

            ViewGroup.LayoutParams layoutParams = fileGrid.getLayoutParams();
            if (NtxView.isEinkHandWritingHardwareType() && NtxView.isEink6InchHardwareType()) {
                layoutParams.height = GridView_Landscape_height_6Inch;
            } else {
                layoutParams.height = GridView_Landscape_height_13Inch;
            }

            fileGrid.setLayoutParams(layoutParams);
        }

        simpleAdapter = new SimpleAdapter(this,
                items, R.layout.image_picker_item, new String[]{"image", "text"},
                new int[]{R.id.picker_image, R.id.picker_text}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                int mWidth = 205 * 2;
                int mHeight = 150 * 2;
                if (convertView == null) {
                    convertView = super.getView(position, convertView, parent);
                }

                final TextView nameView = (TextView) convertView.findViewById(R.id.picker_text);

                final int indexNameS1 = items.get(position).get("image").toString().lastIndexOf('/') + 1;
                final String filenameS1 = ((indexNameS1 > 0) ? items.get(position).get("image").toString().substring(indexNameS1).toLowerCase().intern() : "");
                nameView.setText(filenameS1);

                final ImageView coverView = (ImageView) convertView.findViewById(R.id.picker_image);
                if (position > 0) {
                    coverView.setImageBitmap(null);
                }

                if (asyncTask[position] != null && asyncTask[position].getStatus().equals(Status.RUNNING)) {
                    asyncTask[position].cancel(true);
                }

                asyncTask[position] = new BitmapWorkerTask(coverView, mWidth, mHeight);
                asyncTask[position].execute(items.get(position).get("image").toString());
                return convertView;
            }
        };
        fileGrid.setAdapter(simpleAdapter);
        fileGrid.setOnItemClickListener(this);

        if (resultPath.isEmpty()) {
            Toast.makeText(ImagePickerActivity.this, R.string.image_pick_no_image, Toast.LENGTH_LONG).show();
        }

        setTitle(R.string.image_pick_title);

        txtv_pageinfo = (TextView) findViewById(R.id.page_info);
        imgv_pre_page = (ImageView) findViewById(R.id.imgb_pre_page);
        imgv_next_page = (ImageView) findViewById(R.id.imgb_next_page);

        imgv_pre_page.setOnClickListener(instantKeyListener);
        imgv_next_page.setOnClickListener(instantKeyListener);

    }

    private OnClickListener instantKeyListener = new OnClickListener() {
        // TODO
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.imgb_pre_page:
                    currentPage--;
                    if (currentPage <= 0)
                        currentPage = totalPage;

                    txtv_pageinfo.setText(currentPage + " of " + totalPage);
                    updateItems();
                    simpleAdapter.notifyDataSetChanged();
                    break;
                case R.id.imgb_next_page:
                    currentPage++;
                    if (currentPage > totalPage)
                        currentPage = 1;

                    txtv_pageinfo.setText(currentPage + " of " + totalPage);
                    updateItems();
                    simpleAdapter.notifyDataSetChanged();
                    break;
                default:
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
        int realPos = 0;

        realPos = numPerPage * (currentPage - 1) + pos;
        String path = resultPath.get(realPos);
        final File f = new File(path);

        if (f.isFile())
            pickFile(f);
    }


    @Override
    protected void onPause() {
        super.onPause();
        changeEinkControlPermission(false);
    }

    private void pickFile(File file) {
        Intent intent = getIntent();
        intent.setData(Uri.fromFile(file));
        setResult(RESULT_OK, intent);
        finish();
    }

    //scan all the specific type files. ex:*.note
    public void searchFiles(File file) {
        File[] the_Files = file.listFiles();

        if (the_Files == null)
            return;

        //search all dirs and files
        for (File tempF : the_Files) {
            if (tempF.isDirectory()) {
                if (!tempF.isHidden())
                    searchFiles(tempF);
            } else {
                try {
                    boolean isImage = false;
                    //compare file. if the key is matched, return value > -1.
                    for (String type : searchImageType) {

                        final int index = tempF.getPath().lastIndexOf('.');
                        final String myExtension = ((index > 0) ? tempF.getPath().substring(index).toLowerCase().intern() : "");

                        if (myExtension.equals(type) && tempF.getName().substring(0, 1).equals(".") == false) {
                            isImage = true;
                            break;
                        }
                    }
                    if (isImage) {
                        //add matched path into array
                        resultPath.add(tempF.getPath());
                    }
                } catch (Exception e) {
                    Toast.makeText(ImagePickerActivity.this, "error - searching image files.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeEinkControlPermission(true);

        // update page info
        totalPage = getTotalPage();
        if (currentPage == 0)
            currentPage = 1;
        txtv_pageinfo.setText(currentPage + " of " + totalPage);

        if (totalPage == 1) { //only one page
            txtv_pageinfo.setVisibility(View.INVISIBLE);
            imgv_pre_page.setVisibility(View.INVISIBLE);
            imgv_next_page.setVisibility(View.INVISIBLE);
        } else {
            txtv_pageinfo.setVisibility(View.VISIBLE);
            imgv_pre_page.setVisibility(View.VISIBLE);
            imgv_next_page.setVisibility(View.VISIBLE);
        }
    }

    private void changeEinkControlPermission(boolean isForNtxAppsOnly) {
        Intent changePermissionIntent = new Intent("ntx.eink_control.CHANGE_PERMISSON");
        changePermissionIntent.putExtra("isPermissionNtxApp", isForNtxAppsOnly);
        sendBroadcast(changePermissionIntent);
    }

    // calculate total page
    private int getTotalPage() {
        int totalpage = resultPath.size() / (numPerPage);

        if (resultPath.size() % (numPerPage) != 0 | resultPath.size() / (numPerPage) == 0)
            totalpage++;
        return totalpage;
    }

    // update items for showing
    private void updateItems() {
        if (items.size() != 0)
            items.clear();

        for (int i = numPerPage * (currentPage - 1); i < numPerPage * currentPage; i++) {
            if (i >= resultPath.size())
                break;

            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", resultPath.get(i));
            items.add(item);
        }
    }

    public class SortIgnoreCase implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;

            final int indexNameS1 = s1.lastIndexOf('/');
            final String filenameS1 = ((indexNameS1 > 1) ? s1.substring(indexNameS1).toLowerCase().intern() : "");

            final int indexNameS2 = s2.lastIndexOf('/');
            final String filenameS2 = ((indexNameS2 > 1) ? s2.substring(indexNameS2).toLowerCase().intern() : "");

            return filenameS1.toLowerCase().compareTo(filenameS2.toLowerCase());
        }
    }
}