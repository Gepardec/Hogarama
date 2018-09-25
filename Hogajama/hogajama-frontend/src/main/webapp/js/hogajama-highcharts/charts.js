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
            type: 'datetime'
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
		let wateringDatas = getWateringDataForSensor(sensorNames[i], from, to);
		let sensor = [];
		sensor.name = sensorNames[i];
		if(sensorDatas.length == 0){
			sensor.location = getLocationBySensorName(sensorNames[i]);
		} else {
			sensor.location = sensorDatas[0]['location'];
		}
		sensor.sensorData = sensorDatas;
		sensor.wateringData = wateringDatas;
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
       let values = [];
       for(let j = 0; j < sensors[i].sensorData.length; j++){
    	   let value = {};
    	   let dateStr = sensors[i].sensorData[j]['time'];
           value['x'] = getDateFromString(dateStr);
           value['y'] = sensors[i].sensorData[j]['value'];
           values.push(value);
       }

       for(let j = 0; j < sensors[i].wateringData.length; j++){
           let value = {};
           let dateStr = sensors[i].wateringData[j]['time'];
           value['x'] = getDateFromString(dateStr);
           value['duration'] = sensors[i].wateringData[j]['duration'];
           value['marker'] = {
               enabled: true,
               symbol: "url(img/waterdrop.png)"
           };
           values.push(value);
       }

       values.sort(Comparator);
       for(let j = 0; j < values.length; j++){
           if(values[j]['y'] == null && j != 0){
               values[j]['y'] = values[j-1]['y'];
           }
	   }

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
