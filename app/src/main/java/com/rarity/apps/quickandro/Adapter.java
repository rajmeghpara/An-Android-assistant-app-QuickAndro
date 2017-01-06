package com.rarity.apps.quickandro;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    List<String> al_final;

    // this class is data type of design of each row
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView st_mess;
        public LinearLayout l_layout;
        public RelativeLayout r_layout;

        public MyViewHolder(View view) {
            super(view);
            st_mess = (TextView) view.findViewById(R.id.mess);
            l_layout = (LinearLayout) view.findViewById(R.id.inner_layout);
            r_layout = (RelativeLayout) view.findViewById(R.id.rl);
        }
    }

    public Adapter(List<String> alf) {
        al_final = alf;
    }

    @Override
    public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) //Can't actually understand this method.....but this is used to set each row layout
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        //itemView.findViewById(R.id.inner_layout).setBackgroundResource(R.drawable.a);
        //itemView.findViewById(R.id.inner_layout).setBackgroundColor(parent.getResources().getColor(R.color.chatBackground));
        // We can set background color of linear layout of any message from java file by upper line code
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter.MyViewHolder holder, int position) //this method is for editing layout of each row at different positions
    {
        // this is to set different alignment and background of messages of different users
        try{
            if(al_final.get(position).charAt(0) == ' ') {
                holder.r_layout.setGravity(Gravity.RIGHT);
                holder.l_layout.setBackgroundResource(R.drawable.chat_right);
            }
            else {
                holder.r_layout.setGravity(Gravity.LEFT);
                holder.l_layout.setBackgroundResource(R.drawable.chat_left);
            }

            holder.st_mess.setText(al_final.get(position));
        }
        catch(Exception e){holder.st_mess.setText("");}
    }

    @Override
    public int getItemCount() {
        return al_final.size();
    }


}