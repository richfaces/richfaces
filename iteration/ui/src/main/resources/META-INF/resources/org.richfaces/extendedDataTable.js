/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
(function(richfaces, jQuery) {
    richfaces.utils = richfaces.utils || {};

    richfaces.utils.getCSSRule = function (className) {
		var rule = null;
		var sheets = document.styleSheets;
		for (var j = 0; !rule && j < sheets.length; j++) {
			var rules = sheets[j].cssRules ? sheets[j].cssRules : sheets[j].rules;
			for (var i = 0; !rule && i < rules.length; i++) {
				if (rules[i].selectorText && rules[i].selectorText.toLowerCase() == className.toLowerCase()) {
					rule = rules[i];
				}
			}
		}
		return rule;			
	};

	richfaces.utils.Ranges = function() {
		this.ranges = [];
	};
	
	richfaces.utils.Ranges.prototype = {

		add: function(index) {
			var i = 0;
			while(i < this.ranges.length && index >= this.ranges[i++][1]);
			i--;
			if(this.ranges[i-1] && index==(this.ranges[i-1][1]+1) ) {
				if(index==(this.ranges[i][0]-1)) {
					this.ranges[i-1][1] = this.ranges[i][1];
					this.ranges.splice(i, 1);
				} else {
					this.ranges[i-1][1]++;			
				}
			} else {
				if(this.ranges[i]){
					if(this.ranges[i] && index==(this.ranges[i][0]-1)) {
						this.ranges[i][0]--;			
					} else {
						if(index==(this.ranges[i][1]+1)){
							this.ranges[i][1]++;			
						} else {
							if(index<this.ranges[i][1]){
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
			while(i < this.ranges.length && index > this.ranges[i++][1]);
			i--;
			if(this.ranges[i]) {
				if(index==(this.ranges[i][1]) ) {
					if(index==(this.ranges[i][0])){
						this.ranges.splice(i, 1);
					} else {
						this.ranges[i][1]--;			
					}
				} else {
					if(index==(this.ranges[i][0])){
						this.ranges[i][0]++;			
					} else {
					this.ranges.splice(i+1, 0, [index+1, this.ranges[i][1]]);
					this.ranges[i][1] = index-1;
					}
				}
			}		
		},

		clear: function() {
			this.ranges = [];
		},
		
		contains: function(index) {
			var i = 0;
			while(i < this.ranges.length && index >= this.ranges[i][0]) {
				if(index >= this.ranges[i][0] && index <= this.ranges[i][1]) {
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

	richfaces.ExtendedDataTable = function(id, rowCount, ajaxFunction, options) {
		var WIDTH_CLASS_NAME_BASE = "rf-edt-c-";
		var MIN_WIDTH = 20;
		
		options = options || {};
		var ranges = new richfaces.utils.Ranges();
		var element = document.getElementById(id);
		var bodyElement, contentElement, spacerElement, dataTableElement, normalPartStyle, rows, rowHeight, parts, tbodies,
			shiftIndex, activeIndex, selectionFlag;
		var dragElement = document.getElementById(id + ":d");
		var reorderElement = document.getElementById(id + ":r");
		var reorderMarkerElement = document.getElementById(id + ":rm");
		var widthInput = document.getElementById(id + ":wi");
		var selectionInput = document.getElementById(id + ":si");
		var header = jQuery(element).children(".rf-edt-hdr");
		var resizerHolders = header.find(".rf-edt-rsz-cntr");
		
		var frozenHeaderPartElement = document.getElementById(id + ":frozenHeader");
		var frozenColumnCount = frozenHeaderPartElement ? frozenHeaderPartElement.firstChild.rows[0].cells.length : 0;//TODO Richfaces.firstDescendant;
		
		var scrollElement = document.getElementById(id + ":footer");
		
		var resizeData = {};
		var idOfReorderingColumn = "";
		var newWidths = {};
		
		var timeoutId = null;
		
		var sendAjax = function(event, map) {
			for (key in options.parameters) {
				if(!map[key]) {
					map[key] = options.parameters[key];
				}
			}
			ajaxFunction(event, map);
		};
		
		var updateLayout = function() {
			normalPartStyle.width = "auto";
			var offsetWidth = frozenHeaderPartElement ? frozenHeaderPartElement.offsetWidth : 0;
			var width = Math.max(0, element.clientWidth - offsetWidth);
			if (width) {
				if (parts.width() > width) {
					normalPartStyle.width = width + "px";
				}
				normalPartStyle.display = "block";
				scrollElement.style.overflowX = "";
				if (scrollElement.clientWidth < scrollElement.scrollWidth
						&& scrollElement.scrollHeight == scrollElement.offsetHeight) {
					scrollElement.style.overflowX = "scroll";
				}
				var delta = scrollElement.firstChild.offsetHeight - scrollElement.clientHeight;
				if (delta) {
					scrollElement.style.height = scrollElement.offsetHeight + delta;
				}
			} else {
				normalPartStyle.display = "none";
			}
			var height = element.clientHeight;
			var el = element.firstChild;
			while (el && (!el.nodeName || el.nodeName.toUpperCase() != "TABLE")) {
				if(el.nodeName && el.nodeName.toUpperCase() == "DIV" && el != bodyElement) {
					height -= el.offsetHeight;
				}
				el = el.nextSibling;
			}
			if (bodyElement.offsetHeight > height) {
				bodyElement.style.height = height + "px";
			}
		};
		
		var adjustResizers = function() {
			var scrollLeft = scrollElement ? scrollElement.scrollLeft : 0;
			var clientWidth = element.clientWidth - 3;
			var i = 0;
			for (; i < frozenColumnCount; i++) {
				if (clientWidth > 0) {
					resizerHolders[i].style.display = "none";
					resizerHolders[i].style.display = "";
					clientWidth -= resizerHolders[i].offsetWidth;
				}
				if (clientWidth <= 0) {
					resizerHolders[i].style.display = "none";
				}
			}
			scrollLeft -= 3;
			for (; i < resizerHolders.length; i++) {
				if (clientWidth > 0) {
					resizerHolders[i].style.display = "none";
					if (scrollLeft > 0) {
						resizerHolders[i].style.display = "";
						scrollLeft -= resizerHolders[i].offsetWidth;
						if (scrollLeft > 0) {
							resizerHolders[i].style.display = "none";
						} else {
							clientWidth += scrollLeft;
						}
					} else {
						resizerHolders[i].style.display = "";
						clientWidth -= resizerHolders[i].offsetWidth;
					}
				}
				if (clientWidth <= 0) {
					resizerHolders[i].style.display = "none";
				}
			}
		};

		var updateScrollPosition = function() {
			if (scrollElement) {
				var scrollLeft = scrollElement.scrollLeft;
				parts.each(function() {
					this.scrollLeft = scrollLeft;
				});
			}
			adjustResizers();
		};

		var initialize = function() {
			bodyElement = document.getElementById(id + ":b");
			bodyElement.tabIndex = -1; //TODO don't use tabIndex.
			normalPartStyle = richfaces.utils.getCSSRule("div.rf-edt-cnt").style;
			var bodyJQuery = jQuery(bodyElement);
			contentElement = bodyJQuery.children("div:first")[0];
			if (contentElement) {
				spacerElement = contentElement.firstChild;//TODO this.marginElement = Richfaces.firstDescendant(this.contentElement);
				dataTableElement = contentElement.lastChild;//TODO this.dataTableElement = Richfaces.lastDescendant(this.contentElement);
				tbodies = jQuery(document.getElementById(id + ":tbf")).add(document.getElementById(id + ":tbn"));
				rows = tbodies[0].rows.length;
				rowHeight = dataTableElement.offsetHeight / rows;
				if (rowCount != rows) {
					contentElement.style.height = (rowCount * rowHeight) + "px";
				}
				bodyJQuery.bind("scroll", bodyScrollListener);
				if (options.selectionMode != "none") {
					tbodies.bind("click", selectionClickListener);
					bodyJQuery.bind(window.opera ? "keypress" : "keydown", selectionKeyDownListener);
					initializeSelection();
				}
			} else {
				spacerElement = null;
				dataTableElement = null;
			}
			parts = jQuery(element).find(".rf-edt-cnt, .rf-edt-ftr-cnt");
			updateLayout();
			updateScrollPosition(); //TODO Restore horizontal scroll position
		};
		
		var drag = function(event) {
			jQuery(dragElement).setPosition({left:Math.max(resizeData.left + MIN_WIDTH, event.pageX)});
			return false;
		};
		
		var beginResize = function(event) {
			var id = this.parentNode.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
			resizeData = {
				id : id,
				left : jQuery(this).parent().offset().left
			};
			dragElement.style.height = element.offsetHeight + "px";
			jQuery(dragElement).setPosition({top:jQuery(element).offset().top, left:event.pageX});
			dragElement.style.display = "block";
			jQuery(document).bind("mousemove", drag);
			jQuery(document).one("mouseup", endResize);
			return false;
		};
		
		var setColumnWidth = function(id, width) {
			width = width + "px";
			richfaces.utils.getCSSRule("." + WIDTH_CLASS_NAME_BASE + id).style.width = width;
			newWidths[id] = width;
			var widthsArray = new Array();
			for (var id in newWidths) {
				widthsArray.push(id + ":" + newWidths[id]);
			}
			widthInput.value = widthsArray.toString();
			updateLayout();
			adjustResizers();
			sendAjax(); // TODO Maybe, event model should be used here.
		};

		var endResize = function(event) {
			jQuery(document).unbind("mousemove", drag);
			dragElement.style.display = "none";
			var width = Math.max(MIN_WIDTH, event.pageX - resizeData.left);
			setColumnWidth(resizeData.id, width);
		};

		var reorder = function(event) {
			jQuery(reorderElement).setPosition(event, {offset:[5,5]});
			reorderElement.style.display = "block";
			return false;
		};

		var beginReorder = function(event) {
			idOfReorderingColumn = this.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
			jQuery(document).bind("mousemove", reorder);
			header.find(".rf-edt-hdr-c").bind("mouseover", overReorder);
			jQuery(document).one("mouseup", cancelReorder);
			return false;
		};
		
		var overReorder = function(event) {
			if (idOfReorderingColumn != this.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1]) {
				var thisElement = jQuery(this);
				var offset = thisElement.offset();
				jQuery(reorderMarkerElement).setPosition({top:offset.top + thisElement.height(), left:offset.left - 5});
				reorderMarkerElement.style.display = "block";
				thisElement.one("mouseout", outReorder);
				thisElement.one("mouseup", endReorder);
			}
		};
		
		var outReorder = function(event) {
			reorderMarkerElement.style.display = "";
			jQuery(this).unbind("mouseup", endReorder);
		};
		
		var endReorder = function(event) {
			reorderMarkerElement.style.display = "";
			jQuery(this).unbind("mouseout", outReorder);
			var id = this.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
			var colunmsOrder = "";
			header.find(".rf-edt-hdr-c").each(function() {
				var i = this.className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
				if (i == id) {
					colunmsOrder += idOfReorderingColumn + "," + id + ",";
				} else if (i != idOfReorderingColumn) {
					colunmsOrder += i + ",";
				}
			});
			sendAjax(event, {"rich:columnsOrder" : colunmsOrder}); // TODO Maybe, event model should be used here.
		};
		
		var cancelReorder = function(event) {
			jQuery(document).unbind("mousemove", reorder);
			header.find(".rf-edt-hdr-c").unbind("mouseover", overReorder);
			reorderElement.style.display = "none";
		};
		
		var loadData = function(event) {
			var clientFirst = Math.round((bodyElement.scrollTop + bodyElement.clientHeight / 2) / (rowHeight) - rows / 2);
			if (clientFirst <= 0) {
				clientFirst = 0;
			} else {
				clientFirst = Math.min(rowCount - rows, clientFirst);
			}
			sendAjax(event, {"rich:clientFirst" : clientFirst});// TODO Maybe, event model should be used here.
		}

		var bodyScrollListener = function(event) {
			if(timeoutId) {
				window.clearTimeout(timeoutId);
				timeoutId = null;
			}
			if (Math.max(this.scrollTop - rowHeight, 0) < spacerElement.offsetHeight
						|| Math.min(this.scrollTop + rowHeight + this.clientHeight, this.scrollHeight) > spacerElement.offsetHeight + dataTableElement.offsetHeight) {
				timeoutId = window.setTimeout(function (event) {loadData(event)}, 1000);
			}
		};

		var showActiveRow = function() {
			if (bodyElement.scrollTop > activeIndex * rowHeight + spacerElement.offsetHeight) { //UP
				bodyElement.scrollTop = Math.max(bodyElement.scrollTop - rowHeight, 0);
			} else if (bodyElement.scrollTop + bodyElement.clientHeight
					< (activeIndex + 1) * rowHeight + spacerElement.offsetHeight) { //DOWN
				bodyElement.scrollTop = Math.min(bodyElement.scrollTop + rowHeight, bodyElement.scrollHeight - bodyElement.clientHeight);
			}
		}

		var selectRow = function(index) {
			ranges.add(index);
			for ( var i = 0; i < tbodies.length; i++) {
				jQuery(tbodies[i].rows[index]).addClass("rf-edt-r-sel");
			}
		}
		
		var deselectRow = function (index) {
			ranges.remove(index);
			for ( var i = 0; i < tbodies.length; i++) {
				jQuery(tbodies[i].rows[index]).removeClass("rf-edt-r-sel");
			}
		}

		var setActiveRow = function (index) {
			if(typeof activeIndex == "number") {
				for ( var i = 0; i < tbodies.length; i++) {
					jQuery(tbodies[i].rows[activeIndex]).removeClass("rf-edt-r-act");
				}
				
			}
			activeIndex = index;
			for ( var i = 0; i < tbodies.length; i++) {
				jQuery(tbodies[i].rows[activeIndex]).addClass("rf-edt-r-act");
			}
		}
		
		var resetShiftRow = function () {
			if(typeof shiftIndex == "number") {
				for ( var i = 0; i < tbodies.length; i++) {
					jQuery(tbodies[i].rows[shiftIndex]).removeClass("rf-edt-r-sht");
				}
				
			}
			shiftIndex = null;
		}
		
		var setShiftRow = function (index) {
			resetShiftRow();
			shiftIndex = index;
			if(typeof index == "number") {
				for ( var i = 0; i < tbodies.length; i++) {
					jQuery(tbodies[i].rows[shiftIndex]).addClass("rf-edt-r-sht");
				}
			}
		}
		
		var initializeSelection = function() {
			ranges.clear();
			var strings = selectionInput.value.split("|");
			activeIndex = strings[1] || null;
			shiftIndex = strings[2] || null;
			selectionFlag = null;
			var rows = tbodies[0].rows;
			for (var i = 0; i < rows.length; i++) {
				var row = jQuery(rows[i]);
				if (row.hasClass("rf-edt-r-sel")) {
					ranges.add(row[0].rowIndex)
				}
				if (row.hasClass("rf-edt-r-act")) {
					activeIndex = row[0].rowIndex;
				}
				if (row.hasClass("rf-edt-r-sht")) {
					shiftIndex = row[0].rowIndex;
				}
			}
			writeSelection();
		}

		var writeSelection = function() {
			selectionInput.value = [ranges, activeIndex, shiftIndex, selectionFlag].join("|");
		}
		
		var selectRows = function(range) {
			if (typeof range == "number") {
				range = [range, range];
			}
			var changed;
			var i = 0;
			for (; i < range[0]; i++) {
				if (ranges.contains(i)) {
					deselectRow(i);
					changed = true;
				}
			}
			for (; i <= range[1]; i++) {
				if (!ranges.contains(i)) {
					selectRow(i);
					changed = true;
				}
			}
			for (; i < rows; i++) {
				if (ranges.contains(i)) {
					deselectRow(i);
					changed = true;
				}
			}
			selectionFlag = typeof shiftIndex == "string" ? shiftIndex : "x";
			return changed;
		}
		
		var processSlectionWithShiftKey = function(index) {
			if(shiftIndex == null) {
				setShiftRow(activeIndex != null ? activeIndex : index);
			}
			var range;
			if ("u" == shiftIndex) {
				range = [0, index];
			} else if ("d" == shiftIndex) {
				range = [index, rows - 1];
			} else if (index >= shiftIndex) {
				range = [shiftIndex, index];
			} else {
				range = [index, shiftIndex];
			}
			return selectRows(range);			
		}
		
		var onbeforeselectionchange = function (event) {
			return !options.onbeforeselectionchange || options.onbeforeselectionchange.call(element, event) !== false;
		}
		
		var onselectionchange = function (event, index, changed) {
			if(!event.shiftKey) {
				resetShiftRow();
			}
			if (activeIndex != index) {
				setActiveRow(index);
				showActiveRow();
			}
			if (changed) {
				writeSelection();
				if (options.onselectionchange) {
					options.onselectionchange.call(element, event);
				}
			}
		}
		
		var selectionClickListener = function (event) {
			if (!onbeforeselectionchange(event)) {
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
			while (tbodies.index(tr.parentNode) == -1) {
				tr = tr.parentNode;
			}
			var index = tr.rowIndex;
			if (options.selectionMode == "single" || (options.selectionMode != "multipleKeyboardFree"
				&& !event.shiftKey && !event.ctrlKey)) {
				changed = selectRows(index);
			} else if (options.selectionMode == "multipleKeyboardFree" || (!event.shiftKey &&  event.ctrlKey)) {
				if (ranges.contains(index)) {
					deselectRow(index);
				} else {
					selectRow(index);
				}
				changed = true;
			} else {
				changed = processSlectionWithShiftKey(index);
			}
			onselectionchange(event, index, changed);
		}

		var selectionKeyDownListener = function(event) {
			if (event.ctrlKey && options.selectionMode != "single" && (event.keyCode == 65 || event.keyCode == 97) //Ctrl-A
					&& onbeforeselectionchange(event)) {
				selectRows([0, rows]);
				selectionFlag = "a";
				onselectionchange(event, activeIndex, true); //TODO Is there a way to know that selection haven't changed?
				event.preventDefault();
			} else {
				var index;
				if (event.keyCode == 38) { //UP
					index = -1;
				} else if (event.keyCode == 40) { //DOWN
					index = 1;
				}
				if (index != null && onbeforeselectionchange(event)) {
					if (typeof activeIndex == "number") {
						index += activeIndex;
						if (index >= 0 && index < rows ) {
							var changed;
							if (options.selectionMode == "single" || (!event.shiftKey && !event.ctrlKey)) {
								changed = selectRows(index);
							} else if (event.shiftKey) {
								changed = processSlectionWithShiftKey(index);
							}
							onselectionchange(event, index, changed);
						}
					}
					event.preventDefault();
				}
			}
		}

		var ajaxComplete = function (event, data) {
			if (data.reinitializeHeader) {
				bindHeaderHandlers();
			} else {
				selectionInput = document.getElementById(id + ":si");
				if (data.reinitializeBody) {
					rowCount = data.rowCount;
					initialize();
				} else if (options.selectionMode != "none") {
					initializeSelection();
				}
				if (spacerElement) {
					spacerElement.style.height = (data.first * rowHeight) + "px";
				}
			}
		};
		
		jQuery(document).ready(initialize);
		jQuery(window).bind("resize", updateLayout);
		jQuery(scrollElement).bind("scroll", updateScrollPosition);
		var bindHeaderHandlers = function () {
			header.find(".rf-edt-rsz").bind("mousedown", beginResize);
			header.find(".rf-edt-hdr-c").bind("mousedown", beginReorder);
		}
		bindHeaderHandlers();
		jQuery(element).bind("rich:onajaxcomplete", ajaxComplete);
		
		//JS API
		element["richfaces"] = element["richfaces"] || {}; // TODO ExtendedDataTable should extend richfaces.BaseComponent instead of using it.
		element.richfaces.component = this;
		this.destroy = function() {
			element.richfaces.component = null;
			jQuery(window).unbind("resize", updateLayout);			
		}
		
		this.getColumnPosition = function(id) {
			var position;
			var headers = header.find(".rf-edt-hdr-c");
			for (var i = 0; i < headers.length; i++) {
				if (id == headers[i].className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1]) {
					position = i;
				}
			}
			return position;
		}
		
		this.setColumnPosition = function(id, position) {
			var colunmsOrder = "";
			var before;
			var headers = header.find(".rf-edt-hdr-c");
			for (var i = 0; i < headers.length; i++) {
				var current = headers[i].className.match(new RegExp(WIDTH_CLASS_NAME_BASE + "([^\\W]*)"))[1];
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
			sendAjax(null, {"rich:columnsOrder" : colunmsOrder}); // TODO Maybe, event model should be used here.
		}
		
		this.setColumnWidth = function(id, width) {
			setColumnWidth(id, width);
		}
		
		this.filter = function(colunmId, filterValue, isClear) {
			if (typeof(filterValue) == "undefined" || filterValue == null) {
				filterValue = "";
			}
			var map = {}
			map[id + "rich:filtering"] = colunmId + ":" + filterValue + ":" + isClear;
			sendAjax(null, map); // TODO Maybe, event model should be used here.
		}
		
		this.clearFiltering = function() {
			this.filter("", "", true);
		}
		
		this.sort = function(colunmId, sortOrder, isClear) {
			if (typeof(sortOrder) == "string") {
				sortOrder = sortOrder.toUpperCase();
			}
			var map = {}
			map[id + "rich:sorting"] = colunmId + ":" + sortOrder + ":" + isClear;
			sendAjax(null, map); // TODO Maybe, event model should be used here.
		}
		
		this.clearSorting = function() {
			this.filter("", "", true);
		}
	};
}(window.RichFaces, jQuery));

