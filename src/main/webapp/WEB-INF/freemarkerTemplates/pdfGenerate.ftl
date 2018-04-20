<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8"></meta>
		<title>flying sauser</title>
		<style type="text/css">
			body {  
	         	font-family: SimSun;  
	     	}  
		</style>
	</head>
	<body>
		<h1>
			${title}
		</h1>
		<table>
		<tr>
		     <td>学号</td>
		     <td>姓名</td>
		     <td>年龄</td>
		     <td>科目</td>
		     <td>得分</td>
		   </tr>
		<#list userList as user>
           <tr>
		     <td>${user.id}</td>
		     <td>${user.name}</td>
		     <td>${user.age}</td>
		     <td>${user.subject}</td>
		     <td>${user.score}</td>
		   </tr>
        </#list>
		</table>
		
		
		<table>
		 <tr>
		 <#list imgpaths as img>
			 <td><img src="data:image/jpg;base64,${img}" width="100px" height="50px"/></td>
		  </#list>
		 </tr>
		</table>
	</body>
</html>