package afeka.com.hw1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderboardMap extends SupportMapFragment implements OnMapReadyCallback{
    private View view;
    private GoogleMap googleMap;
    private ArrayList<Score> scoreList;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        scoreList = new ArrayList<>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("PlayerScore");
        query.orderByDescending("Score");
        try {
            List<ParseObject> data = query.find();
            for(ParseObject ob : data){
                Score temp = new Score();
                temp.setUserName(ob.getString("Name"));
                temp.setGameMode(ob.getInt("GameMode"));
                temp.setScore(ob.getDouble("Score"));
                temp.setCoordinates(ob.getParseGeoPoint("Coordinates"));
                scoreList.add(temp);

            }
        } catch (ParseException e){
            e.printStackTrace();
        }


        getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // goToLocation(32.0863863,34.809778, 16);


        for(Score score: scoreList){
            MarkerOptions opt = new MarkerOptions()
                    .title(score.getName())
                    .position(new LatLng(score.getCoordinates().getLatitude(),score.getCoordinates().getLongitude()))
                    .snippet("Game Mode: " + score.getGameMode() + " Score: " + score.getScore());
            this.googleMap.addMarker(opt);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation

            }
    }
        googleMap.setMyLocationEnabled(true);

    }

    private void goToLocation(double x, double y, int zoom) {
        LatLng ll = new LatLng(x,y);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        googleMap.moveCamera(update);
    }


}
