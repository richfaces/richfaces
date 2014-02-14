(function($, rf) {

  $.widget('rf.editorBridge', $.rf.bridgeBase, {

    _create: function() {
      this._super();

      var clientId = this.element.attr('id');
      var textareaId = clientId + 'Input';

      $(document.getElementById(textareaId)).editor(this.options);
      this._storeWidget($(document.getElementById(textareaId)).data('editor'));
    }
  });

}(RichFaces.jQuery, RichFaces));