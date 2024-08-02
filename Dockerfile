FROM ghdtjdwo126/ubuntu-preset as builder
WORKDIR /
COPY ./src/. /src/
COPY ./gradlew /
COPY ./build.gradle /
COPY ./gradle/. /gradle/
COPY ./settings.gradle /
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM ghdtjdwo126/ubuntu-preset
WORKDIR /
COPY --from=builder /build/libs/*.jar /
COPY /run.sh /
COPY run.sh run.sh
RUN chmod +x ./run.sh
CMD ["/run.sh"]