package com.zhijia.service.data.Medol;

/**
 * 鐧诲綍鐢ㄦ埛淇℃伅
 */
public class UserLoginInfoModel {

    private String username;

    //娑堟伅鏁� 
    private Integer count;

    //澶村儚鍦板潃url
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
