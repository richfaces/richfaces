========================================================================

                      RichFaces - Ajax enabled JSF 2.0 component library

                                                   RichFaces 4.0.0.Final
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
    - Complete JSF2 support with advanced extentions
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

    Search for the 'artifacts' directory in the extracted files. There you
    will find 'framework' and 'ui' directories, each of them having the 
    jars needed to use RichFaces inside your project.
    
        - From 'ui' directory: Copy richfaces-components-api-4.x.jar and 
          richfaces-components-ui-4.x.jar to your application libraries 
          folder.

        - From 'framework' directory: Copy richfaces-core-api-4.x.jar and
          richfaces-core-impl-4.x.jar to your application libraries folder.
    
    In addition to the RichFaces jars, a number of dependencies are 
    required in order to properly configure the framework.
       
      
      a.  sac-1.3, cssparser-0.9.5 - required for components CSS work.

      b.  google-guava-r08 - core runtime dependency.

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

   - Use xmlns:a4j="http://richfaces.org/a4j" namespace for core components
   - Use xmlns:rich="http://richfaces.org/rich" namespace for rich components

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
http://www.jboss.org/community/wiki/RichFaces40Planning


GET INVOLVED
------------

RichFaces is an Open Source project built by people across the globe. 
If you want to help create the next version of RichFaces take a look at 
this sites

    - RichFaces source code:
      http://www.jboss.org/richfaces/sourcecode

    - How to build RichFaces 4.0: 
      http://community.jboss.org/docs/DOC-15747?uniqueTitle=false
    
    - RichFaces 4.0 Directory structure:
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


New in Version 4.0.Final
----------------------

    - Minor, low risk stabilizations, and clean up tasks for the Final build to minimize risk.
    - Thorough review of all the component attributes done and corrections were made based on complexity. 
    - Bug fixing for CSV and Object Validation features. 
    - Refactored Listeners classes, methods and Event names according to standard JSF convention.
      https://issues.jboss.org/browse/RF-10712
    - Review and update predefined rf-* CSS classes for components to satisfy naming convention 
      https://issues.jboss.org/browse/RF-9290
    - TreeNode and TreeDataModel model support for rich:tree added.
      https://issues.jboss.org/browse/RF-9718
    - jQuery updated to 1.5.1. https://issues.jboss.org/browse/RF-10686
    - Several taglib issues found during QE and made corresponding corrections for better IDE support 
      https://issues.jboss.org/browse/RF-9936
    - RichFaces showcase design corrections 
    - Reviewed current state of RichFaces showcase in different JSF environments support and 
      finalized pom.xml profiles for: 
          richfaces-showcase (Mojara 2.0.3 - GAE build, Mojara 2.0.4 default build) 
          richfaces-archetype-simpleapp (Mojara 2.0.4 default build)
          rf-gae-sample archetype (MyFaces 2.0.4 both GAE and default builds)
    - Completed Push component demo - irc-sample, with blog and documents before release


New in Version 4.0.CR1
----------------------

    - Completed nearly all taglib's corrections for better IDE support 
      https://issues.jboss.org/browse/RF-9936
    - Stabilized the CSV feature with messages, localization and customization 
      improvements https://issues.jboss.org/browse/RF-10556
      https://issues.jboss.org/browse/RF-10434 https://issues.jboss.org/browse/RF-10611
    - Stabilized messages components https://issues.jboss.org/browse/RF-10293 
      https://issues.jboss.org/browse/RF-10370
    - A lots of stabilization issues resolved for Switchable Panels and 
      Panel Menu
    - Important push problems solved https://issues.jboss.org/browse/RF-10457 
      https://issues.jboss.org/browse/RF-10473 https://issues.jboss.org/browse/RF-10487 
      and more customization options added https://issues.jboss.org/browse/RF-10454
    - A lot of stabilization issues for MyFaces support got resolved
    - Environment updates: JQuery 1.5, MyFaces 2.0.4, Mojara 2.0.4, Atmosphere 0.6.4
    - RichFaces showcase has been updated with new design (https://issues.jboss.org/browse/RF-9636) 
      and now runs stable with MyFaces!


New in Version 4.0.M6
---------------------

    - Client Side Validation feature is ready!! All the standard JSF
      and most of the JSR-303 validators are available on the client by using 
      just <rich:validator> behavior! We will post detailed blog with examples 
      in a few days.
    - ObjectValidation feature also was finalized and allows you to validate 
      complete objects and dependent fields by just using JSR-303 validators 
      and the <rich:graphValidator> component. This will also be covered in an
      up coming blog.
    - The Ajax framework is improved and now provides ignoreDupResponce core 
      feature! Together with queue it adds great optimization to your Ajax'ififed
      pages.
    - All the richfaces components client side API are reviewed and stabilized. 
      API Tables will be documented soon in the component reference.
    - Further review and corrections in components for improving IDE's support.
    - RichFaces showcase demo application is updated with new showcases for 
      validation components and new examples for previously released components.
    - Further improvements in Component Development Kit.
    - Stabilization works for migrated code. Automation tests with quint, junit. 
      Review of attributes and facets for consistency and stability are done 
      and most issues get fixed.
      

###
