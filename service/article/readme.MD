## 데이터베이스 생성
- create database article;

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

- 분산 데이터베이스를 가정하기 때문에 샤딩을 직접 구현하진 않지만 N개의 샤드로 분산되는 상황을 고려하여 Shard Key = board_id(게시판 ID)로 가정
- 게시글은 게시판 다뉭로 서비스를 이요하기에 게시판 단위로 게시글 목록이 조회되기 위해서 Shard Key가 게시판 ID로 설정


## Primary Key
- 오름차순 유니크 숫자를 만들기 위해 SnowFlask를 활용

## 개시글 데이터 삽입
- 1,200만이라 부족해 보이지만 유의미한 개수 생성

## 게시글 목록 조회
### select * from article where board_id = 1 order by article_id desc limit 30 offset 90;
- 1번 게시판, 4번 페이지에서 30건 데이터를 조회해도 N초가 소요되므로 인덱스롤 사용하는 것이 좋다.
  - create index idx_board_id_article_id on article (board_id asc, article_id desc);
- 조회한 query를 explain을 해보는 것도 좋다.
- 
### select * from article where board_id = 1 order by article_id desc limit 30 offset 1499970;
- offset 1499970개를 스캔 & 버리고, 30개를 추출. 즉, 1,499,970번 인덱스를 따라가서 실제 데이터도 30번 추출
- 
### select board_id, article_id from article where board_id = 1 order by article_id desc limit 30 offset 1499970;
- 인덱스의 데이터만으로 조회하는 Covering Index가 적용되서 query 수정
- 
### select * from (  select article_id from article where board_id = 1 order by article_id desc limit 30 offset 1499970 ) t left join article on t.article_id = article.article_id;
- covering Index를 활용하여 30건의 article_id를 sub query의 결과로 만들고, article 테이블과 join하여 데이터 조회
- 
### select * from (  select article_id from article where board_id = 1 order by article_id desc limit 30 offset 8999970 ) t left join article on t.article_id = article.article_id;
- 300,000번 페이지를 조회한다고 가정한 경우에는 테이블을 분리하는 방법도 있다.
  - 하지만 300,000번 페이지를 조회하는 것이 정상적일 사용자일까?를 생각했을 때 정책적으로 풀어내거나 텍스트 검색 기능을 활용하는 방법도 있다.