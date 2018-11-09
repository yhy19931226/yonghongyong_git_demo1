package com.seckill.dto;

import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import lombok.Data;

/**
 * 封装执行秒杀后的结果
 */
@Data
public class SeckillExecution {

    private Long seckillId;
    /**秒杀状态*/
    private int state;
    /**秒杀状态说明*/
    private String stateInfo;
    /**秒杀成功对象*/
    private SuccessKilled successKilled;

    public SeckillExecution(Long seckillId, SeckillStateEnum seckillStateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(Long seckillId, SeckillStateEnum seckillStateEnum) {
        this.seckillId = seckillId;
        this.state = seckillStateEnum.getState();
        this.stateInfo = seckillStateEnum.getStateInfo();
    }
}
