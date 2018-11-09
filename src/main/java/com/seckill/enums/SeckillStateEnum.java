package com.seckill.enums;

import lombok.Getter;

/**
 * 秒杀状态的枚举类
 */
@Getter
public enum SeckillStateEnum {

    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"内部错误"),
    DATA_REWRITE(-3,"数据篡改"),
    ;

    private int state;

    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateof(int state){
        for (SeckillStateEnum seckillStateEnum : values()){
            if(state == seckillStateEnum.getState()){
                return seckillStateEnum;
            }
        }
        return null;
    }
}
