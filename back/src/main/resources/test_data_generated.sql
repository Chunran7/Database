-- 测试数据（自动生成）
-- 主题：旅游风景分享
-- 资源目录（相对于后端静态目录）:
--  /uploads/avatars/{id}.jpg
--  /uploads/articles/{id}.jpg
--  /uploads/article_txt/{id}.txt
--  说明：文章正文在 content 字段中写了简短描述并指向对应的文本文件位置，若需完整正文可手动从文件夹复制到 content 字段中。
SET
    NAMES utf8mb4;

-- ==========================
-- 用户 (10 个，头像来自 uploads/avatars)
-- ==========================
INSERT INTO
    `user` (
        username,
        password,
        nickname,
        email,
        user_pic,
        status
    )
VALUES
    (
        'tester',
        MD5('password'),
        '测试用户',
        'tester@example.com',
        '/uploads/avatars/1.jpg',
        1
    ),
    (
        'traveler1',
        MD5('password1'),
        '旅友1',
        'traveler1@example.com',
        '/uploads/avatars/1.jpg',
        1
    ),
    (
        'traveler2',
        MD5('password2'),
        '旅友2',
        'traveler2@example.com',
        '/uploads/avatars/2.jpg',
        1
    ),
    (
        'traveler3',
        MD5('password3'),
        '旅友3',
        'traveler3@example.com',
        '/uploads/avatars/3.jpg',
        1
    ),
    (
        'traveler4',
        MD5('password4'),
        '旅友4',
        'traveler4@example.com',
        '/uploads/avatars/4.jpg',
        1
    ),
    (
        'traveler5',
        MD5('password5'),
        '旅友5',
        'traveler5@example.com',
        '/uploads/avatars/5.jpg',
        1
    ),
    (
        'traveler6',
        MD5('password6'),
        '旅友6',
        'traveler6@example.com',
        '/uploads/avatars/6.jpg',
        1
    ),
    (
        'traveler7',
        MD5('password7'),
        '旅友7',
        'traveler7@example.com',
        '/uploads/avatars/7.jpg',
        1
    ),
    (
        'traveler8',
        MD5('password8'),
        '旅友8',
        'traveler8@example.com',
        '/uploads/avatars/8.jpg',
        1
    ),
    (
        'traveler9',
        MD5('password9'),
        '旅友9',
        'traveler9@example.com',
        '/uploads/avatars/9.jpg',
        1
    ),
    (
        'traveler10',
        MD5('password10'),
        '旅友10',
        'traveler10@example.com',
        '/uploads/avatars/10.jpg',
        1
    );

-- ==========================
-- 文章 (10 篇，封面来自 uploads/articles，正文文件位于 uploads/article_txt)
-- ==========================
INSERT INTO
    `article` (
        user_id,
        title,
        first_picture,
        description,
        content,
        views,
        like_count,
        is_deleted,
        create_time,
        update_time
    )
