#!/bin/sh

CONFIG_FILE=./public/configuration.js

echo "window.__configuration__ = {" > $CONFIG_FILE

while read -r line || [[ -n "$line" ]]; do
  case $line in
    "#"*) continue ;; # skip lines that are commented out
    *) key="${line%%=*}"
       value="${line#*=}"
       echo "  $key: \"$value\"," >> $CONFIG_FILE ;;
  esac
done < .env

echo "};" >> $CONFIG_FILE
