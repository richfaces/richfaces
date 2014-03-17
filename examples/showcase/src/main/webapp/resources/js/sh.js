function expandHighlighter() {
    jQuery('.show').each(function() {
        this.style.display = 'none'
    });
    jQuery('.source').slideDown();
    jQuery('.hide').fadeIn();
}
function collapseHighlighter() {
    jQuery('.hide').each(function() {
        this.style.display = 'none'
    });
    jQuery('.show').fadeIn();
    jQuery('.source').slideUp();
}