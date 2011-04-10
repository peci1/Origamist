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

	<!-- Move <startPoint> and <endPoint> tags into one <line> tag. -->
	<xsl:template match="operation[startPoint and endPoint]">
		<xsl:element name="operation">
			<!-- Copy attributes. -->
			<xsl:copy-of select="@*"/>
			<!-- Copy all sub-elements except for <startPoint> and <endPoint>. -->
	    	<xsl:apply-templates select="*[not(local-name() = 'startPoint') and not(local-name() = 'endPoint')]" />
	    	
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
		</xsl:element>
	</xsl:template>

	<!-- Copy all other elements as they are. -->
    <xsl:template match="node()">
    	<xsl:element name="{name()}">
			<xsl:copy-of select="@*"/>
        	<xsl:apply-templates select="node()"/>
      	</xsl:element>
    </xsl:template>

   	<xsl:template match="text()">
     	<xsl:value-of select="." />
   	</xsl:template>
   	
</xsl:stylesheet>