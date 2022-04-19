FROM openjdk:8-jre
ENV TZ=Asia/Shanghai
ADD springboot-util-0.0.1-SNAPSHOT.jar /opt/app/springboot-util.jar
ENTRYPOINT ["nohup","java","-jar","/opt/app/springboot-util.jar","&"]