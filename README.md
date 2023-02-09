# Glol-Server

## 🙋‍♂️ 프로젝트 간단 소개
> 리그오브레전드 API를 통한 전적 갱신, 태그 조회, 내전 팀 밸런싱 기능을 제공하는 웹 서비스입니다

## 🛠 사용된 기술

- 언어 : Kotlin
- 컴파일러 : Gradle
- IDE : Intellij IDEA
- 프레임워크 : Spring Boot
- 데이터베이스 : H2
- 사용된 기술 : Riot API, Spring Data JPA, Querydsl, Coroutine, WebClient, Logger
- 테스트 기술 : Kotest, Mockk
- 테스트 패턴 : DCI (Describe, Context, It)

## 🗺 ERD

![erd](/src/resources/glol-server-erd.png)

## 프로젝트 기능

> 전적
- 전적 갱신
- 전적 단일 조회
- 전적 전체 조회

> 소환사
- 소환사 등록

> 소환사 프로필
- 소환사 프로필 조회
- 소환사 프로필 저장

> 소속
- 소속 등록
- 소속 태그 조회

> 팀
- 팀 등록
- 팀 존재 여부 확인
