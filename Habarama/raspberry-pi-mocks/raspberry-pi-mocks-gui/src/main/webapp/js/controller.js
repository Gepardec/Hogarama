angular.module('MockApp', [])
   .controller('MockController', function($scope) {
       $scope.ws = new WebSocket("ws://localhost:8080/mock");
       $scope.ws.onmessage = function (event){
    	   let data = event.data;
    	   $scope.$apply(function(){
			if(data == "ok"){
				$scope.nrOfSentMessages +=1;
			}
			   
		   });
       };
	   $scope.greeting = "Hello World";
	   $scope.intervalHandler = null;
	   
	   $scope.sendPing = function(){
		 $scope.ws.send("ping");  
	   };
	   
	   $scope.currentValue = 50;
	   $scope.changeCurrentValue = function(value){
		   $scope.currentValue += value;
		   if($scope.currentValue > 100){
			   $scope.currentValue = 100;
		   }
		   
		   if($scope.currentValue < 0){
			   $scope.currentValue = 0;
		   }
	   }
	   
	   $scope.running = false;
	   $scope.brokerUrl = "https://broker-amq-mqtt-ssl-hogarama.10.0.75.2.nip.io";
	   $scope.nrOfSentMessages = 0;
	   $scope.start = function(){
		   $scope.running = true;
		   $scope.intervalHandler = setInterval(function() {
			   $scope.ws.send("" + $scope.currentValue);  
			  
			   
		   }, 3000);
	   }
	   
	   $scope.stop = function(){
		   $scope.running = false;
		   clearInterval($scope.intervalHandler);
	   }
	   
});