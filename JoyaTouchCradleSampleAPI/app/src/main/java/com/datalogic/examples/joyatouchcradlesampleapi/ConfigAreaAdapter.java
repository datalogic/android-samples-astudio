// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class ConfigAreaAdapter extends BaseAdapter
{
	private Context context;
	
	private byte[] values;
	
	public ConfigAreaAdapter(Context context, byte[] values)
	{
		this.context = context;
		this.values = values;
	}
	
	@Override
	public int getCount()
	{
		return values.length;
	}

	@Override
	public Object getItem(int position)
	{
		return values[position];
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		
		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.config_area_grid_cell, parent, false);
			holder = new ViewHolder();
			holder.editText = (EditText)convertView.findViewById(R.id.editTextConfigAreaGridCell);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		if (holder.textWatcher != null)
			holder.editText.removeTextChangedListener(holder.textWatcher);
		
        holder.editText.setText("" + (values[position] & 0xFF));
        holder.textWatcher = new TextWatcherCell(position);
        holder.editText.addTextChangedListener(holder.textWatcher);
        return convertView;
	}

	public static class ViewHolder
	{
		public EditText editText;
		public TextWatcherCell textWatcher;
	}
	
	public class TextWatcherCell implements TextWatcher
	{
		private int position;
		
		public TextWatcherCell(int position)
		{
			this.position = position;
		}
		
		@Override
		public void afterTextChanged(Editable e)
		{
			try
			{
				int tmp = Integer.parseInt(e.toString());
				values[position] = (byte)tmp;
			}
			catch (NumberFormatException ex)
			{

			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start,
				int count, int after)
		{
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count)
		{
			
		}
	}
}
