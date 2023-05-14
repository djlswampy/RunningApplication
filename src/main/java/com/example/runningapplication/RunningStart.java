package com.example.runningapplication;

import static android.text.format.DateUtils.formatElapsedTime;

import static com.example.runningapplication.Login.ID;
import static com.example.runningapplication.Login.user;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.runningapplication.ClassPakage.PreferenceManager;
import com.example.runningapplication.RecyclerView.DetailRecord_ItemData;
import com.example.runningapplication.RecyclerView.ItemData;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

public class RunningStart extends AppCompatActivity implements OnMapReadyCallback {
    //OnMapReadyCallback 인터페이스는 Google Maps API에서 제공하는 지도 객체(GoogleMap)가 사용 가능해질 때 호출되는 콜백 인터페이스

    private DrawerLayout drawerLayout;
    private View drawer_menu;

    private FusedLocationSource locationSource;
//    private Location location;
    private LatLng latLng;
    private LatLng prevLatlng;
    private PathOverlay path;
    private NaverMap naverMap;
//    private Marker marker;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private float totalDistance = 0;
    private float sectionDistance = 0;
    private boolean runningStart;
    private int num = 0; //초반에 거리 오차가 심한 것 방지.


    long MillisecondTime = 0L;  // 스탑워치 시작 버튼을 누르고 흐른 시간
    long StartTime = 0L;        // 스탑워치 시작 버튼 누르고 난 이후 부터의 시간
    long TimeBuff = 0L;         // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간
    long UpdateTime = 0L;       // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간 + 시작 버튼 누르고 난 이후 부터의 시간 = 총 시간


    Handler handler= new Handler();
    int Seconds, Minutes;
    TextView tv_time1, tv_speed, tv_distance1;
    private long sectionTime_start, MillisecondTime_section; //측정시작 시의 시간을 저장하기 위한 변수
    int Seconds_section, Minutes_section, sectionPace;

    private ArrayList<LatLng> wholePathLatngList; //전체 경로 좌표 리스트
    private ArrayList<PathOverlay> paintingList = new ArrayList<>(); //페인팅 객체 리스트
    private ArrayList<Marker> markerList = new ArrayList<>(); //마커객체 리스트. 마커 제거할 때 사용

    private ArrayList<LatLng> markerLatLngList = new ArrayList<>(); //마커 좌표 리스트

    //구간 기록
    private String sectionTime;
    private ArrayList<LatLng> sectionLatngList; //구간별 경로 좌표 리스트

    //test
    private ArrayList<ArrayList<LatLng>> allSectionLatngList = new ArrayList<>(); //구간별 경로 좌표 리스트를 담을 리스트?

    private ArrayList<String[]> sectionInfoList = new ArrayList<>(); //구간별 정보 입력을 위한 어레이. time_distance_pace을 담음
    private String[] time_distance_pace = new String[3]; //구간별 정보들을 담을 배열
    int count = 0;

    private ArrayList<InfoWindow> infoWindowList = new ArrayList<>(); //정보창 닫을 때 사용하는 리스트.
    private ArrayList<String> infoList = new ArrayList<>();
    private ArrayList<DetailRecord_ItemData> sectionRecordItemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runningstart);
        user = PreferenceManager.getObject(RunningStart.this, ID);

        tv_speed = findViewById(R.id.tv_speed);
        tv_time1 = findViewById(R.id.tv_time1);
        tv_distance1 = findViewById(R.id.tv_pace);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.myMap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.myMap, mapFragment).commit();
        }

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        mapFragment.getMapAsync(this);
        Log.d("MAP_TEST", "맵 생성");

