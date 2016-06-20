(function ($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        useNative: false
    };

    rf.ui.Placeholder = rf.BaseComponent.extendClass({

        name:"Placeholder",

        /**
         * Backing object for rich:placeholder
         * 
         * @extends RichFaces.BaseComponent
         * @memberOf! RichFaces.ui
         * @constructs RichFaces.ui.PlaceHolder
         * 
         * @param componentId
         * @param options
         */
        init: function (componentId, options) {
            $super.constructor.call(this, componentId);
            options = $.extend({}, defaultOptions, options);
            this.attachToDom(this.id);
            $(function() {
                options.className = 'rf-plhdr ' + ((options.styleClass) ? options.styleClass : '');
                var elements = (options.selector) ? $(options.selector) : $(document.getElementById(options.targetId));
                // finds all inputs within the subtree of target elements
                var inputs = elements.find('*').andSelf().filter(':editable');
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
            // need to use setTimeout to allow client's native reset to happen
        $(document).on('reset', 'form', function() {setTimeout( $.watermark.showAll, 0); });
    });
    
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Placeholder.$super;
})(RichFaces.jQuery, RichFaces);