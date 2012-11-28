<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
<html>

   <head>
      <title>Assignment</title>
      <style type="text/css">
        table {
            font-family: verdana;
            text-align:center;
        }

          
          
          .dayTable{
          margin-bottom:10px;
          margin-top:50px;
          
          }
          .dayTD{
          	background:rgb(158,180,254);
          <!--	color:rgb(103,99,165); -->
          color:blue;
          }
          .parcourTD{
          	background:rgb(171,190,254);
           <!--	color:rgb(103,99,165); -->
          	 color:blue;
          }
          .postTR{
          	background:rgb(185,201,254);
           <!--	color:rgb(121,159,232); -->
          	 color:blue;
          }
          
          .studentTR{
          	background:rgb(229,235,235);
          	color:rgb(137,120,164);
          }
          
          <!--  /* Color Shade */-->
		.accordion-title-selectedcolor_1 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#f6d4a3', endColorstr='#fdf0dd'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#f6d4a3), to(#fdf0dd)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #f6d4a3, #fdf0dd) !important; /* for firefox 3.6+ */	
		}
		.accordion-title-selectedcolor_2 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#eeee8c', endColorstr='#fcfcd1'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#eeee8c), to(#fcfcd1)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #eeee8c, #fcfcd1) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_3 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fadce7', endColorstr='#f9eaea'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#fadce7), to(#f9eaea)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #fadce7, #f9eaea) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_4 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#cfe3d4', endColorstr='#e8f5eb'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#cfe3d4), to(#e8f5eb)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #cfe3d4, #e8f5eb) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_5 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d1cee1', endColorstr='#e9e8f4'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#d1cee1), to(#e9e8f4)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #d1cee1, #e9e8f4) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_6 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#f2cec6', endColorstr='#fcf4f2'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#f2cec6), to(#fcf4f2)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #f2cec6, #fcf4f2) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_7 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#e5d9c9', endColorstr='#fffbf5'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#e5d9c9), to(#fffbf5)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #e5d9c9, #fffbf5) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_8 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#c4dbfe', endColorstr='#f3f7fd'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#c4dbfe), to(#f3f7fd)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #c4dbfe, #f3f7fd) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_9 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ccccff', endColorstr='#e6e8fd'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#ccccff), to(#e6e8fd)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #ccccff, #e6e8fd) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_10 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fce0a0', endColorstr='#fcf1d9'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#fce0a0), to(#fcf1d9)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #fce0a0, #fcf1d9) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_11 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#c3f4ad', endColorstr='#e6fcdc'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#c3f4ad), to(#e6fcdc)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #c3f4ad, #e6fcdc) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_12 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ccffff', endColorstr='#ebfefe'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#ccffff), to(#ebfefe)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #ccffff, #ebfefe) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_13 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d7d6d6', endColorstr='#f7f7f7'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#d7d6d6), to(#f7f7f7)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #d7d6d6, #f7f7f7) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_14 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#e1c7fd', endColorstr='#f7f1fe'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#e1c7fd), to(#f7f1fe)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #e1c7fd, #f7f1fe) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_15 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#c1cfcd', endColorstr='#e4e9e8'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#c1cfcd), to(#e4e9e8)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #c1cfcd, #e4e9e8) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_16 {
		 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d1d5a0', endColorstr='#f7f8e5'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#d1d5a0), to(#f7f8e5)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #d1d5a0, #f7f8e5) !important; /* for firefox 3.6+ */
		}
		.accordion-title-selectedcolor_1:hover, .accordion-title-selectedcolor_2:hover, .accordion-title-selectedcolor_3:hover, .accordion-title-selectedcolor_4:hover, .accordion-title-selectedcolor_5:hover, .accordion-title-selectedcolor_6:hover, .accordion-title-selectedcolor_7:hover, .accordion-title-selectedcolor_8:hover, .accordion-title-selectedcolor_9:hover, .accordion-title-selectedcolor_10:hover, .accordion-title-selectedcolor_11:hover, .accordion-title-selectedcolor_12:hover, .accordion-title-selectedcolor_13:hover, .accordion-title-selectedcolor_14:hover, .accordion-title-selectedcolor_15:hover, .accordion-title-selectedcolor_16:hover {
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#c1c1c1', endColorstr='#ededed'); /* for IE */
			background: -webkit-gradient(linear, left top, left bottom, from(#c1c1c1), to(#ededed)) !important; /* for webkit browsers */
			background: -moz-linear-gradient(top, #c1c1c1, #ededed) !important; /* for firefox 3.6+ */
		}
      </style>
   </head>

   <body>
      
         <xsl:for-each select="osceDays/osceDay">
            <table class="dayTable" border="1px" cellSpacing="0" align="center">
            <tr >
           
                  
                  <td bgColor="#9EB4FE"><xsl:value-of select="osceDayID" /></td>
                 
            </tr>
            <xsl:for-each select="parcours/parcour">
           
         
        
     <!--  
	            <tr >
	               
	               <td class="parcourTD"><xsl:value-of select="parcourColor" /></td>
	            </tr>
          -->
	            	<xsl:choose>
       						<xsl:when test="parcourColor = 'color_1'">
					            <tr >
					            <td  bgColor="#f6d4a3"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#f6d4a3"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#f6d4a3"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#f6d4a3"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#f6d4a3"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#f6d4a3"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_2'">
					            <tr >
					            <td  bgColor="#eeee8c"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#eeee8c"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td  bgColor="#eeee8c"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#eeee8c"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#eeee8c"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#eeee8c"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_3'">
					            <tr >
					            <td  bgColor="#fadce7"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fadce7"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td bgColor="#fadce7"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fadce7"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#fadce7"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fadce7"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_4'">
					            <tr >
					            <td  bgColor="#cfe3d4"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#cfe3d4"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td bgColor="#cfe3d4"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#cfe3d4"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#cfe3d4"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#cfe3d4"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_5'">
					            <tr >
					            <td  bgColor="#d1cee1"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#d1cee1"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td bgColor="#d1cee1"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#d1cee1"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#d1cee1"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#d1cee1"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_6'">
					            <tr >
					            <td  bgColor="#f2cec6"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#f2cec6"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td  bgColor="#f2cec6"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#f2cec6"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#f2cec6"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#f2cec6"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_7'">
					            <tr >
					            <td  bgColor="#e5d9c9"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#e5d9c9"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#e5d9c9"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#e5d9c9"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#e5d9c9"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#e5d9c9"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_8'">
					            <tr >
					            <td  bgColor="#c4dbfe"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#c4dbfe"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#c4dbfe"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#c4dbfe"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#c4dbfe"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#c4dbfe"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_9'">
					            <tr >
					            <td  bgColor="#ccccff"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#ccccff"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#ccccff"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ccccff"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#ccccff"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ccccff"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_10'">
					            <tr >
					            <td  bgColor="#fce0a0"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fce0a0"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#fce0a0"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fce0a0"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#fce0a0"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fce0a0"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_11'">
					            <tr >
					            <td  bgColor="#c3f4ad"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#c3f4ad"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#c3f4ad"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#c3f4ad"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#c3f4ad"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#c3f4ad"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_12'">
					            <tr >
					            <td  bgColor="#ccffff"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#ccffff"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#ccffff"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ccffff"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#ccffff"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ccffff"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_13'">
					            <tr >
					            <td  bgColor="#d7d6d6"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#d7d6d6"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#d7d6d6"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#d7d6d6"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#d7d6d6"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#d7d6d6"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_14'">
					            <tr >
					            <td  bgColor="#e1c7fd"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#e1c7fd"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td  bgColor="#e1c7fd"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#e1c7fd"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#e1c7fd"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#e1c7fd"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_15'">
					            <tr >
					            <td  bgColor="#c1cfcd"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#c1cfcd"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#c1cfcd"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#c1cfcd"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#c1cfcd"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#c1cfcd"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_16'">
					            <tr >
					            <td  bgColor="#d1d5a0"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#d1d5a0"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#d1d5a0"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#d1d5a0"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#d1d5a0"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#d1d5a0"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:otherwise>
		            			<tr class="postTR">
					            <td></td>
					              <xsl:for-each select="posts/post">
					               	<td><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td></td>
					              <xsl:for-each select="posts/post">
					               	<td><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:otherwise>
		            </xsl:choose>
		            
		            <xsl:for-each select="rotations/rotation">            
						            <tr>
						               
						               <td><b><xsl:value-of select="rotationId" /></b></td>
						               
						               <xsl:for-each select="examiners/examiner">     
						               		<td><xsl:value-of select="examinerName" /></td>
						               </xsl:for-each>
						            </tr>
				           
				            
						            <xsl:for-each select="startEndTimes/startEndTime">  
						            	
           									   
           									 
           									  	<tr >
           									  		<td bgColor="#E5EBEB"><xsl:value-of select="startEndTimeValue" /></td>
									                
									                <xsl:for-each select="students/student">
									                   <td bgColor="#E5EBEB"><xsl:value-of select="studentName" /></td>
									                </xsl:for-each>
						           			 	</tr>
           									 
          							
						            
						               
									               
						             </xsl:for-each>
           			 </xsl:for-each>
           			 
           			  <tr>
				            	<td >
				            		<xsl:attribute name="colSpan">
				   					 	<xsl:value-of select="postCount" />
				 					 </xsl:attribute>
				 					 
				 					<xsl:value-of select="spBreak" />
				 				</td>
				 				
				 				
				            </tr>
           			 
           			 <xsl:for-each select="spBreakrotations/rotation"> 
	           			
	            
				            
						            <tr>
						            <td>
						            <xsl:attribute name="colSpan">
						   					 	<xsl:value-of select="rotationPostCount" />
						 					 </xsl:attribute>
						            	
						            	<b><xsl:value-of select="rotationId" /></b>
						            	</td>
						            </tr>
					            	
					            	 <xsl:for-each select="startEndTimes/startEndTime">  
					            	 	<tr >
		           									  		<td bgColor="#E5EBEB"><xsl:value-of select="startEndTimeValue" /></td>
											                	
												             <td bgColor="#E5EBEB">
												             	 <xsl:attribute name="colSpan">
						   					 						<xsl:value-of select="spBreakPostCount" />
						 										 </xsl:attribute>
												             	<xsl:value-of select="commaSeperatedSpBreak" />
												             </td>  
								          </tr>
					            	 </xsl:for-each>
				            
           			   </xsl:for-each>
            </xsl:for-each>
            	
           
			            
          		
             </table>
         </xsl:for-each>
     
    </body>
</html>
</xsl:template>
</xsl:stylesheet>