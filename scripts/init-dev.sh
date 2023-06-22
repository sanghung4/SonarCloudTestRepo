#!/bin/sh

SRC_DIR=$(pwd)

if [ ! -f "$SRC_DIR/.env" ]; then
  echo "> No '.env' file found, initializing with'.env.example'."
  cp -v "$SRC_DIR/.env.example" "$SRC_DIR/.env"
fi