VALUES
    (
        1,
        '海边日出 - 宁静的早晨',
        '/uploads/articles/1.jpg',
        '清晨的海边，金色的阳光洒在海平面上，适合拍照和沉思。',
        '文章正文存放于 /uploads/article_txt/1.txt，主题：海边日出与拍摄心得。',
        320,
        12,
        0,
        NOW(),
        NOW()
    ),
    (
        2,
        '山间徒步 - 林间小径的呼吸',
        '/uploads/articles/2.jpg',
        '穿行在林间小径，感受大自然的呼吸与风声。',
        '文章正文存放于 /uploads/article_txt/2.txt，主题：山间徒步路线与注意事项。',
        210,
        8,
        0,
        NOW(),
        NOW()
    ),
    (
        3,
        '古镇漫步 - 慢节奏的旅行',
        '/uploads/articles/3.jpg',
        '古镇的青石板路、手工小店与当地小吃，是慢旅行的最佳去处。',
        '文章正文存放于 /uploads/article_txt/3.txt，主题：古镇摄影与美食推荐。',
        180,
        6,
        0,
        NOW(),
        NOW()
    ),
    (
        4,
        '高山湖泊 - 绝美倒影',
        '/uploads/articles/4.jpg',
        '高山湖泊的倒影如镜，是拍摄风景的绝佳场景。',
        '文章正文存放于 /uploads/article_txt/4.txt，主题：高山湖泊的路线与拍摄时间建议。',
        430,
        20,
        0,
        NOW(),
        NOW()
    ),
    (
        5,
        '黄昏小镇 - 灯火阑珊',
        '/uploads/articles/5.jpg',
        '黄昏时分的小镇有种温柔的色调，适合慢慢散步。',
        '文章正文存放于 /uploads/article_txt/5.txt，主题：黄昏拍摄技巧与器材推荐。',
        150,
        4,
        0,
        NOW(),
        NOW()
    ),
    (
        6,
        '沙漠星空 - 漫天繁星',
        '/uploads/articles/6.jpg',
        '远离城市光害，在沙漠能看到最清晰的星空银河。',
        '文章正文存放于 /uploads/article_txt/6.txt，主题：星空拍摄与露营体验。',
        290,
        10,
        0,
        NOW(),
        NOW()
    ),
    (
        7,
        '瀑布秘境 - 声音的旅行',
        '/uploads/articles/7.jpg',
        '奔腾的水流与清凉的雾气，带来视觉与听觉的双重震撼。',
        '文章正文存放于 /uploads/article_txt/7.txt，主题：瀑布探险与安全须知。',
        170,
        5,
        0,
        NOW(),
        NOW()
    ),
    (
        8,
        '沿海自驾 - 路线与拍点',
        '/uploads/articles/8.jpg',
        '沿海公路自驾，推荐若干观景点与停车拍照点。',
        '文章正文存放于 /uploads/article_txt/8.txt，主题：沿海自驾攻略。',
        240,
        9,
        0,
        NOW(),
        NOW()
    ),
    (
        9,
        '花海追梦 - 季节花期指南',
        '/uploads/articles/9.jpg',
        '从郁金香到薰衣草，花海是人像与风光结合的好素材。',
        '文章正文存放于 /uploads/article_txt/9.txt，主题：花海拍摄与着装建议。',
        130,
        3,
        0,
        NOW(),
        NOW()
    ),
    (
        10,
        '城市屋顶 - 俯瞰城市风景',
        '/uploads/articles/10.jpg',
        '从高处俯瞰，城市的纹理与光影更具美感。',
        '文章正文存放于 /uploads/article_txt/10.txt，主题：城市屋顶取景与注意事项。',
        205,
        7,
        0,
        NOW(),
        NOW()
    );

-- ==========================
-- 部分文章点赞与收藏（示例）
-- ==========================
INSERT INTO
    `article_like` (article_id, user_id)
VALUES
    (1, 2),
    (1, 3),
    (1, 4),
    (4, 1),
    (4, 2),
    (4, 5),
    (6, 3),
    (6, 7),
    (8, 2),
    (8, 6);

INSERT INTO
    `article_favorite` (article_id, user_id)
VALUES
    (1, 5),
    (2, 3),
    (4, 1),
    (6, 8);

-- 更新文章统计（like_count 与 views 已在插入时设置为示例值，下面示例再次确保一致）
UPDATE
    `article`
SET
    like_count = (
        SELECT
            COUNT(*)
        FROM
            article_like
        WHERE
            article_like.article_id = article.id
    )
WHERE
    id IN (1, 2, 4, 6, 8);

-- ==========================
-- 可选：视频与帖子使用 uploads/videos，如果需要可以在此处追加初始化
-- ==========================
-- ==========================
-- 视频（10 条，来自 /uploads/videos）
-- ==========================
INSERT INTO
    `video` (
        user_id,
        title,
        url,
        cover,
        description,
        create_time,
        update_time
    )
