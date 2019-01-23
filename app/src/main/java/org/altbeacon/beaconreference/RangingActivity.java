package org.altbeacon.beaconreference;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    // list example from https://stackoverflow.com/questions/40665728/android-altbeacon-print-all-found-beacons-informations-using-custom-adapter-and
    private ListView listview;
    private ArrayList<Beacon> beaconList;
    private AdapterBeacon bAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        beaconList = new ArrayList<Beacon>();
        this.bAdapter = new AdapterBeacon(this, beaconList);

        listview = (ListView) findViewById(R.id.rangingList);
        listview.setAdapter(bAdapter);

        beaconManager.getBeaconParsers().clear();
        BeaconParser iBeaconParser = new BeaconParser();
        iBeaconParser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(iBeaconParser);
        beaconManager.bind(this);

    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override 
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override 
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.e(TAG, "onBeaconServiceConnect");
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bAdapter.copyBeacons(beacons);
                        listview.setAdapter(bAdapter);
                    }
                });
            }
        });

        try {
            Identifier uuid = Identifier.parse("f7826da6-4fa2-4e98-8024-bc5b71e0893e");
            Identifier majorId = Identifier.parse("555");
            beaconManager.startRangingBeaconsInRegion(new Region("winchesterThurston", uuid, majorId, null));
        } catch (RemoteException e) {
            Log.e(TAG, "Not bound to beacon scanning service");
        }
    }
}
