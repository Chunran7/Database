-- 创建文章表
CREATE TABLE `article` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(100) NOT NULL COMMENT '文章标题',
  `content` longtext COMMENT '文章内容',
  `author` varchar(50) NOT NULL COMMENT '文章作者',
  `picture` varchar(255) DEFAULT NULL COMMENT '文章图片URL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `category` varchar(50) DEFAULT NULL COMMENT '文章分类',
  `status` varchar(20) DEFAULT 'draft' COMMENT '文章状态（draft:草稿, published:已发布, deleted:已删除）',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '发帖用户ID',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '正文',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    reply_count INT NOT NULL DEFAULT 0 COMMENT '回复数（冗余字段）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

