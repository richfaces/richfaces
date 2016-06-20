/*if(!window.LOG){
 window.LOG = {warn:function(){}};
 }*/

// TODO: try to change RichFaces.$ to $$ if possible

(function ($, rf) {

    rf.ui = rf.ui || {};

    //calendar templates
    var CalendarView = {
        getControl: function(text, attributes, functionName, paramsStr) {
            var attr = $.extend({
                    onclick: (functionName ? "RichFaces.$$('Calendar',this)." + functionName + "(" + (paramsStr ? paramsStr : "") + ");" : "") + "return true;"
                }, attributes);
            return new E('div', attr, [new T(text)]);
        },

        getSelectedDateControl: function(calendar) {

            if (!calendar.selectedDate || calendar.options.showApplyButton) return "";

            var text = rf.calendarUtils.formatDate(calendar.selectedDate, (calendar.timeType ? calendar.datePattern : calendar.options.datePattern), calendar.options.monthLabels, calendar.options.monthLabelsShort);
            var onclick = "RichFaces.$$('Calendar',this).showSelectedDate(); return true;"
            var markup = ( calendar.options.disabled ?
                new E('div', {'class': 'rf-cal-tl-btn-dis'}, [new ET(text)]) :
                new E('div', {'class': 'rf-cal-tl-btn', 'onclick': onclick}, [new ET(text)]) );

            return markup;
        },

        getTimeControl: function(calendar) {

            if (!calendar.selectedDate || !calendar.timeType) return "";

            var text = rf.calendarUtils.formatDate(calendar.selectedDate, calendar.timePattern, calendar.options.monthLabels, calendar.options.monthLabelsShort);

            var onmouseover = "RichFaces.jQuery(this).removeClass('rf-cal-btn-press');";
            var onmouseout = "RichFaces.jQuery(this).addClass('rf-cal-btn-press');";
            var onclick = "RichFaces.$$('Calendar',this).showTimeEditor();return true;";
            var markup = calendar.options.disabled || calendar.options.readonly ?
                new E('div', {'class': 'rf-cal-tl-btn-btn-dis'}, [new ET(text)]) :
                new E('div', {'class': 'rf-cal-tl-btn rf-cal-tl-btn-hov rf-cal-btn-press', 'onclick': onclick,
                        'onmouseover': + onmouseover ,
                        'onmouseout' : + onmouseout}, [new ET(text)]);

            return markup;
        },

        toolButtonAttributes: {className: "rf-cal-tl-btn", onmouseover:"this.className='rf-cal-tl-btn rf-cal-tl-btn-hov'", onmouseout:"this.className='rf-cal-tl-btn'", onmousedown:"this.className='rf-cal-tl-btn rf-cal-tl-btn-hov rf-cal-tl-btn-btn-press'", onmouseup:"this.className='rf-cal-tl-btn rf-cal-tl-btn-hov'"},
        nextYearControl: function (context) {
            return (!context.calendar.options.disabled ? CalendarView.getControl(">>", CalendarView.toolButtonAttributes, "nextYear") : "");
        },
        previousYearControl: function (context) {
            return (!context.calendar.options.disabled ? CalendarView.getControl("<<", CalendarView.toolButtonAttributes, "prevYear") : "");
        },
        nextMonthControl: function (context) {
            return (!context.calendar.options.disabled ? CalendarView.getControl(">", CalendarView.toolButtonAttributes, "nextMonth") : "");
        },
        previousMonthControl: function (context) {
            return (!context.calendar.options.disabled ? CalendarView.getControl("<", CalendarView.toolButtonAttributes, "prevMonth") : "");
        },
        currentMonthControl: function (context) {
            var text = rf.calendarUtils.formatDate(context.calendar.getCurrentDate(), "MMMM, yyyy", context.monthLabels, context.monthLabelsShort);
            var markup = context.calendar.options.disabled ?
                new E('div', {className: "rf-cal-tl-btn-dis"}, [new T(text)]) :
                CalendarView.getControl(text, CalendarView.toolButtonAttributes, "showDateEditor");
            return markup;
        },
        todayControl: function (context) {
            return (!context.calendar.options.disabled && context.calendar.options.todayControlMode != 'hidden' ? CalendarView.getControl(context.controlLabels.today, CalendarView.toolButtonAttributes, "today") : "");
        },
        closeControl: function (context) {
            return (context.calendar.options.popup ? CalendarView.getControl(context.controlLabels.close, CalendarView.toolButtonAttributes, "close", "false") : "");
        },
        applyControl: function (context) {
            return (!context.calendar.options.disabled && !context.calendar.options.readonly && context.calendar.options.showApplyButton ? CalendarView.getControl(context.controlLabels.apply, CalendarView.toolButtonAttributes, "close", "true") : "");
        },
        cleanControl: function (context) {
            return (!context.calendar.options.disabled && !context.calendar.options.readonly && context.calendar.selectedDate ? CalendarView.getControl(context.controlLabels.clean, CalendarView.toolButtonAttributes, "__resetSelectedDate") : "");
        },

        selectedDateControl: function (context) {
            return CalendarView.getSelectedDateControl(context.calendar);
        },
        timeControl: function (context) {
            return CalendarView.getTimeControl(context.calendar);
        },
        timeEditorFields: function (context) {
            return context.calendar.timePatternHtml;
        },

        header: [
            new E('table', {'border': '0', 'cellpadding': '0', 'cellspacing': '0', 'width': '100%'},
                [
                    new E('tbody', {},
                        [
                            new E('tr', {},
                                [
                                    new E('td', {'class': 'rf-cal-tl'},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("previousYearControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl'},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("previousMonthControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-hdr-month'},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("currentMonthControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl'},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("nextMonthControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl'},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("nextYearControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl rf-cal-btn-close', 'style':function(context) {
                                            return (this.isEmpty ? 'display:none;' : '');
                                        }},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("closeControl", context)
                                            })
                                        ])
                                ])
                        ])
                ]
            )],

        footer: [
            new E('table', {'border': '0', 'cellpadding': '0', 'cellspacing': '0', 'width': '100%'},
                [
                    new E('tbody', {},
                        [
                            new E('tr', {},
                                [
                                    new E('td', {'class': 'rf-cal-tl-ftr', 'style':function(context) {
                                            return (this.isEmpty ? 'display:none;' : '');
                                        }},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("selectedDateControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl-ftr', 'style':function(context) {
                                            return (this.isEmpty ? 'display:none;' : '');
                                        }},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("cleanControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl-ftr', 'style':function(context) {
                                            return (this.isEmpty ? 'display:none;' : '');
                                        }},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("timeControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl-ftr', 'style': 'background-image:none;', 'width': '100%'}, []),
                                    new E('td', {'class': 'rf-cal-tl-ftr', 'style':function(context) {
                                            return (this.isEmpty ? 'display:none;' : '') + (context.calendar.options.disabled || context.calendar.options.readonly || !context.calendar.options.showApplyButton ? 'background-image:none;' : '');
                                        }},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("todayControl", context)
                                            })
                                        ]),
                                    new E('td', {'class': 'rf-cal-tl-ftr', 'style':function(context) {
                                            return (this.isEmpty ? 'display:none;' : '') + 'background-image:none;';
                                        }},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("applyControl", context)
                                            })
                                        ])
                                ])
                        ])
                ]
            )],

        timeEditorLayout: [

            new E('table', {'id': function(context) {
                    return context.calendar.TIME_EDITOR_LAYOUT_ID
                }, 'border': '0', 'cellpadding': '0', 'cellspacing': '0', 'class': 'rf-cal-timepicker-cnt'},
                [
                    new E('tbody', {},
                        [
                            new E('tr', {},
                                [
                                    new E('td', {'class': 'rf-cal-timepicker-inp', 'colspan': '2', 'align': 'center'},
                                        [
                                            new ET(function (context) {
                                                return rf.calendarTemplates.evalMacro("timeEditorFields", context)
                                            })
                                        ])
                                ]),
                            new E('tr', {},
                                [
                                    new E('td', {'class': 'rf-cal-timepicker-ok'},
                                        [
                                            new E('div', {'id': function(context) {
                                                    return context.calendar.TIME_EDITOR_BUTTON_OK
                                                }, 'class': 'rf-cal-time-btn', 'style': 'float:right;', 'onmousedown': "RichFaces.jQuery(this).addClass('rf-cal-time-btn-press');", 'onmouseout': "RichFaces.jQuery(this).removeClass('rf-cal-time-btn-press');", 'onmouseup': "RichFaces.jQuery(this).removeClass('rf-cal-time-btn-press');", 'onclick': function(context) {
                                                    return "RichFaces.component('" + context.calendar.id + "').hideTimeEditor(true)";
                                                }},
                                                [
                                                    new E('span', {},
                                                        [
                                                            new ET(function (context) {
                                                                return context.controlLabels.ok;
                                                            })
                                                        ])
                                                ])
                                        ])
                                    ,
                                    new E('td', {'class': 'rf-cal-timepicker-cancel'},
                                        [
                                            new E('div', {'id': function(context) {
                                                    return context.calendar.TIME_EDITOR_BUTTON_CANCEL
                                                }, 'class': 'rf-cal-time-btn', 'style': 'float:left;', 'onmousedown': "RichFaces.jQuery(this).addClass('rf-cal-time-btn-press');", 'onmouseout': "RichFaces.jQuery(this).removeClass('rf-cal-time-btn-press');", 'onmouseup': "RichFaces.jQuery(this).removeClass('rf-cal-time-btn-press');", 'onclick': function(context) {
                                                    return "RichFaces.component('" + context.calendar.id + "').hideTimeEditor(false)";
                                                }},
                                                [
                                                    new E('span', {},
                                                        [
                                                            new ET(function (context) {
                                                                return context.controlLabels.cancel;
                                                            })
                                                        ])
                                                ])
                                        ])
                                ])
                        ])
                ]
            )],

        dayList: [new ET(function (context) {
            return context.day
        })],
        weekNumber: [new ET(function (context) {
            return context.weekNumber
        })],
        weekDay: [new ET(function (context) {
            return context.weekDayLabelShort
        })]
    };
    // calendar templates end

    // calendar context
    var CalendarContext = function(calendar) {
        this.calendar = calendar;
        this.monthLabels = calendar.options.monthLabels;
        this.monthLabelsShort = calendar.options.monthLabelsShort;
        this.weekDayLabelsShort = calendar.options.weekDayLabelsShort;
        this.controlLabels = calendar.options.labels;
    };

    $.extend(CalendarContext.prototype, {
            nextYearControl: CalendarView.nextYearControl,
            previousYearControl: CalendarView.previousYearControl,
            nextMonthControl: CalendarView.nextMonthControl,
            previousMonthControl: CalendarView.previousMonthControl,
            currentMonthControl: CalendarView.currentMonthControl,
            selectedDateControl: CalendarView.selectedDateControl,
            cleanControl: CalendarView.cleanControl,
            timeControl: CalendarView.timeControl,
            todayControl: CalendarView.todayControl,
            closeControl: CalendarView.closeControl,
            applyControl: CalendarView.applyControl,
            timeEditorFields: CalendarView.timeEditorFields
        });

    // must be :defaultTime, minDaysInFirstWeek, firstWeekday, weekDayLabelsShort, monthLabels, monthLabelsShort

    // defaults definition
    var defaultOptions = {
        showWeekDaysBar: true,
        showWeeksBar: true,
        datePattern: "MMM d, yyyy",
        horizontalOffset: 0,
        verticalOffset: 0,
        dayListMarkup: CalendarView.dayList,
        weekNumberMarkup: CalendarView.weekNumber,
        weekDayMarkup: CalendarView.weekDay,
        headerMarkup: CalendarView.header,
        footerMarkup: CalendarView.footer,
        isDayEnabled: function (context) {
            return true;
        },
        dayStyleClass: function (context) {
            return "";
        },
        showHeader: true,
        showFooter: true,
        direction: "AA",
        jointPoint: "AA",
        popup: true,
        boundaryDatesMode: "inactive",
        todayControlMode: "select",
        style: "",
        className: "",
        disabled: false,
        readonly: false,
        enableManualInput: false,
        showInput: true,
        resetTimeOnDateSelect: false,
        style: "z-index: 3;",
        showApplyButton: false,
        selectedDate: null,
        currentDate: null,
        defaultTime: {hours:12,minutes:0, seconds:0},
        mode: "client",
        hidePopupOnScroll: true,
        defaultLabel:""
    };

    var defaultLabels = {apply:'Apply', today:'Today', clean:'Clean', ok:'OK', cancel:'Cancel', close:'x'};

    var eventHandlerNames = ["change", "dateselect", "beforedateselect", "currentdateselect",
        "beforecurrentdateselect", "currentdateselect", "clean", "complete", "collapse",
        "datemouseout", "datemouseover", "show", "hide", "timeselect", "beforetimeselect"];

    var updateDefaultLabel = function (value) {
        var field = rf.getDomElement(this.INPUT_DATE_ID);
        if (
            (field.value == this.options.defaultLabel && !value) ||
                (value == this.options.defaultLabel && !field.value)
            ) {
            field.value = value;
            if (value) {
                $(field).addClass("rf-cal-dflt-lbl");
            } else {
                $(field).removeClass("rf-cal-dflt-lbl");
            }
        }
    }

    var onFocusBlur = function (event) {
        this.isFocused = event.type == "focus";
        if (!this.isFocused && this.isVisible) return;
        updateDefaultLabel.call(this, (event.type == "focus" ? "" : this.options.defaultLabel));
    }
    
    var keydownhandler = function(calendar) {
        
        return function (e) {
            var code;

            if (e.keyCode) {
                code = e.keyCode;
            } else if (e.which) {
                code = e.which;
            }
            
            if (code == rf.KEYS.TAB) {
                return true;
            }

            e.preventDefault();
            
            if (calendar.keydownDisabled) { // waiting for ajax request
                return;
            }

            var newDate = new Date(calendar.selectedDate || Date.now());
            
            var addDays = function (days) {
                newDate.setDate(newDate.getDate() + days);
            };

            switch (code) {
                case rf.KEYS.LEFT:
                    addDays(-1);
                    break;
                case rf.KEYS.RIGHT:
                    addDays(1);
                    break;
                case rf.KEYS.UP:
                    addDays(-7);
                    break;
                case rf.KEYS.DOWN:
                    addDays(7);
                    break;
                case rf.KEYS.PAGEUP:
                    if (e.shiftKey) {
                        newDate.setFullYear(newDate.getFullYear() - 1);
                        break;
                    }
                    newDate.setMonth(newDate.getMonth() - 1);
                    break;
                case rf.KEYS.PAGEDOWN:
                    if (e.shiftKey) {
                        newDate.setFullYear(newDate.getFullYear() + 1);
                        break;
                    }
                    newDate.setMonth(newDate.getMonth() + 1);
                    break;
                case 67: // C
                    calendar.__resetSelectedDate();
                    return false;
                case 84: // T
                    calendar.today();
                    return false;
                case 72: // H
                    calendar.showTimeEditor();
                    return false;
                case rf.KEYS.RETURN:
                    if (!calendar.__getDayCell(newDate).hasClass("rf-cal-dis")) {
                        calendar.close(true);
                    }
                    return false;
                case rf.KEYS.ESC:
                    calendar.close(false);
                    return false;
                default:
                    return false;
            }
            
            calendar.__selectDate(newDate);
            
            return false;
        }
    }

    // Constructor definition
    /**
     * Backing object for rich:calendar
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.Calendar
     * 
     * @param componentId {string} component id
     * @param locale {string} calendar locale
     * @param options {string}
     * @param markups
     */
    rf.ui.Calendar = function(componentId, locale, options, markups) {

        // dayListMarkup - day cell markup
        //		context: {day, date, weekNumber, weekDayNumber, isWeekend, isCurrentMonth,  elementId, component}
        // weekNumberMarkup - week number cell markup
        //		context: {weekNumber, elementId, component}
        // weekDayMarkup - week day cell markup
        //		context: {weekDayLabelShort, weekDayNumber, isWeekend, elementId, component}

        // headerMarkup
        // footerMarkup
        // optionalHeaderMarkup - user defined header (optional)
        // optionalFooterMarkup - user defined footer (optional)

        // currentDate - date to show month (day not used) (MM/yyyy)
        // selectedDate - selected date (mm/dd/yyyy)
        // weekDayLabelsShort - collection of week day short labels keyed by week day numbers
        // minDaysInFirstWeek - locale-specific constant defining number of days in the first week
        // firstWeekDay - (0..6) locale-specific constant defining number of the first week day
        // showWeekDaysBar - show WeekDays Bar [default value is true]
        // showWeeksBar - show Weeks numbers bar [default value is true]
        // showApplyButton
        // showHeader
        // showFooter

        // POPUP description
        // direction - [top-left, top-right, bottom-left, bottom-right, auto]
        // jointPoint - [top-left, top-right, bottom-left, bottom-right]
        // popup - true
        // id+PopupButton, id+InputDate,

        // boundaryDatesMode - boundary dates onclick action:
        // 						"inactive" or undefined - no action (default)
        //						"scroll" - change current month
        //						"select" - change current month and select date
        //						"hidden" - does not render content for boundary dates
        //
        // todayControlMode - today control onclick action:
        //						"scroll"
        //						"select"
        //						"hidden"

        // isDayEnabled - end-developer JS function
        // dayStyleClass - end-developer JS function that provide style class for day's cells.

        // dayCellClass - add div to day cell with class 'rf-cal-c-cnt' and add this class to TD if defined
        // style - table style
        // styleClass - table class

        // disabled
        // readonly

        //var _d = new Date();

        // call constructor of parent class
        $super.constructor.call(this, componentId);

        this.namespace = "." + rf.Event.createNamespace(this.name, componentId);

        //create parameters
        //this.options = $.extend(this.options, defaultOptions, options);
        this.options = $.extend(this.options, defaultOptions, locales[locale], options, markups);

        // labels
        this.options.labels = $.extend({}, defaultLabels, options.labels);

        this.popupOffset = [this.options.horizontalOffset, this.options.verticalOffset];

        //
        if (!this.options.popup) this.options.showApplyButton = false;

        //
        this.options.boundaryDatesMode = this.options.boundaryDatesMode.toLowerCase();
        this.hideBoundaryDatesContent = this.options.boundaryDatesMode == "hidden";
        this.options.todayControlMode = this.options.todayControlMode.toLowerCase();

        // time
        this.setTimeProperties();

        this.customDayListMarkup = (this.options.dayListMarkup != CalendarView.dayList);

        this.currentDate = this.options.currentDate ? this.options.currentDate : (this.options.selectedDate ? this.options.selectedDate : new Date());
        this.currentDate.setDate(1);
        this.selectedDate = this.options.selectedDate;

        this.todayDate = new Date();

        this.firstWeekendDayNumber = 6 - this.options.firstWeekDay;
        this.secondWeekendDayNumber = (this.options.firstWeekDay > 0 ? 7 - this.options.firstWeekDay : 0);

        this.calendarContext = new CalendarContext(this);

        // TODO: move it from constructor
        this.DATE_ELEMENT_ID = this.id + 'DayCell';
        this.WEEKNUMBER_BAR_ID = this.id + "WeekNum";
        this.WEEKNUMBER_ELEMENT_ID = this.WEEKNUMBER_BAR_ID + 'Cell';
        this.WEEKDAY_BAR_ID = this.id + "WeekDay";
        this.WEEKDAY_ELEMENT_ID = this.WEEKDAY_BAR_ID + 'Cell';
        this.POPUP_ID = this.id + 'Popup';
        this.POPUP_BUTTON_ID = this.id + 'PopupButton';
        this.INPUT_DATE_ID = this.id + 'InputDate';
        this.EDITOR_ID = this.id + 'Editor';
        this.EDITOR_SHADOW_ID = this.id + 'EditorShadow';

        this.TIME_EDITOR_LAYOUT_ID = this.id + 'TimeEditorLayout';
        this.DATE_EDITOR_LAYOUT_ID = this.id + 'DateEditorLayout';
        this.EDITOR_LAYOUT_SHADOW_ID = this.id + 'EditorLayoutShadow';
        this.TIME_EDITOR_BUTTON_OK = this.id + 'TimeEditorButtonOk';
        this.TIME_EDITOR_BUTTON_CANCEL = this.id + 'TimeEditorButtonCancel';
        this.DATE_EDITOR_BUTTON_OK = this.id + 'DateEditorButtonOk';
        this.DATE_EDITOR_BUTTON_CANCEL = this.id + 'DateEditorButtonCancel';
        this.CALENDAR_CONTENT = this.id + "Content";

        this.firstDateIndex = 0;

        this.daysData = {startDate:null, days:[]};
        this.days = [];
        this.todayCellId = null;
        this.todayCellColor = "";

        this.selectedDateCellId = null;
        this.selectedDateCellColor = "";

        var popupStyles = "";
        this.isVisible = true;
        if (this.options.popup == true) {
            // popup mode initialisation
            popupStyles = "display:none; position:absolute;"
            this.isVisible = false;
        }

        var tempStr = "RichFaces.component('" + this.id + "').";

        var htmlTextHeader = '<table id="' + this.CALENDAR_CONTENT + '" border="0" tabindex="-1" cellpadding="0" cellspacing="0" class="rf-cal-extr rf-cal-popup ' + this.options.styleClass + '" style="' + popupStyles + this.options.style + '" onmousedown="' + tempStr + 'skipEventOnCollapse=true;"><tbody>';
        var colspan = (this.options.showWeeksBar ? "8" : "7");
        var htmlHeaderOptional = (this.options.optionalHeaderMarkup) ? '<tr><td class="rf-cal-hdr-optnl" colspan="' + colspan + '" id="' + this.id + 'HeaderOptional"></td></tr>' : '';
        var htmlFooterOptional = (this.options.optionalFooterMarkup) ? '<tr><td class="rf-cal-ftr-optl" colspan="' + colspan + '" id="' + this.id + 'FooterOptional"></td></tr>' : '';
        var htmlControlsHeader = (this.options.showHeader ? '<tr><td class="rf-cal-hdr" colspan="' + colspan + '" id="' + this.id + 'Header"></td></tr>' : '');
        var htmlControlsFooter = (this.options.showFooter ? '<tr><td class="rf-cal-ftr" colspan="' + colspan + '" id="' + this.id + 'Footer"></td></tr>' : '');
        var htmlTextFooter = '</tbody></table>'

        // days bar creation
        var styleClass;
        var bottomStyleClass;
        var htmlTextWeekDayBar = [];
        var context;

        var eventsStr = this.options.disabled || this.options.readonly ? '' : 'onclick="' + tempStr + 'eventCellOnClick(event, this);" onmouseover="' + tempStr + 'eventCellOnMouseOver(event, this);" onmouseout="' + tempStr + 'eventCellOnMouseOut(event, this);"';
        if (this.options.showWeekDaysBar) {
            htmlTextWeekDayBar.push('<tr id="' + this.WEEKDAY_BAR_ID + '">');
            if (this.options.showWeeksBar) htmlTextWeekDayBar.push('<td class="rf-cal-day-lbl"><br/></td>');
            var weekDayCounter = this.options.firstWeekDay;
            for (var i = 0; i < 7; i++) {
                context = {weekDayLabelShort: this.options.weekDayLabelsShort[weekDayCounter], weekDayNumber:weekDayCounter, isWeekend:this.isWeekend(i), elementId:this.WEEKDAY_ELEMENT_ID + i, component:this};
                var weekDayHtml = this.evaluateMarkup(this.options.weekDayMarkup, context);
                if (weekDayCounter == 6) weekDayCounter = 0; else weekDayCounter++;

                styleClass = "rf-cal-day-lbl";
                if (context.isWeekend) {
                    styleClass += " rf-cal-holliday-lbl";
                }
                if (i == 6) styleClass += " rf-cal-right-c";
                htmlTextWeekDayBar.push('<td class="' + styleClass + '" id="' + context.elementId + '">' + weekDayHtml + '</td>');
            }
            htmlTextWeekDayBar.push('</tr>\n');
        }

        // week & weekNumber creation
        var htmlTextWeek = [];
        var p = 0;
        this.dayCellClassName = [];

        for (k = 1; k < 7; k++) {
            bottomStyleClass = (k == 6 ? "rf-btm-c " : "");
            htmlTextWeek.push('<tr id="' + this.WEEKNUMBER_BAR_ID + k + '">');
            if (this.options.showWeeksBar) {
                context = {weekNumber: k, elementId:this.WEEKNUMBER_ELEMENT_ID + k, component:this};
                var weekNumberHtml = this.evaluateMarkup(this.options.weekNumberMarkup, context);
                htmlTextWeek.push('<td class="rf-cal-week ' + bottomStyleClass + '" id="' + context.elementId + '">' + weekNumberHtml + '</td>');
            }

            // day cells creation
            for (var i = 0; i < 7; i++) {
                styleClass = bottomStyleClass + (!this.options.dayCellClass ? "rf-cal-c-cnt-overflow" : (!this.customDayListMarkup ? this.options.dayCellClass : "")) + " rf-cal-c";
                if (i == this.firstWeekendDayNumber || i == this.secondWeekendDayNumber) styleClass += " rf-cal-holiday";
                if (i == 6) styleClass += " rf-cal-right-c";

                this.dayCellClassName.push(styleClass);
                htmlTextWeek.push('<td class="' + styleClass + '" id="' + this.DATE_ELEMENT_ID + p + '" ' +
                    eventsStr +
                    '>' + (this.customDayListMarkup ? '<div class="rf-cal-c-cnt' + (this.options.dayCellClass ? ' ' + this.options.dayCellClass : '') + '"></div>' : '') + '</td>');
                p++;
            }
            htmlTextWeek.push('</tr>');
        }

        var div = rf.getDomElement(this.CALENDAR_CONTENT);
        div = $(div).replaceWith(htmlTextHeader + htmlHeaderOptional + htmlControlsHeader + htmlTextWeekDayBar.join('') + htmlTextWeek.join('') + htmlControlsFooter + htmlFooterOptional + htmlTextFooter);

        if (!this.options.popup) {
            rf.getDomElement(this.CALENDAR_CONTENT).title = document.getElementById(this.INPUT_DATE_ID).title;
        }

        this.attachToDom(); // TODO: optimize double $

        // memory leaks fix // from old 3.3.x code, may be not needed now
        div = null;

        // add onclick event handlers to input field and popup button
        if (this.options.popup && !this.options.disabled) {
            var handler = new Function('event', "RichFaces.component('" + this.id + "').switchPopup();");
            rf.Event.bindById(this.POPUP_BUTTON_ID, "click" + this.namespace, handler, this);
            if (!this.options.enableManualInput) {
                rf.Event.bindById(this.INPUT_DATE_ID, "focus" + this.namespace, this.showPopup, this);
            } else {
                var inputKeyDownHandler = function (event) {
                    var code;

                    if (event.keyCode) {
                        code = event.keyCode;
                    } else if (event.which) {
                        code = event.which;
                    }
                    
                    if (code == rf.KEYS.UP) {
                        handler();
                        event.preventDefault();
                    }
                }
                
                rf.Event.bindById(this.INPUT_DATE_ID, "keydown" + this.namespace, inputKeyDownHandler, this);
            }
            if (this.options.defaultLabel) {
                updateDefaultLabel.call(this, this.options.defaultLabel);
                rf.Event.bindById(this.INPUT_DATE_ID, "focus" + this.namespace + " blur" + this.namespace, onFocusBlur, this);
            }
        }

        this.scrollElements = null;

        //define isAjaxMode variable
        this.isAjaxMode = this.options.mode == "ajax";

        //alert(new Date().getTime()-_d.getTime());
        
        $(rf.getDomElement(this.CALENDAR_CONTENT)).on("keydown", keydownhandler(this));
    };

    // Extend component class and add protected methods from parent class to our container
    rf.BaseComponent.extend(rf.ui.Calendar);

    // define super class link
    var $super = rf.ui.Calendar.$super;

    // static methods definition
    var locales = {};

    rf.ui.Calendar.addLocale = function (locale, symbols) {
        if (!locales[locale]) {
            locales[locale] = symbols;
        }
    };

    /*
     * Prototype definition
     */
    $.extend(rf.ui.Calendar.prototype, {
            name: "Calendar",
            destroy: function() {
                if (this.options.popup && this.isVisible) {
                    this.scrollElements && rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
                    this.scrollElements = null;
                }
                $super.destroy.call(this);
            },
            __getDayCell: function(date) {
                return $(rf.getDomElement(this.DATE_ELEMENT_ID + (this.firstDateIndex + date.getDate() - 1)));
            },
            dateEditorSelectYear: function(value) {
                if (this.dateEditorYearID) {
                    $(rf.getDomElement(this.dateEditorYearID)).removeClass('rf-cal-edtr-btn-sel');
                }
                this.dateEditorYear = this.dateEditorStartYear + value;
                this.dateEditorYearID = this.DATE_EDITOR_LAYOUT_ID + 'Y' + value;
                $(rf.getDomElement(this.dateEditorYearID)).addClass('rf-cal-edtr-btn-sel');
            },

            dateEditorSelectMonth: function(value) {
                this.dateEditorMonth = value;
                $(rf.getDomElement(this.dateEditorMonthID)).removeClass('rf-cal-edtr-btn-sel');
                this.dateEditorMonthID = this.DATE_EDITOR_LAYOUT_ID + 'M' + value;
                $(rf.getDomElement(this.dateEditorMonthID)).addClass('rf-cal-edtr-btn-sel');
            },

            scrollEditorYear: function(value) {
                var element = rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID + 'TR');

                if (this.dateEditorYearID) {
                    $(rf.getDomElement(this.dateEditorYearID)).removeClass('rf-cal-edtr-btn-sel');
                    this.dateEditorYearID = '';
                }

                if (!value) {
                    // update month selection when open editor (value == 0)
                    if (this.dateEditorMonth != this.getCurrentMonth()) {
                        this.dateEditorMonth = this.getCurrentMonth();
                        $(rf.getDomElement(this.dateEditorMonthID)).removeClass('rf-cal-edtr-btn-sel');
                        this.dateEditorMonthID = this.DATE_EDITOR_LAYOUT_ID + 'M' + this.dateEditorMonth;
                        $(rf.getDomElement(this.dateEditorMonthID)).addClass('rf-cal-edtr-btn-sel');
                    }
                }

                if (element) {
                    var div;
                    var year = this.dateEditorStartYear = this.dateEditorStartYear + value * 10;
                    for (var i = 0; i < 5; i++) {
                        element = element.nextSibling;
                        div = element.firstChild.nextSibling.nextSibling;
                        div.firstChild.innerHTML = year;
                        if (year == this.dateEditorYear) {
                            $(div.firstChild).addClass('rf-cal-edtr-btn-sel');
                            this.dateEditorYearID = div.firstChild.id;
                        }
                        div = div.nextSibling;
                        div.firstChild.innerHTML = year + 5;
                        if (year + 5 == this.dateEditorYear) {
                            $(div.firstChild).addClass('rf-cal-edtr-btn-sel');
                            this.dateEditorYearID = div.firstChild.id;
                        }
                        year++;
                    }
                }
            },

            updateDateEditor: function() {
                this.dateEditorYear = this.getCurrentYear();
                this.dateEditorStartYear = this.getCurrentYear() - 4;
                this.scrollEditorYear(0);
            },

            updateTimeEditor: function() {
                var th = rf.getDomElement(this.id + 'TimeHours');
                var ts = rf.getDomElement(this.id + 'TimeSign');
                var tm = rf.getDomElement(this.id + 'TimeMinutes');

                var h = this.selectedDate.getHours();
                var m = this.selectedDate.getMinutes();
                if (this.timeType == 2) {
                    var a = (h < 12 ? 'AM' : 'PM');
                    ts.value = a;
                    h = (h == 0 ? '12' : (h > 12 ? h - 12 : h));
                }
                th.value = (this.timeHoursDigits == 2 && h < 10 ? '0' + h : h);
                tm.value = (m < 10 ? '0' + m : m);

                if (this.showSeconds) {
                    var tsec = rf.getDomElement(this.id + 'TimeSeconds');
                    var s = this.selectedDate.getSeconds();
                    tsec.value = (s < 10 ? '0' + s : s);
                }
            },


            createEditor: function() {
                var element = $(rf.getDomElement(this.CALENDAR_CONTENT));
                var zindex = parseInt(element.css('z-index'), 10);
                var htmlBegin = '<div id="' + this.EDITOR_SHADOW_ID + '" class="rf-cal-edtr-shdw" style="position:absolute; display:none;z-index:' + zindex + '"></div><table border="0" cellpadding="0" cellspacing="0" id="' + this.EDITOR_ID + '" style="position:absolute; display:none;z-index:' + (zindex + 1) + '"><tbody><tr><td class="rf-cal-edtr-cntr" align="center"><div style="position:relative; display:inline-block;">';
                var htmlContent = '<div id="' + this.EDITOR_LAYOUT_SHADOW_ID + '" class="rf-cal-edtr-layout-shdw"></div>';

                var htmlEnd = '</div></td></tr></tbody></table>';
                element.after(htmlBegin + htmlContent + htmlEnd);

                this.isEditorCreated = true;

                return rf.getDomElement(this.EDITOR_ID);
            },

            createTimeEditorLayout: function(editor) {
                $(rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID)).after(this.evaluateMarkup(CalendarView.timeEditorLayout, this.calendarContext));

                var th = rf.getDomElement(this.id + 'TimeHours');
                var ts;
                var tm = rf.getDomElement(this.id + 'TimeMinutes');
                if (this.timeType == 1) {
                    sbjQuery(th).SpinButton({digits:this.timeHoursDigits,min:0,max:23});
                }
                else {
                    sbjQuery(th).SpinButton({digits:this.timeHoursDigits,min:1,max:12});
                    ts = rf.getDomElement(this.id + 'TimeSign');
                    sbjQuery(ts).SpinButton({});
                }
                sbjQuery(tm).SpinButton({digits:2,min:0,max:59});
                if (this.showSeconds) {
                    var tsec = rf.getDomElement(this.id + 'TimeSeconds');
                    sbjQuery(tsec).SpinButton({digits:2,min:0,max:59});
                }

                this.correctEditorButtons(editor, this.TIME_EDITOR_BUTTON_OK, this.TIME_EDITOR_BUTTON_CANCEL);

                this.isTimeEditorLayoutCreated = true;
                
                var timeKeyDownHandler = function(calendar) {
                    return function(e) {
                        var code;
    
                        if (e.keyCode) {
                            code = e.keyCode;
                        } else if (e.which) {
                            code = e.which;
                        }
                        
                        switch(code) {
                            case rf.KEYS.TAB:
                                if ((e.target.id == calendar.id + 'TimeMinutes' && !calendar.showSeconds && !calendar.timeType == 2)  ||
                                        (e.target.id == calendar.id + 'TimeSeconds' && !calendar.timeType == 2) ||
                                        (e.target.id == calendar.id + 'TimeSign')) {
                                    e.preventDefault();
                                    rf.getDomElement(calendar.id + 'TimeHours').focus();
                                }
                                break;
                            case rf.KEYS.ESC:
                                calendar.hideTimeEditor(false);
                                break;
                            case rf.KEYS.RETURN:
                                calendar.hideTimeEditor(true);
                                return false;
                        }
                    }
                }
                
                $(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID)).on('keydown', timeKeyDownHandler(this));
            },

            correctEditorButtons: function(editor, buttonID1, buttonID2) {
                var button1 = rf.getDomElement(buttonID1);
                var button2 = rf.getDomElement(buttonID2);
                editor.style.visibility = "hidden";
                editor.style.display = "";
                var width1 = $(button1.firstChild).width();
                var width2 = $(button2.firstChild).width();
                editor.style.display = "none";
                editor.style.visibility = "";

                if (width1 != width2) {
                    button1.style.width = button2.style.width = (width1 > width2 ? width1 : width2) + "px";
                }
            },

            createDECell: function(id, value, buttonType, param, className) {
                if (buttonType == 0) {
                    return '<div id="' + id + '" class="rf-cal-edtr-btn' + (className ? ' ' + className : '') +
                        '" onmouseover="this.className=\'rf-cal-edtr-btn rf-cal-edtr-tl-over\';" onmouseout="this.className=\'rf-cal-edtr-btn\';" onmousedown="this.className=\'rf-cal-edtr-btn rf-cal-edtr-tl-press\';" onmouseup="this.className=\'rf-cal-edtr-btn rf-cal-edtr-tl-over\';" onclick="RichFaces.component(\'' + this.id + '\').scrollEditorYear(' + param + ');">' + value + '</div>';
                }
                else {
                    var onclick = (buttonType == 1 ? 'RichFaces.component(\'' + this.id + '\').dateEditorSelectMonth(' + param + ');' :
                        'RichFaces.component(\'' + this.id + '\').dateEditorSelectYear(' + param + ');' );
                    return '<div id="' + id + '" class="rf-cal-edtr-btn' + (className ? ' ' + className : '') +
                        '" onmouseover="RichFaces.jQuery(this).addClass(\'rf-cal-edtr-btn-over\');" onmouseout="$(this).removeClass(\'rf-cal-edtr-btn-over\');" onclick="' + onclick + '">' + value + '</div>';
                }
            },

            createDateEditorLayout: function(editor) {
                var htmlBegin = '<table id="' + this.DATE_EDITOR_LAYOUT_ID + '" class="rf-cal-monthpicker-cnt" border="0" cellpadding="0" cellspacing="0"><tbody><tr id="' + this.DATE_EDITOR_LAYOUT_ID + 'TR">';
                var htmlEnd = '</tr></tbody></table>';
                var month = 0;
                this.dateEditorYear = this.getCurrentYear();
                var year = this.dateEditorStartYear = this.dateEditorYear - 4;
                var htmlContent = '<td align="center">' + this.createDECell(this.DATE_EDITOR_LAYOUT_ID + 'M' + month, this.options.monthLabelsShort[month], 1, month) + '</td>'
                    + '<td align="center" class="rf-cal-monthpicker-split">' + this.createDECell(this.DATE_EDITOR_LAYOUT_ID + 'M' + (month + 6), this.options.monthLabelsShort[month + 6], 1, month + 6) + '</td>'
                    + '<td align="center">' + this.createDECell('', '&lt;', 0, -1) + '</td>'
                    + '<td align="center">' + this.createDECell('', '&gt;', 0, 1) + '</td>';
                month++;

                for (var i = 0; i < 5; i++) {
                    htmlContent += '</tr><tr><td align="center">' + this.createDECell(this.DATE_EDITOR_LAYOUT_ID + 'M' + month, this.options.monthLabelsShort[month], 1, month) + '</td>'
                        + '<td align="center" class="rf-cal-monthpicker-split">' + this.createDECell(this.DATE_EDITOR_LAYOUT_ID + 'M' + (month + 6), this.options.monthLabelsShort[month + 6], 1, month + 6) + '</td>'
                        + '<td align="center">' + this.createDECell(this.DATE_EDITOR_LAYOUT_ID + 'Y' + i, year, 2, i, (i == 4 ? 'rf-cal-edtr-btn-sel' : '')) + '</td>'
                        + '<td align="center">' + this.createDECell(this.DATE_EDITOR_LAYOUT_ID + 'Y' + (i + 5), year + 5, 2, i + 5) + '</td>';
                    month++;
                    year++;
                }
                this.dateEditorYearID = this.DATE_EDITOR_LAYOUT_ID + 'Y4';
                this.dateEditorMonth = this.getCurrentMonth();
                this.dateEditorMonthID = this.DATE_EDITOR_LAYOUT_ID + 'M' + this.dateEditorMonth;

                htmlContent += '</tr><tr><td colspan="2" class="rf-cal-monthpicker-ok">' +
                    '<div id="' + this.DATE_EDITOR_BUTTON_OK + '" class="rf-cal-time-btn" style="float:right;" onmousedown="RichFaces.jQuery(this).addClass(\'rf-cal-time-btn-press\');" onmouseout="RichFaces.jQuery(this).removeClass(\'rf-cal-time-btn-press\');" onmouseup="RichFaces.jQuery(this).removeClass(\'rf-cal-time-btn-press\');" onclick="RichFaces.component(\'' + this.id + '\').hideDateEditor(true);"><span>' + this.options.labels.ok + '</span></div>' +
                    '</td><td colspan="2" class="rf-cal-monthpicker-cancel">' +
                    '<div id="' + this.DATE_EDITOR_BUTTON_CANCEL + '" class="rf-cal-time-btn" style="float:left;" onmousedown="RichFaces.jQuery(this).addClass(\'rf-cal-time-btn-press\');" onmouseout="RichFaces.jQuery(this).removeClass(\'rf-cal-time-btn-press\');" onmouseup="RichFaces.jQuery(this).removeClass(\'rf-cal-time-btn-press\');" onclick="RichFaces.component(\'' + this.id + '\').hideDateEditor(false);"><span>' + this.options.labels.cancel + '</span></div>' +
                    '</td>';


                $(rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID)).after(htmlBegin + htmlContent + htmlEnd);

                $(rf.getDomElement(this.dateEditorMonthID)).addClass('rf-cal-edtr-btn-sel');

                this.correctEditorButtons(editor, this.DATE_EDITOR_BUTTON_OK, this.DATE_EDITOR_BUTTON_CANCEL);

                this.isDateEditorLayoutCreated = true;
            },

            createSpinnerTable: function(id) {
                return '<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>' +
                    '<td class="rf-cal-sp-inp-cntr">' +
                    '<input id="' + id + '" name="' + id + '" class="rf-cal-sp-inp" type="text" />' +
                    '</td>' +
                    '<td class="rf-cal-sp-btn">' +
                    '<table border="0" cellspacing="0" cellpadding="0"><tbody>' +
                    '<tr><td>' +
                    '<div id="' + id + 'BtnUp" class="rf-cal-sp-up"' +
                    ' onmousedown="this.className=\'rf-cal-sp-up rf-cal-sp-press\'"' +
                    ' onmouseup="this.className=\'rf-cal-sp-up\'"' +
                    ' onmouseout="this.className=\'rf-cal-sp-up\'"><span></span></div>' +
                    '</td></tr>' +
                    '<tr><td>' +
                    '<div id="' + id + 'BtnDown" class="rf-cal-sp-down"' +
                    ' onmousedown="this.className=\'rf-cal-sp-down rf-cal-sp-press\'"' +
                    ' onmouseup="this.className=\'rf-cal-sp-down\'"' +
                    ' onmouseout="this.className=\'rf-cal-sp-down\'"><span></span></div>' +
                    '</td></tr>' +
                    '</tbody></table>' +
                    '</td>' +
                    '</tr></tbody></table>';
            },

            setTimeProperties: function() {
                this.timeType = 0;

                var dateTimePattern = this.options.datePattern;
                var pattern = [];
                var re = /(\\\\|\\[yMdaHhms])|(y+|M+|d+|a|H{1,2}|h{1,2}|m{2}|s{2})/g;
                var r;
                while (r = re.exec(dateTimePattern))
                    if (!r[1])
                        pattern.push({str:r[0],marker:r[2],idx:r.index});

                var datePattern = "";
                var timePattern = "";

                var digits,h,hh,m,s,a;
                var id = this.id;

                var getString = function (p) {
                    return (p.length == 0 ? obj.marker : dateTimePattern.substring(pattern[i - 1].str.length + pattern[i - 1].idx, obj.idx + obj.str.length));
                };

                for (var i = 0; i < pattern.length; i++) {
                    var obj = pattern[i];
                    var ch = obj.marker.charAt(0);
                    if (ch == 'y' || ch == 'M' || ch == 'd') datePattern += getString(datePattern);
                    else if (ch == 'a') {
                        a = true;
                        timePattern += getString(timePattern);
                    }
                    else if (ch == 'H') {
                        h = true;
                        digits = obj.marker.length;
                        timePattern += getString(timePattern);
                    }
                    else if (ch == 'h') {
                        hh = true;
                        digits = obj.marker.length;
                        timePattern += getString(timePattern);
                    }
                    else if (ch == 'm') {
                        m = true;
                        timePattern += getString(timePattern);
                    }
                    else if (ch == 's') {
                        this.showSeconds = true;
                        timePattern += getString(timePattern);
                    }


                }
                this.datePattern = datePattern;
                this.timePattern = timePattern;

                var calendar = this;

                this.timePatternHtml = timePattern.replace(/(\\\\|\\[yMdaHhms])|(H{1,2}|h{1,2}|m{2}|s{2}|a)/g,
                    function($1, $2, $3) {
                        if ($2) return $2.charAt(1);
                        switch ($3) {
                            case 'a'  :
                                return '</td><td>' + calendar.createSpinnerTable(id + 'TimeSign') + '</td><td>';
                            case 'H'  :
                            case 'HH' :
                            case 'h'  :
                            case 'hh' :
                                return '</td><td>' + calendar.createSpinnerTable(id + 'TimeHours') + '</td><td>';
                            case 'mm' :
                                return '</td><td>' + calendar.createSpinnerTable(id + 'TimeMinutes') + '</td><td>';
                            case 'ss' :
                                return '</td><td>' + calendar.createSpinnerTable(id + 'TimeSeconds') + '</td><td>';
                        }
                    }
                );

                this.timePatternHtml = '<table border="0" cellpadding="0"><tbody><tr><td>' + this.timePatternHtml + '</td></tr></tbody></table>';

                if (m && h) {
                    this.timeType = 1;
                }
                else if (m && hh && a) {
                    this.timeType = 2;
                }
                this.timeHoursDigits = digits;
            },

            eventOnScroll: function (e) {
                this.hidePopup();
            },

            /**
            * Hide the popup
            * 
            * @method
            * @name RichFaces.ui.Calendar#hidePopup
            */
            hidePopup: function() {

                if (!this.options.popup || !this.isVisible) return;

                if (this.invokeEvent("hide", rf.getDomElement(this.id))) {
                    if (this.isEditorVisible) this.hideEditor();
                    this.scrollElements && rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
                    this.scrollElements = null;
                    rf.Event.unbindById(this.id, "focusout" + this.namespace);
                    if (!this.options.enableManualInput) {
                        rf.Event.bindById(this.INPUT_DATE_ID, "click" + this.namespace, this.showPopup, this);
                    }

                    $(rf.getDomElement(this.CALENDAR_CONTENT)).hide();
                    this.isVisible = false;
                    if (this.options.defaultLabel && !this.isFocused) {
                        updateDefaultLabel.call(this, this.options.defaultLabel);
                    }
                }
            },

            /**
             * Show the popup
             * 
             * @method
             * @name RichFaces.ui.Calendar#showPopup
             */
            showPopup: function(e) {
                if (!this.isRendered) {
                    this.isRendered = true;
                    this.render();
                }
                this.skipEventOnCollapse = false;
                if (e && e.type == 'click') this.skipEventOnCollapse = true;
                if (!this.options.popup || this.isVisible) return;

                var element = rf.getDomElement(this.id);

                if (this.invokeEvent("show", element, e)) {
                    var base = rf.getDomElement(this.POPUP_ID)
                    var baseInput = base.firstChild;
                    var baseButton = baseInput.nextSibling;

                    if (this.options.defaultLabel) {
                        if (!this.isFocused) updateDefaultLabel.call(this, "");
                    }
                    if (baseInput.value) {
                        this.__selectDate(baseInput.value, false, {event:e, element:element});
                    }

                    //rect calculation

                    if (this.options.showInput) {
                        base = base.children;
                    } else {
                        base = baseButton;
                    }
                    ;

                    $(rf.getDomElement(this.CALENDAR_CONTENT)).setPosition(base, {type:"DROPDOWN", from: this.options.jointPoint, to:this.options.direction, offset: this.popupOffset}).show();

                    this.isVisible = true;

                    this.scrollElements && rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
                    this.scrollElements = null;
                    if (this.options.hidePopupOnScroll) {
                        this.scrollElements = rf.Event.bindScrollEventHandlers(element, this.eventOnScroll, this);
                    }
                    
                    $(rf.getDomElement(this.CALENDAR_CONTENT)).focus();
                    rf.Event.bindById(this.id, "focusout" + this.namespace, this.eventOnCollapse, this);
                    rf.Event.unbindById(this.INPUT_DATE_ID, "click" + this.namespace);
                }
            },

            /**
             * Switch the state of the popup
             * 
             * @method
             * @name RichFaces.ui.Calendar#switchPopup
             */
            switchPopup: function(e) {
                this.isVisible ? this.hidePopup() : this.showPopup(e);
            },

            eventOnCollapse: function (e) {
                that = this;
                window.setTimeout(function() {
                if (that.skipEventOnCollapse) {
                    that.skipEventOnCollapse = false;
                    return true;
                }

                if (e.target.id == that.POPUP_BUTTON_ID || (!that.options.enableManualInput && e.target.id == that.INPUT_DATE_ID)) return true;

                that.hidePopup();

                return true;
                }, 200);
            },

            setInputField: function(dateStr, event) {
                var field = rf.getDomElement(this.INPUT_DATE_ID);
                if (field.value != dateStr) {
                    field.value = dateStr;
                    this.invokeEvent("change", rf.getDomElement(this.id), event, this.selectedDate);
                    $(rf.getDomElement(this.INPUT_DATE_ID)).blur();
                }
            },

            getCurrentDate: function() {
                return this.currentDate;
            },

            /**
             * Focus the input element
             * 
             * @method
             * @name RichFaces.ui.Calendar#focus
             */
            focus: function() {
                rf.getDomElement(this.INPUT_DATE_ID).focus();
            },

            __getSelectedDate: function() {
                if (!this.selectedDate) return null; else return this.selectedDate;
            },
            __getSelectedDateString: function(pattern) {
                if (!this.selectedDate) return "";
                if (!pattern) pattern = this.options.datePattern;
                return rf.calendarUtils.formatDate(this.selectedDate, pattern, this.options.monthLabels, this.options.monthLabelsShort);
            },

            getPrevYear: function() {
                var value = this.currentDate.getFullYear() - 1;
                if (value < 0) value = 0;
                return value;
            },
            getPrevMonth: function(asMonthLabel) {
                var value = this.currentDate.getMonth() - 1;
                if (value < 0) value = 11;
                if (asMonthLabel) {
                    return this.options.monthLabels[value];
                } else return value;
            },
            /**
             * Get the current year
             * 
             * @method
             * @name RichFaces.ui.Calendar#getCurrentYear
             * @return {int} current year
             */
            getCurrentYear: function() {
                return this.currentDate.getFullYear();
            },
            /**
             * Get the number of the current month
             * 
             * @method
             * @name RichFaces.ui.Calendar#getCurrentMonth
             * @return {int} number of current month, 0-based
             */
            getCurrentMonth: function(asMonthLabel) {
                var value = this.currentDate.getMonth();
                if (asMonthLabel) {
                    return this.options.monthLabels[value];
                } else return value;
            },
            getNextYear: function() {
                return this.currentDate.getFullYear() + 1;
            },
            getNextMonth: function(asMonthLabel) {
                var value = this.currentDate.getMonth() + 1;
                if (value > 11) value = 0;
                if (asMonthLabel) {
                    return this.options.monthLabels[value];
                } else return value;
            },

            isWeekend: function(weekday) {
                return (weekday == this.firstWeekendDayNumber || weekday == this.secondWeekendDayNumber);
            },

            setupTimeForDate: function (date) {
                var result = new Date(date);
                if (this.selectedDate && (!this.options.resetTimeOnDateSelect ||
                    (this.selectedDate.getFullYear() == date.getFullYear() &&
                        this.selectedDate.getMonth() == date.getMonth() &&
                        this.selectedDate.getDate() == date.getDate()))) {
                    result = rf.calendarUtils.createDate(date.getFullYear(), date.getMonth(), date.getDate(), this.selectedDate.getHours(), this.selectedDate.getMinutes(), this.selectedDate.getSeconds());
                } else {
                    result = rf.calendarUtils.createDate(date.getFullYear(), date.getMonth(), date.getDate(), this.options.defaultTime.hours, this.options.defaultTime.minutes, this.options.defaultTime.seconds);
                }
                return result;
            },

            eventCellOnClick: function (e, obj) {
                var daydata = this.days[parseInt(obj.id.substr(this.DATE_ELEMENT_ID.length), 10)];
                if (daydata.enabled && daydata._month == 0) {
                    var date = rf.calendarUtils.createDate(this.currentDate.getFullYear(), this.currentDate.getMonth(), daydata.day);
                    if (this.timeType) {
                        date = this.setupTimeForDate(date);
                    }

                    if (this.__selectDate(date, true, {event:e, element:obj}) && !this.options.showApplyButton) {
                        this.hidePopup();
                    }

                } else if (daydata._month != 0) {
                    if (this.options.boundaryDatesMode == "scroll")
                        if (daydata._month == -1) this.prevMonth(); else this.nextMonth();
                    else if (this.options.boundaryDatesMode == "select") {
                        var date = new Date(daydata.date);
                        if (this.timeType) {
                            date = this.setupTimeForDate(date);
                        }

                        if (this.__selectDate(date, false, {event:e, element:obj}) && !this.options.showApplyButton) {
                            this.hidePopup();
                        }
                    }
                }
            },

            eventCellOnMouseOver: function (e, obj) {
                var daydata = this.days[parseInt(obj.id.substr(this.DATE_ELEMENT_ID.length), 10)];
                if (this.invokeEvent("datemouseover", obj, e, daydata.date) && daydata.enabled) {
                    if (daydata._month == 0 && obj.id != this.selectedDateCellId && obj.id != this.todayCellId) {
                        $(obj).addClass('rf-cal-hov');
                    }
                }
            },

            eventCellOnMouseOut: function (e, obj) {
                var daydata = this.days[parseInt(obj.id.substr(this.DATE_ELEMENT_ID.length), 10)];
                if (this.invokeEvent("datemouseout", obj, e, daydata.date) && daydata.enabled) {
                    if (daydata._month == 0 && obj.id != this.selectedDateCellId && obj.id != this.todayCellId) {
                        $(obj).removeClass('rf-cal-hov');
                    }
                }
            },

            load:function(daysData, isAjaxMode) {
                //	startDate,
                //	daysData:array[]
                //	{
                //			day
                //			enabled boolean
                //			text1: 'Meeting...',
                //			text2: 'Meeting...'
                //			tooltip
                //			hasTooltip
                //			styleClass
                //	}


                if (daysData) {
                    this.daysData = this.indexData(daysData, isAjaxMode);
                } else {
                    this.daysData = null;
                }

                this.isRendered = false;
                if (this.isVisible) {
                    this.render();
                }
                ;

                if (typeof this.afterLoad == 'function') {
                    this.afterLoad();
                    this.afterLoad = null;
                }
                this.keydownDisabled = false;
            },

            indexData:function(daysData, isAjaxMode) {

                var dateYear = daysData.startDate.year;
                var dateMonth = daysData.startDate.month;
                daysData.startDate = new Date(dateYear, dateMonth)

                daysData.index = [];
                daysData.index[dateYear + '-' + dateMonth] = 0;
                if (isAjaxMode) {
                    this.currentDate = daysData.startDate;
                    this.currentDate.setDate(1);
                    return daysData;
                }
                var idx = rf.calendarUtils.daysInMonthByDate(daysData.startDate) - daysData.startDate.getDate() + 1;

                while (daysData.days[idx]) {
                    if (dateMonth == 11) {
                        dateYear++;
                        dateMonth = 0;
                    } else dateMonth++;
                    daysData.index[dateYear + '-' + dateMonth] = idx;
                    idx += (32 - new Date(dateYear, dateMonth, 32).getDate());
                }
                return daysData;
            },

            getCellBackgroundColor: function(element) {
                return $(element).css('background-color');
            },

            clearEffect: function (element_id, className, className1) {
                if (element_id) {
                    var e = $(rf.getDomElement(element_id)).stop(true, true);
                    if (className) e.removeClass(className);
                    if (className1) e.addClass(className1);
                }
                return null;
            },

            render:function() {
                //var _d=new Date();
                this.isRendered = true;
                this.todayDate = new Date();

                var currentYear = this.getCurrentYear();
                var currentMonth = this.getCurrentMonth();

                var todayflag = (currentYear == this.todayDate.getFullYear() && currentMonth == this.todayDate.getMonth());
                var todaydate = this.todayDate.getDate();

                var selectedflag = this.selectedDate && (currentYear == this.selectedDate.getFullYear() && currentMonth == this.selectedDate.getMonth())
                var selecteddate = this.selectedDate && this.selectedDate.getDate();

                var wd = rf.calendarUtils.getDay(this.currentDate, this.options.firstWeekDay);
                var currentMonthDays = rf.calendarUtils.daysInMonthByDate(this.currentDate);
                var previousMonthDays = rf.calendarUtils.daysInMonth(currentYear, currentMonth - 1);

                var p = 0;
                var month = -1;
                this.days = [];
                var dayCounter = previousMonthDays - wd + 1;

                // previuos month days
                if (wd > 0) while (dayCounter <= previousMonthDays) {
                    this.days.push({day:dayCounter, isWeekend: this.isWeekend(p), _month:month});
                    dayCounter++;
                    p++;
                }

                dayCounter = 1;
                month = 0;

                this.firstDateIndex = p;

                // current month days
                if (this.daysData && this.daysData.index[currentYear + '-' + currentMonth] != undefined) {
                    var idx = this.daysData.index[currentYear + '-' + currentMonth];
                    if (this.daysData.startDate.getFullYear() == currentYear && this.daysData.startDate.getMonth() == currentMonth) {
                        var firstDay = firstDay = (this.daysData.days[idx].day ? this.daysData.days[idx].day : this.daysData.startDate.getDate());
                        while (dayCounter < firstDay) {
                            this.days.push({day:dayCounter, isWeekend:this.isWeekend(p % 7), _month:month});

                            dayCounter++;
                            p++;
                        }
                    }

                    var len = this.daysData.days.length;
                    var obj;
                    var flag;
                    while (idx < len && dayCounter <= currentMonthDays) {
                        flag = this.isWeekend(p % 7);
                        obj = this.daysData.days[idx];
                        obj.day = dayCounter;
                        obj.isWeekend = flag;
                        obj._month = month;
                        this.days.push(obj);
                        idx++;
                        dayCounter++;
                        p++;
                    }
                }
                while (p < 42) {
                    if (dayCounter > currentMonthDays) {
                        dayCounter = 1;
                        month = 1;
                    }
                    this.days.push({day:dayCounter, isWeekend: this.isWeekend(p % 7), _month:month});
                    dayCounter++;
                    p++;
                }

                // render
                this.renderHF();

                //days render
                p = 0;
                var element;
                var dataobj;
                var wn;
                if (this.options.showWeeksBar) wn = rf.calendarUtils.weekNumber(currentYear, currentMonth, this.options.minDaysInFirstWeek, this.options.firstWeekDay); /// fix it
                this.selectedDayElement = null;
                var weekflag = true;

                var e;

                var boundaryDatesModeFlag = (this.options.boundaryDatesMode == "scroll" || this.options.boundaryDatesMode == "select");

                this.todayCellId = this.clearEffect(this.todayCellId);
                this.selectedDateCellId = this.clearEffect(this.selectedDateCellId);

                //var _d=new Date();
                var obj = rf.getDomElement(this.WEEKNUMBER_BAR_ID + "1");
                for (var k = 1; k < 7; k++) {
                    //
                    dataobj = this.days[p];

                    element = obj.firstChild;
                    var weeknumber;

                    // week number update
                    if (this.options.showWeeksBar) {
                        // TODO: fix:  there is no weekNumber in dataobj if showWeeksBar == false;
                        if (weekflag && currentMonth == 11 &&
                            (k == 5 || k == 6) &&
                            (dataobj._month == 1 || (7 - (currentMonthDays - dataobj.day + 1)) >= this.options.minDaysInFirstWeek)) {
                            wn = 1;
                            weekflag = false;
                        }
                        weeknumber = wn;
                        element.innerHTML = this.evaluateMarkup(this.options.weekNumberMarkup, {weekNumber: wn++, elementId:element.id, component:this});
                        if (k == 1 && wn > 52) wn = 1;
                        element = element.nextSibling;
                    }

                    var weekdaycounter = this.options.firstWeekDay;
                    var contentElement = null;

                    while (element) {
                        dataobj.elementId = element.id;
                        dataobj.date = new Date(currentYear, currentMonth + dataobj._month, dataobj.day);
                        dataobj.weekNumber = weeknumber;
                        dataobj.component = this;
                        dataobj.isCurrentMonth = (dataobj._month == 0);
                        dataobj.weekDayNumber = weekdaycounter;

                        // call user function to get day state
                        if (dataobj.enabled != false) dataobj.enabled = this.options.isDayEnabled(dataobj);
                        // call user function to custom class style
                        if (!dataobj.styleClass) dataobj.customStyleClass = this.options.dayStyleClass(dataobj);
                        else {
                            var styleclass = this.options.dayStyleClass(dataobj);
                            dataobj.customStyleClass = dataobj.styleClass;
                            if (styleclass) dataobj.customStyleClass += " " + styleclass;
                        }

                        contentElement = (this.customDayListMarkup ? element.firstChild : element);
                        contentElement.innerHTML = this.hideBoundaryDatesContent && dataobj._month != 0 ? "" : this.evaluateMarkup(this.options.dayListMarkup, dataobj);

                        if (weekdaycounter == 6) weekdaycounter = 0; else weekdaycounter++;

                        var classNames = this.dayCellClassName[p];

                        // class styles
                        if (dataobj._month != 0) {
                            classNames += ' rf-cal-boundary-day';
                            if (!this.options.disabled && !this.options.readonly && boundaryDatesModeFlag) {
                                classNames += ' rf-cal-btn';
                            }
                        }
                        else {
                            if (todayflag && dataobj.day == todaydate) {
                                this.todayCellId = element.id;
                                this.todayCellColor = this.getCellBackgroundColor(element);
                                classNames += " rf-cal-today";
                            }

                            if (selectedflag && dataobj.day == selecteddate) {
                                this.selectedDateCellId = element.id;
                                this.selectedDateCellColor = this.getCellBackgroundColor(element);
                                classNames += " rf-cal-sel";
                            }
                            else if (!this.options.disabled && !this.options.readonly && dataobj.enabled) classNames += ' rf-cal-btn';
                            
                            if (!dataobj.enabled) classNames += ' rf-cal-dis';

                            // add custom style class
                            if (dataobj.customStyleClass) {
                                classNames += ' ' + dataobj.customStyleClass;
                            }
                        }
                        element.className = classNames;

                        p++;

                        dataobj = this.days[p];
                        element = element.nextSibling;
                    }
                    obj = obj.nextSibling;
                }

                //alert(new Date().getTime()-_d.getTime());

            },

            renderHF: function() {
                if (this.options.showHeader) this.renderMarkup(this.options.headerMarkup, this.id + "Header", this.calendarContext);
                if (this.options.showFooter) this.renderMarkup(this.options.footerMarkup, this.id + "Footer", this.calendarContext);

                this.renderHeaderOptional();
                this.renderFooterOptional();
            },

            renderHeaderOptional: function() {
                this.renderMarkup(this.options.optionalHeaderMarkup, this.id + "HeaderOptional", this.calendarContext);
            },

            renderFooterOptional: function() {
                this.renderMarkup(this.options.optionalFooterMarkup, this.id + "FooterOptional", this.calendarContext);
            },

            renderMarkup: function (markup, elementId, context) {
                if (!markup) return;

                var e = rf.getDomElement(elementId);
                if (!e) return;

                e.innerHTML = this.evaluateMarkup(markup, context);
            },

            evaluateMarkup: function(markup, context) {
                if (!markup) return "";

                var result = [];
                var m;
                for (var i = 0; i < markup.length; i++) {
                    m = markup[i];
                    if (m['getContent']) {
                        result.push(m.getContent(context));
                    }
                }
                return result.join('');
            },

            onUpdate: function() {
                var formattedDate = rf.calendarUtils.formatDate(this.getCurrentDate(), "MM/yyyy");
                rf.getDomElement(this.id + 'InputCurrentDate').value = formattedDate;

                if (this.isAjaxMode && this.callAjax)
                    this.callAjax.call(this, formattedDate);
                else
                    this.render();
            },

            callAjax: function(calendar, date) {
                var _this = this;
                var ajaxSuccess = function (event) {
                    var dataDays = event && event.componentData && event.componentData[_this.id];
                    _this.load(dataDays, true);
                }
                var ajaxError = function (event) {
                    // do nothing
                }
                var params = {};
                params[this.id + ".ajax"] = "1";

                rf.ajax(this.id, null, {parameters: params, error: ajaxError, complete:ajaxSuccess});

            },

            nextMonth: function() {
                this.changeCurrentDateOffset(0, 1);
            },

            prevMonth: function() {
                this.changeCurrentDateOffset(0, -1);
            },

            nextYear: function() {
                this.changeCurrentDateOffset(1, 0);
            },

            prevYear: function() {
                this.changeCurrentDateOffset(-1, 0);
            },

            changeCurrentDate: function(year, month, noUpdate) {
                if (this.getCurrentMonth() != month || this.getCurrentYear() != year) {
                    var date = new Date(year, month, 1);
                    if (this.invokeEvent("beforecurrentdateselect", rf.getDomElement(this.id), null, date)) {
                        // fix for RF-2450.
                        // Additional event is fired: after the hidden input with current date
                        // value is updated in function onUpdate() and then
                        // the "currentdateselected" Event is fired.
                        this.currentDate = date;
                        if (noUpdate) this.render(); else this.onUpdate();
                        this.invokeEvent("currentdateselect", rf.getDomElement(this.id), null, date);
                        return true;
                    }
                }
                return false;
            },

            changeCurrentDateOffset: function(yearOffset, monthOffset) {
                var date = new Date(this.currentDate.getFullYear() + yearOffset, this.currentDate.getMonth() + monthOffset, 1);

                if (this.invokeEvent("beforecurrentdateselect", rf.getDomElement(this.id), null, date)) {
                    // fix for RF-2450.
                    // Additional event is fired: after the hidden input with current date
                    // value is updated in function onUpdate() and then
                    // the "currentdateselected" Event is fired.
                    this.currentDate = date;
                    this.onUpdate();
                    this.invokeEvent("currentdateselect", rf.getDomElement(this.id), null, date);
                }
            },

            /**
             * Select today's date
             * 
             * @method
             * @name RichFaces.ui.Calendar#today
             */
            today: function(noUpdate, noHighlight) {

                var now = new Date();

                var nowyear = now.getFullYear();
                var nowmonth = now.getMonth();
                var nowdate = now.getDate();
                var updateflag = false;

                if (nowdate != this.todayDate.getDate()) {
                    updateflag = true;
                    this.todayDate = now;
                }

                if (nowyear != this.currentDate.getFullYear() || nowmonth != this.currentDate.getMonth()) {
                    updateflag = true;
                    this.currentDate = new Date(nowyear, nowmonth, 1);
                }

                if (this.options.todayControlMode == 'select') {
                    noHighlight = true;
                }

                if (updateflag) {
                    if (noUpdate) this.render(); else this.onUpdate();
                }
                else {
                    // highlight today

                    if (this.isVisible && this.todayCellId && !noHighlight) {
                        this.clearEffect(this.todayCellId);
                        if (this.todayCellColor != "transparent") {
                            $(rf.getDomElement(this.todayCellId)).effect("highlight", {easing:'easeInOutSine', color: this.todayCellColor}, 300);
                        }
                    }
                }

                // todayControl select mode
                if (this.options.todayControlMode == 'select' && !this.options.disabled && !this.options.readonly)
                    if (updateflag && !noUpdate && this.submitFunction) {
                        this.afterLoad = this.selectToday;
                    }
                    else this.selectToday();

            },

            selectToday: function() {
                if (this.todayCellId) {
                    var daydata = this.days[parseInt(this.todayCellId.substr(this.DATE_ELEMENT_ID.length), 10)];
                    var today = new Date();
                    var date = new Date(today);
                    if (this.timeType) {
                        date = this.setupTimeForDate(date);
                    }
                    if (daydata.enabled && this.__selectDate(date, true) && !this.options.showApplyButton) {
                        this.hidePopup();
                    }
                }
            },

            __selectDate: function(date, noUpdate, eventData, applySelection) {

                if (!eventData) {
                    eventData = {event: null, element: null};
                }

                if (typeof applySelection === "undefined") {
                    applySelection = !this.options.showApplyButton
                }

                var oldSelectedDate = this.selectedDate;
                var newSelectedDate;
                if (date) {
                    if (typeof date == 'string') {
                        date = rf.calendarUtils.parseDate(date, this.options.datePattern, this.options.monthLabels, this.options.monthLabelsShort);
                    }
                    newSelectedDate = date;
                }
                else {
                    newSelectedDate = null;
                }

                if (newSelectedDate) {
                    var newCell = this.__getDayCell(newSelectedDate);

                    if (newSelectedDate.getMonth() == this.currentDate.getMonth() && newSelectedDate.getFullYear() == this.currentDate.getFullYear() && newCell.hasClass('rf-cal-dis')) { // do not apply date, just select
                        this.selectedDate = newSelectedDate;
                        this.clearEffect(this.selectedDateCellId, "rf-cal-sel", (this.options.disabled || this.options.readonly ? null : "rf-cal-btn"));
                        this.selectedDateCellId = newCell.attr('id');
                        this.selectedDateCellColor = this.getCellBackgroundColor(e);

                        newCell.removeClass("rf-cal-btn");
                        newCell.removeClass("rf-cal-hov");
                        newCell.addClass("rf-cal-sel");

                        this.renderHF();
                        
                        return false;
                    }
                }
                
                // fire user event
                var flag = true;
                var isDateChange = false;
                if ((oldSelectedDate - newSelectedDate) && (oldSelectedDate != null || newSelectedDate != null)) {
                    isDateChange = true;
                    flag = this.invokeEvent("beforedateselect", eventData.element, eventData.event, date);
                }

                if (flag) {
                    if (newSelectedDate != null) {
                        if (newSelectedDate.getMonth() == this.currentDate.getMonth() && newSelectedDate.getFullYear() == this.currentDate.getFullYear()) {
                            this.selectedDate = newSelectedDate;
                            if (!oldSelectedDate || (oldSelectedDate - this.selectedDate)) {
                                // find cell and change style class
                                var e = this.__getDayCell(this.selectedDate);

                                this.clearEffect(this.selectedDateCellId, "rf-cal-sel", (this.options.disabled || this.options.readonly ? null : "rf-cal-btn"));
                                this.selectedDateCellId = e.attr('id');
                                this.selectedDateCellColor = this.getCellBackgroundColor(e);

                                e.removeClass("rf-cal-btn");
                                e.removeClass("rf-cal-hov");
                                e.addClass("rf-cal-sel");

                                this.renderHF();
                            }
                            else if (this.timeType != 0) this.renderHF();
                        }
                        else {
                            //RF-5600
                            this.selectedDate = newSelectedDate;

                            // change currentDate and call this.onUpdate();
                            if (this.changeCurrentDate(newSelectedDate.getFullYear(), newSelectedDate.getMonth(), noUpdate)) {
                                //this.selectedDate = newSelectedDate;

                                this.afterLoad = function() {
                                    this.selectedDate = newSelectedDate;
                                    var newCell = this.__getDayCell(newSelectedDate);

                                    
                                        this.clearEffect(this.selectedDateCellId, "rf-cal-sel", (this.options.disabled || this.options.readonly ? null : "rf-cal-btn"));
                                        this.selectedDateCellId = newCell.attr('id');
                                        this.selectedDateCellColor = this.getCellBackgroundColor(e);

                                        newCell.removeClass("rf-cal-btn");
                                        newCell.removeClass("rf-cal-hov");
                                        newCell.addClass("rf-cal-sel");

                                        this.renderHF();

                                    if (newCell.hasClass('rf-cal-dis')) { // do not apply date, just select
                                        return;
                                    }

                                    this.invokeEvent("dateselect", eventData.element, eventData.event, this.selectedDate);
                                    if (applySelection === true) {
                                        this.setInputField(this.selectedDate != null ? this.__getSelectedDateString(this.options.datePattern) : "", eventData.event);
                                    }
                                }
                                
                                if (!this.isAjaxMode) {
                                    this.afterLoad();
                                    this.afterLoad = null;
                                } else {
                                    this.keydownDisabled = true;
                                    this.selectedDate = oldSelectedDate;
                                }
                            } else {
                                this.selectedDate = oldSelectedDate;
                            }
                            isDateChange = false;
                        }
                    }
                    else {
                        this.selectedDate = null;

                        this.clearEffect(this.selectedDateCellId, "rf-cal-sel", (this.options.disabled || this.options.readonly ? null : "rf-cal-btn"));

                        if (this.selectedDateCellId) {
                            this.selectedDateCellId = null;
                            this.renderHF();
                        }

                        var date = new Date();
                        if (this.currentDate.getMonth() == date.getMonth() && this.currentDate.getFullYear() == date.getFullYear()) {
                            this.renderHF();
                        }

                        var todayControlMode = this.options.todayControlMode;
                        this.options.todayControlMode = '';
                        this.today(noUpdate, true);
                        this.options.todayControlMode = todayControlMode;
                    }

                    // call user event
                    if (isDateChange) {
                        this.invokeEvent("dateselect", eventData.element, eventData.event, this.selectedDate);
                        if (applySelection === true) {
                            this.setInputField(this.selectedDate != null ? this.__getSelectedDateString(this.options.datePattern) : "", eventData.event);
                        }
                    }
                }

                return isDateChange;
            },

            __resetSelectedDate: function() {
                if (!this.selectedDate) return;
                if (this.invokeEvent("beforedateselect", null, null, null)) {
                    this.selectedDate = null;
                    this.invokeEvent("dateselect", null, null, null);

                    this.selectedDateCellId = this.clearEffect(this.selectedDateCellId, "rf-cal-sel", (this.options.disabled || this.options.readonly ? null : "rf-cal-btn"));
                    this.invokeEvent("clean", null, null, null);
                    this.renderHF();
                    if (!this.options.showApplyButton) {
                        this.setInputField("", null);
                        this.hidePopup();
                    }
                }
            },

            /**
             * Show the month containing the selected date
             * 
             * @method
             * @name RichFaces.ui.Calendar#showSelectedDate
             */
            showSelectedDate: function() {
                if (!this.selectedDate) return;
                if (this.currentDate.getMonth() != this.selectedDate.getMonth() || this.currentDate.getFullYear() != this.selectedDate.getFullYear()) {
                    this.currentDate = new Date(this.selectedDate);
                    this.currentDate.setDate(1);
                    this.onUpdate();
                }
                else {
                    // highlight Selected Date
                    if (this.isVisible && this.selectedDateCellId) {
                        this.clearEffect(this.selectedDateCellId);
                        if (this.selectedDateCellColor != "transparent") {
                            $(rf.getDomElement(this.selectedDateCellId)).effect("highlight", {easing:'easeInOutSine', color: this.selectedDateCellColor}, 300);

                        }
                    }
                }
            },

            close: function(updateDate) {
                if (updateDate) {
                    this.setInputField(this.__getSelectedDateString(this.options.datePattern), null);
                }
                this.hidePopup();
            },

            clonePosition: function (source, elements, offset) {
                var jqe = $(source);
                if (!elements.length) elements = [elements];
                offset = offset || {left:0,top:0};
                var width = jqe.outerWidth() + "px", height = jqe.outerHeight() + "px";
                var pos = jqe.position();
                var left = Math.floor(pos.left) + offset.left + "px", top = Math.floor(pos.top) + offset.top + "px";
                var element;
                for (var i = 0; i < elements.length; i++) {
                    element = elements[i];
                    element.style.width = width;
                    element.style.height = height;
                    element.style.left = left;
                    element.style.top = top;
                }
            },

            /**
             * Show the time editor popup
             * 
             * @method
             * @name RichFaces.ui.Calendar#showTimeEditor
             */
            showTimeEditor: function() {
                rf.Event.unbindById(this.id, "focusout" + this.namespace);
                var editor;
                if (this.timeType == 0) return;
                if (!this.isEditorCreated) editor = this.createEditor();
                else editor = rf.getDomElement(this.EDITOR_ID);
                if (!this.isTimeEditorLayoutCreated) this.createTimeEditorLayout(editor);

                $(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID)).show();

                var editor_shadow = rf.getDomElement(this.EDITOR_SHADOW_ID);

                this.clonePosition(rf.getDomElement(this.CALENDAR_CONTENT), [editor, editor_shadow]);

                this.updateTimeEditor();

                $(editor_shadow).show();

                $(editor).show();

                this.clonePosition(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID), rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID), {left: 3, top: 3});
                this.isEditorVisible = true;
                rf.getDomElement(this.id + 'TimeHours').focus();
            },

            hideEditor: function() {
                if (this.isTimeEditorLayoutCreated) $(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID)).hide();
                if (this.isDateEditorLayoutCreated) $(rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID)).hide();
                $(rf.getDomElement(this.EDITOR_ID)).hide();
                $(rf.getDomElement(this.EDITOR_SHADOW_ID)).hide();
                this.isEditorVisible = false;
            },

            /**
             * Hide the time editor popup
             * 
             * @method
             * @name RichFaces.ui.Calendar#hideTimeEditor
             */
            hideTimeEditor: function(updateTime) {
                this.hideEditor();
                if (updateTime && this.selectedDate) {
                    var s = this.showSeconds ? parseInt(rf.getDomElement(this.id + 'TimeSeconds').value, 10) : this.options.defaultTime.seconds;
                    var m = parseInt(rf.getDomElement(this.id + 'TimeMinutes').value, 10);
                    var h = parseInt(rf.getDomElement(this.id + 'TimeHours').value, 10);
                    if (this.timeType == 2) {
                        if (rf.getDomElement(this.id + 'TimeSign').value.toLowerCase() == "am") {
                            if (h == 12) h = 0;
                        }
                        else {
                            if (h != 12) h += 12;
                        }
                    }
                    var date = rf.calendarUtils.createDate(this.selectedDate.getFullYear(), this.selectedDate.getMonth(), this.selectedDate.getDate(), h, m, s);
                    if (date - this.selectedDate && this.invokeEvent("beforetimeselect", null, null, date)) {
                        this.selectedDate = date;
                        this.renderHF();
                        if (!this.options.popup || !this.options.showApplyButton) this.setInputField(this.__getSelectedDateString(this.options.datePattern), null);
                        this.invokeEvent("timeselect", null, null, this.selectedDate);
                    }
                }
                if (this.options.popup && !this.options.showApplyButton) this.close(false);
                this.skipEventOnCollapse = false;
                $(rf.getDomElement(this.CALENDAR_CONTENT)).focus();
                rf.Event.bindById(this.id, "focusout" + this.namespace, this.eventOnCollapse, this);
            },

            /**
             * Show the date editor popup
             * 
             * @method
             * @name RichFaces.ui.Calendar#showDateEditor
             */
            showDateEditor: function() {
                rf.Event.unbindById(this.id, "focusout" + this.namespace);
                var editor;
                if (!this.isEditorCreated) editor = this.createEditor();
                else editor = rf.getDomElement(this.EDITOR_ID);
                if (!this.isDateEditorLayoutCreated) this.createDateEditorLayout(editor);
                else this.updateDateEditor();

                $(rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID)).show();

                var editor_shadow = rf.getDomElement(this.EDITOR_SHADOW_ID);

                this.clonePosition(rf.getDomElement(this.CALENDAR_CONTENT), [editor, editor_shadow]);

                $(editor_shadow).show();
                $(editor).show();

                this.clonePosition(rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID), rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID), {left: 3, top: 3});

                this.isEditorVisible = true;
            },

            /**
             * Hide the date editor popup
             * 
             * @method
             * @name RichFaces.ui.Calendar#hideDateEditor
             */
            hideDateEditor: function(updateCurrentDate) {
                this.hideEditor();
                if (updateCurrentDate) {
                    this.changeCurrentDate(this.dateEditorYear, this.dateEditorMonth);
                }
                this.skipEventOnCollapse = false;
                $(rf.getDomElement(this.CALENDAR_CONTENT)).focus();
                rf.Event.bindById(this.id, "focusout" + this.namespace, this.eventOnCollapse, this);
            },

            /**
            * Get the current date value
            * 
            * @method
            * @name RichFaces.ui.Calendar#getValue
            * @return {date} current date value
            */
            getValue: function() {
                return this.__getSelectedDate();
            },

            /**
            * Get the current date value as string, formatted by the given pattern
            * 
            * @method
            * @name RichFaces.ui.Calendar#getValueAsString
            * @param [pattern] {string} date pattern
            * @return {string} current value
            */
            getValueAsString: function(pattern) {
                return this.__getSelectedDateString(pattern);
            },

            /**
            * Set new date value
            * 
            * @method
            * @name RichFaces.ui.AutocompleteBase#setValue
            * @param value {date} new date
            */
            setValue: function(value) {
                this.__selectDate(value, undefined, undefined, true);
            },

            /**
             * Clear the current date value
             * 
             * @method
             * @name RichFaces.ui.Calendar#resetValue
             */
            resetValue: function() {
                this.__resetSelectedDate();
                if (this.options.defaultLabel && !this.isFocused) {
                    updateDefaultLabel.call(this, this.options.defaultLabel);
                }
            },

            getNamespace: function () {
                return this.namespace;
            }
        });
})(RichFaces.jQuery, RichFaces);
