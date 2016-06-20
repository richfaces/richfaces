/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
(function($, rf) {

    var NEW_NODE_TOGGLE_STATE = "__NEW_NODE_TOGGLE_STATE";

    var TRIGGER_NODE_AJAX_UPDATE = "__TRIGGER_NODE_AJAX_UPDATE";

    var SELECTION_STATE = "__SELECTION_STATE";

    var TREE_CLASSES = ["rf-tr-nd-colps", "rf-tr-nd-exp"];

    var TREE_HANDLE_CLASSES = ["rf-trn-hnd-colps", "rf-trn-hnd-exp"];

    var TREE_ICON_CLASSES = ["rf-trn-ico-colps", "rf-trn-ico-exp"];

    rf.ui = rf.ui || {};

    rf.ui.TreeNode = rf.BaseComponent.extendClass({

            name: "TreeNode",

            /**
             * Backing object for rich:treeNode
             * 
             * @extends RichFaces.BaseComponent
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.TreeNode
             * 
             * @param id
             * @param commonOptions
             */
            init: function (id, commonOptions) {
                $superTreeNode.constructor.call(this, id);
                this.__rootElt = $(this.attachToDom());

                this.__children = new Array();

                this.__initializeChildren(commonOptions);

                var handlers = (commonOptions.clientEventHandlers || {})[this.getId().substring(commonOptions.treeId.length)] || {};

                if (handlers.bth) {
                    rf.Event.bind(this.__rootElt, "beforetoggle", new Function("event", handlers.bth));
                }

                if (handlers.th) {
                    rf.Event.bind(this.__rootElt, "toggle", new Function("event", handlers.th));
                }

                this.__addLastNodeClass();
            },

            destroy: function() {

                if (this.parent) {
                    this.parent.removeChild(this);
                    this.parent = null;
                }

                this.__clientToggleStateInput = null;

                this.__clearChildren();

                this.__rootElt = null;

                $superTreeNode.destroy.call(this);
            },

            __initializeChildren: function(commonOptions) {
                var _this = this;
                this.__rootElt.children(".rf-tr-nd").each(function() {
                    _this.addChild(new rf.ui.TreeNode(this, commonOptions));
                });
            },

            __addLastNodeClass: function() {
                if (this.__rootElt.next("div").length == 0) {
                    this.__rootElt.addClass("rf-tr-nd-last");
                }
            },

            __getNodeContainer: function() {
                return this.__rootElt.find(" > .rf-trn:first");
            },

            __getHandle: function() {
                return this.__getNodeContainer().find(" > .rf-trn-hnd:first");
            },

            __getContent: function() {
                return this.__getNodeContainer().find(" > .rf-trn-cnt:first");
            },

            __getIcons: function() {
                return this.__getContent().find(" > .rf-trn-ico");
            },

            getParent: function() {
                return this.__parent;
            },

            setParent: function(newParent) {
                this.__parent = newParent;
            },

            addChild: function(child, idx) {
                var start;
                if (typeof idx != 'undefined') {
                    start = idx;
                } else {
                    start = this.__children.length;
                }

                this.__children.splice(start, 0, child);
                child.setParent(this);
            },

            removeChild: function(child) {
                if (this.__children.length) {
                    var idx = this.__children.indexOf(child);
                    if (idx != -1) {
                        var removedChildren = this.__children.splice(idx, 1);
                        if (removedChildren) {
                            for (var i = 0; i < removedChildren.length; i++) {
                                removedChildren[i].setParent(undefined);
                            }
                        }
                    }
                }
            },

            __clearChildren: function() {
                for (var i = 0; i < this.__children.length; i++) {
                    this.__children[i].setParent(undefined);
                }

                this.__children = new Array();
            },

            isExpanded: function() {
                return !this.isLeaf() && this.__rootElt.hasClass("rf-tr-nd-exp");
            },

            isCollapsed: function() {
                return !this.isLeaf() && this.__rootElt.hasClass("rf-tr-nd-colps");
            },

            isLeaf: function() {
                return this.__rootElt.hasClass("rf-tr-nd-lf");
            },

            __canBeToggled: function() {
                return !this.isLeaf() && !this.__rootElt.hasClass("rf-tr-nd-exp-nc") && !this.__loading;
            },

            toggle: function() {
                if (!this.__canBeToggled()) {
                    return;
                }

                if (this.isCollapsed()) {
                    this.expand();
                } else {
                    this.collapse();
                }
            },

            __updateClientToggleStateInput: function(newState) {
                if (!this.__clientToggleStateInput) {
                    this.__clientToggleStateInput = $("<input type='hidden' />").appendTo(this.__rootElt)
                        .attr({name: this.getId() + NEW_NODE_TOGGLE_STATE});
                }

                this.__clientToggleStateInput.val(newState.toString());

            },

            __fireBeforeToggleEvent: function() {
                return rf.Event.callHandler(this.__rootElt, "beforetoggle");
            },

            __fireToggleEvent: function() {
                rf.Event.callHandler(this.__rootElt, "toggle");
            },

            __makeLoading: function() {
                this.__loading = true;
                this.__getNodeContainer().addClass("rf-trn-ldn");
            },

            __resetLoading: function() {
                this.__loading = false;
                this.__getNodeContainer().removeClass("rf-trn-ldn");
            },

            __changeToggleState: function(newState) {
                if (!this.isLeaf()) {
                    if (newState ^ this.isExpanded()) {

                        if (this.__fireBeforeToggleEvent() === false) {
                            return;
                        }

                        var tree = this.getTree();

                        switch (tree.getToggleType()) {
                            case 'client':
                                this.__rootElt.addClass(TREE_CLASSES[newState ? 1 : 0]).removeClass(TREE_CLASSES[!newState ? 1 : 0]);
                                this.__getHandle().addClass(TREE_HANDLE_CLASSES[newState ? 1 : 0]).removeClass(TREE_HANDLE_CLASSES[!newState ? 1 : 0]);

                                var icons = this.__getIcons();
                                if (icons.length == 1) {
                                    icons.addClass(TREE_ICON_CLASSES[newState ? 1 : 0]).removeClass(TREE_ICON_CLASSES[!newState ? 1 : 0]);
                                }

                                this.__updateClientToggleStateInput(newState);
                                this.__fireToggleEvent();
                                break;

                            case 'ajax':
                            case 'server':
                                //TODO - event?
                                tree.__sendToggleRequest(null, this, newState);
                                break;
                        }
                    }
                }
            },

            collapse: function() {
                this.__changeToggleState(false);
            },

            expand: function() {
                this.__changeToggleState(true);
            },

            __setSelected: function(value) {
                var content = this.__getContent();
                if (value) {
                    content.addClass("rf-trn-sel");
                } else {
                    content.removeClass("rf-trn-sel");
                }

                this.__selected = value;
            },

            isSelected: function() {
                return this.__selected;
            },

            getTree: function() {
                return this.getParent().getTree();
            },

            getId: function() {
                return this.__rootElt.attr('id');
            }

        });

    // define super class link for TreeNode
    var $superTreeNode = rf.ui.TreeNode.$super;

    rf.ui.TreeNode.initNodeByAjax = function(nodeId, commonOptions) {
        var node = $(document.getElementById(nodeId));

        var opts = commonOptions || {};

        var parent = node.parent(".rf-tr-nd, .rf-tr");

        var idx = node.prevAll(".rf-tr-nd").length;

        var parentNode = rf.component(parent[0]);
        opts.treeId = parentNode.getTree().getId();

        var newChild = new rf.ui.TreeNode(node[0], opts);
        parentNode.addChild(newChild, idx);

        var tree = parentNode.getTree();

        if (tree.getSelection().contains(newChild.getId())) {
            newChild.__setSelected(true);
        }
    };

    rf.ui.TreeNode.emitToggleEvent = function(nodeId) {
        var node = document.getElementById(nodeId);
        if (!node) {
            return;
        }

        rf.component(node).__fireToggleEvent();
    };

    var findTree = function(elt) {
        return rf.component($(elt).closest(".rf-tr"));
    };

    var findTreeNode = function(elt) {
        return rf.component($(elt).closest(".rf-tr-nd"));
    };

    var isEventForAnotherTree = function(tree, elt) {
        return tree != findTree(elt);
    };

    rf.ui.Tree = rf.ui.TreeNode.extendClass({

            name: "Tree",

            /**
             * Backing object for rich:tree
             * 
             * @extends RichFaces.ui.TreeNode
             * @memberOf! RichFaces.ui
             * @constructs RichFaces.ui.Tree
             * 
             * @param id
             * @param options
             */
            init: function (id, options) {
                this.__treeRootElt = $(rf.getDomElement(id));

                var commonOptions = {};
                commonOptions.clientEventHandlers = options.clientEventHandlers || {};
                commonOptions.treeId = id;

                $superTree.constructor.call(this, this.__treeRootElt, commonOptions);

                this.__toggleType = options.toggleType || 'ajax';
                this.__selectionType = options.selectionType || 'client';

                if (options.ajaxSubmitFunction) {
                    this.__ajaxSubmitFunction = new Function("event", "source", "params", "complete", options.ajaxSubmitFunction);
                }

                if (options.onbeforeselectionchange) {
                    rf.Event.bind(this.__treeRootElt, "beforeselectionchange", new Function("event", options.onbeforeselectionchange));
                }

                if (options.onselectionchange) {
                    rf.Event.bind(this.__treeRootElt, "selectionchange", new Function("event", options.onselectionchange));
                }

                this.__toggleNodeEvent = options.toggleNodeEvent;
                if (this.__toggleNodeEvent) {
                    this.__treeRootElt.delegate(".rf-trn", this.__toggleNodeEvent, this, this.__nodeToggleActivated);
                }
                if (!this.__toggleNodeEvent || this.__toggleNodeEvent != 'click') {
                    this.__treeRootElt.delegate(".rf-trn-hnd", "click", this, this.__nodeToggleActivated);
                }

                this.__treeRootElt.delegate(".rf-trn-cnt", "mousedown", this, this.__nodeSelectionActivated);

                this.__findSelectionInput();
                this.__selection = new rf.ui.TreeNodeSet(this.__selectionInput.val());

                $(document).ready($.proxy(this.__updateSelectionFromInput, this));
            },

            __findSelectionInput: function () {
                this.__selectionInput = $(" > .rf-tr-sel-inp", this.__treeRootElt);
            },

            __addLastNodeClass: function() {
                //stub function overriding parent class method
            },

            destroy: function() {
                if (this.__toggleNodeEvent) {
                    this.__treeRootElt.undelegate(".rf-trn", this.__toggleNodeEvent, this, this.__nodeToggleActivated);
                }
                if (!this.__toggleNodeEvent || this.__toggleNodeEvent != 'click') {
                    this.__treeRootElt.undelegate(".rf-trn-hnd", "click", this, this.__nodeToggleActivated);
                }

                this.__treeRootElt.undelegate(".rf-trn-cnt", "mousedown", this.__nodeSelectionActivated);
                this.__treeRootElt = null;

                this.__selectionInput = null;
                this.__ajaxSubmitFunction = null;
                $superTree.destroy.call(this);
            },

            __nodeToggleActivated: function(event) {
                var theTree = event.data;
                if (isEventForAnotherTree(theTree, this)) {
                    return;
                }

                var treeNode = findTreeNode(this);
                treeNode.toggle();
            },

            __nodeSelectionActivated: function(event) {
                var theTree = event.data;
                if (isEventForAnotherTree(theTree, this)) {
                    return;
                }

                var treeNode = findTreeNode(this);

                if (event.ctrlKey) {
                    theTree.__toggleSelection(treeNode);
                } else {
                    theTree.__addToSelection(treeNode);
                }
            },

            __sendToggleRequest: function(event, toggleSource, newNodeState) {
                var toggleSourceId = toggleSource.getId();

                var clientParams = {};
                clientParams[toggleSourceId + NEW_NODE_TOGGLE_STATE] = newNodeState;

                if (this.getToggleType() == 'server') {
                    var form = this.__treeRootElt.closest('form');
                    rf.submitForm(form, clientParams);
                } else {
                    toggleSource.__makeLoading();
                    clientParams[toggleSourceId + TRIGGER_NODE_AJAX_UPDATE] = newNodeState;
                    this.__ajaxSubmitFunction(event, toggleSourceId, clientParams, function() {
                        var treeNode = rf.component(toggleSourceId);
                        if (treeNode) {
                            treeNode.__resetLoading();
                        }
                    });
                }
            },

            getToggleType: function() {
                return this.__toggleType;
            },

            getSelectionType: function() {
                return this.__selectionType;
            },

            getTree: function() {
                return this;
            },

            __handleSelectionChange: function(newSelection) {
                var eventData = {
                    oldSelection: this.getSelection().getNodes(),
                    newSelection: newSelection.getNodes()
                };

                if (rf.Event.callHandler(this.__treeRootElt, "beforeselectionchange", eventData) === false) {
                    return;
                }

                this.__selectionInput.val(newSelection.getNodeString());

                if (this.getSelectionType() == 'client') {
                    this.__updateSelection(newSelection);
                } else {
                    this.__ajaxSubmitFunction(null, this.getId());
                }
            },

            __toggleSelection: function(node) {
                var newSelection = this.getSelection().cloneAndToggle(node);
                this.__handleSelectionChange(newSelection);
            },

            __addToSelection: function(node) {
                var newSelection = this.getSelection().cloneAndAdd(node);
                this.__handleSelectionChange(newSelection);
            },

            __updateSelectionFromInput: function() {
                this.__findSelectionInput();
                this.__updateSelection(new rf.ui.TreeNodeSet(this.__selectionInput.val()));
            },

            __updateSelection: function(newSelection) {

                var oldSelection = this.getSelection();

                oldSelection.each(function() {
                    this.__setSelected(false)
                });
                newSelection.each(function() {
                    this.__setSelected(true)
                });

                if (oldSelection.getNodeString() != newSelection.getNodeString()) {
                    rf.Event.callHandler(this.__treeRootElt, "selectionchange", {
                            oldSelection: oldSelection.getNodes(),
                            newSelection: newSelection.getNodes()
                        });
                }

                this.__selection = newSelection;
            },

            getSelection: function() {
                return this.__selection;
            },

            __getMenuSelector: function (menu) {
                var selector = "[id='" + this.id[0].id + "'] ";
                selector += (typeof menu.options.targetSelector === 'undefined')
                    ?  ".rf-trn-cnt" : menu.options.targetSelector;
                selector = $.trim(selector);
                return selector;
            },

            contextMenuAttach: function (menu) {
                var selector = this.__getMenuSelector(menu);
                rf.Event.bind(selector, menu.options.showEvent, $.proxy(menu.__showHandler, menu), menu);
            },

            contextMenuDetach: function (menu) {
                var selector = this.__getMenuSelector(menu);
                rf.Event.unbind(selector, menu.options.showEvent);
            }
        });

    // define super class link for Tree
    var $superTree = rf.ui.Tree.$super;

    rf.ui.TreeNodeSet = function() {
        this.init.apply(this, arguments);
    };

    //TODO - that's a single-node set, implement multi-node support!
    $.extend(rf.ui.TreeNodeSet.prototype, {

            init: function(nodeId) {
                this.__nodeId = nodeId;
            },

            contains: function(node) {
                if (node.getId) {
                    return this.__nodeId == node.getId();
                } else {
                    return this.__nodeId == node;
                }
            },

            getNodeString: function() {
                return this.__nodeId;
            },

            toString: function() {
                return this.getNodeString();
            },

            getNodes: function() {
                if (this.__nodeId) {
                    var node = rf.component(this.__nodeId);
                    if (node) {
                        return [node];
                    } else {
                        return null;
                    }
                }

                return [];
            },

            cloneAndAdd: function(node) {
                return new rf.ui.TreeNodeSet(node.getId());
            },

            cloneAndToggle: function(node) {
                var nodeId;
                if (this.contains(node)) {
                    nodeId = "";
                } else {
                    nodeId = node.getId();
                }

                return new rf.ui.TreeNodeSet(nodeId);
            },

            each: function(callback) {
                $.each(this.getNodes() || [], callback);
            }
        });

}(RichFaces.jQuery, RichFaces));