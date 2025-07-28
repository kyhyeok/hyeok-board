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