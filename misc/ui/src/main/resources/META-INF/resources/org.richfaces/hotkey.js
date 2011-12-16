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
        this.attachToDom(this.componentId);

        this.handler = $.proxy(this.__pressHandler, this);
        this.options.selector = (this.options.selector) ? this.options.selector : document;

        $(document).ready($.proxy(function() {
            $(this.options.selector).bind(this.options.type, this.options, this.handler);
        }, this));
    };

    rf.BaseComponent.extend(rf.ui.HotKey);

    var $super = rf.ui.HotKey.$super;

    $.extend(rf.ui.HotKey.prototype, {

        name : "HotKey",

        __pressHandler : function(event) {
            this.invokeEvent.call(this, 'press', document.getElementById(this.id), event);
        },

        destroy : function() {
            rf.Event.unbindById(this.id, this.namespace);

            $(this.options.selector).unbind(this.options.type, this.handler);

            $super.destroy.call(this);
        }
    });

})(jQuery, RichFaces);