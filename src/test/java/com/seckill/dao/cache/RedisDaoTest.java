package com.seckill.dao.cache;

import com.seckill.dao.SeckillDao;
import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/spring-dao.xml",
                        "/spring/spring-service.xml",
                        "/spring/spring-web.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void TestSeckill() {
        Seckill seckill = seckillDao.selectSeckillById(1000L);
        String result = redisDao.putSeckill(seckill);
        System.out.println(result);
        Seckill seckill1 = redisDao.getSeckill(seckill.getSeckillId());
        System.out.println(seckill);
    }
}