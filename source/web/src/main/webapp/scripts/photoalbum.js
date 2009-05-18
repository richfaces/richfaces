function selectPopularTag(tag, target) {
	if(target) {
		var value = target.value.strip();
		if(value.indexOf(tag) == -1) {
			target.value = value.length != 0 ? value + ", " + tag : tag; 
		}
	}
}

function applyModalPanelEffect(panelId, effectFunc, params) {
	if (panelId && effectFunc) {
		
		var modalPanel = $(panelId);
		
		if (modalPanel && modalPanel.component) {
			var component = modalPanel.component;
			var div = component.getSizedElement(); 
			
			Element.hide(div);
						
			effectFunc.call(this, Object.extend({targetId: div.id}, params || {}));
		}
				
	}
}

// fix IE6 footer position
function kickLayout(selector) {
	
	if(Richfaces && Richfaces.browser.isIE6) {
		var element = jQuery(selector);
		if(element) {
			element.css('zoom','normal').css('zoom','100%');
		}
	}	
}