/**
 * 
 */

$(document).ready(function (e) { 
	
	
	var habarama_system_uptime = 0;
//	$.get("/hogajama-rs/rest/helloworld/mongodb/1", function(response){
//    	//console.log(response.system_uptime);
//		if(response.system_uptime > 0) {
//			habarama_system_uptime = response.system_uptime;
//		}
//    });
	$.ajax({
        url: '/hogajama-rs/rest/helloworld/mongodb/1',
        success: function (response) {
        	//console.log(response.system_uptime);
        	if(response.system_uptime > 0) {
    			habarama_system_uptime = response.system_uptime;
    		}
        },
        async: false
    });
	
    var uptimeChart = Highcharts.chart('uptime-chart', {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Habarama Uptime'
        },
        xAxis: {
            categories: [ 'Habaramas' ]
        },
        yAxis: {
            title: {
                text: 'Up Time'
            }
        },
        series: [{
            name: 'Node 1',
            data: [2]
        }, {
            name: 'Node 2',
            data: [3]
        }, {
            name: 'Node 3 (Live Data)',
            data: [ habarama_system_uptime ]
        }]
    });
    
    var fruitChart = Highcharts.chart('fruit-chart', {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Fruit Consumption'
        },
        xAxis: {
            categories: ['Apples', 'Bananas', 'Oranges']
        },
        yAxis: {
            title: {
                text: 'Fruit eaten'
            }
        },
        series: [{
            name: 'Herbert',
            data: [1, 0, 4]
        }, {
            name: 'Adam',
            data: [5, 7, 3]
        }]
    });
    
});
