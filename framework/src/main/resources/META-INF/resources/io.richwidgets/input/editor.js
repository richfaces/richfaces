(function($) {

  $.widget('rich.editor', {

    options : {
      toolbar : 'Basic'
    },

    DIRTY_EVENTS: [ 'key', 'paste', 'undo', 'redo' ],

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

    getEditor: function() {
      return this.editorInstance;
    },

    editor: function() {
      return this.getEditor();
    },

    setValue: function(newValue) {

    },

    value: function(newValue) {
      if (newValue === undefined) {
        return this.getEditor().getData();
      } else {
        this.getEditor().setData(newValue);
      }
    },

    focus: function() {
      this.getEditor().focus();
    },

    blur: function() {
      this.getEditor().forceBlur();
    },

    focused: function() {
      return this.getEditor().focusManager.hasFocus;
    },

    dirty: function() {
      return this.dirtyState || this.getEditor().checkDirty();
    },

    isValueChanged: function() {
      return this.valueChanged || this.isDirty();
    },

    readOnly: function(readOnly) {
      if (readOnly === undefined) {
        return this.getEditor().readOnly;
      } else {
        this.getEditor().setReadOnly(readOnly !== false);
      }
    },

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