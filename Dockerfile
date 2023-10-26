# 단계 1: 애플리케이션 빌드
# gradle:7.3.1-jdk17을 기반으로 하는 이미지 사용
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:7.3.1-jdk17 AS builder

# 작업 디렉토리 설정.
WORKDIR project

# 현재 디렉토리의 내용을 컨테이너의 작업 디렉토리에 복사
COPY . .

# Gradle에 대한 프록시 설정을 지정
RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

# 테스트를 제외하고 Gradle 빌드를 실행
RUN ./gradlew clean build -x test
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:7.3.1-jdk17

# 빌드 단계에서 생성된 jar 파일을 /opt/bdbdworkspace 디렉토리에 복사
COPY --from=builder /home/gradle/project/build/libs/bdbd.jar .

# 데이터베이스 URL 환경 변수를 설정
ENV DATABASE_URL=jdbc:mariadb://mariadb:3306/krampoline

# 애플리케이션 실행
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "bdbd.jar"]