//        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//            ActivityCompat.requestPermissions( RunningStart.this, new String[] {
//                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
//            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
//
//        } else {
//            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
//        }


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RunningStart.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }

        Button btn_start = findViewById(R.id.btn_start);
        Button btn_stop = findViewById(R.id.btn_stop);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//시작 & 정지버튼

                if(wholePathLatngList == null){//시작버튼을 누르면 리스트 생성
                    wholePathLatngList = new ArrayList<>();
                }

                if(btn_start.getText().toString().equals("시작")){ //시작버튼 눌렀을 경우 일시정지 버튼으로 바뀜
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    btn_start.setBackgroundColor(Color.RED);
                    btn_start.setText("일시정지");

                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    handler.post(runnable_sectionTime);

                    runningStart = true; //이동거리 측정 시작
                    sectionDistance = 0; //구간 이동거리 초기화

                    //기록측정시 이동한 경로 나타내기 위한 어레이 생성
                    sectionLatngList = new ArrayList<>();

                    sectionTime_start = SystemClock.uptimeMillis();

                } else if (btn_start.getText().toString().equals("일시정지")){ //일시정지 버튼 (누르면 다시 시작버튼으로)
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    runningStart = false; //이동거리 측정 정지

                    //구간 루트를 저장하기 위한 작업
                    allSectionLatngList.add(sectionLatngList);

                    btn_start.setBackgroundColor(Color.parseColor("#F00543B3"));
                    btn_start.setText("시작");

                    // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간
                    TimeBuff += MillisecondTime;

                    // Runnable 객체 제거
                    handler.removeCallbacks(runnable);
                    handler.removeCallbacks(runnable_sectionTime);

                    if(latLng != null && sectionDistance > 0){
                        String str = String.format("%.2f", sectionDistance) + "KM";
                        float sectionPace = (float)(MillisecondTime_section / 1000) / sectionDistance;

                        int sectionPace_Minutes = (int)sectionPace / 60;
                        int sectionPace_Seconds = (int)sectionPace % 60;

                        String sectionPace_time = sectionPace_Minutes + " : " +  String.format("%02d", sectionPace_Seconds) + " (분/km)";

                        time_distance_pace[0] = sectionTime;  //시간
                        time_distance_pace[1] = str; //거리
                        time_distance_pace[2] = sectionPace_time; //ㅍㅔ이스

                        sectionInfoList.add(time_distance_pace); //마커 정보창에 표시할 정보를 담은 배열을 어레이에 추가

                        String tag = "<구간기록> \n" + "이동시간 : " + sectionInfoList.get(count)[0]
                                + "\n이동거리 : " + sectionInfoList.get(count)[1]
                                + "\n평균페이스 : " + sectionInfoList.get(count)[2];

                        infoList.add(tag);

                        DetailRecord_ItemData recordDataModel = new DetailRecord_ItemData(time_distance_pace[0], time_distance_pace[1], time_distance_pace[2]);

                        sectionRecordItemList.add(recordDataModel);

                        Marker marker = new Marker(); //마커생성
                        marker.setPosition(latLng);
                        marker.setMap(naverMap);
                        marker.setTag(tag);
                        markerList.add(marker);
                        markerLatLngList.add(latLng); //마커 좌표 저장

                        markerInfoWindow(marker); //정보창 메서드
                        count++;
                        sectionDistance = 0;

                    }
                }
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date nowDate = new Date();

        btn_stop.setOnClickListener(new View.OnClickListener() {  //종료버튼(초기화)
            @Override
            public void onClick(View view) {
                //다이얼로그
                AlertDialog.Builder dialog = new AlertDialog.Builder(RunningStart.this);
                final EditText editText = new EditText(RunningStart.this);
                dialog.setTitle("기록을 저장하시겠습니까?");
                dialog.setMessage("장소");
                dialog.setView(editText);

                dialog.setPositiveButton("저장", new DialogInterface.OnClickListener() { //저장하기
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        runningStart = false; //기록 측정 중단

                        user.totalRunningDistance.add(totalDistance);
                        String distance = String.format("%.2f", totalDistance); //거리

                        String date = dateFormat.format(nowDate); //날짜
                        String inputText = editText.getText().toString(); //메모
                        String time = tv_time1.getText().toString();

                        //데이터 객체 담은 어레이리스트
                        ItemData dataModel = new ItemData(distance, time, date, inputText);

                        user.items.add(dataModel);
//                        if(user.items == null){
//
//                        }

                        user.wholePathLatngList_List.add(wholePathLatngList); //전체 경로 좌표 리스트 저장
                        user.sectionPathLatngList_List.add(allSectionLatngList); //구간 경로 데이터를 모은 리스트를 저장하는 작업
                        user.markerLatLngList_List.add(markerLatLngList); //마커들의 좌표를 모은 리스트 저장
                        user.infoList_List.add(infoList);
                        user.recordItemsList_List.add(sectionRecordItemList);

                        PreferenceManager.setObject(RunningStart.this, ID, user);
                        recordReset(); //측정 기록 초기화

                        Intent intent = new Intent(RunningStart.this, Record.class);
                        startActivity(intent);
                    }
                });

                dialog.setNegativeButton("저장 안 함", new DialogInterface.OnClickListener() { //저장안함
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        runningStart = false;
                        totalDistance = 0; //이동거리 초기화

                        recordReset();//측정기록 초기화

                    }
                });

                dialog.show();
            }
        });

        //------메뉴

        drawerLayout = findViewById(R.id.runningStart);
        drawer_menu = findViewById(R.id.drawer_menu);
        ImageButton btn_menu = findViewById(R.id.btn_menu); //메뉴 열기

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawer_menu);

            }
        });

