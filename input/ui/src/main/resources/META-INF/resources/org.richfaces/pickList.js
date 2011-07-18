function add(source, target) {
    var sourceId = RichFaces.escapeCSSMetachars(source);
    var targetId = RichFaces.escapeCSSMetachars(target);
    $('#' + sourceId + ' option:selected').remove().appendTo('#' + targetId);
}

function remove(source, target) {
    var sourceId = RichFaces.escapeCSSMetachars(source);
    var targetId = RichFaces.escapeCSSMetachars(target);
    $('#' + targetId + ' option:selected').remove().appendTo('#' + sourceId);
}

function addAll(source, target) {
    var sourceId = RichFaces.escapeCSSMetachars(source);
    var targetId = RichFaces.escapeCSSMetachars(target);
    $('#' + sourceId + ' option').remove().appendTo('#' + targetId);

}

function removeAll(source, target) {
    var sourceId = RichFaces.escapeCSSMetachars(source);
    var targetId = RichFaces.escapeCSSMetachars(target);
    $('#' + targetId + ' option').remove().appendTo('#' + sourceId);
}

$(document).ready( function () {
    $('form:has(.rf-pick-target)').submit(function() {
        $('.rf-pick-target option', $(this)).attr('selected', 'selected');
    });
});