// noinspection ES6ConvertVarToLetConst

// noinspection JSUnusedGlobalSymbols
function OnUpdate(doc, meta) {
    // log("original document: ", doc);
    // log("meta data: ", meta)

    try {
        // transform document
        var newDoc = transformValues(null, "", doc);

        // generate a document ID for the transformed document based on the type and the _id attribute value
        var id = generateId(newDoc, getTypeFromId(meta.id));
        //log("transformed document (id = " + id + "): ", newDoc);

        // write transformed document to target bucket with generated ID
        target[id] = newDoc;
    } catch (e) {
        log("error transforming document " + meta.id + ". See error bucket for more details.");

        // if there are any errors, store error message in the error attribute
        doc["error"] = e;

        // write untransformed document to error bucket with original ID
        error[meta.id] = doc;
    }
}

// noinspection JSUnusedGlobalSymbols
function OnDelete(meta) {
}

// This is a recursive function that will iterate over all properties in the document (including arrays & sub-objects)
// It will transform Extended JSON to standard JSON.
function transformValues(parentObj, parentProperty, obj) {
    var propertyType = "";

    // for every property in the object
    for (var property in obj) {
        if (obj.hasOwnProperty(property) && obj[property] != null) {
            switch (property) {
                case "$oid":
                    // convert parentObj.parentProperty = {"$oid":"3487634876"}
                    // to parentObj.parentProperty = "3487634876"

                    var cleanedParentProperty = parentProperty;
                    if (parentProperty.startsWith("_")) {
                        cleanedParentProperty = parentProperty.replace(/^[_]+/, '');
                        parentObj[cleanedParentProperty] = obj[property];
                        delete parentObj[parentProperty];
                    } else {
                        parentObj[parentProperty] = obj[property];
                    }
                    break;

                case "$date":
                    if (obj["$date"]["$numberLong"] != null) {
                        // convert parentObj.parentProperty = {"$date":{"$numberLong":"-2418768000000"}}
                        // to parentObj.parentProperty = -2418768000000
                        parentObj[parentProperty] = Number(obj["$date"]["$numberLong"]);
                        break;
                    }

                    // convert parentObj.parentProperty = {"$date":"1983-04-27T20:39:15Z"}}
                    // to parentObj.parentProperty = "1983-04-27T20:39:15Z"
                    parentObj[parentProperty] = obj["$date"];
                    break;

                case "$numberInt":
                case "$numberDecimal":
                case "$numberLong":
                case "$numberDouble":
                    // convert parentObj.parentProperty = {"$numberInt":"1"}
                    // to parentObj.parentProperty = 1
                    parentObj[parentProperty] = Number(obj[property]);
                    break;

                // !!! This function can be extended by adding additional case statements here !!!

                default:
                    // otherwise, check the property type
                    propertyType = determineType(obj[property]);
                    switch (propertyType) {
                        case "Object":
                            // if the property is an object, recursively transform the object
                            transformValues(obj, property, obj[property]);
                            break;

                        case "Array":
                            // if the property is an array, transform every element in the array
                            transformArray(obj[property]);
                            break;

                        default:
                            // otherwise, do nothing
                            break;
                    }
            }
        }
    }

    return obj;
}

// Determine the type of the specified object
function determineType(obj) {
    return obj == null ? "null" : obj.constructor.name;
}

// Transform every element in the specified array
function transformArray(obj) {
    for (var i = 0; i < obj.length; i++) {
        transformValues(obj, i, obj[i]);
    }
}

// Get document type from specified id.
// This function expects that documents will be imported with IDs in the following format:
// example: <document type>:12
function getTypeFromId(id) {
    return id.split(":")[0];
}

// Generate a document ID for the specified document.
// The new ID will be based on the value of the type attribute and the value of the "id" attribute:
// <type>::<id>
function generateId(document, metaId) {
    var documentId = document["id"];
    if (determineType(documentId) !== "String") {
        throw "'id' value must be a String: id = '" + documentId + "'";
    }
    return metaId + "::" + documentId;
}
