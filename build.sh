#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

# install UI
cd $DIR/frontend/
npm install -g create-react-app
npm install
npm run build
cd $DIR

# copy UI
rm -rf $DIR/src/main/resources/static/**

cp -r $DIR/frontend/build/*.js $DIR/src/main/resources/static/
cp -r $DIR/frontend/build/*.ico $DIR/src/main/resources/static/
cp -r $DIR/frontend/build/*.html $DIR/src/main/resources/static/
cp -r $DIR/frontend/build/*.json $DIR/src/main/resources/static/
cp -r $DIR/frontend/build/static/** $DIR/src/main/resources/static/

# build BACKEND

mvn clean package

