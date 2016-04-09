package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pranjal on 9/4/16.
 */
public class OrderRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Order> orders;
    private  int DETAILED = 1,BRIEF = 0;
    public OrderRecyclerView(ArrayList<Order> orders){
        this.orders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        CardView rowView;
        switch (viewType) {

            case 0 :
                rowView = (CardView) inflater.inflate(R.layout.order_list_card,parent,false);
                viewHolder = new MyItemHolder(rowView);
                return viewHolder;

            case 1 :
                rowView = (CardView) inflater.inflate(R.layout.order_card_detailed, parent, false);
                viewHolder = new MyItemHolder_detailed(rowView);
                return viewHolder;

            default:
                rowView = (CardView) inflater.inflate(R.layout.order_list_card, parent, false);
                viewHolder = new MyItemHolder(rowView);
                return viewHolder;


        }


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Order order = orders.get(position);
        if(position==0){
            ((MyItemHolder_detailed)holder).setName(order.getName());
            ((MyItemHolder_detailed)holder).setCid(order.getCid() + "");
            ((MyItemHolder_detailed)holder).setDoc(order.getDoc());
            ((MyItemHolder_detailed)holder).setDoo(order.getDoo());}
        else {
            ((MyItemHolder)holder).setName(order.getName());
            ((MyItemHolder)holder).setCid(order.getCid() + "");
            ((MyItemHolder)holder).setDoc(order.getDoc());
            ((MyItemHolder)holder).setDoo(order.getDoo());
        }


    }


    @Override
    public int getItemViewType(int position) {
        // return super.getItemViewType(position);
        if(position==0)
            return DETAILED;
        else
            return BRIEF;

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public static class MyItemHolder extends RecyclerView.ViewHolder{

        TextView name,doo,doc,cid;
        ImageView orderImg;

        public MyItemHolder(CardView itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.cardView_name);
            doo = (TextView)itemView.findViewById(R.id.cardView_doo);
            doc = (TextView)itemView.findViewById(R.id.cardView_doc);
            cid = (TextView)itemView.findViewById(R.id.cardView_cid);
            orderImg = (ImageView)itemView.findViewById(R.id.cardView_image);

        }

        public void setCid(String s) {
            cid.setText(s);
        }

        public void setDoc(String s) {
            doc.setText(s);
        }

        public void setDoo(String s) {
            doo.setText(s);
        }

        public void setName(String s) {
            name.setText(s);
        }
    }


    public static class MyItemHolder_detailed extends RecyclerView.ViewHolder {

        TextView name,doo,doc,cid;
        ImageView orderImg;

        public MyItemHolder_detailed(CardView itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.cardView_detail_name);
            doo = (TextView)itemView.findViewById(R.id.cardView_detail_doo);
            doc = (TextView)itemView.findViewById(R.id.cardView_detail_doc);
            cid = (TextView)itemView.findViewById(R.id.cardView_detail_cid);
            orderImg = (ImageView)itemView.findViewById(R.id.cardView_detail_image);


        }

        public void setCid(String s) {
            cid.setText(s);
        }

        public void setDoc(String s) {
            doc.setText(s);
        }

        public void setDoo(String s) {
            doo.setText(s);
        }

        public void setName(String s) {
            name.setText(s);
        }

    }
}