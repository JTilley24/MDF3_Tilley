$("#home").on("pageinit", function(){
	$("#login").on("click", function(){
		var email = $("#enterEmail").val();
		var password = $("#enterPassword").val();
		//webview.getLogin(email, password);
		webview.hasEmail(email);
		var check = webview.hasEmail(email);
		webview.getBoolean(check);
		if(check){
			webview.hasPassword(email, password);
		}
		
	});
});

$("#account").on("pageinit", function(){
	$("#sport").on("change", function(){
		if($("#sport").val() == "*Please Select Sport*"){
			$("#team").empty();
			$("#team").selectmenu("refresh", true);
		}else{
			loadData($("#sport").val());
			$("#team").val([]);
		}
	});
	
	$("#submit").on("click", function(){
		
		saveData();
		
	})

});

var jsonData;

var loadData = function(sport){
	
	$.getJSON("data/data.json", function(data){
		if(sport === "MLB"){
			displayTeam(data.MLB);
			jsonData = data.MLB;
		}else if(sport === "NFL"){
			displayTeam(data.NFL);
			jsonData = data.NFL;
		}else if(sport === "NHL"){
			displayTeam(data.NHL);
			jsonData = data.NHL;
		}else if(sport === "NBA"){
			displayTeam(data.NBA);
			jsonData = data.NBA;
		}
		
	})


};
var displayTeam = function(data){
	$("#team").empty();
	
	for(var i=0; i < data.length; i++){
		var name = data[i].location + " " + data[i].name;
		
		var sublist = $("<option value='"+ i +"'>"+ name + "</option>")
		sublist.appendTo("#team");
	}
	$("#team").selectmenu("refresh", true);
};

var saveData = function(){
	jsonObj = [];
	item = {};
	item["firstname"] = $("#firstname").val();
	item["lastname"] = $("#lastname").val();
	item["email"] = $("#email").val();
	item["password"] = $("#password").val();
	item["sport"] = $("#sport").val();
	item["team"] = jsonData[$("#team").val()];
	jsonObj.push(item);
	console.log(jsonObj);
	webview.getString(JSON.stringify(jsonObj));
	webview.getAccount(JSON.stringify(jsonObj));
}