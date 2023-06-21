#!/bin/bash

function mincron_tunnels () {
  sudo ssh -F ~/.ssh/config -L 449:172.16.203.2:449 reece-ecomm-dev-priv -N -f
  sudo ssh -F ~/.ssh/config -L 8476:172.16.203.2:8476 reece-ecomm-dev-priv -N -f
  sudo ssh -F ~/.ssh/config -L 8475:172.16.203.2:8475 reece-ecomm-dev-priv -N -f
  ssh -L 8100:10.200.201.14:80 reece-ecomm-dev-priv -N -f

  printf "\e[1;33mMake sure you have updated your mincron service application.properties file with the following:\n"
  printf "\tmincron_host_program_call=localhost\n"
  printf "\tmincron_host_websmart=http://localhost:8100/webSmart-apiTest\n \e[0m"
}

function eclipse_tunnel () {
  ssh -L 8999:192.168.154.106:443 reece-ecomm-dev-priv -N -f

  printf "\e[1;33mEclipse tunnel now opened\n"
  printf "Add the following to your hosts file:\n"
  printf "\t127.0.0.1 ewitest.morsco.com\n"
  printf "And update your eclipse application.properties file with the following:\n"
  printf "\teclipse_endpoint=https://ewitest.morsco.com:8999/eserv/eclipse.ecl\n\e[0m"
}

function ssh_config () {
  pemDir=$(pwd)/pems/

  echo "Host reece-ecomm-dev-bastion
    Hostname ec2-52-201-234-139.compute-1.amazonaws.com
    User ec2-user
    IdentityFile ${pemDir}bastion_dev.pem
    StrictHostKeyChecking no
    UserKnownHostsFile=/dev/null
Host reece-ecomm-dev-priv
    Hostname 10.248.1.125
    User ec2-user
    IdentityFile ${pemDir}private_host_dev.pem
    ProxyJump reece-ecomm-dev-bastion
    LocalForward 8801 127.0.0.1:8801
    LocalForward 192.168.154.106:80 127.0.0.1:80
    StrictHostKeyChecking no
    UserKnownHostsFile=/dev/null"
}

function kill_all_ssh () {
  sudo killall ssh
  echo "Killed all ssh connections"
}

function show_usage () {
  printf "Usage: $0 [options [parameters]]\n"
  printf "\n"
  printf "Options:\n"
  printf " -h|--help, Print help\n"
  printf " -m|--mincron, Start Mincron tunnels\n"
  printf " -e|--eclipse, Start Eclipse tunnel\n"
  printf " -s|--stop, Stop all ssh connections\n"
  printf " -i|--install, Install script\n"
  printf " -c|--config, Will print the required ssh config file. Use like this: './reece-ssh -c >> ~/.ssh/config'\n"

  printf "\n"

  return 0
}

while [ ! -z "$1" ];do
   case "$1" in
        -h|--help)
          show_usage
          ;;
        -m|--mincron)
          mincron_tunnels
          ;;
        -e|--eclipse)
          eclipse_tunnel
          ;;
        -i|--install)
          sudo cp $0 /usr/local/bin/reece-ssh
          ;;
        -c|--config)
          ssh_config $1
          ;;
        -s|--stop)
          shift
          kill_all_ssh
          ;;
        *)
       echo "Incorrect input provided"
       show_usage
   esac
shift
done

