RichFaces - Ajax enabled JSF 2.0 component library
=========================================================
Version 4.0.0.M6

This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt). 

Getting up and running quickly
-------------------------------

1) Put RichFaces libraries and its dependencies in your application libraries folder:
	1.1)  richfaces-core-api.jar
	1.2)  richfaces-core-impl.jar
	1.3)  richfaces-components-api.jar
	1.4)  richfaces-components-ui.jar
	1.5)  sac-1.3, cssparser-0.9.5 - required for components CSS work
	1.6)  google-guava-r06 - core runtime dependency.
	1.7)  annotations.jar from org.richfaces.cdk
	    - It's optional and only needs to be added if RichFaces components will be 
	      created/accessed dynamically in your apps actions/listeners. 
	1.8)  validation-api.jar and any implementation like hibernate-validators.jar - It's optional and should be added if you using Client or Graph Validation. Should be added only if it's not provided by server(Java EE 6 servers)  

For optimal performance it's recommended to add one of these caching frameworks to application classpath: EhCache, JBoss Cache, OsCache. 	

2) As opposed to the previous 3.x.y versions of RichFaces, no special configuration steps are necessary. 
You don't need to modify web.xml or faces-config.xml (except a4j:push component that requires additional configuration in web.xml; 
please refer to the documentation)

3) Add RichFaces namespaces/taglibs declaration to your VDL/XHTML pages which will use RichFaces components
   Use xmlns:a4j="http://richfaces.org/a4j" namespace for core components
   Use xmlns:rich="http://richfaces.org/rich" namespace for rich components

4) Add one of the available RichFaces 4.0.M5 components to your page and try it!
4.1) Take a look at the RichFaces examples for assistance with usage.

Available Components/Behaviors/Tags/Functions

---------------------------------
Core:
---------------------------------

a4j:ajax
a4j:commandLink
a4j:commandButton
a4j:push
a4j:mediaOutput
a4j:status
a4j:jsFunction
a4j:log
a4j:outputPanel
a4j:poll
a4j:param
a4j:queue
a4j:attachQueue
a4j:repeat  
a4j:region
a4j:actionListener

---------------------------------
Iteration:
---------------------------------
rich:dataTable
rich:extendedDataTable
rich:collapsibleSubTable
rich:dataScroller
rich:collapsibleSubTableToggler
rich:dataGrid
rich:list
rich:columnGroup
rich:column

---------------------------------
Miscellaneous:
---------------------------------
rich:jQuery
rich:clientId
rich:element
rich:component
rich:isUserInRole
rich:findComponent
rich:componentControl
rich:hashParam

---------------------------------
Output:
---------------------------------
rich:collapsiblePanel
rich:panel
rich:popupPanel
rich:tabPanel
rich:tab
rich:togglePanel
rich:toggleControl
rich:togglePanelItem
rich:itemChangeListener
rich:accordion
rich:accordionItem
rich:panelMenu
rich:panelMenuGroup
rich:panelMenuItem
rich:progressBar
rich:tooltip
rich:message

---------------------------------
Menus:
---------------------------------
rich:toolbar
rich:toolbarGroup
rich:dropDownMenu
rich:menuGroup
rich:menuItem
rich:menuSeparator
rich:panelMenu
rich:panelMenuGroup
rich:panelMenuItem

---------------------------------
Input:
---------------------------------
rich:autocomplete
rich:inplaceInput
rich:inplaceSelect
rich:inputNumberSlider
rich:inputNumberSpinner
rich:select
rich:calendar
rich:fileUpload

---------------------------------
Trees:
---------------------------------
rich:tree
rich:treeNode
rich:treeModelAdaptor
rich:treeModelRecursiveAdaptor
rich:treeToggleListener
rich:treeSelectionChangeListener

---------------------------------
Drag'n'Drop:
---------------------------------
rich:dragSource
rich:dropTarget
rich:dragIndicator

---------------------------------
Validation:
---------------------------------
rich:validator
rich:graphValidator

Learn more
----------
RichFaces Project - http://www.jboss.org/richfaces
RichFaces 4.0 Wiki - http://www.jboss.org/community/wiki/RichFaces40Planning
