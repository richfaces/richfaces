(function(jquery, richfaces) {
	var logLevels = ['debug', 'info', 'warn', 'error'];
	var logLevelsPadded = {'debug': 'debug', 'info': 'info ', 'warn': 'warn ', 'error': 'error'};
	var logLevelValues = {'debug': 1, 'info': 2, 'warn': 3, 'error': 4};
	var logLevelColors = {'debug': 'darkblue', 'info': 'blue', 'warn': 'gold', 'error': 'red'};

	var logClassMethods = {

		__openPopup: function() {
			if (!this.__popupWindow || this.__popupWindow.closed) {
				this.__popupWindow = open("", "_richfaces_logWindow", "height=400, width=600, resizable = yes, status=no, " +
				"scrollbars = yes, statusbar=no, toolbar=no, menubar=no, location=no");
				
				this.__popupWindow.document.write("<html><head></head><body><div id='richfaces.log'></div></body></html>");
				this.__popupWindow.document.close();
				this.__consoleElement = jquery("#richfaces\\.log", this.__popupWindow.document);
				this.__initializeControls();
			} else {
				this.__popupWindow.focus();
			}
		},
			
		__hotkeyHandler: function(event) {
			if (event.ctrlKey && event.shiftKey) {
				if ((this.hotkey || 'l').toLowerCase() == String.fromCharCode(event.keyCode).toLowerCase()) {
					this.__openPopup();
				}
			}
		},
			
		__getTimeAsString: function() {
			var date = new Date();

			var timeString = this.__lzpad(date.getHours(), 2) + ':' + this.__lzpad(date.getMinutes(), 2) + ':' + 
			this.__lzpad(date.getSeconds(), 2) + '.' + this.__lzpad(date.getMilliseconds(), 3);

			return timeString;
		},

		__lzpad: function(s, length) {
			s = s.toString();
			var a = new Array();
			for (var i = 0; i < length - s.length; i++) {
				a.push('0');
			}
			a.push(s);
			return a.join('');
		},

		__getMessagePrefix: function(level) {
			return logLevelsPadded[level] + '[' + this.__getTimeAsString() + ']: ';
		},

		__setLevelFromSelect: function(event) {
			this.setLevel(event.target.value);
		},

		__initializeControls : function() {
			var console = this.__consoleElement;

			var clearBtn = console.children("button.rich-log-element");
			if (clearBtn.length == 0) {
				clearBtn = jquery("<button type='button' class='rich-log-element'>Clear</button>").appendTo(console);
			}

			clearBtn.click(jquery.proxy(this.clear, this));

			var levelSelect = console.children("select.rich-log-element");
			if (levelSelect.length == 0) {
				levelSelect = jquery("<select class='rich-log-element' name='richfaces.log' />").appendTo(console);
			}

			if (levelSelect.children().length == 0) {
				for (var l = 0; l < logLevels.length; l++) {
					jquery("<option value='" + logLevels[l]+ "'>" + logLevels[l] + "</option>").appendTo(levelSelect);
				}
			}

			levelSelect.val(this.getLevel());
			levelSelect.change(jquery.proxy(this.__setLevelFromSelect, this));

			var consoleEntries = console.children(".rich-log-contents");
			if (consoleEntries.length == 0) {
				consoleEntries = jquery("<div class='rich-log-contents'></div>").appendTo(console);
			}
		},

		__log: function(level, message) {
			//TODO scroll to the added message
			if (!this.__consoleElement) {
				return;
			}
			
			var console = this.__consoleElement;

			if (logLevelValues[level] >= logLevelValues[this.getLevel()]) {
				var newEntry = jquery(document.createElement("div")).appendTo(console.children(".rich-log-contents"));
				jquery("<span style='color: " + logLevelColors[level] + "'></span>").appendTo(newEntry).text(this.__getMessagePrefix(level));

				var entrySpan = jquery(document.createElement("span")).appendTo(newEntry);
				if (typeof message != 'object' || !message.appendTo) {
					entrySpan.text(message);
				} else {
					message.appendTo(entrySpan);
				}
			}
		},

		init: function(options) {
			this.level = options.level;
			this.hotkey = options.hotkey;
			this.mode = options.mode;

			if (this.mode == 'popup') {
				this.__boundHotkeyHandler = jquery.proxy(this.__hotkeyHandler, this);
				jquery(document).bind('keydown', this.__boundHotkeyHandler);
			} else {
				this.__consoleElement = jquery("#richfaces\\.log");
				this.__initializeControls();
			}
		},

		destroy: function() {
			//TODO test this method
			if (this.__popupWindow) {
				this.__popupWindow.close();
			}
			this.__popupWindow = null;
			
			jquery(document).unbind('keydown', this.__boundHotkeyHandler);
			this.__boundHotkeyHandler = null;

			this.__consoleElement = null;
			this.$super.destroy.call(this);
		},

		setLevel: function(level) {
			this.level = level;
			this.clear();
		},

		getLevel: function() {
			return this.level || 'info';
		},

		clear: function() {
			this.__consoleElement.children(".rich-log-contents").children().remove();
		}
	};

	for (var i = 0; i < logLevels.length; i++) {
		logClassMethods[logLevels[i]] = (function() {
			var level = logLevels[i];
			return function(message) {
				this.__log(level, message);
			}
		}());
	}
	
	richfaces.HtmlLog = richfaces.BaseComponent.extendClass(logClassMethods);
}(jQuery, RichFaces));

