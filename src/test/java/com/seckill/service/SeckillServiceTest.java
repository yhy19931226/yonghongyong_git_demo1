package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                       "classpath:spring/spring-service.xml"})
@Slf4j
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    @Test
    public void querySeckillList() {
        List<Seckill> seckillList = seckillService.querySeckillList();
        for (Seckill seckill : seckillList){
            System.out.println(seckill);
        }
    }

    @Test
    public void querySeckillById() {
        Seckill seckill = seckillService.querySeckillById(1000L);
        System.out.println(seckill);
        log.info("seckill = {}",seckill);
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1000L);
        if(exposer.isExposed()){
            log.info("exposer = {}",exposer);
            String md5= exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(1000L, 12345689810L, md5);
                log.info("seckillExecution = {}",seckillExecution);
            }catch (SeckillCloseException e){
                log.info(e.getMessage());
            }catch (RepeatKillException e){
                log.info(e.getMessage());
            }
        }else{
            log.warn("exposer = {}",exposer);
        }
        //md5=a9659c3648b4f55a2e976941c3986271
    }

    @Test
    public void executeSeckill() {
        String md5= "a9659c3648b4f55a2e976941c3986271";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(1000L, 12345688810L, md5);
            log.info("seckillExecution = {}",seckillExecution);
        }catch (SeckillCloseException e){
            log.info(e.getMessage());
        }catch (RepeatKillException e){
            log.info(e.getMessage());
        }
    }

    @Test
    public void TestProcedure(){
        Long seckillId = 1000L;
        Long userPhone = 12314778910L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            log.info(seckillExecution.getStateInfo());
        }

    }
}