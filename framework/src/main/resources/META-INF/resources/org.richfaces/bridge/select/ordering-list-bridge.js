(function($, RichFaces) {

  $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

    options : {
      clientId: null,
      hiddenInputSuffix: 'Input',
      widgetRootSuffix: 'List',
      height: null,
      heightMax: null,
      heightMin: null
    },

    _create : function() {
      $.rf.bridgeBase.prototype._create.call( this );
      var orderingListOptions = $.extend({}, this.options, {});

      if (orderingListOptions.buttonsText) {
          orderingListOptions.buttonsText = $.parseJSON(orderingListOptions.buttonsText);
      }
      this._addUnitsIfRequired(orderingListOptions, 'height');
      this._addUnitsIfRequired(orderingListOptions, 'heightMax');
      this._addUnitsIfRequired(orderingListOptions, 'heightMin');

      this.widgetRootElement = $(document.getElementById(this._getClientId() + this.options.widgetRootSuffix));
      this.widgetRootElement.orderingList(orderingListOptions);
      this._storeWidget(this.widgetRootElement.data('orderingList'));
      this._addDomElements();
      this._registerListeners();
    },

    _addDomElements: function() {
      var clientId = this._getClientId();
      var hiddenInputId = clientId + this.options.hiddenInputSuffix;
      this.hiddenInput = $('<input type="hidden" />').attr('id', hiddenInputId).attr('name', clientId);
      this.element.append(this.hiddenInput);
      var ui = this.widgetRootElement.data('orderingList')._uiHash();
      this._refreshInputValues(ui.orderedKeys);
    },

    _getClientId: function() {
      return (this.options.clientId) ? this.options.clientId : this.element.attr('id');
    },

    _registerListeners: function() {
      var bridge = this;
      this.widgetRootElement.on('orderinglistchange', function(event, ui) {
        bridge._refreshInputValues(ui.orderedKeys);
      });
    },

    _refreshInputValues: function(orderedKeys) {
      var csvKeys = orderedKeys.join(',');
      this.hiddenInput.val(csvKeys);
    }

  });

}(jQuery, RichFaces));