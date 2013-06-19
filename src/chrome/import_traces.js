function traces() {
	var url = "http://localhost:8080/user/traces";
	var method = 'GET';
	var async = false;
	var request = new XMLHttpRequest();
	
	request.open(method, url, async);	
	request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	request.send(null);
	
	var traces = request.responseText;
	
	var tracesJson = JSON.parse(traces);
	
	var sources = tracesJson["hits"].hits;
	
	var nbTraces = tracesJson["hits"].total;
	
	document.getElementById('list_trace').innerHTML = nbTraces + " pages dans l'historique";
	if(nbTraces>50) nbTraces = 50;
	
	for(var i=0;i<nbTraces;i++) {
		var content = sources[i];
		var newLink_li = document.createElement('li');
		
		var id_li = 'list'+i;
		
		newLink_li.id = id_li;
		
		document.getElementById('list_trace').appendChild(newLink_li);
		
		var newLink = document.createElement('a');
		var newLinkText = document.createTextNode(content["_source"].document.title+'  ('+content["_source"].temporal+')');
		 
		newLink.id    = 'history';
		newLink.href = content["_source"].document.url;
		 
		newLink.appendChild(newLinkText);  
		 
		document.getElementById(id_li).appendChild(newLink);
	}
}

document.addEventListener('DOMContentLoaded', function () {
  traces();
});