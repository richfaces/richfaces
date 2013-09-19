(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};
    var selectionEventHandler = function(event) {
        event.stopPropagation();
        event.preventDefault();
    };

    var disableSelection = function (element) {
        if (typeof element.onselectstart != "undefined") //IE
        {
            $(rf.getDomElement(element)).bind('selectstart', selectionEventHandler);
        }
        else //All other (ie: Opera)
        {
            $(rf.getDomElement(element)).bind('mousedown', selectionEventHandler);
        }
    };

    var enableSelection = function (element) {
        if (typeof element.onselectstart != "undefined") //IE
        {
            $(rf.getDomElement(element)).unbind('selectstart', selectionEventHandler);
        }
        else //All other (ie: Opera)
        {
            $(rf.getDomElement(element)).unbind('mousedown', selectionEventHandler);
        }
    };

    var defaultOptions = {
        width:-1,
        height:-1,
        minWidth:-1,
        minHeight:-1,
        modal:true,
        moveable:true,
        resizeable: false,
        autosized: false,
        left: "auto",
        top : "auto",
        zindex:100,
        shadowDepth : 5,
        shadowOpacity: 0.1,
        attachToBody:true
    };


    rf.rf4.ui.PopupPanel = function(id, options) {

        $super.constructor.call(this, id);
        this.markerId = id;
        this.attachToDom(this.markerId);
        this.options = $.extend(this.options, defaultOptions, options || {});

        this.minWidth = this.getMinimumSize(this.options.minWidth);
        this.minHeight = this.getMinimumSize(this.options.minHeight);
        this.maxWidth = this.options.maxWidth;
        this.maxHeight = this.options.maxHeight;

        this.baseZIndex = this.options.zindex;

        this.div = $(rf.getDomElement(id));
        this.cdiv = $(rf.getDomElement(id + "_container"));
        this.contentDiv = $(rf.getDomElement(id + "_content"));
        this.shadowDiv = $(rf.getDomElement(id + "_shadow"));
        this.shadeDiv = $(rf.getDomElement(id + "_shade"));
        this.scrollerDiv = $(rf.getDomElement(id + "_content_scroller"));

        $(this.shadowDiv).css("opacity", this.options.shadowOpacity);
        this.shadowDepth = parseInt(this.options.shadowDepth);

        this.borders = new Array();
        this.firstHref = $(rf.getDomElement(id + "FirstHref"));
        if (this.options.resizeable) {
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerN", this, "N-resize", rf.rf4.ui.PopupPanel.Sizer.N));
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerE", this, "E-resize", rf.rf4.ui.PopupPanel.Sizer.E));
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerS", this, "S-resize", rf.rf4.ui.PopupPanel.Sizer.S));
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerW", this, "W-resize", rf.rf4.ui.PopupPanel.Sizer.W));

            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerNW", this, "NW-resize", rf.rf4.ui.PopupPanel.Sizer.NW));
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerNE", this, "NE-resize", rf.rf4.ui.PopupPanel.Sizer.NE));
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerSE", this, "SE-resize", rf.rf4.ui.PopupPanel.Sizer.SE));
            this.borders.push(new rf.rf4.ui.PopupPanel.Border(id + "ResizerSW", this, "SW-resize", rf.rf4.ui.PopupPanel.Sizer.SW));
        }

        if (this.options.moveable && rf.getDomElement(id + "_header")) {
            this.header = new rf.rf4.ui.PopupPanel.Border(id + "_header", this, "move", rf.rf4.ui.PopupPanel.Sizer.Header);
        } else {
            $(rf.getDomElement(id + "_header")).css('cursor', 'default');
        }

    };

    rf.BaseComponent.extend(rf.rf4.ui.PopupPanel);
    var $super = rf.rf4.ui.PopupPanel.$super;
    $.extend(rf.rf4.ui.PopupPanel.prototype, (function (options) {

        return {

            name: "PopupPanel",
            saveInputValues: function(element) {
                /* Fix for RF-3856 - Checkboxes in modal panel does not hold their states after modal was closed and opened again */
                if ($.browser.msie /* reproducible for checkbox/radio in IE6, radio in IE 7/8 beta 2 */) {
                    $('input[type=checkbox], input[type=radio]', element).each(function(index) {
                        $(this).defaultChecked = $(this).checked;
                    });
                }
            },

            width: function() {
                return this.getContentElement()[0].clientWidth;//TODO
            },

            height: function() {
                return this.getContentElement()[0].clientHeight;//TODO
            },

            getLeft : function () {
                return this.cdiv.css('left');
            },

            getTop : function () {
                return this.cdiv.css('top');
            },

            getInitialSize : function() {
                if (this.options.autosized) {
                    return 15;
                } else {
                    return $(rf.getDomElement(this.markerId + "_header_content")).height();
                }
            },

            getContentElement: function() {
                if (!this._contentElement) {
                    this._contentElement = this.cdiv;
                }

                return this._contentElement;
            },
            getSizeElement : function() {
                return document.body;
            },

            getMinimumSize : function(size) {
                return Math.max(size, 2 * this.getInitialSize() + 2);
            },

            __getParsedOption: function(options, name) {
                var value = parseInt(options[name], 10);

                if (value < 0 || isNaN(value)) {
                    value = this[name];
                }

                return value;
            },

            destroy: function() {

                this._contentElement = null;
                this.firstOutside = null;
                this.lastOutside = null;
                this.firstHref = null;
                this.parent = null;
                if (this.header) {
                    this.header.destroy();
                    this.header = null;
                }

                for (var k = 0; k < this.borders.length; k++) {
                    this.borders[k].destroy();
                }
                this.borders = null;

                if (this.domReattached) {
                    this.div.remove();
                }

                this.markerId = null;
                this.options = null;

                this.div = null;
                this.cdiv = null;
                this.contentDiv = null;
                this.shadowDiv = null;
                this.scrollerDiv = null;
                this.userOptions = null;
                this.eIframe = null;

                $super.destroy.call(this);

            },

            initIframe : function() {
                if (this.contentWindow) {
                    $(this.contentWindow.document.body).css("margin", "0px 0px 0px 0px");
                } else {
                    //TODO opera etc.

                }

                if ("transparent" == $(document.body).css("background-color")) {
                    $(this).css('filter', "alpha(opacity=0)");
                    $(this).css('opacity', "0");
                }
            },

            setLeft: function(pos) {
                if (!isNaN(pos)) {
                    this.cdiv.css('left', pos + "px");
                }
            },

            setTop: function(pos) {
                if (!isNaN(pos)) {
                    this.cdiv.css('top', pos + "px");
                }
            },

            show: function(event, opts) {
                var element = this.cdiv;
                if (!this.shown && this.invokeEvent("beforeshow", event, null, element)) {
                    this.preventFocus();


                    if (!this.domReattached) {
                        this.parent = this.div.parent();

                        var domElementAttachment;
                        if (opts) {
                            domElementAttachment = opts.domElementAttachment;
                        }

                        if (!domElementAttachment) {
                            domElementAttachment = this.options.domElementAttachment;
                        }

                        var newParent;
                        if ('parent' == domElementAttachment) {
                            newParent = this.parent;
                        } else if ('form' == domElementAttachment) {
                            newParent = this.findForm(element)[0] || document.body;
                        } else {
                            //default - body
                            newParent = document.body;
                        }

                        if (newParent != this.parent) {
                            this.saveInputValues(element);
                            this.shadeDiv.length && newParent.appendChild(this.shadeDiv.get(0));
                            newParent.appendChild(this.cdiv.get(0));
                            this.domReattached = true;
                        } else {
                            this.parent.show();
                        }
                    }

                    var forms = $("form", element);

                    if (this.options.keepVisualState && forms) {
                        for (var i = 0; i < forms.length; i++) {
                            var popup = this;
                            $(forms[i]).bind("submit", {popup:popup}, this.setStateInput);
                        }
                    }


                    var options = {};
                    this.userOptions = {};
                    $.extend(options, this.options);

                    if (opts) {
                        $.extend(options, opts);
                        $.extend(this.userOptions, opts);
                    }

                    this.currentMinHeight = this.getMinimumSize(this.__getParsedOption(options, 'minHeight'));
                    this.currentMinWidth = this.getMinimumSize(this.__getParsedOption(options, 'minWidth'));

                    var eContentElt = this.getContentElement();

                    if (!this.options.autosized) {
                        if (options.width && options.width == -1)
                            options.width = 300;
                        if (options.height && options.height == -1)
                            options.height = 200;
                    }

                    if (options.width && options.width != -1) {
                        if (this.currentMinWidth > options.width) {
                            options.width = this.currentMinWidth;
                        }
                        if (options.width > this.maxWidth) {
                            options.width = this.maxWidth;
                        }
                        $(rf.getDomElement(eContentElt)).css('width', options.width + (/px/.test(options.width) ? '' : 'px'));
                        this.shadowDiv.css('width', options.width + (/px/.test(options.width) ? '' : 'px'));
                        this.scrollerDiv.css('width', options.width + (/px/.test(options.width) ? '' : 'px'));
                    }

                    if (options.height && options.height != -1) {
                        if (this.currentMinHeight > options.height) {
                            options.height = this.currentMinHeight;
                        }
                        if (options.height > this.maxHeight) {
                            options.height = this.maxHeight;
                        }
                        $(rf.getDomElement(eContentElt)).css('height', options.height + (/px/.test(options.height) ? '' : 'px'));
                        var headerHeight = $(rf.getDomElement(this.markerId + "_header")) ? $(rf.getDomElement(this.markerId + "_header")).innerHeight() : 0;
                        this.scrollerDiv.css('height', options.height - headerHeight + (/px/.test(options.height) ? '' : 'px'));
                    }

                    var eIframe;
                    if (this.options.overlapEmbedObjects && !this.iframe) {
                        this.iframe = this.markerId + "IFrame";
                        $("<iframe src=\"javascript:''\" frameborder=\"0\" scrolling=\"no\" id=\"" + this.iframe + "\" " +
                            "class=\"rf-pp-ifr\" style=\"width:" + this.options.width + "px; height:" + this.options.height + "px;\">" +
                            "</iframe>").insertBefore($(':first-child', this.cdiv)[0]);

                        eIframe = $(rf.getDomElement(this.iframe));

                        eIframe.bind('load', this.initIframe);
                        this.eIframe = eIframe;
                    }

                    if (options.left) {
                        var _left;
                        if (options.left != "auto") {
                            _left = parseInt(options.left, 10);
                        } else {
                            var cw = this.__calculateWindowWidth();
                            var _width = this.width();
                            if (cw >= _width) {
                                _left = (cw - _width) / 2;
                            } else {
                                _left = 0;
                            }
                        }

                        this.setLeft(Math.round(_left));
                        $(this.shadowDiv).css("left", this.shadowDepth);
                    }

                    if (options.top) {
                        var _top;
                        if (options.top != "auto") {
                            _top = parseInt(options.top, 10);
                        } else {
                            var ch = this.__calculateWindowHeight();
                            var _height = this.height();
                            if (ch >= _height) {
                                _top = (ch - _height) / 2;
                            } else {
                                _top = 0;
                            }
                        }

                        this.setTop(Math.round(_top));
                        $(this.shadowDiv).css("top", this.shadowDepth);
                        $(this.shadowDiv).css("bottom", -this.shadowDepth);
                    }


                    this.div.css('visibility', '');
                    if ($.browser.msie && $.browser.version >= 9.0 && document.documentMode >= 9) {
                        $(this.cdiv).find('input').each(function() {
                            // Force a CSS "touch" of all popupPanel children to ensure visibility in IE 9/10 for RF-12850
                            var $this = $(this);
                            var visibility = $this.css('visibility');
                            $this.css('visibility', visibility);
                        })
                    }
                    this.div.css('display', 'block');
                    if (this.options.autosized) {
                        this.shadowDiv.css('width', this.cdiv[0].clientWidth);

                    }

                    var showEvent = {};
                    showEvent.parameters = opts || {};
                    this.shown = true;
                    this.invokeEvent("show", showEvent, null, element);
                }
            },

            __calculateWindowHeight: function() {
                var documentElement = document.documentElement;
                return self.innerHeight || (documentElement && documentElement.clientHeight) || document.body.clientHeight;
            },

            __calculateWindowWidth: function() {
                var documentElement = document.documentElement;
                return self.innerWidth || (documentElement && documentElement.clientWidth) || document.body.clientWidth;
            },

            startDrag: function(border) {
                disableSelection(document.body);
            },
            firstOnfocus: function(event) {
                var e = $(event.data.popup.firstHref);
                if (e) {
                    e.focus();
                }
            },

            processAllFocusElements: function(root, callback) {
                var idx = -1;
                var tagName;
                var formElements = "|a|input|select|button|textarea|";

                if (root.focus && root.nodeType == 1 && (tagName = root.tagName) &&
                    // Many not visible elements have focus method, we is had to avoid processing them.
                    (idx = formElements.indexOf(tagName.toLowerCase())) != -1 &&
                    formElements.charAt(idx - 1) === '|' &&
                    formElements.charAt(idx + tagName.length) === '|' &&
                    !root.disabled && root.type != "hidden") {
                    callback.call(this, root);
                } else {
                    if (root != this.cdiv.get(0)) {
                        var child = root.firstChild;
                        while (child) {
                            if (!child.style || child.style.display != 'none') {
                                this.processAllFocusElements(child, callback);
                            }
                            child = child.nextSibling;
                        }
                    }
                }
            },

            processTabindexes:    function(input) {
                if (!this.firstOutside) {
                    this.firstOutside = input;
                }
                if (!input.prevTabIndex) {
                    input.prevTabIndex = input.tabIndex;
                    input.tabIndex = -1;
                }

                if (!input.prevAccessKey) {
                    input.prevAccessKey = input.accessKey;
                    input.accessKey = "";
                }
            },

            restoreTabindexes:    function(input) {
                if (input.prevTabIndex != undefined) {
                    if (input.prevTabIndex == 0) {
                        $(input).removeAttr('tabindex');
                    } else {
                        input.tabIndex = input.prevTabIndex;
                    }
                    input.prevTabIndex = undefined;
                }
                if (input.prevAccessKey != undefined) {
                    if (input.prevAccessKey == "") {
                        $(input).removeAttr('accesskey');
                    } else {
                        input.accessKey = input.prevAccessKey;
                    }
                    input.prevAccessKey = undefined;
                }
            },

            preventFocus:    function() {
                if (this.options.modal) {
                    this.processAllFocusElements(document, this.processTabindexes);
                    var popup = this;
                    if (this.firstOutside) {

                        $(rf.getDomElement(this.firstOutside)).bind("focus", {popup: popup}, this.firstOnfocus);
                    }
                }
            },

            restoreFocus: function() {
                if (this.options.modal) {
                    this.processAllFocusElements(document, this.restoreTabindexes);

                    if (this.firstOutside) {
                        $(rf.getDomElement(this.firstOutside)).unbind("focus", this.firstOnfocus);
                        this.firstOutside = null;
                    }
                }
            },

            endDrag: function(border) {
                for (var k = 0; k < this.borders.length; k++) {
                    this.borders[k].show();
                    this.borders[k].doPosition();
                }
                enableSelection(document.body);
            },

            hide: function(event, opts) {
                var element = this.cdiv;
                this.restoreFocus();
                if (this.shown && this.invokeEvent("beforehide", event, null, element)) {

                    this.currentMinHeight = undefined;
                    this.currentMinWidth = undefined;

                    this.div.hide();

                    if (this.parent) {
                        if (this.domReattached) {
                            this.saveInputValues(element);
                            var div = this.div.get(0);
                            this.shadeDiv.length && div.appendChild(this.shadeDiv.get(0));
                            div.appendChild(element.get(0));

                            this.domReattached = false;
                        }
                    }

                    var hideEvent = {};
                    hideEvent.parameters = opts || {};

                    var forms = $("form", element);
                    if (this.options.keepVisualState && forms) {
                        for (var i = 0; i < forms.length; i++) {
                            $(forms[i]).unbind("submit", this.setStateInput);
                        }
                    }

                    this.shown = false;
                    this.invokeEvent("hide", hideEvent, null, element)
                }
            },

            getStyle: function(elt, name) {
                return parseInt($(rf.getDomElement(elt)).css(name).replace("px", ""), 10);
            },

            doResizeOrMove: function(diff) {
                var vetoes = {};
                var shadowHash = {};
                var cssHash = {};
                var cssHashWH = {};
                var shadowHashWH = {};
                var contentHashWH = {};
                var scrollerHashWH = {};
                var newSize;
                var scrollerHeight = 22;
                var scrollerWidth = 0;
                var eContentElt = this.getContentElement();

                newSize = this.getStyle(eContentElt, "width");

                var oldWidthSize = newSize;
                newSize += diff.deltaWidth || 0;


                if (newSize >= this.currentMinWidth || this.options.autosized) {
                    cssHashWH.width = newSize + 'px';
                    shadowHashWH.width = newSize + 'px';
                    contentHashWH.width = newSize - scrollerWidth + 'px';
                    scrollerHashWH.width = newSize - scrollerWidth + 'px';
                } else {
                    cssHashWH.width = this.currentMinWidth + 'px';
                    shadowHashWH.width = this.currentMinWidth + 'px';
                    contentHashWH.width = this.currentMinWidth - scrollerWidth + 'px';
                    scrollerHashWH.width = this.currentMinWidth - scrollerWidth + 'px';
                    vetoes.vx = oldWidthSize - this.currentMinWidth;

                    vetoes.x = true;
                }

                if (newSize > this.options.maxWidth) {
                    if (diff.deltaWidth) {
                        cssHashWH.width = this.currentMaxWidth + 'px';
                        shadowHashWH.width = this.currentMaxWidth + 'px';
                        contentHashWH.width = this.currentMaxWidth - scrollerWidth + 'px';
                        scrollerHashWH.width = this.currentMaxWidth - scrollerWidth + 'px';
                        vetoes.vx = oldWidthSize - this.currentMaxWidth;
                    }

                    vetoes.x = true;
                }

                if (vetoes.vx && diff.deltaX) {
                    diff.deltaX = -vetoes.vx;
                }

                var eCdiv = $(this.cdiv);

                if (diff.deltaX && (vetoes.vx || !vetoes.x)) {
                    if (vetoes.vx) {
                        diff.deltaX = vetoes.vx;
                    }

                    var newLeftPos = this.getStyle(eCdiv, "left");
                    newLeftPos += diff.deltaX;
                    cssHash.left = newLeftPos + 'px';

                }

                newSize = this.getStyle(eContentElt, "height");

                var oldHeightSize = newSize;
                newSize += diff.deltaHeight || 0;

                if (newSize >= this.currentMinHeight || this.options.autosized) {
                    if (diff.deltaHeight) {
                        cssHashWH.height = newSize + 'px';
                        shadowHashWH.height = newSize + 'px';
                        scrollerHashWH.height = newSize - scrollerHeight + 'px';
                    }
                } else {
                    if (diff.deltaHeight) {
                        cssHashWH.height = this.currentMinHeight + 'px';
                        shadowHashWH.height = this.currentMinHeight + 'px';
                        scrollerHashWH.height = this.currentMinHeight - scrollerHeight + 'px';
                        vetoes.vy = oldHeightSize - this.currentMinHeight;
                    }

                    vetoes.y = true;
                }

                if (newSize > this.options.maxHeight) {
                    if (diff.deltaHeight) {
                        cssHashWH.height = this.currentMaxHeight + 'px';
                        shadowHashWH.height = this.currentMaxHeight + 'px';
                        scrollerHashWH.height = this.currentMaxHeight - scrollerHeight + 'px';
                        vetoes.vy = oldHeightSize - this.currentMaxHeight;
                    }

                    vetoes.y = true;
                }

                if (vetoes.vy && diff.deltaY) {
                    diff.deltaY = -vetoes.vy;
                }

                if (diff.deltaY && (vetoes.vy || !vetoes.y)) {
                    if (vetoes.vy) {
                        diff.deltaY = vetoes.vy;
                    }

                }
                if (diff.deltaY && (vetoes.vy || !vetoes.y)) {
                    if (vetoes.vy) {
                        diff.deltaY = vetoes.vy;
                    }

                    var newTopPos = this.getStyle(eCdiv, "top");
                    newTopPos += diff.deltaY;
                    cssHash.top = newTopPos + 'px';
                }
                eContentElt.css(cssHashWH);
                this.scrollerDiv.css(scrollerHashWH);
                if (this.eIframe) {
                    this.eIframe.css(scrollerHashWH);
                }
                this.shadowDiv.css(shadowHashWH);

                eCdiv.css(cssHash);
                this.shadowDiv.css(shadowHash);

                $.extend(this.userOptions, cssHash);
                $.extend(this.userOptions, cssHashWH);
                var w = this.width();
                var h = this.height();

                this.reductionData = null;

                if (w <= 2 * this.getInitialSize()) {
                    this.reductionData = {};
                    this.reductionData.w = w;
                }

                if (h <= 2 * this.getInitialSize()) {
                    if (!this.reductionData) {
                        this.reductionData = {};
                    }

                    this.reductionData.h = h;
                }

                if (this.header) {
                    this.header.doPosition();
                }

                return vetoes;
            },

            setSize : function (width, height) {
                var w = width - this.width();
                var h = height - this.height();
                var diff = new rf.rf4.ui.PopupPanel.Sizer.Diff(0, 0, w, h);
                this.doResizeOrMove(diff);
            },

            moveTo : function (top, left) {
                this.cdiv.css('top', top);
                this.cdiv.css('left', left);
            },

            move : function (dx, dy) {
                var diff = new rf.rf4.ui.PopupPanel.Sizer.Diff(dx, dy, 0, 0);
                this.doResizeOrMove(diff);
            },

            resize : function (dx, dy) {
                var diff = new rf.rf4.ui.PopupPanel.Sizer.Diff(0, 0, dx, dy);
                this.doResizeOrMove(diff);
            },

            findForm: function(elt) {
                var target = elt;
                while (target) {
                    if (target[0] && (!target[0].tagName /* document node doesn't have tagName */
                        || target[0].tagName.toLowerCase() != "form")) {

                        target = $(target).parent();
                    } else {
                        break;
                    }
                }

                return target;
            },

            setStateInput: function(event) {
                // Concret input but not entire form is a target element for onsubmit in FF
                var popup = event.data.popup;
                target = $(popup.findForm(event.currentTarget));

                var input = document.createElement("input");
                input.type = "hidden";
                input.id = popup.markerId + "OpenedState";
                input.name = popup.markerId + "OpenedState";
                input.value = popup.shown ? "true" : "false";
                target.append(input);

                $.each(popup.userOptions, function(key, value) {
                    input = document.createElement("input");
                    input.type = "hidden";
                    input.id = popup.markerId + "StateOption_" + key;
                    input.name = popup.markerId + "StateOption_" + key;
                    input.value = value;
                    target.append(input);
                });

                return true;
            }


        }

    })());
    $.extend(rf.rf4.ui.PopupPanel, {

            showPopupPanel : function (id, opts, event) {
                rf.Event.ready(function() {
                    rf.component(id).show()
                });
            },

            hidePopupPanel : function (id, opts, event) {
                rf.Event.ready(function() {
                    rf.component(id).hide()
                });
            }
        });

})(RichFaces.jQuery, window.RichFaces);
