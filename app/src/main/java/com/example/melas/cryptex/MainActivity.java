package com.example.melas.cryptex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.melas.cryptex.models.ApiResponse;
import com.example.melas.cryptex.models.Currency;
import com.example.melas.cryptex.rest.ApiClient;
import com.example.melas.cryptex.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CurrencyAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private final static String QUERY1 = "BTC,ETH";
    private final static String QUERY2 =
            "USD,NGN,GBP,AUD,CAD,EUR,CNY,GHS,ILS,JMD,JPY,KES,MUR,MXN,NOK,QAR,RUB,RWF,SEK,ZAR";
    public static final String DATA_CURRENCY = "currency";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView currencyRecyclerView;
    private TextView errorMessageTextView;
    private ArrayList<Currency> currencyList;
    private CurrencyAdapter adapter;
    private ArrayList<String> userChoices;
    private ApiInterface apiInterface;

    private SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getCurrencies();
        }
    };

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startAddCurrencyActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUI();
        getUserChoices();
        getCurrencies();
    }

    public void setUI() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        currencyRecyclerView = findViewById(R.id.currency_recycler_view);
        errorMessageTextView = findViewById(R.id.error_message_text_view);
        currencyList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(swipeListener);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        userChoices = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getUserChoices() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> preferencesMap = preferences.getAll();
        userChoices.clear();
        for (Map.Entry<String, ?> entry : preferencesMap.entrySet()) {
            if(entry.getValue().toString().equals("true")) {
                userChoices.add(entry.getKey());
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void getCurrencies() {
        if (!isNetworkAvailable()) {
            showErrors(getString(R.string.network_error));
            return;
        }

        swipeRefreshLayout.setRefreshing(true);
        hideErrors();

        Call<ApiResponse> call = apiInterface.getCurrencies(QUERY1, QUERY2);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()) {
                    currencyList.clear();
                    Set<Map.Entry<String, Double>> btcSet = response.body().getBTC().entrySet();
                    Iterator btcIterator = btcSet.iterator();

                    Set<Map.Entry<String, Double>> ethSet = response.body().getEth().entrySet();

                    while (btcIterator.hasNext()) {
                        Map.Entry<String, Double> btcEntry = (Map.Entry) btcIterator.next();
                        String key = btcEntry.getKey();
                        Double btcValue = btcEntry.getValue();
                        if(userChoices.contains(key)) {
                            Iterator ethIterator = ethSet.iterator();
                            while (ethIterator.hasNext()) {
                                Map.Entry<String, Double> ethEntry = (Map.Entry) ethIterator.next();
                                if(key.equals(ethEntry.getKey())) {
                                    double ethValue = ethEntry.getValue();
                                    Currency currency = new Currency(key, key, key, btcValue, ethValue);
                                    currencyList.add(currency);
                                }
                            }
                        }
                    }

                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    currencyRecyclerView.setLayoutManager(manager);
                    currencyRecyclerView.setHasFixedSize(true);
                    adapter = new CurrencyAdapter(currencyList, MainActivity.this);
                    currencyRecyclerView.setAdapter(adapter);

                    hideErrors();

                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    showErrors(getString(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showErrors(t.getMessage());
            }
        });
    }

    private void showErrors(String message) {
        errorMessageTextView.setText(message);
        errorMessageTextView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void hideErrors() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(int itemClickedIndex) {
        Currency currency = currencyList.get(itemClickedIndex);
        Intent intent = new Intent(this, ConversionActivity.class);
        intent.putExtra(DATA_CURRENCY, currency);
        startActivity(intent);
    }

    public void startAddCurrencyActivity() {
        Intent intent = new Intent(this, AddCurrencyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        getUserChoices();
        getCurrencies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager
                .getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
