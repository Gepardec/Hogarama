import { Component, NgZone } from '@angular/core';
import { OnInit } from '@angular/core';

declare let $:any;

@Component({
  selector: 'app-root',
  templateUrl: './templates/app.component.html',
  styleUrls: ['./css/app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Gepardec';

  constructor(private zone: NgZone) {}

  ngOnInit(): void {
    this.zone.runOutsideAngular(() => {
      $(document).ready(function(e) {
          $('#navigation').scrollToFixed();
          $('.res-nav_click').click(function(){
              $('.main-nav').slideToggle();
              return false

          });

      });
    });
  }

}
