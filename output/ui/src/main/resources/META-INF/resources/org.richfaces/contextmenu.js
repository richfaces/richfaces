(function($, rf) {
    rf.ui = rf.ui || {};

    var defaultOptions = {
        showEvent : 'contextmenu',
        cssRoot : "ctx",
        cssClasses : {},
        attached : true
    };

    // constructor definition
    rf.ui.ContextMenu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
    }

    rf.ui.Menu.extend(rf.ui.ContextMenu);

    // define super class link
    var $super = rf.ui.ContextMenu.$super;

    $.extend(rf.ui.ContextMenu.prototype, (function() {
        return {
            name : "ContextMenu",

            getTarget : function() {
                if (!this.options.attached) {
                    return null;
                }
                var target = typeof this.options.target === 'undefined' ?
                    this.element.parentNode.id : this.options.target;
                return target;
            },

            __showHandler : function(e) {
                if (this.__isShown()) {
                    this.hide();
                }
                return $super.__showHandler.call(this, e);
            },

            show : function(e) {
                if (this.menuManager.openedMenu != this.id) {
                    this.menuManager.shutdownMenu();
                    this.menuManager.addMenuId(this.id);
                    this.__showPopup(e); // include the event to position the popup at the cursor
                    var parent = rf.$(this.target);
                    if (parent && parent.contextMenuShow) {
                        parent.contextMenuShow(this, e);
                    }
                }
            }

        };
    })());

})(jQuery, RichFaces);