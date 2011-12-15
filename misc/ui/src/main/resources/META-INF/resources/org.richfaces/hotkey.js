/**
 * @author Ilya Shaikovsky
 * @author Lukas Fryc
 */

(function($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        type : 'keyup',
        enabledInInput : false
    };

    rf.ui.HotKey = function(componentId, options) {
        $super.constructor.call(this, componentId);
        this.options = $.extend({}, defaultOptions, options);
        this.options.disableInInput = !this.options.enabledInInput;
        this.attachToDom(this.componentId);

        this.handler = $.proxy(this.__pressHandler, this);
        
        if (!this.options.selector) {
            $(document).bind(this.options.type, this.options, this.handler);
        } else {
            $(this.options.selector).live(this.options.type, this.options, this.handler);
        }
    };

    rf.BaseComponent.extend(rf.ui.HotKey);

    var $super = rf.ui.HotKey.$super;

    $.extend(rf.ui.HotKey.prototype, {

        name : "HotKey",

        __pressHandler : function() {
            this.invokeEvent.call(this, 'press', document.getElementById(this.id));
        },

        destroy : function() {
            rf.Event.unbindById(this.id, this.namespace);

            if (!this.options.selector) {
                $(document).unbind(this.options.type, this.handler)
            } else {
                $(this.options.selector).die(this.options.type, this.handler);
            }

            $super.destroy.call(this);
        }
    });

})(jQuery, RichFaces);