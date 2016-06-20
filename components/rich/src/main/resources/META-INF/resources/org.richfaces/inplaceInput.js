(function ($, rf) {

    rf.ui = rf.ui || {};

    /**
     * Backing object for rich:inplaceInput
     * 
     * @extends RichFaces.ui.InplaceBase
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.InplaceInput
     * 
     * @param id
     * @param options
     */
    rf.ui.InplaceInput = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.label = $(document.getElementById(id + "Label"));
        var labelText = this.label.text();
        var inputLabel = this.__getValue();
        this.initialLabel = (labelText == inputLabel) ? labelText : "";
        this.useDefaultLabel = labelText != inputLabel
        this.saveOnBlur = mergedOptions.saveOnBlur;
        this.showControls = mergedOptions.showControls;
        this.getInput().bind("focus", $.proxy(this.__editHandler, this));
        if (this.showControls) {
            var btnContainer = document.getElementById(id + "Btn");
            if (btnContainer) {
                btnContainer.tabIndex = -1;
            }
            this.okbtn = $(document.getElementById(id + "Okbtn"));
            this.cancelbtn = $(document.getElementById(id + "Cancelbtn"));
            this.okbtn.bind("mousedown", $.proxy(this.__saveBtnHandler, this));
            this.cancelbtn.bind("mousedown", $.proxy(this.__cancelBtnHandler, this));
        }
    };

    rf.ui.InplaceBase.extend(rf.ui.InplaceInput);
    var $super = rf.ui.InplaceInput.$super;

    var defaultOptions = {
        defaultLabel: "",
        saveOnBlur: true,
        showControl: true,
        noneCss: "rf-ii-none",
        readyCss: "rf-ii",
        editCss: "rf-ii-act",
        changedCss: "rf-ii-chng"
    };

    $.extend(rf.ui.InplaceInput.prototype, ( function () {

        return {

            name : "inplaceInput",
            defaultLabelClass : "rf-ii-dflt-lbl",

            getName: function() {
                return this.name;
            },

            getNamespace: function() {
                return this.namespace;
            },

            __keydownHandler: function(e) {
                this.tabBlur = false;
                switch (e.keyCode || e.which) {
                    case rf.KEYS.ESC:
                        e.preventDefault();
                        this.cancel();
                        this.onblur(e);
                        break;
                    case rf.KEYS.RETURN:
                        e.preventDefault();
                        this.save();
                        this.onblur(e);
                        break;
                    case rf.KEYS.TAB:
                        this.tabBlur = true;
                        break;
                }
            },

            __blurHandler: function(e) {
                this.onblur(e);
            },

            __isSaveOnBlur: function() {
                return this.saveOnBlur;
            },

            __setInputFocus: function() {
                this.getInput().unbind("focus", this.__editHandler);
                this.getInput().focus();
            },

            __saveBtnHandler: function(e) {
                this.cancelButton = false;
                this.save();
                this.onblur(e);
            },

            __cancelBtnHandler: function(e) {
                this.cancelButton = true;
                this.cancel();
                this.onblur(e);
            },

            __editHandler: function(e) {
                $super.__editHandler.call(this, e);
                this.onfocus(e);
            },

            getLabel: function() {
                return this.label.text();
            },

            setLabel: function(value) {
                this.label.text(value);
                if (value == this.defaultLabel) {
                    this.label.addClass(this.defaultLabelClass);
                } else {
                    this.label.removeClass(this.defaultLabelClass);
                }
            },

            /**
             * Returns true if the value has changed from default
             * 
             * @method
             * @name RichFaces.ui.InplaceInput#isValueChanged
             * @return {boolean} true if the value has changed
             */
            isValueChanged: function () {
                return (this.__getValue() != this.initialLabel);
            },

            onshow: function() {
                this.__setInputFocus();
            },

            onhide: function() {
                if (this.tabBlur) {
                    this.tabBlur = false;
                } else {
                    this.getInput().focus();
                }
            },

            onfocus: function(e) {
                if (!this.__isFocused()) {
                    this.__setFocused(true);
                    this.focusValue = this.__getValue();
                    this.invokeEvent.call(this, "focus", document.getElementById(this.id), e);
                }
            },

            onblur: function(e) {
                if (this.__isFocused()) {
                    this.__setFocused(false);
                    this.invokeEvent.call(this, "blur", document.getElementById(this.id), e);

                    if (this.isValueSaved() || this.__isSaveOnBlur()) {
                        this.save();
                    } else {
                        this.cancel();
                    }

                    this.__hide();

                    if (!this.cancelButton) {
                        if (this.__isValueChanged()) {
                            this.invokeEvent.call(this, "change", document.getElementById(this.id), e);
                        }
                    }
                    var _this = this;
                    window.setTimeout(function() {
                        _this.getInput().bind("focus", $.proxy(_this.__editHandler, _this));
                    }, 1);
                }
            },

            __isValueChanged: function() {
                return (this.focusValue != this.__getValue());
            },

            __setFocused: function(focused) {
                this.focused = focused;
            },

            __isFocused: function() {
                return this.focused;
            },
            /**
             * Set new value
             * 
             * @method
             * @name RichFaces.ui.InplaceInput#setValue
             * @param value {string} new value
             */
            setValue: function(value) {
                this.__setValue(value);
                this.save();
            }
        }
    })());

})(RichFaces.jQuery, window.RichFaces);
