package com.seckill.dao;

import com.seckill.entry.SecKill;

/**
 * 秒杀记录数据访问层
 *
 * Created by hgf on 16-5-21.
 */
public interface SecKillDaoInterface {
    /**
     * 插入秒杀记录
     * @param itemId 秒杀的商品id
     * @return 插入成功的行数，0表示没有插入成功
     */
    public int insertSuccessKill(long itemId);

    /**
     * 根据商品id获取秒杀详情
     * @param itemId 商品id
     * @return 秒杀信息
     */
    public SecKill getSecKillById(long itemId);

}
