<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>EZ Tracker Login</title>
		<script type="text/javascript" src="https://ajax.microsoft.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
	<!--	<script src="validation_check.js"></script>	-->
		<link rel="stylesheet" type="text/css" href="EZ.css">
		<style>

		</style>
	</head>	
<body class="loginBody" id="runningMan" background="background2.jpg">	
<?php
	$error = shell_exec("cat error.txt");

	if($error == 1){
		header("Location: fail.php");
		exit();
	}
//	else{
//		header("Location: success.php");
//		exit();
//	}
	$email = $_POST['email'];
	$password = $_POST['passwd'];

//	echo "{$email} ";
	//echo "{$password}";
	$output = shell_exec("./listdir.sh $email $password");
?>
 
 		<center><h1 style="color: white;"></h1></center>
	
		<div  id="loginWrapper">
			<form name="login" action="login.php" method="POST">
					<center><img src="ezlogotransparent.png" alt="EZ Logo" width="260px" height="190" /></center>		
				<div id="failinside">
					<br/><label for="uname" id="failPlace"><strong>Success! You may now use your device.</strong></label><br/><br/>

				</div>
						
			</form>		
		</div>	




</body>
</html>
