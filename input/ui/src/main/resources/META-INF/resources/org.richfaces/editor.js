/**
 * @author Lukas Fryc
 */

(function($, rf) {
    rf.ui = rf.ui || {};
    
    var defaultOptions = {
        toolbar: 'Basic',
        readonly: false,
        style: '',
        styleClass: '',
        editorStyle: '',
        editorClass: ''
    };
    
    rf.ui.Editor = function(componentId, domBinding, options) {
        $super.constructor.call(this, componentId);
        this.options = $.extend({}, defaultOptions, options);
        
        var $this = this;
        this.textareaId = componentId;
        this.domBinding = domBinding;
        this.editorElementId = 'cke_' + componentId;
        this.valueChanged = false;
        
        this.attachToDom(this.textareaId);
        this.attachToDom(this.domBinding);
        
        this.__initializeEditor = function() {
            $this.ckeditor = CKEDITOR.replace($this.textareaId, $this.__getConfiguration());
            
            // register event handlers
            rf.Event.bind($this.__getForm(), 'ajaxsubmit', $this.__updateElement);
            $this.ckeditor.on('instanceReady', $this.__instanceReadyHandler);
            $this.ckeditor.on('blur', $this.__blurHandler);
            $this.ckeditor.on('focus', $this.__focusHandler);
        }
        
        this.__updateElement = function() {
            $this.ckeditor.updateElement();
        }
        
        this.__instanceReadyHandler = function(e) {
            $this.__setupStyling();
            $this.__setupPassThroughAttributes();
            
            $this.invokeEvent.call($this, "init", $this.__getTextarea(), e);
        }
        
        this.__blurHandler = function(e) {
            $this.invokeEvent.call($this, "blur", $this.__getTextarea(), e);
            if ($this.getEditor().checkDirty()) {
                $this.valueChanged = true;
                $this.__changeHandler();
            }
            $this.getEditor().resetDirty();
        }
        
        this.__focusHandler = function(e) {
            $this.invokeEvent.call($this, "focus", $this.__getTextarea(), e);
        }
        
        this.__changeHandler = function(e) {
            $this.invokeEvent.call($this, "change", $this.__getTextarea(), e);
        }
        
        $(document).ready(this.__initializeEditor);
        rf.Event.bindById(this.__getTextarea(), 'init', this.options.oninit, this);
    };
    
    rf.BaseComponent.extend(rf.ui.Editor);
    
    var $super = rf.ui.Editor.$super;
    
    $.extend(rf.ui.Editor.prototype, {
        
        name: "Editor",
    
        /**
         * Updates editor with the value and attributes of associated textarea
         */
        __updateEditor: function() {
            var textarea = this.__getTextarea();
            
            textarea.hide();
            this.attachToDom(textarea);
            
            this.__updateEditorConfiguration();
        },
        
        __getTextarea: function() {
            return $(document.getElementById(this.textareaId));
        },
        
        /**
         * Returns the form where this editor component is placed
         */
        __getForm: function() {
            return $('form').has(this.__getTextarea()).get(0);
        },
        
        __getConfiguration: function() {
            var textarea = this.__getTextarea();
            return {
                toolbar: this.__getToolbar(),
                readOnly: textarea.attr('readonly') || this.options.readonly,
                width: this.__resolveUnits(textarea.width()),
                height: this.__resolveUnits(textarea.height()),
                bodyClass: 'rf-ed-b'
            }
        },
        
        __setupStyling: function() {
            var span = $(document.getElementById(this.editorElementId));
            if (!span.hasClass('rf-ed')) {
                span.addClass('rf-ed');
            }
            var styleClass = $.trim(this.options.styleClass + ' ' + this.options.editorClass);
            if (this.initialStyle == undefined) {
                this.initialStyle = span.attr('style');
            }
            var style = this.__concatStyles(this.initialStyle, this.options.style, this.options.editorStyle);
            if (this.oldStyleClass !== styleClass) {
                if (this.oldStyleClass) {
                    span.removeClass(this.oldStyleClass);
                }
                span.addClass(styleClass);
                this.oldStyleClass = styleClass;
            }
            if (this.oldStyle !== style) {
                span.attr('style', style);
                this.oldStyle = style;
            }
        },
        
        __setupPassThroughAttributes: function() {
            var textarea = this.__getTextarea();
            var span = $(document.getElementById(this.editorElementId));
            
            // title
            span.attr('title', textarea.attr('title'));
        },
        
        __concatStyles: function() {
            var result = "";
            for( var i = 0; i < arguments.length; i++ ) {
                var style = $.trim(arguments[i]);
                if (style) {
                    result = result + style + "; ";
                }
            }
            return result;
        },
        
        __getToolbar: function() {
            var toolbar = this.options.toolbar;
            
            var lowercase = toolbar.toLowerCase();
            if (lowercase === 'basic') {
                return 'Basic';
            }
            if (lowercase === 'full') {
                return 'Full';
            }
            
            return toolbar;
        },
        
        __setOptions: function(options) {
            this.options = $.extend({}, defaultOptions, options);
        },
        
        /**
         * Updates editor configuration and value by synchronizing its settings with options and textarea settings.
         */
        __updateEditorConfiguration: function() {
            var conf = this.__getConfiguration();
            var editor = this.getEditor();
            var textarea = this.__getTextarea();
            
            // toolbar 
            if (editor.config.toolbar !== conf.toolbar) {
                editor.destroy();
                this.__initializeEditor();
                return;
            }
            
            // readonly
            if (this.isReadOnly() !== conf.readOnly) {
                this.setReadOnly(conf.readOnly);
            }
            
            // width & height
            var newWidth = (editor.config.width !== textarea.width()) ?  textarea.width() : null;
            var newHeight = (editor.config.height !== textarea.height()) ?  textarea.height() : null;
            if (newWidth !== null || newHeight !== null) {
                if (newWidth === null) {
                    newWidth = editor.config.width;
                }
                if (newHeight === null) {
                    newHeight = editor.config.height;
                }
                editor.resize(newWidth, newHeight, true);
            }
            
            // styling
            this.__setupStyling();
            
            // pass through attributes
            this.__setupPassThroughAttributes();
            
            // value
            var newValue = textarea.val();
            this.setValue(newValue);
        },
        
        __resolveUnits: function(dimension) {
            var dimension = $.trim(dimension);
            if (dimension.match(/^[0-9]+$/)) {
                return dimension + 'px';
            } else {
                return dimension;
            }
        },
        
        getEditor: function() {
            return this.ckeditor;
        },
        
        setValue: function(newValue) {
            $this = this;
            this.ckeditor.setData(newValue, function() {
                $this.valueChanged = false;
                $this.ckeditor.resetDirty();
            });
        },
        
        getValue: function() {
            return this.ckeditor.getData();
        },
        
        getInput: function() {
            return document.getElementById(this.textareaId);
        },
        
        focus: function() {
            this.ckeditor.focus();
        },
        
        blur: function() {
            this.ckeditor.blur();
        },
        
        isFocused: function() {
            return this.ckeditor.focusManager.hasFocus;
        },
        
        isDirty: function() {
            return this.ckeditor.checkDirty();
        },
        
        isValueChanged: function() {
            return this.valueChanged || this.ckeditor.checkDirty();
        },
        
        setReadOnly: function(readOnly) {
            this.ckeditor.setReadOnly(readOnly !== false);
        },
        
        isReadOnly: function() {
            return this.ckeditor.readOnly;
        },
    
        /**
         * Overrides #destroy method in order to do not destroy
         * editor component when cleaning textarea (needs to be destroyed
         * when cleaning domBinding)
         */
        destroy: function() {
            // do not destroy component here
        },
        
        /**
         * Detaches editor from DOM element and destroys component if necessary (see bellow).
         * 
         * Destroys editor component if detaching from non-textarea element.
         * This method replaces #destroy() when applied to non-textarea elements.
         */
        detach: function(source) {
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