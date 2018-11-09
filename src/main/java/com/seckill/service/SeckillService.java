package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口: 秒杀信息业务接口
 */
public interface SeckillService {

    /**
     * 查询所有秒杀信息
     * @return
     */
   List<Seckill> querySeckillList();

    /**
     * 查询单个秒杀信息
     * @param seckillId
     * @return
     */
   Seckill querySeckillById(Long seckillId);

    /**
     * 秒杀开启输出秒杀接口地址
     * 否则输出秒杀时间和结束时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(Long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException;

    /**
     * 通过存储过程来执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProcedure(Long seckillId, Long userPhone, String md5);


}
