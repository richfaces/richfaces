/**
 * @author Lukas Fryc
 */

(function($, rf) {
    rf.ui = rf.ui || {};
    
    var defaultOptions = {};
    
    rf.ui.Editor = function(componentId, domBinding, options) {
        $super.constructor.call(this, componentId, options, defaultOptions);
        
        var self = this;
        this.textareaId = componentId;
        this.domBinding = domBinding;
        
        this.attachToDom(this.textareaId);
        this.attachToDom(this.domBinding);
        
        this.__updateElement = function() {
            self.ckeditor.updateElement();
        }
        
        $(document).ready(function() {
            self.ckeditor = CKEDITOR.replace(self.textareaId);
            rf.Event.bind(self.__getForm(), 'ajaxsubmit', self.__updateElement);
        });
    };
    
    rf.BaseComponent.extend(rf.ui.Editor);
    
    var $super = rf.ui.Editor.$super;
    
    $.extend(rf.ui.Editor.prototype, {
        
        name : "Editor",
    
        /**
         * Updates editor with the content of associated textarea
         */
        __updateEditor : function() {
            var textarea = this.__getTextarea();
            textarea.hide();
            this.attachToDom(textarea);
            var newValue = textarea.val();
            this.ckeditor.setData(newValue);
        },
        
        __getTextarea : function() {
            return $(document.getElementById(this.textareaId));
        },
        
        /**
         * Returns the form where this editor component is placed
         */
        __getForm : function() {
            return $('form').has(this.__getTextarea()).get(0);
        },
    
        /**
         * Overrides #destroy method in order to do not destroy
         * editor component when cleaning textarea (needs to be destroyed
         * when cleaning domBinding)
         */
        destroy : function() {
            // do not destroy component here
        },
        
        /**
         * Detaches editor from DOM element and destroys component if necessary (see bellow).
         * 
         * Destroys editor component if detaching from non-textarea element.
         * This method replaces #destroy() when applied to non-textarea elements.
         */
        detach : function(source) {
            // destroy editor
            if (!$(source).is('textarea')) {
                if (this.__updateElement) {
                    rf.Event.unbind(this.__getForm(), 'ajaxsubmit', this.__updateElement);
                    this.__updateElement = null;
                }
                
                if (this.ckeditor) {
                    this.ckeditor.destroy();
                    this.ckeditor = null;
                }
                
                this.__getTextarea().show();
                
                $super.destroy.call(this);
            }
            
            // detach editor
            $super.detach.call(this);
        }
    });
})(jQuery, RichFaces);