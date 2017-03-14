import { Component, NgZone } from '@angular/core';
import { OnInit } from '@angular/core';

declare let $:any;
declare let WOW:any;

@Component({
  selector: 'app-root',
  templateUrl: './templates/team.component.html',
  styleUrls: ['./css/team.component.css']
})
export class TeamComponent implements OnInit {
  title = 'Gepardec';

  constructor(private zone: NgZone) {}

  ngOnInit(): void {
    this.zone.runOutsideAngular(() => {

      var wow = new WOW(
        {
          animateClass: 'animated',
          offset:       100
        }
      );
      wow.init();

      /*$(window).load(function(){

        $('.main-nav li a').bind('click',function(event){
          var $anchor = $(this);

          $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top - 102
          }, 1500,'easeInOutExpo');
          /*
          if you don't want to use the easing effects:
          $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top
          }, 1000);
          */
          /*event.preventDefault();
        });
      });*/


      $(window).load(function(){


        var $container = $('.portfolioContainer'),
            $body = $('body'),
            colW = 375,
            columns = null;


        $container.isotope({
          // disable window resizing
          resizable: true,
          masonry: {
            columnWidth: colW
          }
        });

        $(window).smartresize(function(){
          // check if columns has changed
          var currentColumns = Math.floor( ( $body.width() -30 ) / colW );
          if ( currentColumns !== columns ) {
            // set new column count
            columns = currentColumns;
            // apply width to container manually, then trigger relayout
            $container.width( columns * colW )
              .isotope('reLayout');
          }

        }).smartresize(); // trigger resize to set container width
        $('.portfolioFilter a').click(function(){
              $('.portfolioFilter .current').removeClass('current');
              $(this).addClass('current');

              var selector = $(this).attr('data-filter');
              $container.isotope({

                  filter: selector,
              });
              return false;
          });

      });

      $(document).ready(function(e) {

          $.get("/hogajama-rs/rest/helloworld/team-members", function(response){
            //console.log(response);
            var team_member_page = $.parseHTML(response);
            var $team_list = $(team_member_page).find(".teamlist");
      //        console.log($team_list);

              $(".team-leader-block").empty();

              var $team_leader_box_template = $('<div class="team-leader-box"> \
                    <div class="team-leader wow fadeInDown delay-03s"> \
                    <div class="team-leader-shadow"><a href="#"></a></div> \
                    <img class="teammem-img" src="" alt=""> \
                    <ul> \
                        <li><a href="#" class="fa-twitter"></a></li> \
                        <li><a href="#" class="fa-facebook"></a></li> \
                        <li><a href="#" class="fa-pinterest"></a></li> \
                        <li><a href="#" class="fa-google-plus"></a></li> \
                    </ul> \
                </div> \
                <h3 class="wow fadeInDown delay-03s">Erhard Siegl</h3> \
                <span class="wow fadeInDown delay-03s">Chief</span> \
                <p class="wow fadeInDown delay-03s"> \
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit. \
                  Proin consequat sollicitudin cursus. Dolor sit amet,\
                  consectetur adipiscing elit proin consequat.\
              </p> \
            </div>');

              $team_list.children().each(function(){

                var $team_leader_box = $team_leader_box_template.clone();
      //        	console.log($(this).find('img').attr("src"));

                // Set image url
                $team_leader_box.find('img').attr("src", $(this).find('img').attr("src"));
                // Set name
                $team_leader_box.find('h3').text($(this).find('h3').text());
                // Set position
                $team_leader_box.find('span').text($(this).find('h4').text());
                // Set info
                $team_leader_box.find('p').text($(this).find('div.teaminfospec p').text());

      //        	console.log($team_leader_box);

                $(".team-leader-block").append($team_leader_box);
              });

          });

      });

    });
  }

}
