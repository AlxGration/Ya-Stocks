package com.alex.yastocks.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.yastocks.R;

import java.util.ArrayList;

public class StocksListRecyclerAdapter extends RecyclerView.Adapter<StocksListRecyclerAdapter.ViewHolder> {

    ArrayList<Stock> data;

    public StocksListRecyclerAdapter( ArrayList<Stock> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stock, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your _data_ at this position and replace the
        // contents of the view with that element
        Stock stock = data.get(position);

        // покрас списка в виде "зебры"
        if (position % 2 == 0){
            holder.setGreyBG();
        }else{
            holder.setWhiteBG();
        }

        holder.tvTicker.setText(stock.getTicker());
        holder.tvCompanyName.setText(stock.getCompanyName());
        holder.imgStar.setImageResource(stock.isSelected() ? R.drawable.ic_star_selected: R.drawable.ic_star_unselected);
        holder.imgStock.setImageResource(stock.getStockImg());


        holder.tvPrice.setText(CurrencyFormatter.format(stock.getCurrency(), stock.getPrice()));
        // покраска изменения цены
        if (stock.getPriceChange() > 0)
            holder.setGreenChange();
        else if(stock.getPriceChange() < 0)
            holder.setRedChange();
        else holder.setBlackChange();
        //


        holder.tvPriceChange.setText(
                CurrencyFormatter.formatWithPercent(
                        stock.getCurrency(),
                        stock.getPriceChange(),
                        stock.getPriceChangePercent())
        );

        
        holder.itemView.setTag(position);
        // i don't know, is it ok?
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Stock stock = data.get(((int)view.getTag()));
                    listener.startInfoActivityWith(stock.getTicker(), stock.getCompanyName(), stock.isSelected());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static IonItemClickListener listener;
    public static void setOnItemClickListener(IonItemClickListener lis){
        listener = lis;
    }
    public interface IonItemClickListener{
        void startInfoActivityWith(String ticker, String companyName, boolean isSelected);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTicker;
        private final TextView tvCompanyName;
        private final TextView tvPrice;
        private final TextView tvPriceChange;
        private final ImageView imgStar;
        private final ImageView imgStock;

        private final View view;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            this.view = view;

            tvTicker = view.findViewById(R.id.tv_ticker);
            tvCompanyName = view.findViewById(R.id.tv_company_name);
            tvPrice = view.findViewById(R.id.tv_price);
            tvPriceChange = view.findViewById(R.id.tv_price_change);
            imgStar = view.findViewById(R.id.img_star);
            imgStock = view.findViewById(R.id.img_stock);
        }
        private void setGreyBG() {
            if (view == null) return;
            view.setBackgroundResource(R.drawable.bg_stock_item_grey);
        }
        private void setWhiteBG() {
            if (view == null) return;
            view.setBackgroundResource(R.drawable.bg_stock_item_white);
        }

        private void setGreenChange(){
            tvPriceChange.setTextColor(view.getResources().getColor(R.color.green));
        }
        private void setRedChange(){
            tvPriceChange.setTextColor(view.getResources().getColor(R.color.red));
        }
        private void setBlackChange(){
            tvPriceChange.setTextColor(view.getResources().getColor(R.color.black));
        }
    }
}
