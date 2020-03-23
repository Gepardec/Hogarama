#!/bin/bash
#set -ex

# Set username and password
USER="$1"
PASSWORD="$2"
BACKUP_DIR="$3"

if [ -z "$USER" ] || [ -z "$PASSWORD" ] || [ -z "$BACKUP_DIR" ] 
then
      echo "usage: $0 <USER> <PASSWORD> <BACKUP_DIR>"
      exit 1
fi

APP_NAME="rocketchat"
TIMESTAMP=`date +%F-%H%M`
BACKUP_PATH="$BACKUP_DIR/$APP_NAME-$TIMESTAMP.tgz"
mongodump -h localhost:27017 --authenticationDatabase admin -u "$USER" -p "$PASSWORD" --dumpDbUsersAndRoles -d rocketchatdb --archive=$BACKUP_DIR/archive-$TIMESTAMP.json
7z a -t7z -mx=4 -mfb=64 -md=32m -ms=on $BACKUP_PATH $BACKUP_DIR/archive-$TIMESTAMP.json
