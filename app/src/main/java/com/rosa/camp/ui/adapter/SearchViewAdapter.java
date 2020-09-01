package com.rosa.camp.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rosa.camp.R;
import com.rosa.camp.view.HomeActivity;
import com.rosa.camp.view.MapFragment;

import java.util.List;

import ir.map.servicesdk.response.SearchResponse;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {

    private SearchResponse mItems;

    static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private SearchResponse mItem;

        SearchViewHolder(View v) {
            super(v);

            mNameTextView = v.findViewById(R.id.search_view_list_item_name);
            v.setOnClickListener(this);
        }

        void bindData(@NonNull SearchResponse item) {

            for (int i = 0; i < item.getCount(); i++) {

                mItem = item;

                if (TextUtils.isEmpty(item.getSearchItems().get(i).getAddress())) {
                    mNameTextView.setText("");
                } else {
                    mNameTextView.setText(item.getSearchItems().get(i).getAddress());
                }
            }


        }


        @Override
        public void onClick(View v) {
            HomeActivity homeActivity = (HomeActivity) v.getContext();
            Fragment fragment = homeActivity.getSupportFragmentManager().findFragmentById(R.id.content);
            if (fragment instanceof MapFragment) {
                MapFragment fgf = (MapFragment)fragment;
                fgf.showItemOnMap();
            }
        }
    }

    public SearchViewAdapter(SearchResponse items) {
        mItems = items;
    }

    @NonNull
    @Override
    public SearchViewAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_map, parent, false);
        return new SearchViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewAdapter.SearchViewHolder holder, int position) {
        SearchResponse item = mItems.getSearchItems().get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return mItems.getCount();
    }
}
