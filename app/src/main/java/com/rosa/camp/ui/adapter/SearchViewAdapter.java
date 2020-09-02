package com.rosa.camp.ui.adapter;

import android.text.TextUtils;
import android.util.Log;
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

import ir.map.servicesdk.model.inner.SearchItem;
import ir.map.servicesdk.response.SearchResponse;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {

    private List<SearchItem> mItems;

    static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private SearchItem mItem;

        SearchViewHolder(View v) {
            super(v);

            mNameTextView = v.findViewById(R.id.search_view_list_item_name);
            v.setOnClickListener(this);
        }

        void bindData(@NonNull SearchItem item) {

                mItem = item;

                if (TextUtils.isEmpty(item.getAddress())) {
                    mNameTextView.setText("");
                } else {
                    mNameTextView.setText(item.getAddress());
                }

        }


        @Override
        public void onClick(View v) {
            HomeActivity homeActivity = (HomeActivity) v.getContext();
            Fragment fragment = homeActivity.getSupportFragmentManager().findFragmentById(R.id.content);
            if (fragment instanceof MapFragment) {
                MapFragment fgf = (MapFragment)fragment;

                fgf.showItemOnMap(mItem);
            }
            else {
                Log.d("frag","no fragment");
            }
        }
    }

    public SearchViewAdapter(List<SearchItem> items) {
        mItems = items;
    }

    @NonNull
    @Override
    public SearchViewAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_autocomplete_item, parent, false);
        return new SearchViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewAdapter.SearchViewHolder holder, int position) {
        SearchItem item = mItems.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
