package com.zhijia.ui.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhijia.service.data.Medol.HouseListItemModel;
import com.zhijia.ui.R;
import com.zhijia.util.DownloadImageTask;

import java.util.List;

/**
 * 新房列表中的限期特惠、热点楼盘、近期开盘列表条目自定义Adapter
 */
abstract public class AbstractHouseListViewAdapter extends BaseAdapter {

    protected List<HouseListItemModel> items;

    protected LayoutInflater layoutInflater;

    public AbstractHouseListViewAdapter(Context context, List<HouseListItemModel> items) {
        this.items = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (this.items != null && !this.items.isEmpty()) {
            return items.size();
        } else {
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if (items != null && !this.items.isEmpty()) {
            return items.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void addItem(HouseListItemModel model) {
        this.items.add(model);
    }


    public View setErrorValue(HouseListItemModel itemModel) {
        View convertView = getLayoutInflater().inflate(R.layout.house_list_wait_load, null);
        View imageView = convertView.findViewById(R.id.house_list_wait_load_image);
        imageView.setVisibility(View.GONE);
        TextView textView = (TextView) convertView.findViewById(R.id.house_list_wait_load_content);
        textView.setText(itemModel.getErrorDefault());
        textView.setTextSize(16);
        textView.setTextColor(R.color.black);
        return convertView;
    }

    public void setImage(HouseListItemModel itemModel, final ImageView imageView) {
        if (itemModel.getImageURL() != null) {
            new DownloadImageTask().doInBackground(itemModel.getImageURL(), imageView, R.drawable.house);
        } else {
            imageView.setImageDrawable(itemModel.getHouseImage());
        }
    }

    public List<HouseListItemModel> getItems() {
        return items;
    }
}
