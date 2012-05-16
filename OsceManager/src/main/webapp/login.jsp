<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title >Login Page</title>
<style>
.errorblock {
    color: #ff0000;
    background-color: #ffEEEE;
    border: 3px solid #ff0000;
    padding: 8px;
    margin: 16px;
	text-align:center;
	font-family: Arial Unicode MS,Arial,sans-serif;
	font-weight: bold;
	color:#666666;
		
}

.mainPanel{
	vertical-align:middle;
 	color: #ff0000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
    margin: 16px;
	text-align:center;
	

}

table{
	border:1px solid #000;

}
td{
	border:none;
	padding-top:20px;

}
.tableStyle{
	border: 1px solid #AAAAAA;
	-webkit-border-radius:15px;
	-moz-border-radius:15px;
	padding:0px 0px 0px 0px;
	-moz-box-shadow: 0 0 20px #CBCBCB;
	-webkit-box-shadow: 0 0 20px #CBCBCB;
	font-family: Arial Unicode MS,Arial,sans-serif;
	font-weight: bold;
	color:#666666;
	
	
}
.buttonStyle{
font-size:16px;
font-family: Arial Unicode MS,Arial,sans-serif;
font-weight: bold;
color:#666666;
}

</style>
</head>
<body onload='document.f.j_username.focus();'>
  

	   <c:if test="${not empty param.login_error}">
        <div class="errorblock">
            Your login attempt was not successful, try again.<br /> Caused :
            ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
        </div>
    </c:if>

    <form name='f' action="<c:url value='/resources/j_spring_security_check' />"
        method='POST'>


	<table width="296" align="center" class="tableStyle"  style="padding:50px" >
            <tr>
                <td width="92" align="right">User:</td>
                <td width="192"><input name='j_username' type='text' value='' style="font-size:15px ; height:30px" ></td>
            </tr>
			
			<tr>
                <td width="92" align="right">Password:</td>
                <td width="192"><input name='j_password' type='password'  value='' style="font-size:15px ; height:30px"></td>
            </tr>
			
			
			
           <!-- <tr>
                <td  width="80"> Password:</td>
              <td  width="212"><input name='j_password' type='password'   style="width:auto" value='' size="25"></td>
            </tr>-->
         	
		
			<tr>
                <td height="79"> </td>
                
				<td align="middle">
				<input name="submit" type="submit"
                    value="Submit"  width="100px" height="50px"  class="buttonStyle"/>
				<input name="reset" type="reset"  value="Reset"  width="100px" height="50px"  class="buttonStyle" />               </td>
            </tr>
      </table>
  
    </form>
</body>
</html>

