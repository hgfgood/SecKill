package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SecKillExecution;
import com.seckill.entry.SecItem;
import com.seckill.exception.ClosedSecKillException;
import com.seckill.exception.RepeatSecKillException;
import com.seckill.exception.SecKillException;

import java.util.List;

/**
 * 站在使用者的角度上设计接口
 * Created by hgf on 16-5-22.
 */
public interface SecKillService {

    /**
     * 获取所有秒杀商品的列表
     * @return 秒杀商品列表
     */
    public List<SecItem> getSecItems();

    /**
     * 通过商品id查看秒杀商品详细信息
     * @param itemId 商品id
     * @return 商品详细信息
     */
    public SecItem getSecItemById(long itemId);

    /**
     * 暴露秒杀接口给用户
     *  当前时间在秒杀规定的时间范围，返回秒杀地址
     *  当前时间不是秒杀时间，返回系统时间和秒杀时间
     *
     * @param itemId 商品id
     */
    public Exposer exposeSecKillUrl(long itemId);

    /**
     * 执行秒杀
     *
     * @param itemId 秒杀商品id
     * @param userPhone 用户手机号
     * @param md5 暴露秒杀地址的md5
     * @return
     * @throws SecKillException 秒杀异常
     * @throws RepeatSecKillException 重复秒杀异常
     * @throws ClosedSecKillException 关闭秒杀异常
     */
    public SecKillExecution executeSecKill(long itemId, long userPhone, String md5) throws SecKillException, RepeatSecKillException, ClosedSecKillException;

}
