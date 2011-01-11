(function ($, richfaces) {

    richfaces.ui = richfaces.ui || {};

    var initButtons = function(buttons, css, component) {
        var id;

        var fn = function(e) {
            e.data.fn.call(e.data.component, e);
        }

        var data = {};
        data.component = component;

        for (id in buttons) {
            var element = $(document.getElementById(id));

            data.id = id;
            data.page = buttons[id];
            data.element = element;
            data.fn = component.processClick;

            element.bind('click', copy(data), fn);
        }
    };

    var copy = function(data) {
        var key;
        var eventData = {};

        for (key in data) {
            eventData[key] = data[key];
        }

        return eventData;
    };

    richfaces.ui.DataScroller = function(id, submit, options) {

        $super.constructor.call(this, id);

        var dataScrollerElement = this.attachToDom();

        this.options = options;
        this.currentPage = options.currentPage;
        var buttons = options.buttons;
        var digitals = options.digitals;

        if (submit && typeof submit == 'function') {
            RichFaces.Event.bindById(id, this.getScrollEventName(), submit);
        }

        var css = {};

        if (buttons) {

            $(dataScrollerElement).delegate('.rf-ds-btn', 'mouseover mouseup mouseout mousedown', function(event) {
                if (event.type == 'mousedown') {
                    $(this).addClass('rf-ds-hov');
                } else {
                    $(this).removeClass('rf-ds-hov');
                }
            });

            initButtons(buttons.left, css, this);
            initButtons(buttons.right, css, this);
        }

        if (digitals) {

            $(dataScrollerElement).delegate('.rf-ds-nmb-btn', 'mouseover mouseup mouseout mousedown', function(event) {
                if (event.type == 'mouseover' || event.type == 'mouseup') {
                    $(this).addClass('rf-ds-hov');
                } else if (event.type == 'mouseout') {
                    $(this).removeClass('rf-ds-hov');
                } else if (event.type == 'mousedown')  {
                    $(this).toggleClass('rf-ds-press');
                }
            });

            initButtons(digitals, css, this);
        }
    };

    richfaces.BaseComponent.extend(richfaces.ui.DataScroller);
    var $super = richfaces.ui.DataScroller.$super;

    $.extend(richfaces.ui.DataScroller.prototype, (function (options) {

        var scrollEventName = "rich:datascroller:onscroll";

        return {

            name: "RichFaces.ui.DataScroller",

            processClick: function(event) {
                var data = event.data;
                if (data) {
                    var page = data.page;
                    if (page) {
                        this.switchToPage(page);
                    }
                }
            },

            switchToPage: function(page) {
                if (typeof page != 'undefined' && page != null) {
                    RichFaces.Event.fireById(this.id, this.getScrollEventName(), {'page' : page});
                }
            },

            fastForward: function() {
                this.switchToPage("fastforward");
            },

            fastRewind: function() {
                this.switchToPage("fastrewind");
            },

            next: function() {
                this.switchToPage("next");
            },

            previous: function() {
                this.switchToPage("previous");
            },

            first: function() {
                this.switchToPage("first");
            },

            last: function() {
                this.switchToPage("last");
            },

            getScrollEventName: function() {
                return scrollEventName;
            },
            destroy: function() {
                $super.destroy.call(this);
            }
        }

    })());

})(jQuery, window.RichFaces);