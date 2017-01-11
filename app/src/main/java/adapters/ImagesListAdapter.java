package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 11/01/2017.
 */

public class ImagesListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private HashMap<String,Boolean> map;

    public ImagesListAdapter(Context context, String[] values, HashMap<String,Boolean> map) {
        super(context, R.layout.rowimagelist, values);
        this.context = context;
        this.values = values;
        this.map= map;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowimagelist, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        String s = values[position];
        if (map.get(s)) {
            imageView.setImageResource(android.R.drawable.presence_online);
        } else {
            imageView.setImageResource(android.R.drawable.presence_offline);
        }

        return rowView;
    }
}
