(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
		var INTERFACE = {
			SelectListener : { 
				processItem: function(e, element){}
			}	
		};
		
        rf.ui.PopupList =  function(id, listener, options) {
        	$super.constructor.call(this, id, options);
            this.selectListener =  listener;
            this.selectItemCss = options.selectItemCss;
            this.itemCss = options.itemCss;
            
           	//TODO: from option map?
           	this.index = -1;
           	this.visible = false;
           	this.__updateItemsList();
        };
        
        rf.ui.Popup.extend(rf.ui.PopupList);
    	var $super = rf.ui.PopupList.$super;
        
    	$.extend(rf.ui.PopupList.prototype, ( function () {

    		var isSelectListener = function(obj) {
    			for (var method in INTERFACE.SelectListener) {
    		        if ( (typeof obj[method] != typeof INTERFACE.SelectListener[method]) ) {
    		        	return false;
    		        }
    		    }
    		    return true;
    		};
    		
    		return{
    			
    			name : "popupList", 
           			
           		processItem: function(e, element) {
           			if(isSelectListener(this.selectListener)) {
           				this.selectListener.processItem(e, element);
           			}
           		},
           		
           		__updateItemsList: function () {
					this.items = this.popup.find("."+this.itemCss);
				},

           		__select: function(index) {
           			var item;

           			if (this.index != -1) {
						item = this.items.eq(this.index);
						this.__unSelectItem(item);
					}
					
					this.index += index;
					if (this.index < 0 ) {
						this.index = this.items.length - 1;
					} else if (this.index >= this.items.length) {
						this.index = 0;
					}

           			item = this.items.eq(this.index);
           			this.__selectItem(item);
           		},
           		
           		__selectItem: function(item) {
           			item.addClass(this.selectItemCss);
           		},
           		
           		__unSelectItem: function(item) {
           			item.removeClass(this.selectItemCss);
           		},
           		
           		__onEnter: function(e) {
           		},
           		
           		//remove event, rename
           		__onKeyUp: function(e) {
           			this.__select(-1);
           		},
           		
           		//remove event, rename 
           		__onKeyDown: function(e) {
           			this.__select(1);
           		},
           		
				__onMouseOver: function(e) {
           			var item = this.__getItem(e);
    				var index = this.items.index(item);
    				this.__select(index);
           		},
           		
           		__onClick: function(e) {
           			var item = this.__getItem(e);
    				var index = this.items.index(item);
    				this.__select(index);
           			this.processItem(e, item);
           		}, 
				
				__getItem: function(e) {
					return $(e.target).closest("."+this.itemCss, e.currentTarget);
				}, 
				
				__getItems: function () {
					return this.items;
				}
    		}
    	})());

})(jQuery, window.RichFaces);