/**
 * A widget providing sorting capabilities for a list of elements
 *
 * @module Select
 * @class orderingList
 */
(function ($) {

  $.widget('rich.orderingList', {

    options: {
      /**
       * Disable the orderingList widget
       *
       * @property disabled
       * @type Boolean
       * @default false
       */
      disabled: false,
      /**
       * The text to use for the orderingList header
       *
       * @property header
       * @type String
       */
      header: null,
      /**
       * The height of the orderingList
       *
       * 1. A String with appropriate units, eg.:
       *        height: '120px'
       *
       * 2. An integer without any units, in which case the units of `px` are assumed.
       *        height: 120
       *
       * @property height
       * @type String
       */
      height: null,
      /**
       * The minimum height of the orderingList
       *
       * 1. A String with appropriate units, eg.:
       *        heightMin: '120px'
       *
       * 2. An integer without any units, in which case the units of `px` are assumed:
       *        heightMin: 120
       *
       * @property heightMin
       * @type String
       */
      heightMin: null,
      /**
       * The maximum height of the orderingList
       *
       * 1. A String with appropriate units, eg.:
       *        heightMax: '120px'
       *
       * 2. An integer without any units, in which case the units of `px` are assumed:
       *        heightMax: 120

       * @property heightMax
       * @type String
       */
      heightMax: null,
      /**
       * A CSS class to be added to the orderingList.
       * Multiple classes should be space separated
       *
       * @property styleClass
       * @type String
       */
      styleClass: null,
      /**
       * When _table_ layout is used,
       * _columnClasses_ specifies a comma-delimited list of CSS style classes to apply to each column.
       * A space separated list of classes may be specified for an individual column.
       *
       * If the number of classes in this list is less than the number of column children, then no class will be
       * applied to the columns greater than the number of classes.  If however the final class in the list is the `*`
       * character, the classes will instead be applied in a repeating manner every n-fold column, where n is the order
       * the class in this list.
       *
       *  If there are more class names than columns, the overflow ones are ignored.
       *
       * @property columnClasses
       * @type String
       */
      columnClasses: null,
      /**
       * When true, show the buttons for ordering the selected elements
       *
       * @property showButtons
       * @type Boolean
       * @default true
       */
      showButtons: true,
      /**
       * Text to be applied to the ordering buttons of the ordering list.  The text should be arranged as an object in
       * JSON notation.
       *
       * eg. {first: ..., up: ..., down: ..., last: ...}
       *
       * @property buttonsText
       * @type JSON
       * @default null
       */
      buttonsText: null,
      /**
       * When true, it is not possible to move a selected element outside of the orderingList
       *
       * @property contained
       * @type Boolean
       * @default true
       */
      contained: true,
      /**
       * When true dragging the mouse over the elements selects multiple elements.  This also reduces the drag handle
       * used to re-order the elements.
       *
       * @property dragSelect
       * @type Boolean
       * @default true
       */
      dragSelect: false,
      /**
       * Allows elements to be dragged onto an empty list when true
       *
       * @property dropOnEmpty
       * @type Boolean
       * @default true
       */
      dropOnEmpty: true,
      /**
       * Allows elements to be ordered by drag and drop with the mouse when true
       *
       * @property mouseOrderable
       * @type Boolean
       * @default true
       */
      mouseOrderable: true,

      /**
       * Function used to sort the elements after an item has been added to the list.
       * `sortFunction` must meet the API requirements of the compareFunction for the
       * [Array.prototype.sort](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort)
       * method.
       *
       * @property sortFunction
       * @type Function
       */
      sortFunction: null,

      // callbacks

      /**
       * Fired when the target list of the orderingList changes values.
       *
       * @event change
       */
      change: null,
      /**
       * Fired after the dynamically created DOM elements of the orderingList have been created
       *
       * @event addDomElements
       */
      addDomElements: null,
      /**
       * Fired after the orderingList has been created
       *
       * @event create
       */
      create: null,
      /**
       * Fired after the orderingList has been destroyed
       *
       * @event destroy
       */
      destroy: null,
      /**
       * Fired when the orderingList receives focus
       *
       * @event focus
       */
      focus: null,
      /**
       * Fired when the orderingList loses focus
       *
       * @event blur
       */
      blur: null
    },

    _create: function () {
      var widget = this;
      this.selectableOptions = {
        disabled: widget.options.disabled
      };
      this.sortableOptions = { handle: this.options.dragSelect ? '.handle' : false,
        disabled: this.options.disabled,
        dropOnEmpty: this.options.dropOnEmpty,
        scroll: true,
        placeholder: 'placeholder',
        tolerance: 'pointer',
        start: function (event, ui) {
          widget.currentItems = ui.item.parent().children('.ui-selected').not('.placeholder').not('.helper-item');
          var helper = ui.helper;
          var placeholder = widget.element.find('.placeholder');
          placeholder.css('height', helper.css('height'));

          widget.currentItems.not(ui.item).hide();
        },
        sort: function (event, ui) {
          var $widget = $(this);
          var helperTop = ui.helper.position().top,
            helperBottom = helperTop + ui.helper.outerHeight();
          $widget.children('.ui-selectee').not('.placeholder').not('.helper-item').not('.ui-selected').each(function () {
            var item = $(this);
            var itemTop = item.position().top;
            var itemMiddle = item.position().top + item.outerHeight() / 2;
            /* if the helper overlaps half of an item, move the placeholder */
            if (helperTop < itemMiddle && itemMiddle < helperBottom) {
              if (itemTop > helperTop) {
                $('.placeholder', $widget).insertAfter(item);
              } else {
                $('.placeholder', $widget).insertBefore(item);
              }
              return false;
            }
          });
        },
        cancel: function (event, ui) {
          widget.currentItems.show();
        },
        over: function (event, ui) {
          if (widget.fillItem) {
            widget._updateFillRow();
          }
        },
        beforeStop: function (event, ui) {
        },
        stop: function (event, ui) {
          var first = widget.currentItems.first();
          if (first.get(0) !== ui.item.get(0)) {
            ui.item.before(first);
            first.after(widget.currentItems.not(first).detach());
          } else {
            ui.item.after(widget.currentItems.not(ui.item).detach());
          }
          widget.currentItems.not('.placeholder').show();
          if (widget.fillItem) {
            widget._updateFillRow();
          }
          var receivingWidget = ui.item.parents('.list').data('orderingList');
          if (receivingWidget !== widget) {
            receivingWidget._checkSort();

            // fire change events here instead of in the 'receive' handler since we manually add/remove the extra drag elements
            var remUi = widget._uiHash();
            remUi.change = 'remove';
            widget._trigger('change', event, remUi);

            var addUi = receivingWidget;
            addUi.change = 'add';
            receivingWidget._trigger('change', event, addUi);
          }
        },
        update: function(event, ui) {
          if (ui.sender === null && ui.item.parents('.ordering-list').get(0) === widget.element.parents('.ordering-list').get(0)) {
            var ui2 = widget._uiHash();
            ui2.change = 'move';
            ui2.movement = 'drag';
            widget._trigger('change', event, ui2);
          }
        },
      };
      if (this.element.is('table')) {
        this.strategy = 'table';
        this.$pluginRoot = this.element.find('tbody');
        this.sortableOptions.items = 'tr:not(.fill-item)';
        this.selectableOptions.filter = 'tr:not(.fill-item)';
        this.sortableOptions.helper = $.proxy(this._rowHelper, this);
      } else {
        this.strategy = 'list';
        this.$pluginRoot = this.element;
        this.selectableOptions.filter = 'li';
        this.sortableOptions.helper = $.proxy(this._listHelper, this);
      }
      if (this.options.contained !== false) {
        this.sortableOptions.containment = this.$pluginRoot;
        this.sortableOptions.axis = 'y';
      }
      // if mouse ordering is disabled buttons have to be shown
      this._addDomElements();
      if (this.options.widgetEventPrefix) {
        this.widgetEventPrefix = this.options.widgetEventPrefix;
      }
      if (this.options.mouseOrderable === true) {
        this.$pluginRoot.sortable(this.sortableOptions);
      }

      this.$pluginRoot.selectable(this.selectableOptions);
      if (this.options.disabled === true) {
        widget._disable();
      }
      this._addDragListeners();
      this.selectList.on('focusin', function (event) {
        widget._trigger('focus', event, widget._uiHash());
      });
      this.selectList.on('focusout', function (event) {
        widget._trigger('blur', event, widget._uiHash());
      });
      if (this.options.height !== null) {
        this._setHeight(this.options.height);
      }
      if (this.options.heightMin !== null) {
        this._setHeightMin(this.options.heightMin);
      }
      if (this.options.heightMax !== null) {
        this._setHeightMax(this.options.heightMax);
      }
    },

    _destroy: function () {
      this._super();
      if (this.options.mouseOrderable === true) {
        this.$pluginRoot.sortable('destroy');
      }
      this.$pluginRoot.selectable('destroy');
      this._removeDomElements();

      // remove empty class attributes                             y
      if (!this.element.attr('class')) {
        this.element.removeAttr('class');
      }
      if (this.strategy === 'table') {
        this.element.children().each(function () {
          var $part = $(this);
          if (!$part.attr('class')) {
            $part.removeAttr('class');
          }
          $part.children().each(function () {
            var $row = $(this);
            if (!$row.attr('class')) {
              $row.removeAttr('class');
            }
            $row.children().each(function () {
              var $cell = $(this);
              if (!$cell.attr('class')) {
                $cell.removeAttr('class');
              }
            });
          });
        });
      } else {
        this.element.children().each(function () {
          var $selectable = $(this);
          if (!$selectable.attr('class')) {
            $selectable.removeAttr('class');
          }
        });
      }
      this._trigger('destroy', undefined, {});
    },

    /** Public API methods **/

    /**
     * Connect the orderingList with another orderingList, allowing items to be dragged between them.
     *
     * @param target {Object} The orderingList with which to connect.
     * @method connectWith
     * @chainable
     */
    connectWith: function (target) {
      if (! this.options.mouseOrderable) {
        return this;
      }
      var targetOrderingList = target.data('richOrderingList');
      this.$pluginRoot.sortable('option', 'connectWith', targetOrderingList.$pluginRoot);
      this._addFillRow();
      target.on('sortover', $.proxy(this._updateFillRow, this));  // own 'out' event causes placeholder interference
      return this;
    },

    /**
     * Determine if the given item is selected
     *
     * @method isSelected
     * @param item {Object} jQuery object of one of the orderingList elements
     * @returns {Boolean} True if the item is selected, false otherwise
     */
    isSelected: function (item) {
      return $(item).hasClass('ui-selected');
    },

    /**
     * Find and return the list of orderingList elements that are selected
     *
     * @method getSelected
     * @returns {Object} A jQuery list of selected items
     */
    getSelected: function() {
      return this.element.find('.ui-selected');
    },

    /**
     * Select the given item
     *
     * @method selectItem
     * @param item {Object} jQuery object of the orderingList element to select
     * @chainable
     */
    selectItem: function (item) {
      $(item).addClass('ui-selected');
      return this;
    },

    /**
     * Un-select the given item
     *
     * @method unSelectItem
     * @param item {Object} jQuery object of the orderingList element to un-select
     * @chainable
     */
    unSelectItem: function (item) {
      $(item).removeClass('ui-selected');
      return this;
    },

    /**
     * Select all items in the orderingList
     *
     * @method selectAll
     * @chainable
     */
    selectAll: function () {
      this.element.find('.ui-selectee').addClass('ui-selected');
      return this;
    },

    /**
     * Un-select all items in the orderingList
     *
     * @method unSelectAll
     * @chainable
     */
    unSelectAll: function () {
      this.element.find('.ui-selected').removeClass('ui-selected');
      return this;
    },

    /**
     * Move the given items to the top of the orderingList
     *
     * @method moveFirst
     * @param items {Object} the items to move to the top
     * @param [event=null] {Object} the event used that triggered this movement
     * @chainable
     */
    moveFirst: function (items, event) {
      event = event || null;
      items = items || $('.ui-selected', this.element);
      if (this.options.disabled) { return; }
      var moved = this.$pluginRoot.find(items);
      var first = moved.prevAll().not('.ui-selected').last();
      var initialState = this.getOrderedKeys();
      $(moved).insertBefore(first);
      var finalState = this.getOrderedKeys();
      if (initialState.toString() !== finalState.toString()) {
        var ui = this._uiHash();
        ui.change = 'move';
        ui.movement = 'moveFirst';
        this._trigger('change', event, ui);
      }
      return this;
    },

    /**
     * Move the given items up one step in the orderingList
     *
     * @method moveUp
     * @param [items] {Object} the items to move up
     * @param [event=null] {Object} the event used that triggered this movement
     * @chainable
     */
    moveUp: function (items, event) {
      event = event || null;
      items = items || $('.ui-selected', this.element);
      if (this.options.disabled) { return; }
      var initialState = this.getOrderedKeys();
      var moved = this.$pluginRoot.find(items);
      $(moved).each(function () {
        var $item = $(this);
        var prev = $item.prevAll().not('.ui-selected').first();
        if (prev.length > 0) {
          $item.insertBefore(prev);
        }
      });
      var finalState = this.getOrderedKeys();
      if (initialState.toString() !== finalState.toString()) {
        var ui = this._uiHash();
        ui.change = 'move';
        ui.movement = 'moveUp';
        this._trigger('change', event, ui);
      }
      return this;
    },

    /**
     * Move the given items down one step in the orderingList
     *
     * @method moveDown
     * @param [items] {Object} the items to move down
     * @param [event=null] {Object} the event used that triggered this movement
     * @chainable
     */
    moveDown: function (items, event) {
      event = event || null;
      items = items || $('.ui-selected', this.element);
      if (this.options.disabled) { return; }
      var initialState = this.getOrderedKeys();
      var moved = this.$pluginRoot.find(items);
      $(moved).sort(function () {
        return 1;
      }).each(function () {
          var $item = $(this);
          var next = $item.nextAll().not('.ui-selected').first();
          if (next.length > 0) {
            $item.insertAfter(next);
          }
        });
      var finalState = this.getOrderedKeys();
      if (initialState.toString() !== finalState.toString()) {
        var ui = this._uiHash();
        ui.change = 'move';
        ui.movement = 'moveDown';
        this._trigger('change', event, ui);
      }
      return this;
    },

    /**
     * Move the given items to the end of the orderingList
     *
     * @method moveLast
     * @param [items] {Object} the items to move to the end
     * @param [event=null] {Object} the event used that triggered this movement
     * @chainable
     */
    moveLast: function (items, event) {
      event = event || null;
      items = items || $('.ui-selected', this.element);
      if (this.options.disabled) { return; }
      var initialState = this.getOrderedKeys();
      var moved = this.$pluginRoot.find(items);
      var last = moved.nextAll().not('.ui-selected').last();
      $(moved).insertAfter(last);
      var finalState = this.getOrderedKeys();
      if (initialState.toString() !== finalState.toString()) {
        var ui = this._uiHash();
        ui.change = 'move';
        ui.movement = 'moveLast';
        this._trigger('change', event, ui);
      }
      return this;
    },

    /**
     * Remove the given items from the orderingList
     *
     * @method remove
     * @param [items] {Object} the items to remove
     * @returns {Object} the items removed from the orderingList
     */
    remove: function (items) {
      items = items || $('.ui-selected', this.element);
      if (!items || items.length === 0) {
        return null;
      }
      var removed = this.$pluginRoot.find(items).detach();
      var ui = this._uiHash();
      ui.change = 'remove';
      this._trigger('change', {}, ui);
      return removed;
    },

    /**
     * Add the given items to the orderingList
     *
     * @method add
     * @param items {Object} the items to add
     * @returns {Object} the items added to the orderingList
     */
    add: function (items) {
      if (!items || items.length === 0) {
        return null;
      }
      this.$pluginRoot.prepend(items);
      this._checkSort();
      var ui = this._uiHash();
      ui.change = 'add';
      this._trigger('change', {}, ui);
      return items;
    },

    /**
     * Retrieve the jQuery list of all items in the orderingList
     *
     * @method getOrderedElements
     * @returns {Object} the jQuery list of selected elements
     */
    getOrderedElements: function () {
      return this.element.find('.ui-selectee');
    },

    /**
     * Retrieve the keys of all items in the orderingList
     *
     * @method getOrderedKeys
     * @returns {Array} An array of the keys of the selected elements
     */
    getOrderedKeys: function () {
      return (this._createKeyArray( this.getOrderedElements()));
    },

    /**
     * Removes the ordering list functionality completely. This will return the element back to its pre-init state.
     *
     * @method destroy
     */
    // method implemented in $.widget

    _setOption: function (key, value) {
      var widget = this;
      if (this.options.key === value) {
        return;
      }
      switch (key) {
        case 'disabled':
          if (value === true) {
            widget._disable();
          } else {
            widget._enable();
          }
          break;
        case 'header':
          if (!widget.header) {
            widget._addHeader();
          }
          widget.header.text(value);
          break;
        case 'height':
          widget._setHeight(value);
          break;
        case 'heightMin':
          widget._setHeightMin(value);
          break;
        case 'heightMax':
          widget._setHeightMax(value);
          break;
        case 'columnClasses':
          if (widget.options.columnClasses) {
            widget._removeColumnClasses(widget.options.columnClasses);
          }
          widget._addColumnClasses(value);
          break;
        case 'styleClass':
          if (widget.options.styleClass) {
            widget.selectList.removeClass(this.options.styleClass);
          }
          widget.selectList.addClass(value);
          break;
        case 'buttonsText':
          this._applyButtonsText(this.selectList.find('.btn-group-vertical'), value);
          break;
      }
      this._super(key, value);
    },

    _addDragListeners: function() {
      var widget = this;
      if (this.options.dragSelect === false) {
        this.element.on('mousedown', '.ui-selectee', function (event) {
          var item = $(this);
          if (widget.selectList.get(0) !== document.activeElement) {
            widget.selectList.focus();
          }
          var list = item.parents('.list').first();
          list.data('richOrderingList').mouseStarted = true;
        });
        this.$pluginRoot.on('mousemove', '.ui-selectee', function (event) {
          var item = $(this);
          var list = item.parents('.list').first();
          var orderingList = list.data('richOrderingList');
          if (orderingList.mouseStarted) {
            orderingList.mouseStarted = false;
            if (!item.hasClass('ui-selected')) {
              var selectable = orderingList.$pluginRoot.data('uiSelectable');
              selectable._mouseStart(event);
              selectable._mouseStop(event);
            }
          }
        });
        this.element.on('mouseup', '.ui-selectee', function (event) {
          var item = $(this);
          var list = item.parents('.list').first();
          var orderingList = list.data('richOrderingList');
          if (orderingList.mouseStarted) {
            orderingList.mouseStarted = false;
            var selectable = orderingList.$pluginRoot.data('uiSelectable');
            selectable._mouseStart(event);
            selectable._mouseStop(event);
          }
        });
      } else {
        this.element.find('.handle').on('mousedown', function (event) {
          var item = $(this).parents('.ui-selectee').first();
          if (!item.hasClass('ui-selected')) {
            var list = item.parents('.list').first();
            var selectable = list.data('richOrderingList').$pluginRoot.data('uiSelectable');
            selectable._mouseStart(event);
            selectable._mouseStop(event);
          }
        });
      }
    },

    _removeDragListeners: function() {
      if (this.options.dragSelect === false) {
        this.element.off('mousedown', '.ui-selectee');
        this.element.off('mousemove', '.ui-selectee');
        this.element.off('mouseup', '.ui-selectee');
      } else {
        this.element.find('.handle').off('mousedown');
      }
    },

    _listHelper: function (e, item) {
      var $helper = $('<ol />').addClass('helper')
        .css('height', 'auto').css('width', this.element.css('width'));
      item.parent().children('.ui-selected').not('.ui-sortable-placeholder').clone().addClass('helper-item').show().appendTo($helper);
      return $helper;
    },

    _rowHelper: function (e, item) {
      var $helper = $('<div />').addClass('helper').css('height', 'auto');
      item.parent().children('.ui-selected').not('.ui-sortable-placeholder').clone().addClass('helper-item').show().appendTo($helper);
      /* we lose the cell width in the clone, so we re-set it here: */
      var firstRow = $helper.children('tr').first();
      /* we only need to set the column widths on the first row */
      firstRow.children().each(function (colindex) {
        var originalCell = item.children().get(colindex);
        var originalWidth = $(originalCell).css('width');
        $(this).css('width', originalWidth);
      });
      return $helper;
    },

    _createKeyArray: function (items) {
      var keys = [];
      items.each(function () {
        var $this = $(this);
        var dataKey = $this.data('key');
        var key = (typeof dataKey !== 'undefined') ? dataKey : $this.text();
        keys.push(key);
      });
      return keys;
    },

    _checkSort: function () {
      if (this.options.sortFunction && typeof this.options.sortFunction === 'function') {
        var sortedElements = this.getOrderedElements().sort(this.options.sortFunction);
        var widget = this;
        sortedElements.each(function() {
          widget.element.append(this);
        });
      }
    },

    /** Initialisation methods **/

    _addDomElements: function () {
      this._addParents();
      this._addMouseHandles();
      if (this.options.showButtons === true) {
        this._addButtons();
      }
      if (this.strategy === 'table') { /* round the table row corners */
        var widget = this;
        this.element.find('tr').each(function () {
            var $tr = $(this);
            var children = $tr.children();
            children.last().addClass('last');
            children.first().addClass('first');
            if (widget.options.columnClasses) {
              widget._addColumnClassesToCells(children, widget.options.columnClasses);
            }
          });
      }
      this._trigger('addDomElements', undefined, this._uiHash());
    },

    _addColumnClasses: function(columnClasses) {
      if (this.strategy !== 'table') {
        return;
      }
      var widget = this;
      this.element.find('tr').each(function () {
          widget._addColumnClassesToCells($(this).children(), columnClasses);
        });
    },

    _addColumnClassesToCells: function(cells, columnClassesString) {
      var columnClasses = this._splitColumnClasses(columnClassesString);
      var repeat = $.trim(columnClasses[columnClasses.length - 1]) === '*';
      var length = repeat ? columnClasses.length - 1 : columnClasses.length;
      cells.each(function(count) {
        if (repeat || count < columnClasses.length) {
          var columnClass = columnClasses[count % length];
          $(this).addClass(columnClass);
        } else {
          return false;
        }
      });
    },

    _splitColumnClasses: function(string) {
      var array = string.split(',');
      $.each(array, function(index, value) {
        array[index] = $.trim(value);
      });
      return array;
    },

    _addButtons: function () {
      var buttonStack = $('<div/>')
        .addClass('btn-group-vertical');
      this._addButton(buttonStack, 'first', $.proxy(this._firstHandler, this));
      this._addButton(buttonStack, 'up', $.proxy(this._upHandler, this));
      this._addButton(buttonStack, 'down', $.proxy(this._downHandler, this));
      this._addButton(buttonStack, 'last', $.proxy(this._lastHandler, this));
      if (this.options.buttonsText) {
        this._applyButtonsText(buttonStack, this.options.buttonsText);
      }
      this.content.append(
        $('<div />').addClass('button-column').append(buttonStack));
    },

    _applyButtonsText: function(buttonStack, buttonsText) {
      this._applyButtonText(buttonStack.find('.btn-first'), buttonsText.first);
      this._applyButtonText(buttonStack.find('.btn-up'), buttonsText.up);
      this._applyButtonText(buttonStack.find('.btn-down'), buttonsText.down);
      this._applyButtonText(buttonStack.find('.btn-last'), buttonsText.last);
    },

    _applyButtonText: function(button, text) {
      if (!text) {
        if (button.hasClass('labeled')) {
          button.removeClass('labeled');
          button.find('span').remove();
        }
        return;
      }
      if (button.hasClass('labeled')) {
        button.find('span').text(text);
      } else {
        button.addClass('labeled').append($('<span />').text(text));
      }
    },

    _addButton: function (buttonStack, buttonClass, handler) {
      var button = $('<button/>')
        .attr('type', 'button')
        .addClass('btn btn-default')
        .addClass('btn-' + buttonClass)
        .on('click.orderingList', handler)
        .append($('<i />').addClass('fa richicon-' + buttonClass));
      buttonStack.append(button);
    },

    _addMouseHandles: function () {
      if (this.options.mouseOrderable !== true) {
        return;
      }
      if (this.options.dragSelect === true) {
        this.content.addClass('with-handle');
        if (this.strategy === 'table') {
          this.element
            .find('tbody > tr')
            .prepend('<th class="handle"><i class="fa fa-arrows"></i></th>');
          this.element
            .find('thead > tr')
            .prepend('<th class="handle"></th>');
        } else if (this.strategy === 'list') {
          this.element
            .find('li')
            .prepend('<div class="handle"><i class="fa fa-arrows"></i></div>');
        }
      }
    },

    _addParents: function () {
      this.element.addClass('list').wrap(
        $('<div />').addClass('ordering-list select-list').attr('tabindex', -1)
          .focus(function(){
            var x = window.scrollX, y = window.scrollY;
            setTimeout(function() {
              window.scrollTo(x,y);
            }, 0);
          })
          .append(
          $('<div />').addClass('content').append(
            $('<div />').addClass('scroll-box')
          )
        )
      );
      this.selectList = this.element.parents('.select-list').first();
      if (this.options.styleClass) {
        this.selectList.addClass(this.options.styleClass);
      }
      if (this.options.header) {
        this._addHeader();
      }
      this.content = this.selectList.find('.content');
    },

    _addHeader: function() {
      var header = $('<div />').addClass('header');
      header.html(this.options.header);
      this.selectList.prepend(header);
      this.header = header;
    },

    _addFillRow: function() {
      var connectedList = this.$pluginRoot.sortable( 'option', 'connectWith' );
      if (!connectedList || this.strategy !== 'table') {
        return;
      }

      var itemsSelector = this.$pluginRoot.sortable( 'option', 'items' );
      var children = this.$pluginRoot.find(itemsSelector);
      var child;

      if (children.length > 0) {
        child = children.first();
      } else {
        var connectedChildren = $(connectedList).find('tr');
        if (connectedChildren.length > 0) {
          child = connectedChildren.first();
        }
      }
      if (child) {
        var fillItem = child.clone();
        fillItem.removeClass().addClass('fill-item').removeClass('ui-selectee');
        fillItem.find('td').empty();
        fillItem.data('key', undefined);
        this.$pluginRoot.append(fillItem);
        this.fillItem = fillItem;
        this.element.on(this.options.widgetEventPrefix + 'change', $.proxy(this._updateFillRow, this));
      }
      this._updateFillRow();
    },

    _updateFillRow: function() {
      if (this.fillItem) {
        this.fillItem.css('height', '0');
        var table = this.fillItem.parents('table').first();
        var tbody = this.fillItem.parents('tbody').first();
        var scrollBox = this.fillItem.parents('.scroll-box').first();
        this.fillItem.detach();
        var height = scrollBox.height() - table.height();
        var placeholder = this.element.find('.placeholder');
        if (placeholder) {
          height = height - placeholder.height();
        }
        this.fillItem.height(height);
        this.fillItem.toggle((height > 2));
        tbody.append(this.fillItem);
      }
    },

    _setHeight: function(height) {
      this.selectList.find('.scroll-box').css('height', height);
    },

    _setHeightMin: function(height) {
      this.selectList.find('.scroll-box').css('min-height', height);
    },

    _setHeightMax: function(height) {
      this.selectList.find('.scroll-box').css('max-height', height);
    },

    _disable: function () {
      this.$pluginRoot
        .sortable('option', 'disabled', true)
        .selectable('option', 'disabled', true);
      this.element
        .addClass('disabled')
        .find('.ui-selected').removeClass('ui-selected');
      this.element.find('.ui-selectee').removeClass('ui-selectee').addClass('ui-disabled');
      $('.button-column', this.content).find('button').attr('disabled', true);
      this._removeDragListeners();
    },

    _enable: function () {
      this.$pluginRoot
        .sortable('option', 'disabled', false)
        .selectable('option', 'disabled', false);
      this.element.removeClass('disabled');
      this.element.find('.ui-disabled').removeClass('ui-disabled').addClass('ui-selectee');
      $('.button-column', this.content).find('button').attr('disabled', false);
      this._addDragListeners();
    },

    /**
     * A dump of the current state of the widget
     *
     * @method _uiHash
     * @private
     * @returns {Object} A `ui` object holding the current state.  The `ui` object has the properties:
     * ````javascript
     * {
     *   change: 'move'| 'add' | 'remove',
     *   movement: 'drag' | 'moveUp' | 'moveFirst' | 'moveDown' | 'moveLast'
     * }
     * ````
     */
    _uiHash: function () {
      var ui = {};
      ui.orderedElements = this.getOrderedElements();
      ui.orderedKeys = this.getOrderedKeys();
      return ui;
    },

    /** Cleanup methods **/

    _removeDomElements: function () {
      this.element.find('.ui-selected').removeClass('ui-selected');
      if (this.strategy === 'table') { /* round the table row corners */
        var widget = this;
        this.element.find('tr').each(function () {
            var $tr = $(this);
            var children = $tr.children();
            children.last().removeClass('last');
            children.first().removeClass('first');
            if (widget.options.columnClasses) {
              widget._removeColumnClassesFromCells(children, widget.options.columnClasses);
            }
          });
        if (this.fillItem) {
          this.element.find('.fill-item').remove();
        }

      }
      var list = this.element.detach();
      this.selectList.replaceWith(list);
      if (this.options.dragSelect === true) {
        this.content.removeClass('with-handle');
        this.element.find('.handle').remove();
      }
      if (this.options.disabled === true) {
        this.element.removeClass('disabled');
        this.element.find('.ui-disabled').removeClass('ui-disabled');
      }
      this.element.removeClass('list');
    },

    _removeColumnClasses: function(columnClasses) {
      if (this.strategy !== 'table') {
        return;
      }
      var widget = this;
      this.element.find('tr').each(function() {
        widget._removeColumnClassesFromCells($(this).children(), columnClasses);
      });
    },

    _removeColumnClassesFromCells: function(cells, columnClassesString) {
      var columnClasses = this._splitColumnClasses(columnClassesString);
      var repeat = $.trim(columnClasses[columnClasses.length - 1]) === '*';
      var length = repeat ? columnClasses.length - 1 : columnClasses.length;
      cells.each(function(count) {
        if (repeat || count < columnClasses.length) {
          var columnClass = columnClasses[count % length];
          $(this).removeClass(columnClass);
        } else {
          return false;
        }
      });
    },

    /** Event Handlers **/

    _firstHandler: function (event) {
      this.moveFirst($('.ui-selected', this.element), event);
    },

    _upHandler: function (event) {
      this.moveUp($('.ui-selected', this.element), event);
    },

    _downHandler: function (event) {
      this.moveDown($('.ui-selected', this.element), event);
    },

    _lastHandler: function (event) {
      this.moveLast($('.ui-selected', this.element), event);
    }

  });

}(jQuery));
