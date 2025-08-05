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
