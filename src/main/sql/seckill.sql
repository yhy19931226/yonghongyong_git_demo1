-- 定义秒杀的存储过程
DELIMITER $$
-- row_count() 返回上一行sql影响的行数（insert,delete,update）

CREATE PROCEDURE execute_seckill(
  in v_seckill_id bigint, in v_phone bigint,
  in v_kill_time timestamp,out r_result int)
BEGIN
  DECLARE insert_count int default 0;
  START TRANSACTION ;
  insert ignore into success_killed(seckill_id,user_phone,create_time)
  values (v_seckill_id,v_phone,v_kill_time);
  select row_count() into insert_count;
  IF(insert_count=0)THEN
  ROLLBACK;
  SET r_result = -1;
  ELSEIF(insert_count<0) THEN
  ROLLBACK;
  SET r_result = -2;
  ELSE
    update seckill
    set number = number -1
    where seckill_id = v_seckill_id
    and number >0
    and end_time>v_kill_time
    and start_time<v_kill_time;
    select row_count() into insert_count;
    IF(insert_count = 0) THEN
    ROLLBACK;
    SET r_result = 0;
    ELSEIF(insert_count <0) THEN
    ROLLBACK;
    SET r_result = -2;
    ELSE
    COMMIT;
    SET r_result = insert_count;
    END IF;
  END IF;
END;
$$
DELIMITER ;

-- 定义结果变量，并执行存储过程
set @r_result = -3;
call execute_seckill(1000,12336945610,now(),@r_result);

select @r_result;