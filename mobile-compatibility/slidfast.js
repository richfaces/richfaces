/*!
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
 *
 *  Author: Wesley Hales
 */

var slidfast = {

    start: function() {
        this.hideURLBar();
        //set the default page
        this.focusPage = document.getElementById(this.defaultPageID);
        this.locationChange();
    },

    defaultPageID: null,

    defaultPageHash: null,

    focusPage:  null,

    isReady: false,

    hideURLBar: function() {
        //hide the url bar on mobile devices
        setTimeout(scrollTo, 0, 0, 1)
    },

    slideTo :   function (id) {
        //check for double hash change call and no hash scenario on
        //initial page load.
        if (id == this.focusPage.id && (location.hash != "#" + this.defaultPageHash && location.hash != '')) {
            return;
        }
        var focusPage = this.focusPage;
        //1.)the page we are bringing into focus dictates how
        // the current page will exit. So let's see what classes
        // our incoming page is using. We know it will have stage[right|left|etc...]
        var classes = document.getElementById(id).className.split(' ');

        //2.)decide if the incoming page is assigned to right or left
        // (-1 if no match)
        var stageType = classes.indexOf('stage-left');

        //3.) decide how this focused page should exit.
        if (stageType > 0) {
            focusPage.className = 'page transition stage-right';
        } else {
            focusPage.className = 'page transition stage-left';
        }

        //4. refresh/set the variable
        focusPage = this.focusPage = document.getElementById(id);

        //5. Bring in the new page and set the global.
        focusPage.className = 'page transition stage-center';

    },

    init: function(defaultPageID, defaultPageHash) {
        this.defaultPageID = defaultPageID;
        this.defaultPageHash = defaultPageHash;

        window.addEventListener('load', function(e) {
            slidfast.isReady = true;
            slidfast.start(slidfast.defaultPageID, slidfast.defaultPageHash);

        }, false);
        window.addEventListener('hashchange', function(e) {
            slidfast.locationChange();
        }, false);

    },

    locationChange: function() {
        if (location.hash === "#" + this.defaultPageHash || location.hash == '') {
            this.slideTo(this.defaultPageID);
            //we're on the default page, so no need for back button
            document.getElementById("back-button").className = 'hide-button';
        } else {
            var hashArray = location.hash.split(':');
            var id;
            var sample;
            if (hashArray.length === 2) {
                id = hashArray[0].replace('#', '');
                sample = hashArray[1];
                handleHashChange(id, sample);
                //show the back button and attach functions
                document.getElementById("back-button").className = 'basic-button left-header-button';
                document.getElementById("back-button").onclick = function() {
                    slidfast.slideTo(slidfast.defaultPageID);
                    location.hash = slidfast.defaultPageHash;
                };
            }

        }
    },

    flip: function() {
        //get a handle on the flippable region
        var front = document.getElementById('front');
        var back = document.getElementById('back');

        //just a simple way to see what the state is
        var classes = front.className.split(' ');
        var flipped = classes.indexOf('flipped');

        if (flipped >= 0) {
            //already flipped, so return to original
            front.className = 'normal';
            back.className = 'flipped';
        } else {
            //do the flip
            front.className = 'flipped';
            back.className = 'normal';

        }
    }

};
