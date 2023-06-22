#!/bin/sh

CONFIG_FILE=./public/configuration.js

echo "window.__configuration__ = {" > $CONFIG_FILE

while read env_var; do
  case $env_var in
    "#"*) ;; # skip lines that are commented out
    *)    echo $env_var | awk '{split($1, a, "="); print "  "a[1]": \""a[2]"\","}' >> $CONFIG_FILE ;;
  esac
done < .env

echo "};" >> $CONFIG_FILE
