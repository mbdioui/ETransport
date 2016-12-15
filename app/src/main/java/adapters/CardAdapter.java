package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Beans.Transport;
import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 14/12/2016.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    ArrayList<Transport> items;

    public CardAdapter(List<Transport> list){
        super();
        items = new ArrayList<Transport>();
        for(int i =0; i<list.size(); i++){
            Transport item = new Transport();
            item.setTransport_id(list.get(i).getTransport_id());
            item.setText(list.get(i).getText());
            items.add(item);
        }
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
        holder.TextIdView.setText(String.valueOf(item.getTransport_id()));
        holder.TextViewDescription.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView TextIdView;
        public TextView TextViewDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            TextIdView = (TextView) itemView.findViewById(R.id.firstLine);
            TextViewDescription = (TextView) itemView.findViewById(R.id.secondLine);

        }
    }
}