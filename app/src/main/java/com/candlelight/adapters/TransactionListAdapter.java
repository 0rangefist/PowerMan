package com.candlelight.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rancard.kudi.domain.Transaction;

import java.util.List;

public class TransactionListAdapter  extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Transaction> mTransactions;

    public TransactionListAdapter(Context context, List<Transaction> transactions) {
        mInflater = LayoutInflater.from(context);
        mTransactions = transactions;
    }

    @Override
    public int getCount() {
        return mTransactions.size();
    }

    @Override
    public Object getItem(int position) {
        return mTransactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = mInflater.inflate(com.candlelight.powerman.R.layout.list_row_layout, parent, false);
            holder = new ViewHolder();
            holder.transactionTv = (TextView)view.findViewById(com.candlelight.powerman.R.id.id_field);
            holder.accFromTv = (TextView)view.findViewById(com.candlelight.powerman.R.id.from_field);
            holder.stateTv = (TextView)view.findViewById(com.candlelight.powerman.R.id.state_field);
            holder.eventTv = (TextView)view.findViewById(com.candlelight.powerman.R.id.event_field);
            holder.amountTv = (TextView)view.findViewById(com.candlelight.powerman.R.id.amount_field);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Transaction transaction = mTransactions.get(position);
        String transId = transaction.getTransactionId().toString();
        String account = transaction.getAccountNumber().toString();
        String state = transaction.getState().toString();
        String event = transaction.getEvent().toString();
        String amount = transaction.getAmount()+"";


        holder.transactionTv.setText( transId);
        holder.accFromTv.setText(account);
        holder.stateTv.setText(state);
        holder.eventTv.setText(event);
        holder.amountTv.setText(amount);

        return view;
    }

    private class ViewHolder {
        public TextView transactionTv;
        public TextView accFromTv;
        public TextView stateTv;
        public TextView eventTv;
        public TextView amountTv;
    }
}