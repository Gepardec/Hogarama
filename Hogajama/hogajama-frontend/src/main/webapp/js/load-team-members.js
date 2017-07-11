/**
 * 
 */

$(document).ready(function(e) {
    
    $.get("/hogajama-rs/rest/team", function(response){
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