package com.example.juraj.note.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.data.Note;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FragmentMap extends AbstractFragent implements OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GoogleMap gMap;
    private MapView mapView;
    private String mParam1;
    private String mParam2;
    private String title = "Map";
    private int notesWithPosCount = 0;
    private static ArrayList<Note> notes;

    public FragmentMap() {
        // Required empty public constructor
    }

    public static FragmentMap newInstance(String param1, String param2) {
        FragmentMap fragment = new FragmentMap();
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        notes = (ArrayList<Note>) ((MainActivity) getActivity())
                .getDaoSession().getNoteDao().loadAll();

        mapView = view.findViewById(R.id.map_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(48.153556, 17.100034);
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getContext(), latLng.toString(), Toast.LENGTH_LONG).show();
            }
        });

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < notes.size(); i++) {
            Note item = notes.get(i);
            if (item.getLatitude() != null && !item.getLatitude().equals("")) {
                double lat = Double.parseDouble(item.getLatitude());
                double lon = Double.parseDouble(item.getLongitude());

                gMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lon)));
                builder.include(new LatLng(lat, lon));
            }
        }

        /*LatLngBounds bounds = builder.build();
        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));*/
        //gMap.addMarker(new MarkerOptions().position(sydney).title("Slavin"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void refreshGridView() {

    }
}
