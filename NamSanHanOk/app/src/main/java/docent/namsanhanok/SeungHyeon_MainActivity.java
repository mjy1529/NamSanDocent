//package docent.namsanhanok;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import com.heinrichreimersoftware.materialdrawer.DrawerView;
//import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
//import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
//import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
//
///**
// * Created by hugeterry(http://hugeterry.cn)
// */
//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQUEST_CAMERA = 0;
//    private static final int REQUEST_MICROPHONE = 1;
//    private static final int REQUEST_EXTERNAL_STORAGE = 2;
//    private int PermissionComplete[] = { 0, 0 };
//
//    private static String auth_key_="";
//    private static String id_= "";
//    private static String pswd_ = "";
//
//    private CoordinatorTabLayout mCoordinatorTabLayout;
//    private int[] mImageArray, mColorArray;
//    private ArrayList<Fragment> mFragments;
//
//    //private TimerTask timer_task_;
//    //private final Handler handler_ = new Handler();
//    private String cmd_status_ = "fail";
//    private Map<String, ContentItem> content_list_map_ = new HashMap<String, ContentItem>();
//
//    // 상단메뉴 이름설정
//    private final String[] mTitles = {DEFINITION.TAB0, DEFINITION.TAB1, DEFINITION.TAB2, DEFINITION.TAB3};
//    private ViewPager mViewPager;
//    private DrawerLayout mDrawerLayout;
//
//    //drawer고양이 예제
//    private DrawerView drawer;
//    private ActionBarDrawerToggle drawerToggle;
//    @SuppressLint("ResourceAsColor")
//
//    //vod 리스트를 가져오기 위해 tag가 일단 필수적으로 필요하기 때문에
//    String vod_tag ="entertainment";
//    int isTitle = 5;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        Intent intent=new Intent(this.getIntent());
//        auth_key_ = intent.getStringExtra("auth_key");
//        id_ = intent.getStringExtra("id");
//        pswd_ = intent.getStringExtra("pswd");
//
//        initFragments();
//        initViewPager();
//        mImageArray = new int[]{
//                R.mipmap.bg_android,
//                R.mipmap.bg_ios,
//                R.mipmap.bg_js,
//                R.mipmap.bg_other};
//        mColorArray = new int[]{
//                android.R.color.holo_blue_light,
//                android.R.color.holo_red_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_green_light};
//
//        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
//        mCoordinatorTabLayout.setTranslucentStatusBar(this)
//                .setTitle(DEFINITION.APP_NAME)
//                .setBackEnable(true)
//                .setImageArray(mImageArray, mColorArray)
//                .setupWithViewPager(mViewPager);
//
//        // timer list 업데이트
////        timer_task_ = new TimerTask() {
////            @Override
////            public void run() {
////                TimerUpdate();
////            }
////        };
////        Timer timer = new Timer();
////        timer.schedule(timer_task_, 0,60000);
//
//        MainFragment mainFragment = (MainFragment)mFragments.get(0);
//
//        //tab_position_ = mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition();
//        mCoordinatorTabLayout.getTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                String title = tab.getText().toString();
//                int position = tab.getPosition();
//
//                if (select_tab_cmd(title) == false) {
//                    return;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                String title = tab.getText().toString();
//                int position = tab.getPosition();
//                MainFragment mainFragment = (MainFragment)mFragments.get(position);
//                mainFragment.clearContentList();
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                //String tt = tab.getText().toString();
//            }
//        });
//
///*
//        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                menuItem.setChecked(true);
//                mDrawerLayout.closeDrawers();
//
//                int id = menuItem.getItemId();
//                switch (id) {
//                    case R.id.navigation_item_attachment:
//                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
//                        break;
//
//                    case R.id.navigation_item_images:
//                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
//                        break;
//
//                    case R.id.navigation_item_location:
//                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
//                        break;
//
//                    case R.id.nav_sub_menu_item01:
//                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
//                        break;
//
//                    case R.id.nav_sub_menu_item02:
//                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
//                        break;
//
//                }
//
//                return true;
//            }
//        });
//*/
//
//// ============================ Drawer UI 관련 생성 시작 ======================================= //
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer = (DrawerView) findViewById(R.id.drawer);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//
//        drawerToggle = new ActionBarDrawerToggle(
//                this,
//                drawerLayout,
//                toolbar,
//                R.string.drawer_open,
//                R.string.drawer_close
//        ) {
//
//            public void onDrawerClosed(View view) {
//                invalidateOptionsMenu();
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                invalidateOptionsMenu();
//            }
//        };
//
//        drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.color_primary_dark_3));
//        drawerLayout.addDrawerListener(drawerToggle);
//        drawerLayout.closeDrawer(drawer);
//
//        drawer.addItem(new DrawerItem()
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//                .setTextSecondary(getString(R.string.lorem_ipsum_long))
//        );
//
//        drawer.addItem(new DrawerItem()
//                .setImage(ContextCompat.getDrawable(this, R.drawable.ic_email))
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//                .setTextSecondary(getString(R.string.lorem_ipsum_long))
//        );
//
//        drawer.addDivider();
//
//        drawer.addItem(new DrawerItem()
//                .setRoundedImage((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cat_1))
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//                .setTextSecondary(getString(R.string.lorem_ipsum_long))
//        );
//
//        drawer.addItem(new DrawerHeaderItem().setTitle(getString(R.string.lorem_ipsum_short)));
//
//        drawer.addItem(new DrawerItem()
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//        );
//
//        drawer.addItem(new DrawerItem()
//                .setRoundedImage((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cat_2), DrawerItem.SMALL_AVATAR)
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//                .setTextSecondary(getString(R.string.lorem_ipsum_long), DrawerItem.THREE_LINE)
//        );
//
//        drawer.selectItem(1);
//        drawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
//            @Override
//            public void onClick(DrawerItem item, long id, int position) {
//                drawer.selectItem(position);
//                Toast.makeText(MainActivity.this, "Clicked item #" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        drawer.addFixedItem(new DrawerItem()
//                .setRoundedImage((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cat_2), DrawerItem.SMALL_AVATAR)
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//        );
//
//        drawer.addFixedItem(new DrawerItem()
//                .setImage(ContextCompat.getDrawable(this, R.drawable.ic_flag))
//                .setTextPrimary(getString(R.string.lorem_ipsum_short))
//        );
//
//        drawer.setOnFixedItemClickListener(new DrawerItem.OnItemClickListener() {
//            @Override
//            public void onClick(DrawerItem item, long id, int position) {
//                drawer.selectFixedItem(position);
//                Toast.makeText(MainActivity.this, "Clicked fixed item #" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        drawer.addProfile(new DrawerProfile()
//                .setId(1)
//                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cat_1))
//                .setBackground(ContextCompat.getDrawable(this, R.drawable.cat_wide_1))
//                .setName(getString(R.string.lorem_ipsum_short))
//                .setDescription(getString(R.string.lorem_ipsum_medium))
//        );
//
//        drawer.addProfile(new DrawerProfile()
//                .setId(2)
//                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cat_2))
//                .setBackground(ContextCompat.getDrawable(this, R.drawable.cat_wide_1))
//                .setName(getString(R.string.lorem_ipsum_short))
//        );
//
//        drawer.addProfile(new DrawerProfile()
//                .setId(3)
//                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cat_1))
//                .setBackground(ContextCompat.getDrawable(this, R.drawable.cat_wide_2))
//                .setName(getString(R.string.lorem_ipsum_short))
//                .setDescription(getString(R.string.lorem_ipsum_medium))
//        );
//
//
//        drawer.setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
//            @Override
//            public void onClick(DrawerProfile profile, long id) {
//                Toast.makeText(MainActivity.this, "Clicked profile *" + id, Toast.LENGTH_SHORT).show();
//            }
//        });
//        drawer.setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
//            @Override
//            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
//                Toast.makeText(MainActivity.this, "Switched from profile *" + oldId + " to profile *" + newId, Toast.LENGTH_SHORT).show();
//            }
//        });
//        // ============================ Drawer UI 관련 생성 끝 ======================================= //
//    }
//
//    private void initFragments() {
//        mFragments = new ArrayList<>();
//        for (String title : mTitles) {
//            mFragments.add(MainFragment.getInstance(title, id_, pswd_, auth_key_));
//        }
//    }
//
//    private void initViewPager() {
//        mViewPager = (ViewPager) findViewById(R.id.vp);
//        mViewPager.setOffscreenPageLimit(4);
//        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
//    }
///*
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        switch (id) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//            case R.id.action_settings:
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//*/
//    /*
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//            return true;
//        }
//
//        switch (item.getItemId()) {
//            case R.id.action_about:
//                //IntentUtils.openUrl(this, "https://github.com/hugeterry/CoordinatorTabLayout");
//                break;
//            case R.id.action_about_me:
//                IntentUtils.openUrl(this, "http://hugeterry.cn/about");
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//*/
//    /*
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        //return super.onCreateOptionsMenu(menu);
//        return true;
//    }
//*/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (drawerToggle.onOptionsItemSelected(item)) {
////            return true;
////        }
//
////여기에 카메라 버튼 추가
//        switch (item.getItemId()) {
//            case R.id.action_record:
//                boolean isCameraPermission = grantCameraPermission();
//                if (isCameraPermission == true) {
//                    boolean isMicPermission = grantMicPermission();
//                    if (isMicPermission == true) {
//                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                        intent.putExtra("auth_key", auth_key_);
//                        intent.putExtra("id", id_);
//                        startActivity(intent);
//                    }
//                }
//
////                boolean isCameraPermission = grantCameraPermission();
////                boolean isMicPermission = grantMicPermission();
////                if ( isCameraPermission == true && isMicPermission == true) {
////                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
////                    intent.putExtra("auth_key", auth_key_);
////                    intent.putExtra("id", id_);
////                    startActivity(intent);
////                }
////                break;
//        }
////냥
//        return super.onOptionsItemSelected(item);
////        return true;
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
//
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        drawerToggle.syncState();
//    }
//
//    private boolean grantCameraPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                PermissionComplete[REQUEST_CAMERA] = 1;
//                Log.v("MainActivity","Camera Permission is granted");
//                return true;
//            }else{
//                Log.v("MainActivity","Camera Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
//                return false;
//            }
//        }else{
//            Toast.makeText(this, "Camera Permission is Grant", Toast.LENGTH_SHORT).show();
//            Log.d("MainActivity", "Camera Permission is Grant ");
//            return true;
//        }
//    }
//
//    private boolean grantMicPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//                PermissionComplete[REQUEST_MICROPHONE] = 1;
//                Log.v("MainActivity","Mic Permission is granted");
//                return true;
//            }else{
//                Log.v("MainActivity","Mic Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
//                return false;
//            }
//        }else{
//            Toast.makeText(this, "Mic Permission is Grant", Toast.LENGTH_SHORT).show();
//            Log.d("MainActivity", "Mic Permission is Grant ");
//            return true;
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//
//                switch (requestCode) {
//                    case REQUEST_CAMERA:
//                        Log.v("MainActivity","Camera Permission: "+permissions[0]+ "was "+grantResults[0]);
//                        PermissionComplete[REQUEST_CAMERA] = 1;
//                        boolean isMicPermission = grantMicPermission();
//                        if (isMicPermission == true) {
//                            PermissionComplete[REQUEST_MICROPHONE] = 1;
//                        }
//                        break;
//
//                    case REQUEST_MICROPHONE:
//                        Log.v("MainActivity","Mic Permission: "+permissions[0]+ "was "+grantResults[0]);
//                        PermissionComplete[REQUEST_MICROPHONE] = 1;
//                        break;
//                }
//            }
//
//            if (PermissionComplete[REQUEST_CAMERA] == 1 && PermissionComplete[REQUEST_MICROPHONE] == 1) {
//                //resume tasks needing this permission
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                intent.putExtra("auth_key", auth_key_);
//                intent.putExtra("id", id_);
//                startActivity(intent);
//            }
//        }
//    }
//    // ========================== 통신 처리 ============================== //
////    protected void TimerUpdate() {
////        Runnable updater = new Runnable() {
////            @Override
////            public void run() {
////                int position = mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition();
////                MainFragment mainFragment = (MainFragment)mFragments.get(position);
////                String title = mainFragment.getTitle();
////                if (title == null) {
////                    return;
////                }
////
////                if (select_tab_cmd(title) == false){
////                    return ;
////                }
////            }
////        };
////        handler_.post(updater);
////    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        int position = mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition();
//        MainFragment mainFragment = (MainFragment)mFragments.get(position);
//        String title = mainFragment.getTitle();
//        if (title == null) {
//            super.onResume();
//            return ;
//        }
//
//        if (select_tab_cmd(title) == false){
//            return ;
//        }
//
//        super.onResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        // 공통 사항
//        http_api_cmd("/userinfo", login_out_cmd_msg(id_, pswd_,"exit"));
//        super.onDestroy();
//    }
//
//    private boolean select_tab_cmd(String title){
//        if (title.compareTo(DEFINITION.TAB0) == 0) {
//            // HOME
//
//        }else if(title.compareTo(DEFINITION.TAB1) == 0) {
//            // LIVE
//            http_api_cmd("/stream", get_live_stream_list_cmd_msg(auth_key_));
//        }else if(title.compareTo(DEFINITION.TAB2) == 0) {
//            // VOD
//            http_api_cmd("/stream", get_vod_stream_list_cmd_msg(auth_key_,id_,vod_tag));//냥
//        }else if(title.compareTo(DEFINITION.TAB3) == 0) {
//            // PICK
//
//        }else {
//            Toast.makeText(MainActivity.this, "unknown tab is selected.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true;
//    }
//
//
//    private void http_api_cmd(String param, String body) {
//        String url = "http://" + DEFINITION.FRONT_IP+ ":" + DEFINITION.FRONT_PORT + param;
//        //String url = "http://222.122.30.58:6800/userinfo";
//        // AsyncTask를 통해 HttpURLConnection 수행.
//        NetworkTask net_task = new NetworkTask(url, body);
//        net_task.execute();
//    }
//
//    private class NetworkTask extends AsyncTask<Void, Void, String> {
//
//        private String url;
//        private String body;
//
//        public NetworkTask(String url, String body) {
//            this.url = url;
//            this.body = body;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String result; // 요청 결과를 저장할 변수.
//            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
//            result = requestHttpURLConnection.request(url, body); // 해당 URL로 부터 결과물을 얻어온다.
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            if (s == null)
//                return;
//            if (s.contains("ErrorCode(") == true) {
//                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            //tv_outPut.setText(s);
//            int position = mCoordinatorTabLayout.getTabLayout().getSelectedTabPosition();
//            MainFragment mainFragment = (MainFragment)mFragments.get(position);
//            String title = mainFragment.getTitle();
//            if (title.compareTo(DEFINITION.TAB0) == 0) {
//                // HOME
//
//            }else if(title.compareTo(DEFINITION.TAB1) == 0) {
//                // LIVE
//                if (s.length() > 0) {
//                    if (url.contains("/stream") == true) {
//                        if (cmd_status_.equals("get_live_list") == true) {
//                            // add stream ack
//                            get_live_stream_list_ack(s);
//                            int map_size = content_list_map_.size();
//                            if (map_size > 0) {
//                                cmd_status_ = "obtained_live_list";
//
//                                mainFragment.putContentList(content_list_map_,DEFINITION.TAB1);
////                                for( String key : content_list_map_.keySet() ){
////                                    //adapter.addItemUrl(content_list_map_.get(key).view_thumbnail_url, content_list_map_.get(key).index, content_list_map_.get(key).create_date);
////                                    // System.out.println( String.format("키 : %s, 값 : %s", key, content_list_map_.get(key)) );
////                                }
//                                //adapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                }
//            }else if(title.compareTo(DEFINITION.TAB2) == 0) {
//                // VOD
//                if (s.length() > 0) {
//                    if (url.contains("/stream") == true) {
//                        if (cmd_status_.equals("get_vod_list") == true) {
//                            // add stream ack
//                            get_vod_stream_list_ack(s);
//                            int map_size = content_list_map_.size();
//                            if (map_size > 0) {
//                                cmd_status_ = "obtained_vod_list";
//
//                                mainFragment.putContentList(content_list_map_,DEFINITION.TAB2);
////                                for( String key : content_list_map_.keySet() ){
////                                    //adapter.addItemUrl(content_list_map_.get(key).view_thumbnail_url, content_list_map_.get(key).index, content_list_map_.get(key).create_date);
////                                    // System.out.println( String.format("키 : %s, 값 : %s", key, content_list_map_.get(key)) );
////                                }
//                                //adapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                }
//            }else if(title.compareTo(DEFINITION.TAB3) == 0) {
//                // PICK
//
//            }else {
//                Toast.makeText(MainActivity.this, "unknown tab is selected.", Toast.LENGTH_SHORT).show();
//                return ;
//            }
//        }
//    }
//
//    private String login_out_cmd_msg(String userid, String pswd, String action) {
//        String json = "";
//        try {
//            JSONObject data = new JSONObject();
//            data.put("user",userid);
//            data.put("pswd", pswd);
//            data.put("action", action);
//
//            JSONObject root = new JSONObject();
//            root.put("userinfo", data);
//            json = root.toString();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }
//
//    private String get_live_stream_list_cmd_msg(String auth_key) {
//        cmd_status_ = "get_live_list";
//        content_list_map_.clear();
////        adapter.clearItem();
////        adapter.notifyDataSetChanged();
//
//        String json = "";
//        try {
//            JSONObject data = new JSONObject();
//            data.put("auth_key",auth_key);
//
//            JSONObject root = new JSONObject();
//            root.put("live_stream_list", data);
//            json = root.toString();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }
//
//    private String get_vod_stream_list_cmd_msg(String auth_key,String id,String tag) {
//        cmd_status_ = "get_vod_list";
//        content_list_map_.clear();
////        adapter.clearItem();
////        adapter.notifyDataSetChanged();
//
//        String json = "";
//        try {
//            JSONObject data = new JSONObject();
//            data.put("auth_key",auth_key);
//            data.put("user",id);
//            data.put("vod_tag",tag);
//
//            JSONObject root = new JSONObject();
//            root.put("vod_stream_list", data);
//            json = root.toString();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }
//
//    private void get_live_stream_list_ack(String ack_body) {
//        JSONObject json = null;
//        try {
//            json = new JSONObject(ack_body);
//            JSONArray jArray = json.getJSONArray("live_streams");
//            System.out.println("*****JARRAY*****"+jArray.length());
//            for(int i=0;i<jArray.length();i++){
//
//                ContentItem MapItem = new ContentItem();
//                MapItem.live_info = new LiveInfoItem();
//                MapItem.vod_info = new VODInfoItem();
//                MapItem.user_info = new UserInfoItem();
//
//                JSONObject json_data = jArray.getJSONObject(i);
//                // common information
//                MapItem.index = json_data.getString("index");
//
//                // live information
//                MapItem.live_info.index = MapItem.index;
//                MapItem.live_info.origin_stream_url = json_data.getString("origin_stream_url");
//                MapItem.live_info.create_date = json_data.getString("create_date");
//                MapItem.live_info.view_stream_url = json_data.getString("view_stream_url");
//                MapItem.live_info.view_rtmp_stream_url = json_data.getString("view_rtmp_stream_url");
//                MapItem.live_info.view_thumbnail_url = json_data.getString("view_thumbnail_url");
//
//                // vod information
//                MapItem.vod_info.index = MapItem.index;
//                MapItem.vod_info.vod_thumbnail_url = "http://175.123.138.135:8070/hot.mp4";
//                MapItem.vod_info.vod_url_low = "http://175.123.138.135:8070/hot.mp4";
//                MapItem.vod_info.vod_url_middle = "http://175.123.138.125:8070/oceanworld.mp4";
//                MapItem.vod_info.vod_url_high = "http://175.123.138.125:8070/oceanworld.mp4";
//                MapItem.vod_info.vod_url_general = "http://175.123.138.125:8070/oceanworld.mp4";
//
//                // user information
//                MapItem.user_info.id = "content provide user id"; // id
//                MapItem.user_info.nickname = "codecaffe"; // 별명
//                MapItem.user_info.hits = "0"; // 조회수
//                MapItem.user_info.goods = "0"; // 좋아요
//                MapItem.user_info.title = "안녕 하세요"; // 제목
//                MapItem.user_info.detail = "세부내용 입니다."; // 세부내용
//                MapItem.user_info.category = "Entertainment"; // 카테고리
//
////                Log.i(TAG,"_id"+json_data.getInt("account")+
////                        ", mall_name"+json_data.getString("name")+
////                        ", location"+json_data.getString("number")+
////                        ", telephone"+json_data.getString("url")+
////                        ",----"+json_data.getString("balance")+
////                        ",----"+json_data.getString("credit")+
////                        ",----"+json_data.getString("displayName")
////                );
//
//                content_list_map_.put(MapItem.index, MapItem);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void get_vod_stream_list_ack(String ack_body) {
//        JSONObject json = null;
//        try {
//            json = new JSONObject(ack_body);
//            JSONArray jArray = json.getJSONArray("vod_streams");
//            System.out.println("*****JARRAY*****"+jArray.length());
//            for(int i=0;i<jArray.length();i++){
//
//                ContentItem MapItem = new ContentItem();
//                MapItem.live_info = new LiveInfoItem();
//                MapItem.vod_info = new VODInfoItem();
//                MapItem.user_info = new UserInfoItem();
//
//                JSONObject json_data = jArray.getJSONObject(i);
//                // common information
//                MapItem.index = json_data.getString("index");
//
//                // vod information
//                MapItem.vod_info.index = MapItem.index;
//                MapItem.vod_info.vod_thumbnail_url = json_data.getString("origin_stream_url");
//                MapItem.vod_info.vod_url_low = json_data.getString("vod_url_low");
//                MapItem.vod_info.vod_url_middle = json_data.getString("vod_url_middle");
//                MapItem.vod_info.vod_url_high = json_data.getString("vod_url_high");
//                MapItem.vod_info.vod_url_general = json_data.getString("vod_url_general");
//
//                // user information
//                MapItem.user_info.id = "content provide user id"; // id
//                MapItem.user_info.nickname = "codecaffe"; // 별명
//                MapItem.user_info.hits = "0"; // 조회수
//                MapItem.user_info.goods = "0"; // 좋아요
//                MapItem.user_info.title = "안녕 하세요"; // 제목
//                MapItem.user_info.detail = "세부내용 입니다."; // 세부내용
//                MapItem.user_info.category = "Entertainment"; // 카테고리
//
////                Log.i(TAG,"_id"+json_data.getInt("account")+
////                        ", mall_name"+json_data.getString("name")+
////                        ", location"+json_data.getString("number")+
////                        ", telephone"+json_data.getString("url")+
////                        ",----"+json_data.getString("balance")+
////                        ",----"+json_data.getString("credit")+
////                        ",----"+json_data.getString("displayName")
////                );
//
//                content_list_map_.put(MapItem.index, MapItem);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//}