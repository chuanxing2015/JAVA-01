##### 分库分表
1. 创建database
```$xslt
create database ds0;
create database ds1;
```
2. 创建数据表
在每个库中创建16张表，分别为t_order0...15
```$xslt
CREATE TABLE `t_order15` (
    `user_id` INT UNSIGNED COMMENT '自增ID',
    `order_id` VARCHAR(128) NOT NULL COMMENT '订单ID',
    `uid` INT UNSIGNED  NOT NULL COMMENT '用户ID',
    `gid` INT UNSIGNED NOT NULL COMMENT '商品ID',
    `address` VARCHAR(200) NOT NULL COMMENT '邮寄地址',
    `create_time` BIGINT NOT NULL COMMENT '下单时间',
    `pay_time` BIGINT  COMMENT '支付时间',
    PRIMARY KEY (`order_id`)
)ENGINE = innoDB DEFAULT CHARSET=utf8;
```
3. 遇到的问题
* NoSuchElementException , 这个问题主要的原因是sql中没有写表字段名，添加之后就不存在这个问题了
* Missing the data source name: 'null' ,
