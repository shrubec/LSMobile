

var isMobile = {
	Android : function() {
		return navigator.userAgent.match(/Android/i);
	},
	BlackBerry : function() {
		return navigator.userAgent.match(/BlackBerry/i);
	},
	iOS : function() {
		return navigator.userAgent.match(/iPhone|iPad|iPod/i);
	},
	Opera : function() {
		return navigator.userAgent.match(/Opera Mini/i);
	},
	Windows : function() {
		return navigator.userAgent.match(/IEMobile/i);
	},
	any : function() {
		return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS()
				|| isMobile.Opera() || isMobile.Windows());
	}
};


google.load("visualization", "1", {packages:["corechart"]});
function drawChart($data) {
	
	if ($data != null) {
		
		var dataArray=[];
		dataArray[0]='Description';
		dataArray[1]='Total';
		
		var chartArray=[];
		chartArray[0]=dataArray;
		
		for (var i=1; i <= $data.length; i++) {
			var dataArray1=[];
			dataArray1[0]=$data[i-1][0];
			dataArray1[1]=$data[i-1][1];
			chartArray[i]=dataArray1;
		}
		
		var chartData = google.visualization.arrayToDataTable(chartArray);
		
		var options = {
				//title: 'Simulation statistics',
				is3D: true,  
				chartArea: {left:5, top:25, width:'100%'},
				legend: {top:20,alignment:'center'},
				sliceVisibilityThreshold: 0
				
		};
		
		var chart = new google.visualization.PieChart(document.getElementById('piechart'));
		chart.draw(chartData, options);
	}
	
}

var app = angular.module('app', []);


app.controller('MainController', function($rootScope,$scope, $http) {
	
	 $rootScope.random=Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 10);
	 $rootScope.activePage='firstPage';
	 $scope.renderDesktopAdd = function() {
			 if (isMobile.any()) {
				 return false;
			 }
			 else {
				 return true;
			 }
	  };  
	  
	  $scope.renderMobileAdd = function() {
			 if (isMobile.any()) {
				 return true;
			 }
			 else {
				 return false;
			 }
	  };  
	  
	  $scope.getContentClass = function() {
			 if (isMobile.any()) {
				 return 'contentMobile';
			 }
			 else {
				 return 'contentDesktop';
			 }
	  };  
	 
	
});


/** Prva stranica*/
app.controller('InitialController', function($rootScope,$scope, $http) {

	getInitialData($scope, $http);

	function getInitialData($scope, $http) {
		$http.get('/initialSimulationParameters').success(function(data) {
			$scope.formData = data;
			$scope.formData.random=$rootScope.random;
		});
	}
	;

	$scope.processForm = function() {

		var valid = false;
		for (var i = 0; i < $scope.formData.days.length; i++) {
			valid = $scope.formData.days[i];
		}
		if (!valid) {
			alert('Please select Day of the week');
		} else {
			saveData($http, $scope).success(function() {
				$rootScope.activePage='secondPage';
			});
		}

	};

	saveData = function($http, $scope) {
		var data = $scope.formData;
		return $http({
			method : 'POST',
			url : '/saveParameters',
			contentType : "application/json",
			data : $scope.formData
		});
	};

});


/** Select brojeva*/

app.controller('NumbersController', function($rootScope,$scope,$http){
	
	getInitialData($rootScope,$scope,$http);
	$scope.numCount=0;
    
    function getInitialData($rootScope,$scope, $http) {
        $http.get('/simulationParameters').
            success(function(data) {
                $scope.simulationParameters = data;
                $scope.getPanelClass = function() {
	    			 if ($scope.numCount == $scope.simulationParameters.numbers) {
	    				 return "panel panel-success";
	    			 }
	    			 else {
	    				 return "panel panel-danger";
	    			 }
	    		  }   
	    		 
	    		 $scope.getButtonClass = function() {
   	    			 if ($scope.numCount == $scope.simulationParameters.numbers) {
   	    				 return "btn btn-primary btn-lg btn-block";
   	    			 }
   	    			 else {
   	    				 return "btn btn-primary btn-lg btn-block disabled";
   	    			 }
	    		  	}   
	    		 
	    		 $scope.getButtonClassDisabled = function() {
   	    			 if ($scope.numCount == $scope.simulationParameters.numbers) {
   	    				 return '';
   	    			 }
   	    			 else {
   	    				 return 'disabled';
   	    			 }
	    		  }   
                
            });
    };
	 
	 $scope.goBack = function() {
		 $rootScope.activePage='firstPage';
	 };
	 
	 $scope.selectNumber = function() {
		$scope.numCount=0;
		for (var i=0; i < $scope.simulationParameters.renderNumbers.length; i++) {
			 num=$scope.simulationParameters.renderNumbers[i];
		 	 if (num.selected == true) {
		 		$scope.numCount++;
		   	 }
		}
	 };
	 
	 $scope.processForm = function() {
		 saveData($http, $scope).success(function(){ 
			 	$rootScope.activePage='thirdPage';
		   	});
	};
	
	saveData  = function($http, $scope) {
		 $scope.simulationParameters.action=1;
		 var data=$scope.simulationParameters;
		 return $http({
    	        method: 'POST',
    	        url: '/initializeSimulation',
    	        contentType: "application/json",
    	     	data:$scope.simulationParameters
    	  });
	 };

});



