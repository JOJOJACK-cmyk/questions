# Spring MVC 게시판 평가 프로젝트
Java 17 / Spring Boot 3.5.5 / Gradle / MyBatis 3.0.4 / Thymeleaf.
등록, 목록, 조회, 수정, 삭제와 페이지 처리를 구현했다. 기본 DB는 파일형 H2이고 MySQL 프로필도 제공한다.
AJAX는 제시된 6문항의 개별 요구사항에 없으므로 이 프로젝트는 서버 렌더링 MVC 방식이다.

## 실행
1. JDK 17과 Gradle 8.14.3을 설치한다. 또는 IDE에서 build.gradle을 Gradle 프로젝트로 가져온다.
2. 프로젝트 루트에서 `gradle bootRun`을 실행한다.
3. 브라우저에서 http://localhost:8080 에 접속한다.
4. `gradle test`로 통합 테스트를 실행한다.

H2 DB는 실행 경로의 data/에 보관한다. schema.sql이 최초 테이블을 생성하며 기존 글은 재시작 후 유지된다.
Gradle Wrapper가 포함된 경우 Windows에서는 `gradlew.bat bootRun`, macOS/Linux에서는 `./gradlew bootRun`도 가능하다.
첫 실행에는 의존성 다운로드를 위한 인터넷 연결이 필요하다.

## MySQL 사용 (선택)
MySQL 8 이상에서 `CREATE DATABASE post_test CHARACTER SET utf8mb4;`를 실행한다.
DB_USERNAME, DB_PASSWORD, 필요시 DB_URL 환경변수를 설정한다.
`gradle bootRun --args='--spring.profiles.active=mysql'`로 실행한다.
MySQL 계정에는 테이블 생성 및 CRUD 권한이 필요하다. 비밀번호는 소스에 기록하지 않는다.

## 문항별 제출
submission/ 폴더에 문항 1~5의 제출용 ZIP과 코드 원문 TXT가 있다.
각 ZIP의 코드는 실행 프로젝트의 최종본과 같다. 문항 5는 페이징을 반영한 전체 파일이며 별도 설명에 변경 지점을 적었다.
문항 6에는 이 전체 프로젝트를 올린 실제 GitHub 저장소 URL을 제출한다.

## 요청 경로
| 메서드 | URL | 기능 |
| --- | --- | --- |
| GET | / | 메인 |
| GET | /Post/list?page=1 | 목록 및 페이징 |
| GET | /Post/read?PostNo=1 | 조회 |
| GET / POST | /Post/insert | 등록 화면 / 저장 |
| GET / POST | /Post/update?PostNo=1 | 수정 화면 / 저장 |
| POST | /Post/delete | 삭제 (폼의 PostNo 사용) |

DTO → Service → Mapper 인터페이스 → XML → DB의 계층을 사용한다.
등록·수정은 @Valid로 검증하고, 출력은 th:text로 이스케이프한다. SQL 값은 #{...} 바인딩을 사용한다.
페이지당 10건, 페이지 번호는 5개씩 표시하며 1 미만/마지막 초과 페이지는 범위 안으로 보정한다.
게시글이 없거나 삭제된 번호를 조회·수정·삭제하면 404를 반환한다.
로그인·작성자 권한·CSRF 보호는 포함하지 않은 학습용 프로젝트다.

## GitHub 제출
문항 6 제출 URL: https://github.com/JOJOJACK-cmyk/questions

이 저장소에 문항 1~5의 전체 소스와 제출용 파일을 포함했다.
Windows 실행: `gradlew.bat test` 후 `gradlew.bat bootRun`.
macOS/Linux 실행: `./gradlew test` 후 `./gradlew bootRun`.
실행에는 JDK 17이 필요하다. 현재 작성 환경의 실행 검증 제한은 검증결과.txt를 참고한다.

## 참고 공식 문서
- https://docs.spring.io/spring-boot/3.5/gradle-plugin/getting-started.html
- https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/
