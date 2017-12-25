package com.mikhailbiltsevich.app7;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearestLocationsFragment extends Fragment implements FragmentTitle {

    private LocationManager locationManager;
    private String mTitle;

    private TextView myLocationTextView;
    private ListView nearestLocationsListView;

    private List<BeltelecomBranch> branches;
    ArrayAdapter<BeltelecomBranch> adapter;

    public NearestLocationsFragment(){
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        branches = getBeltelecomBranches();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, branches);
    }

    @Override
    public void onStart() {
        nearestLocationsListView.setAdapter(adapter);
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        View view = inflater.inflate(R.layout.fragment_nearest_locations, container, false);

        myLocationTextView = view.findViewById(R.id.my_location);
        nearestLocationsListView = view.findViewById(R.id.nearest_locations_list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setNeutralButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setTitle("Разрешение на определение местоположения");
            builder.setMessage("В настройках включите приложению доступ к местоположению для работы данного функционала");

            builder.create().show();
            return;
        }

        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000 * 2, 10, locationListener);
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000 * 2, 10, locationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null){

                Location placeLocation = new Location(location);
                for(BeltelecomBranch branch : branches) {
                    placeLocation.setLatitude(branch.getLatitude());
                    placeLocation.setLongitude(branch.getLongitude());
                    branch.setDistance(location.distanceTo(placeLocation));
                };

                Collections.sort(branches, new Comparator<BeltelecomBranch>() {
                    @Override
                    public int compare(BeltelecomBranch branch, BeltelecomBranch t1) {
                        return Double.compare(branch.getDistance(), t1.getDistance());
                    }
                });

                adapter.notifyDataSetChanged();
                myLocationTextView.setText(String.format("Моё местоположение: %f, %f", location.getLatitude(), location.getLongitude()));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            myLocationTextView.setText("Определение местоположения");
        }

        @Override
        public void onProviderDisabled(String s) {
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                myLocationTextView.setText("Местоположение недоступно");
            }
        }
    };

    private List<BeltelecomBranch> getBeltelecomBranches() {
        SQLiteOpenHelper helper = new BeltelecomBranchesDatabaseHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("BELTELECON_BRANCH", new String[]{
                "_id",
                "name",
                "address",
                "latitude",
                "longitude"}, null, null, null, null, null);

        List<BeltelecomBranch> branches = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                branches.add(new BeltelecomBranch(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return branches;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}