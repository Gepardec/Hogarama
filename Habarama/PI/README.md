# How to use Ansible to setup Raspberry Pi 

## Prerequisites

- [Ansible](https://www.ansible.com)
- [GitHub account with SSH key](https://help.github.com/articles/connecting-to-github-with-ssh/)
- Raspberry PI with sensor
- Analog to digital converter (MCP3008)

## Rasberry PI setup

1. [Install raspberrian](https://github.com/Gepardec/Hogarama/wiki/Setup)
1. Wire everything 

![alt text](https://github.com/Gepardec/Hogarama/raw/master/Habarama/PI/Sketch.png "Raspberry PI setup")

3. Update hosts in `ansible` file and vars in `host_vars` directory
3. Run: 
```
cd Habarama/PI/playbooks
ansible-playbook -i ansiblehosts setupPI.yml
ansible-playbook -i ansiblehosts installSensorClientPython.yml 
```
5. Start python client:
```
ansible-playbook -i ansiblehosts runSensorClientPython.yml
```