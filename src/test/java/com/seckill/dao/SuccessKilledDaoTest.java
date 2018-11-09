package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
@Slf4j
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int number = successKilledDao.insertSuccessKilled(1000L, 11345678910L);
        System.out.println(number);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L, 11345678910L);
        log.info("successKilled = {}",successKilled);
//        seckillId=1000,
//        userPhone=12345678910,
//        state=-1,
//        createTime=Fri Nov 02 14:57:03 CST 2018,
//
//        seckill=Seckill(seckillId=1000,
//        name=1000元秒杀iphone6,
//        number=99,
//        startTime=Fri Nov 02 14:33:57 CST 2018,
//        endTime=Sun Nov 11 00:00:00 CST 2018,
//        createTime=Thu Nov 01 20:29:26 CST 2018)
    }
}