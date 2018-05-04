#!/bin/sh
cd ./Habarama
echo Setting up ssh and authorized keys
ansible-playbook -i ansible/hosts/template_hosts/hosts ansible/playbooks/raspberry/setupRaspberry.yml
echo ssh and key setup completed
echo configuring Network
ansible-playbook -i ansible/hosts/network_setup_hosts/hosts ansible/playbooks/raspberry/configureNetwork.yml
echo Network configuration complete
echo Installing sensor client
ansible-playbook -i ansible/hosts/template_hosts/hosts ansible/playbooks/client-python/installSensorClientPython.yml
echo client sensor setup complete
echo setting up logrotate
cd ./ansible/playbooks/ansible-logrotate
ansible-playbook -i ../../hosts -e host=192.168.100.139 -e @defaults/main.yml tasks/main.yml
