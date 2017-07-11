/**
 *
 */

$(document).ready(function (e) {

    var namespace;
    $.ajax({
        url: '/hogajama-rs/rest/openshift/stage',
        success: function (response) {
            namespace = response;
        },
        async: false
    });
    $('#openshift-stage').text(namespace);

    var podId;
    $.ajax({
        url: '/hogajama-rs/rest/openshift/hostname',
        success: function (response) {
            podId = response;
        },
        async: false
    });
    $('#openshift-pod-id').text(podId);

});
