(function ($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        useNative: false
    };

    rf.ui.FocusManager = rf.BaseComponent.extendClass({

        name:"FocusManager",

        init: function (componentId, options) {
            $super.constructor.call(this, componentId);
            options = $.extend({}, defaultOptions, options);
            this.attachToDom(this.id);
            
            var focusInput = $(document.getElementById(componentId + 'InputFocus')); 
            
            $(document).on('focus', ':input', function(e) {
                var target = $(e.target);
                var ids = e.target.id || '';
                target.parents().each(function () {
                    var id = $(this).attr('id');
                    if (id) {
                        ids += ' ' + id;
                    }
                });
                focusInput.val(ids);
                rf.log.debug('FocusManager observed focus event for following componentId candidates: ' + ids);
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
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.FocusManager.$super;
})(jQuery, RichFaces);