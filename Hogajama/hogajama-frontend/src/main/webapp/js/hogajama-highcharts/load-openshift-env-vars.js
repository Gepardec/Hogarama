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

});
