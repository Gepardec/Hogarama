This page describes how to use Hogarama to care for your plants.

## Configure the watering strategy

Whenever a sensor value is sent to the server, the watering component uses the following algorithm:

1. Compute the average sensor value for a sensor with name `sensorName` during the last `measureInterval` minutes
1. If the result is lower than `lowWater`, activate the actor (pump) with name `actorName` for `waterDuration` seconds.

The default for these values (for the sensor with name `GruenerGepard`) is:

```
"sensorName" : "GruenerGepard",                                                                                                                                                                  
"actorName" : "GruenerGepard",                                                                                                                                                            
"measureInterval" : 1,                                                                                                                                                                    
"lowWater" : 0.2,                                                                                                                                                                         
"waterDuration" : 5                                                                                                                                                                       
```

As for now there is no GUI tho change these values. If you want to change the values, do the following:

[Login-to-mongodb](Login-to-mongodb)

On the MongoDB prompt view the `wateringConfig` collection:

```
> db.wateringConfig.find({}).pretty()                                                                                                                                                             
{                                                                                                                                                                                                 
        "_id" : "Mock Sensor",                                                                                                                                                                    
        "className" : "com.gepardec.hogarama.domain.watering.WateringConfigData",                                                                                                                 
        "actorName" : "Mock Sensor",                                                                                                                                                              
        "measureInterval" : 1,                                                                                                                                                                    
        "lowWater" : 0.2,                                                                                                                                                                         
        "waterDuration" : 5                                                                                                                                                                       
}                                                                                                                                                                                                 
{                                                                                                                                                                                                 
        "_id" : "GruenerGepard",                                                                                                                                                                  
        "className" : "com.gepardec.hogarama.domain.watering.WateringConfigData",                                                                                                                 
        "actorName" : "GruenerGepard",                                                                                                                                                            
        "measureInterval" : 1,                                                                                                                                                                    
        "lowWater" : 0.2,                                                                                                                                                                         
        "waterDuration" : 5                                                                                                                                                                       
}  
```

or

```
> db.wateringConfig.find({"_id": "GruenerGepard"}).pretty()                                                                                                                                       
{                                                                                                                                                                                                 
        "_id" : "GruenerGepard",                                                                                                                                                                  
        "className" : "com.gepardec.hogarama.domain.watering.WateringConfigData",                                                                                                                 
        "actorName" : "GruenerGepard",                                                                                                                                                            
        "measureInterval" : 1,                                                                                                                                                                    
        "lowWater" : 0.2,                                                                                                                                                                         
        "waterDuration" : 5                                                                                                                                                                       
}
```

Then change for example the `lowWater` to 0.4. For this you may prepare a `db.wateringConfig.update(...)` command in an
editor and paste it into the terminal. Command:

```
db.wateringConfig.update(
   { "_id" : "GruenerGepard" },
{                                                                                                                                                                                                 
        "_id" : "GruenerGepard",                                                                                                                                                                  
        "className" : "com.gepardec.hogarama.domain.watering.WateringConfigData",                                                                                                                 
        "actorName" : "GruenerGepard",                                                                                                                                                            
        "measureInterval" : 1,                                                                                                                                                                    
        "lowWater" : 0.4,                                                                                                                                                                         
        "waterDuration" : 5                                                                                                                                                                       
},
   { upsert: true }
)
````

