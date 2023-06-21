# ERP SSH Script

The script in this folder can be used to ssh into the Eclipse Test server and make calls from your local machine. This will be necessary to test and dev locally.

## Install

The setup process is mostly outlined in the script itself, but will be listed here as well.

1. Make sure you flag your ssh script as editable `chmod +x eclipse.sh`
1. Install with `eclipse.sh -i`. This will copy it to your bin as `reece-ssh`
1. Configure your ssh `reece-ssh -c >> ~/.ssh/config`
1. When running follow the directions to update any necessary config files

## Usage

After setup run the Eclipse SSH tunnel with `reece-ssh -e`

After setup run the Mincron SSH tunnel with `reece-ssh -m`

Both can be run at once with `reece-ssh -e -m`

To stop your connections run `reece-ssh -s`. (Requires your password)
