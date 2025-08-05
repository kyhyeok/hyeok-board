-- 테이블 생성 --
create table article_view_count (
    article_id bigint not null primary key,
    view_count bigint not null
);