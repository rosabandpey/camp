package com.rosa.camp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rosa.camp.R;

import java.util.ArrayList;

import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.enums.RouteType;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.request.RouteRequest;
import ir.map.servicesdk.response.RouteResponse;
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
                        style.addImage(DEPARTURE_IMAGE, ContextCompat.getDrawable(getContext(), R.drawable.cedarmaps_marker_icon_start));
                        style.addImage(DESTINATION_IMAGE, ContextCompat.getDrawable(getContext(), R.drawable.cedarmaps_marker_icon_end));
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
                Route route = response.getRoutes().get(0);
                Double distance = route.getDistance();
                if (distance == null) {
                    return;
                }
                if (distance > 1000) {
                    distance = distance / 1000.0;
                    distance = (double)Math.round(distance * 100d) / 100d;
                    distanceTextView.setText(String.format("%s Km", distance));
                } else  {
                    distance = (double)Math.round(distance);
                    distanceTextView.setText(String.format("%sm", distance));
                }

                if (route.getGeometry() == null || route.getGeometry().getCoordinates() == null) {
                    return;
                }
                ArrayList<LatLng> coordinates = new ArrayList<>(route.getGeometry().getCoordinates());

                drawCoordinatesInBound(coordinates, route.getBoundingBox());

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




        CedarMaps.getInstance().direction(departure, destination,
                new GeoRoutingResultListener() {
                    @Override
                    public void onSuccess(@NonNull GeoRouting result) {
                        progressBar.clearAnimation();
                        if (result.getRoutes() == null) {
                            return;
                        }
                        Route route = result.getRoutes().get(0);
                        Double distance = route.getDistance();
                        if (distance == null) {
                            return;
                        }
                        if (distance > 1000) {
                            distance = distance / 1000.0;
                            distance = (double)Math.round(distance * 100d) / 100d;
                            distanceTextView.setText(String.format("%s Km", distance));
                        } else  {
                            distance = (double)Math.round(distance);
                            distanceTextView.setText(String.format("%sm", distance));
                        }

                        if (route.getGeometry() == null || route.getGeometry().getCoordinates() == null) {
                            return;
                        }
                        ArrayList<LatLng> coordinates = new ArrayList<>(route.getGeometry().getCoordinates());

                        drawCoordinatesInBound(coordinates, route.getBoundingBox());

                        hintLayout.setVisibility(View.GONE);
                        resultLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(@NonNull String error) {
                        progressBar.clearAnimation();
                        resetToInitialState();
                        Toast.makeText(getActivity(),
                                "خطا در دریافت اطلاعات مسیریابی" + "\n" + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void drawCoordinatesInBound(ArrayList<LatLng> coordinates, LatLngBounds bounds) {
        if (mMapboxMap == null || getContext() == null) {
            return;
        }
        LineOptions options = new LineOptions()
                .withLatLngs(coordinates)
                .withLineWidth(6f)
                .withLineColor(ColorUtils.colorToRgbaString(ContextCompat.getColor(getContext(), R.color.colorPrimary)))
                .withLineOpacity(0.9f);
        lineManager.create(options);

        mMapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150), 1000);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}