(function ($, rf) {

    rf.ui = rf.ui || {};

    var initButtons = function(buttons, css, component) {
        var id;

        var fn = function(e) {
            e.data.fn.call(e.data.component, e);
        };

        var data = {};
        data.component = component;

        for (id in buttons) {
            if (buttons.hasOwnProperty(id)) {
                var element = $(document.getElementById(id));
    
                data.id = id;
                data.page = buttons[id];
                data.element = element;
                data.fn = component.processClick;
    
                element.bind('click', copy(data), fn);
            }
        }
    };

    var copy = function(data) {
        var key;
        var eventData = {};

        for (key in data) {
            if (data.hasOwnProperty(key)) {
                eventData[key] = data[key];
            }
        }

        return eventData;
    };

    var togglePressClass = function(el, event) {
        if (event.type == 'mousedown') {
            el.addClass('rf-ds-press');
        } else if (event.type == 'mouseup' || event.type == 'mouseout') {
            el.removeClass('rf-ds-press');
        }
    };

    /**
     * Backing object for rich:dataScroller
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.DataScroller
     * 
     * @param id
     * @param submit
     * @param options
     */
    rf.ui.DataScroller = function(id, submit, options) {

        $super.constructor.call(this, id);

        var dataScrollerElement = this.attachToDom();

        this.options = options;
        this.currentPage = options.currentPage;

        if (submit && typeof submit == 'function') {
            RichFaces.Event.bindById(id, this.getScrollEventName(), submit);
        }

        var css = {};

        if (options.buttons) {

            $(dataScrollerElement).delegate('.rf-ds-btn', 'mouseup mousedown mouseout', function(event) {
                if ($(this).hasClass('rf-ds-dis')) {
                    $(this).removeClass('rf-ds-press');
                } else {
                    togglePressClass($(this), event);
                }
            });

            initButtons(options.buttons.left, css, this);
            initButtons(options.buttons.right, css, this);
        }

        if (options.digitals) {

            $(dataScrollerElement).delegate('.rf-ds-nmb-btn', 'mouseup mousedown mouseout', function(event) {
                togglePressClass($(this), event);
            });

            initButtons(options.digitals, css, this);
        }
    };

    rf.BaseComponent.extend(rf.ui.DataScroller);
    var $super = rf.ui.DataScroller.$super;

    $.extend(rf.ui.DataScroller.prototype, (function () {

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

            /**
             * Switch to a page
             * 
             * @method
             * @name RichFaces.ui.DataScroller#switchToPage
             * @param page {int|string} page number or a string identifier ("next", "previous", "first", "last", "fastForward", "fastRewind")
             */
            switchToPage: function(page) {
                if (typeof page != 'undefined' && page != null) {
                    RichFaces.Event.fireById(this.id, this.getScrollEventName(), {'page' : page});
                }
            },

            /**
             * Skip forward to a new page based on @fastStep
             * 
             * @method
             * @name RichFaces.ui.DataScroller#fastForward
             */
            fastForward: function() {
                this.switchToPage("fastForward");
            },

            /**
             * Skip backward to a new page based on @fastStep
             * 
             * @method
             * @name RichFaces.ui.DataScroller#fastRewind
             */
            fastRewind: function() {
                this.switchToPage("fastRewind");
            },

            /**
             * Switch to the next page
             * 
             * @method
             * @name RichFaces.ui.DataScroller#next
             */
            next: function() {
                this.switchToPage("next");
            },

            /**
             * Switch to the previous page
             * 
             * @method
             * @name RichFaces.ui.DataScroller#fastForward
             */
            previous: function() {
                this.switchToPage("previous");
            },

            /**
             * Switch to the first page
             * 
             * @method
             * @name RichFaces.ui.DataScroller#fastForward
             */
            first: function() {
                this.switchToPage("first");
            },

            /**
             * Switch to the last page
             * 
             * @method
             * @name RichFaces.ui.DataScroller#fastForward
             */
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

})(RichFaces.jQuery, window.RichFaces);