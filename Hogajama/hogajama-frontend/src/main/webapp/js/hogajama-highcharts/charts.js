var maxNumberOfRecords=-1;
var chart;

$(document).ready(function (e) {
	if(isNormalGraph){
		maxNumberOfRecords = 30;
	}
	// load data for the first time here
	console.log("Create chart");
	chart = createChart();
});

function createChart(){
	//Hide chart until data
	hideChart();

    Highcharts.setOptions({
        global: {
            timezoneOffset: -2 * 60
        }
    });

	return Highcharts.chart('moisture-chart', {
        chart: {
            type: 'line',
			events : {
				load : function () {
					showChart();
					if(isNormalGraph){
						setInterval(() => {
							console.log("Update chart data");
							updateChart();
						}, 3000);
					}
				}
			}
        },
        title: {
            text: null
        },
        xAxis: {
            type: 'datetime',
            plotLines: [{
                color: '{series.color}', // Color value
                dashStyle: 'solid', // Style of the plot line. Default to solid
                value: Date.now(), // Value of where the line will appear
                width: 200 // Width of the line
            }]
        },
        yAxis: {
            min: 0,
            max: 1,
            title: {
                text: 'Feuchtigkeit'
            },
            plotBands: [{
                   color: 'red',
                   from: 0,
                   to: 0.10
               },{
                   color: 'green',
                   from: 0.10,
                   to: 0.75
               },{
                   color: 'blue',
                   from: 0.75,
                   to: 1
               }
            ],
        },
        tooltip: {
            formatter: function () {
                var s = '<b>' + this.series.name + '</b>';

                var d = new Date(this.point.x);
                s += '<br><b>Time:</b> ' + d.toLocaleDateString() + " " + d.toLocaleTimeString();
                s += '<br><b>Value:</b> ' + this.point.y;

                if(this.point.duration){
                    s += '<br><b>Duration</b>: ' + this.point.duration + ' sec';
                }

                return s;
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    format: '{y:.2f}'
                }
            }
        },
        series: loadSeries()
    });
}

function hideChart(){
	$("#spinner").removeClass('hidden');
	$("#moisture-chart").addClass('hidden');
}

function showChart(){
	$("#spinner").addClass('hidden');
	$("#moisture-chart").removeClass('hidden');
}

function loadSeries(){
	var sensors = getSensorsWithData();
    var series = getSeries(sensors);
    return series;
}

function getSensorsWithData(){
	let sensors = [];
	let sensorNames = [];

	let sensorName = getTextFromElementIfExist("sensornames option:selected");

	if(sensorName === ''){
		sensorNames = getSensorNames();
		if(sensorNames == null){
	     	showErrorMessage("Could not load data. Please try again later.");
		} else if(sensorNames.length == 0){
	    	showErrorMessage("Could not find any data. Please try again later.");
	    }
	} else {
		sensorNames.push(sensorName);
	}

	setMesswertIfNeeded();

	sensors = getSensorDataBySensorName(sensorNames);
	return sensors;
}

function setMesswertIfNeeded(){
	if(isNormalGraph){
		$("#chart-subtitle").text("MESSWERTE AM " + getDatePritty(new Date(), "."));
	}
}

function getSensorDataBySensorName(sensorNames){
	let sensors = [];
	for(let i = 0; i < sensorNames.length; i++){
		let from = getFrom();
		let to = getTo();
		let sensorDatas = getDataForSensor(sensorNames[i], from, to);
		let sensor = [];
		sensor.name = sensorNames[i];
		if(sensorDatas.length == 0){
			sensor.location = getLocationBySensorName(sensorNames[i]);
		} else {
			sensor.location = sensorDatas[0]['location'];
		}
		sensor.sensorData = sensorDatas;
		sensors.push(sensor);
	}
	return sensors;
}

function getLocationBySensorName(sensorName){
	let location = '';
	$.ajax({
	    async: false,
	    url: '/hogajama-rs/rest/sensor/location?sensorName=' + sensorName,
	    success: function (response) {
	    	location = response;
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	    	console.error("Error: " + jqXHR.statusText);
	    	location = null;
	    }
	});
	return location;
}

function getFrom(){
	return getDateISOFromElement("fromdatetimepicker");}

function getTo(){
	return getDateISOFromElement("todatetimepicker");
}

function getDateISOFromElement(id){
	let val = getTextFromElementIfExist(id);
	if(val === ""){
		return val;
	}

	let utcDate = getDateFromString(val);
	let date = new Date(utcDate);
	return date.toISOString();
}

function getTextFromElementIfExist(id){
	let value = "";
	if ( isElementExist(id) ) {
		value = $("#" + id ).val();
	}
	return value;
}

function isElementExist(id){
	return $("#" + id ).length;
}

