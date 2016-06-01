package functiontest.testredis;

import com.seckill.dao.cache.SeckItemCache;
import com.seckill.entry.SecItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 17800次插入操作/s
 *
 *
 * Created by hgf on 16-5-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml"})
public class TestRedis {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getClass());

    @Autowired
    private SeckItemCache seckItemCache;

    @Test
    public void testInsert() {
        int num = 1000;
        SecItem item = seckItemCache.getSceItemFromCache(1000);
        long start = System.nanoTime();
        for (int i = 0; i < num; i++) {
            try {
                String result = seckItemCache.putSceItemToCache(item);
            }
            catch (Exception e){
                logger.error(e.getMessage());
            }
        }

        long end = System.nanoTime();
        logger.info("time={}",(end-start));

    }

    @Test
    public void cal(){
        logger.info("cal={}", ((double)(1000000000L/(double)56096041) * (double)1000));
    }
}
