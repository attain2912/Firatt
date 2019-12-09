package com.firatt.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firatt.R;
import com.firatt.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    public List<Chat> messList;
    String peo;

    private final int TYPE_RECEIVE_MESSAGE = 0;
    private final int TYPE_SEND_MESSAGE = -1;
    public ChatAdapter(Context context,List<Chat> messList,String peo){
        this.context=context;
        this.messList=messList;
        this.peo=peo;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout= -1;
        switch (viewType){
            case TYPE_SEND_MESSAGE:
                layout =R.layout.item_chat;
            break;

            case TYPE_RECEIVE_MESSAGE:
                layout=R.layout.item_receive;
            break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Chat mess= messList.get(position);

        holder.tvmess.setText(mess.getMessageText());
        holder.tvtime.setText(DateFormat.format("hh:mm",mess.getMessageTime()));

    }

    @Override
    public int getItemCount() {
        return messList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvmess;
        TextView tvtime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvmess=itemView.findViewById(R.id.tvmess);
            tvtime=itemView.findViewById(R.id.tvtime);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messList.get(position).getType()==0){
            if(messList.get(position).send.equals(peo)){
                return -1;
            }else {
                return 0;
            }
        }else {
            return messList.get(position).getType();
        }
    }

}
