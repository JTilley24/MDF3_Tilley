$("#home").on("pageinit", function(){
	$("#login").on("click", function(){
		$("#loginError").empty();
		var user = $("#enterUser").val();
		var password = $("#enterPassword").val();
		webview.hasUser(user);
		var check = webview.hasUser(user);
		webview.getString(check.toString());
		if(check.toString() == "true"){
			webview.getBoolean(check);
			var passcheck = webview.hasPassword(user, password);
			webview.getBoolean(passcheck);
			if(passcheck.toString() == "true"){
				webview.openProfile(user);
			}else{
				var nopass = $("<li>Password is Incorrect.</li>");
				nopass.appendTo($("#loginError"));
			}
		}else{
			var nouser = $("<li>Username is Invalid.</li>");
			nouser.appendTo($("#loginError"));
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
		validate();
		if(validate() == true){
			saveData();
			$.mobile.navigate("#home");
			$("#enterUser").val($("#user").val());
		}
	})
	
	$("#camera").on("click", function(){
		webview.getCamera();
		var timeout = 10;
		var timer = setInterval(function(){
			if(timeout == 0){
				clearInterval(timer);
			}else{
				var cameraCheck = webview.hasImage();
				webview.getString(cameraCheck.toString());
				if(cameraCheck.toString() == "true"){
					webview.getString("PICTURE");
					displayImage();
					clearInterval(timer);
				}
				timeout--;
			}
		}, 3000);
	})

});

var jsonData;
var picPath = "images/profile.png";

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

var displayImage = function(){
	var path = webview.getImage();
	picPath = path;
	$("#pic").empty();
	var profilepic = $("<img src='"+ path + "' id='#profilepic' width='50' height='50'/>")
	profilepic.appendTo("#pic");
	
}

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
	item["user"] = $("#user").val();
	item["password"] = $("#password").val();
	item["sport"] = $("#sport").val();
	item["team"] = jsonData[$("#team").val()];
	item["picture"] = picPath;
	jsonObj.push(item);
	console.log(jsonObj);
	webview.getString(JSON.stringify(jsonObj));
	webview.getAccount(JSON.stringify(jsonObj));
}

var validate = function (){
	var val = 0;
	$("#valList").empty();
	webview.getString($("#firstname").val().toString());
	if($("#firstname").val() == null || $("#firstname").val() == ""){
		webview.getString($("#firstname").val());
		var fname = $("<li>Please enter First Name.</li>");
		fname.appendTo($("#valList"));
		val++;
	}
	if($("#lastname").val() == null || $("#lastname").val() == ""){
		var lname = $("<li>Please enter Last Name.</li>");
		lname.appendTo($("#valList"));
		val++;	
	}
	if($("#user").val() == null || $("#user").val() == ""){
		var uname = $("<li>Please enter Username.</li>");
		uname.appendTo($("#valList"));
		val++;	
	}
	if($("#password").val() == null || $("#password").val() == ""){
		var pass = $("<li>Please enter Password.</li>");
		pass.appendTo($("#valList"));
		val++;	
	}
	if($("#sport").val() == "*Please Select Sport*"){
		var sname = $("<li>Please Select Sport.</li>");
		sname.appendTo($("#valList"));
		val++;	
	}
	if(val > 0){
		return false;
	}
	
	return true;
}