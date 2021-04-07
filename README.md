# Springboot_Framework

1. 목적 : 데몬 프로그램 개발용 프레임워크
           - Docker container 환경에서 mysql, elasticsearch, kibana 를 구성하고
             springboot Framework 미들웨어를 개발하여, Docker 환경으로 배포
	          * 해당 미들웨어는 DB 데이터를 읽어와 주기적으로 elasticsearch로 보낸다.
	          
=========================================================================================

2. 개발환경
  - IDE : Spring Tool Suite 4 
  - Deploy : Docker v20.10.5
  - SpringBoot : 2.4.4 (lombok, mybatis, devtools)
  - Java : openjdk(zulu) 11
  - DB : MySql

=========================================================================================

3. lombok.jar 설치
![image](https://user-images.githubusercontent.com/16375921/113245267-92a51c80-92f1-11eb-9141-11e47a3d52b3.png)

=========================================================================================

4. docker 설치 (mysql)
![image](https://user-images.githubusercontent.com/16375921/113244361-d0a14100-92ef-11eb-9d1b-ea271952d0ff.png)

=========================================================================================

5. docker 에 mysql, elasticsearch + kibana (docker guide 참고 : https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html)
![image](https://user-images.githubusercontent.com/16375921/113255973-79a56700-9303-11eb-94dd-119f9d22ab99.png)

=========================================================================================

■ docker-elasticsearch run 에러 참고
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

=========================================================================================

6. STS mybatipse 플러그인 설치
  - mybatis mapper.xml 에서 repository를 ctrl 키로 바로가기 가능

7. Maven build jar
1) pom.xml 파일 수정
<groupId>kr.co</groupId>
	<artifactId>spring-boot-maven-plugin</artifactId>
	<version>2.5.0-SNAPSHOT</version>
	<name>solution</name>
	<description>project for Spring Boot</description>
	<packaging>jar</packaging>  <!-- packaging 추가 -->

2) run configurations 설정
![image](https://user-images.githubusercontent.com/16375921/113645249-7aeae100-96c1-11eb-8399-58bf93afb3a7.png)

![image](https://user-images.githubusercontent.com/16375921/113645420-d1f0b600-96c1-11eb-9265-c9eda9b66401.png)

=========================================================================================

8. docker image 생성 (windows 환경? PowerShell에서 작업)
Dockerfile 파일생성 (확장자 x / 작업폴더는 )

[파일내용]
FROM azul/zulu-openjdk-alpine:11.0.7
ARG JAR_FILE=./spring-boot-maven-plugin-2.5.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

[이미지 생성]
docker build -t starterfly/gs-spring-boot-docker .

=========================================================================================

9. docker network 구성
  - springboot container 에서 mysql container 로 접근에 필요한 환경구성

![image](https://user-images.githubusercontent.com/16375921/113812514-320e5780-97a9-11eb-81b4-37fede685f41.png)
* docke에서는 link 방식은 곧 사라질 예정이여서 network 구성을 추천함



=========================================================================================

■ Spingboot 에러 참고
1) jdbc using password:YES ERROR
해결방법 : jdbc datasource password 설정 부분 오타 확인
2021-04-01 13:25:36,579 [Thread-7] ERROR com.zaxxer.hikari.pool.HikariPool(throwPoolInitializationException:593) - HikariPool-1 - Exception during pool initialization.
java.sql.SQLException: Access denied for user 'username'@'172.18.0.1' (using password: YES)
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129) ~[mysql-connector-java-8.0.23.jar:8.0.23]
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122) ~[mysql-connector-java-8.0.23.jar:8.0.23]
	at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:833) ~[mysql-connector-java-8.0.23.jar:8.0.23]
	at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:453) ~[mysql-connector-java-8.0.23.jar:8.0.23]
	at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:246) ~[mysql-connector-java-8.0.23.jar:8.0.23]
	at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:198) ~[mysql-connector-java-8.0.23.jar:8.0.23]
	at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-3.4.5.jar:?]
	at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:358) ~[HikariCP-3.4.5.jar:?]
	at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:206) ~[HikariCP-3.4.5.jar:?]
	at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:477) [HikariCP-3.4.5.jar:?]
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:560) [HikariCP-3.4.5.jar:?]
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:115) [HikariCP-3.4.5.jar:?]
	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) [HikariCP-3.4.5.jar:?]
	at org.springframework.jdbc.datasource.DataSourceUtils.fetchConnection(DataSourceUtils.java:158) [spring-jdbc-5.3.5.jar:5.3.5]
	at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:116) [spring-jdbc-5.3.5.jar:5.3.5]
	at org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:79) [spring-jdbc-5.3.5.jar:5.3.5]
	at org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:80) [mybatis-spring-2.0.6.jar:2.0.6]
	at org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:67) [mybatis-spring-2.0.6.jar:2.0.6]
	at org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:337) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:86) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:325) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:89) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140) [mybatis-3.5.6.jar:3.5.6]
	at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:?]
	at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:?]
	at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:?]
	at java.lang.reflect.Method.invoke(Method.java:566) ~[?:?]
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:427) [mybatis-spring-2.0.6.jar:2.0.6]
	at com.sun.proxy.$Proxy53.selectList(Unknown Source) [?:?]
	at org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:224) [mybatis-spring-2.0.6.jar:2.0.6]
	at org.apache.ibatis.binding.MapperMethod.executeForMany(MapperMethod.java:147) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.binding.MapperMethod.execute(MapperMethod.java:80) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.binding.MapperProxy$PlainMethodInvoker.invoke(MapperProxy.java:152) [mybatis-3.5.6.jar:3.5.6]
	at org.apache.ibatis.binding.MapperProxy.invoke(MapperProxy.java:85) [mybatis-3.5.6.jar:3.5.6]
	at com.sun.proxy.$Proxy54.selectEmployeeHRList(Unknown Source) [?:?]
	at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:?]
	at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:?]
	at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:?]
	at java.lang.reflect.Method.invoke(Method.java:566) ~[?:?]
	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:344) [spring-aop-5.3.5.jar:5.3.5]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:198) [spring-aop-5.3.5.jar:5.3.5]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) [spring-aop-5.3.5.jar:5.3.5]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:137) [spring-tx-5.3.5.jar:5.3.5]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) [spring-aop-5.3.5.jar:5.3.5]
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:215) [spring-aop-5.3.5.jar:5.3.5]
	at com.sun.proxy.$Proxy55.selectEmployeeHRList(Unknown Source) [?:?]
	at kr.co.application.task.test.TestProducer.run(TestProducer.java:39) [classes/:?]
	at java.lang.Thread.run(Thread.java:834) [?:?]
