package com.seckill.dao;

import com.seckill.entry.SecItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hgf on 16-5-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring/spring-dao.xml"})
public class SecItemDaoTest {

//    注入依赖
    @Autowired
    private SecItemDao secItemDao;

    @Test
    public void reduceNumber() throws Exception {
        long id = 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date day = sdf.parse("2016-06-01 00:01:01");
        int item = secItemDao.reduceNumber(id,day);
        System.out.println(item);
    }

    @Test
    public void queryItemById() throws Exception {
        long id = 1000;
        SecItem item = secItemDao.queryItemById(id);
        System.out.println(item);
    }

    @Test
    public void queryAllItems() throws Exception {
        List<SecItem> items = secItemDao.queryAllItems(1,3);
        for(SecItem item:items){
            System.out.println(item);
        }
    }

}