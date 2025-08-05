# 인기글

## Docker를 이용한 Kafka 환경 세팅
- docker run -d --name hyeok-board-kafka -p 9092:9092 apache/kafka:3.8.0

## Docker를 이용한 Kafka 환경 세팅

### Kafka 컨테이너 접속
- docker exec --workdir /opt/kafka/bin/ -it hyeok-board-kafka sh

### 토픽 생성
- hyeok-board-article
  -  ./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic hyeok-board-article -- replication-factor 1 --partitions 3
- hyeok-board-comment
  - ./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic hyeok-board-comment -- replication-factor 1 --partitions 3
- hyeok-board-like
  - ./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic hyeok-board-like --replication-factor 1 --partitions 3
- hyeok-board-view
  - ./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic hyeok-board-view -- replication-factor 1 --partitions 3

## 인기글 서비스 설계 – 스트림과 메시지 브로커 기반
- `게시글/댓글/좋아요/조회수 서비스`에서 데이터 변경 이벤트를 실시간으로 스트림 데이터로 생산
- `인기글 서비스`는 이러한 이벤트를 받아 `점수 집계 및 상위 10건 인기글 선정`
- 조회수 등 트래픽이 많은 이벤트는 일정 단위(에: 100개마다)만 반영, 정밀도보다는 효율을 중시
- 인기글 데이터는 `Redis Sorted Set`에 저장하며, 점수 기준 정렬 및 TTL(자동 삭제) 작용

### 이벤트 전달 및 처리
- 각 서비스는 `Kafka와 같은 메시지 브로커`를 활용해 이벤트를 비동기로 전달
  - 게시글/댓글/좋아요/조회수 서비스: `Producer`
  - 인기글 서비스: `Consumer`
- 메시지 브로커를 사용하면 시스템 결합도를 낮추고, 대규모 데이터도 안전하고 효율적으로 처리 가능
- 마이크로서비스 간의 이런 이벤트 기반 통신 구조를 `Event Driven Architecture`라 부름