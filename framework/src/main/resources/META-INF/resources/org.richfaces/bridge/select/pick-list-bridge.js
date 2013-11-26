(function($, RichFaces) {

  $.widget('rf.pickListBridge', $.rf.bridgeBase, {

    options: {
      hiddenInputSuffix: 'Input',
      height: null,
      heightMax: null,
      heightMin: null
    },

    _create : function() {
      $.rf.bridgeBase.prototype._create.call( this );
      var pickListOptions = $.extend({}, this.options, {});

      if (pickListOptions.pickButtonsText) {
          pickListOptions.pickButtonsText = $.parseJSON(pickListOptions.pickButtonsText);
      }
      if (pickListOptions.orderButtonsText) {
          pickListOptions.orderButtonsText = $.parseJSON(pickListOptions.orderButtonsText);
      }

      this._addUnitsIfRequired(pickListOptions, 'height');
      this._addUnitsIfRequired(pickListOptions, 'heightMax');
      this._addUnitsIfRequired(pickListOptions, 'heightMin');

      this.element.pickList(pickListOptions);
      this._storeWidget(this.element.data('pickList'));
      this._addDomElements();
      this._registerListeners();
    },

    _addDomElements: function() {
      var clientId = this.element.attr('id');
      var hiddenInputId = clientId + this.options.hiddenInputSuffix;
      this.hiddenInput = $('<input type="hidden" />').attr('id', hiddenInputId).attr('name', clientId);
      this.element.append(this.hiddenInput);
      var ui = this.element.data('pickList')._uiHash();
      this._refreshInputValues(ui.pickedKeys);
    },

    _registerListeners: function() {
      var bridge = this;
      this.element.on('picklistchange', function(event, ui) {
        bridge._refreshInputValues(ui.pickedKeys);
      });
      if (this.options.onremoveitems && typeof(this.options.onremoveitems) == "function") {
        this.element.on('picklistchange', function(event, ui) {
          if (ui.change === 'remove') {
            bridge.options.onremoveitems.call(this, event, ui);
          }
        });
      }
      if (this.options.onadditems && typeof(this.options.onadditems) == "function") {
        this.element.on('picklistchange', function(event, ui) {
          if (ui.change === 'add') {
            bridge.options.onadditems.call(this, event, ui);
          }
        });
      }
    },

    _refreshInputValues: function(pickedKeys) {
      var csvKeys = pickedKeys.join(',');
      this.hiddenInput.val(csvKeys);
    }

  });

}(jQuery, RichFaces));