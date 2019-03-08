FROM openjdk:11-jdk-slim as build
WORKDIR /workspace

ARG component
ENV component ${component:-api}

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY common common
COPY $component $component

RUN ./gradlew -Dorg.gradle.daemon=false :$component:assemble
RUN mkdir -p $component/build/libs/dependency && (cd $component/build/libs/dependency; jar -xf ../*.jar)

FROM openjdk:11-jdk-slim
ARG component
ENV component ${component:-api}
ARG mainclass
ENV mainclass ${mainclass:-li.doerf.microstore.api.ApiApplicationKt}
VOLUME /tmp
VOLUME /log
EXPOSE 8080
COPY --from=build /workspace/${component}/build/libs/dependency/BOOT-INF/lib /app/lib
COPY --from=build /workspace/${component}/build/libs/dependency/META-INF /app/META-INF
COPY --from=build /workspace/${component}/build/libs/dependency/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","$mainclass"]
