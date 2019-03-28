

$(function() {
    $('.error').hide();
    $('.button').click(function() {
      // validate and process form here
      
     
      $('.error').hide();
      var uname = $("input#uname").val();
      if (uname == "") {
        $("label#uname_error").show();
        $("input#uname").focus();
        return false;
        
      $('.error').hide();
      var fname = $("input#fname").val();
      if (fname == "") {
        $("label#fname_error").show();
        $("input#fname").focus();
        return false;
      }
      $('.error').hide();
      var lname = $("input#lname").val();
      if (lname == "") {
        $("label#lname_error").show();
        $("input#lname").focus();
        return false;
      }
      $('.error').hide();
      var email = $("input#email").val();
      if (email == "") {
        $("label#email_error").show();
        $("input#email").focus();
        return false;
      }
			$('.error').hide();
      var password = $("input#password").val();
      if (password == "") {
        $("label#pwd_error").show();
        $("input#password").focus();
        return false;
      } 
      $('.error').hide();
      var rptpsw = $("input#psw_repeat").val();
      if (rptpsw == "") {
        $("label#psw_rpt_error").show();
        $("input#psw_repeat").focus();
        return false;
      } 


  


    });
	});
	
	