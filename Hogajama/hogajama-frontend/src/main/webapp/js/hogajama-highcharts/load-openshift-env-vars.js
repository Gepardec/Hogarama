/**
 *
 */

$(document).ready(function (e) {

    var namespace;

    $.ajax({
        url: '/hogajama-rs/rest/openshift/build-namespace',
        success: function (response) {
            namespace = response;
        },
        async: false
    });

    $('#openshift-namespace').text(namespace);

});
