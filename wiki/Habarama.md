Habarama is a part of the Hogarama Project responsible for collecting data and controlling gadgets. It
uses [MQTT](http://mqtt.org) to interconnect the [Raspberries](https://www.raspberrypi.org) and [Hogajama](Hogajama) to
collect data from sensors.

## RasberryPI

You need a internet connected Raspberry. You can find detailed initial RaspberryPi setup [HERE](Habarama-Setup-Guide).

A sketch how to connect the sensors with the raspberry:
![RasberryPi wireing](https://github.com/Gepardec/Hogarama/blob/master/Habarama/raspberry/Sketch.png?raw=true)

Documentation for all working sensors with Habarama is [HERE](Sensors).

## Client set-up

[Ansible](https://www.ansible.com) is used to configure the RaspberryPi and install the client.
So for example to install the python client do the following:

* Modify hosts IPs ans sensor settings in `$project-repository/Habarama/ansible/hosts/template_hosts`
* Modify SSH keys in `$project-repository/Habarama/ansible/playbooks/raspberry/updateAuthorizedKeys.yml`
* Run:

```
cd $project-repository/Habarama
ansible-playbook -i ansible/hosts/template_hosts/hosts ansible/playbooks/raspberry/setupRaspberry.yml
ansible-playbook -i ansible/hosts/template_hosts/hosts ansible/playbooks/client-python/installSensorClientPython.yml
cd $project-repository/Habarama/ansible/playbooks/ansible-logrotate
ansible-playbook -i ../../hosts -e @defaults/main.yml tasks/main.yml
# Use the following command if you only want to update a specific habarama
#ansible-playbook -i ../../hosts -e host=habarama1 -e @defaults/main.yml tasks/main.yml
```

For more configuration possibilities see [Raspberry](Raspberry).


   