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
          	 
          }
          
          .studentTR{
          	background:rgb(229,235,235);
          	color:rgb(137,120,164);
          }
          
         /*parcour colors*/
/* Color Shade */
.accordion-title-selectedcolor_1 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#DB8937', endColorstr='#e9b887'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#DB8937), to(#e9b887)); /* for webkit browsers brown*/
	background: -moz-linear-gradient(top, #DB8937, #e9b887); /* for firefox 3.6+ brown*/	
}
.accordion-title-selectedcolor_2 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fcfc74', endColorstr='#fdfdab'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#fcfc74), to(#fdfdab)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #fcfc74, #fdfdab); /* for firefox 3.6+ yellow*/
}
.accordion-title-selectedcolor_3 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fca9fb', endColorstr='#f9eaea'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#fdcbfc), to(#f9eaea)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #fca9fb, #f9eaea); /* for firefox 3.6+ pink */
}
.accordion-title-selectedcolor_4 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#a7f4fa', endColorstr='#caf8fc'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#a7f4fa), to(#caf8fc)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #a7f4fa, #caf8fc); /* for firefox 3.6+ aqua*/
}
.accordion-title-selectedcolor_5 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#888888', endColorstr='#b7b7b7'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#888888), to(#b7b7b7)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #888888, #b7b7b7); /* for firefox 3.6+ grey*/
}
.accordion-title-selectedcolor_6 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fc6f6f', endColorstr='#ffe4c4'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#fc6f6f), to(#ffe4c4)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #fc6f6f, #ffe4c4); /* for firefox 3.6+ red*/
}
.accordion-title-selectedcolor_7 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffe4c4', endColorstr='#ffeedb'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#ffe4c4), to(#ffeedb)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #ffe4c4, #ffeedb); /* for firefox 3.6+ beige*/
}
.accordion-title-selectedcolor_8 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#83a2eb', endColorstr='#b4c7f3'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#83a2eb), to(#b4c7f3)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #83a2eb, #b4c7f3); /* for firefox 3.6+ blue*/
}
.accordion-title-selectedcolor_9 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#98e6a2', endColorstr='#98e6a2'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#98e6a2), to(#e6e8fd)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #54d665, #98e6a2); /* for firefox 3.6+ green*/
}
.accordion-title-selectedcolor_10 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#f5c93b', endColorstr='#f9de89'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#f5c93b), to(#f9de89)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #f5c93b, #f9de89); /* for firefox 3.6+ orange*/
}
.accordion-title-selectedcolor_11 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff2626', endColorstr='#ff7c7c'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#ff2626), to(#ff7c7c)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #ff2626, #ff7c7c); /* for firefox 3.6+ purpor*/
}
.accordion-title-selectedcolor_12 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fa8072', endColorstr='#fcb2aa'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#fa8072), to(#fcb2aa)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #fa8072, #fcb2aa); /* for firefox 3.6+ maroon*/
}
.accordion-title-selectedcolor_13 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#6b8e23', endColorstr='#a6bb7b'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#6b8e23), to(#a6bb7b)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #6b8e23, #a6bb7b); /* for firefox 3.6+ olive */
}
.accordion-title-selectedcolor_14 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#8f8bd6', endColorstr='#bbb9e6'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#8f8bd6), to(#bbb9e6)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #8f8bd6, #bbb9e6); /* for firefox 3.6+ violet*/
}
.accordion-title-selectedcolor_15 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#4b0082', endColorstr='#9366b4'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#4b0082), to(#9366b4)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #4b0082, #9366b4); /* for firefox 3.6+ indigo*/
}
.accordion-title-selectedcolor_16 {
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#000000', endColorstr='#666666'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#000000), to(#666666)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #000000, #666666); /* for firefox 3.6+ black*/
	color:white !important;
}

/* Color Shade */
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
					            <td  bgColor="#DB8937"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#DB8937"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#DB8937"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#DB8937"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#DB8937"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#DB8937"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_2'">
					            <tr >
					            <td  bgColor="#fcfc74"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fcfc74"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td  bgColor="#fcfc74"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fcfc74"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#fcfc74"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fcfc74"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_3'">
					            <tr >
					            <td  bgColor="#fca9fb"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fca9fb"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td bgColor="#fca9fb"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fca9fb"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#fca9fb"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fca9fb"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_4'">
					            <tr >
					            <td  bgColor="#a7f4fa"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#a7f4fa"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td bgColor="#a7f4fa"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#a7f4fa"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#a7f4fa"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#a7f4fa"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_5'">
					            <tr >
					            <td  bgColor="#888888"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#888888"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr class="postTR">
					            <td bgColor="#888888"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#888888"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#888888"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#888888"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_6'">
					            <tr >
					            <td  bgColor="#fc6f6f"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fc6f6f"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td  bgColor="#fc6f6f"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fc6f6f"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#fc6f6f"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fc6f6f"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_7'">
					            <tr >
					            <td  bgColor="#ffe4c4"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#ffe4c4"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#ffe4c4"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ffe4c4"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#ffe4c4"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ffe4c4"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_8'">
					            <tr >
					            <td  bgColor="#83a2eb"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#83a2eb"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#83a2eb"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#83a2eb"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#83a2eb"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#83a2eb"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_9'">
					            <tr >
					            <td  bgColor="#54d665"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#54d665"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#54d665"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#54d665"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#54d665"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#54d665"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_10'">
					            <tr >
					            <td  bgColor="#f5c93b"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#f5c93b"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#f5c93b"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#f5c93b"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#f5c93b"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#f5c93b"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_11'">
					            <tr >
					            <td  bgColor="#ff2626"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#ff2626"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#ff2626"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ff2626"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#ff2626"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#ff2626"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_12'">
					            <tr >
					            <td  bgColor="#fa8072"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#fa8072"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#fa8072"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fa8072"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#fa8072"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#fa8072"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_13'">
					            <tr >
					            <td  bgColor="#6b8e23"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#6b8e23"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#6b8e23"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#6b8e23"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#6b8e23"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#6b8e23"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_14'">
					            <tr >
					            <td  bgColor="#8f8bd6"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#8f8bd6"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td  bgColor="#8f8bd6"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#8f8bd6"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#8f8bd6"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#8f8bd6"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_15'">
					            <tr >
					            <td  bgColor="#4b0082"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#4b0082"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr >
					            <td bgColor="#4b0082"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#4b0082"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr  >
					            <td bgColor="#4b0082"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#4b0082"><xsl:value-of select="standardizedRole" /></td>
					               </xsl:for-each>
					            </tr>
		            		</xsl:when>
		            		<xsl:when test="parcourColor = 'color_16'">
					            <tr >
					            <td  bgColor="#000000"></td>
					              <xsl:for-each select="posts/post">
					               	<td  bgColor="#000000" style="color:white"><xsl:value-of select="postName" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr color="white">
					            <td bgColor="#000000"></td>
					            
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#000000" style="color:white"><xsl:value-of select="postRoom" /></td>
					               </xsl:for-each>
					            </tr>
					            <tr color="white" >
					            <td bgColor="#000000"></td>
					              <xsl:for-each select="posts/post">
					               	<td bgColor="#000000" style="color:white"><xsl:value-of select="standardizedRole" /></td>
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