package com.rosa.camp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.mapbox.geojson.FeatureCollection;
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
import com.rosa.camp.ui.adapter.Addresslatlng;
import com.rosa.camp.ui.adapter.PrefernceHelperCamp;
import com.rosa.camp.ui.adapter.SearchViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment   implements PermissionsListener, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button newCampButton;
    private static final int LAUNCH_REGISTERCAMP_ACTIVITY=123;
    MapboxMap mapboxMap;
    public Style mapStyle;
    MapView mapView;
    private ProgressBar mProgressBar;
    private ProgressBar sProgressBar;
    private AppCompatTextView mTextView;
    private LocationEngine locationEngine = null;
    private SearchView mSearchView;
    private SearchViewAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private HomeFragment.State state = HomeFragment.State.MAP;

    FloatingActionButton fb;
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

    PrefernceHelperCamp prefernceHelperCamp;
    public int locationLatitudeCount;
    public int locationLatLongCount;
    LatLng latLngl;
    Context context;
    Button adressButton;
    ArrayList<Double> longtitude;
    ArrayList<Double> latitude;
    GeoJsonSource geoJsonSource;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newCampButton=view.findViewById(R.id.newCampButton);
        newCampButton.setOnClickListener(this::onClick);

        mTextView = view.findViewById(R.id.reverse_geocode_textView);
        mTextView.setText("");
        mProgressBar = view.findViewById(R.id.reverse_geocode_progressBar);
        sProgressBar = view.findViewById(R.id.search_progress_bar);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        adressButton = view.findViewById(R.id.addressButton1);
        adressButton.setOnClickListener(this);

        statusCheck();
        prefernceHelperCamp = PrefernceHelperCamp.instanceCamp(getContext());



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

                        // addMarkerToMapViewAtPosition(symbolLayerIconFeatureList);


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
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mLinearLayout = view.findViewById(R.id.search_results_linear_layout);
        mProgressBar = view.findViewById(R.id.reverse_geocode_progressBar);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && state == HomeFragment.State.MAP_PIN) {
                setState(HomeFragment.State.RESULTS);
                return true;
            }
            return false;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_REGISTERCAMP_ACTIVITY) {

            // The user picked a image.
            // The Intent's data Uri identifies which item was selected.
            if (data != null) {

                Bundle extras = data.getBundleExtra("BUNDLE");

                if (extras == null) {
                    return;
                }
               // ArrayList list=extras.getParcelableArrayList("Longtitude");

                longtitude = (ArrayList<Double>) extras.getSerializable("Longtitude");
                latitude = (ArrayList<Double>) extras.getSerializable("latitude");
                Log.d("arrayfound",longtitude.get(0).toString());
                Log.d("arrayfoundsize",String.valueOf(longtitude.size()));
            }
            //     }
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.newCampButton:

                Intent intent = new Intent(view.getContext(), RegisterCampActivity.class);
                startActivityForResult(intent, LAUNCH_REGISTERCAMP_ACTIVITY);
             break;



            case R.id.addressButton1:

                String file=convertToJoGeojson();

                Log.d("file output",file);
                String  fileName="/data/data/" + getApplicationContext().getPackageName() + "/" +"staff.geojson";
                try {
                    File myFile = new File("/data/data/" + getApplicationContext().getPackageName()+ "/" +"staff.geojson");

                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
                    myOutWriter.append(file);


                    myOutWriter.close();
                    fOut.close();
                    Log.d("staff",getApplicationContext().getPackageName().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("file","file not found");
                }

                addSymbolSourceAndLayerToMap(fileName);
                break;
        }

    }

    public String convertToJoGeojson(){

        JSONObject featureCollection = new JSONObject();
        try {
            // addresslatlng=Addresslatlng.getAddressInstance();
            featureCollection.put("type", "FeatureCollection");
            JSONArray featureList = new JSONArray();
            // iterate through your list
            JSONObject properties=new JSONObject();
            locationLatitudeCount=longtitude.size();
            Log.d("count",String.valueOf(locationLatitudeCount));
            for (int i=0;i<locationLatitudeCount;i++) {
                // {"geometry": {"type": "Point", "coordinates": [-94.149, 36.33]}
                JSONObject point = new JSONObject();
                point.put("type", "Point");
                // construct a JSONArray from a string; can also use an array or list
                // Log.d("count"+String.valueOf(i),String.valueOf(addresslatlng.getLongtitude1().get(i)));
                JSONArray coord = new JSONArray("[" + longtitude.get(i) + "," + latitude.get(i) + "]");
                point.put("coordinates", coord);
                JSONObject feature = new JSONObject();
                feature.put("type", "Feature");
                feature.put("properties", properties);
                feature.put("geometry", point);
                featureList.put(feature);
            }
            featureCollection.put("features", featureList);

        } catch (JSONException e) {
            Log.d("jsonobj",e.toString());
        }
        // output the result
        System.out.println("featureCollection="+featureCollection.toString());
        return featureCollection.toString();

    }

    private enum State {
        MAP,
        MAP_PIN,
        SEARCHING,
        RESULTS
    }
    private void setState(HomeFragment.State state) {
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
        fb = getView().findViewById(R.id.showCurrentLocationButton);
        fb.setOnClickListener(v -> {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }

            toggleCurrentLocationButton();
        });
    }

    private void addSymbolSourceAndLayerToMap(String jsonName) {
        // Add source to map

        if (mapboxMap != null && mapboxMap.getStyle() != null) {
            Style style = mapboxMap.getStyle();

            if (style.getImage(MARKER_ICON_ID) == null) {
                //  Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.cedarmaps_marker_icon_default, null);
                //  Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                style.addImage(MARKER_ICON_ID, BitmapFactory.decodeResource(
                        getResources(), R.drawable.cedarmaps_marker_icon_default));
            }

            FeatureCollection featureCollection = FeatureCollection.fromJson(loadGeoJsonFile(jsonName));

            if (style.getSource(MARKERS_SOURCE) == null) {

                geoJsonSource = new GeoJsonSource(MARKERS_SOURCE, featureCollection);
                style.addSource(geoJsonSource);

            } else {
                geoJsonSource = (GeoJsonSource) style.getSource(MARKERS_SOURCE);
            }
            if (geoJsonSource == null) {
                return;
            }

            style.removeLayer(MARKERS_LAYER);

            SymbolLayer symbolLayer = new SymbolLayer(MARKERS_LAYER, MARKERS_SOURCE);
            symbolLayer.withProperties(
                    PropertyFactory.iconImage(MARKER_ICON_ID),
                    PropertyFactory.iconAllowOverlap(true)
            );
            style.addLayer(symbolLayer);
        }
    }

    private String loadGeoJsonFile(String fileName) {
        String contents = "";

        try {
            InputStream stream = new FileInputStream(fileName);

            int size = stream.available();
            byte[] buffer = new byte[size];

            stream.read(buffer);
            stream.close();

            contents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return contents;
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
            if (hasFocus && state == HomeFragment.State.MAP_PIN) {
                circleManager.deleteAll();
                if (!TextUtils.isEmpty(searchView.getQuery())) {
                    setState(HomeFragment.State.RESULTS);
                } else {
                    setState(HomeFragment.State.MAP);
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
                    setState(HomeFragment.State.MAP);
                } else {
                    setState(HomeFragment.State.SEARCHING);
                    LatLng mapTargetLat=mapboxMap.getCameraPosition().target;
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
                            Toast.makeText(getActivity(), "پاسخ جستجو دریافت شد", Toast.LENGTH_SHORT).show();
                            setState(HomeFragment.State.RESULTS);
                            if (response.getCount() > 0 && newText.equals(mSearchView.getQuery().toString())) {

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

    public void showItemOnMap(final SearchItem item) {
        Log.d("onclick","view clicked");
        setState(HomeFragment.State.MAP_PIN);
        if (getActivity() == null || item.getGeom().getCoordinates() == null) {
            return;
        }

        circleManager.deleteAll();
        mSearchView.clearFocus();

        int color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        int strokeColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        double longitude = item.getGeom().getLongitude();
        double latitude = item.getGeom().getLatitude();

        LatLng latLng2=new LatLng(latitude,longitude);
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
                Toast.makeText(getContext(), item.getAddress(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), item.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });

        if (item.getGeom() != null) {
            mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(latLng2), 1000);
        }
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

        private WeakReference<HomeFragment> fragmentWeakReference;

        MapFragmentLocationCallback(HomeFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        /* The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            HomeFragment fragment = fragmentWeakReference.get();

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