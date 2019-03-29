package com.theone.using.activity.message;

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
public class MessageItemAdpter extends ArrayAdapter<MessageItem> {
    private int resourceId;

    public MessageItemAdpter(Context context, int resource, List<MessageItem> listObjects) {
        super(context, resource, listObjects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageItem item = getItem(position); // 获取当前项
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.messageHead = (TextView) view.findViewById(R.id.message_head);
            viewHolder.messageContent = (TextView) view.findViewById(R.id.message_content);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.messageHead.setText(item.getHistoryType());
        viewHolder.messageContent.setText(item.getHistoryContent());
        return view;
    }

    class ViewHolder {
        TextView messageHead;
        TextView messageContent;
    }
}