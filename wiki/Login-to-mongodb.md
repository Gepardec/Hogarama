How to connect to Mongo-DB

## With OpenShift GUI

Locate the active Mongodb pod:

1. Login to OpenShift
1. Switch to the Hogarama project
1. Navigate to the active MongoDb pod (Applications->Pods-> active mongodb pod)

Navigate to the tab **Environment** and remember the following properties:

* MONGODB_USER (default `hogajama`)
* MONGODB_PASSWORD (default `hogajama@mongodb`)
* MONGODB_DATABASE (default `hogajamadb`)

Navigate to the tab **Terminal** and execute the following command with the appropriate values:

```
mongo 127.0.0.1:27017/MONGODB_DATABASE -u MONGODB_USER -p MONGODB_PASSWORD
```

e.g:

```
sh-4.2$ mongo 127.0.0.1:27017/hogajamadb -u hogajama -p hogajama@mongodb                                                                                                                          
MongoDB shell version: 3.2.10                                                                                                                                                                     
connecting to: 127.0.0.1:27017/hogajamadb                                                                                                                                                         
>                                                                                                                                                                                                 
```
