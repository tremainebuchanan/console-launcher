package com.tremainebuchanan.consolelauncher.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tremainebuchanan.consolelauncher.R;
import com.tremainebuchanan.consolelauncher.models.Command;
import java.util.List;

/**
 * Created by captain_kirk on 8/12/17.
 */

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.MyViewHolder> {
    private List<Command> commandList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView input;
        public MyViewHolder(View view){
            super(view);
            input =  (TextView) view.findViewById(R.id.input);
        }
    }

    public CommandAdapter(List<Command> commandList){
        this.commandList = commandList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.command_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Command command = commandList.get(position);
        holder.input.setText(command.getUserInput());
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }
}
