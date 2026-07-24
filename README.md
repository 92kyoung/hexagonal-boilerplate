# hexagonal-boilerplate

Kotlin + Spring Boot로 헥사고날 아키텍처(포트/어댑터 패턴) 프로젝트를 시작할 때 쓰는
보일러플레이트. `Sample`이라는 최소 CRUD 예시 도메인이 패턴을 끝까지 보여주도록
포함되어 있고, DB는 H2 인메모리라 클론하고 바로 실행할 수 있다.

새 프로젝트를 시작할 땐 이 저장소를 **fork(또는 템플릿으로 사용)한 뒤 `Sample` 관련
코드를 지우고 실제 도메인으로 교체**하면 된다.

## 패키지 구조

```
domain/                       도메인 모델 (Sample, SampleNotFoundException) - 프레임워크 의존 없음
application/
  port/in/                    인바운드 포트 (CreateSampleUseCase, GetSampleUseCase, UpdateSampleUseCase, DeleteSampleUseCase)
  port/out/                   아웃바운드 포트 (SamplePersistencePort)
  service/                    유스케이스 구현체 (SampleService) - 인바운드 포트 전부를 구현
adapter/
  in/web/                     인바운드 어댑터 - REST 컨트롤러, 요청/응답 DTO, 예외 핸들러
  out/persistence/            아웃바운드 어댑터 - JPA 엔티티/리포지토리, SamplePersistencePort 구현체
config/                       Jackson 등 공통 설정
```

`in`/`out`은 Kotlin 예약어라서 **폴더명은 `in`/`out`, 패키지 선언은 `incoming`/`outgoing`**을
쓴다 (파일 위치와 패키지 문자열이 달라 보이는 건 의도된 것).

## 헥사고날 규칙

- **`domain/`은 Spring/JPA를 몰라야 한다.** import에 `org.springframework.*`, `jakarta.persistence.*`가
  보이면 계층을 잘못 둔 것.
- **`application/service/`는 포트 인터페이스만 의존한다.** `SampleJpaRepository`나
  `KafkaTemplate` 같은 구체 기술 타입을 직접 참조하지 않는다.
- **어댑터는 서로 모른다.** `adapter/in/web`이 `adapter/out/persistence`를 직접 호출하는 일은
  없고, 항상 포트(`application/port/*`)를 거친다.
- **유스케이스 하나당 인터페이스 하나**(인터페이스 분리 원칙). `CreateSampleUseCase`처럼
  작게 쪼개 두면, 어댑터가 실제로 쓰는 기능만 정확히 의존하게 되고 테스트용 가짜 구현도
  최소한으로만 만들면 된다.

## 실행

```bash
./gradlew bootRun
```

DB가 H2 인메모리라 별도 설치 없이 바로 뜬다. `http://localhost:8080/h2-console`에서
콘솔 확인 가능 (JDBC URL: `jdbc:h2:mem:boilerplate`, user: `sa`, password 없음).

```bash
curl -X POST http://localhost:8080/api/samples -H "Content-Type: application/json" -d '{"name":"hello"}'
curl http://localhost:8080/api/samples
```

## 테스트

```bash
./gradlew test
```

`SampleServiceTest`가 헥사고날의 핵심 이점을 보여준다 — `SamplePersistencePort`를
인메모리 가짜 구현으로 바꿔치기하면 `@SpringBootTest`도, DB도 없이 코어 로직만
순수 Kotlin 객체로 밀리초 단위에 테스트할 수 있다.

## 새 프로젝트로 포크할 때 체크리스트

1. `settings.gradle.kts`의 `rootProject.name`, `build.gradle.kts`의 `group` 변경
2. `com.hanati.boilerplate` 패키지를 새 프로젝트 패키지로 일괄 변경 (폴더 이동 + 패키지 선언 동시에)
3. `Sample`/`SampleJpaEntity`/`SampleController` 등 예시 코드를 지우고 실제 도메인으로 교체
4. DB를 실제 쓸 DB로 교체 (H2 → Postgres 등이면 `application.yml`의 `datasource`/`jpa.properties.hibernate.dialect` 변경, `runtimeOnly("com.h2database:h2")`를 실제 드라이버 의존성으로 교체)
5. 필요하면 `adapter/out/`에 새 아웃바운드 어댑터 추가 (Kafka 발행, 외부 API 호출 등) - 포트부터 정의하고 어댑터는 그 다음
