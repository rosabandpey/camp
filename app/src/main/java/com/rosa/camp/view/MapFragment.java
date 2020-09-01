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

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import ir.map.sdk_map.maps.MapView;
import ir.map.sdk_map.MapirStyle;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.enums.RouteType;
import ir.map.servicesdk.enums.SelectOptions;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.model.inner.SearchItem;
import ir.map.servicesdk.request.RouteRequest;
import ir.map.servicesdk.request.SearchRequest;
import ir.map.servicesdk.response.AutoCompleteSearchResponse;
import ir.map.servicesdk.response.ReverseGeoCodeResponse;
import ir.map.servicesdk.response.RouteResponse;
import ir.map.servicesdk.response.SearchResponse;

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
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rosa.camp.R;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.rosa.camp.ui.adapter.SearchViewAdapter;

import static android.os.Looper.getMainLooper;
import static android.service.controls.ControlsProviderService.TAG;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment  implements PermissionsListener {


    MapboxMap mapboxMap;
    public Style mapStyle;
    MapView mapView;
    SearchView searchView;
    EditText searchText;
    Button searchButton;
    private ProgressBar mProgressBar;
    private AppCompatTextView mTextView;
    private LocationEngine locationEngine = null;
    LatLng latLng;
    private SearchView mSearchView;
    private SearchViewAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private State state = State.MAP;
    private CircleManager circleManager;
    private LinearLayout mLinearLayout;
    private MapFragmentLocationCallback callback = new MapFragmentLocationCallback(this);
    private PermissionsManager permissionsManager;
    private MapService mapService = new MapService();
    public final static LatLng VANAK_SQUARE=new LatLng(35.7572,51.4099);
    public final static LatLng BAKU=new LatLng(51.4099,35.7572);
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
    private enum State {
        MAP,
        MAP_PIN,
        SEARCHING,
        RESULTS
    }
    private void setState(State state) {
        this.state = state;
        switch (state) {
            case MAP:
            case MAP_PIN:
                mLinearLayout.setVisibility(View.GONE);
                break;
            case SEARCHING:
                mLinearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case RESULTS:
                mLinearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                break;
        }
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
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
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
        if (!mapboxMap.getLocationComponent().isLocationComponentActivated() || !mapboxMap.getLocationComponent().isLocationComponentEnabled()) {
            return;
        }
        Location location = mapboxMap.getLocationComponent().getLastKnownLocation();
        if (location != null) {
            animateToCoordinate(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        switch (mapboxMap.getLocationComponent().getRenderMode()) {
            case RenderMode.NORMAL:
                mapboxMap.getLocationComponent().setRenderMode(RenderMode.COMPASS);
                break;
            case RenderMode.GPS:
            case RenderMode.COMPASS:
                mapboxMap.getLocationComponent().setRenderMode(RenderMode.NORMAL);
                break;
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (getActivity() == null) {
            return;
        }

        // Check if permissions are enabled and if not request
       // if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                            .useDefaultLocationEngine(true)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);

            initializeLocationEngine();
     //   } else {
       //     permissionsManager = new PermissionsManager((PermissionsListener) this);
         //   permissionsManager.requestLocationPermissions(getActivity());
      //  }
    }

    @SuppressLint("MissingPermission")
    private void setupCurrentLocationButton() {
        if (getView() == null) {
            return;
        }
        FloatingActionButton fb = getView().findViewById(R.id.showCurrentLocationButton);
        fb.setOnClickListener(v -> {
          if (mapboxMap.getStyle() != null) {
               enableLocationComponent(mapboxMap.getStyle());
            }

            toggleCurrentLocationButton();
        });
    }


    private void addMarkerToMapViewAtPosition(LatLng coordinate) {
        if (mapboxMap != null && mapboxMap.getStyle() != null) {
            Style style = mapboxMap.getStyle();

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

        public void statusCheck() {
        final LocationManager manager = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void reverseGeocode(LatLng latLng1) {

        if (TextUtils.isEmpty(mTextView.getText())) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.VISIBLE);


        mapService.reverseGeoCode(latLng1.getLatitude(),
                latLng1.getLongitude()

                ,new ResponseListener<ReverseGeoCodeResponse>() {

                    @Override
                    public void onSuccess(ReverseGeoCodeResponse result) {
                        mProgressBar.setVisibility(View.GONE);
                        mTextView.setVisibility(View.VISIBLE);

                        String address = result.getAddressCompact();
                        if (address != null) {
                            mTextView.setText(address);
                        } else {
                            mTextView.setText("آدرسی یافت نشد");
                        }

                    }

                    @Override
                    public void onError(MapirError error) {
                        mProgressBar.setVisibility(View.GONE);
                        mTextView.setVisibility(View.VISIBLE);

                        mTextView.setText("خطا در پردازش");
                    }

                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = view.findViewById(R.id.reverse_geocode_textView);
        mProgressBar = view.findViewById(R.id.reverse_geocode_progressBar);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mLinearLayout = view.findViewById(R.id.search_results_linear_layout);
        mProgressBar = view.findViewById(R.id.search_progress_bar);
        statusCheck();

        //Installing Map


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMapinternal) {
                mapboxMap = mapboxMapinternal;
                Style.Builder style1;
                mapboxMap.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {

                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;
                        circleManager = new CircleManager(mapView, mapboxMap, style);
                        // TODO;
                        //Add marker to map
                        addMarkerToMapViewAtPosition(VANAK_SQUARE);
                        enableLocationComponent(style);
                    }

                });
             //   if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

           //     }
                mapboxMap.setMaxZoomPreference(18);
                mapboxMap.setMinZoomPreference(6);
                mapboxMap.setCameraPosition(
                        new CameraPosition.Builder()
                                .target(VANAK_SQUARE)
                                .zoom(15)
                                .build());

                //Set a location picker on listener of mapbox
                LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;
                reverseGeocode(mapTargetLatLng);
                mapboxMap.addOnCameraIdleListener(() -> reverseGeocode(mapboxMap.getCameraPosition().target));



                //Set a touch event listener on the map
                mapboxMap.addOnMapClickListener(point -> {
                    addMarkerToMapViewAtPosition(point);
                    return true;

                });
                setupCurrentLocationButton();
            }

        });


        //Searching



        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && state == State.MAP_PIN) {
                setState(State.RESULTS);
                return true;
            }
            return false;
        });


       //searchView=view.findViewById(R.id.search);
      /*  searchText=view.findViewById(R.id.searchtext);
        searchButton=view.findViewById(R.id.searchbutton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search;
                search= searchText.getText().toString();
                List <SearchResponse> mItem;
                SearchRequest requestBody = new SearchRequest.Builder(search).build();
                mapService.search(requestBody, new ResponseListener<SearchResponse>() {
                    @Override
                    public void onSuccess(SearchResponse response) {
                        Toast.makeText(getActivity(), "پاسخ جستجو دریافت شد", Toast.LENGTH_SHORT).show();
                        addMarkerToMapViewAtPosition(BAKU);
                        mapboxMap.setCameraPosition(
                                new CameraPosition.Builder()
                                        .target(BAKU)
                                        .zoom(15)
                                        .build());

                    }
                    @Override
                    public void onError(MapirError error) {
                        Toast.makeText(getActivity(), "مشکلی در جستجو پیش آمده", Toast.LENGTH_SHORT).show();
                        //addMarkerToMapViewAtPosition(BAKU);
                    }
                });
            }
        });  */

      /*  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                SearchRequest requestBody = new SearchRequest.Builder(newText).build();
                mapService.search(requestBody, new ResponseListener<SearchResponse>() {
                    @Override
                    public void onSuccess(SearchResponse response) {
                        Toast.makeText(getActivity(), "پاسخ جستجو دریافت شد", Toast.LENGTH_SHORT).show();
                        addMarkerToMapViewAtPosition(BAKU);
                    }
                    @Override
                    public void onError(MapirError error) {
                        Toast.makeText(getActivity(), "مشکلی در جستجو پیش آمده", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });  */

/*
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
*/

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_view_menu_item, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        if (menu.findItem(R.id.action_search) == null) {
            return;
        }
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("جستجو در معابر");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView = searchView;

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && state == State.MAP_PIN) {
                circleManager.deleteAll();
                if (!TextUtils.isEmpty(searchView.getQuery())) {
                    setState(State.RESULTS);
                } else {
                    setState(State.MAP);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                if (TextUtils.isEmpty(newText)) {
                    circleManager.deleteAll();
                    setState(State.MAP);
                } else {
                    setState(State.SEARCHING);

                    SearchRequest requestBody = new SearchRequest.Builder(newText).build();
                    mapService.search(requestBody, new ResponseListener<SearchResponse>() {
                        @Override
                        public void onSuccess(SearchResponse response) {
                            Toast.makeText(getActivity(), "پاسخ جستجو دریافت شد", Toast.LENGTH_SHORT).show();
                            setState(State.RESULTS);
                            if (response.getCount() > 0 && newText.equals(mSearchView.getQuery().toString())) {

                                    //List<SearchItem> searchlist=response.getSearchItems();
                                    //searchlist.add(response.getSearchItems().get(i).getAddress());

                                    mRecyclerAdapter = new SearchViewAdapter(response.getSearchItems());

                                    mRecyclerView.setAdapter(mRecyclerAdapter);
                            }
                        }
                        @Override
                        public void onError(MapirError error) {
                            Toast.makeText(getActivity(), "مشکلی در جستجو پیش آمده", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showItemOnMap() {
        setState(State.MAP_PIN);
        addMarkerToMapViewAtPosition(BAKU);
    }



    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "برای عملکرد این ویژگی به موقعیت مکانی نیاز است", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
                toggleCurrentLocationButton();
            }
        } else {
            Toast.makeText(getActivity(), "برای عملکرد این ویژگی به موقعیت مکانی نیاز است", Toast.LENGTH_LONG).show();
        }
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

                if (fragment.mapboxMap != null && result.getLastLocation() != null) {
                    fragment.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
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