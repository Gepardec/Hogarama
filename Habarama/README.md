# Habarama
Habarama is the RaspberryPi part of the Hogarama Project.
Sensors are connected to the RaspberryPi and a client reads the sensor data which sends it to an AcitveMQ broker.

## Ansible
Ansible is used to configure the RaspberryPi and install the client.
So for example to install the python client do the following:
ansible-playbook -i ansible/hosts ansible/playbooks/client-python/installSensorClientPython.yml

## Clients
Currently there is a working python client.
The Java client is under development.

## Rasberry
A sketch how to connect the sensors and actors with the raspberry
