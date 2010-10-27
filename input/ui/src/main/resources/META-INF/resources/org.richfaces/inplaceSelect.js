(function ($, rf) {
	
	rf.ui = rf.ui || {};
    
	rf.ui.InplaceSelect =  function(id, options) {
       	var mergedOptions = $.extend({}, defaultOptions, options);
    	$super.constructor.call(this, id, mergedOptions)
    	mergedOptions['attachTo'] = id;
    	this.popupList = new rf.ui.PopupList(id+"List", this, mergedOptions);
    	this.items = mergedOptions.items;
    	this.selValueInput = $(document.getElementById(id+"selValue"));
    	this.list = $(document.getElementById(id+"List"));
    	this.list.bind("click", $.proxy(this.__onListClick, this));
		this.openPopup = false; 
    }
	
    rf.ui.InplaceInput.extend(rf.ui.InplaceSelect);
	var $super = rf.ui.InplaceSelect.$super;
	
	var defaultOptions = {
    	defaultLabel: "",
    	showControl: false,
    	itemCss: "rf-is-opt",
    	selectItemCss: "rf-is-sel", 
    	listCss: "rf-is-lst-cord",
    	noneCss: "rf-is-none",
	    editCss: "rf-is-edit", 
	    changedCss: "rf-is-c-s"
	};

	
	$.extend(rf.ui.InplaceSelect.prototype, ( function () {
		
		return{
			name : "inplaceSelect",
			
			getName: function() {
				return this.name;
			},
			
			geNamespace: function() {
				return this.namespace;
			},

			onshow: function() {
				if(this.openPopup) {
					this.popupList.show();
				}
				
				if(!this.openPopup) {
					this.openPopup = true;
				}
				
				$super.onshow.call(this);
			}, 
			
			onhide: function() {
				this.popupList.hide();
				this.openPopup = false;
			}, 
			
			processItem: function(item) {
				var key = $(item).attr("id");
				var value = this.getItemValue(key);
				this.saveItemValue(value);
				var label = this.getItemLabel(key);
				this.setValue(label);
				
           		this.popupList.hide();
				this.openPopup = false;
           		this.__setInputFocus();
			},
			
			getItemValue: function(key) {
				for(var i in this.items) {
					var item = this.items[i];
					if(item && item.id == key) {
						return item.value;
					}
				}
			}, 
			
			saveItemValue: function(value) {
				this.selValueInput.val(value);
			},
			
			getItemLabel: function(key) {
				for(var i in this.items) {
					var item = this.items[i];
					if(item && item.id == key) {
						return item.label;
					}
				}
			}, 
			
       		__keydownHandler: function(e) {
				
				var code; 
				
				if(e.keyCode) {
					code = e.keyCode;
				} else if(e.which) {
					code = e.which;
				}
       			
				if(this.popupList.isVisible()) {
	       			switch(code) {
	       				case rf.KEYS.DOWN: 
	       					e.preventDefault();
	       					this.popupList.__selectNext(); 
	       	           		this.__setInputFocus();
	       					break;
	       				
	       				case rf.KEYS.UP:
	       					e.preventDefault();
	       					this.popupList.__selectPrev();
	       	           		this.__setInputFocus();
	       					break;
	       				
	       				case rf.KEYS.RETURN:
	       					e.preventDefault();
	       					this.popupList.__selectCurrent();
	       	           		this.__setInputFocus();
	       					return false;
	       					break;
	   				}
				}
       			
				$super.__keydownHandler.call(this,e);
			},
		
			__blurHandler: function(e) {
				if(this.isEditState()) {
					this.timeoutId = window.setTimeout($.proxy(function(){
						this.popupList.hide();
						this.__handleBlur();
					}, this), 200);
				}	
       		}, 

       		__onListClick: function(e) {
       			window.clearTimeout(this.timeoutId);
       		}
		}
		
	})());

})(jQuery, window.RichFaces);
