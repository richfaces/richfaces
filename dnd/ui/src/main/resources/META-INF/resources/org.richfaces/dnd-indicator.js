/*
 * code review by Pavel Yaschenko
 * 
 * 1. No need to save DOM element (this.indicator). We can use id to get dom element. It helps to avoid memory leaks :)
 * 
 * 2. Name refactoring: change names acceptClass, rejectClass, draggingClass 
 * 						to more readable names: getAcceptClass, getRejectClass, getDragClass
 * 
 */

(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.DragIndicator = function(id, options) {
        $super.constructor.call(this, id);
        this.attachToDom(id);

        this.indicator = $(document.getElementById(id));
        this.options = options;
    };

    var defaultOptions = {
    };

    rf.BaseComponent.extend(rf.rf4.ui.DragIndicator);
    var $super = rf.rf4.ui.DragIndicator.$super;

    $.extend(rf.rf4.ui.DragIndicator.prototype, ( function () {
        return {
            show : function() {
                this.indicator.show();
            },

            hide: function() {
                this.indicator.hide();
            },

            getAcceptClass: function() {
                return this.options.acceptClass;
            },

            getRejectClass: function() {
                return this.options.rejectClass;
            },

            getDraggingClass: function() {
                return this.options.draggingClass;
            },

            getElement: function() {
                return this.indicator;
            }
        }
    })());

})(RichFaces.jQuery, window.RichFaces);

