#
# conductor:server - Netflix conductor server
#

# 0. Builder stage
FROM conductor-base AS conductor-base-image

WORKDIR /conductor
# Run gradle build
RUN ./gradlew build -x test

# Build the server image
FROM registry.imanage.com/m-java8:1.0.16

MAINTAINER Saurabh Saxena <saurabh.saxena@imanage.com>
ARG ATLAS_IMAGE_VERSION
LABEL ATLAS_IMAGE_VERSION=${ATLAS_IMAGE_VERSION}

# Make app folders
RUN mkdir -p /app/config /app/logs /app/libs

# Copy the project directly onto the image
COPY --from=conductor-base-image /conductor/docker/server/bin /app
COPY --from=conductor-base-image /conductor/docker/server/config /app/config
COPY --from=conductor-base-image /conductor/server/build/libs/conductor-server-*-all.jar /app/libs

# Copy the files for the server into the app folders
RUN chmod +x /app/startup.sh

EXPOSE 8080
EXPOSE 8090

CMD [ "/app/startup.sh" ]
ENTRYPOINT [ "/bin/sh"]



