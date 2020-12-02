#
# stage: build
#

FROM gradle:6.7.1-jdk15 AS build

# copy source files
COPY --chown=gradle:gradle . /src/h721

# build project
RUN cd /src/h721 && gradle --no-daemon contract:assemble -Dspotless.check.skip=true

#
# stage: run
#

FROM adoptopenjdk:15 as run

# copy built jar
COPY --from=build /src/h721/contract/build/libs/contract.jar /srv/h721.jar

EXPOSE 9000

WORKDIR /srv
ENTRYPOINT ["java", "-jar", "/srv/h721.jar"]
