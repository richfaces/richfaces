<xsl:stylesheet version="1.0"
    xmlns:javaee="http://java.sun.com/xml/ns/javaee"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:import href="help-doc.html.xsl" />
    <xsl:import href="index.html.xsl" />
    <xsl:import href="overview-frame.html.xsl" />
    <xsl:import href="overview-summary.html.xsl"/>
    <xsl:import href="tag.html.xsl" />
    <xsl:import href="tld-frame.html.xsl" />
    <xsl:import href="tld-summary.html.xsl" />
    <xsl:import href="alltags.xsl"/>
    <xsl:import href="function.html.xsl" />

    <xsl:param name="output-dir" />

    <xsl:variable name="window-title">
    	<xsl:value-of select="properties/window-title"/>
    </xsl:variable>

    <xsl:template match="/">

        <xsl:call-template name="overview-summary" />
        <xsl:call-template name="overview-frame"/>
        <xsl:call-template name="index"/>
        <xsl:call-template name="help-doc"/>
        <xsl:call-template name="alltags" />
        <xsl:for-each select="/properties/taglibs/taglib">
            <xsl:apply-templates select="document(path)/javaee:facelet-taglib">
                <xsl:with-param name="display-name" select="display-name"/>
                <xsl:with-param name="description" select="description"/>
                <xsl:with-param name="short-name" select="@short-name"/>
            </xsl:apply-templates>
        </xsl:for-each>
    </xsl:template>
    <xsl:template match="javaee:facelet-taglib">
        <xsl:param name="display-name"/>
        <xsl:param name="description"/>
        <xsl:param name="short-name"/>
        <xsl:call-template name="tldframe">
            <xsl:with-param name="display-name" select="$display-name"/>
            <xsl:with-param name="description" select="$description"/>
            <xsl:with-param name="short-name" select="$short-name"/>
        </xsl:call-template>
        <xsl:call-template name="tldsummary">
            <xsl:with-param name="display-name" select="$display-name"/>
            <xsl:with-param name="description" select="$description"/>
            <xsl:with-param name="short-name" select="$short-name"/>
        </xsl:call-template>
        <xsl:call-template name="tag">
            <!--<xsl:with-param name="display-name" select="display-name" />-->
            <!--<xsl:with-param name="description" select="description" />-->
            <xsl:with-param name="short-name" select="$short-name"/>
        </xsl:call-template>
        <xsl:call-template name="function-tag" />
    </xsl:template>
</xsl:stylesheet>