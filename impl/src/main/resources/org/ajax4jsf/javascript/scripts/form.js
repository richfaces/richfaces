
if (!window.A4J) { window.A4J= {};}

if(!A4J.findForm){

function _JSFFormSubmit(linkId, formName, target, parameters){
	var form = (typeof formName == 'string' ? document.getElementById(formName) : formName);
	if(form){
		/*var hiddenField = form.elements[formName+":_idcl"];
		if(hiddenField){
			hiddenField.value=linkId;
	    } else {
			var input = document.createElement("input");
			input.type="hidden";
			input.name=formName+":_idcl";
			input.value=linkId;
			form.appendChild(input);
		}*/
		
		var paramNames = [];
		
		var oldTarget = form.target;
		if(target){
			form.target=target;
		}
		if(parameters){
			for(var param in parameters){
				paramNames.push(param);
				if(form.elements[param]){
					form.elements[param].value = parameters[param];
				} else {
					var input = document.createElement("input");
					input.type="hidden";
					input.id=param;
					input.name=param;
					input.value=parameters[param];
					
					if (param === "javax.faces.portletbridge.STATE_ID" /* fix for fileUpload in portlets */ 
						&& form.firstChild) {
						
						form.insertBefore(input, form.firstChild);
					} else {
						form.appendChild(input);
					}
				}
			}
		}
		
		var onsubmitResult;
		if (form.fireEvent) {
			onsubmitResult = form.fireEvent("onsubmit");
		} else {
			var event = document.createEvent("HTMLEvents");
			event.initEvent("submit", true, true)

			onsubmitResult = form.dispatchEvent(event);
		}

		if (onsubmitResult) {
			form.submit();
		}
		
		_clearJSFFormParameters(formName,oldTarget,paramNames);
		
	} else {
		alert("Form "+formName+" not found in document");
	}
	return false;
};

function _clearJSFFormParameters(formName,target,fields){
	var form = (typeof formName == 'string' ? document.getElementById(formName) : formName);
	if(form){
		if(target){
			form.target = target;
		} else {
			form.target = '';
		}
		if(fields){
		 for(var i=0;i<fields.length;i++){
			var field = form.elements[fields[i]];
			if(field){
				var pNode = field.parentNode;
				if (pNode) {
					pNode.removeChild(field);
				}
			}
		 }
		}
	}
}

function clearFormHiddenParams(formName,target,fields){
	_clearJSFFormParameters(formName,target,fields);
}


  A4J.findForm = function(element){
	var parent = element;
	do{
		parent = parent.parentNode;
	} while (parent && parent.nodeName.toLowerCase() != 'form');
	if(!parent){
		parent = {reset:function(){}, submit:function(){}};
	}
	return parent;
  }

  A4J._formInput = null;

// setup form to handle 'onclick' events, to detect input element.
  A4J.setupForm = function(id){
  		var element = (typeof id == 'string' ? window.document.getElementById(id) : id);
	    var name = "click";
        if (element.addEventListener) {
             element.addEventListener(name, A4J._observer, false);
        } else if (element.attachEvent) {
             element.attachEvent('on' + name, A4J._observer);
        }
        // TODO - handle array of all attached forms and remove listeners
        // on ajax updates, to avoid memory leaks.
  }

  A4J._observer = function(evt){
  	var src = evt.target||evt.srcElement;
  	if( src && src.nodeName.toUpperCase() == 'INPUT' && src.type.toUpperCase() == 'SUBMIT'){
  		A4J._formInput = src;
  	} else {
  		A4J._formInput = null;
  	}
  }
}
