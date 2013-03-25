function selectPopularTag(tag, target) {
    // console.log(target);
    // console.log(target.value);
    if (target) {
        var value = target.getValue().trim();
        if (value.indexOf(tag) == -1) {
            target.setValue(value.length != 0 ? value + ", " + tag : tag);
        }
    }
}

function applyModalPanelEffect(panelId, /* effectFunc, */params) {
    if (panelId /* && effectFunc */) {

        var modalPanel = $(panelId);

        if (modalPanel && modalPanel.component) {
            var component = modalPanel.component;
            var div = component.getSizedElement();

            // Element.hide(div);
            div.style.visibility = 'hidden';

            // effectFunc.call(this, Object.extend({targetId: div.id}, params ||
            // {}));
        }

    }
}

/*
 * // fix IE6 footer position function kickLayout(selector) {
 *
 * if(Richfaces && Richfaces.browser.isIE6) { var element = jQuery(selector);
 * if(element) { element.css('zoom','normal').css('zoom','100%'); } } }
 */

function show(id) {
    document.getElementById(id).style.display = 'inherit';
}

function hide(id) {
    document.getElementById(id).style.display = 'none';
}

/** Facebook * */

pushImage = function(album_id, url, message) {
    FB.getLoginStatus(function(response) {
        if (response.status === "connected") {

            FB.api('/' + album_id + '/photos', 'post', {
                message : (message || 'No description'),
                url : url
            }, function(response) {
                if (!response || response.error) {
                    console.log('Error occured');
                    console.log(response);
                } else {
                    console.log('Post ID: ' + response.id);
                }
            });
        }
    });
};

getAlbums = function(callback) {
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {
            FB.api({
                method : 'fql.multiquery',
                queries : {
                    query1 : 'SELECT cover_pid, object_id, name FROM album WHERE owner = me() AND can_upload = "true"',
                    query2 : 'SELECT src FROM photo WHERE pid IN (SELECT cover_pid FROM #query1)'
                }
            }, function(response) {
                if (!response || response.error) {
                    console.log('Error occured');
                    console.log(response);
                } else {
                    for (var i = 0; i < response[0].fql_result_set.length; i++) {
                        response[0].fql_result_set[i].src = response[1].fql_result_set[i].src;
                    }
                    callback(JSON.stringify(response[0].fql_result_set));
                    console.log(response[0].fql_result_set);
                }
            });
        }
    });
};

FBlogin = function(callback) {
    FB.login(function(response) {
        if (response.authResponse) {
            console.log('Welcome!  Fetching your information.... ');

            FB.api('/me', function(response) {
                console.log('Good to see you, ' + response.name + '.');
            });

            getUserInfo(callback);
        } else {
            console.log('User cancelled login or did not fully authorize.');
        }
    }, {
        scope : 'read_stream, publish_stream, user_photos, user_birthday, email'
    });
};

FBlogout = function() {
    FB.logout(function(response) {
        if (!response || response.error) {
            console.log('Error during logout!');
            console.log(response);
        }
    });
};

getUserInfo = function(callback) {
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {

            FB.api('/me?fields=first_name,last_name,email,username,birthday,gender,picture.width(24).height(24)', 'get', function(response) {
                if (!response || response.error) {
                    console.log('Error occured');
                    console.log(response);
                } else {
                    callback(JSON.stringify(response));
                    console.log(response);
                }
            });
        }
    });
};
