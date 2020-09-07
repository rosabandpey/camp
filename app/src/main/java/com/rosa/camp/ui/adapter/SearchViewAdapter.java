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
import com.rosa.camp.view.DirectionFragment;
import com.rosa.camp.view.HomeActivity;
import com.rosa.camp.view.LoginFragment;
import com.rosa.camp.view.MapFragment;

import java.util.ArrayList;
import java.util.List;

import ir.map.servicesdk.model.inner.SearchItem;
import ir.map.servicesdk.response.SearchResponse;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> {

    private List<SearchItem> mItems;



    static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mTypeTextView;
        private TextView mCityTextView;
        private TextView mDistrictTextView;
        private SearchItem mItem;


        SearchViewHolder(View v) {
            super(v);

            mNameTextView = v.findViewById(R.id.search_view_list_item_name);
           // mTypeTextView = v.findViewById(R.id.search_view_list_item_type);
            mCityTextView = v.findViewById(R.id.search_view_list_city);
            mDistrictTextView = v.findViewById(R.id.search_view_list_item_district);
            v.setOnClickListener(this);
        }

        void bindData(@NonNull SearchItem item) {

                mItem = item;

                if (TextUtils.isEmpty(item.getAddress())) {
                    mNameTextView.setText("");
                } else {
                    mNameTextView.setText(item.getAddress());
                }





            String city = null;
            if (item.getCity() != null) {
                city = item.getCity();
                if (TextUtils.isEmpty(city)) {
                    mCityTextView.setText("");
                    mCityTextView.setVisibility(View.GONE);
                } else {
                    mCityTextView.setText(city);
                    mCityTextView.setVisibility(View.VISIBLE);
                }
            } else {
                mCityTextView.setText("");
                mCityTextView.setVisibility(View.GONE);
            }



            StringBuilder district = null;
            if (item.getDistrict() != null && item.getDistrict() != null) {

                    String l = item.getDistrict();
                    if (district == null) {
                        district = new StringBuilder(l);
                    } else {
                        district.append("، ").append(l);
                    }
                }
                if (TextUtils.isEmpty(district != null ? district.toString() : null)) {
                mDistrictTextView.setText("");
                    mDistrictTextView.setVisibility(View.GONE);
            } else {
                    mDistrictTextView.setText(district.toString());
                    mDistrictTextView.setVisibility(View.VISIBLE);
                if (mCityTextView.getText().length() != 0) {
                    String result = city != null ? city + "،" : "";
                    mCityTextView.setText(result);
                }
            }



        }


        @Override
        public void onClick(View v) {

                HomeActivity homeActivity = (HomeActivity) v.getContext();
                List<Fragment> fragments = homeActivity.getSupportFragmentManager().getFragments();
                ArrayList<Fragment> visibleFragments = new ArrayList<>();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment != null && fragment.isVisible())
                            visibleFragments.add(fragment);
                        if (fragment instanceof MapFragment ) {
                            MapFragment fgf = (MapFragment)fragment;

                            fgf.showItemOnMap(mItem);
                        }
                        else {

                            Log.d("frag","no frag");
                        }

                        if (fragment instanceof DirectionFragment ) {
                            DirectionFragment fgf = (DirectionFragment)fragment;

                            fgf.showItemOnMap(mItem);
                        }
                        else {

                            Log.d("frag","no frag");
                        }
                    }
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
