/**
 * An autocomplete widget
 *
 * @module Input
 * @class richAutocomplete
 */
(function ($) {

  var LAYOUT = {
    list: 0,
    table: 1
  };

  $.ui.richAutocomplete = {};

  $.extend($.ui.richAutocomplete, {
    objectCache: function () {
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
  });

  $.widget('rf.richAutocomplete', {

    LAYOUT: LAYOUT,

    options: {
      token: '',
      showButton: false,
      autoFocus: false,
      autoFill: false,
      source: [],
      layout: LAYOUT.list,
      minLength: 0,

      /**
       * cached turns caching on
       *
       * @property cached
       * @type Boolean
       * @default false
       */
      cached: false,
      /**
       * defines what prefix of searchTerm will be used to query cache
       *
       * @property extractCacheSearchPrefix
       * @type String
       */
      extractCacheSearchPrefix: defaultExtractCacheSearchPrefix,
      /**
       * pluggable cache cacheImplemenation
       *
       * @property cached
       * @type Object
       */
      cacheImplemenation: $.ui.richAutocomplete.objectCache,
      /**
       * Provide function which will be used to filter array of suggestions by given searchTerm
       * function filter(array, searchTerm)
       *
       * @property filter
       * @type Object
       */
      filter: $.ui.autocomplete.filter,
      /**
       * Function called when search triggered but before suggestions are composed.
       *
       * It gives chance to update source of suggestions as reflection to current search term.
       *
       * function({ term: currentSearchTerm }, doneCallback)
       *
       * when doneCallback is specified, autocomplete will wait with update of suggestions before doneCallback
       * is called. Usually it is called on the end of AJAX data update.
       *
       * @property update
       * @type Function
       */
      update: null,

      // TODO should not be option, but private member
      suggestions: []
    },

    _create: function () {
      var widget = this;
      this.input = this.element;

      // initialize DOM structure
      this.root = this._initDom();

      if (this.options.cached) {
        this.cache = new this.options.cacheImplemenation();
      }

      if (!this.options.layout) {
        this._setOption('layout', this.LAYOUT.list);
      }

      var autocompleteOptions = this._getAutocompleteOptions();
      this.input.autocomplete(autocompleteOptions);

      this.input.keydown(function(event) {
        widget.lastKeyupEvent = event;
      });

      this._registerListeners();

      if (this.options.source) {
        this._setOption('source', this.options.source);
      }

      if (this.input.disabled) {
        this._setOption('disabled', true);
      }
    },

    _destroy: function () {
      this.input.autocomplete('destroy');
      this._destroyDom();
    },

    _enable: function () {
      this.input.autocomplete('enable');
      this.button.removeAttr('disabled');
    },

    _disable: function () {
      this.input.autocomplete('disable');
      this.button.attr('disabled', 'disabled');
    },

    _initDom: function () {
      this.root = $($('<div class="r-autocomplete"></div>').insertBefore(this.input)[0]);
      this.root.append(this.input.detach());

      if (this.options.showButton) {
        var widget = this;

        this.root.addClass('input-group');
        this.button = $('<span class="input-group-btn"><button class="btn btn-light" type="button"><i class="icon-chevron-down"></button></i></span>').appendTo(this.root).find('button');

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

    _getAutocompleteOptions: function () {
      var widget = this;

      return {
        delay: 0,
        minLength: this.options.minLength,
        autoFocus: this.options.autoFocus,
        source: function (request, response) {
          widget._getSuggestions(request, response);
        },
        search: function () {
          if (widget.options.autoFill) {
            if (widget.entered === widget.input.val()) {
              return false;
            }
          }
          widget.entered = widget.input.val();

          if (widget.button) {
            widget.button.off('click', widget.buttonClickHandler);
            widget.button.find('i').removeClass('icon-chevron-down');
            widget.button.find('i').addClass('icon-chevron-up');
          }
        },
        focus: function (event, ui) {
          if (!widget.options.autoFill) {
            return false;
          }
          if (widget.lastKeyupEvent.keyCode === 8) {
            // refuse to auto-fill on on backspace
            return false;
          }
          var input = widget.input,
            original = widget.entered,
            label = ui.item.label;

          original = original.substring(0, input[0].selectionStart);

          var lastTerm = widget._extractSearchTerm(original);
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
        close: function (event) {
          if (widget.button) {
            widget.button.find('i').removeClass('icon-chevron-up');
            widget.button.find('i').addClass('icon-chevron-down');

            // we must delay attaching of event handler because otherwise the click will happen
            // and the handler will be triggered right after closing the menu
            setTimeout(function() {
              widget.button.on('click', widget.buttonClickHandler);
            }, 150);
          }
        },
        select: function (event, ui) {
          this.value = widget._selectValue(event, ui, this.value);
          return false;
        }
      };
    },

    _splitTokens: function (val) {
      var regexp = new RegExp('\\s*' + this.options.token + '\\s*');
      return val.split(regexp);
    },


    _extractLastToken: function (term) {
      return this._splitTokens(term).pop();
    },

    _extractSearchTerm: function (term) {
      if (this.options.token) {
        return this._extractLastToken(term);
      } else {
        return term;
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
      var prefix = this.options.extractCacheSearchPrefix.call(this, request.term);

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

      this._setOption('suggestions', suggestions);
    },

    _preventTabbing: function () {
      this.element.bind('keydown', function (event) {
        if (event.keyCode === $.ui.keyCode.TAB &&
          $(this).data('autocomplete').menu.active) {
          event.preventDefault();
        }
      });
    },

    _registerListeners: function () {
      var widget = this;
      $.each(['search', 'open', 'focus', 'select', 'close', 'change'], function(i, ev) {
        if (widget.options[ev]) {
          widget.input.on('autocomplete' + ev, widget.options[ev]);
        }
      });
    },

    _setLayout: function (layout) {
      var data = this.input.autocomplete().data('ui-autocomplete');
      switch (layout) {
        case this.LAYOUT.list :
          data._renderMenu = $.ui.autocomplete.prototype._renderMenu;
          data._renderItem = function (ul, item) {
            var content = item.dom ? $('<a>').html(item.dom.html()) : $('<a>').text(item.label);
            return $('<li>').append(content).appendTo(ul);
          };
          break;
        case this.LAYOUT.table :
          this._setOption('appendTo', $('<div class="ui-autocomplete-layout-table-wrapper">').appendTo($('body')));
          data._renderMenu = function (ul, items) {
            ul.addClass('ui-autocomplete-layout-table');
            return $.ui.autocomplete.prototype._renderMenu.call(this, ul, items);
          };
          data._renderItem = function (ul, item) {
            var link = $('<a>');
            item.dom.find('td').each(function () {
              $('<span>').html($(this).html()).appendTo(link);
            });
            return $('<li></li>')
              .data('item.autocomplete', item)
              .append(link)
              .appendTo(ul);
          };
          break;
      }
    },

    _setOption: function (key, value) {
      if (key === 'layout') {
        this._setLayout(value);
      }
      if (key === 'disabled') {
        if (value) {
          this._disable();
        } else {
          this._enable();
        }
      }

      this._super(key, value);

      if (key === 'source') {
        if (isDomBasedSource(value)) {
          this._updateDomSuggestions();
        }
      }
    }
  });

  /**
   * Default implementation of extracting searchTerm prefix for checking cache
   */
  function defaultExtractCacheSearchPrefix(searchTerm) {
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