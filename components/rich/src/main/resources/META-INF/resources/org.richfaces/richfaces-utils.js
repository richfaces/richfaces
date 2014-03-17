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

// RichFaces.utils
// RichFaces.utils.cache
(function ($, rf) {
    rf.utils = rf.utils || {};

    rf.utils.Cache = function (key, items, values, useCache) {
        this.key = key.toLowerCase();
        this.cache = {};
        this.cache[this.key] = items || [];
        this.originalValues = typeof values == "function" ? values(items) : values || this.cache[this.key];
        this.values = processValues(this.originalValues);
        this.useCache = useCache || checkValuesPrefix.call(this);
    };

    var processValues = function (values) {
        var processedValues = [];
        for (var i = 0; i < values.length; i++) {
            processedValues.push(values[i].toLowerCase());
        }
        return processedValues;
    };

    var checkValuesPrefix = function () {
        var result = true;
        for (var i = 0; i < this.values.length; i++) {
            if (this.values[i].indexOf(this.key) != 0) {
                result = false;
                break;
            }
        }
        return result;
    };

    var getItems = function (key, filterFunction) {
        key = key.toLowerCase();
        var newCache = [];

        if (key.length < this.key.length) {
            return newCache;
        }

        if (this.cache[key]) {
            newCache = this.cache[key];
        } else {
            var useCustomFilterFunction = typeof filterFunction == "function";
            var itemsCache = this.cache[this.key];
            for (var i = 0; i < this.values.length; i++) {
                var value = this.values[i];
                if (useCustomFilterFunction && filterFunction(key, value)) {
                    newCache.push(itemsCache[i]);
                } else {
                    var p = value.indexOf(key);
                    if (p == 0) {
                        newCache.push(itemsCache[i]);
                    }
                }
            }

            if ((!this.lastKey || key.indexOf(this.lastKey) != 0) && newCache.length > 0) {
                this.cache[key] = newCache;
                if (newCache.length == 1) {
                    this.lastKey = key;
                }
            }
        }

        return newCache;
    };

    var getItemValue = function (item) {
        return this.originalValues[this.cache[this.key].index(item)];
    };

    var isCached = function (key) {
        key = key.toLowerCase();
        return this.cache[key] || this.useCache && key.indexOf(this.key) == 0;
    };

    $.extend(rf.utils.Cache.prototype, (function () {
        return  {
            getItems: getItems,
            getItemValue: getItemValue,
            isCached: isCached
        };
    })());

})(RichFaces.jQuery, RichFaces);