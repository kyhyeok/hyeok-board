## 데이터베이스 생성
- create database article_like;

## 좋아요 테이블 (article_like)
| 컬럼명             | 데이터 타입     | 설명 / 제약조건       |
|-----------------|---------------|-------------------|
| article_like_id | BIGINT        | Primary Key       |
| article_id      | BIGINT        | 게시판 ID(Shard Key) |
| user_id         | BIGINT        | 작성자 ID            |
| created_at      | DATETIME      | 생성 시간             |

- 각 사용자는 각 게시글에 1회 좋아요를 할 수 있다
  - 취소도 가능하다
- 좋아요는 가 게시글마다 1회만 수행 된다
  - 1개의 고유한 데이터만 관리되어야 한다


## Primary Key
- 오름차순 유니크 숫자를 만들기 위해 SnowFlask를 활용


## 유니크 인덱스 사용
- 게시글 ID + 사용자 ID로 유니크 인덱스를 만들면 된다