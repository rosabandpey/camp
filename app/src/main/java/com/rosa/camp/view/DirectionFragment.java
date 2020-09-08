package com.rosa.camp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rosa.camp.R;
import com.rosa.camp.ui.adapter.SearchViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.enums.RouteType;
import ir.map.servicesdk.enums.SelectOptions;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.model.inner.RouteItem;
import ir.map.servicesdk.model.inner.SearchItem;
import ir.map.servicesdk.request.RouteRequest;
import ir.map.servicesdk.request.SearchRequest;
import ir.map.servicesdk.response.RouteResponse;
import ir.map.servicesdk.response.SearchResponse;
import okhttp3.Route;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends Fragment {

    public Style mapStyle;
    private static final String DEPARTURE_IMAGE = "DEPARTURE_IMAGE";
    private static final String DESTINATION_IMAGE = "DESTINATION_IMAGE";
    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private LinearLayout hintLayout;
    private LinearLayout resultLayout;
    private ProgressBar progressBar;
    private TextView hintTextView;
    private TextView distanceTextView;
    private ProgressBar sProgressBar;
    private AppCompatTextView mTextView;
    private LocationEngine locationEngine = null;
    private SearchView mSearchView;
    private SearchViewAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private DirectionFragment.State state = DirectionFragment.State.MAP;
    private CircleManager circleManager;
    private LinearLayout mLinearLayout;
    private SymbolManager symbolManager;
    private LineManager lineManager;
    private MapService mapService = new MapService();

    private ArrayList<Symbol> symbols = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DirectionFragment() {
        // Required empty public constructor
    }

    public static DirectionFragment newInstance() {
        DirectionFragment f = new DirectionFragment();
        return f;
    }

    private enum State {
        MAP,
        MAP_PIN,
        SEARCHING,
        RESULTS
    }
    private void setState(DirectionFragment.State state) {
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
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectionFragment newInstance(String param1, String param2) {
        DirectionFragment fragment = new DirectionFragment();
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

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_direction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.mapView);
        hintLayout = view.findViewById(R.id.direction_hint_layout);
        resultLayout = view.findViewById(R.id.direction_result_layout);
        progressBar = view.findViewById(R.id.direction_progress_bar);
        hintTextView = view.findViewById(R.id.direction_hint_text_view);
        distanceTextView = view.findViewById(R.id.direction_distance_text_view);
        sProgressBar = view.findViewById(R.id.search_progress_bar);
        view.findViewById(R.id.direction_reset_button).setOnClickListener(v -> resetToInitialState());

        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMapinternal) {
                mMapboxMap = mapboxMapinternal;
                Style.Builder style1;
                mMapboxMap.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {

                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;
                        circleManager = new CircleManager(mMapView, mMapboxMap, style);
                        style.addImage(DEPARTURE_IMAGE, Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.cedarmaps_marker_icon_start)));
                        style.addImage(DESTINATION_IMAGE, Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.cedarmaps_marker_icon_end)));
                        symbolManager = new SymbolManager(mMapView, mMapboxMap, style);

                        lineManager = new LineManager(mMapView, mMapboxMap, style);
                        // TODO;

                    }

                });

                mMapboxMap.setMaxZoomPreference(17);
                mMapboxMap.setMinZoomPreference(6);

                mMapboxMap.addOnMapClickListener(latLng -> {
                    if (symbols.size() == 0) {
                        addMarkerToMapViewAtPosition(latLng, DEPARTURE_IMAGE);
                    } else if (symbols.size() == 1) {
                        addMarkerToMapViewAtPosition(latLng, DESTINATION_IMAGE);
                        computeDirection(symbols.get(0).getLatLng(), symbols.get(1).getLatLng());
                    }
                    return true;
                });

            }

        });

        //Searching
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mLinearLayout = view.findViewById(R.id.search_results_linear_layout);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && state == DirectionFragment.State.MAP_PIN) {
                setState(DirectionFragment.State.RESULTS);
                return true;
            }
            return false;
        });

    }

    private void computeDirection(LatLng departure, LatLng destination) {
        progressBar.setVisibility(View.VISIBLE);
        hintTextView.setVisibility(View.GONE);
        progressBar.animate();


        //Routing
        RouteRequest requestBody = new RouteRequest.Builder(
                departure.getLatitude(),departure.getLongitude() ,destination.getLatitude(),destination.getLongitude(),

                RouteType.DRIVING
        ).build();
        mapService.route(requestBody, new ResponseListener<RouteResponse>() {
            @Override
            public void onSuccess(RouteResponse response) {
                Toast.makeText(getActivity(),"پاسخ مسیریابی دریافت شد!",Toast.LENGTH_SHORT).show();
                progressBar.clearAnimation();
                if (response.getRoutes() == null) {
                    return;
                }
                RouteItem route = response.getRoutes().get(0);



                if (route.getGeometry() == null ) {
                    return;
                }
                String geometry=route.getGeometry();
                drawCoordinatesInBound(geometry);

                hintLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);

            }
            @Override
            public void onError(MapirError error) {
                progressBar.clearAnimation();
                resetToInitialState();
                Toast.makeText(getActivity(),"مشکلی در مسیریابی پیش آمده",Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void drawCoordinatesInBound(String geometry) {
        if (mMapboxMap == null || getContext() == null) {
            return;
        }

        LineString routeLine = LineString.fromPolyline(geometry, 5);
        LineOptions lineOptions = new LineOptions()
                .withGeometry(routeLine)
                .withLineColor("#ff5252")
                .withLineWidth(5f);
        lineManager.create(lineOptions);

    }

    private void resetToInitialState() {
        symbolManager.deleteAll();
        lineManager.deleteAll();
        symbols.clear();

        resultLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        hintTextView.setVisibility(View.VISIBLE);
        hintLayout.setVisibility(View.VISIBLE);
    }

    private void addMarkerToMapViewAtPosition(LatLng coordinate, String imageName) {
        if (mMapboxMap != null && getContext() != null) {
            SymbolOptions options = new SymbolOptions()
                    .withLatLng(coordinate)
                    .withIconOffset(new Float[]{0f, -22f})
                    .withIconImage(imageName);
            symbols.add(symbolManager.create(options));
        }
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
            if (hasFocus && state == DirectionFragment.State.MAP_PIN) {
                circleManager.deleteAll();
                if (!TextUtils.isEmpty(searchView.getQuery())) {
                    setState(DirectionFragment.State.RESULTS);
                } else {
                    setState(DirectionFragment.State.MAP);
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
                    setState(DirectionFragment.State.MAP);
                } else {
                    setState(DirectionFragment.State.SEARCHING);
                    LatLng mapTargetLat=mMapboxMap.getCameraPosition().target;
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
                            setState(DirectionFragment.State.RESULTS);
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
        setState(DirectionFragment.State.MAP_PIN);
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
            mMapboxMap.easeCamera(CameraUpdateFactory.newLatLng(latLng2), 1000);
        }
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (symbolManager != null) {
            symbolManager.onDestroy();
        }
        mMapView.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mMapView = null;
    }


}