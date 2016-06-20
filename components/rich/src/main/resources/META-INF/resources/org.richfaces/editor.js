/**
 * @author Lukas Fryc
 */

(function($, rf) {
    rf.ui = rf.ui || {};

    /**
     * Default component configuration
     */
    var defaultOptions = {
        toolbar : 'Basic',
        skin: 'moono',
        readonly : false,
        style : '',
        styleClass : '',
        editorStyle : '',
        editorClass : '',
        width : '100%',
        height : '200px'
    };
    
    var eventsForDirty = [ "key", "paste", "undo", "redo" ];

    /**
     * Backing object for rich:editor
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Editor
     * 
     * @param componentId {string} component id
     * @param options {object} editor options
     * @param config {object} CKeditor configuration
     */
    rf.ui.Editor = function(componentId, options, config) {
        $super.constructor.call(this, componentId);
        this.options = $.extend({}, defaultOptions, options);

        this.componentId = componentId;
        this.textareaId = componentId + ':inp';
        this.editorElementId = 'cke_' + this.textareaId;
        this.valueChanged = false;
        this.dirtyState = false;
        this.config = $.extend({}, config);

        this.attachToDom(this.componentId);

        $(document).ready($.proxy(this.__initializationHandler, this));
        rf.Event.bindById(this.__getTextarea(), 'init', this.options.oninit, this);
        rf.Event.bindById(this.__getTextarea(), 'dirty', this.options.ondirty, this);
    };

    rf.BaseComponent.extend(rf.ui.Editor);

    var $super = rf.ui.Editor.$super;

    $.extend(rf.ui.Editor.prototype, {

        name : "Editor",

        __initializationHandler : function() {
            this.ckeditor = CKEDITOR.replace(this.textareaId, this.__getConfiguration());

            // register event handlers
            if (this.__getForm()) {
                this.__updateTextareaHandlerWrapper = rf.Event.bind(this.__getForm(), 'ajaxsubmit', $.proxy(this.__updateTextareaHandler, this));
            }
            this.ckeditor.on('instanceReady', $.proxy(this.__instanceReadyHandler, this));
            this.ckeditor.on('blur', $.proxy(this.__blurHandler, this));
            this.ckeditor.on('focus', $.proxy(this.__focusHandler, this));
            // register handlers for 'dirty' event
            for (var i = 0; i < eventsForDirty.length; i++) {
                this.ckeditor.on(eventsForDirty[i], $.proxy(this.__checkDirtyHandlerWithDelay, this));
            }
            // interval for dirty checking
            this.dirtyCheckingInterval = window.setInterval($.proxy(this.__checkDirtyHandler, this), 100);
        },
        
        __checkDirtyHandlerWithDelay : function() {
            window.setTimeout($.proxy(this.__checkDirtyHandler, this), 0);
        },
        
        __checkDirtyHandler : function() {
            if (this.ckeditor.checkDirty()) {
                this.dirtyState = true;
                this.valueChanged = true;
                this.ckeditor.resetDirty();
                this.__dirtyHandler();
            }
        },
        
        __dirtyHandler : function() {
            this.invokeEvent.call(this, "dirty", document.getElementById(this.textareaId));
        },
        
        __updateTextareaHandler : function() {
            this.ckeditor.updateElement();
        },

        __instanceReadyHandler : function(e) {
            this.__setupStyling();
            this.__setupPassThroughAttributes();

            this.invokeEvent.call(this, "init", document.getElementById(this.textareaId), e);
        },

        __blurHandler : function(e) {
            this.invokeEvent.call(this, "blur", document.getElementById(this.textareaId), e);
            if (this.isDirty()) {
                this.valueChanged = true;
                this.__changeHandler();
            }
            this.dirtyState = false;
        },

        __focusHandler : function(e) {
            this.invokeEvent.call(this, "focus", document.getElementById(this.textareaId), e);
        },

        __changeHandler : function(e) {
            this.invokeEvent.call(this, "change", document.getElementById(this.textareaId), e);
        },

        __getTextarea : function() {
            return $(document.getElementById(this.textareaId));
        },

        /**
         * @private
         * 
         * Returns the form where this editor component is placed
         */
        __getForm : function() {
            return $('form').has(this.__getTextarea()).get(0);
        },

        __getConfiguration : function() {
            var textarea = this.__getTextarea();
            return $.extend({
                skin : this.options.skin,
                toolbar : this.__getToolbar(),
                readOnly : textarea.attr('readonly') || this.options.readonly,
                width : this.__resolveUnits(this.options.width),
                height : this.__resolveUnits(this.options.height),
                bodyClass : 'rf-ed-b',
                defaultLanguage : this.options.lang,
                contentsLanguage : this.options.lang
            }, this.config);
        },

        __setupStyling : function() {
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

        __setupPassThroughAttributes : function() {
            var textarea = this.__getTextarea();
            var span = $(document.getElementById(this.editorElementId));

            // title
            span.attr('title', textarea.attr('title'));
        },

        __concatStyles : function() {
            var result = "";
            for ( var i = 0; i < arguments.length; i++) {
                var style = $.trim(arguments[i]);
                if (style) {
                    result = result + style + "; ";
                }
            }
            return result;
        },

        __getToolbar : function() {
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

        __setOptions : function(options) {
            this.options = $.extend({}, defaultOptions, options);
        },

        __resolveUnits : function(dimension) {
            var dimension = $.trim(dimension);
            if (dimension.match(/^[0-9]+$/)) {
                return dimension + 'px';
            } else {
                return dimension;
            }
        },

        /**
         * Get the CKEditor object instance
         * 
         * @method
         * @name RichFaces.ui.Editor#getEditor
         * @return {CKEDITOR} CKEditor instance
         */
        getEditor : function() {
            return this.ckeditor;
        },

        /**
         * Set value
         * 
         * @method
         * @name RichFaces.ui.Editor#setValue
         * @param value {string} new value
         */
        setValue : function(newValue) {
            this.ckeditor.setData(newValue, $.proxy(function() {
                this.valueChanged = false;
                this.dirtyState = false;
                this.ckeditor.resetDirty();
            }, this));
        },

        /**
         * Get current value
         * 
         * @method
         * @name RichFaces.ui.Editor.getValue
         * @return {string} current value
         */
        getValue : function() {
            return this.ckeditor.getData();
        },

        getInput : function() {
            return document.getElementById(this.textareaId);
        },

        /**
         * Set focus to this component
         * 
         * @method
         * @name RichFaces.ui.Editor#focus
         */
        focus : function() {
            this.ckeditor.focus();
        },

        /**
         * Remove focus from this component
         * 
         * @method
         * @name RichFaces.ui.Editor#blur
         */
        blur : function() {
            this.ckeditor.focusManager.blur(true);
        },

        /**
         * Returns true if the component is focused
         * 
         * @method
         * @name RichFaces.ui.Editor#isFocused
         * @return {boolean} true if the editor is focused
         */
        isFocused : function() {
            return this.ckeditor.focusManager.hasFocus;
        },

        /**
         * Returns true if the editor is in a dirty state, i.e. edited since last receiving focus
         * 
         * @method
         * @name RichFaces.ui.Editor#isDirty
         * @return {boolean} true if the editor is in a dirty state
         */
        isDirty : function() {
            return this.dirtyState || this.ckeditor.checkDirty();
        },

        /**
         * Returns true of the value has changed from default
         * 
         * @method
         * @name RichFaces.ui.Editor#isValueChanged
         * @return {boolean} true if the value is changed
         */
        isValueChanged : function() {
            return this.valueChanged || this.isDirty();
        },

        /**
         * Set the readonly state
         * 
         * @method
         * @name RichFaces.ui.Editor#setReadOnly
         * @param readOnly {boolean} the new state
         */
        setReadOnly : function(readOnly) {
            this.ckeditor.setReadOnly(readOnly !== false);
        },

        /**
         * Returns true if the editor is read-only
         * 
         * @method
         * @name RichFaces.ui.Editor#isReadOnly
         * @return {boolean} true if the editor is read-only
         */
        isReadOnly : function() {
            return this.ckeditor.readOnly;
        },

        destroy : function() {
            window.clearInterval(this.dirtyCheckingInterval);
            
            if (this.__getForm()) {
                rf.Event.unbind(this.__getForm(), 'ajaxsubmit', this.__updateTextareaHandlerWrapper);
            }

            if (this.ckeditor) {
                this.ckeditor.destroy();
                this.ckeditor = null;
            }

            this.__getTextarea().show();

            $super.destroy.call(this);
        }
    });
})(RichFaces.jQuery, RichFaces);
