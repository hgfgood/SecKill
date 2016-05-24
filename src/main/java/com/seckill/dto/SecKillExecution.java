package com.seckill.dto;

import com.seckill.entry.SecKill;
import com.seckill.enums.SecKillExecutionStatus;

/**
 * 秒杀操作结果
 *
 * Created by hgf on 16-5-22.
 */
public class SecKillExecution {

    private long itemId;

    /**
     * 秒杀状态
     */
    private int status;

    /**
     * 秒杀描述信息
     */
    private String secKillInfo;

    /**
     * 秒杀结果对象
     */
    private SecKill secKill;


    /**
     * 秒杀成功
     * @param itemId
     * @param statusenum
     * @param secKill
     */
    public SecKillExecution(long itemId, SecKillExecutionStatus statusenum, SecKill secKill) {
        this.itemId = itemId;
        this.status = statusenum.getStatus();
        this.secKillInfo = statusenum.getStatusInfo();
        this.secKill = secKill;
    }


    /**
     * 秒杀失败
     * @param itemId
     * @param statusenum
     */
    public SecKillExecution(long itemId, SecKillExecutionStatus statusenum) {
        this.itemId = itemId;
        this.status = statusenum.getStatus();
        this.secKillInfo = statusenum.getStatusInfo();
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSecKillInfo() {
        return secKillInfo;
    }

    public void setSecKillInfo(String secKillInfo) {
        this.secKillInfo = secKillInfo;
    }

    public SecKill getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKill secKill) {
        this.secKill = secKill;
    }

    @Override
    public String toString() {
        return "SecKillExecution{" +
                "itemId=" + itemId +
                ", status=" + status +
                ", secKillInfo='" + secKillInfo + '\'' +
                ", secKill=" + secKill +
                '}';
    }
}
