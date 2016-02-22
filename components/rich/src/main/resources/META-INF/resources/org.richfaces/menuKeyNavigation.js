(function($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.MenuKeyNavigation = {

        __updateItemsList : function() {
            var items = $('.' + this.options.cssClasses.listContainerCss + ':first',
                this.popup.popup).find('>.' + this.options.cssClasses.itemCss).not(
                '.' + this.options.cssClasses.disabledItemCss);
            return (this.items = items);
        },

        __selectPrev : function() {
            if (-1 == this.currentSelectedItemIndex) {
                this.currentSelectedItemIndex = this.items.length - 1;
            } else {
                this.__deselectCurrentItem();
            }

            if (this.currentSelectedItemIndex > 0) {
                this.currentSelectedItemIndex--;
            } else {
                this.currentSelectedItemIndex = this.items.length - 1;
            }

            this.__selectCurrentItem();
        },

        __selectNext : function() {
            if (-1 != this.currentSelectedItemIndex) {
                this.__deselectCurrentItem();
            }
            if (this.currentSelectedItemIndex < this.items.length - 1) {
                this.currentSelectedItemIndex++;
            } else {
                this.currentSelectedItemIndex = 0;
            }

            this.__selectCurrentItem();
        },

        __deselectCurrentItem : function() {
            this.__deselectByIndex(this.currentSelectedItemIndex);
        },

        __selectCurrentItem : function() {
            this.__selectByIndex(this.currentSelectedItemIndex);
        },

        __selectFirstItem : function() {
            this.currentSelectedItemIndex = 0;
            this.__selectCurrentItem();
        },

        __selectByIndex : function(index) {
            if (-1 != index) {
                rf.component(this.items.eq(index)).select();
            }
        },

        __deselectByIndex : function(index) {
            if (index > -1) {
                rf.component(this.items.eq(index)).unselect();
            }

        },

        __openGroup : function() {
            var item = this.__getItemByIndex(this.currentSelectedItemIndex);
            if (this.__isGroup(item)) {
                rf.component(item).show();
                rf.component(item).__selectFirstItem();
                this.active = false;
            }
        },

        __closeGroup : function() {
            var item = this.__getItemByIndex(this.currentSelectedItemIndex);
            if (this.__isGroup(item)) {
                rf.component(item).__deselectCurrentItem();
                rf.component(item).hide();
                this.active = true;
            }
        },

        __returnToParentMenu : function() {
            var item = this.__getItemByIndex(this.currentSelectedItemIndex);
            var menu;
            menu = this.__getParentMenu();
            if (menu != null && this.id != menu.id) {
                this.hide();
                menu.popupElement.focus();
            } else {
                this.hide();
            }
        },

        __activateMenuItem : function() {
            var item = this.__getCurrentItem();
            if (item) {
                menuItemId = item.attr('id');
                this.activateItem(menuItemId);
            }
        },

        __getItemByIndex : function(index) {
            if (index > -1) {
                return this.items.eq(index);
            } else {
                return null;
            }
        },

        __getCurrentItem : function() {
            return this.__getItemByIndex(this.currentSelectedItemIndex);
        },

        __keydownHandler : function(e) {
            var code;

            if (e.keyCode) {
                code = e.keyCode;
            } else if (e.which) {
                code = e.which;
            }

            activeMenu = rf.ui.MenuManager.getActiveSubMenu();

            if (this.popup.isVisible()) {
                switch (code) {
                    case rf.KEYS.DOWN:
                        e.preventDefault();
                        activeMenu.__selectNext();
                        //this.__setInputFocus();
                        break;

                    case rf.KEYS.UP:
                        e.preventDefault();
                        activeMenu.__selectPrev();
                        //this.__setInputFocus();
                        break;
                    case rf.KEYS.LEFT:
                        e.preventDefault();
                        activeMenu.__returnToParentMenu();
                        break;

                    case rf.KEYS.RIGHT:
                        e.preventDefault();
                        activeMenu.__openGroup();
                        break;

                    case rf.KEYS.ESC:
                        e.preventDefault();
                        activeMenu.__returnToParentMenu();
                        break;

                    case rf.KEYS.RETURN:
                        e.preventDefault();
                        activeMenu.__activateMenuItem();
                        //this.__setInputFocus();
                        //return false;
                        break;
                }
                e.stopPropagation();
            }
        }
    }
})(RichFaces.jQuery, RichFaces);