package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SecKillExecution;
import com.seckill.entry.SecItem;
import com.seckill.exception.ClosedSecKillException;
import com.seckill.exception.RepeatSecKillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by hgf on 16-5-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SecKillServiceTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillService secKillService;

    long itemId = 1000;


    @Test
    public void getSecItems() throws Exception {
        List<SecItem> items = secKillService.getSecItems();
        logger.info("list={}", items);
    }

    @Test
    public void getSecItemById() throws Exception {
        SecItem item = secKillService.getSecItemById(itemId);
        logger.info("item={}", item);
    }

    @Test
    public void exposeSecKillUrl() throws Exception {
        Exposer exposer = secKillService.exposeSecKillUrl(itemId);
        logger.info("exposer={}", exposer);
//        exposer=Exposer{canExpose=true, md5='61510a2e6b8dbece7bdf8be722fbdb79', secItemId=1000, now=0, start=0, end=0}
    }

    @Test
    public void testSecKillServiceLogic() {
        Exposer exposer = secKillService.exposeSecKillUrl(itemId);
        long phone = 13716112488L;
        logger.info("exposer={}", exposer);
        String md5 = exposer.getMd5();
        try {
            SecKillExecution execution = secKillService.executeSecKill(itemId, phone, md5);
            logger.info("execution={}", execution);

        }catch (RepeatSecKillException e){
            logger.warn(e.getMessage());
        }catch (ClosedSecKillException e2){
            logger.warn(e2.getMessage());
        }
    }


}