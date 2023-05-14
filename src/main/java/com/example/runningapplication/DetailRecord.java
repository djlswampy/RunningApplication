package com.example.runningapplication;

import static com.example.runningapplication.Login.ID;
import static com.example.runningapplication.Login.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.example.runningapplication.ClassPakage.PreferenceManager;
import com.example.runningapplication.RecyclerView.DetailRecordRecyclerAdapter;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

public class DetailRecord extends AppCompatActivity implements OnMapReadyCallback {

    RecyclerView recyclerView;
    Context context;
    Handler handler = new Handler();
    int position;
    LatLng makerlatlng;
    ArrayList<InfoWindow> windowList = new ArrayList<>();
//    ArrayList<Marker> makerList = new ArrayList<>();

    private NaverMap naverMap;
    private PathOverlay path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);

        recyclerView = findViewById(R.id.detailRecordRecycler);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.recordMap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.recordMap, mapFragment).commit();
        }


        mapFragment.getMapAsync(this);

        DetailRecordRecyclerAdapter adapter = new DetailRecordRecyclerAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //방향설정

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0); //아이템 포지션 받아오는 인텐트

        user = PreferenceManager.getObject(DetailRecord.this, ID); //이거 안 해주면 왜 오류????????

        handler.postDelayed(runnable, 0);
    }

    public Runnable runnable = new Runnable() { //지도위에 경로 오버레이 그리기
        public void run() {

            if(user.wholePathLatngList_List.get(position).size() >= 2){
                path = new PathOverlay(); //경로선 오버레이 객체 생성
                path.setCoords(user.wholePathLatngList_List.get(position)); //선을 그리기 위한 좌표들을 설정하기 위해 사용. 각각의 LatLng 객체는 선의 좌표를 나타냄
                path.setGlobalZIndex(1000); //인덱스 값이 높을수록 다른 오버레이보다 먼저 그려짐
                path.setWidth(30);
                path.setColor(Color.GRAY);
                path.setMap(naverMap);//지도에 PathOverlay 객체를 추가
            }


            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder(); //모든 오버레이 한 화면에 표현
            for (int i = 0; i < user.wholePathLatngList_List.get(position).size(); i++) {
                boundsBuilder.include(user.wholePathLatngList_List.get(position).get(i));
            }

            LatLngBounds bounds = boundsBuilder.build(); //바운드로 좌표 묶기. 이 좌표들을 한 화면에 표현하기 위해서 사용

            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(bounds, 200);
            naverMap.moveCamera(cameraUpdate);

            for (int i = 0; i < user.sectionPathLatngList_List.get(position).size(); i++) {
                if(user.sectionPathLatngList_List.get(position).get(i).size() >= 2){
                    path = new PathOverlay(); //경로선 오버레이 객체 생성
                    path.setCoords(user.sectionPathLatngList_List.get(position).get(i)); //선을 그리기 위한 좌표들을 설정하기 위해 사용. 각각의 LatLng 객체는 선의 좌표를 나타냄
                    path.setGlobalZIndex(2000); //인덱스 값이 높을수록 다른 오버레이보다 먼저 그려짐
                    path.setWidth(30);
                    path.setColor(Color.BLUE);
                    path.setMap(naverMap);//지도에 PathOverlay 객체를 추가
                }
            }

            for (int i = 0; i < user.markerLatLngList_List.get(position).size(); i++) { //마커 생성
                makerlatlng = user.markerLatLngList_List.get(position).get(i);

                Marker marker = new Marker(); //마커생성
                marker.setPosition(makerlatlng);
                marker.setWidth(80);
                marker.setHeight(110);
                marker.setMap(naverMap);
                marker.setTag(user.infoList_List.get(position).get(i));
                markerInfoWindow(marker); //각 마커에 해당하는 정보창 추가
//                makerList.add(marker);
            }
        }
    };


    public void markerInfoWindow(Marker marker) { //정보창 추가 메서드
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setPosition(makerlatlng);

        windowList.add(infoWindow);
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(DetailRecord.this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            for (int i = 0; i < windowList.size(); i++) { //열려있는 모든 정보창 닫기
                windowList.get(i).close();
            }
        });

// 마커를 클릭하면:
        Overlay.OnClickListener listener = overlay -> {
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