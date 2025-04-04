Hogarama is not a big application. Still it is rather complex (by design) and a full Hogarama installation uses many
components from JBoss and Keycloak to Kafka and Postgres. It is rather painful to setup all these components in a
development environment. Therefore we designed a modular system that enables us to use only a minimal set of components
in a development environment, depending on which part of the application you are working on.

The following diagram shows the concept with a security component
![image](https://user-images.githubusercontent.com/1012746/185590319-a04aed30-0ff9-425a-8ddf-da7b634dd8a2.png)
For each complex componente (e.g. JWT with Keycloak) there should be a dummy component that uses only basic Java (JEE)
methods. At compile time we link either the dummy or the JWT component to the WAR application. Exactly one security
component is needed for the application to function properly. The compile-dependencies are always from the optional
module to the domain module.

From an architecture point of view we implement a kind
of [onion architecture](https://www.codeguru.com/csharp/understanding-onion-architecture/) which uses
the [inversion of control](https://en.wikipedia.org/wiki/Inversion_of_control) principle. The core of the system is the
module `hogajama-domain`, which ideally has no outbound dependencies.
The interfaces that are implemented by the optional modules are also defined in the domain module.

At the moment, we choose the optional modules at build time with help of maven profiles, activated by environment
variables. This might change in the future.

Currently the modularisation architecture is not fully implemented. More work has to be done.

**Short guidelines for extending hogarama with new features:**

* Every complex feature (a feature with aditional system requirements) should be optional in a development environment
* If the feature is needed in the domain module, implement a simple dummy-module without external dependencies that
  implements the needed interfaces
* Choosing between optional feature modules should be possible via environment variables (makes things on OpenShift
  easier)
* Compile dependencies should point only towards the domain module. Use dependency inversion via CDI if needed.