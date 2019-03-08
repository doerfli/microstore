FROM openjdk:11-jdk-slim as build
WORKDIR /workspace

ARG component=api
ARG mainclass=li.doerf.microstore.api.ApiApplicationKt

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY common common
COPY $component $component

RUN ./gradlew -Dorg.gradle.daemon=false :$component:assemble
RUN mkdir -p $component/build/libs/dependency && (cd $component/build/libs/dependency; jar -xf ../*.jar)

FROM openjdk:11-jdk-slim
VOLUME /tmp
VOLUME /log
EXPOSE 8080
ARG DEPENDENCY=/workspace/$component/build/libs/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","$mainclass"]
