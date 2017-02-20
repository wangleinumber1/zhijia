package com.zhijia.service.data.Medol;

import java.util.List;

/**
 * 特惠信息模型
 */
public class PrivilegeModel {

    private String privilege;

    private String time;

    private List<Youhui> youhui;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Youhui> getYouhui() {
        return youhui;
    }

    public void setYouhui(List<Youhui> youhui) {
        this.youhui = youhui;
    }

    public static class Youhui {
        private String pid;
        private String info;
        private String amount;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
