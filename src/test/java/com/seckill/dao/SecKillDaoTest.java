package com.seckill.dao;

import com.seckill.entry.SecItem;
import com.seckill.entry.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by hgf on 16-5-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {

    @Resource
    private SecKillDao secKillDao;

    long itemId = 1000;
    long phone = 13716112222L;

    @Test
    public void insertSuccessKill() throws Exception {

        int re = secKillDao.insertSuccessKill(itemId,phone);
        System.out.println(re);
    }

    @Test
    public void getSecKillById() throws Exception {
        SecKill secKill = secKillDao.getSecKillById(itemId,phone);
        System.out.println(secKill);
        System.out.println(secKill.getItem());
    }

}