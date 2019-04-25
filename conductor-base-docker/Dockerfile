#
# conductor:server - Netflix conductor server
#

# 0. Builder stage
FROM openjdk:8-jdk AS builder

MAINTAINER Netflix OSS <conductor@netflix.com>

# Copy the project directly onto the image
COPY . /conductor
WORKDIR /conductor

