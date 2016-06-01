package com.seckill.dao.cache;

import com.seckill.dao.SecItemDao;
import com.seckill.entry.SecItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by hgf on 16-5-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml"})
public class SeckItemCacheTest {

    private Logger logger = LoggerFactory.getLogger(SeckItemCacheTest.class);

    @Autowired
    private SeckItemCache seckItemCache;

    @Autowired
    private SecItemDao secItemDao;

    @Test
    public void cacheTest() throws Exception {
        long id = 1000;
        SecItem item = seckItemCache.getSceItemFromCache(id);
        logger.info("secitem={}", item);

        if(item == null){
            item = secItemDao.queryItemById(id);
            String result = seckItemCache.putSceItemToCache(item);

            logger.info("result={}", result);
        }


    }


}