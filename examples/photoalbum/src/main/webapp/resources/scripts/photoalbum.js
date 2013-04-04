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

mergeResults = function(first, second) {                        
    for(var i = 0; i < first.length; i++) {
        $.extend(first[i], second[i]);    
    }
    return first;
};

getAlbums = function(callback) {
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {
            var query1 = "SELECT cover_pid, object_id, name FROM album WHERE owner = me() AND can_upload = 'true'";
            var query2 = "SELECT src FROM photo WHERE pid IN (SELECT cover_pid FROM #q1)";
            FB.api('fql', {q: {"q1" : query1, "q2" : query2}},            
                function(response) {
                if (!response || response.error) {
                    console.log('Error occured');
                    console.log(response);
                } else {
                    result = mergeResults(response.data[0].fql_result_set, response.data[1].fql_result_set);
                    console.log(result);
                    callback(JSON.stringify(result));
                }
            });
        }
    });
};

FBlogin = function(infoCb, photoCb) {
    FB.login(function(response) {
        if (response.authResponse) {
            console.log('Welcome!  Fetching your information.... ');

            FB.api('/me?fields=first_name,last_name,email,username,birthday,gender,picture.width(24).height(24)', 'get', function(response) {
                if (!response || response.error) {
                    console.log('Error occured');
                    console.log(response);
                } else {
                    infoCb(JSON.stringify(response));
                    console.log('Good to see you, ' + response.first_name + '.');
                    console.log(response);
                    
                    FBgetShelfAlbums(response.id, photoCb);
                }
            });
        } else {
            console.log('User cancelled login or did not fully authorize.');
        }
    }, {
        scope : 'read_stream, publish_stream, user_photos, user_birthday, email'
    });
};

FBbind = function(exec, bind) {
    FB.login(function(response) {
        if (response.authResponse) {
            FB.api('/me?fields=id', 'get', function(response) {
                if (!response || response.error) {
                    console.log('Error occured');
                    console.log(response);
                } else {
                    console.log(response);
                    bind.value = response.id;
                    exec();
                }
            });
        } else {
            console.log('User cancelled login or did not fully authorize.');
        }
    });
};

// get info about all user albums on Facebook (without images)
FBgetShelfAlbums = function(userId, callback) {
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {
            var query1 = "SELECT aid, cover_pid, name, created, size FROM album WHERE owner = " + userId;
            var query2 = "SELECT src FROM photo WHERE pid IN (SELECT cover_pid FROM #q1)";
            
            FB.api('fql', {q: {"q1": query1, "q2": query2}}, 
                function(response) {
                    if (!response || response.error) {
                        console.log('Error occured');
                        console.log(response.error);
                    } else {
                        result = mergeResults(response.data[0].fql_result_set, response.data[1].fql_result_set);
                        console.log(result);
                        callback(JSON.stringify(result));
                    }
                }
            );
        }
    });
};
 
// get images from a given album
FBgetAlbumImages = function(albumId, callback) {
    FB.getLoginStatus(function(response) {
        if (response.status === "connected") {
            var query1 = "SELECT aid, pid, src, src_big, caption, created FROM photo WHERE aid = " + albumId;
            FB.api('fql', {q: {"q1" : query1}}, 
                function(response) {
                    if (!response || response.error) {
                        console.log('Error occured');
                        console.log(response);
                    } else {
                        console.log(response.data[0].fql_result_set);
                        callback(JSON.stringify(response.data[0].fql_result_set));
                    }
                }
            );
        }
    });
};