VALUES
    (
        1,
        '水乡暮色 - 游船与灯笼',
        '/uploads/videos/1.mp4',
        '/uploads/videos/1.png',
        '水乡暮色里，游船划过波光，沿岸灯笼暖光摇曳，尽显江南夜韵。',
        NOW(),
        NOW()
    ),
    (
        2,
        '雪山之巅 - 澄澈与云絮',
        '/uploads/videos/2.mp4',
        '/uploads/videos/2.png',
        '澄澈蓝天衬着巍峨雪山，云絮轻绕峰峦，尽显高原壮阔澄澈。',
        NOW(),
        NOW()
    ),
    (
        3,
        '岸边暮色 - 长草与静谧',
        '/uploads/videos/3.mp4',
        '/uploads/videos/3.png',
        '夕阳铺染的岸边长草轻晃，水色与暮色交融，氛围静谧悠然。',
        NOW(),
        NOW()
    ),
    (
        4,
        '古长城的云雾',
        '/uploads/videos/4.mp4',
        '/uploads/videos/4.png',
        '云雾漫卷间，古长城盘踞山峦，若隐若现，尽显雄关朦胧壮阔。',
        NOW(),
        NOW()
    ),
    (
        5,
        '田野晚霞 - 流云与麦浪',
        '/uploads/videos/5.mp4',
        '/uploads/videos/5.png',
        '晚霞裹着落日沉向田野，流云与麦浪共绘温柔的黄昏图景。',
        NOW(),
        NOW()
    ),
    (
        6,
        '秋日村落 - 白墙黛瓦与柿子',
        '/uploads/videos/6.mp4',
        '/uploads/videos/6.png',
        '白墙黛瓦旁，橙红柿子挂满枝桠，衬出烟火气里的秋日清甜。',
        NOW(),
        NOW()
    ),
    (
        7,
        '山间瀑布 - 清冽水花',
        '/uploads/videos/7.mp4',
        '/uploads/videos/7.png',
        '山间瀑布溅起清冽水花，旁侧繁花簇簇，绘就清新灵动的自然画卷。',
        NOW(),
        NOW()
    ),
    (
        8,
        '海滨黄昏 - 归鸟掠过',
        '/uploads/videos/8.mp4',
        '/uploads/videos/8.png',
        '落日熔金，海面铺展橘色光带，归鸟掠过，晕染静谧海滨黄昏。',
        NOW(),
        NOW()
    ),
    (
        9,
        '彩林雪山 - 斑斓山间',
        '/uploads/videos/9.mp4',
        '/uploads/videos/9.png',
        '雪山为衬，彩林覆坡，云雾轻笼山谷，织就斑斓澄澈的山间盛景。',
        NOW(),
        NOW()
    ),
    (
        10,
        '林间湖镜 - 清冽水色',
        '/uploads/videos/10.mp4',
        '/uploads/videos/10.png',
        '澄澈湖面如镜，映着林影与天光，尽显清冽静谧的林间水色。',
        NOW(),
        NOW()
    );

-- 随机统计数据（浏览量与点赞数示例）
UPDATE
    `video`
SET
    views = 420,
    like_count = 18
WHERE
    id = 1;

UPDATE
    `video`
SET
    views = 380,
    like_count = 15
WHERE
    id = 2;

UPDATE
    `video`
SET
    views = 220,
    like_count = 9
WHERE
    id = 3;

UPDATE
    `video`
SET
    views = 305,
    like_count = 12
WHERE
    id = 4;

UPDATE
    `video`
SET
    views = 175,
    like_count = 6
WHERE
    id = 5;

UPDATE
    `video`
SET
    views = 240,
    like_count = 8
WHERE
    id = 6;

UPDATE
    `video`
SET
    views = 360,
    like_count = 14
WHERE
    id = 7;

UPDATE
    `video`
SET
    views = 410,
    like_count = 17
WHERE
    id = 8;

UPDATE
    `video`
SET
    views = 195,
    like_count = 7
WHERE
    id = 9;

UPDATE
    `video`
SET
    views = 225,
    like_count = 10
WHERE
    id = 10;

-- 脚本结束
COMMIT;

-- ==========================
-- 帖子（10 条，与旅游风光主题相关）
-- ==========================
INSERT INTO
    `post` (
        user_id,
        title,
        content,
        views,
        like_count,
        reply_count,
        is_deleted,
        create_time,
        update_time
    )