/** Simulacija  **/

app.controller('Simulation', function($rootScope,$scope,$http,$timeout){
	
	
	var messageList = $("#messages");
    var socket = new SockJS('/stomp');
    var stompClient = Stomp.over(socket);

    stompClient.connect({ }, function(frame) {
        stompClient.subscribe("/topic/message/"+$rootScope.random, function(data) {
            var message = data.body;
             $timeout(function(){
            	$scope.simulation = JSON.parse(message);
             });
            
        });
    });			

});

app.controller('SimulationStatistics', function($rootScope,$scope,$http,$timeout){
	
	var messageList = $("#messages");

    // defined a connection to a new socket endpoint
    var socket = new SockJS('/stomp');
    var stompClient = Stomp.over(socket);

    stompClient.connect({ }, function(frame) {
        // subscribe to the /topic/message endpoint
        stompClient.subscribe("/topic/message2/"+$rootScope.random, function(data) {
            var message = data.body;
             $timeout(function(){
            	$scope.simulation = JSON.parse(message);
            	
            	drawChart($scope.simulation.currentStatistics);
            	
             });
            
        });
    });			
        

});

app.controller('SimulationButtons', function($rootScope,$scope,$http){
	

	 $scope.simulationAction='0';
	 
	 $scope.renderStart= function() {
		 if ($scope.simulationAction != '2') {
			 return true;
		 }
		 else {
			 return false;
		 }
	 };
	 
	 $scope.renderResume= function() {
		 if ($scope.simulationAction == '2') {
			 return true;
		 }
		 else {
			 return false;
		 }
	 };
	 
	 $scope.start = function() {
		 $scope.simulationAction='1';
		 sendAction($http, $scope).success(function(){ 
		   	 	console.log('start simulation...');
	   		});
	 };
	 
	 $scope.pause = function() {
		 $scope.simulationAction='2';
		 sendAction($http, $scope).success(function(){ 
		   	 	//$window.location.href = '/simulation.html';
		   	 	console.log('pause simulation');
	   		});
	 };
	 
	 $scope.next = function() {
		 $scope.simulationAction='3';
		 sendAction($http, $scope).success(function(){ 
		   	 	//$window.location.href = '/simulation.html';
		   	 	console.log('next drawing');
		   	 	$scope.simulationAction='2';
	   		});
	 };
	 
	 $scope.restart = function() {
		 $scope.simulationAction='4';
		 sendAction($http, $scope).success(function(){ 
		   	 	//$window.location.href = '/simulation.html';
		   	 	console.log('reset');
		   	 	$scope.simulationAction='0';
			   	 var chartPanel = document.getElementById('piechart');
			     chartPanel.innerHTML ='';
			     
//			     var node = document.getElementById('piechart');
//			     while (node.hasChildNodes()) {
//			         node.removeChild(node.firstChild);
//			     }
			     
	   		});
	 };
	 
	 $scope.back = function() {
		 $scope.simulationAction='5';
		 sendAction($http, $scope).success(function(){ 
		   	 	console.log('go back');
		   	 	$rootScope.activePage='firstPage';
	   		});
	 };
	 
	 
	
	sendAction  = function($http, $scope) {
		return $http({
	        method: 'GET',
	        url: '/simulationAction?action='+$scope.simulationAction+'&random='+Math.random() //Random je zbog explorera
	    });      
	}
	
	$scope.getActionButtonClass= function(buttonType) {
		
		console.log('button type ' + buttonType);
		
		if (buttonType == 1) {
			if ($scope.simulationAction == 0 || $scope.simulationAction == 2 || $scope.simulationAction == 3) {
				return "btn btn-success btn-block"
			}
			else {
				return "btn btn-success btn-block disabled"
			}
		}
		else if (buttonType == 2) {
			if ($scope.simulationAction == 1 || $scope.simulationAction == 4) {
				return "btn btn-primary btn-block"
			}
			else {
				return "btn btn-primary btn-block disabled"
			}
		}
		else if (buttonType == 3) {
			if ($scope.simulationAction != 1 && $scope.simulationAction != 4) {
				return "btn btn-primary btn-block"
			}
			else {
				return "btn btn-primary btn-block disabled"
			}					
		}
		else if (buttonType == 4) {
			return "btn btn-primary btn-block"
		}
		else if (buttonType == 5) {
			return "btn btn-default btn-block"
		}
	
	}

	
	$scope.getActionButtonClassDisabled= function(buttonType) {
		
		console.log('button type ' + buttonType);
		
		if (buttonType == 1) {
			if ($scope.simulationAction == 0 || $scope.simulationAction == 2 || $scope.simulationAction == 3) {
				console.log('test 11');
				
				
				return "";
			}
			else {
				return "disabled"
			}
		}
		else if (buttonType == 2) {
			if ($scope.simulationAction == 1 || $scope.simulationAction == 4) {
				return "";
			}
			else {
				return "disabled"
			}
		}
		else if (buttonType == 3) {
			if ($scope.simulationAction == 2) {
				return "";
			}
			else {
				return "disabled"
			}				
		}
	
	}
	
});


