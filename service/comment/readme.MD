## 데이터베이스 생성
- create database article;

## 게시글 테이블 (comment)
| 컬럼명               | 데이터 타입        | 설명 / 제약조건     |
|-------------------|---------------|-------------------|
| comment_id        | BIGINT        | Primary Key       |
| content           | varchar(3000) | 내용                |
| article_id        | BIGINT        | 게시판 ID(Shard Key) |
| parent_comment_id | BIGINT        | 상위 댓글 ID          |
| writer_id         | BIGINT        | 작성자 ID            |
| deleted           | BOOL          | 삭제여부              |
| created_at        | DATETIME      | 생성 시간             |

- 계층형 댓글에서는 계층 관계를 알아야하므로, 사윙 댓글의 참조 키고 필요하여 상위 댓글 ID(parent_comment_id)를 가진다


## Primary Key
- 오름차순 유니크 숫자를 만들기 위해 SnowFlask를 활용