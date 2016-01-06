(function($, rf, jsf) {


  /**
   * Global object accessing general state of Push functionality.
   *
   * Push internally uses Atmosphere to handle connection.
   *
   * @module RichFaces.push
   */
  rf.push = {

    options: {
      transport: "websocket",
      fallbackTransport: "long-polling",
      logLevel: "info"
    },

    /**
     * topics that we subscribed to in current atmosphere connection
     */
    _subscribedTopics: {},

    /**
     * topics to be added to subscription during next invocation updateConnection
     */
    _addedTopics: {},

    /**
     * topics to be removed from subscription during next invocation updateConnection
     */
    _removedTopics: {},

    /**
     * Object that holds mapping of addresses to number of subscribed widgets
     *
     * { address: counter }
     *
     * If counter reaches 0, it is safe to unsubscribe given address.
     */
    _handlersCounter: {},

    /**
     * current Push session identifier
     */
    _pushSessionId: null,
    /**
     * sequence number which identifies which messages were already processed
     */
    _lastMessageNumber: -1,

    /**
     * the URL that handles Push subscriptions
     */
    _pushResourceUrl: null,
    /**
     * the URL that handles Atmosphere requests
     */
    _pushHandlerUrl: null,

    /**
     * Checks whether there are requests for adding or removing topics to the list of subscribed topics.
     *
     * If yes, it reconnects Atmosphere connection.
     *
     * @method updateConnection
     */
    updateConnection: function() {
      if ($.isEmptyObject(this._handlersCounter)) {
        this._disconnect();
      } else if (!$.isEmptyObject(this._addedTopics) || !$.isEmptyObject(this._removedTopics)) {
        this._disconnect();
        this._connect();
      }

      this._addedTopics = {};
      this._removedTopics = {};
    },

    /**
     * Increases number of subscriptions to given topic
     *
     * @method increaseSubscriptionCounters
     * @param topic
     */
    increaseSubscriptionCounters: function(topic) {
      if (isNaN(this._handlersCounter[topic]++)) {
        this._handlersCounter[topic] = 1;
        this._addedTopics[topic] = true;
      }
    },

    /**
     * Decreases number of subscriptions to given topic
     *
     * @method decreaseSubscriptionCounters
     * @param topic
     */
    decreaseSubscriptionCounters: function(topic) {
      if (--this._handlersCounter[topic] == 0) {
        delete this._handlersCounter[topic];
        this._removedTopics[topic] = true;
        this._subscribedTopics[topic] = false;
      }
    },

    /**
     * Setups the URL that handles Push subscriptions
     *
     * @method setPushResourceUrl
     * @param url
     */
    setPushResourceUrl: function(url) {
      this._pushResourceUrl = qualifyUrl(url);
    },

    /**
     * Setups the URL that handles Atmosphere requests
     *
     * @method setPushHandlerUrl
     * @param url
     */
    setPushHandlerUrl: function(url) {
      this._pushHandlerUrl = qualifyUrl(url);
    },

    /**
     * Handles messages transported using Atmosphere
     */
    _messageCallback: function(response) {
      if (response.state && response.state === "opening") {
          this._lastMessageNumber = -1;
          return;
      }
      var suspendMessageEndMarker = /^(<!--[^>]+-->\s*)+/;
      var messageTokenExpr = /<msg topic="([^"]+)" number="([^"]+)">([^<]*)<\/msg>/g;

      var dataString = response.responseBody.replace(suspendMessageEndMarker, "");
      if (dataString) {
        var messageToken;
        while (messageToken = messageTokenExpr.exec(dataString)) {
          if (!messageToken[1]) {
            continue;
          }

          var message = {
              topic: messageToken[1],
              number: parseInt(messageToken[2]),
              data: $.parseJSON(messageToken[3])
          };

          if (message.number <= this._lastMessageNumber) {
            continue;
          }

          var event = new jQuery.Event('push.push.RICH.' + message.topic, { rf: { data: message.data } });

          (function(event) {
            $(function() {
              $(document).trigger(event);
            });
          })(event);

          this._lastMessageNumber = message.number;
        }
      }
    },

    /**
     * Handles errors during Atmosphere initialization and transport
     */
    _errorCallback: function(response) {
      for (var address in this.newlySubcribed) {
        if (this.newlySubcribed.hasOwnProperty(address)) {
          this._subscribedTopics[address] = true;
          $(document).trigger('error.push.RICH.' + address, response);
        }
      }
    },

    /**
     * Initializes Atmosphere connection
     */
    _connect: function() {
      this.newlySubcribed = {};

      var topics = [];
      for (var address in this._handlersCounter) {
        if (this._handlersCounter.hasOwnProperty(address)) {
          topics.push(address);
          if (!this._subscribedTopics[address]) {
            this.newlySubcribed[address] = true;
          }
        }
      }

      var data = {
        'pushTopic': topics
      };

      if (this._pushSessionId) {
        data['forgetPushSessionId'] = this._pushSessionId;
      }

      //TODO handle request errors
      $.ajax({
        data: data,
        dataType: 'text',
        traditional: true,
        type: 'POST',
        url: this._pushResourceUrl,
        success: $.proxy(function(response) {
          var data = $.parseJSON(response);

          for (var address in data.failures) {
            $(document).trigger('error.push.RICH.' + address);
          }

          if (data.sessionId) {
            this._pushSessionId = data.sessionId;

            var url = this._pushHandlerUrl || this._pushResourceUrl;
            url += "?__richfacesPushAsync=1&pushSessionId="
            url += this._pushSessionId;

            var messageCallback = $.proxy(this._messageCallback, this);
            var errorCallback = $.proxy(this._errorCallback, this);

            atmosphere.subscribe(url, messageCallback, {
              transport: this.options.transport,
              fallbackTransport: this.options.fallbackTransport,
              logLevel: this.options.logLevel,
              onError: errorCallback
            });

            // fire subscribed events
            for (var address in this.newlySubcribed) {
              this._subscribedTopics[address] = true;
              $(document).trigger('subscribed.push.RICH.' + address);
            }
          }
        }, this)
      });
    },

    /**
     * Ends Atmosphere connection
     */
    _disconnect: function() {
      atmosphere.unsubscribe();
    }
  };

  /**
   * jQuery plugin which mades easy to bind the Push to the DOM element
   * and manage plugins lifecycle and event handling
   *
   * @function $.fn.richpush
   */

  $.fn.richpush = function( options ) {
    var widget = $.extend({}, $.fn.richpush);

    // for all selected elements
    return this.each(function() {
      widget.element = this;
      widget.options = $.extend({}, widget.options, options);
      widget.eventNamespace = '.push.RICH.' + widget.element.id;

      // call constructor
      widget._create();

      // listen for global DOM destruction event
      $(document).on('beforeDomClean' + widget.eventNamespace, function(event) {
        // is the push component under destructed DOM element (event target)?
        if (event.target && (event.target === widget.element || $.contains(event.target, widget.element))) {
          widget._destroy();
        }
      });
    });
  };

  $.extend($.fn.richpush, {

    options: {

      /**
       * Specifies which address (topic) will Push listen on
       *
       * @property address
       * @required
       */
      address: null,

      /**
       * Fired when Push is subscribed to the specified address
       *
       * @event subscribed
       */
      subscribed: null,

      /**
       * Triggered when Push receives data
       *
       * @event push
       */
      push: null,

      /**
       * Triggered when error is observed during Push initialization or communication
       *
       * @event error
       */
      error: null
    },

    _create: function() {
      var widget = this;

      this.address = this.options.address;

      this.handlers = {
        subscribed: null,
        push: null,
        error: null
      };

      $.each(this.handlers, function(eventName) {
        if (widget.options[eventName]) {

          var handler = function(event, data) {
            if (data) {
              $.extend(event, {
                rf: {
                  data: data
                }
              });
            }

            widget.options[eventName].call(widget.element, event);
          };

          widget.handlers[eventName] = handler;
          $(document).on(eventName + widget.eventNamespace + '.' + widget.address, handler);
        }
      });

      rf.push.increaseSubscriptionCounters(this.address);
    },

    _destroy: function() {
      rf.push.decreaseSubscriptionCounters(this.address);
      $(document).off(this.eventNamespace);
    }

  });

  /*
   * INITIALIZE PUSH
   *
   * when document is ready and when JSF errors happens
   */

  $(document).ready(function() {
    rf.push.updateConnection();
  });

  jsf.ajax.addOnEvent(jsfErrorHandler);
  jsf.ajax.addOnError(jsfErrorHandler);


  /* PRIVATE FUNCTIONS */

  function jsfErrorHandler(event) {
    if (event.type == 'event') {
      if (event.status != 'success') {
        return;
      }
    } else if (event.type != 'error') {
      return;
    }

    rf.push.updateConnection();
  }

  function qualifyUrl(url) {
    var result = url;
    if (url.charAt(0) == '/') {
      result = location.protocol + '//' + location.host + url;
    }
    return result;
  }

}(RichFaces.jQuery, RichFaces, jsf));