#商品表  
CREATE TABLE `goods`(
`id` BIGINT(20) not null auto_increment COMMENT '商品id',
`goods_name` varchar(16) DEFAULT null COMMENT '商品名称',
`goods_title` varchar(64) DEFAULT null COMMENT '商品标题',
`goods_img` varchar(64) DEFAULT NULL COMMENT '商品图片',
`goods_detail` longtext COMMENT '商品的详情介绍',
`goods_price` DECIMAL(10,2) DEFAULT '0.00' comment '商品单价',
`goods_stock` int(11) DEFAULT '0' COMMENT '商品库存,-1表示无限',
PRIMARY KEY(`id`)
)ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

# 秒杀商品表  
CREATE TABLE `second_goods` (
`id` bigint(20) not null auto_increment comment '秒杀的商品',
`goods_id` bigint(20) default null comment '商品id',
`second_price` decimal(10,2) default '0.00' comment '秒杀价',
`stock_count` int(11) default null comment '库存数量',
`start_date` datetime default null comment '秒杀开始时间',
`end_date` datetime default null comment '秒杀结束时间',
primary key(id)
)ENGINE=InnoDB AUTO_INCREMENT=3 default charset=utf8mb4;

insert into `second_goods` values(1,1,0.01,4,'2018-05-23 10:00:00','2018-06-10 20:00:00'),(2,2,0.01,10,'2018-05-25 10:00:00','2018-06-10 20:00:00');
insert into `second_goods` values(3,3,0.01,20,'2018-06-23 10:00:00','2018-06-25 20:00:00')
# 订单表  
create table `order_info`(
`id` bigint(20) not null auto_increment,
`user_id` bigint(20) default null comment '用户id',
`goods_id` bigint(20) default null comment '商品id',
`delivery_addr_id` bigint(20) default null comment '收货地址id',
`goods_name` varchar(16) default null comment '冗余商品名称',
`goods_count` int(11) default '0' comment '商品数量',
`goods_price` decimal(10,2) default '0.00' comment '商品单价',
`order_channel` tinyint(4) default '0' comment '1pc, 2android, 3ioS',
`status` tinyint(4) default '0' comment '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
`create_date` datetime default null comment '订单创建时间',
`pay_date` datetime default null comment '支付时间',
primary key(`id`)
)ENGINE=InnoDB auto_increment=12 default charset=utf8mb4;

# 秒杀表  
create table `second_order`(
`id` bigint(20) not null auto_increment,
`user_id` bigint(20) default null comment '用户id',
`order_id` bigint(20) default null comment '订单id',
`goods_id` bigint(20) default null comment '商品id',
primary key(`id`)
)ENGINE=InnoDB auto_increment=3 default charset=utf8mb4;

