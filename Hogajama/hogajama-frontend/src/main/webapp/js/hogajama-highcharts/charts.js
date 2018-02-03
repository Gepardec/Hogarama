/**
 * 
 */

$(document).ready(function (e) { 
	
	// load data for the first time here
    console.log("Load chart data first time");
    var [series, dateOfTheLastRecord] = loadChartData(null);
    
    if(series == null){
    	return;
    }

    $("#spinner").addClass('hidden');
    $("#moisture-chart").removeClass('hidden');
    $("#chart-subtitle").text("MESSWERTE AM " + dateOfTheLastRecord);

    console.log("Create chart");
    var moistureChart = Highcharts.chart('moisture-chart', {
        chart: {
            type: 'line'
        },
        title: {
            text: null
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            min: 0,
            max: 100,
            title: {
                text: 'Feuchtigkeit'
            },
            plotBands: [{
                   color: 'red',
                   from: 1,
                   to: 10
               },{
                   color: 'green',
                   from: 10,
                   to: 75
               },{
                   color: 'blue',
                   from: 75,
                   to: 100
               }
            ],
        },
        tooltip: {
            headerFormat: '<b>{series.name}</b><br>',
            pointFormat: '<b>Time</b> :{point.x:%H:%M:%S}<br><b>Value:</b> {point.y:.2f}'
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        series: series
    });
    
    setInterval(function(){
        // Udate chart with the current data
        console.log("Update chart data");
        loadChartData(moistureChart, true);

    }, 3000);
    
});


/**
 * AWA: I know, that I ve changed the scope, but it is better to read the main functionality
 */


function loadChartData(moistureChart, async, max){

    if (typeof max == 'undefined'){
        max = 30;
    }

	var sensors = [];
    if (typeof async == 'undefined'){

    	sensors = getSensors();
    	
    	if(sensors == null){
	     	showDataError("Could not load data. Please try again later.");
	     	return [null, null, null, null];
    	}

        if(sensors.length == 0){
        	showDataError("Could not find any data. Please try again later.");
            return [null, null, null, null];
        }
        return getDatasForChart(sensors, max);
    } 

    $.ajax({
        async: true,
        url: '/hogajama-rs/rest/sensor',
        success: function (response) {

            sensors = response;
            
            var [series] = getDatasForChart(sensors, max);
            // Update data
            moistureChart.update({
                series: series
            }, true);


        }
    });

}

function getDatasForChart(sensors, max){
	
	var sensorDatas = getSensorDatas(sensors, max);
	
    sensors = addLocationToSensor(sensors, sensorDatas);
    
    var dateOfTheLastRecord = getDateOfLastRecord(sensorDatas);
    
    sensorDatas = filteredRecordsForLastDay(sensorDatas, dateOfTheLastRecord);
    
    var series = getSeries(sensors, sensorDatas);
    
    return [series, dateOfTheLastRecord];
}

function getSensors(){
	var sensors = [];
	$.ajax({
	    async: false,
	    url: '/hogajama-rs/rest/sensor',
	    success: function (response) {
	   	 sensors = response;
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
		 console.error("Error: " + jqXHR.statusText);
	   	 sensors = null;
	    }
	});
	return sensors;
}

function addLocationToSensor(sensors, sensorDatas){
	var sensorsWithLocation = [];
	for(var j = 0; j < sensorDatas.length; j++){
		
		var sensorName = sensorDatas[j][0]['sensorName'];
		var sensorLocation = sensorDatas[j][0]['location'];
		for(var i = 0; i < sensors.length; i++){
			if(sensorName == sensors[i]){
				  var sensor = {
		           name: sensors[i],
		           location: sensorLocation
				  };
				  sensorsWithLocation.push(sensor);
				  break;
			}
		}
	}
	return sensorsWithLocation;
}

function showDataError(message){
	$("#spinner").addClass('hidden');
	$("#moisture-chart").removeClass('hidden');
	$("#moisture-chart").append( $("<h1 />").css( "text-align", "center" ).text(message));
}


function getSensorDatas(sensors, max){
	var sensorDatas = [];
	for(var i = 0; i < sensors.length; i++){
      $.ajax({
          url: '/hogajama-rs/rest/sensor/allData?maxNumber=' + max + '&sensor=' + sensors[i],
          success: function (response) {

              sensorDatas.push(response);
          },
          async: false,
      });
    }
	return sensorDatas;
}


function getDateOfLastRecord(sensorDatas){
	var dateOfTheLastRecord;
	for(var j = 0; j < sensorDatas.length; j++){
		for(var i = 0; i < sensorDatas[j].length; i++){  
			var date = getDatomFromRecord(sensorDatas[j][i]);
	        if(typeof dateOfTheLastRecord != 'undefined' && date >= dateOfTheLastRecord){
	        	dateOfTheLastRecord = date;
	        } else if(typeof dateOfTheLastRecord == 'undefined'){
	        	dateOfTheLastRecord = date;
	        }
		}
	}
	return dateOfTheLastRecord;
}

function getDatomFromRecord(sensorData){
	var date = sensorData['time'].match(/\d\d\d\d-\d\d-\d\d/)[0].split("-");
	return (date[2] + "." + date[1] + "." + date[0]);
}

function filteredRecordsForLastDay(sensorDatas, dateOfTheLastRecord){
   var filteredSensorDatas = [];
   	for(var j = 0; j < sensorDatas.length; j++){
   		var filteredSensorData = [];
        for(var i = 0; i < sensorDatas[j].length; i++){
        	var sensorData = sensorDatas[j][i];
        	var datum = getDatomFromRecord(sensorData);
        	if(datum == dateOfTheLastRecord){
        		filteredSensorData.push(sensorData);
        	}
        }
        filteredSensorDatas.push(filteredSensorData);
   	}
   return filteredSensorDatas;
}

function getSeries(sensors, sensorDatas){
   var series = [];
   for(var i = 0; i < sensorDatas.length; i++){
       var values = [];
       for(var j = 0; j < sensorDatas[i].length; j++){
           var value = [];
           var date = sensorDatas[i][j]['time'].match(/\d\d\d\d-\d\d-\d\d/)[0].split("-");
           var time = sensorDatas[i][j]['time'].match(/\d\d:\d\d:\d\d/)[0].split(":");
           value[0] = Date.UTC(date[0], date[1], date[2], time[0], time[1], time[2]);
           value[1] = sensorDatas[i][j]['value'];
           values.push(value);
       }

       var serie = {
           name: sensors[i].name + " " + sensors[i].location,
           data: values.reverse()
       };
       series.push(serie);
   }
   return series;
}
