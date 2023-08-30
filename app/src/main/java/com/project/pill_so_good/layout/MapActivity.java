package com.project.pill_so_good.layout;

import android.location.Location;
import android.os.Bundle;
import android.Manifest;
import android.content.res.AssetManager;
import android.content.pm.PackageManager;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.pill_so_good.R;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapPOIItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean userHasMovedMap = false;  // 이 변수를 클래스 멤버로 추가
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.setZoomLevel(7, true);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation(mapView);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                int tag = mapPOIItem.getTag();
                if (tag == 0) {
                    mapPOIItem.setItemName("현재 내 위치");
                } else {
                    String tagString = String.valueOf(tag);
                    mapPOIItem.setItemName(tagString);
                }
            }
            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
                // 여기에 발풍식 풍선을 눌렀을 때의 동작을 추가하거나 비워 둘 수 있습니다.
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
                // 여기에 발풍식 풍선 버튼을 눌렀을 때의 동작을 추가하거나 비워 둘 수 있습니다.
            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
                // 여기에 드래그 가능한 POI 아이템이 움직였을 때의 동작을 추가하거나 비워 둘 수 있습니다.
            }
        });
        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open("Daegu.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                Log.d("DEBUG", "Reading CSV");
                double latitude = Double.parseDouble(tokens[3]);  // Latitude
                double longitude = Double.parseDouble(tokens[4]);  // Longitude
                Log.d("DEBUG", "addMarker called: Lat = " + latitude + ", Lon = " + longitude);
                addMarker(mapView, latitude, longitude, tokens[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DEBUG", "Error reading CSV: " + e.getMessage());
        }
        mapView.setMapViewEventListener(new MapView.MapViewEventListener() {
            @Override
            public void onMapViewInitialized(MapView mapView) {}

            @Override
            public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
                userHasMovedMap = true;  // 사용자가 지도를 움직였을 때 이 변수를 true로 설정
            }

            @Override
            public void onMapViewZoomLevelChanged(MapView mapView, int i) {}

            @Override
            public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {}

            @Override
            public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {}

            @Override
            public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {}

            @Override
            public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {}

            @Override
            public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {}

            @Override
            public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // 추가된 부분
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MapView mapView = (MapView) findViewById(R.id.map_view);
                getDeviceLocation(mapView);
            }
        }
    }

    private void getDeviceLocation(final MapView mapView) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        Log.d("DEBUG", "getDeviceLocation called");
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || userHasMovedMap) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        // 현재 위치를 지도의 중심으로 설정
                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), true);
                        // 현재 위치에 마커 추가
                        addMarker(mapView, location.getLatitude(), location.getLongitude(), "0");
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    private void addMarker(MapView mapView, double latitude, double longitude, String tag) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(tag.equals("0") ? "현재 내 위치" : tag); // 조건에 따라 이름을 다르게 설정
        if (tag.equals("0"))
        {
            Log.d("DEBUG", "Setting current location marker");
            marker.setCustomImageResourceId(R.drawable.psg);
        }
        else
        {
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        }
        Log.d("DEBUG", "Setting marker tag: " + (tag.equals("0") ? 0 : tag.hashCode()));
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }
}
