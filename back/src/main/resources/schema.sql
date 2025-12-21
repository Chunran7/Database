-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `DBproject` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `DBproject`;

-- 2. 用户表 (对应 User.java)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                        `password` VARCHAR(128) NOT NULL COMMENT '密码',
                        `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
                        `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
                        `user_pic` VARCHAR(255) DEFAULT NULL COMMENT '用户头像地址',
                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 文章表 (对应 Article.java，结合了 sql 片段中的长内容支持)
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
                           `title` VARCHAR(255) NOT NULL COMMENT '文章标题',
                           `first_picture` VARCHAR(255) DEFAULT NULL COMMENT '首图URL',
                           `description` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要/描述',
                           `content` LONGTEXT COMMENT '文章正文',
                           `author` VARCHAR(50) NOT NULL COMMENT '作者',
                           `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                           `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `views` INT DEFAULT 0 COMMENT '浏览量',
                           `likes` INT DEFAULT 0 COMMENT '点赞数',
                           PRIMARY KEY (`id`),
                           KEY `idx_author` (`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 4. 帖子表 (对应 Post.java)
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
                        `user_id` BIGINT NOT NULL COMMENT '发帖用户ID',
                        `title` VARCHAR(255) NOT NULL COMMENT '标题',
                        `content` TEXT NOT NULL COMMENT '正文',
                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                        `like_count` INT DEFAULT 0 COMMENT '点赞数',
                        `reply_count` INT DEFAULT 0 COMMENT '回复数',
                        PRIMARY KEY (`id`),
                        KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 5. 评论表 (对应 Comment.java，支持多级回复)
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
                           `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID (针对二级回复)',
                           `root_id` BIGINT DEFAULT 0 COMMENT '根评论ID (属于哪个一级评论)',
                           `post_id` BIGINT NOT NULL COMMENT '所属帖子ID',
                           `user_id` BIGINT NOT NULL COMMENT '发表评论的用户ID',
                           `reply_user_id` BIGINT DEFAULT NULL COMMENT '被回复人的用户ID',
                           `content` TEXT NOT NULL COMMENT '评论内容',
                           `is_deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除, 1:已删除)',
                           `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_post_id` (`post_id`),
                           KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 6. 视频表 (对应 Video.java)
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '视频ID',
                         `title` VARCHAR(255) NOT NULL COMMENT '视频标题',
                         `url` VARCHAR(255) NOT NULL COMMENT '视频播放链接',
                         `cover` VARCHAR(255) DEFAULT NULL COMMENT '视频封面图',
                         `description` TEXT COMMENT '视频描述',
                         `author` VARCHAR(50) DEFAULT NULL COMMENT '作者',
                         `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                         `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                         `views` INT DEFAULT 0 COMMENT '浏览量',
                         PRIMARY KEY (`id`),
                         KEY `idx_author` (`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频表';