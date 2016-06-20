(function($, rf) {
    rf.ui = rf.ui || {};

    var defaultOptions = {
        showEvent : 'contextmenu',
        cssRoot : "ctx",
        cssClasses : {},
        attached : true,
        showOptions : {},
        attachToBody : true
    };

    // constructor definition
    /**
     * Backing object for rich:contextMenu
     * 
     * @extends RichFaces.ui.Menu
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.ContextMenu
     * 
     * @param componentId
     * @param options
     */
    rf.ui.ContextMenu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        rf.Event.unbindById(this.id, "mouseenter");
        rf.Event.unbindById(this.id, "mouseleave");
        rf.Event.bind(this.popup.popup, "mouseenter", $.proxy(this.__overHandler, this), this);
        if (!this.options.sticky) {
            rf.Event.bind(this.popup.popup, "mouseleave", $.proxy(this.__leaveHandler, this), this);
        }
        rf.Event.bind('body', 'click' + this.namespace, $.proxy(this.__leaveHandler, this));
        rf.Event.bindById(this.popup.popup, 'click' + this.namespace, $.proxy(this.__clilckHandler, this));
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

            /**
             * Show the contextMenu
             * 
             * @method
             * @name RichFaces.ui.ContextMenu#show
             * @param [event] {Event} event triggering this behavior
             * @param [opts] {Object} object containing options for the menu
             */
            show : function(e, options) {
                if (e) {
                    e.stopPropagation();
                }
                if (this.menuManager.openedMenu != this.id) {
                    this.menuManager.shutdownMenu();
                    this.menuManager.addMenuId(this.id);
                    this.options.showOptions = options || {};
                    if (this.options.showOptions.replace) {
                        this.__replaceOnShow();
                    }
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
                rf.Event.unbindById(this.popup.popup, "mouseenter");
                rf.Event.unbindById(this.popup.popup, "mouseleave");
                rf.Event.unbind('body', 'click' + this.namespace);
                rf.Event.unbindById(this.popup.popup, 'click' + this.namespace);

                // call parent's destroy method
                $super.destroy.call(this);
            },
            
            __replaceOnShow : function() {
                var labels = this.element.find(".rf-ctx-itm-lbl"),
                    opts = this.options.showOptions.replace,
                    first = true;

                for (var o in opts) {
                    if (opts.hasOwnProperty(o)) {
                        var re = new RegExp("{" + o + "}", "g");
                        labels.map(function(index, label) {
                            var $label = $(label);
                            if (!$label.attr("data-original")) {
                                $label.attr("data-original", $label.text());
                            }

                            var text = first ? $label.attr("data-original") : $label.text();
                            $label.text(text.replace(re, opts[o]));
                        });
                        first = false;
                    }
                }
            }
        };
    })());

})(RichFaces.jQuery, RichFaces);