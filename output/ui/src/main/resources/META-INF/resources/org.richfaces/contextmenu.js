(function($, rf) {
    rf.ui = rf.ui || {};

    var defaultOptions = {
        showEvent : 'contextmenu',
        cssRoot : "ctx",
        cssClasses : {}
    };

    // constructor definition
    rf.ui.ContextMenu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        rf.getDomElement(this.attachId).appendChild(this.element[0]);
    }

    rf.ui.Menu.extend(rf.ui.ContextMenu);

    // define super class link
    var $super = rf.ui.ContextMenu.$super;

    $.extend(rf.ui.ContextMenu.prototype, (function() {
        return {
            name : "ContextMenu",

            getAttachId : function() {
                var attachId = typeof this.options.attachTo === 'undefined' ?
                    this.element.parentNode.id : this.options.attachTo;
                return attachId;
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
                }
            }

        };
    })());

})(jQuery, RichFaces);