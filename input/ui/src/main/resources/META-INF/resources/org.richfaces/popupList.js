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

				__select: function(item) {
    				var index = this.items.index(item);
    				this.__selectByIndex(index);
				},
				
           		__selectByIndex: function(index, isOffset) {
					if (this.items.length==0 || (!isOffset && this.index == index)) return;

					var item;
           			if (this.index != -1) {
						item = this.items.eq(this.index);
						this.__unSelectItem(item);
					}
           			
           			if (index==undefined) {
           				this.index = -1;
           				return;
           			}

           			if (isOffset) {
           				this.index += index;
           				if ( this.index<0 ) {
           					this.index = this.items.length - 1;
           				} else if (this.index >= this.items.length) {
           					this.index = 0;
           				}
           			} else {
           				if (index<0) {
           					index = 0;
           				} else if (index>=this.items.length) {
           					index = this.items.length - 1;
           				}
           				this.index = index;
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
           		
           	//remove event, rename ???
           		__onEnter: function(e) {
           			var item;
           			if(this.items) {
           				item = this.items.eq(this.index);
               			this.processItem(e, item);
           			}
           		},
           		
           		//remove event, rename
           		__onKeyUp: function(e) {
           			this.__selectByIndex(-1, true);
           		},
           		
           		//remove event, rename 
           		__onKeyDown: function(e) {
           			this.__selectByIndex(1, true);
           		},
           		
				__onMouseOver: function(e) {
           			var item = this.__getItem(e);
           			if(item) {
           				this.__select(item);
           			}
           		},
           		
           		__onClick: function(e) {
           			var item = this.__getItem(e);
           			this.processItem(e, item);
    				this.__select(item);
           		}, 
				
				__getItem: function(e) {
					return $(e.target).closest("."+this.itemCss, e.currentTarget).get(0);
				}, 
				
				__getItems: function () {
					return this.items;
				}
    		}
    	})());

})(jQuery, window.RichFaces);