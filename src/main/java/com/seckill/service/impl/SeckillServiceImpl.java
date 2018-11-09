package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    /**md5盐加密字符串*/
    private final String slat = "axfdgajhf:^&*^*45";

    @Override
    public List<Seckill> querySeckillList() {
        return seckillDao.queryAll(0,5);
    }

    @Override
    public Seckill querySeckillById(Long seckillId) {
        return seckillDao.selectSeckillById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(Long seckillId) {
        //优化点：缓存优化
        //1.先查询redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            //2.redis没有在查询数据库
            seckill = seckillDao.selectSeckillById(seckillId);
            if(seckill == null){
                return new Exposer(false,seckillId);
            }else{
                //3.将查询到的对象存放在redis当中
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(Long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行描扫逻辑--->减库存+新增购买记录
        Date nowTime = new Date();
        try {
            //新增购买记录
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if(insertCount<=0){
                //秒杀重复
                throw new RepeatKillException("seckill repeated");
            }else{
                //减库存
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if(updateCount<=0){
                    //表示没有更新到记录 秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                }else{
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }

        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error"+e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(Long seckillId, Long userPhone, String md5) {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        HashMap<String, Object> map = new HashMap<>();
        map.put("seckillId",seckillId);
        map.put("userPhone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            seckillDao.killByProcedure(map);
            //获取result
            Integer result = MapUtils.getInteger(map, "result", -2);
            if(result == 1){
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,successKilled);
            }else{
                return new SeckillExecution(seckillId,SeckillStateEnum.stateof(result));
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }
}
