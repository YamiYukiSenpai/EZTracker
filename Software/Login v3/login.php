<!DOCTYPE html>
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
	
	$email = $_POST['email'];
	$password = $_POST['passwd'];
	
	$output = shell_exec("sudo bash runEz.sh $email $password &");	
	
	$error = shell_exec("cat error.txt");
	
	if($error == 1){
		header("Location: fail.php");
		exit();
	}
	elseif($error == 2){
		header("Location: error.html");
		exit();
	}
	elseif($error == 0)
	{
		header("Location: success.html");
		exit();
	}
	else
	{
		header("Location: error.html");
		exit();
	}

?>	
		<center><h1 style="color: white;"></h1></center>
	
		<div  id="loginWrapper">
			<form name="login" action="welcome.php" method="POST">
					<center><img src="ezlogotransparent.png" alt="EZ Logo" width="260px" height="190" /></center>		
				<div id="inside">
					<label for="uname" id="emailPlace"><strong>E-mail</strong></label><br/>
					<input type="text" id="uname" name="email" placeholder="Enter e-mail" required><br/><br/>
				
					<label for="password" id="passwordPlace"><strong>Password</strong></label><br/>
					<input type="password" id="password" name="passwd" placeholder="Enter password" required><br/><br/>

				</div>
				<div id="submitBtn">
					<input type="submit" value="Submit" class="button"><span></span></input>	
				</div>		
			</form>		
		</div>			
	</body>
</html>
