# Hogarama-Jenkins
Docker image containing Jenkins for building and deployment of Hogarama project. This image provides Docker inside itself, which allows to run any Docker container in your Jenkins build script.

## Building image
```sh
docker build -t jenkins-hogarama .
```
The subfolder **initjobs** contains initial configurations of Jenkins Jobs that will be preinstalled in Container while first run.

## Starting container
```sh
docker run -p 8180:8080 --privileged --rm -it -v /path/to/local/jenkins/config/dir:/var/lib/jenkins jenkins-hogarama
```

While starting at first time Jenkins will be configured:
- Installation of plugin "Copy artefacts"
- Installation of plugin "Green balls"
- Installation of plugin "Parametrized trigger"
- Creating of initial Jobs (import from subfolder **initjobs**)

## Configuring Jenkins
After starting container you can login with default user (init:init123). After login it is necessary to perform several manual configurations steps:
- Jenkins's Getting Start Wizard: you are free to install any additional plugins you need (like git). The simpliest way is to install all recommenden plugins.
- **IMPORTANT!** Restart Docker container
- Confugure JDK: *Manage Jenkins* > *Global Tool Configuration* > *Add JDK* > Select needed JDK Version (must be java 8), name it as _**def_jdk**_. Don't forget to accept the lisence!!
- Configure Maven:*Manage Jenkins* > *Global Tool Configuration* > *Add Maven* > Select needed Maven Version (must be >3.3), name it as _**def_mvn**_

## Configuring Jobs (if necessary)
Jobs must be fully configured after installing. If it is not the case (e.g. because of outdated configuration xml of imported jobs) there is a possibility to configure Jobs manually:
- **01-hogarama-compile** : Configure SCM in the Job (for the repo https://github.com/Gepardec/Hogarama.git)
- **02-build-image** : Configure "Copy artefacts" build step from previous job (\*\*/hogajama-pkg/\*\*)
- **02-build-image** : Configure trigger of _03-hogajama-deploy_ using param _environment=local_ or _environment=aws_

Now Jenkins is ready. Enjoy!
