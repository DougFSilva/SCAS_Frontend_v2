# Usando uma imagem do OpenJDK como base
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado pelo Maven/Gradle para dentro do container
COPY target/*.jar app.jar

# Expõe a porta 8080 para acesso externo
EXPOSE 8080

# Comando para rodar o Spring Boot no container
ENTRYPOINT ["java", "-jar", "app.jar"]