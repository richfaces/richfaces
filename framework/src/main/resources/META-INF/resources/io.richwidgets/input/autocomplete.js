/**
 * Autocomplete enables users to quickly find and select from a pre-populated list of values as they type, leveraging searching and filtering.
 *
 * The widget is based on popular jQuery UI Autocomplete widget, but it brings some advanced built-in features:
 *
 * * caching
 * * tokenizing
 * * using DOM as a source of suggestions
 * * auto-fill
 * * built-in button
 *
 * @module Input
 * @class autocomplete
 * @uses $.ui.autocomplete
 */
(function ($) {

  var LAYOUT = {
    list: 0,
    table: 1
  };

  $.widget('rich.autocomplete', $.ui.autocomplete, {

    LAYOUT: LAYOUT,

    options: {

      /* OPTIONS */

      /**
       * The delay in milliseconds between when a keystroke occurs and when a search is performed. A zero-delay makes sense for local data (more responsive), but can produce a lot of load for remote data, while being less responsive.
       *
       * @property delay
       * @type Integer
       * @default 0
       */
      delay: 0,

      /**
       * Allows to repeat auto-completion several times by using separator between words.
       *
       * You can provide multiple tokens.
       *
       * @property token
       * @type String
       * @default null
       */
      token: '',

      /**
       * Allows to append a button to the input to allow opening suggestion box by mouse.
       *
       * @property showButton
       * @type Boolean
       * @default false
       */
      showButton: false,

      /**
       * Auto focusing mode selects first suggestion in a list once it appears, so user can just confirm it using enter key.
       *
       * @property autoFocus
       * @type Boolean
       * @default false
       */
      autoFocus: false,

      /**
       * Auto filling mode previews a selected suggestion in the input as a user types.
       *
       * @property autoFill
       * @type Boolean
       * @default false
       */
      autoFill: false,

      /**
       * Defines the data to use, must be specified.
       *
       * **Multiple types supported:**
       *
       * * **Array:** An array can be used for local data.There are two supported formats:
       *    * An array of strings: `[ "Choice1", "Choice2" ]`
       *    * An array of objects with `label` and `value` properties: `[ { label: "Choice1", value: "value1" }, ... ]`
       *
       *    The label property is displayed in the suggestion menu. The value will be inserted into the input element when a user selects an item. If just one property is specified, it will be used for both, e.g., if you provide only value properties, the value will also be used as the label.
       *
       * * **String:** When a string is used, the Autocomplete plugin expects that string to point to a URL resource that will return JSON data. It can be on the same host or on a different one (must provide JSONP). The Autocomplete plugin does not filter the results, instead a query string is added with a term field, which the server-side script should use for filtering the results. For example, if the source option is set to "http://example.com" and the user types foo, a GET request would be made to http://example.com?term=foo. The data itself can be in the same format as the local data described above.
       *
       * * **Function:** The third variation, a callback, provides the most flexibility and can be used to connect any data source to Autocomplete. The callback gets two arguments:
       *    * A request object, with a single term property, which refers to the value currently in the text input. For example, if the user enters "new yo" in a city field, the Autocomplete term will equal "new yo".
       *    * A response callback, which expects a single argument: the data to suggest to the user. This data should be filtered based on the provided term, and can be in any of the formats described above for simple local data. It's important when providing a custom source callback to handle errors during the request. You must always call the response callback even if you encounter an error. This ensures that the widget always has the correct state.
       *
       *    When filtering data locally, you can make use of the built-in $.ui.autocomplete.escapeRegex function. It'll take a single string argument and escape all regex characters, making the result safe to pass to new RegExp().
       *
       * @property source
       * @required
       */
      source: [],

      /**
       * The minimum number of characters a user must type before a search is performed. Zero is useful for local data with just a few items, but a higher value should be used when a single character search could match a few thousand items.
       *
       * @property minLength
       * @type Number
       * @default 0
       */
      minLength: 0,

      /**
       * Turns suggestions caching on.
       *
       * In caching mode, autocomplete first tries to look suggestions for given term up in cache given by
       * `cacheImplementation` option and if no suggestions are found, autocomplete uses classic `source`
       * to retrieve suggestions.
       *
       * @property cached
       * @type Boolean
       * @default false
       */
      cached: false,

      /**
       * Allows to plug in implementation of cache, which in turn allows to use stores like Local Storage
       * for remembering values.
       *
       * `cacheImplementation` is an class with two methods:
       *
       * - `function get(searchTerm)` - returns all cached suggestions for given searchTerm prefix
       * - `function set(searchTerm, suggestions)` - stores all provided suggestions into cache under given searchTerm prefix
       *
       * @property cacheImplementation
       * @type Object
       * @default $.rich.autocomplete.ObjectCache
       */
      cacheImplemenation: ObjectCache,

      /**
       * Function that determines what prefix of search term will be used to query a cache:
       *
       * `function cachePrefixExtract(searchTerm)` - default implementation extracts first `minLength` characters of a search term
       *
       * @property cachePrefixExtract
       * @type Function
       * @default $.rich.autocomplete.cachePrefixExtract
       */
      cachePrefixExtract: cachePrefixExtract,

      /**
       * Provide function which will be used to filter array of suggestions by given searchTerm:
       *
       * `function filter(array, searchTerm)`
       *
       * @property filter
       * @type Function
       */
      filter: $.ui.autocomplete.filter,

      /* INHERITED OPTIONS */

      /**
       * Which element the menu should be appended to. When the value is null, the parents of the input field will be checked for a class of ui-front. If an element with the ui-front class is found, the menu will be appended to that element. Regardless of the value, if no element is found, the menu will be appended to the body.
       *
       * @property appendTo
       * @type Selector
       * @default null
       */
      // option implemented in $.ui.autocomplete

      /**
       * Disables the autocomplete if set to true.
       *
       * @property disabled
       * @type Boolean
       * @default false
       */
      // option implemented in $.ui.autocomplete

      /**
       * Identifies the position of the suggestions menu in relation to the associated input element. The of option defaults to the input element, but you can specify another element to position against. You can refer to the jQuery UI Position utility for more details about the various options.
       *
       * @property position
       * @type Object
       * @default `{ my: "left top", at: "left bottom", collision: "none" }`
       */
      // option implemented in $.ui.autocomplete

      /* EVENTS */

      /**
       * Triggered when search is triggered but before raw suggestions are parsed/processed.
       *
       * It gives chance to update `source` of suggestions as reflection to current search term.
       *
       * function update(request [, doneCallback])
       *
       * * `request` is object which contains search term: `{ term: 'searchTerm' }`
       * * when `doneCallback` is provided, widget will wait until `doneCallback` is called. Usually it is called on the end of AJAX data update.
       *
       * @event update
       */
      update: null,

      /* INHERITED EVENTS */

      /**
       * Triggered when the field is blurred, if the value has changed.
       *
       * @event change
       *
       * @override
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered when the menu is hidden. Not every close event will be accompanied by a change event.
       *
       * @event close
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered when the autocomplete is created.
       *
       * @event create
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered when focus is moved to an item (not selecting).
       * The default action is to replace the text field's value with the value of the focused item, though only if the event was triggered by a keyboard interaction.
       * Canceling this event prevents the value from being updated, but does not prevent the menu item from being focused.
       *
       * @event focus
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered when the suggestion menu is opened or updated.
       *
       * @event open
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered after a search completes, before the menu is shown.
       * Useful for local manipulation of suggestion data, where a custom source option callback is not required.
       * This event is always triggered when a search completes, even if the menu will not be shown because there are no results or the Autocomplete is disabled.
       *
       * @event response
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered before a search is performed, after minLength and delay are met.
       * If canceled, then no request will be started and no items suggested.
       *
       * @event search
       */
      // event implemented in $.ui.autocomplete

      /**
       * Triggered when an item is selected from the menu.
       * The default action is to replace the text field's value with the value of the selected item.
       * Canceling this event prevents the value from being updated, but does not prevent the menu from closing.
       *
       * @event select
       */
      // event implemented in $.ui.autocomplete

      /* PRIVATE OPTIONS */
      layout: LAYOUT.list

    },

    _create: function () {
      var widget = this;
      this.input = this.element;
      this.callbacks = {};

      // initialize DOM structure
      this.root = this._initDom();

      this._super();

      if (this.options.cached) {
        this.cache = new (this.options.cacheImplemenation)();
      }

      this.input.keydown(function(event) {
        widget.lastKeyupEvent = event;
      });

      this._setOptions({
        source: this.options.source
      });

      $.each(this._handlers, function(ev, handler) {
        widget.input.on('autocomplete' + ev, $.proxy(handler, widget));
      });
    },

    _destroy: function () {
      this._destroyDom();
      this._super();
    },

    /* PUBLIC METHODS */

    /* INHERITED PUBLIC METHODS */

    /**
     * Closes the Autocomplete menu. Useful in combination with the search method, to close the open menu.
     *
     * @method close
     *
     * @uses $.ui.autocomplete
     */
    // method implemented in $.ui.autocomplete

    /**
     * Removes the autocomplete functionality completely. This will return the element back to its pre-init state.
     *
     * @method destroy
     */
    // method implemented in $.ui.autocomplete

    /**
     * Disables the autocomplete.
     *
     * @method disable
     */
    // method implemented in $.ui.autocomplete

    /**
     * Enables the autocomplete.
     *
     * @method enable
     */
    // method implemented in $.ui.autocomplete

    /**
     * `option( optionName )`
     *
     * Gets an object containing key/value pairs representing the current autocomplete options hash.
     *
     * `option()`
     *
     * Gets an object containing key/value pairs representing the current autocomplete options hash.
     *
     * `option( optionName, value )`
     *
     * Sets the value of the autocomplete option associated with the specified optionName.
     *
     * `option( options )`
     *
     * Sets one or more options for the autocomplete.
     *
     * @method option
     */
    // method implemented in $.ui.autocomplete

    /**
     * Triggers a search event and invokes the data source if the event is not canceled.
     * Can be used by a selectbox-like button to open the suggestions when clicked.
     * When invoked with no parameters, the current input's value is used.
     *
     * @method search
     * @param {String} [value] optional
     */
    // method implemented in $.ui.autocomplete

    /**
     * Returns a jQuery object containing the menu element. Although the menu items are constantly created and destroyed, the menu element itself is created during initialization and is constantly reused.
     *
     * @method widget
     */
    // method implemented in $.ui.autocomplete


    /* PRIVATE METHODS */

    _setOption: function (key, value) {

      if (key === 'disabled') {
        if (value) {
          this._disable();
        } else {
          this._enable();
        }
      }

      if (key === 'source') {
        if (isDomBasedSource(value)) {
          this._updateDomSuggestions();
        }
      }

      this._super(key, value);

      if (key === 'layout') {
        this._initLayout();
      }
    },

    _initDom: function () {
      this.root = $($('<div class="r-autocomplete"></div>').insertBefore(this.input)[0]);
      this.root.append(this.input.detach());

      if (this.options.showButton) {
        var widget = this;

        this.root.addClass('input-group');
        this.button = $('<span class="input-group-btn"><button class="btn btn-default" type="button"><i class="fa fa-angle-down"></button></i></span>').appendTo(this.root).find('button');

        this.buttonClickHandler = function () {
          widget.input.autocomplete('search');
          widget.input.focus();
        };

        this.button.on('click', this.buttonClickHandler);
      }

      return this.root;
    },

    _destroyDom: function () {
      this.input.detach().insertAfter(this.root);
      this.root.remove();
    },

    _enable: function () {
      if (this.button) {
        this.button.removeAttr('disabled');
      }
    },

    _disable: function () {
      if (this.button) {
        this.button.attr('disabled', 'disabled');
      }
    },

    /**
     * @override $.ui.autocomplete._initSource
     */
    _initSource: function() {
      this.source = this._getSuggestions;
    },

    /**
     * @override $.ui.autocomplete._renderMenu
     */
    _renderMenu: function(ul, items) {
      if (this.options.layout === this.LAYOUT.table) {
        ul.addClass('ui-autocomplete-layout-table');
      }
      return this._superApply(arguments);
    },

    /**
     * @override $.ui.autocomplete._renderItem
     */
    _renderItem: function(ul, item) {
      switch (this.options.layout) {
        case this.LAYOUT.list :
          var content = item.dom ? $('<a>').html(item.dom.html()) : $('<a>').text(item.label);
          return $('<li>').append(content).appendTo(ul);
        case this.LAYOUT.table :
          var link = $('<a>');
          item.dom.find('td').each(function () {
            $('<span>').html($(this).html()).appendTo(link);
          });
          return $('<li></li>')
            .data('item.autocomplete', item)
            .append(link)
            .appendTo(ul);
      }
    },

    _initLayout: function () {
      if (this.options.layout === this.LAYOUT.table) {
        this._setOption('appendTo', $('<div class="ui-autocomplete-layout-table-wrapper">').appendTo($('body')));
      }
    },

    /**
     * callbacks for important jQuery UI Autocomplete events that helps to handle extension functionality
     */
    _handlers: {

      search: function () {
        if (this.options.autoFill) {
          if (this.entered === this.input.val()) {
            return false;
          }
        }
        this.entered = this.input.val();

        if (this.button) {
          this.button.off('click', this.buttonClickHandler);
          this.button.find('i').removeClass('fa-angle-down');
          this.button.find('i').addClass('fa-angle-up');
        }
      },

      focus: function (event, ui) {
        if (!this.options.autoFill) {
          return false;
        }
        if (this.lastKeyupEvent.keyCode === 8) {
          // refuse to auto-fill on on backspace
          return false;
        }
        var input = this.input,
          original = this.entered,
          label = ui.item.label;

        original = original.substring(0, input[0].selectionStart);

        var lastTerm = this._extractSearchTerm(original);
        var prefix = original.substring(0, original.length - lastTerm.length);

        if (lastTerm.length > 0 && label.toLowerCase().indexOf(lastTerm.toLowerCase()) === 0) {
          input.val(original + label.substring(lastTerm.length));

          input[0].selectionStart = original.length;
          input[0].selectionEnd = prefix.length + label.length;

          return false;
        } else {
          input.val(original);
          return false;
        }
      },

      close: function () {
        if (this.button) {
          this.button.find('i').removeClass('fa-angle-up');
          this.button.find('i').addClass('fa-angle-down');

          // we must delay attaching of event handler because otherwise the click will happen
          // and the handler will be triggered right after closing the menu
          setTimeout($.proxy(function() {
            this.button.on('click', this.buttonClickHandler);
          }, this), 150);
        }

        // workaround for a bug where $.ui.menu plugin doesn't reset mouseHandled flag
        $(document).click();
      },

      select: function (event, ui) {
        this.value = this._selectValue(event, ui, this.input.val());
        this.input.val(this.value);
        return false;
      }

    },

    _selectValue: function (event, ui, value) {
      if (this.options.token) {
        var terms = this._splitTokens(value);
        // remove the current input
        terms.pop();
        // add the selected item
        terms.push(ui.item.value);
        return terms.join(this.options.token + ' ');
      } else {
        return ui.item.value;
      }
    },

    _extractSearchTerm: function (term) {
      if (this.options.token) {
        return this._extractLastToken(term);
      } else {
        return term;
      }
    },

    _extractLastToken: function (term) {
      return this._splitTokens(term).pop();
    },

    _splitTokens: function (val) {
      var regexp = new RegExp('\\s*' + this.options.token + '\\s*');
      return val.split(regexp);
    },

    _getSuggestions: function (request, response) {
      var searchTerm = this._extractSearchTerm(request.term);

      if ($.type(searchTerm) === 'string' && searchTerm.length < this.options.minLength) {
        return false;
      }

      var req = $.extend({}, request, {
        term: searchTerm
      });

      if (this.cache) {
        this._getCachedSuggestions(req, response);
      } else {
        this._retrieveSuggestions(req, response);
      }
    },

    _getCachedSuggestions: function (request, response) {
      var prefix = this.options.cachePrefixExtract.call(this, request.term);

      var cached = this.cache.get(prefix);

      if (cached) {
        response.call(window, this.options.filter(cached, request.term));
        return;
      }

      var resp = $.proxy(function (result) {
        this.cache.put(prefix, result);
        return response.apply(window, arguments);
      }, this);

      var req = $.extend({}, request, {
        term: prefix
      });

      this._retrieveSuggestions(req, resp);
    },

    _retrieveSuggestions: function (request, response) {
      var source = this.options.source;

      if (isDomBasedSource(source)) {
        // DOM-based
        this._suggestFromDom(request, response);
      } else if ($.isFunction(source)) {
        // function-based
        source(request, response);
      } else {
        // array-based
        response(this.options.filter(source, request.term));
      }
    },

    _suggestFromDom: function(request, response) {
      var updateFn = this.options.update;

      var updateSuggestionsAndRespond = $.proxy(function () {
        this._updateDomSuggestions();
        response(this.options.filter(this.options.suggestions, request.term));
      }, this);

      if ($.isFunction(updateFn)) {
        // has the function second parameter? (which is 'done' callback)
        if (updateFn.length >= 2) {
          updateFn.call(window, request, updateSuggestionsAndRespond);
        } else {
          updateFn.call(window, request);
          updateSuggestionsAndRespond();
        }
      } else {
        response(this.options.filter(this.options.suggestions, request.term));
      }
    },

    _updateDomSuggestions: function () {
      var suggestions = [];
      var domSource = $(this.options.source);
      var layout = LAYOUT.list;

      if (domSource.is('table')) {
        layout = LAYOUT.table;
        domSource = domSource.children('tbody');
      }
      $(domSource).children('tr, li').each(function () {
        suggestions.push({
          value: $(this).data('label') || $(this).text().trim(),
          dom: $(this).clone()
        });
      });

      if (this.option('layout') !== layout) {
        this._setOption('layout', layout);
      }

      this.options.suggestions = suggestions;
    }

  });

  $.extend($.rich.autocomplete, {
    ObjectCache: ObjectCache,
    cachePrefixExtract: cachePrefixExtract
  });

  function ObjectCache() {
    var cache = {};
    return {
      get: function (term) {
        return cache[term];
      },
      put: function (term, value) {
        cache[term] = value;
      }
    };
  }

  function cachePrefixExtract(searchTerm) {
    if (searchTerm && this.options.minLength > 0 && this.options.minLength < searchTerm.length) {
      return searchTerm.substring(0, this.options.minLength);
    } else {
      return searchTerm;
    }
  }

  function isDomBasedSource(source) {
    return source instanceof HTMLElement || typeof source === 'string';
  }

}(jQuery));