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
	var sensors = [];
	var sensorDaten = [];
    $.ajax({
        url: '/hogajama-rs/rest/sensor',
        success: function (response) {
            sensors = response;
        },
        async: false
    });

    for(var i = 0; i < sensors.length; i++){
        $.ajax({
            url: 'hogajama-rs/rest/sensor/allData?maxNumber=30&sensor=' + sensors[i],
            success: function (response) {
                sensorDaten.push(response);
            },
            async: false
        });
    }

    $("#spinner").addClass('hidden');
    $("#moisture-chart").removeClass('hidden');


    var times = [];
    var series = [];

    var datum = sensorDaten[0][0]['time'].match(/\d\d\d\d-\d\d-\d\d/)[0].split("-");
    datum = datum[2] + "." + datum[1] + "." + datum[0];

    for(var i = 0; i < sensorDaten[0].length; i++){
        times.push(sensorDaten[0][i]['time'].match(/T(\d\d:\d\d)/)[1]);
    }

    for(var i = 0; i < sensorDaten.length; i++){

        var values = [];

        for(var j = 0; j < sensorDaten[i].length; j++){
            values.push(sensorDaten[i][j]['value']);
        }

        var serie = {
            name: sensors[i] + " " + sensorDaten[i][0].location,
            data: values.reverse()
        };
        series.push(serie);
    }

    var moistureChart = Highcharts.chart('moisture-chart', {
            chart: {
                type: 'line'
            },
            title: {
                text: 'Feuchtigkeit Messwerte am ' + datum
            },
            subtitle: {
                text: 'Source: gepardec.com'
            },
            xAxis: {
                categories: times.reverse()
            },
            yAxis: {
                title: {
                    text: 'Feuchtigkeit'
                }
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            },
            series: series
        });
    
});
