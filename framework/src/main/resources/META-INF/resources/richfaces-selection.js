(function (richfaces) {

    richfaces.Selection = richfaces.Selection || {};

    richfaces.Selection.set = function (field, start, end) {
        if (field.setSelectionRange) {
            field.focus();
            field.setSelectionRange(start, end);
        } else if (field.createTextRange) {
            var range = field.createTextRange();
            range.collapse(true);
            range.moveEnd('character', end);
            range.moveStart('character', start);
            range.select();
        }
    }

    richfaces.Selection.getStart = function(field) {
        if (field.setSelectionRange) {
            return field.selectionStart;
        } else if (document.selection && document.selection.createRange) {
            var r = document.selection.createRange().duplicate();
            r.moveEnd('character', field.value.length);
            if (r.text == '') return field.value.length;
            return field.value.lastIndexOf(r.text);
        }
    }

    richfaces.Selection.getEnd = function(field) {
        if (field.setSelectionRange) {
            return field.selectionEnd;
        } else if (document.selection && document.selection.createRange) {
            var r = document.selection.createRange().duplicate();
            r.moveStart('character', -field.value.length);
            return r.text.length;
        }
    }

    richfaces.Selection.setCaretTo = function (field, pos) {
        if (!pos) pos = field.value.length;
        richfaces.Selection.set(field, pos, pos);
    }
})(window.RichFaces || (window.RichFaces = {}));