SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `detail_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `product_price` decimal(8,2) NOT NULL DEFAULT '0.00',
  `product_quantity` int(11) NOT NULL COMMENT '商品数量',
  `product_icon` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for order_master
-- ----------------------------
DROP TABLE IF EXISTS `order_master`;
CREATE TABLE `order_master` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `buyer_name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '买家名字',
  `buyer_phone` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '买家电话',
  `buyer_address` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '买家地址',
  `buyer_openid` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '买家微信id',
  `order_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `order_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态  0新下单',
  `pay_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '支付状态 0未支付',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `order_no` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`order_id`),
  KEY `idx_buyer_openid` (`buyer_openid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of order_master
-- ----------------------------

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目名称',
  `category_type` int(11) DEFAULT NULL COMMENT '类目编号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES ('1', '少女最爱', '3', '2020-01-15 15:39:22', '2020-01-17 10:30:54');
INSERT INTO `product_category` VALUES ('2', '男神最爱', '2', '2020-01-15 16:00:38', '2020-01-17 10:30:52');
INSERT INTO `product_category` VALUES ('3', '热销榜', '1', '2020-01-16 15:57:47', '2020-01-16 15:57:47');


-- ----------------------------
-- Table structure for product_info
-- ----------------------------
DROP TABLE IF EXISTS `product_info`;
CREATE TABLE `product_info` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `product_price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '单价',
  `product_stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `product_icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小图',
  `category_type` int(11) NOT NULL COMMENT '类目',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `product_status` tinyint(3) DEFAULT '0' COMMENT '0正常 1下架',
  `product_desc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of product_info
-- ----------------------------
INSERT INTO `product_info` VALUES ('7', '皮蛋瘦肉粥', '5.20', '275', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1383503283,1395820520&fm=26&gp=0.jpg', '1', '2020-01-16 16:39:09', '2020-04-03 11:35:08', '0', '很好喝的粥');
INSERT INTO `product_info` VALUES ('10', '煲仔饭粥', '15.20', '94', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=659286534,510217608&fm=26&gp=0.jpg', '1', '2020-01-16 16:42:06', '2020-03-10 19:21:06', '0', '很好吃的饭');
INSERT INTO `product_info` VALUES ('11', '陈村肠粉', '3.50', '182', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2832546724,1968571262&fm=26&gp=0.jpg', '1', '2020-01-16 16:43:58', '2020-01-17 23:15:03', '0', '美味的肠粉');
INSERT INTO `product_info` VALUES ('12', '芒果冰沙', '5.00', '283', 'http://img1.imgtn.bdimg.com/it/u=2573785069,3858275524&fm=26&gp=0.jpg', '2', '2020-01-17 10:32:56', '2020-04-03 11:30:42', '0', null);
INSERT INTO `product_info` VALUES ('13', '皮蛋瘦肉粥', '5.20', '77', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1383503283,1395820520&fm=26&gp=0.jpg', '1', '2020-01-16 16:39:09', '2020-01-18 01:52:44', '0', '很好喝的粥');
INSERT INTO `product_info` VALUES ('14', '皮蛋瘦肉粥', '5.20', '77', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1383503283,1395820520&fm=26&gp=0.jpg', '1', '2020-01-16 16:39:09', '2020-01-18 01:52:44', '0', '很好喝的粥');
INSERT INTO `product_info` VALUES ('15', '皮蛋瘦肉粥', '5.20', '77', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1383503283,1395820520&fm=26&gp=0.jpg', '1', '2020-01-16 16:39:09', '2020-01-18 01:52:44', '0', '很好喝的粥');
INSERT INTO `product_info` VALUES ('16', '韭菜炒蛋', '15.00', '9', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583763761915&di=e476aeaffe2c749d4b8771ba8752fa2a&imgtype=0&src=http%3A%2F%2Fi5.chuimg.com%2Fabcca5ec61854470bbf7981b7325ac75_1080w_1080h.jpg%402o_50sh_1pr_1l_600w_90q_1wh', '2', '2020-03-09 19:34:59', '2020-04-03 11:33:40', '0', '韭菜炒蛋，老婆爱！');
INSERT INTO `product_info` VALUES ('17', '烤生蚝', '5.00', '100', 'http://i5.chuimg.com/96c794bb847847d6b721539aea9f8d61_1080w_1080h.jpg', '1', '2020-03-10 10:06:24', '2020-03-10 10:06:24', '0', '女生美容院，男生加油站！');

-- ----------------------------
-- Table structure for seller_info
-- ----------------------------
DROP TABLE IF EXISTS `seller_info`;
CREATE TABLE `seller_info` (
  `seller_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(32) COLLATE utf8mb4_general_ci NOT NULL,
  `openid` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`seller_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of seller_info
-- ----------------------------
INSERT INTO `seller_info` VALUES ('1', 'admin', 'admin', 'abcd', '2020-03-10 11:32:50', '2020-03-10 11:32:50');
