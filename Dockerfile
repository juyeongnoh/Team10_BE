# 단계 1: 애플리케이션 빌드
# Gradle 7.3.1과 JDK 17을 기반으로 하는 이미지 사용
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:7.3.1-jdk17

# 작업 디렉토리를 /home/gradle/project로 설정.
WORKDIR project

# 현재 디렉토리의 내용을 컨테이너의 /home/gradle/project 디렉토리에 복사
COPY . .

# Gradle에 대한 프록시 설정을 지정
RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

RUN gradle init

RUN gradle wrapper

# 단계 2: 애플리케이션 실행
# build
RUN ./gradlew clean build -x test

EXPOSE 8080

# 데이터베이스 URL 환경 변수를 설정
ENV DATABASE_URL=jdbc:mariadb://mariadb:3306/krampoline

# 애플리케이션 실행
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/home/gradle/project/build/libs/bdbd.jar"]