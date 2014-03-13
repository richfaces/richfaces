/* global CKEDITOR */
/**
 * Editor widget for powerfull WYSIWYG editor based on CKEditor 4.
 *
 * For various options check documentation for CKEditor: http://docs.ckeditor.com/
 *
 * (You can pass the same options to the widget options as you would pass in CKEditor config / options.)
 *
 * @module Input
 * @class editor
 * @uses CKEDITOR
 */
(function($) {

  $.widget('rich.editor', {

    /* OPTIONS */

    options : {
      /**
       * The toolbox (alias toolbar) definition. It is a toolbar name or an array of toolbars (strips), each one being also an array, containing a list of UI items.
       *
       * Otherwise the Editor widget share all options with the CKEditor 4 (http://docs.ckeditor.com/).
       *
       * @property toolbar
       * @type Integer
       * @default 'Basic'
       */
      toolbar: 'Basic',

      /**
       * Do not load config as it is loaded manually
       */
      customConfig: '',

      /**
       * Overrides default Basic toolbar button set
       */
      'toolbar_Basic': [
        ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink']
      ],

      /**
       * Overrides default Full toolbar button set
       */
      'toolbar_Full': [
        { name: 'document',   items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },
        { name: 'clipboard',  items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
        { name: 'editing',    items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
        { name: 'forms',    items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
        '/',
        { name: 'basicstyles',  items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
        { name: 'paragraph',  items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
        { name: 'links',    items : [ 'Link','Unlink','Anchor' ] },
        { name: 'insert',   items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] },
        '/',
        { name: 'styles',   items : [ 'Styles','Format','Font','FontSize' ] },
        { name: 'colors',   items : [ 'TextColor','BGColor' ] },
        { name: 'tools',    items : [ 'Maximize', 'ShowBlocks'] }
      ]
    },

    DIRTY_EVENTS: [ 'key', 'paste', 'undo', 'redo' ],

    /* LIFECYCLE METHODS */

    _create : function() {
      this.dirtyState = false;
      this.valueChanged = false;
      this.options = $.extend(this.options, {
        on: this._getHandlers()
      });
      this.element.on('editorblur', $.proxy(this._triggerChange, this));
      if (this.element.is('textarea')) {
        this.editorInstance = CKEDITOR.replace(this.element.get(0), this.options);
      } else {
        this.editorInstance = CKEDITOR.inline(this.element.get(0), this.options);
      }
      this.dirtyCheckingInterval = window.setInterval($.proxy(this._dirtyCheck, this), 100);
    },

    _destroy: function() {
      window.clearInterval(this.dirtyCheckingInterval);
      this.editor().destroy();
    },

    /* PUBLIC METHODS */

    /**
     * Returns CKEditor instance.
     *
     * @method editor
     */
    editor: function() {
      return this.editorInstance;
    },

    /**
     * Read or write editor's value:
     *
     * * value() - returns a current value
     * * value(newValue) - setup a new value
     *
     * Note: this method is asynchronous. The callback parameter must be used if interaction with the editor is needed after setting the data.
     *
     * @method value
     * @param newValue HTML code to replace the curent content in the editor.
     * @param callback Function to be called after the value is set.
     */
    value: function(newValue, callback) {
      if (newValue === undefined) {
        return this.editor().getData();
      } else {
        this.editor().setData(newValue, callback);
      }
    },

    /**
     * Focus the editor
     *
     * @method focus
     */
    focus: function() {
      this.editor().focus();
    },

    /**
     * Blur the editor
     *
     * @method blur
     */
    blur: function() {
      this.editor().focusManager.blur(true);
    },

    /**
     * Checks whether the editor is focused
     *
     * @method isFocused
     * @returns {boolean}
     */
    isFocused: function() {
      return this.editor().focusManager.hasFocus;
    },

    /**
     * Checks whether the editor value has changed since last focus event.
     *
     * @method isDirty
     * @returns {boolean}
     */
    isDirty: function() {
      return this.dirtyState || this.editor().checkDirty();
    },

    /**
     * Checks whether the editor value has changed from its initial state.
     *
     * @method isValueChanged
     * @returns {boolean}
     */
    isValueChanged: function() {
      return this.valueChanged || this.isDirty();
    },

    /**
     * Switches the editor to read-only mode or checks whether it is in read-only mode.
     *
     * * readOnly() - returns a value
     * * readOnly(true) - switches the editor to read-only mode
     *
     * @method readOnly
     * @param {boolean} [readOnly] optional
     * @returns {boolean}
     */
    readOnly: function(readOnly) {
      if (readOnly === undefined) {
        return this.editor().readOnly;
      } else {
        this.editor().setReadOnly(readOnly);
        return readOnly;
      }
    },

    /* EVENTS */

    /**
     * Triggered when the Editor instance is fully initialized
     *
     * @event init
     */

    /**
     * Triggered when the Editor is blurred
     *
     * @event blur
     */

    /**
     * Triggered when the field is focused
     *
     * @event focus
     */

    /**
     * Triggered everytime the editors value changes.
     *
     * Note: beware, this event is fired for each single action user takes that changes the value
     *
     * @event dirty
     */

    /**
     * Triggered when the field is blurred, if the value has changed.
     *
     * @event change
     */

    /* PRIVATE METHODS */

    // returns a hash of handlers for CKEditor 'on' property
    _getHandlers: function() {
      var widget = this;
      var handlers = {
        instanceReady: function(ev) {
          widget._trigger('init', ev);
        },
        blur: function(ev) {
          widget._trigger('blur', ev);
        },
        focus: function(ev) {
          widget._trigger('focus', ev);
        }
      };
      $.each(this.DIRTY_EVENTS, function(eventName) {
        handlers[eventName] = function(ev) {
          window.setTimeout($.proxy(widget._dirtyCheck, widget), 0, ev);
        };
      });
      return handlers;
    },

    // setup appropriate flags when the editor reports that its value is "dirty"
    _dirtyCheck: function(ev) {
      if (this.editor().checkDirty()) {
        this.dirtyState = true;
        this.valueChanged = true;
        this.editor().resetDirty();
        this._trigger('dirty', ev);
      }
    },

    // triggers a change event
    _triggerChange: function(ev) {
      if (this.isDirty()) {
        this.valueChanged = true;
        this._trigger('change', ev);
      }
      this.dirtyState = false;
    }

  });

  /**
   * Disables automatic enhancement of all contanteditable areas to avoid conflicts.
   */
  $(function() {
    CKEDITOR.disableAutoInline = true;
  });

}(jQuery));