//        btn_menu.setOnClickListener(view -> {         //람다식 표현?
//            drawerLayout.openDrawer(drawer_manu);
//        });

        Button btn_back = findViewById(R.id.btn_back); //메뉴 닫기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawer_menu);

            }
        });

        Button btn_record = findViewById(R.id.btn_record); //정보 액티비티 열기
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RunningStart.this, Record.class);
                startActivity(intent);

            }
        });

        Button btn_information = findViewById(R.id.btn_information); //정보 액티비티 열기
        btn_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RunningStart.this, Information.class);
                startActivity(intent);

            }
        });

        Button btn_calendar = findViewById(R.id.btn_calendar); //캘린더 액티비티 열기
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RunningStart.this, CalendarMemo.class);
                startActivity(intent);

            }
        });

        Button btn_statistics = findViewById(R.id.btn_statistics); //통계 액티비티 열기
        btn_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RunningStart.this, Statistics.class);
                startActivity(intent);

            }
        });

        Button btn_ask = findViewById(R.id.btn_ask);
        btn_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:djlswampy@gmail.com"); //mailto는 이메일 주소를 가리키는 url스키마
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);

//                Uri uri = Uri.parse("mailto:djlswampy@gmail.com"); // 다른 방법
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SENDTO);
//                intent.setData(uri);
//                startActivity(intent);

            }
        });

        Button btn_logout = findViewById(R.id.btn_logout); //로그아웃
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunningStart.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //----- 지도

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //지도가 준비가 되면 호출되는 매서드
        this.naverMap = naverMap;
        Log.d("MAP_TEST", "onMapReady 호출");
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setMinZoom(18.0);
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        // 콜백 메서드
        public void onLocationChanged(Location location) {
            if (num <= 2){
                num += 1;
            }
//            naverMap.setLocationSource(locationSource);
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            TextView tv_speed = findViewById(R.id.tv_speed);
            TextView tv_distance1 = findViewById(R.id.tv_pace);
            String nowSpeed = String.format("%.2f", location.getSpeed());

            if(num > 2 && wholePathLatngList != null && totalDistance != 0){
                paintingPath(location, wholePathLatngList, 1000, Color.GRAY); //경로선 메서드. 전체 이동 경로
//                prevLatlng = new LatLng(location);
            }

            if(runningStart == true) {
                if(totalDistance != 0 && num > 1) { //처음에 현재 위치로 이동할때 거리차이 반영 안 하도록
                    paintingPath(location, sectionLatngList, 2000, Color.BLUE);//경로선 메서드. 구간 이동 경로

                    location.distanceTo(locationSource.getLastLocation()); //이전 지점과의 거리차이
//                    sectionDistanceList.add(location.distanceTo(locationSource.getLastLocation())); //지점간 거리들을 어레이리스트에 담기
                    totalDistance += location.distanceTo(locationSource.getLastLocation()) * 0.001; //미터를 키로미터 단위로 변경. 전체 이동거리
                    sectionDistance += location.distanceTo(locationSource.getLastLocation()) * 0.001; //구간별 이동거리

                    String distance1 = String.format("%.2f", totalDistance);
                    tv_distance1.setText(distance1 + " KM");
                    tv_speed.setText(nowSpeed + "(m/s)");

                } else {
                    totalDistance = 0.00001f;

                }
            }

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // request code와 권한 획득 여부 확인
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    public void recordReset() {
        runningStart = false;
        totalDistance = 0; //이동거리 초기화

        // 측정 시간을 모두 0으로 리셋시켜준다.
        MillisecondTime = 0L ;
        StartTime = 0L ;
        TimeBuff = 0L ;
        UpdateTime = 0L ;
        Seconds = 0 ;
        Minutes = 0 ;

        //text 초기화
        tv_time1.setText("00:00");
        tv_speed.setText("0.00(m/s)");
        tv_distance1.setText("0.00 KM"); //거리 텍스트 초기화
        // Runnable 객체 제거
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable_sectionTime);


        for (int j = 0; j < paintingList.size(); j++) { //경로선 초기화
            paintingList.get(j).setMap(null);
        }
        paintingList.clear();

        for (int j = 0; j < markerList.size(); j++) { //마커초기화
            markerList.get(j).setMap(null);
        }
        markerList.clear();
        wholePathLatngList.clear();
        paintingList.clear();
        sectionInfoList.clear();
        count = 0;
        allSectionLatngList.clear();
        markerLatLngList.clear();
        infoList.clear();
        infoWindowList.clear();
        sectionRecordItemList.clear();
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            // 디바이스를 부팅한 후 부터 현재까지 시간 - 시작 버튼을 누른 시간
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간 + 시작 버튼 누르고 난 이후 부터의 시간 = 총 시간
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;

            // TextView에 UpdateTime을 갱신해준다
            tv_time1.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds));
            handler.postDelayed(this, 0);
        }
    };

    public Runnable runnable_sectionTime = new Runnable() { //구간별 걸린 시간
        public void run() {
            // 디바이스를 부팅한 후 부터 현재까지 시간 - 시작 버튼을 누른 시간
            MillisecondTime_section = SystemClock.uptimeMillis() - sectionTime_start;
            Seconds_section = (int)(MillisecondTime_section / 1000); //밀리세컨드를 초단위로
            Minutes_section = Seconds_section / 60;
            Seconds_section = Seconds_section % 60;

            sectionTime = Minutes_section + " : " + String.format("%02d", Seconds_section);
            handler.post(runnable_sectionTime); //ui작업 안 하므로 핸들러 필요 없을 듯
        }
    };


    public void paintingPath(Location location, ArrayList arrayList, int i, int i1){ //뒤에 정수들은 각각 중요도, 색깔 의미

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        arrayList.add(latLng);

        if(arrayList.size() >= 2){
            path = new PathOverlay(); //경로선 오버레이 객체 생성
            path.setCoords(arrayList); //선을 그리기 위한 좌표들을 설정하기 위해 사용. 각각의 LatLng 객체는 선의 좌표를 나타냄
            path.setGlobalZIndex(i); //인덱스 값이 높을수록 다른 오버레이보다 먼저 그려짐
            path.setWidth(30);
            path.setColor(i1);
            path.setMap(naverMap);//지도에 PathOverlay 객체를 추가

            paintingList.add(path); //경로선 초기화 할 때 사용
        }
    }

    public void markerInfoWindow(Marker marker) {
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setPosition(latLng);

        infoWindowList.add(infoWindow);
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(RunningStart.this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            for (int i = 0; i < infoWindowList.size(); i++) { //열려있는 모든 정보창 닫기
                infoWindowList.get(i).close();
            }
        });

// 마커를 클릭하면:
        Overlay.OnClickListener listener = overlay -> {
//            Marker marker = (Marker)overlay;

            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }

            return true;
        };
        marker.setOnClickListener(listener);
    }
}