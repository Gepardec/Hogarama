/**
 * 
 */

$(document).ready(function (e) { 
	

    function loadChartData(max){

        if (typeof max == 'undefined'){
            max = 30;
        }

        var sensors = [];
        var sensorData = [];
        $.ajax({
            url: '/hogajama-rs/rest/sensor',
            success: function (response) {
                sensors = response;
            },
            async: false
        });

        for(var i = 0; i < sensors.length; i++){
            $.ajax({
                url: 'hogajama-rs/rest/sensor/allData?maxNumber=' + max + '&sensor=' + sensors[i],
                success: function (response) {
                    sensorData.push(response);
                },
                async: false
            });
        }
        var times = [];
        var series = [];
        for(var i = 0; i < sensorData[0].length; i++){
            times.push(sensorData[0][i]['time'].match(/T(\d\d:\d\d)/)[1]);
        }

        for(var i = 0; i < sensorData.length; i++){

            var values = [];

            for(var j = 0; j < sensorData[i].length; j++){
                values.push(sensorData[i][j]['value']);
            }

            var serie = {
                name: sensors[i] + " " + sensorData[i][0].location,
                data: values.reverse()
            };
            series.push(serie);
        }

        return [sensors, sensorData, times, series];

    }

	// load data for the first time here
    console.log("Load chart data first time");
    var [sensors, sensorData, times, series] = loadChartData();

    $("#spinner").addClass('hidden');
    $("#moisture-chart").removeClass('hidden');

    var datum = sensorData[0][0]['time'].match(/\d\d\d\d-\d\d-\d\d/)[0].split("-");
    datum = datum[2] + "." + datum[1] + "." + datum[0];

    console.log("Create chart");
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

    setInterval(function(){
        // Udate chart with the current data
        console.log("Update chart data");
        var [newSensors, newSensorData, newTimes, newSeries] = loadChartData();

        // Update time
        moistureChart.xAxis[0].setCategories(newTimes.reverse(), true);
        // Update data
        moistureChart.update({
            series: newSeries
        }, true);

        //moistureChart.redraw();

    }, 2500);
    
});
