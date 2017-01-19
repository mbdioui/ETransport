package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Beans.Transport;
import tn.android.etransport.etransport.Detail_transport_item;
import tn.android.etransport.etransport.R;

/**
 * Created by mohamed salah on 14/12/2016.
 */

public class NewCardAdapter extends RecyclerView.Adapter<NewCardAdapter.ViewHolder> {

    private final PullToRefreshRecyclerView mRecycleview;
    private ArrayList<Transport> items;
    private HashMap<Integer,String> mapgoods= new HashMap<>();
    private int mExpandedPosition =-1;
    private Context context;
    private Activity activity;

    public NewCardAdapter(ArrayList<Transport> list, HashMap map, PullToRefreshRecyclerView recycleview
            , Context context, Activity activity){
        super();
        this.context=context;
        Date currentdate = new Date(System.currentTimeMillis());
        items = new ArrayList<>();
        if (list!=null)
            for(int i =0; i<list.size(); i++){
               if (list.get(i).getTransport_date_arrival()!=null)
                {
                 if (list.get(i).getTransport_date_arrival().compareTo(currentdate)>=0)
                    items.add(list.get(i));
                }
               else
                   if (list.get(i).getTransport_date_arrival_max().compareTo(currentdate)>=0)
                    items.add(list.get(i));
            }
        mapgoods=map;
        mRecycleview=recycleview;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transport_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Transport item =  items.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

        if (item.getTransport_date_go()!=null)
            holder.dateTextView.setText(String.valueOf(dateFormatter.format(item.getTransport_date_go())));
        else if (item.getTransport_date_go_min()!=null)
            holder.dateTextView.setText(String.valueOf(dateFormatter.format(item.getTransport_date_go_min())));
        if (!item.getAddress_from().equals("null"))
            holder.AddressgoTextView.setText(item.getAddress_from());
        else
            holder.AddressgoTextView.setText("inconnue");
        if (!item.getAddress_to().equals("null"))
            holder.AddressArriveTextView.setText(item.getAddress_to());
        else
            holder.AddressArriveTextView.setText("inconnue");

        //goods
        if (item.getId_type_goods()>0 && item.getId_type_goods()<7)
        {
            holder.TypegoodsTextView.setText(mapgoods.get(item.getId_type_goods()));
        }
        else
            holder.TypegoodsTextView.setText("non spécifiée");

//            details adapter
//        if(!item.getTransport_date_add().equals("null"))
//            holder.carddetail_datepublish.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_add())));
//        if (item.getTransport_date_go()!=null)
//        {
//            holder.carddetail_dategomin.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_go())));
//            holder.carddetail_dategomax.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_go())));
//        }
//        else
//        {
//            holder.carddetail_dategomin.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_go_min())));
//            holder.carddetail_dategomax.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_go_max())));
//        }
//        if(item.getTransport_date_arrival_max()!=null)
//            holder.carddetail_datearrivemax.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_arrival_max())));
//        else
//            holder.carddetail_datearrivemax.setTransport_text(String.valueOf(dateFormatter.format(item.getTransport_date_arrival())));
//
//
//        final boolean isExpanded = position==mExpandedPosition;
//        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        holder.card.setActivated(isExpanded);
//        holder.card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = isExpanded ? -1:position;
//                com.transitionseverywhere.TransitionManager.beginDelayedTransition(mRecycleview);
//                notifyDataSetChanged();
//            }
//        });

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, Detail_transport_item.class);
                        String transportstring= item.getTransport();
                        i.putExtra("item", transportstring);
                        context.startActivity(i);
                        Activity activity = (Activity) context;
                        activity.overridePendingTransition(R.anim.zoom_in, R.anim.out_animation);
                    }
                });

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
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_departtextview);
            AddressgoTextView =(TextView) itemView.findViewById(R.id.address_departtextview);
            AddressArriveTextView =(TextView) itemView.findViewById(R.id.address_arrivetextview);
            TypegoodsTextView = (TextView) itemView.findViewById(R.id.typegoods_textview);
            card =(CardView) itemView.findViewById(R.id.maincontent);
        }
    }
}