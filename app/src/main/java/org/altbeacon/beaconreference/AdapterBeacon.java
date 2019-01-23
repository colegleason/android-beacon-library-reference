package org.altbeacon.beaconreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beaconreference.R;

import java.util.ArrayList;
import java.util.Collection;

public class AdapterBeacon extends ArrayAdapter<Beacon> {

    private Context context;
    private ArrayList<Beacon> allBeacons;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public AdapterBeacon(Context context, ArrayList<Beacon> mBeacons) {
        super(context, R.layout.row_beacon);
        this.context = context;
        this.allBeacons = new ArrayList<Beacon>(mBeacons);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allBeacons .size();
    }

    @Override
    public Beacon getItem(int position) {
        return allBeacons .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(Beacon item) {
        return allBeacons .indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    public void copyBeacons(Collection<Beacon> beacons)
    {
        allBeacons.clear();
        for (Beacon b : beacons)
        {
            allBeacons.add(b);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = mInflater.inflate(R.layout.row_beacon,parent, false);
                    holder.uuid = (TextView) convertView.findViewById(R.id.t1);
                    holder.majorId = (TextView) convertView.findViewById(R.id.t2);
                    holder.minorId = (TextView) convertView.findViewById(R.id.t3);
                    holder.rssi = (TextView) convertView.findViewById(R.id.t4);
                    holder.distance = (TextView) convertView.findViewById(R.id.t5);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.uuid.setText("UUID: " + allBeacons.get(position).getId1().toString());
        holder.majorId.setText("Major ID: " + allBeacons.get(position).getId2().toString());
        holder.minorId.setText("Minor ID: " + allBeacons.get(position).getId3().toString());
        holder.rssi.setText("RSSI: " + allBeacons.get(position).getRssi() + "db");
        holder.distance.setText("Estimated Distance: " + allBeacons.get(position).getDistance() + "m");
        holder.pos = position;
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    //---------------static views for each row-----------//
    static class ViewHolder {

        TextView uuid;
        TextView majorId;
        TextView minorId;
        TextView rssi;
        TextView distance;
        int pos; //to store the position of the item within the list
    }
}