## Overview

Habarama is that part of the Hogarama Project which is responsible for collecting data and controlling gadgets, such as
humidity sensors or water pumps. The main unit, which is controlling these gadgets, is a raspberry pi. Habarama uses
MQTT to interconnect the raspberry pi units with Hogajama to collect data from sensors.

In order to set up a personal Habarama to connect to our Hogarama project you will need a raspberry pi connected to the
internet. A detailed guide on how to setup your personal raspberry pi to communicate with Hogajama and connect it as
part of the Hogarama ecosystem follows.

## Habarama Software Setup Guide

### Installing an OS on your RaspberryPi

In general we do not want to interfere with your personal choice of Operating System that you want to use on your
raspberry pi device. However, it is important to note that the current scripts expect you to run Linux on your raspberry
and have only been tested with different flavors of the raspbian
distribution (https://www.raspberrypi.org/downloads/raspbian/). In order to create a bootable SDCard using a raspbian
distribution follow installation instruction on the link supplied above. If you choose to use a different linux
distribution, please refer to an internet documentation on how to create a bootable SDCard for your chosen distro.

IMPORTANT:
As the deployment of software, configuration files and scripts in this tutorial is using Ansible it is necessary that
you are able to connect to your raspberry pi via ssh. Using raspbian, by default the option to connect to your raspberry
pi via ssh is disabled. In order to enable ssh connection insert the bootable SDCard you have created to a computer and
create a ssh-marker file named “ssh” without any file extension in the root directory of said SDCard.

In order to complete the following steps for configuring your raspberry pi using ansible it is required to first connect
to your raspberry using ssh at least once. This will add the raspberry’s fingerprint to the list of known hosts, which
is a prerequisite for ansible to be able to connect using ssh.

### Getting ready

For your convenience we have automated the setup process to get your RaspberryPi up and running with Hogarama. We have
done so using ansible in order to create a reproducable setup for everyone. It is important to note however, that our
setup scripts expect your RaspberryPi device to be running a Debian Linux variant.

Also, due to the fact that ansible is used in order to configure your RaspberryPi ansible is required on your host
machine which is used to configure the raspberry.
If you are using a Windows 10 powered machine you will need to enable and use a Linux Subsystem in order to be able to
run the commands required for setting up your raspberry pi. For detailled information on how to enable a linux subsystem
on Windows 10 see (https://docs.microsoft.com/en-us/windows/wsl/install-win10).
Following the installation of the Linux Subsystem you will need to install Ansible. (For debian based subsystem → sudo
apt-get install ansible, for others see the offcial documentation on how to use that systems package installer.

### Prerequisites

* Clone the Hogarama git repository

`git clone https://github.com/Gepardec/Hogarama`

* generate a rsa keypair and upload them to your github user

(currently only rsa-keypairs that are not protected by a passphrase are supported. If you usually use a passphrase
protected key, please generate another one)

For creating and hosting your ssh keys on github please refer to the following official
documentation (https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/). Note: that if you are
using Windows 10 using a Linux subsystem make sure to either generate the keypair in the subsystem or - if you are using
puttyGen to generate an ssh keypair - make sure that it has the correct format and copy your keys to ~/.ssh in order to
have access to them in the subsystem

### Adapting the configuration files:

1. Adapt the inventory file.

```
---
  raspberries:
    hosts:
      X.X.X.X
    vars:
      sensors:
          - name: "Pflanze"
            type: "sparkfun"
            channel: 0
            pin: 21
      actors:
          - name: "Pumpe"
            type: "GPIO"
            pin: 4
      ID_STR: "raspi-ID"
      ip_address: X.X.X.X
      SENSOR_LOCATION: "LOCATION"

  all:
    vars:
      BROKER_URLS: "[\"broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at\"]"
      client_python_dir: "{{ansible_env.HOME}}/client_python/"

    # Global vars:
      ansible_ssh_user: pi
      ansible_ssh_pass: raspberry

    # Network vars:
      SSID: "SSID"
      PSK: "Wifi-Password"
      ip_router: Y.Y.Y.Y

    # SETUP FLAGS
      update_raspberry: false
      setup_ssh: false
      setup_network: false

    # SSH Vars:
      ssh_keys:
        - https://github.com/DUMMY.keys
        - https://github.com/DUMMY_2.keys
...
```

The following things NEED to be changed in order for the script to work correctly:

* hosts: your raspberry pi's IP address
* sensors:
    + name: the plant's name
    + type: the sensor type [sparkfun]
    + channel: raspberry pi's SPI channel (default: 0)
    + pin: raspberry pi's GPIO pin which the sensor is connected to
* actors:
    + name: the name of the actor (i.e. watering pump)
    + pin: raspberry pi's GPIO pin which the relay is connected to
* ip_address: your raspberry pi's IP address
* SENSOR_LOCATION: a description of the plant's location
* Network vars: The network's SSID, the wifi password and the router's IP address
* Setup Flags
    + update_raspberry: choose if the raspberry should be updated during the setup process
    + setup_ssh: choose if ansible should connect via ssh-key to your raspberry. if false, username and password is used
    + setup_network: choose if ansible should configure your raspberry's wifi connection
* SSH Vars:
    + ssh_keys: the public keys (hosted on github) that should be allowed to access your raspberry

### Run ansible

1. run the following command:

```
ansible-playbook main.yml -i inventory.yml
```

2. you are done, your habarama is now successfully integrated in the hogarama environment. Congrats.



 


