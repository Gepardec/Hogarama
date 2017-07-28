 /**
  * 
  */
 
$(document).ready(function (e) { 
	
	var qrcode = new QRCode(document.getElementById("qrcode"), {
		width : 100,
		height : 100
	});

	function makeCode () {	
		
		var url = window.location.href; //document.getElementById(window.location)
		document.write(url);
		
		if(url!= null && url.length>0) {
			qrcode.makeCode(url);
		}			
	}

	makeCode();
	document.write("test");
    
});