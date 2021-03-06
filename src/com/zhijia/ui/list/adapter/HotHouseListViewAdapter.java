package com.zhijia.ui.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhijia.service.data.Medol.HouseListItemModel;
import com.zhijia.ui.R;
import com.zhijia.ui.zhijiaActivity.NewHouseDetailsActivity;

import java.util.List;

/**
 * 热点楼盘的Adapter
 */
public class HotHouseListViewAdapter extends AbstractHouseListViewAdapter {

    public HotHouseListViewAdapter(Context context, List<HouseListItemModel> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (this.items != null && !this.items.isEmpty()) {

            if (convertView == null) {
                convertView = this.getLayoutInflater().inflate(R.layout.adapter_house_common_list_item, null);
            }
            final HouseListItemModel houseHot = this.items.get(position);

            if (houseHot.getErrorDefault() == null) {
                final ImageView imageView = (ImageView) convertView.findViewById(R.id.adapter_house_image_one);

                setImage(houseHot, imageView);

                TextView textViewOne = (TextView) convertView.findViewById(R.id.adapter_house_content_one);
                textViewOne.setText(houseHot.getHouseContentOne());

                TextView textViewTwo = (TextView) convertView.findViewById(R.id.adapter_house_content_two);
                textViewTwo.setText(houseHot.getHouseContentTwo());

                TextView textViewThree = (TextView) convertView.findViewById(R.id.adapter_house_content_three);
                textViewThree.setText(houseHot.getHouseContentThree());
                textViewThree.setTextColor(convertView.getResources().getColor(R.color.font_desc));

                TextView textViewFour = (TextView) convertView.findViewById(R.id.adapter_house_content_four);
                textViewFour.setText(houseHot.getHouseContentFour());
                textViewFour.setTextColor(convertView.getResources().getColor(R.color.font_desc));

                TextView textViewFive = (TextView) convertView.findViewById(R.id.adapter_house_content_five);
                if (houseHot.getHouseContentFive() != null && !houseHot.getHouseContentFive().isEmpty()) {
                    textViewFive.setText(houseHot.getHouseContentFive());
                    textViewFive.setTextColor(convertView.getResources().getColor(R.color.red));
                } else {
                    textViewFive.setVisibility(View.INVISIBLE);
                }
                TextView textViewAction = (TextView) convertView.findViewById(R.id.item_action);
                textViewAction.setVisibility(View.GONE);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newHouseDetails = new Intent(view.getContext(), NewHouseDetailsActivity.class);
                        newHouseDetails.putExtra("hid", houseHot.getHid());
                        view.getContext().startActivity(newHouseDetails);
                    }
                });
            } else {
                return setErrorValue(houseHot);
            }


        } else {
            convertView = this.getLayoutInflater().inflate(R.layout.house_list_wait_load, null);
        }
        return convertView;
    }
}