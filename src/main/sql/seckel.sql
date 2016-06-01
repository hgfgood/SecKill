

-- 秒杀存储过程
DELIMITER $$ -- cosole中;转为$$

-- 定义存储过程

-- 存储过程优化：事务行级锁持有时间简化

-- 使用row_count() 函数获取上一条修改操作sql影响的行数

CREATE PROCEDURE `seckill`.`execute_seckill`(in v_secitem_id bigint, in v_phone bigint, in v_kill_time TIMESTAMP ,out r_result int)
BEGIN
  DECLARE insert_count int DEFAULT 0;
  start TRANSACTION ;
  INSERT ignore INTO seckill(item_id, phone,status,kill_time)
  VALUES (v_secitem_id, v_phone, 1, v_kill_time);
  SELECT ROW_COUNT() INTO insert_count;
  IF( insert_count =0) then
    ROLLBACK;
    set r_result = -2;
  ELSEIF (insert_count < 0) THEN
    ROLLBACK;
    set r_result = -4;
  ELSE
    UPDATE secitem
    set number = number-1
    WHERE
      item_id = v_secitem_id
      and end_time > v_kill_time
      and start_time < v_kill_time
      and number>0;
    SELECT ROW_COUNT() INTO insert_count;
    IF (insert_count < 0) THEN
      ROLLBACK;
      set r_result = -4;
    ELSE
      COMMIT ;
      set r_result = 1;
    END IF;
  END IF;
END ;

$$

-- 存储过程结束

DELIMITER ;
SET @r_result = -5;
call execute_seckill(1003, 13716112485, now(), @r_result);

-- 获取结果

SELECT @r_result;