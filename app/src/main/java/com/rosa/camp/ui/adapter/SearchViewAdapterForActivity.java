package com.rosa.camp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rosa.camp.R;
import com.rosa.camp.view.AddressActivity;
import com.rosa.camp.view.DirectionFragment;
import com.rosa.camp.view.MapFragment;

import java.util.ArrayList;
import java.util.List;

import ir.map.servicesdk.model.inner.SearchItem;

public class SearchViewAdapterForActivity  extends RecyclerView.Adapter<SearchViewAdapterForActivity.SearchViewHolderForActivity> {


        private List<SearchItem> mItems;


        static class SearchViewHolderForActivity extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mNameTextView;
            private TextView mTypeTextView;
            private TextView mCityTextView;
            private TextView mDistrictTextView;
            private SearchItem mItem;


            SearchViewHolderForActivity(View v) {
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

                Context context =  v.getContext();

                Activity host=(Activity) v.getContext();
                Log.d("activity",host.toString());
                if (host instanceof AddressActivity) {
                    AddressActivity fgf = (AddressActivity) host;

                    fgf.showItemOnMap(mItem);
                }
                else {

                    Log.d("activity","no activity");
                }
            }
        }

        public SearchViewAdapterForActivity(List<SearchItem> items) {
            mItems = items;
        }


        @NonNull
        @Override
        public SearchViewAdapterForActivity.SearchViewHolderForActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_autocomplete_item, parent, false);
            return new SearchViewHolderForActivity(inflatedView);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolderForActivity holder, int position) {
            SearchItem item = mItems.get(position);
            holder.bindData(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }


