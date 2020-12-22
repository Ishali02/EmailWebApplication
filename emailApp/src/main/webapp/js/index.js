var baseUrl = document.location.origin;
var wscontext = "emailAppWS";
var loggedinUser = "";
var raiseRequestData = "";

$(document).ready(function(){
	
	
	
	
	$("#bodyDiv").addClass("d-none");
//	Login JS
	$("#logInTab").click(function(){
		$(this).removeClass("inactiveTab-login");
		$(this).addClass("activeTab");
		$(this).parent().removeClass("inactiveTabDiv");
		$(this).parent().addClass("activeTabDiv");

		$("#signUpTab").addClass("inactiveTab-signup");
		$("#signUpTab").removeClass("activeTab");
		$("#signUpTab").parent().addClass("inactiveTabDiv");
		$("#signUpTab").parent().removeClass("activeTabDiv");

		$(".loginFormDiv").removeClass("d-none");
		$(".signUpFormDiv").addClass("d-none");
	});
	
	$("#signUpTab").click(function(){
		$(this).removeClass("inactiveTab-signup");
		$(this).addClass("activeTab");
		$(this).parent().removeClass("inactiveTabDiv");
		$(this).parent().addClass("activeTabDiv");

		$("#logInTab").addClass("inactiveTab-login");
		$("#logInTab").removeClass("activeTab");
		$("#logInTab").parent().addClass("inactiveTabDiv");
		$("#logInTab").parent().removeClass("activeTabDiv");

		$(".loginFormDiv").addClass("d-none");
		$(".signUpFormDiv").removeClass("d-none");
	});
	
	
//	Index JS
	
	
	$(".menuListItems").click(function(){
        $(".menuListItems").removeClass("active");
        $(this).addClass("active");
    });
	
	$("#trackReqTab").click(function(){
		event.preventDefault();
        $("#maindiv").load("TrackRequest.html",function(){
//        	$('#trackRequestDataTable').DataTable();
        	trackRequest();
        });
	});
	
	$("#raiseReqTab").click(function(){
		event.preventDefault();
        $("#maindiv").load("raiseReq.html", function(){
        	 resetRequestForm();
         	$("#raiseReqErrorMsgId").hide();
        	$("#raiseReqSuccessMsgId").hide();
        });
        
	});
	

	
	$("#logout-btn").click(function(){
		event.preventDefault();
		$("#bodyDiv").addClass("d-none");
		$("#logindiv").removeClass("d-none");
	});
	
//	$("#loginSubmit").click(function(){
//		event.preventDefault();
////		$("body").load("base.html");
//		$("#bodyDiv").removeClass("d-none");
//		$("#logindiv").addClass("d-none");
//        $("#maindiv").load("raiseReq.html");
//	});
	
	$("#loginForm").submit(function(e){
		event.preventDefault();
		$("#loginErrorMsgId").addClass("d-none");
        var emailId = $("#l_email").val();
        var pwd = $("#l_pwd").val();
        
        var url = baseUrl + "/" + wscontext + "/login/loginAuth";
        var formData = {
    		'emailId': emailId,
    		'userPwd': pwd
        }
        console.log(url);
        
        $.ajax({
            type        : 'POST', // define the type of HTTP verb we want to use (POST for our form)
            url         : url, // the url where we want to POST
            data        : formData, // our data object
            dataType    : 'json', // what type of data do we expect back from the server
        })
            // using the done promise callback
        .done(function(data) {
        	
        	loggedinUser = "";
        	$("#selSW").empty();
        	$("#selManager").empty();
        	$("#selIT").empty();
        	$("#selSwTeam").empty();
        	
            // log data to the console so we can see
            console.log(data);
            if(data.userId == "Failure"){
            	$("#loginErrorMsgId").removeClass("d-none");
            	 setTimeout(function(){
				        $("#loginErrorMsgId").hide();
				}, 6000);
            	return;
            }
            if(data != null)
            {
            	
            	raiseRequestData = data;
	    		$("#bodyDiv").removeClass("d-none");
	    		$("#logindiv").addClass("d-none");
	            $("#maindiv").load("raiseReq.html", function(){
	             	$("#raiseReqErrorMsgId").hide();
	            	$("#raiseReqSuccessMsgId").hide();
	            	
	            	$('.form-control-select2').select2();
	            	$("#selSW").append('<option value=' + "" + '>' + "" + '</option>');
	            	$("#selManager").append('<option value=' + "" + '>' + "" + '</option>');
	            	$("#selIT").append('<option value=' + "" + '>' + "" + '</option>');
	            	$("#selSwTeam").append('<option value=' + "" + '>' + "" + '</option>');
	            	$(data.urList).each(function(i, item) {
	                	console.log(item);
	    				if(item.roleId == "R001") {
	    					$("#selManager").append('<option value=' + item.userId + '>' + item.userName + '</option>');
	    				}
	    				if(item.roleId == "R002") {
	    					$("#selIT").append('<option value=' + item.userId + '>' + item.userName + '</option>');
	    				}
	    				if(item.roleId == "R003") {
	    					$("#selSwTeam").append('<option value=' + item.userId + '>' + item.userName + '</option>');
	    				}
	                });
	            	$(data.swList).each(function(i, item) {
	                	console.log(item);
	    				$("#selSW").append('<option value=' + item.swId + '>' + item.swName + '</option>');
	                });
	            	loggedinUser = data.userId;
	            });
            }
            else
        	{
            	console.log("No Data found");
        	}
            

            // here we will handle errors and validation messages
        })
        .fail(function(data){
        	console.log("Something went wrong : " + url);
        });
        resetLoginSignUpData();
	});
	
	
	
	
	$("#signUpForm").submit(function(e){
		event.preventDefault();
		console.log("signUpSubmit");
		$("#signUpErrorMsgId").addClass("d-none");
	    var suname = $("#suname").val();
	    var email = $("#email").val();
	    var spwd = $("#spwd").val();
	    var cpwd = $("#cpwd").val();
	    if(!(spwd === cpwd)){
	    	console.log("Password Mismatch");
	    	$("#signUpErrorMsgId").removeClass("d-none");
	    	$("#signUpErrorMsgId").html("Password Mismatch");
		     $("#signUpErrorMsgId").show();
			 setTimeout(function(){
				 $("#signUpErrorMsgId").hide();
			}, 9000);
	    	return;
	    }
	    //signUpErrorMsgId
	    
	    var url = baseUrl + "/" + wscontext + "/login/signUpAuth";
	    var formData = {
	    		"userName": suname,
	    	    "emailId": email,
	    	    "userPwd": spwd,
	    	    
	    	  
	    }
	    console.log(url);
	    
	    $.ajax({
	        type        : 'POST', // define the type of HTTP verb we want to use (POST for our form)
	        url         : url, // the url where we want to POST
	        data        : formData, // our data object
	        dataType    : 'json', // what type of data do we expect back from the server
	    })
	        // using the done promise callback
	    .done(function(data) {

	        // log data to the console so we can see
	        console.log(data);
	        $("#signUpErrorMsgId").removeClass("d-none");
	        $("#signUpErrorMsgId").html("");
	        $("#signUpSuccessMsgId").removeClass("d-none");
	        $("#signUpSuccessMsgId").html("");
	        if(data.response == "Account already exists for this emailId."){
	        	 $("#signUpErrorMsgId").html("Account already exists for this emailId.");
			     $("#signUpErrorMsgId").show();
				 setTimeout(function(){
					 $("#signUpErrorMsgId").hide();
				}, 9000);
	        	 
	        }
	        else if(data.response == "Success"){

		        $("#signUpSuccessMsgId").html("Check your email for verification ");
		        $("#signUpSuccessMsgId").show();
	        	setTimeout(function(){
			        $("#signUpSuccessMsgId").hide();
			}, 9000);
	        }
	        // here we will handle errors and validation messages
	    })
	    .fail(function(data){
	    	console.log("Something went wrong : " + url);
	    });
	    resetLoginSignUpData();
		
	});
	
});

function resetLoginSignUpData(){
	$("#signUpForm").trigger("reset");
	$("#loginForm").trigger("reset");
	$("#signUpErrorMsgId").hide();
	$("#signUpErrorMsgId").hide();
	
}
