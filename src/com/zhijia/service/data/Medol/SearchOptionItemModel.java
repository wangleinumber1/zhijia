package com.zhijia.service.data.Medol;

/**
 * UI上的搜索选项类型行模型
 */
public class SearchOptionItemModel {

    //图片
    private int drawableId;

    //类型名称，用于显示
    private String typeName;

    //类型值，传递给后台
    private String typeValue;

    //选项名称，用于显示
    private String optionName;

    //选项值，传递给后台
    private String optionValue;

    public SearchOptionItemModel(int drawableId, String typeName, String typeValue, String optionName, String optionValue) {
        this.drawableId = drawableId;
        this.typeName = typeName;
        this.typeValue = typeValue;
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    public SearchOptionItemModel() {
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
}
