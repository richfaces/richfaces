function selectPopularTag(tag, target) {
	//console.log(target);
	//console.log(target.value);
	if(target) {
		var value = target.getValue().trim();
		if(value.indexOf(tag) == -1) {
			target.setValue(value.length != 0 ? value + ", " + tag : tag); 
		}
	}
}

function applyModalPanelEffect(panelId, /*effectFunc,*/ params) {
	if (panelId /*&& effectFunc*/) {
		
		var modalPanel = $(panelId);
		
		if (modalPanel && modalPanel.component) {
			var component = modalPanel.component;
			var div = component.getSizedElement(); 
			
			//Element.hide(div);
			div.style.visibility = 'hidden';
						
			//effectFunc.call(this, Object.extend({targetId: div.id}, params || {}));
		}
				
	}
}

/*
// fix IE6 footer position
function kickLayout(selector) {
	
	if(Richfaces && Richfaces.browser.isIE6) {
		var element = jQuery(selector);
		if(element) {
			element.css('zoom','normal').css('zoom','100%');
		}
	}
	
} */

function show(id) {
	document.getElementById(id).style.display = 'inherit';
}

function hide(id) {
	document.getElementById(id).style.display = 'none';
}

/** Facebook **/

pushImage = function (album_id, url, message) {
	FB.api('/' + album_id + '/photos', 'post', { message: (message || 'No description'), url:url }, 
            function(response){
              if (!response || response.error) {
                  console.log('Error occured');
                  console.log(response);
              } else {
                  console.log('Post ID: ' + response.id);
              }
    });
};
      
getAlbums = function (callback) {
	FB.api('/me/albums', 'get', function(response){
        if (!response || response.error) {
            console.log('Error occured');
            console.log(response);
        } else {
            callback(JSON.stringify(response.data));
            console.log(response);
        }
	});
};
      
Fblogin = function () {
	FB.login(function(response) {
		   if (response.authResponse) {
		     console.log('Welcome!  Fetching your information.... ');
		     FB.api('/me', function(response) {
		       console.log('Good to see you, ' + response.name + '.');
		     });
		   } else {
		     console.log('User cancelled login or did not fully authorize.');
		   }
		 }, {scope: 'read_stream, publish_stream, user_photos'});
};