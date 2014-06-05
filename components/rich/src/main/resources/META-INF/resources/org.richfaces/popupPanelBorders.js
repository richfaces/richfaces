(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.PopupPanel.Border = function(id, modalPanel, cursor, sizer) {

        $super.constructor.call(this, id);

        this.element = $(rf.getDomElement(id));
        this.element.css('cursor', cursor);
        var border = this;
        this.element.bind('mousedown', {border:border}, this.startDrag);

        this.modalPanel = modalPanel;
        this.sizer = sizer;
    };

    var $super = rf.BaseComponent.extend(rf.ui.PopupPanel.Border);
    var $super = rf.ui.PopupPanel.Border.$super;

    $.extend(rf.ui.PopupPanel.Border.prototype, (function (options) {

        return {

            name: "RichFaces.ui.PopupPanel.Border",

            destroy: function() {
                if (this.doingDrag) {
                    $(document).unbind('mousemove', this.doDrag);
                    $(document).unbind('mouseup', this.endDrag);
                }

                this.element.unbind('mousedown', this.startDrag);
                this.element = null;
                this.modalPanel = null;
            },

            show: function() {
                this.element.show();
            },

            hide: function() {
                this.element.hide();
            },

            startDrag: function(event) {
                var border = event.data.border;
                border.doingDrag = true;

                border.dragX = event.clientX;
                border.dragY = event.clientY;
                $(document).bind('mousemove', {border:border}, border.doDrag);
                $(document).bind('mouseup', {border:border}, border.endDrag);

                border.modalPanel.startDrag(border);

                border.onselectStartHandler = document.onselectstart;
                document.onselectstart = function() {
                    return false;
                }
            },

            getWindowSize : function() {
                var myWidth = 0, myHeight = 0;
                if (typeof( window.innerWidth ) == 'number') {
                    myWidth = window.innerWidth;
                    myHeight = window.innerHeight;
                } else if (document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight )) {
                    myWidth = document.documentElement.clientWidth;
                    myHeight = document.documentElement.clientHeight;
                } else if (document.body && ( document.body.clientWidth || document.body.clientHeight )) {
                    myWidth = document.body.clientWidth;
                    myHeight = document.body.clientHeight;
                }
                return {"width":myWidth,"height":myHeight};
            },

            doDrag: function(event) {
                var border = event.data.border;
                if (!border.doingDrag) {
                    return;
                }

                var evtX = event.clientX;
                var evtY = event.clientY;

                var winSize = border.getWindowSize();

                if (evtX < 0) {
                    evtX = 0;
                } else if (evtX >= winSize.width) {
                    evtX = winSize.width - 1;
                }

                if (evtY < 0) {
                    evtY = 0;
                } else if (evtY >= winSize.height) {
                    evtY = winSize.height - 1;
                }

                var dx = evtX - border.dragX;
                var dy = evtY - border.dragY;

                if (dx != 0 || dy != 0) {

                    var id = border.id;

                    var diff = border.sizer.prototype.doDiff(dx, dy);//TODO
                    var doResize;

                    var element = border.modalPanel.cdiv;

                    if (diff.deltaWidth || diff.deltaHeight) {
                        doResize = border.modalPanel.invokeEvent("resize", event, null, element);
                    } else if (diff.deltaX || diff.deltaY) {
                        doResize = border.modalPanel.invokeEvent("move", event, null, element);
                    }

                    var vetoes;

                    if (doResize) {
                        vetoes = border.modalPanel.doResizeOrMove(diff);
                    }

                    if (vetoes) {
                        if (!vetoes.x) {
                            border.dragX = evtX;
                        } else {
                            if (!diff.deltaX) {
                                border.dragX -= vetoes.vx || 0;
                            } else {
                                border.dragX += vetoes.vx || 0;
                            }
                        }

                        if (!vetoes.y) {
                            border.dragY = evtY;
                        } else {
                            if (!diff.deltaY) {
                                border.dragY -= vetoes.vy || 0;
                            } else {
                                border.dragY += vetoes.vy || 0;
                            }
                        }
                    }
                }
            },

            endDrag: function(event) {
                var border = event.data.border;
                border.doingDrag = undefined;

                $(document).unbind('mousemove', border.doDrag);
                $(document).unbind('mouseup', border.endDrag);

                border.modalPanel.endDrag(border);

                document.onselectstart = border.onselectStartHandler;
                border.onselectStartHandler = null;
            },

            doPosition: function() {
                this.sizer.prototype.doPosition(this.modalPanel, this.element);	//TODO remove prototype
            }
        }

    })());

})(RichFaces.jQuery, window.RichFaces);