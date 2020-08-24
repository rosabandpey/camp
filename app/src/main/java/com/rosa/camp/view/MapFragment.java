package com.rosa.camp.view;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import ir.map.sdk_map.maps.MapView;
import ir.map.sdk_map.MapirStyle;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.enums.RouteType;
import ir.map.servicesdk.enums.SelectOptions;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.request.RouteRequest;
import ir.map.servicesdk.request.SearchRequest;
import ir.map.servicesdk.response.AutoCompleteSearchResponse;
import ir.map.servicesdk.response.RouteResponse;

//import com.google.android.gms.maps.MapView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import com.rosa.camp.R;
import com.mapbox.android.core.location.LocationEngineCallback;
import static android.os.Looper.getMainLooper;
import static android.service.controls.ControlsProviderService.TAG;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment   {


    MapboxMap map;
    Style mapStyle;
    MapView mapView;
    SearchView searchView;
    private LocationEngine locationEngine = null;
    private MapFragmentLocationCallback callback = new MapFragmentLocationCallback(this);
    private PermissionsManager permissionsManager;
    private MapService mapService = new MapService();
    public final static LatLng VANAK_SQUARE=new LatLng(35.7572,51.4099);
    private static final String MARKERS_SOURCE = "markers-source";
    private static final String MARKERS_LAYER = "markers-layer";
    private static final String MARKER_ICON_ID = "marker-icon-id";
    private final int REQUEST_LOCATION_PERMISSION = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    private void animateToCoordinate(LatLng coordinate) {
        CameraPosition position = new CameraPosition.Builder()
                .target(coordinate)
                .zoom(16)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @SuppressWarnings({"MissingPermission"})
    private void initializeLocationEngine() {
        if (getActivity() == null) {
            return;
        }

        locationEngine = LocationEngineProvider.getBestLocationEngine(getActivity());

        long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
        long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
      //  locationEngine.requestLocationUpdates(request, callback,android.os.Looper.getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @SuppressLint("MissingPermission")
    private void toggleCurrentLocationButton() {
        if (!map.getLocationComponent().isLocationComponentActivated() || !map.getLocationComponent().isLocationComponentEnabled()) {
            return;
        }
        Location location = map.getLocationComponent().getLastKnownLocation();
        if (location != null) {
            animateToCoordinate(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        switch (map.getLocationComponent().getRenderMode()) {
            case RenderMode.NORMAL:
                map.getLocationComponent().setRenderMode(RenderMode.COMPASS);
                break;
            case RenderMode.GPS:
            case RenderMode.COMPASS:
                map.getLocationComponent().setRenderMode(RenderMode.NORMAL);
                break;
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (getActivity() == null) {
            return;
        }
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            LocationComponent locationComponent = map.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                            .useDefaultLocationEngine(true)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);

            initializeLocationEngine();
        } else {
            permissionsManager = new PermissionsManager((PermissionsListener) this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @SuppressLint("MissingPermission")
    private void setupCurrentLocationButton() {
        if (getView() == null) {
            return;
        }
        FloatingActionButton fb = getView().findViewById(R.id.showCurrentLocationButton);
        fb.setOnClickListener(v -> {
            if (map.getStyle() != null) {
                enableLocationComponent(map.getStyle());
            }
            toggleCurrentLocationButton();
        });
    }


    private void addMarkerToMapViewAtPosition(LatLng coordinate) {
        if (map != null && map.getStyle() != null) {
            Style style = map.getStyle();

            if (style.getImage(MARKER_ICON_ID) == null) {
              //  Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.cedarmaps_marker_icon_default, null);
              //  Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                style.addImage(MARKER_ICON_ID,BitmapFactory.decodeResource(
                                getResources(), R.drawable.cedarmaps_marker_icon_default));
            }

            GeoJsonSource geoJsonSource;
            if (style.getSource(MARKERS_SOURCE) == null) {
                geoJsonSource = new GeoJsonSource(MARKERS_SOURCE);
                style.addSource(geoJsonSource);
            } else {
                geoJsonSource = (GeoJsonSource) style.getSource(MARKERS_SOURCE);
            }
            if (geoJsonSource == null) {
                return;
            }

            Feature feature = Feature.fromGeometry(
                    Point.fromLngLat(coordinate.getLongitude(), coordinate.getLatitude()));
            geoJsonSource.setGeoJson(feature);

            style.removeLayer(MARKERS_LAYER);

            SymbolLayer symbolLayer = new SymbolLayer(MARKERS_LAYER, MARKERS_SOURCE);
            symbolLayer.withProperties(
                    PropertyFactory.iconImage(MARKER_ICON_ID),
                    PropertyFactory.iconAllowOverlap(true)
            );
            style.addLayer(symbolLayer);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);


        //Installing Map
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;
                        // TODO;
                        addMarkerToMapViewAtPosition(VANAK_SQUARE);
                        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
                            enableLocationComponent(style);
                        }
                    }


                });
                map.setMaxZoomPreference(18);
                map.setMinZoomPreference(6);
                map.setCameraPosition(
                        new CameraPosition.Builder()
                                .target(VANAK_SQUARE)
                                .zoom(15)
                                .build());

                //Set a touch event listener on the map
                map.addOnMapClickListener(point -> {
                    addMarkerToMapViewAtPosition(point);
                    return true;

                });
                setupCurrentLocationButton();
            }

        });


        //Searching
        searchView=view.findViewById(R.id.search);
        CharSequence search;
        search= searchView.getQuery();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchRequest requestBody = new SearchRequest.Builder("خیابان انقلاب")
                        .select(SelectOptions.POI)
                        .select(SelectOptions.REGION)
                        .select(SelectOptions.ROADS)
                        .select(SelectOptions.PROVINCE)
                        .build();
                mapService.autoCompleteSearch(requestBody, new ResponseListener<AutoCompleteSearchResponse>() {
                    @Override
                    public void onSuccess(AutoCompleteSearchResponse response) {
                        Toast.makeText(getActivity(), "پاسخ جستجو دریافت شد", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(MapirError error) {
                        Toast.makeText(getActivity(), "مشکلی در جستجو پیش آمده", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //Routing
        RouteRequest requestBody = new RouteRequest.Builder(
                35.740312,51.422625 ,35.722580,51.51678,

                RouteType.DRIVING
        ).build();
        mapService.route(requestBody, new ResponseListener<RouteResponse>() {
            @Override
            public void onSuccess(RouteResponse response) {
                //  Toast.makeText(MainActivity.this, "پاسخ مسیریابی دریافت شد", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"پاسخ مسیریابی دریافت شد!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(MapirError error) {
                //  Toast.makeText(MainActivity.this, "مشکلی در مسیریابی پیش آمده", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"مشکلی در مسیریابی پیش آمده",Toast.LENGTH_SHORT).show();
            }
        });


    }



    private static class MapFragmentLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private WeakReference<MapFragment> fragmentWeakReference;

        MapFragmentLocationCallback(MapFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        /* The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapFragment fragment = fragmentWeakReference.get();

            if (fragment != null) {
                Location location = result.getLastLocation();
                Log.d("location","location is recieved");

                if (location == null) {
                    Log.d("location","location is null");
                    return;

                }

                if (fragment.map != null && result.getLastLocation() != null) {
                    fragment.map.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            String message = exception.getLocalizedMessage();
            if (message != null) {
                Log.d("LocationChange", message);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mapView = null;
    }


}