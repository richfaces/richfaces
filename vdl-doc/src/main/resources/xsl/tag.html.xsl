<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (c) 2003-2004,-2010 Oracle and/or its affiliates. All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

      - Redistributions of source code must retain the above copyright
        notice, this list of conditions and the following disclaimer.

      - Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

      - Neither the name of Oracle nor the names of its
        contributors may be used to endorse or promote products derived
        from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
    IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
    THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
    PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
    CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-->

<!--
    Document   : tag.html.xsl
    Created on : December 18, 2002, 5:22 PM
    Author     : mroth
    Description:
        Creates the tag detail page (right frame), listing the known
        information for a given tag in a tag library.
-->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:javaee="http://java.sun.com/xml/ns/javaee"
        version="2.0">

    <xsl:output method="html" indent="yes" name="html"/>

    <!--<xsl:param name="output-dir"/>-->

    <!--<xsl:variable name="window-title">-->
        <!--<xsl:value-of select="properties/window-title"/>-->
    <!--</xsl:variable>-->

    <!--<xsl:template match="/">-->
        <!--<xsl:for-each select="/properties/taglibs/taglib">-->
            <!--&lt;!&ndash;<xsl:apply-templates name="tag" select="document(path)/javaee:facelet-taglib"/>&ndash;&gt;-->
            <!--<xsl:call-template name="tag" />-->
        <!--</xsl:for-each>-->
    <!--</xsl:template>-->

    <xsl:template name="tag" match="javaee:facelet-taglib">
         <xsl:param name="short-name" />
        <xsl:variable name="namespace" select="$short-name"/>

        <xsl:for-each select="//javaee:tag">
            <xsl:variable name="tagname">
                <xsl:value-of select="javaee:tag-name"/>
            </xsl:variable>
            <xsl:variable name="filename"
                          select="concat($output-dir,'/',$namespace,'/',javaee:tag-name,'.html')"/>
            <!--<xsl:value-of select="$filename"/>-->
            <!-- Creating  -->
            <xsl:result-document href="{$filename}" format="html">
                <!--<xsl:apply-templates select=".">-->
                    <!---->
                <!--</xsl:apply-templates>-->
                <xsl:call-template name="tag-in-detail">
                    <xsl:with-param name="namespace" select="$namespace"/>
                    <xsl:with-param name="tagname" select="$tagname"/>
                </xsl:call-template>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="tag-in-detail" match="javaee:tag">
        <xsl:param name="namespace"/>
        <xsl:param name="tagname"/>
        <xsl:variable name="title">
          <xsl:value-of select="$tagname"/>
          (<xsl:value-of select="$window-title"/>)
        </xsl:variable>
        <html>
            <head>
                <title>
                    <xsl:value-of select="$title"/>
                </title>
                <meta name="keywords" content="$tagname"/>
                <link rel="stylesheet" type="text/css" href="../css/stylesheet.css"
                      title="Style"/>
            </head>
            <script>
                function asd()
                {
                parent.document.title="<xsl:value-of select="normalize-space($title)"/>";
                }
            </script>
            <body bgcolor="white" onload="asd();">
                <!-- =========== START OF NAVBAR =========== -->
                <a name="navbar_top"><!-- --></a>
                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                    <tr>
                        <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                            <a NAME="navbar_top_firstrow"><!-- --></a>
                            <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                                <tr ALIGN="center" VALIGN="top">
                                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;
                                        <a href="../overview-summary.html">
                                            <font CLASS="NavBarFont1">
                                                <b>Overview</b>
                                            </font>
                                        </a>
                                        &#160;
                                    </td>
                                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;
                                        <a href="tld-summary.html">
                                            <font CLASS="NavBarFont1">
                                                <b>Library</b>
                                            </font>
                                        </a>
                                        &#160;
                                    </td>
                                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &#160;<font
                                            CLASS="NavBarFont1Rev">&#160;Tag&#160;</font>&#160;
                                    </td>
                                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;
                                        <a HREF="../help-doc.html">
                                            <font CLASS="NavBarFont1">
                                                <b>Help</b>
                                            </font>
                                        </a>
                                        &#160;
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td ALIGN="right" VALIGN="top" ROWSPAN="3">
                            <em>
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td BGCOLOR="white" CLASS="NavBarCell2">
                            <font SIZE="-2">
                                <!--&#160;PREV TAGLIB&#160;-->
                                <!--&#160;NEXT TAGLIB&#160;-->
                            </font>
                        </td>
                        <td BGCOLOR="white" CLASS="NavBarCell2">
                            <font SIZE="-2">
                                &#160;
                                <a HREF="../index.html" TARGET="_top">
                                    <b>FRAMES</b>
                                </a>
                                &#160;
                                &#160;
                                <xsl:element name="a">
                                    <xsl:attribute name="href"><xsl:value-of select="javaee:tag-name"/>.html</xsl:attribute>
                                    <xsl:attribute name="target">_top</xsl:attribute>
                                    <b>NO FRAMES</b>
                                </xsl:element>
                                &#160;
                                <script>
                                    <!--
                                    if(window==top) {
                                      document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>All Tags</B></A>');
                                    }
                                    //-->
                                </script>
                                <noscript>
                                    <a HREF="../alltags-noframe.html" TARGET="">
                                        <b>All Tags</b>
                                    </a>
                                </noscript>
                            </font>
                        </td>
                    </tr>
                </table>
                <!-- =========== END OF NAVBAR =========== -->

                <hr/>
                <h2>
                    <font size="-1">
                        <xsl:value-of select="$namespace"/>
                    </font>
                    <br/>
                    Tag
                    <xsl:value-of select="javaee:tag-name"/>
                </h2>
                <hr/>
                <xsl:choose>
                    <xsl:when test="javaee:component">

                        <xsl:value-of select="javaee:component/javaee:description" disable-output-escaping="yes"/>
                        <br/>
                        <p/>
                        <hr/>

                        <!-- Tag Information -->
                        <table border="1" cellpadding="3" cellspacing="0" width="100%">
                            <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                                <td colspan="2">
                                    <font size="+2">
                                        <b>Tag Information</b>
                                    </font>
                                </td>
                            </tr>


                            <tr>
                                <td>Component type</td>
                                <td>
                                    <xsl:variable name="component-type">
                                        <xsl:value-of select="javaee:component/javaee:component-type"/>
                                    </xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="$component-type!=''">
                                            <xsl:value-of select="$component-type"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <i>None</i>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            <tr>
                                <td>Tag Name</td>
                                <td>
                                    <xsl:variable name="fulltagname" select="concat($namespace,':',$tagname)"/>
                                    <xsl:value-of select="$fulltagname"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Renderer Type</td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="javaee:component/javaee:renderer-type!=''">
                                            <xsl:value-of select="javaee:component/javaee:renderer-type"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <i>None</i>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            <tr>
                                <td>Handler Class</td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="javaee:component/javaee:handler-class!=''">
                                            <xsl:value-of select="javaee:component/javaee:handler-class"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <i>None</i>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </table>
                        
   
                        <!--xsl:choose>
                            <xsl:when test="javaee:component/javaee:component-extension/cdk:extension/cdk:facets">
                                <table border="1" cellpadding="3" cellspacing="0" width="100%">
                                    <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                                        <td colspan="5">
                                            <font size="+2">
                                                <b>Supported Facets</b>
                                            </font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="5"><xsl:value-of select="javaee:component/javaee:component-extension/cdk:extension/cdk:facets" /></td>
                                    </tr>
                                </table>
                                <br/>
                                <p/>
                            </xsl:when>
                        </xsl:choose-->
                        
                    </xsl:when>
                    <xsl:when test="javaee:behavior">
                        <xsl:value-of select="javaee:behavior/javaee:description" disable-output-escaping="yes"/>
                        <br/>
                        <p/>
                        <hr/>

                        <!-- Tag Information -->
                        <table border="1" cellpadding="3" cellspacing="0" width="100%">
                            <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                                <td colspan="2">
                                    <font size="+2">
                                        <b>Tag Information</b>
                                    </font>
                                </td>
                            </tr>


                            <tr>
                                <td>Handler Id</td>
                                <td>
                                    <xsl:variable name="handler-id">
                                        <xsl:value-of select="javaee:behavior/javaee:behavior-id"/>
                                    </xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="$handler-id!=''">
                                            <xsl:value-of select="$handler-id"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <i>None</i>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>

                            <tr>
                                <td>Handler Class</td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="javaee:behavior/javaee:handler-class!=''">
                                            <xsl:value-of select="javaee:behavior/javaee:handler-class"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <i>None</i>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </table>
                    </xsl:when>
                    <xsl:when test="javaee:handler-class">
                        <table border="1" cellpadding="3" cellspacing="0" width="100%">
                           <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                                <td colspan="2">
                                    <font size="+2">
                                        <b>Tag Information</b>
                                    </font>
                                </td>
                            </tr>

                            <tr>
                              <td>Handler Class</td>
                                <td>
                                    <xsl:value-of select="javaee:handler-class"/>
                                </td>
                            </tr>
                        </table>
                    </xsl:when>
                </xsl:choose>
                <br/>
                <p/>               

                <table border="1" cellpadding="3" cellspacing="0" width="100%">
                    <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                        <td colspan="5">
                            <font size="+2">
                                <b>Attributes</b>
                            </font>
                        </td>
                    </tr>
                    <xsl:choose>
                        <xsl:when test="count(javaee:attribute)>0">
                            <tr>
                                <td>
                                    <b>Name</b>
                                </td>
                                <td>
                                    <b>Required</b>
                                </td>

                                <td>
                                    <b>Type</b>
                                </td>
                                <td>
                                    <b>Description</b>
                                </td>
                            </tr>
                            <xsl:apply-templates select="javaee:attribute"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <td colspan="5">
                                <i>No Attributes Defined.</i>
                            </td>
                        </xsl:otherwise>
                    </xsl:choose>
                </table>
                <br/>
                <p/>


                <!-- =========== START OF NAVBAR =========== -->
                <a name="navbar_bottom"><!-- --></a>
                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                    <tr>
                        <td COLSPAN="3" BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
                            <a NAME="navbar_bottom_firstrow"><!-- --></a>
                            <table BORDER="0" CELLPADDING="0" CELLSPACING="3">
                                <tr ALIGN="center" VALIGN="top">
                                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;
                                        <a href="../overview-summary.html">
                                            <font CLASS="NavBarFont1">
                                                <b>Overview</b>
                                            </font>
                                        </a>
                                        &#160;
                                    </td>
                                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;
                                        <a href="tld-summary.html">
                                            <font CLASS="NavBarFont1">
                                                <b>Library</b>
                                            </font>
                                        </a>
                                        &#160;
                                    </td>
                                    <td BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &#160;<font
                                            CLASS="NavBarFont1Rev">&#160;Tag&#160;</font>&#160;
                                    </td>
                                    <td BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    &#160;
                                        <a HREF="../help-doc.html">
                                            <font CLASS="NavBarFont1">
                                                <b>Help</b>
                                            </font>
                                        </a>
                                        &#160;
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td ALIGN="right" VALIGN="top" ROWSPAN="3">
                            <em>
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td BGCOLOR="white" CLASS="NavBarCell2">
                            <font SIZE="-2">
                                <!--&#160;PREV TAGLIB&#160;-->
                                <!--&#160;NEXT TAGLIB&#160;-->
                            </font>
                        </td>
                        <td BGCOLOR="white" CLASS="NavBarCell2">
                            <font SIZE="-2">
                                &#160;
                                <a HREF="../index.html" TARGET="_top">
                                    <b>FRAMES</b>
                                </a>
                                &#160;
                                &#160;
                                <xsl:element name="a">
                                    <xsl:attribute name="href"><xsl:value-of select="javaee:tag-name"/>.html</xsl:attribute>
                                    <xsl:attribute name="target">_top</xsl:attribute>
                                    <b>NO FRAMES</b>
                                </xsl:element>
                                &#160;
                                <script>
                                    <!--
                                    if(window==top) {
                                      document.writeln('<A HREF="alltags-noframe.html" TARGET=""><B>All Tags</B></A>');
                                    }
                                    //-->
                                </script>
                                <noscript>
                                    <a HREF="../alltags-noframe.html" TARGET="">
                                        <b>All Tags</b>
                                    </a>
                                </noscript>
                            </font>
                        </td>
                    </tr>
                </table>
                <!-- =========== END OF NAVBAR =========== -->
                <hr/>
                <small>
                    <i>
                        Output Generated by
                        <a href="http://taglibrarydoc.dev.java.net/" target="_blank">Tag Library Documentation
                            Generator</a>.
                    </i>
                </small>
            </body>
        </html>

    </xsl:template>

    <xsl:template match="javaee:attribute">
        <tr valign="top">
            <td>
                <xsl:apply-templates select="javaee:name"/>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="javaee:required!=''">
                        <xsl:value-of select="javaee:required"/>
                    </xsl:when>
                    <xsl:otherwise>false</xsl:otherwise>
                </xsl:choose>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="javaee:type!=''">
                        <code>
                            <xsl:value-of select="javaee:type"/>
                        </code>
                    </xsl:when>
                    <xsl:otherwise>
                        <code>java.lang.String</code>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="javaee:description!=''">
                        <xsl:value-of select="javaee:description" disable-output-escaping="yes"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <i>No Description</i>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>