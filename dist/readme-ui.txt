RichFaces - Ajax enabled JSF 2.0 component library
=========================================================
Version 4.0.0.M2

This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt). 

Getting up and running quickly
-------------------------------

1) Put RichFaces libraries and its dependencies in your application libraries folder:
	1.1)  richfaces-commons-api-4.0.0.20100824-M2.jar
	1.2)  richfaces-core-api-4.0.0.20100824-M2.jar
	1.3)  richfaces-core-impl-4.0.0.20100824-M2.jar
	1.4)  richfaces-components-api-4.0.0.20100824-M2.jar
	1.5)  richfaces-components-ui-4.0.0.20100824-M2.jar
	1.6)  slf4j-api
	1.7)  slf4j-log4j12 (or whatever else - select the implementation according to logging framework that you use)
	1.8)  sac-1.3, cssparser-0.9.5 - required for components CSS work
	1.9)  google-guava-r06 - core runtime dependency.
	1.10) annotations.jar from org.richfaces.cdk . It's optional and should be added only if some RichFaces components will be 
	      created/accessed dynamically from some appication actions/listeners. 

For optimal performance it's recommended to add one of these caching frameworks to application classpath: EhCache, JBoss Cache, OsCache. 	

	
2) As opposed to the previous 3.x.y versions of RichFaces, no special configuration steps are necessary. 
You don't need to modify web.xml or faces-config.xml

3) Add RichFaces namespaces/taglibs declaration to your VDL/XHTML pages which will use RichFaces components
   Use xmlns:a4j="http://richfaces.org/a4j" namespace for core components
   Use xmlns:rich="http://richfaces.org/rich" namespace for rich components

4) Add one of the available RichFaces 4.0.M2 components to your page and try it!
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
rich:subTable
rich:dataScroller
rich:subTableToggleControl
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
rich:panel
rich:popupPanel
rich:togglePanel
rich:toggleControl
rich:togglePanelItem
rich:itemChangeListener
rich:accordion
rich:accordionItem

---------------------------------
Output:
---------------------------------
rich:autocomplete
rich:inplaceInput
rich:inputNumberSlider

Learn more
----------
RichFaces Project - http://www.jboss.org/richfaces
RichFaces 4.0 Wiki - http://www.jboss.org/community/wiki/RichFaces40Planning