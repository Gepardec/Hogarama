/**
 * 
 */

$(document).ready(function (e) { 
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
            data: [5]
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
