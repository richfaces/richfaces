(function($, rf) {
    rf.ui = rf.ui || {};

    var defaultOptions = {
        mode : "server",
        cssRoot : "ddm",
        cssClasses : {}
    }

    // constructor definition
    /**
     * Backing object for rich:menuItem
     * 
     * @extends RichFaces.BaseComponent
     * @memberOf! RichFaces.ui
     * @constructs RichFaces.ui.MenuItem
     * 
     * @param componentId
     * @param options
     */
    rf.ui.MenuItem = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId);
        $.extend(this.options.cssClasses, buildCssClasses.call(this, this.options.cssRoot));
        this.attachToDom(componentId);
        this.element = $(rf.getDomElement(componentId));
        rf.Event.bindById(this.id, 'click', this.__clickHandler, this);
        rf.Event.bindById(this.id, 'mouseenter', this.select, this);
        rf.Event.bindById(this.id, 'mouseleave', this.unselect, this);
        this.selected = false;
    };

    var buildCssClasses = function(cssRoot) {
        var cssClasses = {
            itemCss : "rf-" +cssRoot+ "-itm",
            selectItemCss : "rf-" +cssRoot+ "-itm-sel",
            unselectItemCss : "rf-" +cssRoot+ "-itm-unsel",
            labelCss: "rf-" +cssRoot+ "-lbl"
        }
        return cssClasses;
    }

    rf.BaseComponent.extend(rf.ui.MenuItem);

    // define super class link
    var $super = rf.ui.MenuItem.$super;

    $.extend(rf.ui.MenuItem.prototype, (function() {

        return {
            name : "MenuItem",
            select : function() {
                this.element.removeClass(this.options.cssClasses.unselectItemCss);
                this.element.addClass(this.options.cssClasses.selectItemCss);
                this.selected = true;
            },
            unselect : function() {
                this.element.removeClass(this.options.cssClasses.selectItemCss);
                this.element.addClass(this.options.cssClasses.unselectItemCss);
                this.selected = false;
            },
            activate : function() {
                this.invokeEvent('click', rf.getDomElement(this.id));
            },

            isSelected : function() {
                return this.selected;
            },

            __clickHandler : function(e) {
                if ($(e.target).is(":input:not(:button):not(:reset):not(:submit)")) {
                    return;
                }

                var parentMenu = this.__getParentMenu();
                if (parentMenu) {
                    parentMenu.processItem(this.element);
                }
                
                var item = rf.getDomElement(this.id);
                var params = this.options.params;
                var form = this.__getParentForm(item);
                var itemId = {};
                itemId[item.id] = item.id;
                $.extend(itemId, params || {});
                e.form = form;
                e.itemId = itemId;
                this.options.onClickHandler.call(this, e);
                this.unselect();
            },

            __getParentForm : function(item) {
                var menu = this.__getParentMenu();
                return $(menu ? menu.element : this.element).parents("form").eq(0);
            },

            __getParentMenu : function() {
                var menu = $(this.element).parents('div[data-rf-parentmenu]').get(0);
                return menu ? rf.component(menu.getAttribute('data-rf-parentmenu')) : null;
            }
        };
    })());

})(RichFaces.jQuery, RichFaces);