<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:new="http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2" 
	xmlns:old="http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v1"
	exclude-result-prefixes="old new">
	
	<xsl:output method="xml" indent="yes"/>

	<!-- Change the namespace of the root element to the new version. -->
	<xsl:template match="old:origami">
		<m:origami xmlns:m="http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2" 
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
			xsi:schemaLocation="http://www.mff.cuni.cz/~peckam/java/origamist/diagram/v2 diagram_v2.xsd">
		<xsl:apply-templates />
		</m:origami>
	</xsl:template>

	<xsl:template name="getNewOperationName">
		<xsl:param name="type" />
		<xsl:choose>
			<xsl:when test="$type='MOUNTAIN_FOLD' or $type='VALLEY_FOLD'">
				<xsl:value-of select="'foldOperation'" />
			</xsl:when>
			<xsl:when test="$type='MOUNTAIN_VALLEY_FOLD_UNFOLD' or $type='VALLEY_MOUNTAIN_FOLD_UNFOLD'">
				<xsl:value-of select="'foldUnfoldOperation'" />
			</xsl:when>
			<xsl:when test="$type='INSIDE_REVERSE_FOLD' or $type='OUTSIDE_REVERSE_FOLD'">
				<xsl:value-of select="'reverseFoldOperation'" />
			</xsl:when>
			<xsl:when test="$type='ROTATE'">
				<xsl:value-of select="'rotateOperation'" />
			</xsl:when>
			<xsl:when test="$type='TURN_OVER'">
				<xsl:value-of select="'turnOverOperation'" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- Move <startPoint> and <endPoint> tags into one <line> tag. -->
	<xsl:template match="operation[startPoint and endPoint]">
		<xsl:variable name="newName">
			<xsl:call-template name="getNewOperationName">
				<xsl:with-param name="type" select="@type"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="{$newName}">
			<!-- Copy attributes. -->
			<xsl:copy-of select="@*"/>
			
			<!-- Copy all sub-elements before <startPoint> and <endPoint>. -->
	    	<xsl:apply-templates select="angle" />
	    	
	      	<line>
	      		<start>
	      			<x><xsl:value-of select="startPoint/x"/></x>
	      			<y><xsl:value-of select="startPoint/y"/></y>
	      		</start>
	      		<end>
	      			<x><xsl:value-of select="endPoint/x"/></x>
	      			<y><xsl:value-of select="endPoint/y"/></y>
	      		</end>
	      	</line>
	      	
	      	<!-- Copy all sub-elements after <startPoint> and <endPoint>. -->
	    	<xsl:apply-templates select="layer" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="operation">
		<xsl:variable name="newName">
			<xsl:call-template name="getNewOperationName">
				<xsl:with-param name="type" select="@type"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="{$newName}">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates select="*"/>
		</xsl:element>
	</xsl:template>

	<!-- Copy all other elements as they are (identity template). -->
    <xsl:template match="@*|node()">
  		<xsl:copy>
    		<xsl:apply-templates select="@*|node()"/>
  		</xsl:copy>
	</xsl:template>
   	
</xsl:stylesheet>