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
      </style>
   </head>

   <body>
      
         <xsl:for-each select="osceDays/osceDay">
            <table class="dayTable" border="1px" cellSpacing="0" align="center">
            <tr class="dayTD">
           
                  
                  <td><xsl:value-of select="osceDayID" /></td>
                 
            </tr>
            <xsl:for-each select="parcours/parcour">
	            <tr >
	               
	               <td class="parcourTD"><xsl:value-of select="parcourColor" /></td>
	            </tr>
            
	            	
				            <tr class="postTR">
				            <td></td>
				              <xsl:for-each select="posts/post">
				               	<td><xsl:value-of select="postName" /></td>
				               </xsl:for-each>
				            </tr>
				            <tr class="postTR">
				            <td></td>
				              <xsl:for-each select="posts/post">
				               	<td><xsl:value-of select="standardizedRole" /></td>
				               </xsl:for-each>
				            </tr>
		            
		            
		            <xsl:for-each select="rotations/rotation">            
						            <tr>
						               
						               <td><b><xsl:value-of select="rotationId" /></b></td>
						               
						               <xsl:for-each select="examiners/examiner">     
						               		<td><xsl:value-of select="examinerName" /></td>
						               </xsl:for-each>
						            </tr>
				           
				            
						            <xsl:for-each select="startEndTimes/startEndTime">  
						            <tr class="studentTR">
						               
						               <td><xsl:value-of select="startEndTimeValue" /></td>
						                <xsl:for-each select="students/student">
						                   <td><xsl:value-of select="studentName" /></td>
						                </xsl:for-each>
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