if (typeof jsf != 'undefined') {
	(function(jQuery, richfaces, jsf) {

		//JSF log adapter
		var identifyElement = function(elt) {
			var identifier = '<' + elt.tagName.toLowerCase();
			var e = jQuery(elt);
			if (e.attr('id')) {
				identifier += (' id=' + e.attr('id'));
			}
			if (e.attr('class')) {
				identifier += (' class=' + e.attr('class'));
			}

			identifier += ' ...>';

			return identifier;
		}

		var formatPartialResponseElement = function(logElement, responseElement) {
			var change = jQuery(responseElement);

			logElement.append("Element <b>" + responseElement.nodeName + "</b>");
			if (change.attr("id")) {
				logElement.append(document.createTextNode(" for id=" + change.attr("id")));
			}

			jQuery(document.createElement("br")).appendTo(logElement);
			jQuery("<span style='color:dimgray'></span>").appendTo(logElement).text(change.toXML());
			jQuery(document.createElement("br")).appendTo(logElement);
		}

		var formatPartialResponse = function(partialResponse) {
			var logElement = jQuery(document.createElement("span"));

			partialResponse.children().each(function() {
				var responseElement = jQuery(this);
				if (responseElement.is('changes')) {
					logElement.append("Listing content of response <b>changes</b> element:<br />");
					responseElement.children().each(function() {
						formatPartialResponseElement(logElement, this);
					});
				} else {
					formatPartialResponseElement(logElement, this);
				}
			});

			return logElement;
		}

		var jsfAjaxLogAdapter = function(data) {
			try {
				var log = richfaces.log;

				var source = data.source;
				var type = data.type;

				var responseCode = data.responseCode;
				var responseXML = data.responseXML;
				var responseText = data.responseText;

				if (type != 'error') {
					log.info("Received '" + type + "' event from " + identifyElement(source));

					if (type == 'beforedomupdate') {
						var partialResponse;

						if (responseXML) {
							partialResponse = jQuery(responseXML).children("partial-response");
						}

						var responseTextEntry = jQuery("<span>Server returned responseText: </span><span style='color:dimgray'></span>").eq(1).text(responseText).end();

						if (partialResponse && partialResponse.length) {
							log.debug(responseTextEntry);
							log.info(formatPartialResponse(partialResponse));
						} else {
							log.info(responseTextEntry);
						}
					}
				} else {
					var status = data.status;
					log.error("Received '" + type + '@' + status + "' event from " + identifyElement(source));
					log.error("[" + data.responseCode + "] " + data.errorName + ": " + data.errorMessage);
				}
			} catch (e) {
				//ignore logging errors
			}
		};

		var eventsListener = richfaces.createJSFEventsAdapter({
			begin: jsfAjaxLogAdapter,
			beforedomupdate: jsfAjaxLogAdapter,
			success: jsfAjaxLogAdapter,
			complete: jsfAjaxLogAdapter,
			error: jsfAjaxLogAdapter
		});

		jsf.ajax.addOnEvent(eventsListener);
		jsf.ajax.addOnError(eventsListener);
		//
	}(jQuery, RichFaces, jsf));
};