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
(function($, richfaces) {

	var NEW_NODE_TOGGLE_STATE = "__NEW_NODE_TOGGLE_STATE";
	
	var TRIGGER_NODE_AJAX_UPDATE = "__TRIGGER_NODE_AJAX_UPDATE";
	
	var SELECTION_STATE = "__SELECTION_STATE";	
		
	var TREE_CLASSES = ["rf-tr-nd-colps", "rf-tr-nd-exp"];
	
	var TREE_HANDLE_CLASSES = ["rf-trn-hnd-colps", "rf-trn-hnd-exp"];
	
	var TREE_ICON_CLASSES = ["rf-trn-ico-colps", "rf-trn-ico-exp"];

	richfaces.ui = richfaces.ui || {};
    
	richfaces.ui.TreeNode = richfaces.BaseComponent.extendClass({

		name: "TreeNode",

		init: function (id, commonOptions) {
			this.__rootElt = $(this.attachToDom(id));

			this.__children = new Array();
			
			this.__initializeChildren(commonOptions);
			
			var __toggleHandler = (commonOptions.clientEventHandlers || {})[this.getId().substring(commonOptions.treeId.length)];
			
			if (__toggleHandler) {
				richfaces.Event.bind(this.__rootElt, "toggle", new Function("event", __toggleHandler));
			}
		},
		
		destroy: function() {
			this.$super.destroy.call(this);

			if (this.parent) {
				this.parent.removeChild(this);
				this.parent = null;
			}
			
			this.__clientToggleStateInput = null;
			
			this.__clearChildren();
			
			this.__rootElt = null;
		},

		__initializeChildren: function(commonOptions) {
			var _this = this;
			this.__rootElt.children(".rf-tr-nd").each(function() {
				_this.addChild(new richfaces.ui.TreeNode(this, commonOptions));
			});
		},
		
		__getHandle: function() {
			return this.__rootElt.find(" > .rf-trn:first > .rf-trn-hnd:first");
		},
		
		__getContent: function() {
			return this.__rootElt.find(" > .rf-trn:first > .rf-trn-cnt:first");
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
		
		toggle: function() {
			if (this.isLeaf()) {
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
		
		__fireToggleEvent: function() {
			richfaces.Event.fire(this.__rootElt, "toggle");
		},
		
		__changeToggleState: function(newState) {
			if (!this.isLeaf()) {
				if (newState ^ this.isExpanded()) {
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
							tree.__sendToggleRequest(null, this.getId(), newState);
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

	richfaces.ui.TreeNode.initNodeByAjax = function(nodeId, commonOptions) {
		var node = $(document.getElementById(nodeId));
		
		var opts = commonOptions || {};
		
		if (node.nextAll(".rf-tr-nd:first").length != 0) {
			node.removeClass("rf-tr-nd-last");
		}
		
		var parent = node.parent(".rf-tr-nd, .rf-tr");
		
		var idx = node.prevAll(".rf-tr-nd").length;
		
		var parentNode = richfaces.$(parent[0]);
		opts.treeId = parentNode.getTree().getId();

		var newChild = new richfaces.ui.TreeNode(node[0], opts);
		parentNode.addChild(newChild, idx);
		parentNode.getTree().__updateSelection();
	};
	
	richfaces.ui.TreeNode.emitToggleEvent = function(nodeId) {
		var node = document.getElementById(nodeId);
		if (!node) {
			return;
		}
		
		richfaces.$(node).__fireToggleEvent();
	};
	
	var findTree = function(elt) {
		return richfaces.$($(elt).closest(".rf-tr"));
	};
	
	var findTreeNode = function(elt) {
		return richfaces.$($(elt).closest(".rf-tr-nd"));
	};

	var isEventForAnotherTree = function(tree, elt) {
		return tree != findTree(elt);
	};
	
	richfaces.ui.Tree = richfaces.ui.TreeNode.extendClass({

		name: "Tree",

		init: function (id, options) {
			this.__treeRootElt = $(richfaces.getDomElement(id));
			
			var commonOptions = {};
			commonOptions.clientEventHandlers = options.clientEventHandlers || {};
			commonOptions.treeId = id;
			
			this.$super.init.call(this, this.__treeRootElt, commonOptions);
			
			this.__toggleType = options.toggleType || 'ajax';
			this.__selectionType = options.selectionType || 'client';
			
			if (options.ajaxSubmitFunction) {
				this.__ajaxSubmitFunction = new Function("event", "source", "params", options.ajaxSubmitFunction);
			}
			
			if (options.onselectionchange) {
				richfaces.Event.bind(this.__treeRootElt, "selectionchange", new Function("event", options.onselectionchange));
			}
			
			this.__selectionInput = $(" > .rf-tr-sel-inp", this.__treeRootElt);
			
			this.__treeRootElt.delegate(".rf-trn-hnd", "click", this, this.__itemHandleClicked);
			this.__treeRootElt.delegate(".rf-trn-cnt", "mousedown", this, this.__itemContentClicked);
			
			this.__updateSelection();
		},

		destroy: function() {
			this.$super.destroy.call(this);

			this.__treeRootElt.undelegate(".rf-trn-hnd", "click", this.__itemHandleClicked);
			this.__treeRootElt.undelegate(".rf-trn-cnt", "mousedown", this.__itemContentClicked);
			this.__treeRootElt = null;

			this.__itemContentClickedHandler = null;
			this.__selectionInput = null;
			this.__ajaxSubmitFunction = null;
		},
		
		__itemHandleClicked: function(event) {
			var theTree = event.data;
			if (isEventForAnotherTree(theTree, this)) {
				return;
			}
			
			var treeNode = findTreeNode(this);
			treeNode.toggle();
		},
		
		__itemContentClicked: function(event) {
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
			var clientParams = {};
			clientParams[toggleSource + NEW_NODE_TOGGLE_STATE] = newNodeState;
			
			if (this.getToggleType() == 'server') {
				var form = this.__treeRootElt.closest('form');
				richfaces.submitForm(form, clientParams);
			} else {
				clientParams[toggleSource + TRIGGER_NODE_AJAX_UPDATE] = newNodeState;
				this.__ajaxSubmitFunction(event, toggleSource, clientParams);
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
		
		__bindFocusHandler: function(elt) {
			elt.mousedown(this.__itemContentClickedHandler);
		},
		
		__isSelected: function(node) {
			return this.__selectedNodeId == node.getId();
		},
		
		__handleSelectionChange: function() {
			if (this.getSelectionType() == 'client') {
				this.__updateSelection();
			} else {
				this.__ajaxSubmitFunction(null, this.getId());
			}
		},
		
		__toggleSelection: function(node) {
			if (this.__isSelected(node)) {
				this.__selectionInput.val("");
			} else {
				this.__selectionInput.val(node.getId());
			}

			this.__handleSelectionChange();
		},
		
		__addToSelection: function(node) {
			this.__selectionInput.val(node.getId());

			this.__handleSelectionChange();
		},
		
		__updateSelection: function() {
			var oldSelection = this.__selectedNodeId;
			var nodeId = this.__selectionInput.val();

			if (oldSelection == nodeId) {
				return;
			}
			
			if (oldSelection) {
				var oldSelectionNode = richfaces.$(oldSelection);
				if (oldSelectionNode) {
					oldSelectionNode.__setSelected(false);
				}
			}
			
			var newSelectionNode;
			var selection = new Array();
			
			if (nodeId) {
				newSelectionNode = richfaces.$(nodeId);
			}

			if (newSelectionNode) {
				newSelectionNode.__setSelected(true);
				selection.push(newSelectionNode);
			}

			this.__selectedNodeId = nodeId;
			richfaces.Event.fire(this.__treeRootElt, "selectionchange", {selection: selection});
		}
	});

}(jQuery, RichFaces));