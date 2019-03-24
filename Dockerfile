#= Build ============================================================
FROM openjdk:12-jdk-slim as build
WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY common common

ARG component
ENV component ${component:-api}
COPY $component $component

RUN ./gradlew -Dorg.gradle.daemon=false :$component:assemble
RUN mkdir -p $component/build/libs/dependency && (cd $component/build/libs/dependency; jar -xf ../*.jar)

#= Run ==============================================================
FROM openjdk:12-jdk-slim

VOLUME /tmp
VOLUME /log
EXPOSE 8080

ARG component
ENV component ${component:-api}

COPY --from=build /workspace/${component}/build/libs/dependency/BOOT-INF/lib /app/lib
COPY --from=build /workspace/${component}/build/libs/dependency/META-INF /app/META-INF
COPY --from=build /workspace/${component}/build/libs/dependency/BOOT-INF/classes /app

ARG mainclass
ENV mainclass ${mainclass:-li.doerf.microstore.api.ApiApplicationKt}
ENTRYPOINT java -cp app:app/lib/* $mainclass
