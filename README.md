# Docker 컨테이너 구성 Springboot, mysql, (elasticsearch, kibana)

## 개발사양
```
OS : 윈도우 10
IDE : Spring Tool Suite 4 
Deploy : Docker v20.10.5
SpringBoot : 2.4.4 (lombok, mybatis, devtools)
Java : openjdk(zulu) 11
DB : MySql
```

### Prerequisites / 선행 조건

아래 사항들이 설치가 되어있어야합니다.

```
Java 설치 (OpenJDK zulu 11)
STS 다운로드
lombok.jar 설치
Docker 설치
MySQL, ElasticSearch+kibana 설치

```


### Spring 개발환경 Setup
```
lombok.jar 설치
```
![image](https://user-images.githubusercontent.com/16375921/113245267-92a51c80-92f1-11eb-9141-11e47a3d52b3.png)

```
mybatipse 플러그인 설치 (mybatis mapper.xml 에서 repository를 ctrl 키로 바로가기 가능)
```

### Docker 개발 Container Setup
```
Docker 설치
```
![image](https://user-images.githubusercontent.com/16375921/113244361-d0a14100-92ef-11eb-9d1b-ea271952d0ff.png)
![image](https://user-images.githubusercontent.com/16375921/113255973-79a56700-9303-11eb-94dd-119f9d22ab99.png)

```
mysql, elasticsearch + kibana (docker guide 참고 : https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html)
```

```
docker-elasticsearch run 에러 참고
1) max_map_count 값 오류
  wsl -d docker-desktop
  sysctl -w vm.max_map_count=262144
  
2) docker 실행하자마자 에러나는경우 bash 접근 방법
C:\Users\HP>docker images
REPOSITORY                                      TAG       IMAGE ID       CREATED       SIZE
docker101tutorial                               latest    b919573a306b   8 days ago    27.9MB
mysql                                           latest    26d0ac143221   13 days ago   546MB
docker.elastic.co/kibana/kibana                 7.12.0    7a6b1047dd48   2 weeks ago   1.05GB
docker.elastic.co/elasticsearch/elasticsearch   7.12.0    9337ed510a0c   2 weeks ago   830MB
alpine/git                                      latest    a939554ad0d0   5 weeks ago   25.1MB


C:\Users\HP>docker run -it docker.elastic.co/kibana/kibana:7.12.0 bash

```

### SpringBoot Jar 파일 Docker 이미지생성

```
아무폴더나 생성하여 Dockerfile 파일생성 (확장자 x / 작업폴더는 )

[파일내용]
FROM azul/zulu-openjdk-alpine:11.0.7
ARG JAR_FILE=./spring-boot-maven-plugin-2.5.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

[이미지 생성]
※ PowerShell 에서 작업
docker build -t starterfly/gs-spring-boot-docker .
```

### SpringBoot 컨테이너에서 Mysql 접근에 필요한 Network 구성
```
docke 공식사이트에서는 link 방식은 곧 사라질 예정으로 network 구성을 추천
```
![image](https://user-images.githubusercontent.com/16375921/113812514-320e5780-97a9-11eb-81b4-37fede685f41.png)

```
Docker-compose 파일에서 network 연결 (springboot 컨테이너에서 mysql db 컨테이너 접속)

version: "3.7" # 파일 규격 버전
services: # 이 항목 밑에 실행하려는 컨테이너 들을 정의
  db: # 서비스 명
    image: mysql # 사용할 이미지
    container_name: starterfly-mysql # 컨테이너 이름 설정
    ports:
      - "3306:3306" # 접근 포트 설정 (컨테이너 외부:컨테이너 내부)
    environment: # -e 옵션
      MYSQL_ROOT_PASSWORD: "1q2w3e4r"  # MYSQL 패스워드 설정 옵션
    command: # 명령어 실행
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
    volumes:
      - /Users/hp/datadir:/var/lib/mysql # -v 옵션 (다렉토리 마운트 설정)
    networks:     
      - corn_net
  back: # 서비스 명
    image: starterfly/gs-spring-boot-docker # 사용할 이미지
    container_name: starterfly-boot # 컨테이너 이름 설정
    networks:      
      - corn_net
    depends_on:
      - db
    ports:
      - "9091:80"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/v3sass?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC&useUnicode=true&allowPublicKeyRetrieval=true
      
networks:
  corn_net:
    driver: bridge
    
```

![image](https://user-images.githubusercontent.com/16375921/114003060-6876d000-9898-11eb-9c04-c712cbbd6f61.png)

