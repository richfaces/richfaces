(function ($, rf) {
	
	rf.ui = rf.ui || {};
		
        rf.ui.PopupList =  function(id, listener, options) {
           	var mergedOptions = $.extend({}, defaultOptions, options);
        	$super.constructor.call(this, id, mergedOptions);
        	this.selectListener =  listener;
            this.selectItemCss = mergedOptions.selectItemCss;
            this.scrollContainer = $(mergedOptions.scrollContainer);
            this.itemCss = mergedOptions.itemCss;
            this.listCss = mergedOptions.listCss;
           	this.index = -1;
           	this.popup.bind("mouseover", $.proxy(this.__onMouseOver, this));
           	this.popup.bind("click", $.proxy(this.__onClick, this));
           	
           	this.__updateItemsList();
        };
        
        rf.ui.Popup.extend(rf.ui.PopupList);
    	var $super = rf.ui.PopupList.$super;
    	
    	var defaultOptions = {
   			attachToBody: true,
   			positionType: "DROPDOWN",
   			positionOffset: [0,20]
   		};

        
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
           			this.__scrollToSelectedItem(this);
           		},
           		
           		unselectItem: function(item) {
           			if(this.selectListener.unselectItem && typeof this.selectListener.unselectItem == 'function') {
           				this.selectListener.unselectItem(item);
           			} else {
           				item.removeClass(this.selectItemCss);
           			}
           		},
           		
           		resetItemsSelection: function() {
           			if(this.items) {
           				var popup = this; 
           				this.items.each(function(i,item){ 
           						popup.unselectItem($(item));
           					}
           				);
           			}
           		},
           		
           		isPopupList: function(target) {
	       			var parentId = target.parents("." + this.listCss).attr("id");
	       			return (parentId && (parentId == this.getId()));
	       		},
           		
           		__updateItemsList: function () {
					return (this.items = this.popup.find("."+this.itemCss));
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
				}, 
				
				__setItems: function(items) {
					this.items = items;
				}, 

				__scrollToSelectedItem : function() {
					if(this.scrollContainer) {
				        var offset = 0;

				        this.items.slice(0, this.index).each(function() {
							offset += this.offsetHeight;
						});
				        
				        var parentContainer = this.scrollContainer;
				        if(offset < parentContainer.scrollTop()) {
				        	parentContainer.scrollTop(offset);
				        } else {
				        	offset+=this.items.get(this.index).offsetHeight;
				        	if(offset - parentContainer.scrollTop() > parentContainer.get(0).clientHeight) {
				        		parentContainer.scrollTop(offset - parentContainer.innerHeight());
				            }
				        }
					}    
				}
    		}
    	})());

})(jQuery, window.RichFaces);