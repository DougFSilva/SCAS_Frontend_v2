# Usa uma imagem do Node.js para build
FROM node:22 AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Usa uma imagem do Nginx para servir o frontend
FROM nginx:alpine
COPY --from=build /app/dist/scas /usr/share/nginx/html
EXPOSE 80