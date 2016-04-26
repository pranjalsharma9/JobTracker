package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Pranjal on 16-04-2016.
 */
public class CustomerRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Customer> customers;

    public CustomerRecyclerView(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    //Code to handle clicks
    private static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        CardView rowView;
        rowView = (CardView) inflater.inflate(R.layout.customer_list_card, parent, false);
        viewHolder = new MyItemHolder(rowView);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Customer customer = customers.get(position);
        ((MyItemHolder) holder).setName(customer.getName());
        ((MyItemHolder) holder).setMobile(customer.getMobile());
        ((MyItemHolder) holder).setEmail(customer.getEmail());
        ((MyItemHolder) holder).setAddress(customer.getAddress());
        ((MyItemHolder) holder).setCustomerImg(customer.getProfilePic());

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }


    public static class MyItemHolder extends RecyclerView.ViewHolder {

        TextView name, mobile, email, address;
        ImageView customerImg;

        public MyItemHolder(final CardView itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cardView_customer_name);
            mobile = (TextView) itemView.findViewById(R.id.cardView_customer_mobile);
            email = (TextView) itemView.findViewById(R.id.cardView_customer_email);
            address = (TextView) itemView.findViewById(R.id.cardView_customer_address);
            customerImg = (ImageView) itemView.findViewById(R.id.cardView_customer_image);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }

        public void setMobile(String s) {
            mobile.setText(s);
        }

        public void setEmail(String s) {
            email.setText(s);
        }

        public void setAddress(String s) {
            address.setText(s);
        }

        public void setName(String s) {
            name.setText(s);
        }

        public void setCustomerImg(Bitmap b){
            customerImg.setImageBitmap(b);
        }

    }
}

