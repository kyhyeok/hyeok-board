# 스프링부트로 직접 만들면서 배우는 대규모 시스템 (게시판 - MSA)

## 학습에서의 대규모 시스템이란?
1. 복잡한 시스템을 다루는 방법
2. 병렬 처리
3. 병목 지점 최소화
4. 확장성 있는 구조

## 프로젝트 요구사항
- 대규모 데이터와 트래픽을 지탱할 수 있는 시스템을 고려한다.
    - 대규모 데이터와 트래픽을 다루기 위한 핵심 전략만 다룬다.

## 배울 수 있는 것
- 대규모 시스템에 적용할 수 있는 방법론 및 개발 전략
- Relational Database의 이해 및 활용
- In-memory Database의 이해 및 활용
- Redis에서 지원하는 다양한 연산과 자료구조
- Message Broker의 이해 및 활용

## 시스템 가정
- Distributed Relational Database - MySQL
- Distributed In-memory Database - Redis Cluster
- Distributed Event Streaming Platform - Kafka Cluster
- 학습에서는 Docker 이용하여 로컬에 개발 환경 간단히 구성

## Docker
- MySQL, Redis, Kafka를 설치하기 위해 사용


## 🛠️ 기술 스택
| 구분 | 기술 | 버전    |
|------|------|-------|
| **언어** | Java | 21    |
| **프레임워크** | Spring Boot | 3.3.2 |
| **빌드 도구** | Gradle | 8.0+  |
| **데이터베이스** | MySQL | 8.0+  |
| **파일 처리** | Apache Commons CSV | -     |
| **JSON 처리** | Jackson | -     |
| **코드 간소화** | Lombok | -     |

## Microservice Architecture
- 6개의 마이크로 서비스와 공통 서비스

### Common
- snowFlask를 활용한 

## MSA 서비스
| 구분           | 기술    |
|--------------|-------
| common       | 공통 코드 |
| article      | 게시글   |
| comment      | 댓글    |
| like         | 좋아요   |
| view         | 조회수   |
| hot-article  | 인기글   |
| article-read | 게시글   |


## Docker를 이용한 MySQL 환경 세팅
- docker pull mysql:8.0.38
- docker run --name hyeok-board-mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0.38
- docker ps로 확인
- docker exec -it kuke-board-mysql bash (컨테이너 접속)
- mysql -u root –p
  - 비밀번호 root 입력 (데이터베이스 접속)
- database tool(dataGrip)으로 접속도 가능
- ID는 PK - Auto Increment가 아닌 오름차순을 위해 고안된 알고리즘(Snowflask)를 사용