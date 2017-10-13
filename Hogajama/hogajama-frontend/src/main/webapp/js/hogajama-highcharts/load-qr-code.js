/**
 *
 */

$(document).ready(function (e) {
	var qrcode = new QRCode($("#qrcode").get(0), {
        width : 500,
        height : 500
    });

    function makeCode () {
        
        var url = window.location.href;
        //document.write(url);
        
        if(url!= null && url.length>0) {
            qrcode.makeCode(url);
        }			
    }
    makeCode();

    function showQRCode() {
        var w = window.open();
        $("#qrcode").find("img").css("margin-bottom", "25px");
        var html = $("#qrcode").html();
        
        html += $("<div><div/>").html($("<span><span/>").css("font-size", "x-large").html($("#tinyurl").html())).html();

        html = $("<center><center/>").html(html);

        $(w.document.body).css("padding", "25px");
        $(w.document.body).html(html);
    }

    $(function(){
        $("a#open-qrcode").click(showQRCode);
    });
});