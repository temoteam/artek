package org.artek.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.artek.app.R;

/**
 * Created by Sergey on 01.11.2016.
 */

public class CampsAdapter extends ArrayAdapter<Camps> {

    Context context;
    int layoutResourceId;
    Camps data[] = null;

    public CampsAdapter(Context context, int layoutResourceId, Camps[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CampsHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CampsHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.campIcon);

            row.setTag(holder);
        } else {
            holder = (CampsHolder) row.getTag();
        }

        Camps camps = data[position];
        holder.imgIcon.setImageResource(camps.icon);

        return row;
    }

    static class CampsHolder {
        ImageView imgIcon;
    }
}