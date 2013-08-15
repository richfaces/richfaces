/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
(function($, rf) {
    rf.utils = rf.utils || {};

    rf.utils.addCSSText = function(cssText, elementId) {
        var style = $("<style></style>").attr({type: 'text/css', id: elementId}).appendTo("head");
        try {
            style.html(cssText);
        } catch (e) {
            //IE
            style[0].styleSheet.cssText = cssText;
        }
    };
    
    rf.utils.getCSSRule = function (className) {
        return rf.utils.findCSSRule(function(selectorText) {
            return selectorText.toLowerCase() == className.toLowerCase();
        });
    };

    rf.utils.findCSSRule = function (selectFunction) {
        var rule = null;
        var sheets = document.styleSheets;
        for (var j = 0; !rule && j < sheets.length; j++) {
            try {
                var sheet = sheets[j];
                var rules = sheet.cssRules ? sheet.cssRules : sheet.rules;
                for (var i = 0; !rule && i < rules.length; i++) {
                    if (rules[i].selectorText && selectFunction(rules[i].selectorText)) {
                        rule = rules[i];
                    }
                }
            } catch (e) {
                rf.log.debug("Cannot obtain CSS rule for " + (sheet.href || sheet) + ": " + e);
            }
        }
        return rule;
    };

    rf.utils.Ranges = function() {
        this.ranges = [];
    };

    rf.utils.Ranges.prototype = {

        add: function(index) {
            var i = 0;
            while (i < this.ranges.length && index >= this.ranges[i++][1]);
            i--;
            if (this.ranges[i - 1] && index == (this.ranges[i - 1][1] + 1)) {
                if (index == (this.ranges[i][0] - 1)) {
                    this.ranges[i - 1][1] = this.ranges[i][1];
                    this.ranges.splice(i, 1);
                } else {
                    this.ranges[i - 1][1]++;
                }
            } else {
                if (this.ranges[i]) {
                    if (this.ranges[i] && index == (this.ranges[i][0] - 1)) {
                        this.ranges[i][0]--;
                    } else {
                        if (index == (this.ranges[i][1] + 1)) {
                            this.ranges[i][1]++;
                        } else {
                            if (index < this.ranges[i][1]) {
                                this.ranges.splice(i, 0, [index, index]);
                            } else {
                                this.ranges.splice(i + 1, 0, [index, index]);
                            }
                        }
                    }
                } else {
                    this.ranges.splice(i, 0, [index, index]);
                }
            }
        },

        remove: function(index) {
            var i = 0;
            while (i < this.ranges.length && index > this.ranges[i++][1]);
            i--;
            if (this.ranges[i]) {
                if (index == (this.ranges[i][1])) {
                    if (index == (this.ranges[i][0])) {
                        this.ranges.splice(i, 1);
                    } else {
                        this.ranges[i][1]--;
                    }
                } else {
                    if (index == (this.ranges[i][0])) {
                        this.ranges[i][0]++;
                    } else {
                        this.ranges.splice(i + 1, 0, [index + 1, this.ranges[i][1]]);
                        this.ranges[i][1] = index - 1;
                    }
                }
            }
        },

        clear: function() {
            this.ranges = [];
        },

        contains: function(index) {
            var i = 0;
            while (i < this.ranges.length && index >= this.ranges[i][0]) {
                if (index >= this.ranges[i][0] && index <= this.ranges[i][1]) {
                    return true;
                } else {
                    i++;
                }
            }
            return false;
        },

        toString: function() {
            var ret = new Array(this.ranges.length);
            for (var i = 0; i < this.ranges.length; i++) {
                ret[i] = this.ranges[i].join();
            }
            return ret.join(";");
        }
    };

    var WIDTH_CLASS_NAME_BASE = "rf-edt-c-";
    var MIN_WIDTH = 20;

    rf.ui = rf.ui || {};

    rf.ui.ExtendedDataTable = rf.BaseComponent.extendClass({

            name: "ExtendedDataTable",

            init: function (id, rowCount, ajaxFunction, options) {
                $super.constructor.call(this, id);
                this.ranges = new rf.utils.Ranges();
                this.rowCount = rowCount;
                this.ajaxFunction = ajaxFunction;
                this.options = options || {};
                this.element = this.attachToDom();
                this.newWidths = {};
                this.storeDomReferences();
                if (this.options['onready'] && typeof this.options['onready'] == 'function') {
                    rf.Event.bind(this.element, "r:ready", this.options['onready']);
                }
                this.resizeEventName = "resize.rf.edt." + this.id;
                $(document).ready($.proxy(this.initialize, this));
                this.activateResizeListener();
                $(this.scrollElement).bind("scroll", $.proxy(this.updateScrollPosition, this));
                this.bindHeaderHandlers();
                $(this.element).bind("r:onajaxcomplete", $.proxy(this.ajaxComplete, this));
                
                this.resizeData = {};
                this.idOfReorderingColumn = "";
                this.timeoutId = null;
            },

            storeDomReferences: function() {
                this.dragElement = document.getElementById(this.id + ":d");
                this.reorderElement = document.getElementById(this.id + ":r");
                this.reorderMarkerElement = document.getElementById(this.id + ":rm");
                this.widthInput = document.getElementById(this.id + ":wi");
                this.selectionInput = document.getElementById(this.id + ":si");


                this.header = $(this.element).children(".rf-edt-hdr");
                this.headerCells = this.header.find(".rf-edt-hdr-c");
                this.footerCells = $(this.element).children(".rf-edt-ftr").find(".rf-edt-ftr-c");
                this.resizerHolders = this.header.find(".rf-edt-rsz-cntr");

                this.frozenHeaderPartElement = document.getElementById(this.id + ":frozenHeader");
                this.frozenColumnCount = this.frozenHeaderPartElement ? this.frozenHeaderPartElement.firstChild.rows[0].cells.length : 0;//TODO Richfaces.firstDescendant;

                this.headerElement = document.getElementById(this.id + ":header");
                this.footerElement = document.getElementById(this.id + ":footer");
                this.scrollElement = document.getElementById(this.id + ":scrl");
                this.scrollContentElement = document.getElementById(this.id + ":scrl-cnt");

            },

            getColumnPosition: function(id) {
                var position;
                for (var i = 0; i < this.headerCells.length; i++) {
                    if (id == this.headerCells[i].className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1]) {
                        position = i;
                    }
                }
                return position;
            },

            setColumnPosition: function(id, position) {
                var colunmsOrder = "";
                var before;
                for (var i = 0; i < this.headerCells.length; i++) {
                    var current = this.headerCells[i].className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
                    if (i == position) {
                        if (before) {
                            colunmsOrder += current + "," + id + ",";
                        } else {
                            colunmsOrder += id + "," + current + ",";
                        }
                    } else {
                        if (id != current) {
                            colunmsOrder += current + ",";
                        } else {
                            before = true;
                        }
                    }
                }
                this.ajaxFunction(null, {"r:columnsOrder" : colunmsOrder}); // TODO Maybe, event model should be used here.
            },

            setColumnWidth: function(columnId, width) {
                width = width + "px";
                var $table = $(document.getElementById(this.element.id));
                $table.find("." + WIDTH_CLASS_NAME_BASE + columnId).parent().css('width', width);
                $table.find("." + WIDTH_CLASS_NAME_BASE + columnId).css('width', width);
                this.newWidths[columnId] = width;
                var widthsArray = new Array();
                for (var id in this.newWidths) {
                    widthsArray.push(id + ":" + this.newWidths[id]);
                }
                this.widthInput.value = widthsArray.toString();
                this.updateLayout();
                this.adjustResizers();
                this.ajaxFunction(); // TODO Maybe, event model should be used here.
            },

            filter: function(colunmId, filterValue, isClear) {
                if (typeof(filterValue) == "undefined" || filterValue == null) {
                    filterValue = "";
                }
                var map = {};
                map[this.id + "r:filtering"] = colunmId + ":" + filterValue + ":" + isClear;
                this.ajaxFunction(null, map); // TODO Maybe, event model should be used here.
            },

            clearFiltering: function() {
                this.filter("", "", true);
            },

            sortHandler: function(event) {
                var sortHandle = $(event.data.sortHandle);
                var button = sortHandle.find('.rf-edt-srt-btn');
                var columnId = button.data('columnid');
                var sortOrder = button.hasClass('rf-edt-srt-asc') ? 'descending' : 'ascending';
                this.sort(columnId, sortOrder, false);
            },

            filterHandler: function(event) {
                var filterHandle = $(event.data.filterHandle);
                var columnId = filterHandle.data('columnid');
                var filterValue = filterHandle.val();
                this.filter(columnId, filterValue, false);
            },


            sort: function(colunmId, sortOrder, isClear) {
                if (typeof(sortOrder) == "string") {
                    sortOrder = sortOrder.toLowerCase();
                }
                var map = {}
                map[this.id + "r:sorting"] = colunmId + ":" + sortOrder + ":" + isClear;
                this.ajaxFunction(null, map); // TODO Maybe, event model should be used here.
            },

            clearSorting: function() {
                this.sort("", "", true);
            },

            destroy: function() {
                $(window).unbind("resize", this.updateLayout);
                $(rf.getDomElement(this.id + ':st')).remove();
                $super.destroy.call(this);
            },

            bindHeaderHandlers: function() {
                this.header.find(".rf-edt-rsz").bind("mousedown", $.proxy(this.beginResize, this));
                this.headerCells.bind("mousedown", $.proxy(this.beginReorder, this));
                var self = this;
                this.header.find(".rf-edt-c-srt").each(function() {
                    $(this).bind("click", {sortHandle: this}, $.proxy(self.sortHandler, self));
                });
                this.header.find(".rf-edt-flt-i").each(function() {
                    $(this).bind("blur", {filterHandle: this}, $.proxy(self.filterHandler, self));
                });
            },

            updateLayout: function() {
                this.deActivateResizeListener();
                this.headerCells.height("auto");
                var headerCellHeight = 0;
                this.headerCells.each(function() {
                    if (this.clientHeight > headerCellHeight) {
                        headerCellHeight = this.clientHeight;
                    }
                });
                this.headerCells.height(headerCellHeight + "px");
                this.footerCells.height("auto");
                var footerCellHeight = 0;
                this.footerCells.each(function() {
                    if (this.clientHeight > footerCellHeight) {
                        footerCellHeight = this.clientHeight;
                    }
                });
                this.footerCells.height(footerCellHeight + "px");
                this.normalPartStyle.width = "auto";
                var offsetWidth = this.frozenHeaderPartElement ? this.frozenHeaderPartElement.offsetWidth : 0;
                var width = Math.max(0, this.element.clientWidth - offsetWidth);
                if (width) {
                    this.parts.each(function() {
                        this.style.width = "auto";
                    });
                    var contentWidth = this.parts.width();
                    if (contentWidth > width) {
                        this.normalPartStyle.width = width + "px";
                    }
                    this.normalPartStyle.display = "block";
                    // update scroller and scroll-content
                    if (contentWidth > width) {
                        this.parts.each(function() {
                            this.style.width = width + "px";
                        });
                        this.scrollElement.style.display = "block";
                        this.scrollElement.style.overflowX = "scroll";
                        this.scrollElement.style.width = width + "px";
                        this.scrollContentElement.style.width = contentWidth + "px";
                        this.updateScrollPosition();
                    } else {
                        this.parts.each(function() {
                            this.style.width = "";
                        });
                        this.scrollElement.style.display = "none";
                    }
                } else {
                    this.normalPartStyle.display = "none";
                }
                var height = this.element.clientHeight;
                var el = this.element.firstChild;
                while (el && (!el.nodeName || el.nodeName.toUpperCase() != "TABLE")) {
                    if (el.nodeName && el.nodeName.toUpperCase() == "DIV" && el != this.bodyElement) {
                        height -= el.offsetHeight;
                    }
                    el = el.nextSibling;
                }
                if (this.bodyElement.offsetHeight > height || !this.contentElement) {
                    this.bodyElement.style.height = height + "px";
                }
                this.activateResizeListener();
            },

            adjustResizers: function() { //For IE7 only.
                var scrollLeft = this.scrollElement ? this.scrollElement.scrollLeft : 0;
                var clientWidth = this.element.clientWidth - 3;
                var i = 0;
                for (; i < this.frozenColumnCount; i++) {
                    if (clientWidth > 0) {
                        this.resizerHolders[i].style.display = "none";
                        this.resizerHolders[i].style.display = "";
                        clientWidth -= this.resizerHolders[i].offsetWidth;
                    }
                    if (clientWidth <= 0) {
                        this.resizerHolders[i].style.display = "none";
                    }
                }
                scrollLeft -= 3;
                for (; i < this.resizerHolders.length; i++) {
                    if (clientWidth > 0) {
                        this.resizerHolders[i].style.display = "none";
                        if (scrollLeft > 0) {
                            this.resizerHolders[i].style.display = "";
                            scrollLeft -= this.resizerHolders[i].offsetWidth;
                            if (scrollLeft > 0) {
                                this.resizerHolders[i].style.display = "none";
                            } else {
                                clientWidth += scrollLeft;
                            }
                        } else {
                            this.resizerHolders[i].style.display = "";
                            clientWidth -= this.resizerHolders[i].offsetWidth;
                        }
                    }
                    if (clientWidth <= 0) {
                        this.resizerHolders[i].style.display = "none";
                    }
                }
            },

            updateScrollPosition: function() {
                if (this.scrollElement) {
                    var scrollLeft = this.scrollElement.scrollLeft;
                    this.parts.each(function() {
                        this.scrollLeft = scrollLeft;
                    });
                }
                this.adjustResizers();
            },

            initialize: function() {
                this.deActivateResizeListener();
                if (! $(this.element).is(":visible")) {
                    this.showOffscreen(this.element);
                }
                this.bodyElement = document.getElementById(this.id + ":b");
                this.bodyElement.tabIndex = -1; //TODO don't use tabIndex.
                this.normalPartStyle = rf.utils.getCSSRule("div.rf-edt-cnt").style;
                var bodyJQuery = $(this.bodyElement);
                this.contentElement = bodyJQuery.children("div:not(.rf-edt-ndt):first")[0];
                if (this.contentElement) {
                    this.spacerElement = this.contentElement.firstChild;//TODO this.marginElement = Richfaces.firstDescendant(this.contentElement);
                    this.dataTableElement = this.contentElement.lastChild;//TODO this.dataTableElement = Richfaces.lastDescendant(this.contentElement);
                    this.tbodies = $(document.getElementById(this.id + ":tbf")).add(document.getElementById(this.id + ":tbn"));
                    this.rows = this.tbodies[0].rows.length;
                    this.rowHeight = this.dataTableElement.offsetHeight / this.rows;
                    if (this.rowCount != this.rows) {
                        this.contentElement.style.height = (this.rowCount * this.rowHeight) + "px";
                    }
                    bodyJQuery.bind("scroll", $.proxy(this.bodyScrollListener, this));
                    if (this.options.selectionMode != "none") {
                        this.tbodies.bind("click", $.proxy(this.selectionClickListener, this));
                        bodyJQuery.bind(window.opera ? "keypress" : "keydown", $.proxy(this.selectionKeyDownListener, this));
                        this.initializeSelection();
                    }
                } else {
                    this.spacerElement = null;
                    this.dataTableElement = null;
                }
                this.parts = $(this.element).find(".rf-edt-cnt, .rf-edt-ftr-cnt");
                this.updateLayout();
                this.updateScrollPosition(); //TODO Restore horizontal scroll position
                if ($(this.element).data('offscreenElements')) {
                    this.hideOffscreen(this.element);
                }
                this.activateResizeListener();
                $(this.element).triggerHandler("r:ready", this);
            },

            showOffscreen: function(element) {
                var $element = $(element);
                var offscreenElements = $element.parents(":not(:visible)").addBack().toArray().reverse();
                var that = this;
                $.each(offscreenElements, function() {
                    $this = $(this);
                    if ($this.css('display') === 'none') {
                        that.showOffscreenElement($(this));
                    }
                })
                $element.data('offscreenElements', offscreenElements);
            },

            hideOffscreen: function(element) {
                var $element = $(element);
                var offscreenElements = $element.data('offscreenElements');
                var that = this;
                $.each(offscreenElements, function() {
                    $this = $(this);
                    if ($this.data('offscreenOldValues')) {
                        that.hideOffscreenElement($(this));
                    }
                })
                $element.removeData('offscreenElements');
            },

            showOffscreenElement: function($element) {
                var offscreenOldValues = {};
                offscreenOldValues.oldPosition = $element.css('position');
                offscreenOldValues.oldLeft = $element.css('left');
                offscreenOldValues.oldDisplay = $element.css('display');
                $element.css('position', 'absolute');
                $element.css('left', '-10000');
                $element.css('display', 'block');
                $element.data('offscreenOldValues', offscreenOldValues);
            },

            hideOffscreenElement: function($element) {
                var offscreenOldValues = $element.data('offscreenOldValues');
                $element.css('display', offscreenOldValues.oldDisplay);
                $element.css('left', offscreenOldValues.oldLeft);
                $element.css('position', offscreenOldValues.oldPosition);
                $element.removeData('offscreenOldValues');
            },

            drag: function(event) {
                $(this.dragElement).setPosition({left:Math.max(this.resizeData.left + MIN_WIDTH, event.pageX)});
                return false;
            },

            beginResize: function(event) {
                var id = event.currentTarget.parentNode.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
                this.resizeData = {
                    id : id,
                    left : $(event.currentTarget).parent().offset().left
                };
                this.dragElement.style.height = this.element.offsetHeight + "px";
                $(this.dragElement).setPosition({top:$(this.element).offset().top, left:event.pageX});
                this.dragElement.style.display = "block";
                $(document).bind("mousemove", $.proxy(this.drag, this));
                $(document).one("mouseup", $.proxy(this.endResize, this));
                return false;
            },

            endResize: function(event) {
                $(document).unbind("mousemove", this.drag);
                this.dragElement.style.display = "none";
                var width = Math.max(MIN_WIDTH, event.pageX - this.resizeData.left);
                this.setColumnWidth(this.resizeData.id, width);
            },

            reorder: function(event) {
                $(this.reorderElement).setPosition(event, {offset:[5,5]});
                this.reorderElement.style.display = "block";
                return false;
            },

            beginReorder: function(event) {
                if (!$(event.target).is("a, img, :input")) {
                    this.idOfReorderingColumn = event.currentTarget.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
                    $(document).bind("mousemove", $.proxy(this.reorder, this));
                    this.headerCells.bind("mouseover", $.proxy(this.overReorder, this));
                    $(document).one("mouseup", $.proxy(this.cancelReorder, this));
                    return false;
                }
            },

            overReorder: function(event) {
                if (this.idOfReorderingColumn != event.currentTarget.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1]) {
                    var eventElement = $(event.currentTarget);
                    var offset = eventElement.offset();
                    $(this.reorderMarkerElement).setPosition({top:offset.top + eventElement.height(), left:offset.left - 5});
                    this.reorderMarkerElement.style.display = "block";
                    eventElement.one("mouseout", $.proxy(this.outReorder, this));
                    eventElement.one("mouseup", $.proxy(this.endReorder, this));
                }
            },

            outReorder: function(event) {
                this.reorderMarkerElement.style.display = "";
                $(event.currentTarget).unbind("mouseup", this.endReorder);
            },

            endReorder: function(event) {
                this.reorderMarkerElement.style.display = "";
                $(event.currentTarget).unbind("mouseout", this.outReorder);
                var id = event.currentTarget.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
                var colunmsOrder = "";
                var _this = this;
                this.headerCells.each(function() {
                    var i = this.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
                    if (i == id) {
                        colunmsOrder += _this.idOfReorderingColumn + "," + id + ",";
                    } else if (i != _this.idOfReorderingColumn) {
                        colunmsOrder += i + ",";
                    }
                });
                this.ajaxFunction(event, {"r:columnsOrder" : colunmsOrder}); // TODO Maybe, event model should be used here.
            },

            cancelReorder: function(event) {
                $(document).unbind("mousemove", this.reorder);
                this.headerCells.unbind("mouseover", this.overReorder);
                this.reorderElement.style.display = "none";
            },

            loadData: function(event) {
                var clientFirst = Math.round((this.bodyElement.scrollTop + this.bodyElement.clientHeight / 2) / this.rowHeight - this.rows / 2);
                if (clientFirst <= 0) {
                    clientFirst = 0;
                } else {
                    clientFirst = Math.min(this.rowCount - this.rows, clientFirst);
                }
                this.ajaxFunction(event, {"r:clientFirst" : clientFirst});// TODO Maybe, event model should be used here.
            },

            bodyScrollListener: function(event) {
                if (this.timeoutId) {
                    window.clearTimeout(this.timeoutId);
                    this.timeoutId = null;
                }
                if (Math.max(event.currentTarget.scrollTop - this.rowHeight, 0) < this.spacerElement.offsetHeight
                    || Math.min(event.currentTarget.scrollTop + this.rowHeight + event.currentTarget.clientHeight, event.currentTarget.scrollHeight) > this.spacerElement.offsetHeight + this.dataTableElement.offsetHeight) {
                    var _this = this;
                    this.timeoutId = window.setTimeout(function (event) {
                        _this.loadData(event)
                    }, 1000);
                }
            },

            showActiveRow: function() {
                if (this.bodyElement.scrollTop > this.activeIndex * this.rowHeight + this.spacerElement.offsetHeight) { //UP
                    this.bodyElement.scrollTop = Math.max(this.bodyElement.scrollTop - this.rowHeight, 0);
                } else if (this.bodyElement.scrollTop + this.bodyElement.clientHeight
                    < (this.activeIndex + 1) * this.rowHeight + this.spacerElement.offsetHeight) { //DOWN
                    this.bodyElement.scrollTop = Math.min(this.bodyElement.scrollTop + this.rowHeight, this.bodyElement.scrollHeight - this.bodyElement.clientHeight);
                }
            },

            selectRow: function(index) {
                this.ranges.add(index);
                for (var i = 0; i < this.tbodies.length; i++) {
                    $(this.tbodies[i].rows[index]).addClass("rf-edt-r-sel");
                }
            },

            deselectRow: function (index) {
                this.ranges.remove(index);
                for (var i = 0; i < this.tbodies.length; i++) {
                    $(this.tbodies[i].rows[index]).removeClass("rf-edt-r-sel");
                }
            },

            setActiveRow: function (index) {
                if (typeof this.activeIndex == "number") {
                    for (var i = 0; i < this.tbodies.length; i++) {
                        $(this.tbodies[i].rows[this.activeIndex]).removeClass("rf-edt-r-act");
                    }

                }
                this.activeIndex = index;
                for (var i = 0; i < this.tbodies.length; i++) {
                    $(this.tbodies[i].rows[this.activeIndex]).addClass("rf-edt-r-act");
                }
            },

            resetShiftRow: function () {
                if (typeof this.shiftIndex == "number") {
                    for (var i = 0; i < this.tbodies.length; i++) {
                        $(this.tbodies[i].rows[this.shiftIndex]).removeClass("rf-edt-r-sht");
                    }

                }
                this.shiftIndex = null;
            },

            setShiftRow: function (index) {
                this.resetShiftRow();
                this.shiftIndex = index;
                if (typeof index == "number") {
                    for (var i = 0; i < this.tbodies.length; i++) {
                        $(this.tbodies[i].rows[this.shiftIndex]).addClass("rf-edt-r-sht");
                    }
                }
            },

            initializeSelection: function() {
                this.ranges.clear();
                var strings = this.selectionInput.value.split("|");
                this.activeIndex = strings[1] || null;
                this.shiftIndex = strings[2] || null;
                this.selectionFlag = null;
                var rows = this.tbodies[0].rows;
                for (var i = 0; i < rows.length; i++) {
                    var row = $(rows[i]);
                    if (row.hasClass("rf-edt-r-sel")) {
                        this.ranges.add(row[0].rowIndex)
                    }
                    if (row.hasClass("rf-edt-r-act")) {
                        this.activeIndex = row[0].rowIndex;
                    }
                    if (row.hasClass("rf-edt-r-sht")) {
                        this.shiftIndex = row[0].rowIndex;
                    }
                }
                this.writeSelection();
            },

            writeSelection: function() {
                this.selectionInput.value = [this.ranges, this.activeIndex, this.shiftIndex, this.selectionFlag].join("|");
            },

            selectRows: function(range) {
                if (typeof range == "number") {
                    range = [range, range];
                }
                var changed;
                var i = 0;
                for (; i < range[0]; i++) {
                    if (this.ranges.contains(i)) {
                        this.deselectRow(i);
                        changed = true;
                    }
                }
                for (; i <= range[1]; i++) {
                    if (!this.ranges.contains(i)) {
                        this.selectRow(i);
                        changed = true;
                    }
                }
                for (; i < this.rows; i++) {
                    if (this.ranges.contains(i)) {
                        this.deselectRow(i);
                        changed = true;
                    }
                }
                this.selectionFlag = typeof this.shiftIndex == "string" ? this.shiftIndex : "x";
                return changed;
            },

            processSlectionWithShiftKey: function(index) {
                if (this.shiftIndex == null) {
                    this.setShiftRow(this.activeIndex != null ? this.activeIndex : index);
                }
                var range;
                if ("u" == this.shiftIndex) {
                    range = [0, index];
                } else if ("d" == this.shiftIndex) {
                    range = [index, this.rows - 1];
                } else if (index >= this.shiftIndex) {
                    range = [this.shiftIndex, index];
                } else {
                    range = [index, this.shiftIndex];
                }
                return this.selectRows(range);
            },

            onbeforeselectionchange: function (event) {
                return !this.options.onbeforeselectionchange || this.options.onbeforeselectionchange.call(this.element, event) != false;
            },

            onselectionchange: function (event, index, changed) {
                if (!event.shiftKey) {
                    this.resetShiftRow();
                }
                if (this.activeIndex != index) {
                    this.setActiveRow(index);
                    this.showActiveRow();
                }
                if (changed) {
                    this.writeSelection();
                    if (this.options.onselectionchange) {
                        this.options.onselectionchange.call(this.element, event);
                    }
                }
            },

            selectionClickListener: function (event) {
                if (!this.onbeforeselectionchange(event)) {
                    return;
                }
                var changed;
                if (event.shiftKey || event.ctrlKey) {
                    if (window.getSelection) { //TODO Try to find other way.
                        window.getSelection().removeAllRanges();
                    } else if (document.selection) {
                        document.selection.empty();
                    }
                }
                var tr = event.target;
                while (this.tbodies.index(tr.parentNode) == -1) {
                    tr = tr.parentNode;
                }
                var index = tr.rowIndex;
                if (this.options.selectionMode == "single" || (this.options.selectionMode != "multipleKeyboardFree"
                    && !event.shiftKey && !event.ctrlKey)) {
                    changed = this.selectRows(index);
                } else if (this.options.selectionMode == "multipleKeyboardFree" || (!event.shiftKey && event.ctrlKey)) {
                    if (this.ranges.contains(index)) {
                        this.deselectRow(index);
                    } else {
                        this.selectRow(index);
                    }
                    changed = true;
                } else {
                    changed = this.processSlectionWithShiftKey(index);
                }
                this.onselectionchange(event, index, changed);
            },

            selectionKeyDownListener: function(event) {
                if (event.ctrlKey && this.options.selectionMode != "single" && (event.keyCode == 65 || event.keyCode == 97) //Ctrl-A
                    && this.onbeforeselectionchange(event)) {
                    this.selectRows([0, rows]);
                    this.selectionFlag = "a";
                    this.onselectionchange(event, this.activeIndex, true); //TODO Is there a way to know that selection haven't changed?
                    event.preventDefault();
                } else {
                    var index;
                    if (event.keyCode == 38) { //UP
                        index = -1;
                    } else if (event.keyCode == 40) { //DOWN
                        index = 1;
                    }
                    if (index != null && this.onbeforeselectionchange(event)) {
                        if (typeof this.activeIndex == "number") {
                            index += this.activeIndex;
                            if (index >= 0 && index < this.rows) {
                                var changed;
                                if (this.options.selectionMode == "single" || (!event.shiftKey && !event.ctrlKey)) {
                                    changed = this.selectRows(index);
                                } else if (event.shiftKey) {
                                    changed = this.processSlectionWithShiftKey(index);
                                }
                                this.onselectionchange(event, index, changed);
                            }
                        }
                        event.preventDefault();
                    }
                }
            },

            ajaxComplete: function (event, data) {
                this.storeDomReferences();
                if (data.reinitializeHeader) {
                    this.bindHeaderHandlers();
                    this.updateLayout();
                } else {
                    this.selectionInput = document.getElementById(this.id + ":si");
                    if (data.reinitializeBody) {
                        this.rowCount = data.rowCount;
                        this.initialize();
                    } else if (this.options.selectionMode != "none") {
                        this.initializeSelection();
                    }
                    if (this.spacerElement) {
                        this.spacerElement.style.height = (data.first * this.rowHeight) + "px";
                    }
                }
            },

            activateResizeListener: function() {
                if (typeof this.resizeEventName !== "undefined") {
                    $(window).on(this.resizeEventName, $.proxy(this.updateLayout, this));
                }
            },

            deActivateResizeListener: function() {
                if (typeof this.resizeEventName !== "undefined") {
                    $(window).off(this.resizeEventName);
                }
            },

            contextMenuAttach: function (menu) {
                var selector = "[id='" + this.element.id + "'] ";
                selector += (typeof menu.options.targetSelector === 'undefined')
                    ?  ".rf-edt-b td" : menu.options.targetSelector;
                selector = $.trim(selector);
                rf.Event.bind(selector, menu.options.showEvent, $.proxy(menu.__showHandler, menu), menu);
            },

            contextMenuShow: function (menu, event) {
                var tr = event.target;
                while (this.tbodies.index(tr.parentNode) == -1) {
                    tr = tr.parentNode;
                }
                var index = tr.rowIndex;
                if (! this.ranges.contains(index) ) {
                    this.selectionClickListener(event);
                }
            }
        });

    var $super = rf.ui.ExtendedDataTable.$super;
}(RichFaces.jQuery, RichFaces));