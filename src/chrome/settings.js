var privacySettings;
var userId;

var addressTooltips= {};

function updateUserInfo(){
	userDataJSON = 
	$.ajax({
		   url: "http://localhost:12564/api/v0/users/data",
		   type: "POST",
		   contentType: "application/json;charset=UTF-8",
		   data: JSON.stringify(privacySettings),
		   beforeSend: function (request)
	       {
	           request.setRequestHeader("traceid", userId);
	       },
		   success: function(response) {
				
		   }
		});
}

function doSwitchEmail() {
	if($(".switch-email").attr("class") == "switch-animate switch-email switch-off"){
		$(".switch-email").removeClass("switch-off");
		$(".switch-email").addClass("switch-on");
		privacySettings["privacy"]["email"] = 1;
	}
	else{
		$(".switch-email").removeClass("switch-on");
		$(".switch-email").addClass("switch-off");
		privacySettings["privacy"]["email"]= 0;
	}
	updateUserInfo();
}

function doSwitchGender() {
	if($(".switch-gender").attr("class") == "switch-animate switch-gender switch-off"){
		$(".switch-gender").removeClass("switch-off");
		$(".switch-gender").addClass("switch-on");
		$("#titleSetting").show();
		privacySettings["privacy"]["gender"] = 1;
	}
	else{
		$(".switch-gender").removeClass("switch-on");
		$(".switch-gender").addClass("switch-off");
		$("#titleSetting").hide();
		$("#titlePrivacy").attr("checked","false");
		privacySettings["privacy"]["gender"] = 0;
	}
	updateUserInfo();
}

function doSwitchTitle() {
	if($(".switch-title").attr("class") == "switch-animate switch-title switch-off"){
		$(".switch-title").removeClass("switch-off");
		$(".switch-title").addClass("switch-on");
		privacySettings["privacy"]["title"] = 1;
	}
	else{
		$(".switch-title").removeClass("switch-on");
		$(".switch-title").addClass("switch-off");
		privacySettings["privacy"]["title"] = 0;
	}
	updateUserInfo();
}

function triggerUpdateAddress(){
	
	url = "http://api.geonames.org/postalCodeLookupJSON?postalcode="+userInfo.address.postalcode+"&country=FR&username=eexcess.insa";
	
	$.ajax({
	   url: url,
	   type: "GET",
	   contentType: "text/json;charset=UTF-8",
	   success: function(response) {
			doUpdateAddress(response);
	   }
	});
}

function doUpdateAddress(geoname){
	addressTooltips[0] = "nothing";
	addressTooltips[1] = userInfo.address.country;
	addressTooltips[2] = geoname.postalcodes[0].adminName1;
	addressTooltips[3] = geoname.postalcodes[0].adminName3;
	addressTooltips[4] = userInfo.address.city;
	addressTooltips[5] = userInfo.address.street +", "+userInfo.address.postalcode+" "+ userInfo.address.city+", "+userInfo.address.country;
}

function initTooltips(){
	triggerUpdateAddress();
}

function initializeSettingsDisplay(){
	if(privacySettings.privacy == undefined ){
		privacySettings.privacy={};
	}
	if ( privacySettings.privacy.email == undefined ||  privacySettings.privacy.email == 1 ){
		 privacySettings.privacy.email = 1 ;
		$(".switch-email").removeClass("switch-off");
		$(".switch-email").addClass("switch-on");
	}
	else if(  privacySettings.privacy.email == 0 ){
		$(".switch-email").removeClass("switch-on");
		$(".switch-email").addClass("switch-off");
	}
	if ( privacySettings.privacy.gender == undefined ||  privacySettings.privacy.gender == 1 ){
		 privacySettings.privacy.gender = 1 ;
		 $(".switch-gender").removeClass("switch-off");
		$(".switch-gender").addClass("switch-on");
		$("#titleSetting").show();
	}
	else if(  privacySettings.privacy.gender == 0 ){
		$(".switch-gender").removeClass("switch-on");
		$(".switch-gender").addClass("switch-off");
		$("#titleSetting").hide();
		$("#titlePrivacy").attr("checked","false");
	}
	if ( privacySettings.privacy.title == undefined ||  privacySettings.privacy.title == 1 ){
		 privacySettings.privacy.title = 1 ;
		 $(".switch-title").removeClass("switch-off");
			$(".switch-title").addClass("switch-on");
	}
	else if(  privacySettings.privacy.title == 0 ){
		$(".switch-title").removeClass("switch-on");
		$(".switch-title").addClass("switch-off");
	}
}

function ageSlider(id){
	var indication = "What will be send: ";
	var age;
	var birthY = userInfo.birthdate.split('-')[0];
	var birthM = userInfo.birthdate.split('-')[1];
	var birthD = userInfo.birthdate.split('-')[2];
	
	var date = new Date();
	
	var currentY = date.getFullYear();
	var currentD = date.getDate();
	var currentM = date.getMonth()+1;
	
	age = currentY-birthY;
	if((currentM-birthM < 0) || (currentD-birthD < 0)) age - 1;
	
	switch(id){
	case 0:	age = "nothing";
			break;
	case 1: age = age - age%10 + "'s";
			break;
	case 2:	age = age+ " years";
			break;
	case 3: age = userInfo.birthdate;
			break;
	}
	
	$('.ageExample').html(indication+age);
}

function addressHoverIn(){
	$(".slider-tip").show();
}

function addressHoverOut(){
	$(".slider-tip").hide();
}


window.onload = function() {

$(document).ready(function(){
	
	initTooltips();

	$('.emailSwitch').live("click",doSwitchEmail);
	$('.genderSwitch').live("click",doSwitchGender);
	$('.titleSwitch').live("click",doSwitchTitle);  
	
	$("#slider").slider({
		value:1,
		min: 0,
		max: 3,
		step: 1,
		change: function( event, ui ) {
			ageSlider(ui.value);
		}
	});
	
	$("#sliderAddress").find(".ui-slider-handle").live("mouseenter",addressHoverIn).live("mouseleave",addressHoverOut);
	
	$("#sliderAddress").slider({
		value:2,
		min: 0,
		max: 5,
		step: 1,
		slide: function( event, ui ) {
			$(this).children(".ui-slider-handle").html('<div class="tooltip top slider-tip"><div class="tooltip-arrow"></div><div class="tooltip-inner">' + addressTooltips[ui.value] + '</div></div>');
			if(ui.value == 0){
				$('.tooltip-inner').css("margin-left","85px");
				$('.tooltip-arrow').css("margin-left","-52px");
			}
			if(ui.value == 5){
				$('.tooltip-inner').css("margin-left","-60px");
				$('.tooltip-arrow').css("margin-left","20px");
			}
		},
		stop: function(){
			$(".slider-tip").hide();
		}
	});

	$('.titleSwitch').live("click",doSwitchTitle);
	chrome.runtime.onMessage.addListener(function(message,sender,sendResponse){
		rawInfos = JSON.parse(message.responseText);
		privacySettings = rawInfos["values"];
		userId = rawInfos["id"];
		
		
		initializeSettingsDisplay();
	});
});

}
