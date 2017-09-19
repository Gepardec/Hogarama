/**
 * 
 */

$(document).ready(function (e) { 
	

    function loadChartData(moistureChart, async, max){

        if (typeof max == 'undefined'){
            max = 30;
        }

        var sensors = [];
        var sensorData = [];

        if (typeof async == 'undefined'){
            $.ajax({
                async: false,
                url: '/hogajama-rs/rest/sensor',
                success: function (response) {
                    sensors = response;
                },
                error: function() {
                    $("#spinner").addClass('hidden');
                    $("#moisture-chart").removeClass('hidden');

                    $("#moisture-chart").append( $("<h1 />").css( "text-align", "center" ).text("Could not load data. Please try again later."));

                    return;
                }
            });

            if(sensors.length == 0){
                $("#spinner").addClass('hidden');
                $("#moisture-chart").removeClass('hidden');
                $("#moisture-chart").append( $("<h1 />").css( "text-align", "center" ).text("Could not find any data. Please try again later."));

                return [null, null, null, null];
            }

            for(var i = 0; i < sensors.length; i++){
                $.ajax({
                    url: 'hogajama-rs/rest/sensor/allData?maxNumber=' + max + '&sensor=' + sensors[i],
                    success: function (response) {
                        sensorData.push(response);
                    },
                    async: false,
                });
            }

            var times = [];
            var series = [];
            for(var i = 0; i < sensorData[0].length; i++){
                times.push(sensorData[0][i]['time'].match(/T(\d\d:\d\d:\d\d)/)[1]);
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

        $.ajax({
            async: true,
            url: '/hogajama-rs/rest/sensor',
            success: function (response) {

                sensors = response;

                for(var i = 0; i < sensors.length; i++){
                    $.ajax({
                        url: 'hogajama-rs/rest/sensor/allData?maxNumber=' + max + '&sensor=' + sensors[i],
                        success: function (response) {
                            sensorData.push(response);
                        },
                        async: false,
                    });
                }

                var newTimes = [];
                var newSeries = [];
                for(var i = 0; i < sensorData[0].length; i++){
                    newTimes.push(sensorData[0][i]['time'].match(/T(\d\d:\d\d:\d\d)/)[1]);
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
                    newSeries.push(serie);
                }


                // Update time
                moistureChart.xAxis[0].setCategories(newTimes.reverse(), true);
                // Update data
                moistureChart.update({
                    series: newSeries
                }, true);

                //moistureChart.redraw();

            }
        });

    }

	// load data for the first time here
    console.log("Load chart data first time");
    var [sensors, sensorData, times, series] = loadChartData(null);

    if(sensors == null){
        return;
    }

    $("#spinner").addClass('hidden');
    $("#moisture-chart").removeClass('hidden');

    var datum = sensorData[0][0]['time'].match(/\d\d\d\d-\d\d-\d\d/)[0].split("-");
    datum = datum[2] + "." + datum[1] + "." + datum[0];

    $("#chart-subtitle").text("MESSWERTE AM " + datum);

    console.log("Create chart");
    var moistureChart = Highcharts.chart('moisture-chart', {
            chart: {
                type: 'line',
                height: '40%'
            },
            title: {
                text: null
            },
        /*subtitle: {
            text: 'Source: gepardec.com'
        },*/
            xAxis: {
                categories: times.reverse()
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
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            },
            series: series,
            responsive: {
                rules: [{
                    condition: {
                        minHeight: 400
                    }
                }]
            }
        });

    setInterval(function(){
        // Udate chart with the current data
        console.log("Update chart data");
        loadChartData(moistureChart, true);

    }, 3000);
    
});
