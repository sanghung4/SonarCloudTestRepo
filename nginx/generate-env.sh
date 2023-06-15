#!/bin/sh

CONFIG_FILE=/usr/share/nginx/html/configuration.js

echo "window.__configuration__ = {" > $CONFIG_FILE
for env_var in $(printenv | grep 'REACT_APP_') ; do
  var=$(echo $env_var | awk '{split($1, a, "="); print a[1]}')
  echo "  $var: '$(eval "echo \"\$$var\"")'," >> $CONFIG_FILE
done

echo "};" >> $CONFIG_FILE

cat $CONFIG_FILE