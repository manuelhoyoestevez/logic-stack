FROM node:10.15.3-stretch

# Prepare app directory
RUN mkdir -p /usr/src/app
ADD . /usr/src/app
WORKDIR /usr/src/app

# Install
RUN npm install

# Build
RUN ./node_modules/.bin/webpack --optimize-minimize

# Run server
CMD bash -c "npm install && ./node_modules/.bin/webpack-dev-server --open --host 0.0.0.0 --port 8080"
