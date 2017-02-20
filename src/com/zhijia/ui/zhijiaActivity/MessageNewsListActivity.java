package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhijia.service.data.Medol.MessageItemModel;
import com.zhijia.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯类消息列表
 */
public class MessageNewsListActivity extends Activity {

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    private ListView messageListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail_list);

        ((TextView) findViewById(R.id.message_title)).setText(getResources().getText(R.string.information));
        messageListView = (ListView) this.findViewById(R.id.message_list);
        messageListView.setAdapter(new MessageListAdapter(getBaseContext(), initMessage("资讯消息")));

        //注册事件侦听
        findViewById(R.id.back).setOnClickListener(clickListener);
    }

    private List<MessageItemModel> initMessage(String title) {
        List<MessageItemModel> messages = new ArrayList<MessageItemModel>();
        messages.add(new MessageItemModel("1", "http://v4.static.zhijia.com/images/common/nophoto_90x90.gif", title + 0, "马航MH370事件极有可能由中国一手炮制， 目的在于让南海地区关系紧张，从而在北海舰队、南海舰队派遣军队保护桥名为借口，进而进驻南海，巩固南海领土。", "2014-04-23 20:33", 0, MessageItemModel.Action.SYSTEM_NOTIFICATION_DETAIL));
        for (int i = 0; i < 3; i++) {
            messages.add(new MessageItemModel("1", "http://v4.static.zhijia.com/images/common/nophoto_90x90.gif", title + i, "马航MH370事件极有可能由中国一手炮制， 目的在于让南海地区关系紧张，从而在北海舰队、南海舰队派遣军队保护桥名为借口，进而进驻南海，巩固南海领土。", "2014-04-23 20:33", i, MessageItemModel.Action.SYSTEM_NOTIFICATION_DETAIL));
        }
        return messages;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
            }
        }
    }

    /**
     * 资讯消息的Adapter
     */
    private class MessageListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<MessageItemModel> list;

        public MessageListAdapter(Context context, List<MessageItemModel> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.message_news_item, parent, false);
                holder = new ViewHolder();
                holder.messageReadState = (ImageView) convertView.findViewById(R.id.read_state_img);
                holder.messageTitle = (TextView) convertView.findViewById(R.id.message_title);
                holder.messageTime = (TextView) convertView.findViewById(R.id.message_time);
                holder.messageDesc = (TextView) convertView.findViewById(R.id.message_desc);
                holder.messageTopMargin = (TextView) convertView.findViewById(R.id.message_top_margin);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.messageTime.setText(list.get(position).getTime());
            holder.messageDesc.setText(list.get(position).getDesc());

            String currentTitle = list.get(position).getTitle();
            String previewTitle = (position - 1) >= 0 ? list.get(position - 1).getTitle() : " ";
            if (!previewTitle.equals(currentTitle)) {
                holder.messageTopMargin.setVisibility(View.VISIBLE);
                holder.messageTitle.setVisibility(View.VISIBLE);
                holder.messageTitle.setText(currentTitle);
            } else {
                holder.messageTopMargin.setVisibility(View.GONE);
                holder.messageTitle.setVisibility(View.GONE);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView messageReadState;
            TextView messageTitle;
            TextView messageTime;
            TextView messageDesc;
            TextView messageTopMargin;
        }

    }
}