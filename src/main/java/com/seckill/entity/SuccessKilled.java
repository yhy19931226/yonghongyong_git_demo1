package com.seckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SuccessKilled {

    private Long seckillId;

    private Long userPhone;

    private Short state;

    private Date createTime;

    private Seckill seckill;
}
