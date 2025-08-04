-- 테이블 생성 --
create table article
(
    article_id  bigint        not null primary key,
    title       varchar(100)  not null,
    content     varchar(3000) not null,
    board_id    bigint        not null,
    writer_id   bigint        not null,
    created_at  datetime      not null,
    modified_at datetime      not null
);

-- 인덱스 설정 --
create index idx_board_id_article_id on article (board_id asc, article_id desc);

-- 게시글 목록 조회 --
select *
from article
where board_id = 1
order by article_id desc limit 30
offset 1499970;

-- Covering Index 게시글 목록 조회 --
select board_id, article_id
from article
where board_id = 1
order by article_id desc limit 30
offset 1499970;

-- Covering Index를 활용하여 게시글 목록 조회 --
select *
from (select article_id
    from article
    where board_id = 1
    order by article_id desc limit 30
    offset 1499970) t
left join article on t.article_id = article.article_id;




-- 게시글 수 테이블
create table board_article_count (
    board_id bigint not null primary key,
    article_count bigint not null
);