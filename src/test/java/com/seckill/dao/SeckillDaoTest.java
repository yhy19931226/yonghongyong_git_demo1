package com.seckill.dao;

import com.seckill.entity.Seckill;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


//配置spring和mybatis的整合
@RunWith(SpringJUnit4ClassRunner.class) //该注解帮我们整合spring和mybatis并启动springIoc容器
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
@Slf4j
public class SeckillDaoTest {

    //注入dao的实现类
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void selectSeckillById() {
        Seckill seckill = seckillDao.selectSeckillById(1000L);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckillList = seckillDao.queryAll(0, 10);
        log.info("seckillList = {}",seckillList);
        Assert.assertNotEquals(0,seckillList.size());
    }

    @Test
    public void reduceNumber() {
        int reduceNumber = seckillDao.reduceNumber(1000L, new Date());
        System.out.println(reduceNumber);
    }


}