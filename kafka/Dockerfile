FROM openjdk:8-alpine

ENV KAFKA_VERSION 2.5.0
ENV SCALA_VERSION 2.13
ENV KAFKA_DIR kafka_${SCALA_VERSION}-${KAFKA_VERSION}
ENV KAFKA_TGZ ${KAFKA_DIR}.tgz
ENV KAFKA_DOWNLOAD_URL https://downloads.apache.org/kafka/${KAFKA_VERSION}/${KAFKA_TGZ}

RUN wget ${KAFKA_DOWNLOAD_URL} -O /kafka.tgz && tar xzvf /kafka.tgz -C / \
    && mv -v /${KAFKA_DIR} /kafka && rm /kafka.tgz
RUN apk add --no-cache supervisor && mkdir -p /etc/supervisor.d
RUN apk add --no-cache bash

ADD kafka.ini zookeeper.ini /etc/supervisor.d/

EXPOSE 2181 9092

CMD supervisord -n
