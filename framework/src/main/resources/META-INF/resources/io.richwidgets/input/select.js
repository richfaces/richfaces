/**
 * @module Input
 * @class select
 * @uses $.fn.ckeditor
 */
(function($) {

  $.widget('rich.select', {

    /* OPTIONS */

    options : {
    },

    /* LIFECYCLE METHODS */

    _create : function() {
      $(this.element).select2();
    },

    _destroy: function() {
      $(this.element).select2('destroy');
    }

    /* PUBLIC METHODS */

  });

}(jQuery));