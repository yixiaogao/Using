package com.theone.using.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theone.using.R;

public class TitleLayout extends LinearLayout {

	private ImageView backiv;
	private TextView titletx;

	public TitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.main_title, this);
		backiv = (ImageView) findViewById(R.id.title_iv);
		titletx= (TextView) findViewById(R.id.title_tx);
	}

	public TextView getTitletx() {
		if(titletx !=null){
			return titletx;
		}
		return null;
	}

	public ImageView getBackView(){
		if(backiv !=null){
			return backiv;
		}
		return null;
	}
}
