package com.pranjals.nsit.jobtracker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pranjal Verma on 4/15/2016.
 */
public class NavDrawerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> menuItems;
    String ownerName,ownerEmail;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static OnItemClickListener listener;
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public NavDrawerListAdapter(ArrayList<String> menuItems,String ownerName, String ownerEmail){
        this.menuItems = menuItems;
        this.ownerName=ownerName;
        this.ownerEmail=ownerEmail;
    }

    @Override
    public NavDrawerListAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_count,parent,false);
            mViewHolder viewHolder = new mViewHolder(v,viewType);
            return viewHolder;
        }
        else if(viewType==TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,parent,false);
            mViewHolder viewHolder = new mViewHolder(v,viewType);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(position==0){

            ((NavDrawerListAdapter.mViewHolder)holder).owenerName.setText(ownerName);
            ((NavDrawerListAdapter.mViewHolder)holder).ownerEmail.setText(ownerEmail);
        }
        else {

            ((NavDrawerListAdapter.mViewHolder)holder).menuItem.setText(menuItems.get(position-1));
        }

    }

    @Override
    public int getItemCount() {
        return menuItems.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public static class mViewHolder extends RecyclerView.ViewHolder{

        TextView menuItem;
        TextView owenerName;
        TextView ownerEmail;

        public mViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==TYPE_ITEM){
                menuItem = (TextView)itemView.findViewById(R.id.drawer_list_item_tv);
                menuItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("sdjksjdksjsk", "working");
                        if(listener != null)
                            listener.onItemClick(v,getLayoutPosition());
                    }
                });
            }
            else
            {
                owenerName = (TextView)itemView.findViewById(R.id.owner_name);
                ownerEmail = (TextView)itemView.findViewById(R.id.owner_email);

            }

        }
    }
}
