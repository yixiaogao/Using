package com.theone.using.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theone.using.R;
import com.theone.using.activity.history.HistoryItem;

import java.util.List;

/**
 * Created by liuyuan on 2016/7/21.
 */
public class SearchItemAdpter extends ArrayAdapter<SearchItem> {
    private int resourceId;

    public SearchItemAdpter(Context context, int resource, List<SearchItem> listObjects) {
        super(context, resource, listObjects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchItem item = getItem(position); // 获取当前项
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.searchItemImg = (ImageView) view.findViewById(R.id.iv_search_head);
            viewHolder.searchPlaceNametx = (TextView) view.findViewById(R.id.tx_search_name);
            viewHolder.searchAddresstx = (TextView) view.findViewById(R.id.tx_search_address);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.searchItemImg.setImageResource(item.getSearchItemImgId());
        viewHolder.searchPlaceNametx.setText(item.getSearchPlaceName());
        viewHolder.searchAddresstx.setText(item.getSearchAddress());
        return view;
    }

    class ViewHolder {
        ImageView searchItemImg;
        TextView searchPlaceNametx;
        TextView searchAddresstx;
    }
}