#!/bin/bash

npm run build:prod

rm -f ../main/resources/static/app.bundle.js
rm -f ../main/resources/static/index.html
mv ./build/app.bundle.js ../main/resources/static/
cp ./index.html ../main/resources/static/
rm -rf ./build/
