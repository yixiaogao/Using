package com.theone.using.activity.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theone.using.R;

import java.util.List;


/**
 * Created by liuyuan on 2016/7/12.
 */
public class ListViewAdapter extends ArrayAdapter<LeftListItem> {
    private int resourceId;

    public ListViewAdapter(Context context, int resource, List<LeftListItem> listObjects) {
        super(context, resource, listObjects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftListItem item = getItem(position); // 获取当前项
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.itemtx = (TextView) view.findViewById(R.id.tx_main_list_item);
            viewHolder.itemimg= (ImageView) view.findViewById(R.id.img_main_list_item);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.itemtx.setText(item.getItemtx());
        viewHolder.itemimg.setImageResource(item.getItemimgId());
        return view;
    }

    class ViewHolder {
        ImageView itemimg;
        TextView itemtx;
    }
}
