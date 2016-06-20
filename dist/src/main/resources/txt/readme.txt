========================================================================

                      RichFaces - Ajax enabled JSF 2.0 component library

                                                RichFaces 4.5.17.Final
                                                    http://richfaces.org
                                                              March 2011
                     This software is distributed under the terms of the
                            FSF Lesser Gnu Public License (see lgpl.txt)

========================================================================

RichFaces 4 is a component library for JSF2 and an advanced framework for
easily integrating AJAX capabilities into enterprise applications.

This file explains how to get started and install the RichFaces component
library. If you find any issue with the project, please report the issue
on the RichFaces user forum (http://community.jboss.org/en/richfaces)
or jira (https://issues.jboss.org/browse/RF).


FEATURES
--------

    - Many AJAX enabled components in two libraries.
           * a4j page centric AJAX controls.
           * rich self contained, ready to use components.
    - Complete JSF2 support with advanced extensions
    - Easily skin your entire application
    - Component Development Kit (CDK).
    - Dynamic resource handling.
    - Testing facilities for components, actions, listeners and pages
    - Broad cross-browser support.
    - Large and active community


OBTAINING RICHFACES 4.X
-----------------------

    1. Obtaining the latest stable distribution package

    You can download the distribution package directly from the RichFaces
    project website at http://www.jboss.org/richfaces. Source code is also
    available to download from the same site.


SETTING UP YOUR PROJECT
-----------------------

    1. Manually

    After downloading the distribution package, extract its content in a
    folder of your choice.

    In the root of the extracted folder you will find the jars needed to
    use RichFaces inside your project.  Copy the following jars to your
    application libraries folder:

        - richfaces-rich-4.5.17.Final
        - richfaces-a4j-4.5.17.Final
        - richfaces-core-4.5.17.Final

    In addition to the RichFaces jars, a number of dependencies are
    required in order to properly configure the framework.


      a.  sac.jar, cssparser.jar - required for components CSS work.

      b.  guava - core runtime dependency.

      c.  annotations.jar  - from org.richfaces.cdk.

          - It's optional and only needs to be added if RichFaces components
            will be created/accessed dynamically in your apps actions/listeners.

      d.  validation-api.jar and any implementation like hibernate-validators.jar

          - It's optional and should be added if you using Client or Graph
            Validation. Should be added only if it's not provided by a
            server (Java EE 6 servers).

    For the latest updates, and information on this please see:
    http://community.jboss.org/docs/DOC-16484

    2. Using Maven

    To setup your project using Maven, follow the instruction on the wiki at
    http://community.jboss.org/wiki/HowtoaddRichFaces4xtomavenbasedproject


For optimal performance it's recommended to add one of these caching frameworks
to application classpath: EhCache, JBoss Cache, OsCache.


GETTING STARTED
---------------

1. Aside from the steps above to use RichFaces component library, no
   special configuration steps are necessary. You don't need to modify
   web.xml or faces-config.xml as with previous versions of RichFaces,
   except for a4j:push component that requires additional configuration
   in web.xml

2. Add RichFaces namespaces/taglibs declarations to your VDL/XHTML pages
   which will use RichFaces components.

   - Use xmlns:rich="http://richfaces.org/rich" namespace for core components
   - Use xmlns:a4j="http://richfaces.org/a4j" namespace for rich components

3. Add on of the available RichFaces components to your page and try it!
   Take a look at the RichFaces examples for assistance with usage.

4. For more information on getting started with RichFaces, visit the wiki's
   Getting Started Guide at http://community.jboss.org/wiki/GettingStartedWithRichFaces4x


MORE INFORMATION AND RESOURCES
------------------------------

For more information on RichFaces 4 features, how to migrate from
previous versions, and answers to the most frequently asked questions (FAQ),
please visit RichFaces website at:

http://www.jboss.org/richfaces

Or the Wiki at:

    - http://community.jboss.org/wiki/richfaceswikihomepage

To report or check the status of issues related to RichFaces use the
project's Jira at:

    - https://issues.jboss.org/browse/RF

Community support and help can be obtained from the RichFaces forums at:

    - http://community.jboss.org/en/richfaces

Also you can follow us on Twitter at:

    - http://twitter.com/richfaces


FUTURE RELEASES
---------------

For information on future releases and new features that are planned for next
version please visit project's wiki at:
http://community.jboss.org/wiki/RichFaces410ReleaseCenter


GET INVOLVED
------------

RichFaces is an Open Source project built by people across the globe.
If you want to help create the next version of RichFaces take a look at
this sites

    - RichFaces source code:
      http://www.jboss.org/richfaces/sourcecode

    - How to build RichFaces 4.X:
      http://community.jboss.org/docs/DOC-15747?uniqueTitle=false

    - RichFaces 4.X Directory structure:
      http://community.jboss.org/wiki/RichFaces40BuildDirectoryStructure

    - RichFaces developer forums
      http://community.jboss.org/en/richfaces/dev?view=discussions

    - RichFaces Planning (Jira):
      https://issues.jboss.org/browse/RFPL

We hope to see your contributions!


AVAILABLE COMPONENTS/BEHAVIORS/TAGS/FUNCTIONS
---------------------------------------------
Core:
---------------------------------------------
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
rich:hotKey

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
rich:notify
rich:chart

---------------------------------
Menus:
---------------------------------
rich:toolbar
rich:toolbarGroup
rich:contextMenu
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
rich:inputNumberSlider
rich:inputNumberSpinner
rich:calendar
rich:fileUpload
rich:editor

---------------------------------
Select:
---------------------------------
rich:select
rich:inplaceSelect
rich:orderingList
rich:pickList

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
