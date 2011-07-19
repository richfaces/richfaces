/**
 * @author Lukas Fryc
 */

(function($, rf) {
    rf.ui = rf.ui || {};
    
    var defaultOptions = {};
    
    var ckeditor;
    
    var updateElement = function() {
        ckeditor.updateElement();
    };
    
    rf.ui.Editor = function(componentId, options) {
        $super.constructor.call(this, componentId, options, defaultOptions);
        
        $(document).ready(function() {
            ckeditor = CKEDITOR.replace(componentId);
            
            form = $('form').has(document.getElementById(componentId)).get(0);
            
            rf.Event.bind(form, 'ajaxsubmit', updateElement);
        });
    };
    
    rf.ui.Base.extend(rf.ui.Editor);
    
    var $super = rf.ui.Editor.$super;
    
    $.extend(rf.ui.Editor.prototype, {
        
        name : "Editor",
        
        destroy : function() {
            rf.Event.unbind(form, 'ajaxsubmit', updateElement);
            ckeditor.destroy();
            
            $super.destroy.call(this);
        }
    });
})(jQuery, RichFaces);