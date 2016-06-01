package com.seckill.enums;

/**
 * Created by hgf on 16-5-22.
 */
public enum SecKillExecutionStatus {
    SUCCESS(1,"秒杀成功"),
    END(-1,"秒杀结束"),
    REPEAT(-2,"重复秒杀"),
    DATE_REWRITE(-3,"数据篡改"),
    INNER_ERROR(-4,"系统错误");


    private int status;
    private String statusInfo;

    SecKillExecutionStatus(int status, String statusInfo) {
        this.status = status;
        this.statusInfo = statusInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public static SecKillExecutionStatus statusOf(int index){
        for(SecKillExecutionStatus status: values()){
            if(status.getStatus() == index){
                return status;
            }
        }

        return null;
    }
}
