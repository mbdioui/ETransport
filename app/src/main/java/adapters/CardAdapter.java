package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import Beans.Transport;
import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 14/12/2016.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<Transport> items;
    private HashMap<Integer,String> mapgoods= new HashMap<>();


    public CardAdapter(ArrayList<Transport> list, HashMap map){
        super();
        items = new ArrayList<>();
        for(int i =0; i<list.size(); i++){
            items.add(list.get(i));
        }
        mapgoods=map;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transport_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transport item =  items.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

        if (item.getDate_go()!=null)
            holder.dateTextView.setText(String.valueOf(dateFormatter.format(item.getDate_go())));
        else if (item.getDate_go_min()!=null)
            holder.dateTextView.setText(String.valueOf(dateFormatter.format(item.getDate_go_min())));
        if (!item.getAddress_from().equals("null"))
            holder.AddressgoTextView.setText(item.getAddress_from());
        else
            holder.AddressgoTextView.setText("inconnue");
        if (!item.getAddress_to().equals("null"))
            holder.AddressArriveTextView.setText(item.getAddress_to());
        else
            holder.AddressArriveTextView.setText("inconnue");

        //goods
        if (item.getType_goods()>0 && item.getType_goods()<7)
        {
            holder.TypegoodsTextView.setText(mapgoods.get(item.getType_goods()));
        }
        else
            holder.TypegoodsTextView.setText("non spécifiée");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTextView;
        public TextView AddressgoTextView;
        public TextView AddressArriveTextView;
        public TextView TypegoodsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_departtextview);
            AddressgoTextView =(TextView) itemView.findViewById(R.id.address_departtextview);
            AddressArriveTextView =(TextView) itemView.findViewById(R.id.address_arrivettextview);
            TypegoodsTextView = (TextView) itemView.findViewById(R.id.typegoods_textview);
        }
    }
}