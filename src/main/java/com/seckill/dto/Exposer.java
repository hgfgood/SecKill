package com.seckill.dto;

/**
 * 秒杀暴露地址DTO数据传输层
 *
 * Created by hgf on 16-5-22.
 */
public class Exposer {

    /**
     * 是否开启秒杀
     */
    private boolean canExpose;

    /**
     * 秒杀地址的md5值，防止被用户猜到秒杀地址
     */
    private String md5;

    /**
     * 秒杀商品id
     */
    private long secItemId;

    /**
     * 服务器当前时间（单位毫秒）
     */
    private long now;

    /**
     * 秒杀开始时间
     */
    private long start;

    /**
     * 秒杀结束时间
     */
    private long end;


    public Exposer(boolean canExpose, String md5, long secItemId) {
        this.canExpose = canExpose;
        this.md5 = md5;
        this.secItemId = secItemId;
    }

    public Exposer(boolean canExpose, long secItemId, long now, long start, long end) {
        this.canExpose = canExpose;
        this.secItemId = secItemId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean canExpose, long now, long start, long end) {
        this.canExpose = canExpose;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean canExpose, long secItemId) {
        this.canExpose = canExpose;
        this.secItemId = secItemId;
    }

    public boolean isCanExpose() {
        return canExpose;
    }

    public void setCanExpose(boolean canExpose) {
        this.canExpose = canExpose;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSecItemId() {
        return secItemId;
    }

    public void setSecItemId(long secItemId) {
        this.secItemId = secItemId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "canExpose=" + canExpose +
                ", md5='" + md5 + '\'' +
                ", secItemId=" + secItemId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
