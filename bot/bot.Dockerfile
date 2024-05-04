FROM openjdk:21
COPY . /app
WORKDIR /app
ENV TELEGRAM_TOKEN=${TELEGRAM_TOKEN}
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "-Dapp.telegram-token=$TELEGRAM_TOKEN", "bot.jar"]
