package com.example.greencity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {

    private List<RewardModel> rewardModelList;
    private boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList,boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (useMiniLayout){
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout,parent,false);

        }else {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout,parent,false);

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String title = rewardModelList.get(position).getTitle();
        String date = rewardModelList.get(position).getExpiryDate();
        String body = rewardModelList.get(position).getCoupenBody();
        holder.setData(title,date,body);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title_reward);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }
        private void setData(String title,String date,String body){
            coupenTitle.setText(title);
            coupenExpiryDate.setText(date);
            coupenBody.setText(body);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductDetails.coupenTitle.setText(title);
                        ProductDetails.coupenExpiryDate.setText(date);
                        ProductDetails.coupenBody.setText(body);
                        ProductDetails.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
