-- 테이블 생성 --
create table article_like (
    article_like_id bigint not null primary key,
    article_id bigint not null,
    user_id bigint not null,
    created_at datetime not null
);

-- 인덱스 설정 --
create unique index idx_article_id_user_id on article_like(article_id asc, user_id asc);

create table article_like_count (
    article_id bigint not null primary key,
    like_count bigint not null,
    version bigint not null
);







-- Lock test

-- 터미널 1

-- 테이블 생성
create table lock_test (
    id bigint not null primary key,
    content varchar(100) not null
);

-- 데이터 삽입
insert into lock_test values(1234, 'test');

-- 트랜잭션 시작
start transaction;

--업데이트
update lock_test set content='test2' where id=1234;

/**
  * query lock 조회
  * 레코드에 수행한 쓰기 작업에 의해 id=1234 레코드에 EXCLUSIVE LOCK(=X LOCK)이 걸려있는 것을 확인할 수 있다
  * LOCK_TYPE=RECORD, LOCK_MODE=X, LOCK_DATA=1234
  */
select * from performance_schema.data_locks;


-- 터미널 2

-- commit이 되지 않았기에 content가 업데이트 되지 않은 상태로 출력된다.
select * from lock_test where id =1234;

-- 터미널 1의 트랜잭션 1에서 잡힌 Exclusive Lock에 의해 해제될때까지 기다려야 하는 상황으로 업데이트가 바로 안 된다
update lock_test set content='test2' where id=1234;
--ERROR 1205 (HY000): Lock wait timeout exceeded; try restarting transaction 에러가 나온다

-- 터미널 1에서 commit을 하면 터미널 2 역시 update가 수행된다.
