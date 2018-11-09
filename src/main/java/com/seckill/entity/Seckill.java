package com.seckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Seckill {

    private Long seckillId ;

    private String name;

    private Integer number;

    private Date startTime;

    private Date endTime;

    private Date createTime;

}
