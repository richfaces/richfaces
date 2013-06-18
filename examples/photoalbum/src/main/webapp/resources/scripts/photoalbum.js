/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

var errorDelimiter = "$#$";

function selectPopularTag(tag, target) {
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

/*
 *   Facebook functions
 */

FBpushImage = function(album_id, url, message, errorCb) {
    FB.getLoginStatus(function(response) {
        if (response.status === "connected") {

            FB.api('/' + album_id + '/photos', 'post', {
                message : (message || 'No description'),
                url : url
            }, function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                } else {
                    console.log('Post ID: ' + response.id);
                }
            });
        }
    });
};

mergeResults = function(first, second) {
    for (var i = 0; i < first.length; i++) {
        $.extend(first[i], second[i]);
    }
    return first;
};

getAlbums = function(callback, errorCb) {
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {
            var query1 = "SELECT cover_pid, object_id, name FROM album WHERE owner = me() AND can_upload = 'true'";
            var query2 = "SELECT src FROM photo WHERE pid IN (SELECT cover_pid FROM #q1)";
            FB.api('fql', {
                q : {
                    "q1" : query1,
                    "q2" : query2
                }
            }, function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                } else {
                    result = mergeResults(response.data[0].fql_result_set, response.data[1].fql_result_set);
                    console.log(result);
                    callback(JSON.stringify(result));
                }
            });
        }
    });
};

FBlogin = function(infoCb, albumIdsCb, errorCb) {
    FB.login(function(response) {
        if (response.authResponse) {
            console.log('Welcome!  Fetching your information.... ');

            FB.api('/me?fields=first_name,last_name,email,username,birthday,gender,picture.width(24).height(24)', 'get', function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                } else {
                    infoCb(JSON.stringify(response));
                    console.log('Good to see you, ' + response.first_name + '.');
                    console.log(response);
                }
            });

            FB.api('fql', {
                q : {
                    "q1" : "SELECT aid from album WHERE owner = me()"
                }
            }, function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                } else {
                    var result_set = response.data[0].fql_result_set, result = result_set[0]["aid"], i;

                    for ( i = 1; i < result_set.length; i++) {
                        result += "," + result_set[i]["aid"];
                    }
                    console.log(result);
                    albumIdsCb(result);
                }
            });
        } else {
            console.log('User cancelled login or did not fully authorize.');
        }
    }, {
        scope : 'read_stream, publish_stream, user_photos, user_birthday, email'
    });
};

FBbind = function(exec, bind, errorCb) {
    FB.login(function(response) {
        if (response.authResponse) {
            FB.api('/me?fields=id', 'get', function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
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
FBgetShelfAlbums = function(userId, callback, errorCb) {
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {
            var query1 = "SELECT aid, cover_pid, name, created, size FROM album WHERE owner = " + userId;
            var query2 = "SELECT src FROM photo WHERE pid IN (SELECT cover_pid FROM #q1)";

            FB.api('fql', {
                q : {
                    "q1" : query1,
                    "q2" : query2
                }
            }, function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                } else {
                    result = mergeResults(response.data[0].fql_result_set, response.data[1].fql_result_set);
                    callback(JSON.stringify(result));
                }
            });
        }
    });
};

// get info about albums specified by id - (e.g. "12345", "12347")
FBgetAlbumsById = function(albumIds, callback, errorCb) {
    if (albumIds === "0") {
        callback(JSON.stringify({}));
        return;
    }
    console.log(albumIds);
    FB.getLoginStatus(function(response) {

        if (response.status === "connected") {
            var query1 = "SELECT aid, cover_pid, name, created, size FROM album WHERE aid IN (" + albumIds + ")";
            var query2 = "SELECT src FROM photo WHERE pid IN (SELECT cover_pid FROM #q1)";
            var query3 = "SELECT aid, pid, src, src_big, caption, created FROM photo WHERE aid IN (" + albumIds + ")";

            FB.api('fql', {
                q : {
                    "q1" : query1,
                    "q2" : query2,
                    "q3" : query3
                }
            }, function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                    console.log(response);
                } else {
                    var r = {
                        q1 : null,
                        q2 : null,
                        q3 : null
                    }, i;

                    // the result may not ordered differently than the queries
                    for ( i = 0; i < response.data.length; i++) {
                        r[response.data[i].name] = response.data[i].fql_result_set;
                    }

                    var result = {
                        albums : mergeResults(r.q1, r.q2),
                        images : r.q3
                    };
                    console.log(result);
                    callback(JSON.stringify(result));
                }
            });
        }
    });
};

// get images from a given album
FBgetAlbumImages = function(albumId, callback, errorCb) {
    FB.getLoginStatus(function(response) {
        if (response.status === "connected") {
            var query1 = "SELECT aid, pid, src, src_big, caption, created FROM photo WHERE aid = " + albumId;
            FB.api('fql', {
                q : {
                    "q1" : query1
                }
            }, function(response) {
                if (!response || response.error) {
                    errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                } else {
                    console.log(response.data[0].fql_result_set);
                    callback(JSON.stringify(response.data[0].fql_result_set));
                }
            });
        }
    });
};

/*
*   Google+ functions
*/

var G = {};

