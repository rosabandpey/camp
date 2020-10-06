package com.rosa.camp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
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
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rosa.camp.R;
import com.rosa.camp.ui.adapter.SearchViewAdapter;
import com.rosa.camp.ui.adapter.SearchViewAdapterForActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.enums.SelectOptions;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.model.inner.SearchItem;
import ir.map.servicesdk.request.SearchRequest;
import ir.map.servicesdk.response.ReverseGeoCodeResponse;
import ir.map.servicesdk.response.SearchResponse;

import static android.os.Looper.getMainLooper;

public class AddressActivity extends AppCompatActivity  implements View.OnClickListener {


    boolean mIsVisibleToUser;
    MapboxMap mapboxMap;
    public Style mapStyle;
    MapView mapView;
    private ProgressBar mProgressBar;
    private ProgressBar sProgressBar;
    private TextView mTextView;
    private LocationEngine locationEngine = null;
    private SearchView mSearchView;
    private SearchViewAdapterForActivity mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private AddressActivity.State state = AddressActivity.State.MAP;
    public Button searchButton;
    FloatingActionButton fb;
    private CircleManager circleManager;
    private LinearLayout mLinearLayout;
    private AddressActivity.MapFragmentLocationCallback callback = new MapFragmentLocationCallback(this);
    private PermissionsManager permissionsManager;
    private MapService mapService = new MapService();
    public final static LatLng VANAK_SQUARE=new LatLng(35.7572,51.4099);
    public final static LatLng BAKU=new LatLng(51.4099,35.7572);
    private static final String MARKERS_SOURCE = "markers-source";
    private static final String MARKERS_LAYER = "markers-layer";
    private static final String MARKER_ICON_ID = "marker-icon-id";
    private final int REQUEST_LOCATION_PERMISSION = 1;
    Button approveButton;
    Activity activity;
    Toolbar mtoolbar;
    LatLng address;
    LatLng mapTargetLat;
    private static final int LAUNCH_ADDRESS_ACTIVITY=1;
    String addressDescription;

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.approveButton){

            Intent i = new Intent(this, RegisterCampActivity.class);
            i.putExtra("latitude",address.getLatitude());
            i.putExtra("Longtitude",address.getLongitude());
            i.putExtra("addressDescription",addressDescription);
            setResult(LAUNCH_ADDRESS_ACTIVITY,i);
            finish();

        }
    }


    private enum State {
        MAP,
        MAP_PIN,
        SEARCHING,
        RESULTS
    }

    private void setState(AddressActivity.State state) {
        this.state = state;
        switch (state) {
            case MAP:
            case MAP_PIN:
                mLinearLayout.setVisibility(View.GONE);
                break;
            case SEARCHING:
                mLinearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                sProgressBar.setVisibility(View.VISIBLE);
                break;
            case RESULTS:
                mLinearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                sProgressBar.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        approveButton=findViewById(R.id.approveButton);
        approveButton.setOnClickListener(this);
        mTextView = findViewById(R.id.reverse_geocode_textView);
        mTextView.setText("");
        mProgressBar = findViewById(R.id.reverse_geocode_progressBar);
        sProgressBar = findViewById(R.id.search_progress_bar);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mtoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    address=point;

                    return true;

                });
                setupCurrentLocationButton();
            }

        });


        //Searching
        mRecyclerView = findViewById(R.id.recyclerView);
        mLinearLayout = findViewById(R.id.search_results_linear_layout);
        mProgressBar = findViewById(R.id.reverse_geocode_progressBar);
        activity= (Activity) this;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();
        mapView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && state == State.MAP_PIN) {
                setState(State.RESULTS);
                return true;
            }
            return false;
        });

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
        activity= (Activity) this;
        if (activity == null) {
            return;
        }

        locationEngine = LocationEngineProvider.getBestLocationEngine(activity);

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
        activity= (Activity) this;
        if (activity == null) {
            return;
        }

        // Check if permissions are enabled and if not request
        // if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(activity, loadedMapStyle)
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
        if (findViewById(R.id.showCurrentLocationButton) == null) {
            return;
        }
        fb = findViewById(R.id.showCurrentLocationButton);
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
                style.addImage(MARKER_ICON_ID, BitmapFactory.decodeResource(
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

    public void statusCheck() {
        final LocationManager manager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void reverseGeocode(LatLng latLng2) {

        if (TextUtils.isEmpty(mTextView.getText())) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.VISIBLE);


        mapService.reverseGeoCode(latLng2.getLatitude(),
                latLng2.getLongitude()

                ,new ResponseListener<ReverseGeoCodeResponse>() {

                    @Override
                    public void onSuccess(ReverseGeoCodeResponse result) {
                        mProgressBar.setVisibility(View.GONE);
                        mTextView.setVisibility(View.VISIBLE);

                        addressDescription = result.getAddressCompact();
                        if (address != null) {
                            mTextView.setText(addressDescription);
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_menu_item, menu);
        return  true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {

        if (menu.findItem(R.id.action_search) == null) {
            return false;
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

                    mapTargetLat=mapboxMap.getCameraPosition().target;


                    SearchRequest requestBody = new SearchRequest.Builder(newText)

                            .select(SelectOptions.POI)
                            .select(SelectOptions.REGION)
                            .select(SelectOptions.ROADS)
                            .select(SelectOptions.NEARBY).location(mapTargetLat.getLatitude(),mapTargetLat.getLongitude())
                            .select(SelectOptions.CITY)
                            .select(SelectOptions.COUNTY)
                            .select(SelectOptions.WOOD_WATER)
                            .build();

                    mapService.search(requestBody, new ResponseListener<SearchResponse>() {
                        @Override
                        public void onSuccess(SearchResponse response) {
                            Toast.makeText(getApplicationContext(), "پاسخ جستجو دریافت شد", Toast.LENGTH_SHORT).show();
                            setState(State.RESULTS);

                            if (response.getCount() > 0 && newText.equals(mSearchView.getQuery().toString())) {

                                mRecyclerAdapter = new SearchViewAdapterForActivity(response.getSearchItems());
                                mRecyclerView.setAdapter(mRecyclerAdapter);
                            }
                        }
                        @Override
                        public void onError(MapirError error) {
                            Toast.makeText(getApplicationContext(), "مشکلی در جستجو پیش آمده", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                return false;
            }
        });

        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showItemOnMap(final SearchItem item) {
        Log.d("onclick","view clicked");
        setState(State.MAP_PIN);
        activity= (Activity) this;
        if (activity == null || item.getGeom().getCoordinates() == null) {
            return;
        }

        circleManager.deleteAll();
        mSearchView.clearFocus();

        int color = ContextCompat.getColor(activity, R.color.colorPrimary);
        int strokeColor = ContextCompat.getColor(activity, R.color.colorAccent);
        double longitude = item.getGeom().getLongitude();
        double latitude = item.getGeom().getLatitude();

        LatLng latLng2=new LatLng(latitude,longitude);
        addMarkerToMapViewAtPosition(latLng2);
        address=latLng2;
        CircleOptions circleOptions = new CircleOptions()

                .withLatLng(latLng2)
                .withCircleColor(ColorUtils.colorToRgbaString(color))
                .withCircleStrokeWidth(4f)
                .withCircleStrokeColor(ColorUtils.colorToRgbaString(strokeColor))
                .withCircleBlur(0.5f)
                .withCircleRadius(12f);
        circleManager.create(circleOptions);

        circleManager.addClickListener(circle -> {
            if (!TextUtils.isEmpty(item.getAddress())) {
                Toast.makeText(getApplicationContext(), item.getAddress(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), item.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });

        if (item.getGeom() != null) {
            mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(latLng2), 1000);
        }
    }




    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getApplicationContext(), "برای عملکرد این ویژگی به موقعیت مکانی نیاز است", Toast.LENGTH_LONG).show();
    }


    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
                toggleCurrentLocationButton();
            }
        } else {
            Toast.makeText(getApplicationContext(), "برای عملکرد این ویژگی به موقعیت مکانی نیاز است", Toast.LENGTH_LONG).show();
        }
    }


    private static class MapFragmentLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private WeakReference<AddressActivity> fragmentWeakReference;

        MapFragmentLocationCallback(AddressActivity addressActivity) {
            fragmentWeakReference = new WeakReference(addressActivity);
        }

        /* The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            AddressActivity addressActivity = fragmentWeakReference.get();

            if (addressActivity != null) {
                Location location = result.getLastLocation();
                Log.d("location","location is recieved");

                if (location == null) {
                    Log.d("location","location is null");
                    return;

                }

                if (addressActivity.mapboxMap != null && result.getLastLocation() != null) {
                    addressActivity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
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






}