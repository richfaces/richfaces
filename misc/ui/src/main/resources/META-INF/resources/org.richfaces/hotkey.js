/**
 * @author Ilya Shaikovsky
 * @author Lukas Fryc
 */

(function($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        enabledInInput : false,
        preventDefault : true
    };
    
    var types = [ 'keypress', 'keydown', 'keyup' ];

    rf.ui.HotKey = function(componentId, options) {
        $super.constructor.call(this, componentId);
        this.options = $.extend({}, defaultOptions, options);
        this.attachToDom(this.componentId);
        this.__handlers = {};
        
        this.options.selector = (this.options.selector) ? this.options.selector : document;

        $(document).ready($.proxy(function() {
            this.__bindDefinedHandlers();
        }, this));
    };

    rf.BaseComponent.extend(rf.ui.HotKey);

    var $super = rf.ui.HotKey.$super;

    $.extend(rf.ui.HotKey.prototype, {

        name : "HotKey",
        
        __bindDefinedHandlers : function() {
            for (var i = 0; i < types.length; i++) {
                if (this.options['on' + types[i]]) {
                    this.__bindHandler(types[i]);
                }
            }
        },
        
        __bindHandler : function(type) {
            this.__handlers[type] = $.proxy(function(event) {
                var result = this.invokeEvent.call(this, type, document.getElementById(this.id), event);
                if (this.options.preventDefault) {
                    event.stopPropagation();
                    event.preventDefault();
                    return false;
                }
                return result;
            }, this);
            $(this.options.selector).bind(type, this.options, this.__handlers[type]);
        },

        destroy : function() {
            rf.Event.unbindById(this.id, this.namespace);

            for (var type in this.__handlers) {
                if (this.__handlers.hasOwnProperty(type)) {
                    $(this.options.selector).unbind(type, this.__handlers[type]);
                }
            }

            $super.destroy.call(this);
        }
    });

})(jQuery, RichFaces);