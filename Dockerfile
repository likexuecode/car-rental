# 多阶段构建：JDK8 + Maven 打包，JRE8 运行
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /build/target/car-rental.jar app.jar
RUN mkdir -p /app/upload
EXPOSE 8080
# 默认使用 prod（外挂 MySQL）；免费云平台把 SPRING_PROFILES_ACTIVE 设为 free（内嵌 H2）
ENTRYPOINT ["sh", "-c", "java -Xmx256m -Duser.timezone=Asia/Shanghai -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}"]
