.. hogarama documentation master file, created by
   sphinx-quickstart on Fri Apr  3 11:20:11 2020.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

.. # define a hard line break for HTML
.. |br| raw:: html

   <br />

.. image:: https://img.shields.io/travis/com/gepardec/hogarama?style=flat-square
   :alt: cicd badge
   :align: left
   :target: https://travis-ci.com/Gepardec/hogarama

.. image:: https://img.shields.io/badge/license-GPL%20v3.0-brightgreen.svg?style=flat-square
   :alt: licence badge
   :align: left

.. image:: https://img.shields.io/maintenance/yes/2020?style=flat-square
   :alt: maintenance badge
   :align: left

.. image:: https://www.gepardec.com/files/gepardec_logo_light_background@2000w.png
   :width: 100px
   :alt: gepardec
   :align: right

|br|

Welcome to Hogarama's documentation!
======================================

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Contents

   gettingstarted
   habarama
   hogajama
   documentation

First steps
----------------

We use our *Home and Garden Automation* to water our office plants. As all good applications have a catchy name, we came up with an abbreviation and started to call our application "Hogarama". Hogarama is a distributed application consisting of multiple components. Managing all the components can be cumbersome, hence we use a few tools to make managing Hogarama a breeze. With *Helm* we orchestrate our infrastructure resources on Openshift, with *docker* we abstract the required packages a developer or sysadmin needs installed on his workstation and with *Bash* we provide a cli interface for our users to simplify application lifecycle management.

Next up you can take a look at our :doc:`Getting started <gettingstarted>` guide on how to install Hogarama on OpenShift

Documentation
-----------------

We need to be able to understand the application, its components, lifecycle management, cicd and much more. In addition to the general awareness we want to make it simple for new colleagues to get started with Hogarama. Hence documentation is key. Here is how we do :doc:`Documentation <documentation>` for Hogarama.