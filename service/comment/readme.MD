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


## 댓글 데이터 삽입
- 1,200만이라 부족해 보이지만 유의미한 개수 생성

## 데이터베이스 collation
- 문자열을 정렬하고 비교하는 방법을 정의하는 설정
  - 대소문자 구분, 악센트 구분, 언어별 정렬 순서 등을 포함
    - 데이터베이스 / 테이블 / 컬럼별 설정
- select table_name, table_collation from information_schema.tables where table_schema = 'comment';

### comment 테이블 디폴트 collation
| table_name | table_collation    |
|------------|--------------------|
| comment    | utf8mb4_0900_ai_ci |

### utf8mb4_0900_ai_ci
- utf8mb4: 각 문자 최대 4바이트 utf8 지원
- 0900: 정렬 방식 버전
- ai: 악센트 비구분
- ci: 대소문자 구분

### 대소문자 구분을 위해 utf864b_bin을 사용
- 0-9 < A-Z < a-z


### Backawrd index scan
- 인덱스를 역순으로 스캔하는 것으로 인덱스 트리 leaf node 간에 연결된 양방향 포인터를 활용한다



## 댓글 수 테이블 (article_comment_count)
| 컬럼명           | 데이터 타입     | 설명 / 제약조건              |
|---------------|---------------|------------------------|
| article_id    | BIGINT        | Primary Key(Shard Key) |
| comment_count | BIGINT        | 댓글 수                   |