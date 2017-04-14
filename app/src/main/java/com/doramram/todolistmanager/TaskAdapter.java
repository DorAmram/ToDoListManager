package com.doramram.todolistmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    final static String TAG = "TaskAdapter";

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private List<Task> listItems;
    private Context mContext;
    DataBaseHelper helper;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewGroup viewGroup;
        public TextView id;
        public TextView title;
        public TextView date;

        public ViewHolder(ViewGroup viewGroup) {
            super(viewGroup);
            id = (TextView) viewGroup.findViewById(R.id.task_id);
            title = (TextView) viewGroup.findViewById(R.id.task_title);
            date = (TextView) viewGroup.findViewById(R.id.task_date);
            this.viewGroup = viewGroup;
        }
    }

    public TaskAdapter(List<Task> listItems, Context mContext, DataBaseHelper helper) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.helper = helper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(viewGroup);
        Log.v(TAG, "viewholder created");

        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout linearLayout = new RelativeLayout(mContext);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle(viewHolder.title.getText());
                alertDialogBuilder.setView(linearLayout);

                if(viewHolder.title.getText().toString().startsWith("Call")){

                    final Task task = listItems.get(viewHolder.getAdapterPosition());
                    TextView callTextView = getCallTextView(task, mContext);

                    callTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String number = task.get_title();
                            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                            mContext.startActivity(dial);
                        }
                    });

                    linearLayout.addView(callTextView);
                }

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String taskId = String.valueOf(viewHolder.id.getText());
                                        Log.v(TAG, "removing " + taskId);
                                        helper.deleteTask(Long.parseLong(taskId));
                                        listItems.remove(viewHolder.getAdapterPosition());
                                        notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Task task = listItems.get(position);

        holder.id.setText(String.valueOf(task.get_id()));
        holder.title.setText(task.get_title());
        Date date = listItems.get(position).get_date();
        holder.date.setText(format.format(date));

        if (toCalendar(date).before(Calendar.getInstance())){
            holder.date.setTextColor(Color.RED);
            holder.title.setTextColor(Color.RED);
        } else {
            holder.date.setTextColor(Color.GRAY);
            holder.title.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {return listItems.size();}


    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public TextView getCallTextView(Task task, Context context){
        final TextView callTextView = new TextView(mContext);
        callTextView.setId(View.generateViewId());
        final String number = task.get_title().substring("Call ".length());
        callTextView.setText("Call " + number);
        RelativeLayout.LayoutParams callParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        callParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        callParams.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER);

        return callTextView;
    }

}
