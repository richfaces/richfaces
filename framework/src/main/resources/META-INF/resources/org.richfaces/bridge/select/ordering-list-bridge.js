(function($, RichFaces) {

  $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

    options : {
      clientId: null,
      hiddenInputSuffix: 'Input',
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

      this.element.orderingList(orderingListOptions);
      var rootElement = $(document.getElementById(this._getClientId()));
      this._storeWidget(this.element.data('orderingList'), rootElement);
      this._addDomElements();
      this._registerListeners();
    },

    _addDomElements: function() {
      var clientId = this._getClientId();
      var hiddenInputId = clientId + this.options.hiddenInputSuffix;
      this.hiddenInput = $('<input type="hidden" />').attr('id', hiddenInputId).attr('name', clientId);
      this.element.parents(".select-list").first().append(this.hiddenInput);
      var ui = this.element.data('orderingList')._uiHash();
      this._refreshInputValues(ui.orderedKeys);
    },

    _getClientId: function() {
      return (this.options.clientId) ? this.options.clientId : this.element.attr('id');
    },

    _registerListeners: function() {
      var bridge = this;
      this.element.on('orderinglistchange', function(event, ui) {
        bridge._refreshInputValues(ui.orderedKeys);
      });
    },

    _refreshInputValues: function(orderedKeys) {
      var csvKeys = orderedKeys.join(',');
      this.hiddenInput.val(csvKeys);
    }

  });

}(jQuery, RichFaces));