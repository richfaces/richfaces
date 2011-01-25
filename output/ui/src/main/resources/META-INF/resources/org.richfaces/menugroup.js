(function($, rf) {
	rf.ui = rf.ui || {};
	var defaultOptions = {
		showEvent : 'mouseenter',
		showDelay : 300,
		attachToBody : false,
		positionOffset : [0, 0],

		selectItemCss : "rf-ddm-itm-sel",
		itemCss : "rf-ddm-itm",
		disabledItemCss : "rf-ddm-itm-dis",
		listCss : "rf-ddm-lst",
		listContainerCss : "rf-ddm-lst-bg"
	}
	//constructor definition
	rf.ui.MenuGroup = function(componentId, options) {
		this.id = componentId;
		this.options = {};
		$.extend(this.options, defaultOptions, options || {});
		$super.constructor.call(this, componentId, this.options);
		this.namespace = this.namespace || "."
				+ rf.Event.createNamespace(this.name, this.id);
		this.attachToDom(componentId);
		
		rf.Event.bindById(this.id, this.options.showEvent, $.proxy(
				this.__showHandler, this), this);
		
		this.rootMenu = rf.$(this.options.rootMenuId);

		this.shown = false;
		this.jqueryElement = $(this.element);

	};

	rf.ui.MenuBase.extend(rf.ui.MenuGroup);

	// define super class link
	var $super = rf.ui.MenuGroup.$super;

	$.extend(rf.ui.MenuGroup.prototype, rf.ui.MenuKeyNavigation);

	$.extend(rf.ui.MenuGroup.prototype, (function() {
				return {
					name : "MenuGroup",
					show : function() {
						var id = this.id;
						if (this.rootMenu.groupList[id] && !this.shown) {
							this.rootMenu.invokeEvent("groupshow", rf
											.getDomElement(this.rootMenu.id),
									null);
							this.__showPopup();
							this.shown = true;
						}
					},
					hide : function() {
						var id = this.id;
						var menu = this.rootMenu;
						if (menu.groupList[this.id] && this.shown) {
							menu.invokeEvent("grouphide", rf
											.getDomElement(menu.id), null);
							this.__hidePopup();
							this.shown = false;
						}
					},

					select : function() {
						this.jqueryElement.removeClass('rf-ddm-itm-unsel');
						this.jqueryElement.addClass('rf-ddm-itm-sel');
					},
					unselect : function() {
						this.jqueryElement.removeClass('rf-ddm-itm-sel');
						this.jqueryElement.addClass('rf-ddm-itm-unsel');
					},

					__showHandler : function() {
						this.select();
						$super.__showHandler.call(this);
					},
					__leaveHandler : function() {
						window.clearTimeout(this.showTimeoutId);
						this.hideTimeoutId = window.setTimeout($.proxy(
										function() {
											this.hide();
										}, this), this.options.hideDelay);
						this.unselect();
					},

					destroy : function() {
						// clean up code here
						this.detach(this.id);
						// call parent's destroy method
						$super.destroy.call(this);
					}
				}

			})());
})(jQuery, RichFaces)