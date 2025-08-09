## 데이터베이스(article) - 게시글 테이블 (article)
| 컬럼명        | 데이터 타입     | 설명 / 제약조건       |
|-------------|---------------|-------------------|
| article_id  | BIGINT        | Primary Key       |
| title       | varchar(100)  | 제목                |
| content     | varchar(3000) | 내용                |
| board_id    | BIGINT        | 게시판 ID(Shard Key) |
| writer_id   | BIGINT        | 작성자 ID            |
| created_at  | DATETIME      | 생성 시간             |
| modified_at | DATETIME      | 수정 시간             |

## index 설정
- create index idx_board_id_article_id on article (board_id asc, article_id desc);


## 데이터베이스(article) - 게시글 수 테이블 (board_article_count)
| 컬럼명           | 데이터 타입     | 설명 / 제약조건              |
|---------------|---------------|------------------------|
| board_id      | BIGINT        | Primary Key(Shard Key) |
| article_count | BIGINT        | 게시글 수                 |


## 데이터베이스(article) - outbox 테이블 (outbox)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건   |
|------------|---------------|-------------|
| outbox_id  | BIGINT        | Primary Key |
| event_type | varchar(100)  | 이벤트 타입      |
| payload    | varchar(5000) | 이벤트 데이터     |
| shard_key  | BIGINT        | 샤드 키        |
| created_at | DATETIME      | 생성일시        |

## index 설정
- create index idx_shard_key_created_at on outbox(shard_key asc, created_at asc);



## 데이터베이스(comment) - 게시글 테이블 (comment)
| 컬럼명               | 데이터 타입        | 설명 / 제약조건   |
|-------------------|---------------|-------------------|
| comment_id        | BIGINT        | Primary Key       |
| content           | varchar(3000) | 내용                |
| article_id        | BIGINT        | 게시판 ID(Shard Key) |
| parent_comment_id | BIGINT        | 상위 댓글 ID          |
| writer_id         | BIGINT        | 작성자 ID            |
| deleted           | BOOL          | 삭제여부              |
| created_at        | DATETIME      | 생성 시간             |


## index 설정
- create index idx_article_id_parent_comment_id_comment_id on comment (article_id asc, parent_comment_id asc, comment_id asc);


## 데이터베이스(comment) - 게시글 테이블 (comment_v2)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건         |
|------------|---------------|-------------------|
| comment_id | BIGINT        | Primary Key       |
| content    | varchar(3000) | 내용                |
| article_id | BIGINT        | 게시판 ID(Shard Key) |
| path       | varchar(25)   | 무한 뎁스             |
| writer_id  | BIGINT        | 작성자 ID            |
| deleted    | BOOL          | 삭제여부              |
| created_at | DATETIME      | 생성 시간             |

## index 설정
- create unique index idx_article_id_path on comment_v2(article_id asc, path asc);

## 데이터베이스(comment) - outbox 테이블 (outbox)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건   |
|------------|---------------|-------------|
| outbox_id  | BIGINT        | Primary Key |
| event_type | varchar(100)  | 이벤트 타입      |
| payload    | varchar(5000) | 이벤트 데이터     |
| shard_key  | BIGINT        | 샤드 키        |
| created_at | DATETIME      | 생성일시        |

## index 설정
- create index idx_shard_key_created_at on outbox(shard_key asc, created_at asc);


## 데이터베이스(comment) - 댓글 수 테이블 (article_comment_count)
| 컬럼명           | 데이터 타입     | 설명 / 제약조건              |
|---------------|---------------|------------------------|
| article_id    | BIGINT        | Primary Key(Shard Key) |
| comment_count | BIGINT        | 댓글 수                   |


## 데이터베이스(article_like) - 좋아요 테이블 (article_like)
| 컬럼명             | 데이터 타입     | 설명 / 제약조건       |
|-----------------|---------------|-------------------|
| article_like_id | BIGINT        | Primary Key       |
| article_id      | BIGINT        | 게시판 ID(Shard Key) |
| user_id         | BIGINT        | 작성자 ID            |
| created_at      | DATETIME      | 생성 시간             |

## index 설정
- create unique index idx_article_id_user_id on article_like(article_id asc, user_id asc);

## 데이터베이스(article_like) - 좋아요 수 테이블 (article_like_count)
| 컬럼명        | 데이터 타입     | 설명 / 제약조건              |
|------------|---------------|------------------------|
| article_id | BIGINT        | Primary Key(Shard Key) |
| like_count | BIGINT        | 좋아요 수                  |
| version    | BIGINT      | 낙관적 락 버전 컬럼            |

## 데이터베이스(article_like) - outbox 테이블 (outbox)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건   |
|------------|---------------|-------------|
| outbox_id  | BIGINT        | Primary Key |
| event_type | varchar(100)  | 이벤트 타입      |
| payload    | varchar(5000) | 이벤트 데이터     |
| shard_key  | BIGINT        | 샤드 키        |
| created_at | DATETIME      | 생성일시        |

## index 설정
- create index idx_shard_key_created_at on outbox(shard_key asc, created_at asc);


## 데이터베이스(article_view) - 게시글 뷰 테이블 (article_view_count)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건         |
|------------|---------------|------------------------|
| article_id | BIGINT        | Primary Key(Shard Key) |
| view_count | BIGINT        | 조회수                    |


## 데이터베이스(article_view) - outbox 테이블 (outbox)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건   |
|------------|---------------|-------------|
| outbox_id  | BIGINT        | Primary Key |
| event_type | varchar(100)  | 이벤트 타입      |
| payload    | varchar(5000) | 이벤트 데이터     |
| shard_key  | BIGINT        | 샤드 키        |
| created_at | DATETIME      | 생성일시        |

## index 설정
- create index idx_shard_key_created_at on outbox(shard_key asc, created_at asc);