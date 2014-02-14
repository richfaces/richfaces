(function($) {

  $.widget('rich.editor', {

    options : {
      toolbar : 'Basic'
    },

    DIRTY_EVENTS: [ 'key', 'paste', 'undo', 'redo' ],

    /* LIFECYCLE METHODS */

    _create : function() {
      this.dirtyState = false;
      this.valueChanged = false;
      this.options = $.extend(this.options, {
        on: this._getHandlers()
      });
      $(this.element).on('editorblur', $.proxy(this._triggerChange, this));
      this.editorInstance = $(this.element).ckeditor(this.options).editor;
      this.dirtyCheckingInterval = window.setInterval($.proxy(this._dirtyCheck, this), 100);
    },

    _destroy: function() {
      this.getEditor().destroy();
    },

    /* PUBLIC METHODS */

    getEditor: function() {
      return this.editorInstance;
    },

    editor: function() {
      return this.getEditor();
    },

    setValue : function(newValue) {
      this.getEditor().setData(newValue);
    },

    getValue : function() {
      return this.getEditor().getData();
    },

    getInput : function() {
      return this.element;
    },

    focus : function() {
      this.getEditor().focus();
    },

    blur : function() {
      this.getEditor().focusManager.blur(true);
    },

    isFocused : function() {
      return this.getEditor().focusManager.hasFocus;
    },

    isDirty : function() {
      return this.dirtyState || this.getEditor().checkDirty();
    },

    isValueChanged : function() {
      return this.valueChanged || this.isDirty();
    },

    setReadOnly : function(readOnly) {
      this.getEditor().setReadOnly(readOnly !== false);
    },

    isReadOnly : function() {
      return this.getEditor().readOnly;
    },

    /* PRIVATE METHODS */

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

    _dirtyCheck: function(ev) {
      if (this.getEditor().checkDirty()) {
        this.dirtyState = true;
        this.valueChanged = true;
        this.getEditor().resetDirty();
        this._trigger('dirty', ev);
      }
    },

    _triggerChange: function(ev) {
      if (this.isDirty()) {
        this.valueChanged = true;
        this._trigger('change', ev);
      }
      this.dirtyState = false;
    }

  });

}(jQuery));