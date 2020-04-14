Getting Started
#################################

Requirements
---------------

* OpenShift 4.0 or above
* Docker
* Our infrastructure secrets

In order to run Hogarama you need access to an OpenShift 4.0 cluster or above and to be able to run docker on your workstation. In addition to that you need our infrastructure secrets. The deployment script checks if it can locate the secrets file on your workstation and if it can't find it, it will tell you where to get it from.

Why OpenShift 4.0 or above you ask? We are utilizing Operators which are GA since 4.0. Before OpenShift 4.0, version 3.11 offered Operators as TechPreview. However we do not count a Tech Preview feature as a sufficient requirement for our production setup.

Docker helps abstracts the packages need for our developers and sysadmins to manage the application lifecycle. On one hand our developers and sysadmins use various operating systems from Linux, to MacOS, to Windows and by providing everything in an image we can reduce friction between the different OS types and maintain a single infrastructure workflow accross our infrastructure landscape.

Install Hogarama
-----------------

Our infrastructure code is capable of deploying individual resource :code:`amq, hogajama, fluentd, ...` or resource groups such as :code:`hogarama-all` and :code:`keycloak-all`.

To install Hogarama we will use the group for simplicity. You can however do a deployment of individual resources to e.g. upgrade only this particular resource as well. If in doubt use the :code:`--help` flag to get the latest usage messages for the script.

.. code-block:: bash

  OCP-Infrastructure/helm/scripts/hogarama.sh install --resource hogarama-all

If you need to install keycloak as well you can run

.. code-block:: bash

  OCP-Infrastructure/helm/scripts/hogarama.sh install --resource keycloak-all


Upgarde Hogarama
-----------------

.. code-block:: bash

  OCP-Infrastructure/helm/scripts/hogarama.sh upgrade --resource hogarama-all

Uninstall Hogarama
--------------------

.. code-block:: bash

  OCP-Infrastructure/helm/scripts/hogarama.sh uninstall --resource hogarama-all

