package com.example.melas.cryptex;

import android.icu.text.NumberFormat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.melas.cryptex.models.Currency;

import java.util.Locale;

public class ConversionActivity extends AppCompatActivity {

    private Currency currency;
    private EditText currencyInput;
    private TextView btcTextView;
    private TextView ethTextView;
    private NumberFormat format;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String input = charSequence.toString();
            double doubleValue = 0.00;
            if (!input.isEmpty()) {
                doubleValue = Double.parseDouble(input);
            }
            double btcValue = doubleValue * currency.getBtcConversionRate();
            double ethValue = doubleValue * currency.getEthConversionRate();

            String btc = String.format(Locale.getDefault(),"%.2f", btcValue);
            String eth = String.format(Locale.getDefault(),"%.2f", btcValue);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                format = NumberFormat.getCurrencyInstance(Locale.getDefault());
                format.setCurrency(android.icu.util.Currency.getInstance(currency.getName()));
                btc = format.format(btcValue);
                eth = format.format(ethValue);
            }
            ethTextView.setText(eth);
            btcTextView.setText(btc);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle bundle = getIntent().getExtras();
        try {
            currency = bundle.getParcelable(MainActivity.DATA_CURRENCY);
        } catch (NullPointerException e) {
            Log.i("NULL", e.getMessage());
        }
        currencyInput = findViewById(R.id.currency_input);
        currencyInput.setText(getString(R.string.conversion_start_value));


        String btc = String.valueOf(currency.getBtcConversionRate());
        String eth = String.valueOf(currency.getEthConversionRate());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(android.icu.util.Currency.getInstance(currency.getName()));
            btc = format.format(currency.getBtcConversionRate());
            eth = format.format(currency.getEthConversionRate());
        }

        btcTextView = findViewById(R.id.btc_value);
        btcTextView.setText(btc);
        ethTextView = findViewById(R.id.eth_value);
        ethTextView.setText(eth);

        currencyInput.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
