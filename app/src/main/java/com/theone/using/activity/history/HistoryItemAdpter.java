package com.theone.using.activity.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.theone.using.R;
import java.util.List;

/**
 * Created by liuyuan on 2016/7/21.
 */
public class HistoryItemAdpter extends ArrayAdapter<HistoryItem> {
    private int resourceId;

    public HistoryItemAdpter(Context context, int resource, List<HistoryItem> listObjects) {
        super(context, resource, listObjects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryItem item = getItem(position); // 获取当前项
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.histroyType = (TextView) view.findViewById(R.id.history_type);
            viewHolder.historyContent = (TextView) view.findViewById(R.id.history_content);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.histroyType.setText(item.getHistoryType());
        viewHolder.historyContent.setText(item.getHistoryContent());
        return view;
    }

    class ViewHolder {
        TextView histroyType;
        TextView historyContent;
    }
}