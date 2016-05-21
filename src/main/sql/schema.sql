# database
CREATE DATABASE seckill;

# 标设计

# secitem: record the items to seckill
# item_id,name, number,create_time,start_time,end_time

CREATE TABLE secitem(
  item_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  name VARCHAR(120) NOT NULL  COMMENT '商品名',
  number INT NOT NULL COMMENT '商品数量',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '秒杀商品创建时间',
  start_time TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
  end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (item_id),
  KEY index_start_time(start_time),
  KEY index_end_time(end_time),
  KEY index_create_time(create_time)

)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT ='秒杀库存表';

## init data
INSERT INTO secitem(name,number,start_time,end_time)
    VALUES
      ('iphone6s',10,'2016-06-01 00:00:00','2016-06-02 00:00:00'),
      ('iphone5s',9,'2016-06-01 00:00:00','2016-06-02 00:00:00'),
      ('iphone4s',8,'2016-06-01 00:00:00','2016-06-02 00:00:00'),
      ('ipad4',7,'2016-06-01 00:00:00','2016-06-02 00:00:00');


# seckill: record the seckill info
# item_id,phone,stat,kill_time

CREATE TABLE seckill(
  item_id BIGINT NOT NULL COMMENT '秒杀商品ID',
  phone BIGINT NOT NULL COMMENT '秒杀用户电话',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '秒杀状态：-1无效，0成功，1付款',
  kill_time TIMESTAMP NOT NULL COMMENT '秒杀成功时间',
  PRIMARY KEY (item_id,phone)
)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '秒杀记录表';

## init data

INSERT INTO seckill(item_id, phone, kill_time)
    VALUES
      (1000,'13716112488','2016-06-01 00:00:01'),
      (1001,'13716112487','2016-06-01 00:00:01'),
      (1002,'13716112489','2016-06-01 00:00:01');
