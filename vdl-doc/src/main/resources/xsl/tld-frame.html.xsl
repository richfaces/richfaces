<?xml version="1.0" encoding="UTF-8" ?>
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
    Document   : tld-frame.html.xsl
    Created on : December 18, 2002, 11:40 AM
    Author     : mroth
    Description:
        Creates the TLD frame (lower-left hand corner), listing the tags
        and functions that are in this particular tag library.
-->

<xsl:stylesheet version="1.0"
    xmlns:javaee="http://java.sun.com/xml/ns/javaee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="html" indent="yes" name="html"/>

    <!--<xsl:param name="output-dir" />-->
    <!--<xsl:variable name="window-title">-->
    	<!--<xsl:value-of select="properties/window-title"/>-->
    <!--</xsl:variable>-->


    <!--<xsl:template match="/">-->
        <!--<xsl:for-each select="/properties/taglibs/taglib">-->
            <!--<xsl:apply-templates select="document(path)/javaee:facelet-taglib">-->
                <!--<xsl:with-param name="display-name" select="display-name" />-->
                <!--<xsl:with-param name="description" select="description" />-->
            <!--</xsl:apply-templates>-->
        <!--</xsl:for-each>-->
    <!--</xsl:template>-->
    
    <xsl:template name="tldframe" match="javaee:facelet-taglib">
        <xsl:param name="display-name" />
        <xsl:param name="description" />
        <xsl:param name="short-name" />
        <xsl:variable name="taglibname">
          <xsl:choose>
            <xsl:when test="$display-name!=''">
              <xsl:value-of select="$display-name"/>
            </xsl:when>
            <xsl:otherwise>
              Unnamed TLD
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="taglibfull">
          <xsl:value-of select="$taglibname"/>
          <xsl:choose>
            <xsl:when test="$description!=''">
              (<xsl:value-of select="$description" disable-output-escaping="yes"/>)
            </xsl:when>
            <xsl:otherwise>
              No Description
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="filename"
                          select="concat($output-dir,'/', $short-name, '/tld-frame.html')"/>
        <xsl:value-of select="$filename"/>

        <xsl:result-document href="{$filename}" format="html">
        <html>
          <head>
            <title>
              <xsl:value-of select="$taglibfull"/>
            </title>
            <meta name="keywords" content="$taglibname"/>
            <link rel="stylesheet" type="text/css" href="../css/stylesheet.css"
                  title="Style"/>
            <script>
              function asd()
              {
              parent.document.title="<xsl:value-of select="normalize-space($taglibfull)"/>";
              }
            </script>
          </head>
          <body bgcolor="white" onload="asd();">
            <font size="+1" class="FrameTitleFont">
              <a href="tld-summary.html" target="tagFrame">
                <xsl:value-of select="$taglibname"/>
              </a>
            </font>
              <table border="0" width="100%">
              <xsl:if test="(count(javaee:tag))>0">
                <tr>
                  <td nowrap="true">
                    <font size="+1" class="FrameHeadingFont">
                      Tags
                    </font>&#160;
                    <font class="FrameItemFont">
                      <!--<xsl:apply-templates select="javaee:tag|javaee:tag-file"/>-->
                        <!--<xsl:apply-templates select="javaee:tag"/>-->
                        <xsl:for-each select="javaee:tag">
                            <br/>
                              <xsl:element name="a">
                                <xsl:attribute name="href"><xsl:value-of select="javaee:tag-name"/>.html</xsl:attribute>
                                <xsl:attribute name="target">tagFrame</xsl:attribute>
                                <xsl:value-of select="../@id"/>:<xsl:value-of select="javaee:tag-name"/>
                              </xsl:element>
                        </xsl:for-each>
                    </font>
                  </td>
                </tr>
              </xsl:if>
              <xsl:if test="count(javaee:function)>0">
                <tr>
                  <td nowrap="true">
                    <font size="+1" class="FrameHeadingFont">
                      Functions
                    </font>&#160;
                      <font class="FrameItemFont">
                          <!--<xsl:apply-templates select="javaee:function"/>-->
                          <xsl:for-each select="javaee:function">
                                                <br/>
                          <xsl:element name="a">
                              <xsl:attribute name="href"><xsl:value-of select="javaee:function-name"/>.fn.html</xsl:attribute>
                              <xsl:attribute name="target">tagFrame</xsl:attribute>
                              <i><xsl:value-of select="../@id"/>:<xsl:value-of select="javaee:function-name"/>()</i>
                          </xsl:element>

                          </xsl:for-each>

                      </font>
                  </td>
                </tr>
              </xsl:if>

              </table>
            <!-- <table ... -->
          </body>
        </html>
        </xsl:result-document>
    </xsl:template>

     <!--<xsl:template name="tldframe-tag" match="javaee:tag">-->
      <!---->
    <!--</xsl:template>-->

    <!--<xsl:template name="tldframe-function" match="javaee:function">-->
      <!---->
    <!--</xsl:template>-->

</xsl:stylesheet> 
