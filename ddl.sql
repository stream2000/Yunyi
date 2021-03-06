# 用户表，不设置密码登录
drop table if exists user;
create table user
(
    id    bigint auto_increment comment '主键ID',
    name  varchar(100) not null comment '姓名',
    age   int          null comment '年龄',
    email varchar(50)  null comment '邮箱',
    phone varchar(20)  not null unique,
    primary key (id)
) charset = utf8mb4;


drop table if exists article;
create table article
(
    id            int auto_increment COMMENT '文章主键，唯一自增id',
    uploader_id   int COMMENT '上传者',
    title         varchar(255) not null COMMENT '标题',
    trans_title   varchar(255) COMMENT '翻译后的标题',
    original_text text         not null COMMENT '原文',
    genre         varchar(63)  not null COMMENT '类别',
    has_trans     boolean COMMENT '是否有翻译',
    create_time   datetime     NULL COMMENT '创建时间',
    update_time   datetime     NULL COMMENT '最后一次修改时间',
    primary key (id)
) charset = utf8mb4;

drop table if exists article_stats;
create table article_stats
(
    article_id        int,
    view_num          int,
    like_num          int,
    comment_num       int,
    trans_request_num int,
    primary key (article_id)
);

drop table if exists article_like;
create table article_like
(
    article_id int,
    user_id    int,
    primary key (article_id, user_id)
);

drop table if exists request_trans;
create table request_trans
(
    user_id    int,
    article_id int,
    solved     bool,
    primary key (user_id, article_id)
);

drop table if exists article_comment;
create table article_comment
(
    id              bigint auto_increment COMMENT '评论唯一自增id',
    floor           int COMMENT '楼层数',
    sender_id       int not null COMMENT '发送者id',
    article_id      int not null COMMENT '评论文章id',
    has_ref_comment bool COMMENT '是否引用评论',
    ref_comment_id  int COMMENT '评论引用的评论id',
    content         text COMMENT '评论内容',
    create_time     datetime COMMENT '发送时间',
    primary key (id)
);