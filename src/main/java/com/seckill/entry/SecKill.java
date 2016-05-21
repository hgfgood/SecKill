package com.seckill.entry;

import java.util.Date;

/**
 * Created by hgf on 16-5-21.
 */
public class SecKill {
    private long itemId;
    private long phone;

    /**
     * status=-1    秒杀失败
     * status=0     秒杀成功
     * status=1     付款
     */
    private short status;
    private Date kiilTime;
    private SecItem item;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getKiilTime() {
        return kiilTime;
    }

    public void setKiilTime(Date kiilTime) {
        this.kiilTime = kiilTime;
    }

    public SecItem getItem() {
        return item;
    }

    public void setItem(SecItem item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Seckill{" +
                "itemId=" + itemId +
                ", phone=" + phone +
                ", state=" + status +
                ", kiilTime=" + kiilTime +
                '}';
    }
}
