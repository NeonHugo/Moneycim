package com.vsahin.moneycim.View.SpendingList;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.vsahin.moneycim.Model.Pojo.Spending;
import com.vsahin.moneycim.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Volkan Şahin on 22.08.2017.
 */

public class SpendingRecyclerViewAdapter extends RecyclerView.Adapter<SpendingRecyclerViewAdapter.MyViewHolder> {

    private List<Spending> spendings;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); //you can add dd/MM/yyyy for date
    private  LayoutInflater layoutInflater;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    private int lastPosition = -1;
    Context context;

    public SpendingRecyclerViewAdapter(Context context, ArrayList<Spending> spendings, RecyclerViewItemClickListener listener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.spendings = spendings;
        this.recyclerViewItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_spending_list_row, viewGroup, false);
        Log.d(TAG, "*** onCreateViewHolder: created");
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Spending s = spendings.get(position);
        Double quantity = s.getRawSpending().getQuantity();
        String description = s.getRawSpending().getDescription();
        Date date = s.getRawSpending().getDate();
        String group = s.getGroupName();

        String quantityWithCurrency = context.getString(R.string.turkish_lira_symbol) + String.valueOf(quantity);
        viewHolder.getTxtQuantity().setText(quantityWithCurrency);
        viewHolder.getTxtDescription().setText(description);
        viewHolder.getTxtDate().setText(dateFormat.format(date));
        viewHolder.getTxtSpendingGroup().setText(group);

        setAnimation(viewHolder.itemView, position);

        Log.d(TAG, "*** onBindViewHolder: binded");
    }

    @Override
    public int getItemCount() {
        return spendings.size();
    }

    public void updateItems(List<Spending> spendings){
        final SpendingDiffCallback diffCallback = new SpendingDiffCallback(this.spendings, spendings);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.spendings.clear();
        this.spendings.addAll(spendings);
        diffResult.dispatchUpdatesTo(this);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(slide_up);
            lastPosition = position;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        @BindView(R.id.txtQuantity)
        TextView txtQuantity;

        @BindView(R.id.txtDescription)
        TextView txtDescription;

        @BindView(R.id.txtSpendingGroup)
        TextView txtSpendingGroup;

        @BindView(R.id.txtDate)
        TextView txtDate;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public TextView getTxtDescription() {
            return txtDescription;
        }

        public TextView getTxtSpendingGroup() {
            return txtSpendingGroup;
        }

        public TextView getTxtDate() {
            return txtDate;
        }

        public TextView getTxtQuantity() {
            return txtQuantity;
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if(position != -1){
                recyclerViewItemClickListener.onItemLongClick(spendings.get(position).getRawSpending().getId());
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != -1){
                recyclerViewItemClickListener.onItemClick(spendings.get(position).getRawSpending());
            }
        }
    }
}
