(function($, rf) {
	rf.ui = rf.ui || {};

	var defaultOptions = {
		mode : 'server',
		attachToBody : false,
		showDelay : 50,
		hideDelay : 300,
		verticalOffset : 0,
		horisantalOffset : 0,
		showEvent : 'click',
		positionOffset : [0, 0],
		direction : "AA",
		jointPoint : "AA",
		positionType : "DROPDOWN",
		itemCss : "rf-ddm-itm",
		selectItemCss : "rf-ddm-itm-sel",
		unselectItemCss : "rf-ddm-itm-unsel",
		disabledItemCss : "rf-ddm-itm-dis",
		listCss : "rf-ddm-lst",
		listContainerCss : "rf-ddm-lst-bg"
	}

	// constructor definition
	rf.ui.Menu = function(componentId, options) {
		this.options = {};
		$.extend(this.options, defaultOptions, options || {});
		$super.constructor.call(this, componentId, this.options);
		this.id = componentId;
		this.namespace = this.namespace || "."
				+ rf.Event.createNamespace(this.name, this.id);
		this.groupList = new Array();

		this.attachToDom(componentId);
		if (!rf.ui.MenuManager)
			rf.ui.MenuManager = {};
		this.menuManager = rf.ui.MenuManager;
	};

	rf.ui.MenuBase.extend(rf.ui.Menu);

	// define super class link
	var $super = rf.ui.Menu.$super;

	$.extend(rf.ui.Menu.prototype, rf.ui.MenuKeyNavigation);

	$.extend(rf.ui.Menu.prototype, (function() {
				return {
					name : "Menu",
					initiateGroups : function(groupOptions) {

						for (var i in groupOptions) {
							var groupId = groupOptions[i].id;
							var positionOffset = [
									groupOptions[i].horizontalOffset,
									groupOptions[i].verticalOffset]
							var onshow = groupOptions[i].onshow;
							var onhide = groupOptions[i].onhide;

							var eventType = "mouseover";

							if (null != groupId) {
								// var popup = new RichFaces.ui.Popup(groupId +
								// '_list', options);
								var group = new RichFaces.ui.MenuGroup(groupId,
										{
											rootMenuId : this.id,
											onshow : onshow,
											onhide : onhide
										});
								this.groupList[groupId] = group;
							}
						}
					},

					submitForm : function(item) {
						var form = this.__getParentForm(item);
						if (this.options.mode == "server") {
							rf.submitForm(form, {
										selectedMenuItem : item.id
									});
						}
						if (this.options.mode == "ajax") {
							rf.ajax(item.id);
						}
					},

					show : function() {
						if (this.menuManager.openedMenu != this.id) {
							this.menuManager.shutdownMenu();
							this.menuManager.addMenuId(this.id);
							this.__showPopup();
						}
						this.popupElement.focus();
					},

					hide : function() {
						this.__hidePopup();
						this.menuManager.deletedMenuId();
					},

					__getParentForm : function(item) {
						return item.parents("form")[0];
					},

					destroy : function() {
						// clean up code here
						this.detach(this.id);

						// call parent's destroy method
						$super.destroy.call(this);
					}
				};
			})());

	rf.ui.MenuManager = {
		openedMenu : null,

		activeSubMenu : null,

		addMenuId : function(menuId) {
			this.openedMenu = menuId;
		},

		deletedMenuId : function() {
			this.openedMenu = null;
		},

		shutdownMenu : function() {
			if (this.openedMenu != null) {
				rf.$(rf.getDomElement(this.openedMenu)).hide();
			}
			this.deletedMenuId();
		},

		setActiveSubMenu : function(submenu) {
			this.activeSubMenu = submenu;
		},

		getActiveSubMenu : function() {
			return this.activeSubMenu;
		}

	}
})(jQuery, RichFaces)