(function(G) {

    var clientId = '1039898720906.apps.googleusercontent.com',
        apiKey = 'AIzaSyCdgeC_TiJqDCOBkdoF51n6s2WUZDg1nIM',
        scopes = 'https://picasaweb.google.com/data/ https://www.googleapis.com/auth/plus.login';

    logAndGetAlbums = function(callbacks) {
        return function(authResult) {
            if (authResult && !authResult.error) {
                getUserInfo(authResult.access_token, callbacks.infoCallback, callbacks.albumCallback);
            } else {
                callback.errorCallback("Error occured" + errorDelimiter + authResult.error);
            }
        };
    };

    G.login = function(callbacks) {
        gapi.client.setApiKey(apiKey);
        gapi.auth.authorize({
            client_id : clientId,
            scope : scopes,
            immediate : false
        }, logAndGetAlbums(callbacks));
        return false;
    };

    // Get user info
    getUserInfo = function(accessToken, infoCallback, albumCallback) {
        var opts = {
            path : "plus/v1/people/me?access_token=" + accessToken,
            callback : function(result) {
                infoCallback(JSON.stringify(result));
                G.getUserAlbums(result.id, albumCallback, {
                    access_token : accessToken
                });
            }
        };

        gapi.client.request(opts);
    };

    extractAlbums = function(userId, albumEntries) {
        /*
         * Take a JSON returned from Picasa and extract the important properties
         * 
         * @output
         * [
         *   {
         *     id: ID,
         *     userId: UID,
         *     name: NAME,
         *     created: DATE,
         *     coverImageUrl: URL
         *   }
         * ]
         */

        var extractedAlbums = [];

        for (var i = 0; i < albumEntries.length; i++) {
            var album = {},
                idString = albumEntries[i].id.$t, begin = idString.lastIndexOf("/"), 
                end = idString.indexOf("?", begin), 
                albumId = idString.substr(begin + 1, end - begin - 1),
                albumName = albumEntries[i].title.$t,
                mg = albumEntries[i].media$group.media$content[0],
                splitAt = mg.url.lastIndexOf("/"),
                published = albumEntries[i].published.$t;

            album.id = albumId;
            album.userId = userId;
            album.name = albumName;
            album.created = published.substring(0, published.indexOf("T"));

            album.coverImageUrl = mg.url.substring(0, splitAt) + "/s288" + mg.url.substring(splitAt);

            extractedAlbums.push(album);
        }

        return JSON.stringify(extractedAlbums);
    };

    // Get albums
    G.getUserAlbums = function(userId, albumCallback, authResult) {

        var getAlbums = function(suthResult) {
            var callback = function(data, status, jq) {
                albumCallback(extractAlbums(userId, data.feed.entry));
            };

            var url = "https://picasaweb.google.com/data/feed/base/user/" + userId + "?access_token=" 
                    + authResult.access_token + "&kind=album&alt=json&callback=?";
                    
            $.getJSON(url, null, callback);
        };

        if (authResult) {
            getAlbums(authResult);
            return;
        }

        // if token wasn't provided
        gapi.auth.authorize({
            client_id : clientId,
            scope : scopes,
            immediate : true
        }, getAlbums);
    };

    extractPhotos = function(albumId, photoEntries) {
        /*
         * Take a JSON returned from Picasa and extract the important properties
         *
         * @output
         * [
         *   {
         *     id: ID,
         *     albumId: ID,
         *     created: DATE,
         *     url: URL,
         *     thumbUrl: tURL // created from url
         *   }
         * ]
         */

        var extractedPhotos = [];
        for (var i = 0; i < photoEntries.length; i++) {
            var photo = {}, 
                mg = photoEntries[i].media$group.media$content[0], 
                idString = photoEntries[i].id.$t, 
                begin = idString.lastIndexOf("/"), 
                end = idString.indexOf("?", begin), 
                photoId = idString.substr(begin + 1, end - begin - 1), 
                splitAt = mg.url.lastIndexOf("/"),
                published = photoEntries[i].published.$t;

            photo.id = photoId;
            photo.albumId = albumId;

            photo.created = published.substring(0, published.indexOf("T"));

            // create urls, up to 1600px for the image, up to 288px for thumbnail
            photo.url = mg.url.substring(0, splitAt) + "/s1600" + mg.url.substring(splitAt);
            photo.thumbUrl = mg.url.substring(0, splitAt) + "/s288" + mg.url.substring(splitAt);

            extractedPhotos.push(photo);
        }

        return JSON.stringify(extractedPhotos);
    };

    G.getPhotos = function(userId, albumId, photoCallback) {
        var getPhotos = function(authResult) {
            
            var callback = function(data, status, jq) {
                photoCallback(extractPhotos(albumId, data.feed.entry));
            },
            url = "https://picasaweb.google.com/data/feed/base/user/" + userId + "/albumid/" 
                + albumId + "?access_token=" + authResult.access_token + "&kind=photo&alt=json&callback=?";
            $.getJSON(url, null, callback);
        };
        
        if (gapi.auth.getToken()) {
            getPhotos(gapi.auth.getToken());
            return;
        }
        
        // if token wasn't provided
        gapi.auth.authorize({
            client_id : clientId,
            scope : scopes,
            immediate : true
        }, getPhotos);
    };

})(G);
