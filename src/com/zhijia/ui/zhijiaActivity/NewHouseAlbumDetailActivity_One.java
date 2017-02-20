package com.zhijia.ui.zhijiaActivity;

import java.util.List;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.PictureJsonModel;
import com.zhijia.ui.R;

public class NewHouseAlbumDetailActivity_One extends BaseDetailsActivity{
	private final String PICTURE_URL = Global.API_WEB_URL + "/xinfang/picture";
    private final int header_title = R.id.details_title;
    private List<View> contentViews;
    private ViewPager contentViewPager;
    private String pid;
    private String hid;
    private int position;
    private String catname;
    //图片title的名字
    private String name;
    private String total;
    private List<PictureJsonModel> pictureJsonModelList;

}
