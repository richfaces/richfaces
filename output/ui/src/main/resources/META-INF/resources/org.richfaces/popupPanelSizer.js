(function ($, richfaces) {

    richfaces.ui = richfaces.ui || {};

    richfaces.ui.PopupPanel.Sizer = function(id, modalPanel, cursor, sizer) {

        $super.constructor.call(this, id);

    };

    var $super = richfaces.BaseComponent.extend(richfaces.ui.PopupPanel.Sizer);
    var $super = richfaces.ui.PopupPanel.Sizer.$super;
    $.extend(richfaces.ui.PopupPanel.Sizer.prototype, (function (options) {
        return {

            name: "richfaces.ui.PopupPanel.Sizer",

            doSetupSize: function (modalPanel, elt) {
                var width = 0;
                var height = 0;
                var element = $(richfaces.getDomElement(elt));
                var reductionData = modalPanel.reductionData;

                if (reductionData) {
                    if (reductionData.w) {
                        width = reductionData.w / 2;
                    }

                    if (reductionData.h) {
                        height = reductionData.h / 2;
                    }
                }

                if (width > 0) {
                    if (elt.clientWidth > width) {
                        if (!elt.reducedWidth) {
                            elt.reducedWidth = element.css('width');
                        }
                        element.css('width', width + 'px');
                    } else if (width < 4 && elt.reducedWidth == 4 + 'px') {
                        element.css('width', width + 'px');
                    }
                } else {
                    if (elt.reducedWidth) {
                        element.css('width', elt.reducedWidth);
                        elt.reducedWidth = undefined;
                    }
                }

                if (height > 0) {
                    if (elt.clientHeight > height) {
                        if (!elt.reducedHeight) {
                            elt.reducedHeight = element.css('height');
                        }
                        elt.style.height = height + 'px';
                    } else if (height < 4 && elt.reducedHeight == 4 + 'px') {
                        element.css('height', height + 'px');
                    }
                } else {
                    if (elt.reducedHeight) {
                        element.css('height', elt.reducedHeight);
                        elt.reducedHeight = undefined;
                    }
                }
            },

            doSetupPosition: function (modalPanel, elt, left, top) {
                var element = $(richfaces.getDomElement(elt));
                if (!isNaN(left) && !isNaN(top)) {
                    element.css('left', left + 'px');
                    element.css('top', top + 'px');
                }
            },

            doPosition: function (modalPanel, elt) {

            },

            doDiff: function (dx, dy) {

            }
        }

    })());
    richfaces.ui.PopupPanel.Sizer.Diff = function(dX, dY, dWidth, dHeight) {

        this.deltaX = dX;
        this.deltaY = dY;

        this.deltaWidth = dWidth;
        this.deltaHeight = dHeight;

    };

    richfaces.ui.PopupPanel.Sizer.Diff.EMPTY = function() {
        return new richfaces.ui.PopupPanel.Sizer.Diff(0, 0, 0, 0);
    },

        richfaces.ui.PopupPanel.Sizer.N = function() {

        }

    $.extend(richfaces.ui.PopupPanel.Sizer.N.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.N.prototype, {


            name: "richfaces.ui.PopupPanel.Sizer.N",

            doPosition : function (popupPanel, elt) {
                var element = $(richfaces.getDomElement(elt));
                element.css('width', popupPanel.width() + 'px');
                this.doSetupPosition(popupPanel, elt, 0, 0);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(0, dy, 0, -dy);
            }

        });

    richfaces.ui.PopupPanel.Sizer.NW = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.NW.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.NW.prototype, {

            name: "richfaces.ui.PopupPanel.Sizer.NW",

            doPosition : function (popupPanel, elt) {
                this.doSetupSize(popupPanel, elt);
                this.doSetupPosition(popupPanel, elt, 0, 0);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(dx, dy, -dx, -dy);
            }

        });

    richfaces.ui.PopupPanel.Sizer.NE = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.NE.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.NE.prototype, {

            name: "richfaces.ui.PopupPanel.Sizer.NE",

            doPosition : function (popupPanel, elt) {
                this.doSetupSize(popupPanel, elt);
                this.doSetupPosition(popupPanel, elt, popupPanel.width() - elt.clientWidth, 0);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(0, dy, dx, -dy);
            }

        });

    richfaces.ui.PopupPanel.Sizer.E = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.E.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.E.prototype, {

            name: "richfaces.ui.PopupPanel.Sizer.E",

            doPosition : function (popupPanel, elt) {
                var element = $(richfaces.getDomElement(elt));
                element.css('height', popupPanel.height() + 'px');
                this.doSetupPosition(popupPanel, elt, popupPanel.width() - elt.clientWidth, 0);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(0, 0, dx, 0);
            }

        });

    richfaces.ui.PopupPanel.Sizer.SE = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.SE.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.SE.prototype, {

            name: "richfaces.ui.PopupPanel.Sizer.SE",

            doPosition : function (popupPanel, elt) {
                this.doSetupSize(popupPanel, elt);
                this.doSetupPosition(popupPanel, elt, popupPanel.width() - elt.clientWidth,
                    popupPanel.height() - elt.clientHeight);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(0, 0, dx, dy);
            }

        });

    richfaces.ui.PopupPanel.Sizer.S = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.S.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.S.prototype, {

            name: "richfaces.ui.PopupPanel.Sizer.S",

            doPosition : function (popupPanel, elt) {
                var element = $(richfaces.getDomElement(elt));
                element.css('width', popupPanel.width() + 'px');
                this.doSetupPosition(popupPanel, elt, 0, popupPanel.height() - elt.clientHeight);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(0, 0, 0, dy);
            }

        });


    richfaces.ui.PopupPanel.Sizer.SW = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.SW.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.SW.prototype, {


            name: "richfaces.ui.PopupPanel.Sizer.SW",

            doPosition : function (popupPanel, elt) {
                this.doSetupSize(popupPanel, elt);
                this.doSetupPosition(popupPanel, elt, 0, popupPanel.height() - elt.clientHeight);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(dx, 0, -dx, dy);
            }

        });

    richfaces.ui.PopupPanel.Sizer.W = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.W.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.W.prototype, {


            name: "richfaces.ui.PopupPanel.Sizer.W",

            doPosition : function (popupPanel, elt) {
                var element = $(richfaces.getDomElement(elt));
                element.css('height', popupPanel.height() + 'px');
                this.doSetupPosition(popupPanel, elt, 0, 0);
            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(dx, 0, -dx, 0);
            }


        });


    richfaces.ui.PopupPanel.Sizer.Header = function() {

    }
    $.extend(richfaces.ui.PopupPanel.Sizer.Header.prototype, richfaces.ui.PopupPanel.Sizer.prototype);
    $.extend(richfaces.ui.PopupPanel.Sizer.Header.prototype, {


            name: "richfaces.ui.PopupPanel.Sizer.Header",

            doPosition : function (popupPanel, elt) {

            },

            doDiff : function(dx, dy) {
                return new richfaces.ui.PopupPanel.Sizer.Diff(dx, dy, 0, 0);
            }


        });


})(jQuery, window.RichFaces);