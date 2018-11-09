-- 初始化数据库脚本
create database seckill;
-- 使用seckill数据库
use seckill;
-- 创建秒杀数据库表
create table seckill(
seckill_id bigint not null AUTO_INCREMENT comment '商品库存id',
name varchar(120) not null comment '商品名称',
number int not null comment '商品数量',
start_time timestamp not null comment '秒杀开启时间',
end_time timestamp not null comment '秒杀结束时间',
create_time timestamp not null default current_timestamp comment '创建时间',
primary key (seckill_id),
key idx_start_time (start_time),
key idx_end_time (end_time),
key idx_create_time (create_time)
)ENGINE = INNODB AUTO_INCREMENT = 1000 default charset = UTF8 comment = '秒杀库存表';

--初始化数据
insert into seckill
  (name,number,start_time,end_time)
values
  ("1000元秒杀iphone6",100,'2018-11-01 00:00:00','2018-11-01 00:00:00'),
  ("500元秒杀小米2s",200,'2018-11-01 00:00:00','2018-11-01 00:00:00'),
  ("100元秒杀蚕丝被",100,'2018-11-01 00:00:00','2018-11-01 00:00:00'),
  ("888元秒杀华为mate10",100,'2018-11-01 00:00:00','2018-11-01 00:00:00'),
  ("10元秒杀电扇",100,'2018-11-01 00:00:00','2018-11-01 00:00:00')

create table success_killed(
seckill_id bigint not null comment '秒杀商品id',
user_phone bigint not null comment '手机号',
state tinyint not null default -1 comment '状态标识 -1表示无效，0表示成功，1表示已付款，2表示已发货',
create_time timestamp not null comment '创建时间',
primary key (seckill_id,user_phone), /*联合主键*/
key idx_create_time (create_time)
)engine = InnoDB default charset = UTF8 comment = '秒杀成功明细表';