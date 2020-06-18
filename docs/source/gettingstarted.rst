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

Our infrastructure code is capable of deploying individual resource :code:`amq, hogajama, fluentd, ...` or resource groups such as :code:`hogarama-all`, :code:`keycloak-all` or  :code:`all`.

To install Hogarama we will use the group for simplicity. You can however do a deployment of individual resources to e.g. upgrade only this particular resource as well. If in doubt use the :code:`--help` flag to get the latest usage messages for the script.

.. code-block:: bash

  OCP-Infrastructure/wrapper/hogarama_wrapper.sh install --resource hogarama-all

If you need to install keycloak as well you can run

.. code-block:: bash

  OCP-Infrastructure/wrapper/hogarama_wrapper.sh install --resource keycloak-all


Upgrade Hogarama
-----------------

.. code-block:: bash

  OCP-Infrastructure/helm/wrapper/hogarama_wrapper.sh upgrade --resource hogarama-all

Uninstall Hogarama
--------------------

.. code-block:: bash

  OCP-Infrastructure/wrapper/hogarama_wrapper.sh uninstall --resource hogarama-all

Additional Commmands
-----------------------

Additional commands can be found in the help of your shell script. Simply run the command without any options or with the :code:`--help` flag to generate the latest usage message similar to the following

.. code-block:: none

  OCP-Infrastructure/wrapper/hogarama_wrapper.sh --help
  Usage:
     hogarama.sh COMMAND --resource RESOURCE [--resource RESOURCE] [OPT ..]
        available commands:
            install)                ... installs selected resource(s) in chosen namespace
            upgrade)                ... upgrades selected resource(s) in chosen namespace
            uninstall)              ... uninstalls selected resource(s) in chosen namespace
            template)               ... executes helm template for selected resource(s)
            replace-secrets)        ... creates values.yaml file with secrets provided in 
                                        secrets/secrets.yaml
            help)                   ... this help menu

        availaible options:
            -r | --resource)        ... multiple definitions possible
                                        special resources: hogarama-all, keycloak-all, all
            -f | --force)           ... overwrites existing resources/executes helm upgrade 
                                        if installation fails
            -d | --dryrun)          ... dryrun
            -q | --quiet)           ... quiet
            -e | --extravars)       ... multiple definitions possible. Add additional/overwrite
                                        variables in values.yaml or secret.yaml
            -w | --write-template)  ... helm template output will be written to secrets 
                                        working directory
            --ns-hogarama)          ... namespace to/from which hogarama resources will be 
                                        installed/uninstalled
                                        default-value: hogarama
            --ns-keycloak)          ... namespace to/from which keycloak resources will be 
                                        installed/uninstalled
                                        default-value: gepardec

Troubelshooting
-----------------------

Sometimes it happens that you are deleting a project which still has Kubernetes resources containing a finalizer that is no longer
able to succeed. In these cases the following resources has already helped us out tremendously in the past: `Stackoverflow Thread <https://stackoverflow.com/questions/58638297/project-deletion-struck-in-terminating>`