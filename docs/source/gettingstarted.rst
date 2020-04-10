Getting Started
#################################

Requirements
---------------

* OpenShift 4.0 or above
* Docker
* our infrastructure secrets

In order to run Hogarama you need access to an OpenShift 4.0 cluster or above and to be able to run docker on your workstation.

Why OpenShift 4.0 or above you ask? We are utilizing Operators which are GA since 4.0. Before OpenShift 4.0, version 3.11 offered Operators as TechPreview. However we do not count a Tech Preview feature as a sufficient requirement for our production setup.

Docker helps abstracts the packages need for our developers and sysadmins to manage the application lifecycle. On one hand our developers and sysadmins use various operating systems from Linux, to MacOS, to Windows and by providing everything in an image we can reduce friction between the different OS types and maintain a single infrastructure workflow accross our infrastructure landscape.


Install Hogarama
-----------------

install hogarama on OpenShift

Upgarde Hogarama
-----------------

run helm Upgarde


Uninstall Hogarama
--------------------

run uninstall

Debug IaaC
-----------------

run template
