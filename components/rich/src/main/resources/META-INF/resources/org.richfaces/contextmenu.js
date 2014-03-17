(function($, rf) {
    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    var defaultOptions = {
        showEvent : 'contextmenu',
        cssRoot : "ctx",
        cssClasses : {},
        attached : true
    };

    // constructor definition
    rf.rf4.ui.ContextMenu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        rf.Event.bind('body', 'click' + this.namespace, $.proxy(this.__leaveHandler, this));
        rf.Event.bindById(this.id, 'click' + this.namespace, $.proxy(this.__clilckHandler, this));
    }

    rf.rf4.ui.Menu.extend(rf.rf4.ui.ContextMenu);

    // define super class link
    var $super = rf.rf4.ui.ContextMenu.$super;

    $.extend(rf.rf4.ui.ContextMenu.prototype, (function() {
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
                    var parent = rf.component(this.target);
                    if (parent && parent.contextMenuShow) {
                        parent.contextMenuShow(this, e);
                    }
                }
            },

            __clilckHandler : function (event) {
                event.preventDefault();
                event.stopPropagation();
            },

            destroy : function() {
                rf.Event.unbind('body', 'click' + this.namespace);
                rf.Event.unbindById(this.id, 'click' + this.namespace);

                // call parent's destroy method
                $super.destroy.call(this);
            }


        };
    })());

})(RichFaces.jQuery, RichFaces);