VALUES
    (
        1,
        '春季赏花攻略 - 樱花与油菜花的最佳观赏时间',
        '春天是赏花的最佳季节，从南到北的樱花和油菜花田都美不胜收。这里分享一些赏花路线和拍摄技巧。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        2,
        '夏季海边度假 - 精选海滨城市推荐',
        '夏天到了，海边度假成为许多人的首选。推荐几个适合夏日度假的海滨城市，有清澈海水和细腻沙滩。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        3,
        '秋季登山徒步 - 国内最美红叶观赏地',
        '秋天是登山的好时节，天气凉爽，山间红叶满山。推荐几个国内最美的红叶观赏地，适合周末短途旅行。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        4,
        '冬季滑雪胜地 - 南北滑雪场对比',
        '冬天滑雪是很好的运动方式，分享几个国内热门滑雪场的体验，包括设施、价格、交通等信息。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        5,
        '古镇游记 - 江南水乡的慢生活体验',
        '在江南水乡度过一个周末，体验慢生活，感受古老文化。分享一些古镇游览的注意事项和拍照点。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        6,
        '自驾游攻略 - 川藏线318沿线风光',
        '分享川藏线自驾的经验和注意事项，以及沿途最美的风景点和住宿推荐。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        7,
        '国外旅行 - 东南亚海岛度假体验',
        '最近去了东南亚几个热门海岛，对比一下各个岛屿的特色，分享住宿和玩乐体验。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        8,
        '摄影技巧 - 风光摄影的构图与用光',
        '分享一些风光摄影的技巧，包括构图、用光、后期处理等，适合摄影爱好者参考。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        9,
        '背包客之旅 - 西南地区穷游攻略',
        '用最低的预算完成西南地区的旅行，分享交通、住宿、餐饮省钱小技巧。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    ),
    (
        10,
        '美食之旅 - 地道地方小吃探索',
        '旅行中不可缺少的是美食体验，分享各地最具特色的小吃和餐厅推荐。',
        0,
        0,
        0,
        0,
        NOW(),
        NOW()
    );

-- ==========================
-- 部分帖子点赞（示例）
-- ==========================
INSERT INTO
    `post_like` (post_id, user_id)
VALUES
    -- 帖子1的点赞
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    -- 帖子2的点赞
    (2, 1),
    (2, 3),
    (2, 7),
    -- 帖子3的点赞
    (3, 1),
    (3, 2),
    (3, 4),
    (3, 8),
    -- 帖子4的点赞
    (4, 1),
    (4, 5),
    (4, 9),
    -- 帖子5的点赞
    (5, 2),
    (5, 6),
    (5, 7),
    (5, 10),
    -- 帖子6的点赞
    (6, 1),
    (6, 3),
    (6, 5),
    (6, 8),
    -- 帖子7的点赞
    (7, 2),
    (7, 4),
    (7, 6),
    -- 帖子8的点赞
    (8, 1),
    (8, 3),
    (8, 5),
    (8, 7),
    (8, 9),
    -- 帖子9的点赞
    (9, 2),
    (9, 4),
    (9, 6),
    (9, 8),
    -- 帖子10的点赞
    (10, 1),
    (10, 3),
    (10, 5),
    (10, 7),
    (10, 9);

-- ==========================
-- 部分帖子收藏（示例）
-- ==========================
INSERT INTO
    `post_favorite` (post_id, user_id)
VALUES
    -- 帖子1的收藏
    (1, 2),
    (1, 5),
    (1, 8),
    -- 帖子2的收藏
    (2, 1),
    (2, 4),
    -- 帖子3的收藏
    (3, 2),
    (3, 6),
    (3, 9),
    -- 帖子4的收藏
    (4, 1),
    (4, 5),
    (4, 7),
    -- 帖子5的收藏
    (5, 3),
    (5, 6),
    (5, 8),
    (5, 10),
    -- 帖子6的收藏
    (6, 2),
    (6, 4),
    (6, 7),
    -- 帖子7的收藏
    (7, 1),
    (7, 5),
    (7, 9),
    -- 帖子8的收藏
    (8, 3),
    (8, 6),
    (8, 8),
    -- 帖子9的收藏
    (9, 2),
    (9, 4),
    (9, 7),
    -- 帖子10的收藏
    (10, 1),
    (10, 5),
    (10, 8),
    (10, 9);

-- 更新帖子统计（like_count 与 reply_count 需要更新，但不包含不存在的favorite_count）
UPDATE
    `post`
SET
    like_count = (
        SELECT
            COUNT(*)
        FROM
            post_like
        WHERE
            post_like.post_id = post.id
    );

-- 注意：Post表没有favorite_count字段，所以不会更新此字段
-- ==========================
-- 帖子评论（与前面的帖子相关）
-- ==========================
INSERT INTO
    `comment` (
        post_id,
        user_id,
        parent_id,
        root_id,
        reply_user_id,
        content,
        is_deleted,
        create_time
    )
VALUES
    -- 帖子1的评论
    (1, 2, 0, 0, NULL, '谢谢分享，很有用的赏花攻略！', 0, NOW()),
    (
        1,
        3,
        0,
        0,
        NULL,
        '请问樱花的最佳观赏时间具体是什么时候？',
        0,
        NOW()
    ),
    (1, 4, 2, 1, 2, '一般在3-4月份，具体要看当年的气候', 0, NOW()),
    (1, 5, 0, 0, NULL, '油菜花田我也喜欢，大片的黄色真漂亮。', 0, NOW()),
    (
        1,
        6,
        3,
        2,
        3,
        '今年的樱花可能会开得早一些，因为天气比较暖和。',
        0,
        NOW()
    ),
    (1, 7, 1, 1, 2, '我去年去了武大的樱花，真的很美！', 0, NOW()),
    -- 帖子2的评论
    (2, 1, 0, 0, NULL, '推荐一下青岛，夏天的海风很舒服。', 0, NOW()),
    (2, 6, 0, 0, NULL, '三亚怎么样？听说夏天那边很热。', 0, NOW()),
    (
        2,
        7,
        2,
        2,
        1,
        '青岛确实不错，我去年去过，推荐海边的几个景点。',
        0,
        NOW()
    ),
    (
        2,
        8,
        3,
        3,
        2,
        '三亚夏天确实很热，但海水质量很好，适合潜水。',
        0,
        NOW()
    ),
    (2, 9, 1, 1, 1, '青岛的啤酒节也很有名，可以体验一下。', 0, NOW()),
    -- 帖子3的评论
    (3, 4, 0, 0, NULL, '秋天去香山怎么样？听说红叶很美。', 0, NOW()),
    (3, 5, 0, 0, NULL, '推荐去婺源，秋天的景色也很不错。', 0, NOW()),
    (
        3,
        8,
        2,
        2,
        4,
        '香山的红叶确实不错，但人会比较多，建议早上去。',
        0,
        NOW()
    ),
    (
        3,
        10,
        3,
        3,
        5,
        '婺源的秋天有很多古村落，红叶和徽派建筑搭配很美。',
        0,
        NOW()
    ),
    (
        3,
        2,
        1,
        1,
        4,
        '我去年去了北京的慕田峪长城，秋天的景色也很棒。',
        0,
        NOW()
    ),
    -- 帖子4的评论
    (4, 1, 0, 0, NULL, '推荐长白山附近的滑雪场，雪质很好。', 0, NOW()),
    (4, 9, 0, 0, NULL, '北方的滑雪场设施普遍比南方好。', 0, NOW()),
    (4, 3, 1, 1, 1, '长白山的滑雪场确实不错，但价格也相对较高。', 0, NOW()),
    (4, 6, 2, 2, 9, '南方的滑雪场主要在室内，适合初学者。', 0, NOW()),
    (4, 8, 1, 1, 1, '我去年去了张家口的滑雪场，体验很好。', 0, NOW()),
    -- 帖子5的评论
    (5, 2, 0, 0, NULL, '乌镇和西塘都去过，各有特色。', 0, NOW()),
    (5, 6, 0, 0, NULL, '建议多待几天，慢慢感受水乡的氛围。', 0, NOW()),
    (5, 7, 2, 2, 2, '是的，匆匆忙忙游览就失去了慢生活的意义。', 0, NOW()),
    (5, 10, 1, 1, 2, '我更喜欢乌镇，更安静一些。', 0, NOW()),
    (5, 4, 3, 3, 6, '推荐住一晚民宿，体验更深刻。', 0, NOW()),
    -- 帖子6的评论
    (
        6,
        3,
        0,
        0,
        NULL,
        '川藏线风景确实美，但对驾驶技术要求高。',
        0,
        NOW()
    ),
    (6, 8, 0, 0, NULL, '建议结伴而行，安全第一。', 0, NOW()),
    (6, 10, 2, 2, 3, '如果自驾经验不够丰富，可以考虑包车。', 0, NOW()),
    (
        6,
        2,
        1,
        1,
        3,
        '川藏线的路况已经好了很多，但还是要小心驾驶。',
        0,
        NOW()
    ),
    (6, 5, 3, 3, 10, '包车费用大概多少？', 0, NOW()),
    (
        6,
        7,
        5,
        5,
        5,
        '根据车型和路线不同，大概每天800-1500元不等。',
        0,
        NOW()
    ),
    -- 帖子7的评论
    (7, 4, 0, 0, NULL, '推荐巴厘岛，消费不高，风景好。', 0, NOW()),
    (7, 5, 0, 0, NULL, '普吉岛也不错，海滩很美。', 0, NOW()),
    (
        7,
        9,
        1,
        1,
        4,
        '巴厘岛的文化体验更丰富，有很多寺庙和传统活动。',
        0,
        NOW()
    ),
    (7, 1, 2, 2, 5, '普吉岛的水上活动更多，适合喜欢玩的朋友。', 0, NOW()),
    (7, 3, 1, 1, 4, '我去年去了巴厘岛，住的度假酒店很棒！', 0, NOW()),
    -- 帖子8的评论
    (8, 1, 0, 0, NULL, '风光摄影最重要的是光线的运用。', 0, NOW()),
    (8, 6, 0, 0, NULL, '构图技巧也很重要，三分法则很实用。', 0, NOW()),
    (
        8,
        4,
        1,
        1,
        1,
        '黄金时刻拍摄的照片效果最好，清晨和傍晚的光线最美。',
        0,
        NOW()
    ),
    (8, 7, 2, 2, 6, '对称构图也不错，特别是拍摄建筑的时候。', 0, NOW()),
    (8, 9, 3, 3, 4, '请问有什么推荐的相机吗？', 0, NOW()),
    (
        8,
        2,
        5,
        5,
        9,
        '入门级的话，索尼A6400或者佳能M50都不错。',
        0,
        NOW()
    ),
    -- 帖子9的评论
    (9, 3, 0, 0, NULL, '穷游最重要的是提前做好攻略。', 0, NOW()),
    (9, 7, 0, 0, NULL, '住宿可以选择青旅，能省不少钱。', 0, NOW()),
    (9, 5, 1, 1, 3, '同意，提前订好交通和住宿能省很多钱。', 0, NOW()),
    (9, 10, 2, 2, 7, '青旅还能认识很多志同道合的朋友，很不错。', 0, NOW()),
    (9, 2, 1, 1, 3, '我觉得穷游不是为了省钱，而是一种体验方式。', 0, NOW()),
    -- 帖子10的评论
    (10, 2, 0, 0, NULL, '四川的小吃最棒了，特别是成都。', 0, NOW()),
    (10, 8, 0, 0, NULL, '西安的小吃也很有特色，面食居多。', 0, NOW()),
    (10, 1, 1, 1, 2, '成都的火锅和串串香一定要尝尝！', 0, NOW()),
    (10, 6, 2, 2, 8, '西安的肉夹馍和凉皮是绝配！', 0, NOW()),
    (10, 4, 3, 3, 1, '推荐去锦里古街，有很多成都特色小吃。', 0, NOW()),
    (10, 9, 4, 4, 6, '回民街的小吃也很多，建议晚上去。', 0, NOW());

-- 更新帖子的回复数统计
UPDATE
    `post`
SET
    reply_count = (
        SELECT
            COUNT(*)
        FROM
            comment
        WHERE
            comment.post_id = post.id
            AND comment.is_deleted = 0
    );

UPDATE
    `post`
SET
    like_count = (
        SELECT
            COUNT(*)
        FROM
            post_like
        WHERE
            post_like.post_id = post.id
    );

UPDATE
    `post`
SET
    views = FLOOR(100 + (RAND() * 400));

COMMIT;