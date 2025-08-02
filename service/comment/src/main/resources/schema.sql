create table comment
(
    comment_id        bigint        not null primary key,
    content           varchar(3000) not null,
    article_id        bigint        not null,
    parent_comment_id bigint,
    writer_id         bigint        not null,
    deleted           bool          not null,
    created_at        datetime      not null
);

create index idx_article_id_parent_comment_id_comment_id on comment (
    article_id asc, parent_comment_id asc, comment_id asc
);

-- 테이블 collation 설정 확인
select table_name, table_collation from information_schema.tables where table_schema = 'comment';

/**
  * 댓글 테이블 설계 - 무한 depth
  * 댓글 경로를 나타내기 위한 path, parent_comment_id 제거
  * 5 depth * 5개 문자 = varchar(25)
  * depth를 더울 늘리고 싶으면 varchar 크기를 적절히 조절
 */
create table comment_v2 (
    comment_id bigint not null primary key,
    content varchar(3000) not null,
    article_id bigint not null,
    writer_id bigint not null,
    path varchar(25) character set utf8mb4 collate utf8mb4_bin not null,
    deleted bool not null,
    created_at datetime not null
);

-- unique index로 생성해서 애플리케이션의 동시성 문제를 막아준다
create unique index idx_article_id_path on comment_v2(article_id asc, path asc);

-- 테이블 collation 설정 확인
SELECT table_name, column_name, collation_name
FROM information_schema.COLUMNS
WHERE table_schema = 'comment' and table_name = 'comment_v2' and column_name = 'path';

-- 댓글 목록 조회 - 무한 dpeth

