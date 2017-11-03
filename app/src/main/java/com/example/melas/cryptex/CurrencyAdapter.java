package com.example.melas.cryptex;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.melas.cryptex.models.Currency;

import java.util.ArrayList;
import java.util.Locale;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>{

    private ListItemClickListener listener;
    private ArrayList<Currency> currencyList;


    public interface ListItemClickListener {
        void onListItemClick(int itemClickedIndex);
    }

    public CurrencyAdapter(ArrayList<Currency> currencyList, ListItemClickListener listener) {
        this.currencyList = currencyList;
        this.listener = listener;
    }

    @Override
    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.currency, parent, false);
        return new CurrencyHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.currencyName.setText(currency.getShortCode());
        NumberFormat format;
        String btc = String.valueOf(currency.getBtcConversionRate());
        String eth = String.valueOf(currency.getEthConversionRate());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(android.icu.util.Currency.getInstance(currency.getName()));
            btc = format.format(currency.getBtcConversionRate());
            eth = format.format(currency.getEthConversionRate());
        }
        holder.btcValue.setText(btc);
        holder.ethValue.setText(eth);
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    class CurrencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView currencyName;
        TextView btcValue;
        TextView ethValue;
        private CurrencyHolder(View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.currency_name);
            btcValue = itemView.findViewById(R.id.btc_value);
            ethValue = itemView.findViewById(R.id.eth_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onListItemClick(position);
        }
    }
}
