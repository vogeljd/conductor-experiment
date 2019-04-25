FROM conductor-base as conductor-base-image

WORKDIR /conductor

# Build the server image
FROM node:9-alpine

RUN apk update && apk add \
  autoconf \
  automake \
  libtool \
  build-base \
  libstdc++ \
  gcc \
  abuild \
  binutils \
  nasm \
  libpng \
  libpng-dev \
  libjpeg-turbo \
  libjpeg-turbo-dev \
  python

# Copy the files for the server into the app folders
RUN mkdir -p /app/ui

# Copy the ui files onto the image
COPY ./bin /app

# Copy the project directly onto the image
COPY --from=conductor-base-image /conductor/ui /app/ui
RUN rm /app/ui/package-lock.json

#RUN mv /conductor/ui /app/
RUN chmod +x /app/startup.sh

# Get and install conductor UI
RUN cd /app/ui \
  && npm install \
  && npm run build --server

EXPOSE 5000

CMD [ "/app/startup.sh" ]
ENTRYPOINT ["/bin/sh"]
