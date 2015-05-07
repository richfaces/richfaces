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
 // fix IE6 footer position
 function kickLayout(selector) {

 if(Richfaces && Richfaces.browser.isIE6) {
 var element = jQuery(selector);
 if(element) {
 element.css('zoom','normal').css('zoom','100%');
 }
 }

 } */

function toJQObject(idEndsOrJQObject) {
    if ((typeof idEndsOrJQObject) == "object") {
        return idEndsOrJQObject;
    }
    return $("[id$='" + idEndsOrJQObject + "']");
}

function show(idEndsOrJQObject) {
    toJQObject(idEndsOrJQObject).fadeIn();
}

function hide(idEndsOrJQObject) {
    toJQObject(idEndsOrJQObject).fadeOut();
}

function select(idEndsOrJQObject) {
    toJQObject(idEndsOrJQObject).fadeTo('slow', 0.50);
}

/*
 *   Facebook functions
 */

var F = {};

(function(F) {

    F.pushImage = function(album_id, url, message, errorCb) {
        FB.getLoginStatus(function(response) {
            if (response.status === "connected") {

                FB.api('/' + album_id + '/photos', 'post', {
                    message : (message || 'No description'),
                    url : url
                }, function(response) {
                    if (!response || response.error) {
                        errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
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

    F.login = function(infoCb, albumIdsCb, errorCb) {
        FB.login(function(response) {
            if (response.authResponse) {
                FB.api('/me?fields=first_name,last_name,email,birthday,gender,picture.width(24).height(24)', 'get', function(response) {
                    if (!response || response.error) {
                        errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                    } else {
                        infoCb(JSON.stringify(response));
                    }
                });
            } else {
                errorCb('Error' + errorDelimiter + 'User cancelled login or did not fully authorize.');
            }
        }, {
            scope : 'read_stream, publish_actions, user_photos, user_birthday, email'
        });
    };
    
    translateFBJson = function(dictionary) {
        return function(json) {
            var translatedJson = { type: "facebook" };
        
            for(word in dictionary) {
                if (dictionary.hasOwnProperty(word)) {
                    translatedJson[word] = json[ dictionary[word] ];
                }
            }
        
            return translatedJson;
        };
    };
    
    translateFBImage = function(image) {
        var dict = {
            "created": "created_time",
            "id": "id",
            "name": "name",
            "thumbUrl": "picture"
        };
         
        return (translateFBJson(dict))(image);
    };
    
    translateFBAlbum = function(album) {
        var dict = {
            "created": "created_time",
            "fullId": "id",
            "id": "id",
            "name": "name",
            "size": "count"
        };
         
        return (translateFBJson(dict))(album);
    };
    
    // get all albums and images
    F.getAllAlbums = function(callback, errorCb) {
        FB.getLoginStatus(function(response) {
            if (response.status === "connected") {
                FB.api('me/albums?fields=picture,created_time,count,name,photos{created_time,images,picture,name}', 'get',
                  function(response) {
                    if (!response || response.error) {
                        errorCb('Error occured' + errorDelimiter + (response.error.message || 'Response not received'));
                    } else {
                        callback(JSON.stringify(convertFBAlbums(response.data)));
                    }
                });
            }
        });
    };
    
    convertFBAlbums = function (data) {
        /*
         * In (only relevant fields):
         * 
         * data: [ALBUM]
         * --
         * ALBUM: {
         *   created_time: CREATED, -- 2013-04-21T13:09:54+0000, has to be parsed
         *   count: SIZE, -- missing on empty albums
         *   name: NAME,
         *   id: ID,
         *   photos: {data: [PHOTO]},
         *   picture: {
         *     data: {
         *       url: COVERURL
         *     }
         *   }
         * --
         * PHOTO: {
         *   created_time: CREATED,
         *   images: [IMAGE], -- contains several resolutions, we pick full size and medium size (to fit our layout)
         *   picture: THUMBURL,
         *   id: ID,
         *   name: NAME -- optional
         *   }
         * --
         * IMAGE: {
         *   height: height, width: width,
         *   source: URL
         *   }
         *   
         * Out:
         * [ALBUM]
         * --
         * ALBUM: {
         *   created: CREATED,
         *   size: SIZE,
         *   name: NAME,
         *   id: ID,
         *   coverUrl: COVERURL,
         *   images: [PHOTO]
         *   }
         * --
         * PHOTO: {
         *   thumbUrl: THUMBURL,
         *   url: URL,
         *   fullResUrl: URL
         *   name: NAME/ID
         *   id: ID
         *   created: CREATED
         *   }
         *   
         */
        
        var result = [],
            album;
        
        for (var i = 0; i < data.length; i++) {
            album = translateFBAlbum(data[i]);
            
            album.created = Date.parse(album.created);
            
            if (!album.size) { // empty albums have undefined count
                album.size = 0;
            }
            
            album.coverUrl = data[i].picture ? data[i].picture.data.url : 'resources/img/shell/frame_photo_120.png';
            
            if (data[i].photos) {
                album.images = getFBPhotos(data[i].photos.data);
            }
            
            result.push(album);
        }
        
        return result;
    };
    
    getFBPhotos = function(photos) {
        var result = [],
            photo;
        
        for (var i = 0; i < photos.length; i++) {
            photo = translateFBImage(photos[i]);
            
            photo.created = Date.parse(photo.created);
            
            if (!photo.name) {
                photo.name = photo.id;
            }
            
            addFBPhotoUrls(photo,photos[i].images);
            
            result.push(photo);
        }
        
        return result;
    };
    
    addFBPhotoUrls = function(photo, images) {
        if (!images) {return;}
        photo.fullResurl = images[0].source; // first image is the largest
        
        var max, currentMax = 0, midResUrl;
        for (var i = 0; i < images.length; i++) {
            max = images[i].height > images[i].width ? images[i].height : images[i].width;
            
            if (max > currentMax && max <= 600) {
                midResUrl = images[i].source;
                currentMax = max;
            }
        }
        
        photo.url = midResUrl;
    };
})(F);

/*
*   Google+ functions
*/

var G = {};

(function(G) {

    var clientId = '1039898720906.apps.googleusercontent.com',
        apiKey = 'AIzaSyCdgeC_TiJqDCOBkdoF51n6s2WUZDg1nIM',
        scopes = 'https://picasaweb.google.com/data/ https://www.googleapis.com/auth/plus.me';

    logAndGetAlbums = function(callbacks) {
        return function(authResult) {
            if (authResult && !authResult.error) {
                getUserInfo(authResult.access_token, callbacks.infoCallback, callbacks.albumCallback);
            } else {
                callbacks.errorCallback("Error occured" + errorDelimiter + authResult.error);
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
         *     fullId: "id:albumId",
         *     name: NAME,
         *     created: DATE,
         *     coverUrl: URL
         *   }
         * ]
         */

        var extractedAlbums = [];

        for (var i = 0; i < albumEntries.length; i++) {
            var album = { type: "google" },
                idString = albumEntries[i].id.$t, begin = idString.lastIndexOf("/"), 
                end = idString.indexOf("?", begin), 
                albumId = idString.substr(begin + 1, end - begin - 1),
                albumName = albumEntries[i].title.$t,
                mg = albumEntries[i].media$group.media$content[0],
                splitAt = mg.url.lastIndexOf("/"),
                published = albumEntries[i].published.$t;

            album.id = albumId;
            album.userId = userId;
            album.fullId = userId + ":" + albumId;
            album.name = albumName;
            album.created = published.substring(0, published.indexOf("T"));

            album.coverUrl = mg.url.substring(0, splitAt) + "/s288" + mg.url.substring(splitAt);

            extractedAlbums.push(album);
        }

        return JSON.stringify(extractedAlbums);
    };

    // Get albums
    G.getUserAlbums = function(userId, albumCallback, authResult) {

        var getAlbums = function(authResult) {
            var callback = function(data, status, jq) {
                albumCallback(extractAlbums(userId, data.feed.entry));
            };

            var url = "https://picasaweb.google.com/data/feed/base/user/" + userId + "?access_token=" 
                    + authResult.access_token + "&kind=album&alt=json&fields=entry(id,published,title,media:group(media:content))&callback=?";
                    
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

    extractPhotos = function(fullAlbumId, photoEntries) {
        /*
         * Take a JSON returned from Picasa and extract the important properties
         *
         * @output
         * [
         *   {
         *     id: ID,
         *     albumId: ID,
         *     fullAlbumId: fID,
         *     created: DATE,
         *     name: STRING,
         *     url: URL,
         *     thumbUrl: tURL // created from url
         *   }
         * ]
         */

        var extractedPhotos = [];
        for (var i = 0; i < photoEntries.length; i++) {
            var photo = { type: "google" }, 
                mg = photoEntries[i].media$group.media$content[0], 
                idString = photoEntries[i].id.$t, 
                begin = idString.lastIndexOf("/"), 
                end = idString.indexOf("?", begin), 
                photoId = idString.substr(begin + 1, end - begin - 1), 
                splitAt = mg.url.lastIndexOf("/"),
                published = photoEntries[i].published.$t;
                

            photo.id = photoId;
            photo.albumId = (fullAlbumId.split(":"))[1];
            photo.fullAlbumId = fullAlbumId;
            photo.name = photoEntries[i].media$group.media$title.$t;

            photo.created = published.substring(0, published.indexOf("T"));

            // create urls, up to 1600px for the image, up to 288px for thumbnail
            photo.url = mg.url.substring(0, splitAt) + "/s1600" + mg.url.substring(splitAt);
            photo.thumbUrl = mg.url.substring(0, splitAt) + "/s288" + mg.url.substring(splitAt);

            extractedPhotos.push(photo);
        }
        
        return JSON.stringify(extractedPhotos);
    };

    G.getImages = function(fullId, photoCallback) {
        var getImages = function(authResult) {
            var ids = fullId.split(":"), 
                callback = function(data, status, jq) {
                    photoCallback(extractPhotos(fullId, data.feed.entry));
                },           
                url = "https://picasaweb.google.com/data/feed/base/user/" + ids[0] + "/albumid/" 
                    + ids[1] + "?access_token=" + authResult.access_token + "&kind=photo&alt=json&fields=entry(id,published,media:group(media:content, media:title))&callback=?";
                         
            $.getJSON(url, null, callback);
        };
        
        if (gapi.auth.getToken()) {
            getImages(gapi.auth.getToken());
            return;
        }
        
        // if token wasn't provided
        gapi.auth.authorize({
            client_id : clientId,
            scope : scopes,
            immediate : true
        }, getImages);
    };
    
    G.getAlbumAndImages = function(albumFullId, callback) {
        var getAlbumAndImages = function(authResult) {
            var ids = albumFullId.split(":"),
            
                albumUrl = "https://picasaweb.google.com/data/feed/base/user/" + ids[0] 
                + "?access_token=" + authResult.access_token 
                + "&kind=album&alt=json&fields=entry(id,published,title,media:group(media:content))&callback=?",
            
                compress = function (album, callback) {
                    return function(images) {
                        album.images = JSON.parse(images);
                        callback(JSON.stringify(album));   
                    };
                },
            
                processAlbum = function(albumId, albumString, callback) {
                    var albums = JSON.parse(albumString),
                        album = {};
                
                    for(var i = 0; i < albums.length; i++) {
                        if (albums[i].fullId == albumId) {
                            album = albums[i]; break;
                        }
                    }
                
                    G.getImages(album.fullId, compress(album, callback));
                },
            
                userId = albumFullId.split(":")[0],
            
                albumCb = function(data, status, jq) {
                    processAlbum(albumFullId, extractAlbums(userId, data.feed.entry), callback);
                };

                $.getJSON(albumUrl, null, albumCb);
            };
            
        if (gapi.auth.getToken()) {
            getAlbumAndImages(gapi.auth.getToken());
            return;
        }
        
        // if token wasn't provided
        gapi.auth.authorize({
            client_id : clientId,
            scope : scopes,
            immediate : true
        }, getAlbumAndImages);
    };

})(G);
