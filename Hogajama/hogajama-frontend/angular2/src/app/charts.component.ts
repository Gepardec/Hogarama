import { Component, OnInit } from '@angular/core';

import { Habarama } from './habarama';
import { HabaramaService } from './habarama.service';

@Component({
    selector: 'app-root',
    templateUrl: './templates/charts.component.html',
})
export class ChartsComponent implements OnInit {

  habarama: Habarama;
  options: Object;

  constructor(
    private habaramaService: HabaramaService
  ) { }

  ngOnInit(): void {
    this.habaramaService
      .getHabarama(1)
      .then(habarama => this.habarama = habarama);
      // .catch(error => console.log(error));

    this.options = {
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
            data: [ this.habarama.system_uptime ]
        }]
      };

  }

}
