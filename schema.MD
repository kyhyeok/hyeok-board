## 게시글 테이블 (article)
| 컬럼명        | 데이터 타입     | 설명 / 제약조건       |
|-------------|---------------|-------------------|
| article_id  | BIGINT        | Primary Key       |
| title       | varchar(100)  | 제목                |
| content     | varchar(3000) | 내용                |
| board_id    | BIGINT        | 게시판 ID(Shard Key) |
| writer_id   | BIGINT        | 작성자 ID            |
| created_at  | DATETIME      | 생성 시간             |
| modified_at | DATETIME      | 수정 시간             |


## 게시글 테이블 (comment)
| 컬럼명               | 데이터 타입        | 설명 / 제약조건   |
|-------------------|---------------|-------------------|
| comment_id        | BIGINT        | Primary Key       |
| content           | varchar(3000) | 내용                |
| article_id        | BIGINT        | 게시판 ID(Shard Key) |
| parent_comment_id | BIGINT        | 상위 댓글 ID          |
| writer_id         | BIGINT        | 작성자 ID            |
| deleted           | BOOL          | 삭제여부              |
| created_at        | DATETIME      | 생성 시간             |

## 게시글 테이블 (comment_v2)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건         |
|------------|---------------|-------------------|
| comment_id | BIGINT        | Primary Key       |
| content    | varchar(3000) | 내용                |
| article_id | BIGINT        | 게시판 ID(Shard Key) |
| path       | varchar(25)   | 무한 뎁스             |
| writer_id  | BIGINT        | 작성자 ID            |
| deleted    | BOOL          | 삭제여부              |
| created_at | DATETIME      | 생성 시간             |


## 좋아요 테이블 (article_like)
| 컬럼명             | 데이터 타입     | 설명 / 제약조건       |
|-----------------|---------------|-------------------|
| article_like_id | BIGINT        | Primary Key       |
| article_id      | BIGINT        | 게시판 ID(Shard Key) |
| user_id         | BIGINT        | 작성자 ID            |
| created_at      | DATETIME      | 생성 시간             |

## 좋아요 수 테이블 (article_like_count)
| 컬럼명        | 데이터 타입     | 설명 / 제약조건              |
|------------|---------------|------------------------|
| article_id | BIGINT        | Primary Key(Shard Key) |
| like_count | BIGINT        | 좋아요 수                  |
| version    | BIGINT      | 낙관적 락 버전 컬럼            |


## 댓글 수 테이블 (article_comment_count)
| 컬럼명           | 데이터 타입     | 설명 / 제약조건              |
|---------------|---------------|------------------------|
| article_id    | BIGINT        | Primary Key(Shard Key) |
| comment_count | BIGINT        | 댓글 수                   |


## 게시글 뷰 테이블 (article_view_count)
| 컬럼명        | 데이터 타입        | 설명 / 제약조건         |
|------------|---------------|------------------------|
| article_id | BIGINT        | Primary Key(Shard Key) |
| view_count | BIGINT        | 조회수                    |