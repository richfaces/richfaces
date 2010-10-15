(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
        rf.ui.PopupList =  function(id, listener, options) {
        	$super.constructor.call(this, id, options);
            this.selectListener =  listener;
            this.selectItemCss = options.selectItemCss;
            this.itemCss = options.itemCss;
            this.listCss = options.listCss;
           	this.index = -1;
           	this.__updateItemsList();
        };
        
        rf.ui.Popup.extend(rf.ui.PopupList);
    	var $super = rf.ui.PopupList.$super;
        
    	$.extend(rf.ui.PopupList.prototype, ( function () {
    		
    		return{
    			
    			name : "popupList", 
           		
           		processItem: function(item) {
    				if(this.selectListener.processItem && typeof this.selectListener.processItem == 'function') {
           				this.selectListener.processItem(item);
           			}
           		},
           		
           		selectItem: function(item) {
           			if(this.selectListener.selectItem && typeof this.selectListener.selectItem == 'function') {
           				this.selectListener.selectItem(item);
           			} else {
           				item.addClass(this.selectItemCss);
           			}
           		},
           		
           		unselectItem: function(item) {
           			if(this.selectListener.unselectItem && typeof this.selectListener.unselectItem == 'function') {
           				this.selectListener.unselectItem(item);
           			} else {
           				item.removeClass(this.selectItemCss);
           			}
           		},
           		
           		isPopupList: function(target) {
	       			var parentId = target.parents("." + this.listCss).attr("id");
	       			return (parentId && (parentId == this.getId()));
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
						this.unselectItem(item);
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
           			this.selectItem(item);
           		},
           		
           		__selectCurrent: function() {
           			var item;
           			if(this.items && this.index >= 0) {
           				item = this.items.eq(this.index);
               			this.processItem(item);
           			}
           		},
           		
           		__selectPrev: function() {
           			this.__selectByIndex(-1, true);
           		},
           		
           		__selectNext: function() {
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
           			this.processItem(item);
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