function getSensorNames(){
	let sensorNames = [];
	$.ajax({
	    async: false,
	    url: '/hogajama-rs/rest/sensor',
	    success: function (response) {
	    	sensorNames = response;
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	    	console.error("Error: " + jqXHR.statusText);
	    	sensorNames = null;
	    }
	});
	return sensorNames;
}

function getDataForSensor(sensor, from, to){
	let sensorDatas = [];
	let onlyDataFromToday = isNormalGraph;
	$.ajax({
       url: '/hogajama-rs/rest/sensor/allData?maxNumber=' + maxNumberOfRecords + '&sensor=' + sensor + '&from=' + from + "&to=" + to + "&onlyDataFromToday=" + onlyDataFromToday,
       success: function (response) {
           sensorDatas = response;
       },
       async: false,
	});
	return sensorDatas;
}

function getWateringDataForSensor(sensor, from, to){
	let wateringData = [];
	let onlyDataFromToday = isNormalGraph;
	$.ajax({
		url: '/hogajama-rs/rest/sensor/allWateringData?maxNumber=' + maxNumberOfRecords + '&sensor=' + sensor + '&from=' + from + "&to=" + to + "&onlyDataFromToday=" + onlyDataFromToday,
		success: function (response) {
			wateringData = response;
		},
		async: false,
	});
	return wateringData;
}

function showErrorMessage(message){
	$("#spinner").addClass('hidden');
	$("#moisture-chart").empty();
	$("#moisture-chart").removeClass('hidden');
	$("#moisture-chart").append( $("<h1 />").css( "text-align", "center" ).text(message));
}

function getSeries(sensors){
   let series = [];



   for(let i = 0; i < sensors.length; i++){
       let from = getFrom();
       let to = getTo();
       let wateringData = getWateringDataForSensor(sensors[i].name, from, to);

       let values = [];
       for(let j = 0; j < sensors[i].sensorData.length; j++){
    	   let value = {};
    	   let dateStr = sensors[i].sensorData[j]['time'];
           value['x'] = getDateFromString(dateStr);
           value['y'] = sensors[i].sensorData[j]['value'];
           values.push(value);
       }

       for(let j = 0; j < wateringData.length; j++){
           let value = {};
           let dateStr = wateringData[j]['time'];
           value['x'] = getDateFromString(dateStr);
           value['y'] = 0.2;
           value['duration'] = wateringData[j]['duration'];
           value['marker'] = {
               enabled: true,
               symbol: "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAF/SURBVDhPrZJdS8JgFMeNvkIXdR19noLoC9RtF6F7sfIu6LKg1G0qdhWYUBcGgkGaL5szWGXeCAkFDgqMCnRzQ+X0HHkkassK+sEf9jznf152Ns842LC87BOUFXr8G4xYmuVFxeAk2WSFizl6/UsAJvwR9eqgqA/ixeZgPareLiWTkzT6M5xQXN1OVNv5J4D8I8DW4Y3Ji7KPhsfjlXJTXERppxsmFEgB1Gm9A5wom0wwN01t38NJSjCUaXRHySPtp+9sXirHqM0d7M6TpZ092J+SUZl7C6ew1sT8DLU7IZv37qbq5tfkkXZSdYsVFZbanWzE1OpJ7c01GXVcfQXydWrU7oQTZOO82XdNRmVJDD3U7oQVZDur98EeAFRazgJYnHgsanfij5Wvj7Rn0A2AF9tZBGP+qKpRuxMmXFoMxC87Wb03LIKTfHTvwWa8YjDB0jy1u8NHlBBZZjehtYYj43tj5wBJ5qTyHrWNxxsqLJD/X8OdoPAZ72j4P/F43gEZw3JkItFPHAAAAABJRU5ErkJggg==)"
           };
           values.push(value);
       }

       values.sort(Comparator);

       console.log(values);
       let serie = {
           name: sensors[i].name + " " + sensors[i].location,
           data: values,
           turboThreshold: 0

       };

       series.push(serie);
   }
   return series;
}

function updateChart(){
	let newSeries = loadSeries();
	if(isNormalGraph){
		chart.update({series: newSeries}, true);
	} else {
		chart.update({series: newSeries}, false);
		chart.redraw();
	}
}

function getDatePritty(date, separator){
	let dd = date.getDate();
	let mm = date.getMonth() + 1;
	let yyyy = date.getFullYear();
	return dd + separator + mm + separator + yyyy;
}

function Comparator(a, b) {
   if (a['x'] < b['x']) return -1;
   if (a['x'] > b['x']) return 1;
   return 0;
}

function getDateFromString(dateStr){
    let date = dateStr.match(/\d\d\d\d-\d\d-\d\d/)[0].split("-");
    let time = dateStr.match(/\d\d:\d\d:\d\d/)[0].split(":");
    //Javascript Date months 0-11
    return Date.UTC(date[0], (date[1] - 1), date[2], time[0], time[1], time[2]);
}
