(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    var defaultOptions = {
        useNative: false
    };

    rf.rf4.ui.Placeholder = rf.BaseComponent.extendClass({

        name:"Placeholder",

        init: function (componentId, options) {
            $super.constructor.call(this, componentId);
            options = $.extend({}, defaultOptions, options);
            this.attachToDom(this.id);
            $(function() {
                options.className = 'rf-plhdr ' + ((options.styleClass) ? options.styleClass : '');
                var elements = (options.selector) ? $(options.selector) : $(document.getElementById(options.targetId));
                // finds all inputs within the subtree of target elements
                var inputs = elements.find(':editable').andSelf().filter(':editable');
                inputs.watermark(options.text, options);
            });
        },
        // destructor definition
        destroy: function () {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });
    
    // once per all placeholders on a page
    $(function() {
        $(document).on('ajaxsubmit', 'form', $.watermark.hideAll);
        $(document).on('ajaxbegin', 'form', $.watermark.showAll);
    });
    
    // define super class reference - reference to the parent prototype
    var $super = rf.rf4.ui.Placeholder.$super;
})(RichFaces.jQuery, RichFaces);