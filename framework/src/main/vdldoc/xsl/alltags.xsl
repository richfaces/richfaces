<?xml version="1.0" encoding="UTF-8" ?>

<!--
  - <license>
  - Copyright (c) 2003-2004, Sun Microsystems, Inc.
  - All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are met:
  -
  -     * Redistributions of source code must retain the above copyright
  -       notice, this list of conditions and the following disclaimer.
  -     * Redistributions in binary form must reproduce the above copyright
  -       notice, this list of conditions and the following disclaimer in the
  -       documentation and/or other materials provided with the distribution.
  -     * Neither the name of Sun Microsystems, Inc. nor the names of its
  -       contributors may be used to endorse or promote products derived from
  -       this software without specific prior written permission.
  -
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  - "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
  - TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
  - PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
  - CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  - EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  - ROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  - PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  - LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  - NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  - SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  - </license>
  -->
<xsl:stylesheet version="1.0" xmlns:javaee="http://java.sun.com/xml/ns/javaee"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">

     <xsl:output method="html" indent="yes"/>
    <xsl:param name="output-dir" />

    <xsl:template name="alltags" match="/">
         <xsl:variable name="alltags-frame-filename"
                                  select="concat($output-dir,'/alltags-frame.html')"/>
        <xsl:variable name="alltags-noframe-filename"
                                  select="concat($output-dir,'/alltags-noframe.html')"/>
        <xsl:result-document href="{$alltags-frame-filename}" format="html">

                    <html>
                      <head>
                        <title>All Tags / Functions</title>
                        <link rel="stylesheet" type="text/css" href="css/stylesheet.css" title="Style"/>
                      </head>
                      <script>
                        function asd()
                        {
                          parent.document.title="All Tags / Functions";
                        }
                      </script>
                      <body bgcolor="white" onload="asd();">
                        <font size="+1" class="FrameHeadingFont">
                        <b>All Tags / Functions</b></font>
                        <br/>
                        <table border="0" width="100%">
                          <tr>
                            <td nowrap="true"><font class="FrameItemFont">
                                <xsl:for-each select="/properties/taglibs/taglib">
                                  <xsl:apply-templates
                                      select="document(path)//javaee:tag|document(path)//javaee:function" >
                                      <xsl:with-param name="frame">true</xsl:with-param>
                                      <xsl:sort select="/javaee:facelet-taglib/@id"/>
                                    <xsl:sort select="javaee:tag-name"/>
                                    <xsl:sort select="javaee:function-name"/>
                                  </xsl:apply-templates>
                                </xsl:for-each>
                            </font></td>
                          </tr>
                        </table>
                      </body>
                    </html>


                </xsl:result-document>
        <xsl:result-document href="{$alltags-noframe-filename}" format="html">

            <html>
              <head>
                <title>All Tags / Functions</title>
                <link rel="stylesheet" type="text/css" href="css/stylesheet.css" title="Style"/>
              </head>
              <script>
                function asd()
                {
                  parent.document.title="All Tags / Functions";
                }
              </script>
              <body bgcolor="white" onload="asd();">
                <font size="+1" class="FrameHeadingFont">
                <b>All Tags / Functions</b></font>
                <br/>
                <table border="0" width="100%">
                  <tr>
                    <td nowrap="true"><font class="FrameItemFont">
                        <xsl:for-each select="/properties/taglibs/taglib">
                          <xsl:apply-templates
                              select="document(path)//javaee:tag|document(path)//javaee:function">
                              <xsl:with-param name="frame">false</xsl:with-param>
                            <xsl:sort select="/javaee:facelet-taglib/@id"/>
                            <xsl:sort select="javaee:tag-name"/>
                            <xsl:sort select="javaee:function-name"/>
                          </xsl:apply-templates>
                        </xsl:for-each>
                    </font></td>
                  </tr>
                </table>
              </body>
            </html>

                </xsl:result-document>

    </xsl:template>
       <xsl:template name="alltag-frame-tag" match="javaee:tag">
        <xsl:param name="frame">false</xsl:param>

           <xsl:element name="a">
               <xsl:attribute name="href"><xsl:value-of select="/javaee:facelet-taglib/@id"/>/<xsl:value-of
                       select="javaee:tag-name"/>.html</xsl:attribute>
               <xsl:choose>
                   <xsl:when test="$frame='true'">
                         <xsl:attribute name="target">tagFrame</xsl:attribute>
                   </xsl:when>
                   <xsl:otherwise>
                       <xsl:attribute name="target"></xsl:attribute>
                   </xsl:otherwise>
               </xsl:choose>

               <xsl:value-of select="/javaee:facelet-taglib/@id"/>:<xsl:value-of select="javaee:tag-name"/>
           </xsl:element>
                <br/>
    </xsl:template>

    <!--
      - Same as above, but add the () to indicate it's a function
      - and change the HTML to .fn.html
      -->
    <xsl:template name="alltag-frame-function" match="javaee:function">
        <xsl:param name="frame">false</xsl:param>
                 <xsl:element name="a">
        <xsl:attribute name="href"><xsl:value-of select="/javaee:facelet-taglib/@id"/>/<xsl:value-of select="javaee:function-name"/>.fn.html</xsl:attribute>
        <xsl:choose>
            <xsl:when test="$frame='true'">
                     <xsl:attribute name="target">tagFrame</xsl:attribute>
                      </xsl:when>
            <xsl:otherwise>
                  <xsl:attribute name="target"></xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
        <i><xsl:value-of select="/javaee:facelet-taglib/@id"/>:<xsl:value-of select="javaee:function-name"/>()</i>
      </xsl:element>
      <br/>



    </xsl:template>




</xsl:stylesheet>