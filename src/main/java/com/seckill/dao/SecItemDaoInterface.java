package com.seckill.dao;

import com.seckill.entry.SecItem;

import java.util.Date;
import java.util.List;

/**
 * 秒杀商品数据访问层
 *
 * Created by hgf on 16-5-21.
 */
public interface SecItemDaoInterface {

    /**
     * 秒杀成功后，商品数量减。
     * @param itemId 商品Id
     * @param killTime 秒杀时间
     * @return 返回 0 表示没有更行成功，大于0表示更新成功。
     */
    public int reduceNumber(long itemId, Date killTime);

    /**
     * 通过item id查询商品信息
     * @param itemId
     * @return
     */
    public SecItem queryItemById(long itemId);

    /**
     * 分页获取秒杀商品列表
     * @param offset 偏移量
     * @param limit 查询数量
     * @return 查询的描述商品集合
     */
    public List<SecItem> queryAllItems(int offset, int limit);
}
