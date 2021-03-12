package com.alex.yastocks.ui.stock.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alex.yastocks.R;
import com.alex.yastocks.models.ChartInterval;
import com.alex.yastocks.ui.stock.InfoActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChartFragment extends Fragment {

    private ChartViewModel viewModel;
    private String ticker;
    private LineChart chart;

    public ChartFragment(String ticker){
        this.ticker = ticker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChartViewModel.class);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chart, container, false);


        TextView tvPrice = root.findViewById(R.id.tv_price);
        TextView tvPriceChange = root.findViewById(R.id.tv_price_change);
        ProgressBar pBar = root.findViewById(R.id.progress_bar);
        Button btnBuy = root.findViewById(R.id.btn_buy);

        chart = root.findViewById(R.id.line_chart);
        chartConfig();

        viewModel.getIsLoadingMutableLiveData().observe(this, isLoading ->{
            if (isLoading)
                pBar.setVisibility(View.VISIBLE);
            else
                pBar.setVisibility(View.GONE);
        });

        viewModel.getErrorMessageMutableLiveData().observe(this, msg -> {
            ((InfoActivity) getActivity()).showError(msg);
        });

        viewModel.getPriceChangeMutableLiveData().observe(this, priceChange->{
            tvPriceChange.setText(priceChange);
            if (priceChange.charAt(0)=='+') tvPriceChange.setTextColor(getResources().getColor(R.color.green));
            else if(priceChange.charAt(0)=='-') tvPriceChange.setTextColor(getResources().getColor(R.color.red));
        });
        viewModel.getPriceMutableLiveData().observe(this, price->{
            tvPrice.setText(price);
            btnBuy.setText(getString(R.string.buy_for)+price);
        });

        viewModel.getChartDataMutableLiveData().observe(this, dataSet -> {
            dataSet.setFillDrawable(getResources().getDrawable(R.drawable.fade_black));

            LineData lineData = new LineData(dataSet);

            chart.setData(lineData);
            chart.invalidate(); // refresh chart
        });

        RadioGroup rgInterval = root.findViewById(R.id.rg_interval);
        rgInterval.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id){
                case R.id.rb_day:
                    viewModel.requestChart(ticker, ChartInterval.DAY);
                    break;
                case R.id.rb_week:
                    viewModel.requestChart(ticker, ChartInterval.WEEK);
                    break;
                case R.id.rb_month:
                    viewModel.requestChart(ticker, ChartInterval.MONTH);
                    break;
                case R.id.rb_year:
                    viewModel.requestChart(ticker, ChartInterval.YEAR);
                    break;
            }
        });
        viewModel.requestChart(ticker, ChartInterval.DAY);

        return root;
    }

    private void chartConfig(){
        chart.setTouchEnabled(true);
        chart.setPinchZoom(false);
        XAxis xAxis = chart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }
}