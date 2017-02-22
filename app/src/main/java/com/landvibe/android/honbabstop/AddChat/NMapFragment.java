package com.landvibe.android.honbabstop.AddChat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.landvibe.android.honbabstop.R;
import com.landvibe.android.honbabstop.base.domain.FoodRestaurant;
import com.landvibe.android.honbabstop.base.listener.OnShowMarkerListener;
import com.landvibe.android.honbabstop.nmaps.NMapPOIflagType;
import com.landvibe.android.honbabstop.nmaps.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

/**
 * Created by user on 2017-02-18.
 */

public class NMapFragment extends Fragment
        implements NMapView.OnMapStateChangeListener, NMapLocationManager.OnLocationChangeListener,
        OnShowMarkerListener{

    private final static String TAG = "NMapFragment";

    private final static String CLIENT_ID = "o8uc6MRnWiJu0aHOOYmf";

    private final static int MY_LOCATION_POI_ID=1001;
    private final static int SEARCH_POI_ID=1002;

    private Activity mActivity;

    private NMapContext mMapContext;

    private NMapView mMapView;

    private NMapController mMapController;

    private NMapViewerResourceProvider mMapViewerResourceProvider;

    private NMapOverlayManager mMapOverlayManager;

    private NMapLocationManager mMapLocationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_naver_map, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();

        mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView = (NMapView)getView().findViewById(R.id.mapView);
        mMapView.setClientId(CLIENT_ID);
        mMapContext.setupMapView(mMapView);

        // initialize map view
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();

        // MapView 리스너 등록
        mMapView.setOnMapStateChangeListener(this);

        mMapController = mMapView.getMapController();
        // Overlay 드로잉 관리
        mMapViewerResourceProvider = new NMapViewerResourceProvider(mActivity);
        // Overlay 관리
        mMapOverlayManager = new NMapOverlayManager(mActivity, mMapView, mMapViewerResourceProvider);
        // 내 위치 관리
        mMapLocationManager = new NMapLocationManager(mActivity);
        mMapLocationManager.setOnLocationChangeListener(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }

    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }



    /**
     * NMapView.OnMapStateChangeListener
     */
    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null){
            startMyLocation();
        }
        else{
            Log.e("NMAP", "onMapInitHandler: error=" + nMapError.toString());
        }

    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    private void startMyLocation(){
        if(!mMapLocationManager.isMyLocationEnabled()){
            boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
            if (!isMyLocationEnabled) {
                Toast.makeText(mActivity,"시스템 설정에서 위치 탐색 사용을 체크해주세요!", Toast.LENGTH_LONG).show();
                Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(goToSettings);
                return;
            }
        }
    }

    private void stopMyLocation() {
        mMapLocationManager.disableMyLocation();
    }


    /**
     * NMapLocationManager.OnLocationChangeListener
     */
    @Override
    public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint myLocation) {

        int markId = NMapPOIflagType.PIN;
        NMapPOIdata mMapPOIdata = new NMapPOIdata(1,mMapViewerResourceProvider);
        mMapPOIdata.beginPOIdata(1);
        mMapPOIdata.addPOIitem(myLocation,"내 위치",markId,MY_LOCATION_POI_ID/*id*/);
        mMapPOIdata.endPOIdata();
        NMapPOIdataOverlay poiDataOverlay = mMapOverlayManager.createPOIdataOverlay(mMapPOIdata, null);

        poiDataOverlay.showAllPOIdata(11 /* zoom level*/); //0

        return false; // 한번만 탐색
    }

    @Override
    public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                "위치를 탐색하고 있습니다 잠시만 기다려주세요", Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {}).show();
    }

    @Override
    public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                "잘못된 위치입니다. 위치탐색을 중단합니다.", Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {}).show();
        stopMyLocation();
    }



    /* OnShowMarkerListener */
    @Override
    public void onMarkPin(FoodRestaurant foodRestaurant) {
        Log.d(TAG, foodRestaurant.getTitle());

        mMapOverlayManager.clearOverlays();

        StringBuffer desc = new StringBuffer();
        desc.append(String.format("%s - %s",
                foodRestaurant.getTitle().replace("<b>","").replace("</b>",""),
                foodRestaurant.getAddress()));

        int markId = NMapPOIflagType.PIN;
        NMapPOIdata mMapPOIdata = new NMapPOIdata(1,mMapViewerResourceProvider);
        mMapPOIdata.beginPOIdata(1 /* POI 개수 */);
        mMapPOIdata.addPOIitem(
                new NGeoPoint(foodRestaurant.getLon(),foodRestaurant.getLat()),
                desc.toString(),markId,SEARCH_POI_ID/*id*/);
        mMapPOIdata.endPOIdata();
        NMapPOIdataOverlay poiDataOverlay = mMapOverlayManager.createPOIdataOverlay(mMapPOIdata, null);

        poiDataOverlay.showAllPOIdata(11 /* zoom level*/);

    }
}
