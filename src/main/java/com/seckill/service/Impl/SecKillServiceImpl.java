package com.seckill.service.Impl;

import com.seckill.dao.SecItemDao;
import com.seckill.dao.SecKillDao;
import com.seckill.dao.cache.SeckItemCache;
import com.seckill.dto.Exposer;
import com.seckill.dto.SecKillExecution;
import com.seckill.entry.SecItem;
import com.seckill.entry.SecKill;
import com.seckill.enums.SecKillExecutionStatus;
import com.seckill.exception.ClosedSecKillException;
import com.seckill.exception.RepeatSecKillException;
import com.seckill.exception.SecKillException;
import com.seckill.service.SecKillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hgf on 16-5-22.
 */
@Service
public class SecKillServiceImpl implements SecKillService {

    private final String salt = "lakjfpuhjpfuij08u2083ejdhw8rj0we8fj0ijde";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private SecItemDao secItemDao;

    @Autowired
    private SeckItemCache seckItemCache;


    public List<SecItem> getSecItems() {
        return secItemDao.queryAllItems(0, 3);
    }

    public SecItem getSecItemById(long itemId) {
        return secItemDao.queryItemById(itemId);
    }

    public Exposer exposeSecKillUrl(long itemId) {
//        SecItem item = secItemDao.queryItemById(itemId);
//
//        if (item == null) {
//            return new Exposer(false, itemId);
//        }


        //从cache中读取
        SecItem item = seckItemCache.getSceItemFromCache(itemId);
        if (item == null) {
            //从数据库读起
            item = secItemDao.queryItemById(itemId);
            if (item == null) {
                return new Exposer(false, itemId);
            } else {
                seckItemCache.putSceItemToCache(item);
            }
        }


        Date start = item.getStartTime();
        Date end = item.getEndTime();
        Date now = new Date();
        if (start.getTime() > now.getTime() || end.getTime() < now.getTime()) {
            return new Exposer(false, itemId, now.getTime(), start.getTime(), end.getTime());
        }

        String md5 = getMD5(itemId);

        return new Exposer(true, md5, itemId);
    }

    /**
     * 获取md5值
     *
     * @param itemId 商品id
     * @return 商品md5值
     */
    private String getMD5(long itemId) {
        String base = itemId + "/" + salt;

        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    @Transactional
    public SecKillExecution executeSecKill(long itemId, long userPhone, String md5) throws SecKillException, RepeatSecKillException, ClosedSecKillException {
//        1. 传回的id不存在;
//        2. 传回的userphone为0;
//        3. 传回的md5与计算的md5不一致
//        4. 已经插入过记录

        /**
         * 优先过滤资源消耗小的
         */
        if (md5 == null || !md5.equals(getMD5(itemId))) {
            throw new SecKillException("seckill data rewrited!");
        }

        try {
//            Date now = new Date();
//            int updateNumber = secItemDao.reduceNumber(itemId,now);
//
//            if(updateNumber<=0){
//                throw new ClosedSecKillException("seckill closed!");
//            }
//            else{
//                int count = secKillDao.insertSuccessKill(itemId,userPhone);
//                if(count <= 0){
//                    throw new RepeatSecKillException("repeat submit!");
//                }
//                else{
//                    SecKill secKill = secKillDao.getSecKillById(itemId,userPhone);
//                    return new SecKillExecution(itemId, SecKillExecutionStatus.SUCCESS,secKill);
//                }
//            }


            /**
             * 先插入数据库，过滤掉重复秒杀的请求，然后在请求数据库更新表（想对于先更新表，再插入表来说，可以过滤掉一部分数据库的请求，提升处理效率）
             */
            int count = secKillDao.insertSuccessKill(itemId, userPhone);
            if (count <= 0) {
                throw new RepeatSecKillException("repeat submit!");
            } else {
                Date now = new Date();
                int updateNumber = secItemDao.reduceNumber(itemId, now);
                if (updateNumber <= 0) {
                    throw new ClosedSecKillException("seckill closed!");
                } else {
                    SecKill secKill = secKillDao.getSecKillById(itemId, userPhone);
                    return new SecKillExecution(itemId, SecKillExecutionStatus.SUCCESS, secKill);
                }
            }


        } catch (RepeatSecKillException e1) {
            throw e1;
        } catch (ClosedSecKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            /**
             * 转换编译期异常转为运行期异常
             */
            throw new SecKillException("seckill inner error:" + e.getMessage());
        }

    }

    public SecKillExecution executeSeckillProcedure(long itemId, long userPhone, String md5) {
        logger.warn("md5 new ={}", getMD5(itemId));
        if (md5 == null || !md5.equals(getMD5(itemId)) ) {
            return new SecKillExecution(itemId, SecKillExecutionStatus.DATE_REWRITE);
        }
        Date killTime = new Date();
        Map param = new HashMap();
        param.put("itemId", itemId);
        param.put("phone", userPhone);
        param.put("killTime", killTime);
        param.put("result", null);

        try {
            secKillDao.killByProcedure(param);
            //获取result
            int result = MapUtils.getInteger(param,"result");
            if(result == 1){
                SecKill secKill = secKillDao.getSecKillById(itemId,userPhone);
                return new SecKillExecution(itemId,SecKillExecutionStatus.SUCCESS,secKill);
            }
            else{
                return new SecKillExecution(itemId, SecKillExecutionStatus.statusOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SecKillExecution(itemId, SecKillExecutionStatus.INNER_ERROR);
        }


    }
}
