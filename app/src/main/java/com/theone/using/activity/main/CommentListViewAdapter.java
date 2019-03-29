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
public class CommentListViewAdapter extends ArrayAdapter<CommentItem> {
    private int resourceId;

    public CommentListViewAdapter(Context context, int resource, List<CommentItem> listObjects) {
        super(context, resource, listObjects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItem item = getItem(position); // 获取当前项
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.username = (TextView) view.findViewById(R.id.tx_comment_user);
            viewHolder.commentContent= (TextView) view.findViewById(R.id.tx_comment_content);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.username.setText(item.getUsername());
        viewHolder.commentContent.setText(item.getCommentContent());
        return view;
    }

    class ViewHolder {
        TextView username;
        TextView commentContent;
    }
}
