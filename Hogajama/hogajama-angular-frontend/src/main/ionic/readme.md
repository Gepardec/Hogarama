# Setup Local Dev Environment:

1. Follow [this guide](https://ionicframework.com/docs/installation/android) to install everything needed for Ionic
1. Follow [this guide](https://ionicframework.com/docs/v3/intro/deploying/) to deploy to a device or build

##Coplications

* Gradle not found Error
  * Check the answers on [this Stackoverflow post](https://stackoverflow.com/questions/43480076/ionic-2-error-could-not-find-an-installed-version-of-gradle-either-in-android)
  * Install Gradle and add an environment variable `GRADLE_HOME`
* Android Licenses Error
  * Check the answers on [this Stackoverflow post](https://stackoverflow.com/questions/39760172/you-have-not-accepted-the-license-agreements-of-the-following-sdk-components)
  * Navigate to your `ANDROID_HOME`
  * Navigate to `tools/bin`
  * Run `sdkmanager --licenses`
  