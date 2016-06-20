(function($, rf) {

    rf.ui = rf.ui || {};

    var defaultOptions = {
        useNative : false
    };

    rf.ui.Focus = rf.BaseComponent.extendClass({

        name : "Focus",

        /**
         * Backing object for rich:focus
         * 
         * @extends RichFaces.BaseComponent
         * @memberOf! RichFaces.ui
         * @constructs RichFaces.ui.Focus
         * 
         * @param componentId
         * @param options
         */
        init : function(componentId, options) {
            $super.constructor.call(this, componentId);
            options = this.options = $.extend({}, defaultOptions, options);
            this.attachToDom(this.id);

            var focusInput = $(document.getElementById(componentId + 'InputFocus'));
            var focusCandidates = this.options.focusCandidates;

            $(document).on('focus', ':tabbable', function(e) {
                var target = $(e.target);
                if (!target.is(':editable')) {
                    return;
                }
                var ids = e.target.id || '';
                target.parents().each(function() {
                    var id = $(this).attr('id');
                    if (id) {
                        ids += ' ' + id;
                    }
                });
                focusInput.val(ids);
                rf.log.debug('Focus - clientId candidates for components: ' + ids);
            });

            if (this.options.mode === 'VIEW') {
                $(document).on('ajaxsubmit submit', 'form', function(e) {
                    var form = $(e.target);
                    var input = $("input[name='org.richfaces.focus']", form);
                    if (!input.length) {
                        input = $('<input name="org.richfaces.focus" type="hidden" />').appendTo(form);
                    }
                    input.val(focusInput.val());
                });
            }

            this.options.applyFocus = $.proxy(function() {
                var tabbables = $();

                if (focusCandidates) {
                    var candidates = focusCandidates;
                    rf.log.debug('Focus - focus candidates: ' + candidates);
                    candidates = candidates.split(' ');
                    $.each(candidates, function(i, v) {
                        var candidate = $(document.getElementById(v));
                        tabbables = tabbables.add($(":tabbable", candidate));

                        if (candidate.is(":tabbable")) {
                            tabbables = tabbables.add(candidate);
                        }
                    });

                    if (tabbables.length == 0) {
                        tabbables = $('form').has(focusInput).find(':tabbable')
                    }
                } else if (this.options.mode == 'VIEW') {
                    tabbables = $("body form:first :tabbable");
                }

                if (tabbables.length > 0) {
                    tabbables = tabbables.sort(sortTabindex);
                    tabbables.get(0).focus();
                }
            }, this);
        },

        applyFocus : function() {
            $(this.options.applyFocus);
        },

        // destructor definition
        destroy : function() {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });

    /**
     * Returns the tabindex sort order of two elements based on their tabindex and position in the DOM, following real tabbing
     * order implemented by browsers.
     * 
     * Returns negative number when element A has lesser tabindex than B or it is closer the start of the DOM; returns negative
     * number when element B has lesser tabindex than A or it is closer the start of the DOM; returns 0 if both A and B points
     * to same element.
     */
    var sortTabindex = function(a, b) {
        var result = sortTabindexNums($(a).attr('tabindex'), $(b).attr('tabindex'));

        return (result != 0) ? result : sortByDOMOrder(a, b);
    };

    /**
     * Sorts two tabindex values (positive number or undefined).
     * 
     * Returns negative number when tabindex A is lesser than B; returns positive number when tabindex B is lesser than A;
     * returns 0 if both A and B has same values.
     */
    var sortTabindexNums = function(a, b) {
        if (a) {
            if (b) {
                return a - b;
            } else {
                return -1;
            }
        } else {
            if (b) {
                return +1;
            } else {
                return 0;
            }
        }
    };

    /**
     * Detects absolute order of two elements in the DOM tree.
     * 
     * Returns negative number when element A is closer the start of the DOM; returns positive number when element B is closer
     * the start of the DOM; returns 0 if both A and B points to same element
     */
    var sortByDOMOrder = function(a, b) {
        var r = searchCommonParent(a, b);
        if (a == b) {
            return 0;
        } else if (r.parent == a) {
            return -1;
        } else if (r.parent == b) {
            return +1;
        } else {
            return $(r.aPrevious).index() - $(r.bPrevious).index();
        }
    };

    /**
     * Search for common parent for two given elements.
     * 
     * returns object containing following parameters:
     * 
     * result.parent - the commnon parent for A and B result.aPrevious - the parent's direct child which is on the branch
     * leading to A in DOM tree result.bPrevious - the parent's direct child which is on the branch leading to B in DOM tree
     */
    var searchCommonParent = function(a, b) {
        var aParents = $(a).add($(a).parents()).get().reverse();
        var bParents = $(b).add($(b).parents()).get().reverse();
        var r = {
            aPrevious : a,
            bPrevious : b
        };
        $.each(aParents, function(i, ap) {
            $.each(bParents, function(j, bp) {
                if (ap == bp) {
                    r.parent = ap;
                    return false;
                }
                r.bPrevious = bp;
            });
            if (r.parent) {
                return false;
            }
            r.aPrevious = ap;
        });
        if (!r.parent) {
            return null;
        }
        return r;
    };

    /**
     * Exposes sortTabindex family of functions for testing
     */
    rf.ui.Focus.__fn = {
        'sortTabindex' : sortTabindex,
        'sortTabindexNums' : sortTabindexNums,
        'searchCommonParent' : searchCommonParent,
        'sortByDOMOrder' : sortByDOMOrder
    }

    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Focus.$super;
})(RichFaces.jQuery, RichFaces);