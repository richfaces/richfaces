window.statusChangeObserver = function () {
    MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

    var ITEM_NAME = 'records';
    var ITEMS_SEPARATOR = ';;';
    var REGEXP_VISIBLE = /<visible>/;

    function addToSessionStorage(newRecord) {
        if (!!newRecord) {// do not insert empty record
            var newIsVisible = REGEXP_VISIBLE.test(newRecord);
            var recordsBefore = sessionStorage.getItem(ITEM_NAME);
            if (!!recordsBefore) {
                var recordsArray = recordsBefore.split(ITEMS_SEPARATOR);
                var lastIsVisible = REGEXP_VISIBLE.test(recordsArray[recordsArray.length - 1]);
                // insert only image visibility changed state
                if (lastIsVisible ? !newIsVisible : newIsVisible) {// XOR
                    sessionStorage.setItem(ITEM_NAME, recordsBefore + ITEMS_SEPARATOR + newRecord);
                }
            } else {
                if (newIsVisible) {// insert only image is visible state as first record
                    sessionStorage.setItem(ITEM_NAME, newRecord);
                }
            }
        }
    }

    function cleanRecordsInSessionStorage() {
        sessionStorage.removeItem(ITEM_NAME);
    }

    function isStatusImageVisible(statusElement) {
        return $(statusElement).find('img').is(':visible');
    }

    function generateStatusImageVisibleMessage(statusElement) {
        return 'at <' + new Date().getTime() + '> status image was <'
                + (isStatusImageVisible(statusElement) === true ? 'visible>' : 'not visible>');
    }

    function watchForStatusChangeUsingMutationObserver(statusElement) {
        // create observer
        var observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                addToSessionStorage(generateStatusImageVisibleMessage(mutation.target));
            });
        });
        // create target and configuration
        var config = {
            attributes: true
        };
        // connect observer
        observer.observe(statusElement, config);
    }

    function watchForStatusChangeByRepeatedlyInvokingCheckFunction(statusElement) {
        function repeatableAction(check, repeats, delay) {
            var iteration = 0;
            var intervalID = window.setInterval(function () {
                check();
                if (++iteration === repeats) {
                    window.clearInterval(intervalID);
                }
            }, delay);
        }

        function check() {
            addToSessionStorage(generateStatusImageVisibleMessage(statusElement));
        }
        var repeats = 70;
        var delay = 100;
        repeatableAction(check, repeats, delay);
    }
    return{
        getRecords: function () {
            return sessionStorage.getItem(ITEM_NAME).split(ITEMS_SEPARATOR);
        },
        watchForChangeOfStatus: function (statusIndex) {
            cleanRecordsInSessionStorage();
            var statusElement = $('.rf-st-start').get(statusIndex);
            if (!!MutationObserver) {
                watchForStatusChangeUsingMutationObserver(statusElement);
            } else {// MutationObserver does not work on PhantomJS 1.x
                watchForStatusChangeByRepeatedlyInvokingCheckFunction(statusElement);
            }
        }
    };
}();