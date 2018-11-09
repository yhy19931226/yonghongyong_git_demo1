package com.seckill.dto;

import lombok.Data;

/**
 * 暴露秒杀地址DTO
 */
@Data
public class Exposer {

    /**是否暴露*/
    private boolean exposed;
    /**一种加密机制*/
    private String md5;
    /**seckillId*/
    private Long seckillId;
    /**系统当前时间*/
    private Long now;
    /**秒杀开启时间*/
    private Long start;
    /**秒杀结束时间*/
    private Long end;

    public Exposer(boolean exposed, String md5, Long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed,Long seckillId, Long now, Long start, Long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, Long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }
}
