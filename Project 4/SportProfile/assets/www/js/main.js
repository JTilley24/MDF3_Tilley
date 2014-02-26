$("#home").on("pageinit", function(){
	
});

$("#account").on("pageinit", function(){
	$("#sport").on("change", function(){
		if($("#sport").val() == "*Please Select Sport*"){
			$("#team").empty();
			$("#team").selectmenu("refresh", true);
		}else{
			console.log($("#sport").val());
			loadData($("#sport").val());
			$("#team").val([]);
		}
	})

});



var loadData = function(sport){
	
	$.getJSON("data/data.json", function(data){
		if(sport === "MLB"){
			displayTeam(data.MLB);
		}else if(sport === "NFL"){
			displayTeam(data.NFL);
		}else if(sport === "NHL"){
			displayTeam(data.NHL);
		}else if(sport === "NBA"){
			displayTeam(data.NBA);
		}
		
	})


};
var displayTeam = function(data){
	$("#team").empty();
	
	for(var i=0; i < data.length; i++){
		var name = data[i].location + " " + data[i].name;
		console.log(name);
		var sublist = $("<option value='"+ i +"'>"+ name + "</option>")
		sublist.appendTo("#team");
	}
	$("#team").selectmenu("refresh", true);
};