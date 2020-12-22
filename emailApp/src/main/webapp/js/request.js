function sendRequest()
{
	event.preventDefault();

	var selManager = $("#selManager").val();
	var selIT = $("#selIT").val();
	var selSwTeam = $("#selSwTeam").val();
	var selSW = $("#selSW").val();

	var url = baseUrl + "/" + wscontext + "/request/userRequestSubmit";
	var formData = {
			"userId": loggedinUser,
			"managerId": selManager,
			"itTeamId": selIT,
			"softwareTeamId": selSwTeam,
			"softwareId": selSW
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
		//  $("#raiseReqSuccessMsgId").removeClass("d-none");
		var msg = data.message;
		if(data.response == "Success"){
			$("#raiseReqSuccessMsgId").show();
			setTimeout(function(){
				$("#raiseReqSuccessMsgId").hide();
			}, 6000);
			resetRequestForm();
		}

		else if(data.response=="Failure" && msg.indexOf("Request is already raised for the software") != -1){
			$("#raiseReqErrorMsgId").html("");
			$("#raiseReqErrorMsgId").html(msg);
			$("#raiseReqErrorMsgId").show();
			setTimeout(function(){
				$("#raiseReqErrorMsgId").hide();
			}, 6000);
			resetRequestForm();
		}


		// here we will handle errors and validation messages
	})
	.fail(function(data){
		console.log("Something went wrong : " + url);
	});
}


function resetRequestForm() {
	data = raiseRequestData;
	$("#selSW").empty();
	$("#selManager").empty();
	$("#selIT").empty();
	$("#selSwTeam").empty();
	$('.form-control-select2').select2();
	$("#selSW").append('<option value=' + "" + '>' + "" + '</option>');
	$("#selManager").append('<option value=' + "" + '>' + "" + '</option>');
	$("#selIT").append('<option value=' + "" + '>' + "" + '</option>');
	$("#selSwTeam").append('<option value=' + "" + '>' + "" + '</option>');
	$(data.urList).each(function(i, item) {
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
		$("#selSW").append('<option value=' + item.swId + '>' + item.swName + '</option>');
	});
}

function trackRequest(){
	event.preventDefault();

	var url = baseUrl + "/" + wscontext + "/request/requestTrack/"+loggedinUser;

	console.log(url);

	$.ajax({
		type        : 'GET', // define the type of HTTP verb we want to use (POST for our form)
		url         : url, // the url where we want to POST
		// data        : formData, // our data object
		dataType    : 'json', // what type of data do we expect back from the server
	})
	// using the done promise callback
	.done(function(data) {
		// log data to the console so we can see
		console.log(data);
		writeToDataTable(data);
	})
	.fail(function(data){
		console.log("Something went wrong : " + url);
	});
}

function writeToDataTable(data){
//	console.log("data----------------" + JSON.stringify(data));
	if ( $.fn.DataTable.isDataTable('#example') ) {
		$('#example').DataTable().destroy();
	}

	if(data.length==0)
	{
		var table = $('#example').DataTable({
			"language": {
				"emptyTable": "No Records Found"
			},
		});
		table.clear().draw();
	}
	else {
		var dataTableArr = [];

		var j = 0;

		for (var i = 0; i < data.length; i++) {

			var reqStatus = data[i].reqStatus;
			var reqApprovedStatus = data[i].reqApprovedStatus;
			var ManagerStatus = "";
			var ITStatus = "";
			var SoftwareStatus = "";
			if(reqApprovedStatus == 0)
			{
				ManagerStatus = reqStatus >= 2 ? "Approved": "Pending";
				ITStatus = reqStatus >= 3 ? "Approved": "Pending";
				SoftwareStatus = reqStatus == 4 ? "Approved": "Pending";
			}
			else if(reqApprovedStatus == 2)
			{
				if(reqStatus == 1)
				{
					ManagerStatus = "Rejected";
					ITStatus = "";
					SoftwareStatus = "";
				}
				if(reqStatus == 2)
				{
					ManagerStatus = "Approved";
					ITStatus = "Rejected";
					SoftwareStatus = "";
				}
				if(reqStatus == 3)
				{
					ManagerStatus = "Approved";
					ITStatus = "Approved";
					SoftwareStatus = "Rejected";
				}
			}
			else
			{
				ManagerStatus = "Approved";
				ITStatus = "Approved";
				SoftwareStatus = "Approved";
			}
			dataTableArr[j] = [];
			dataTableArr[j][0] =  data[i].requestId;
			dataTableArr[j][1] = data[i].swName;
			dataTableArr[j][2] = "<p>"+ ManagerStatus + "<br>(" + data[i].managerName + ")</p>";
			dataTableArr[j][3] = "<p>"+ ITStatus + "<br>(" + data[i].itName + ")</p>";
			dataTableArr[j][4] = "<p>"+ SoftwareStatus + "<br>(" + data[i].softwareName + ")</p>";
			dataTableArr[j][5] = data[i].reqDate;
			dataTableArr[j][6] = data[i].reqApprovedStatus == 0 ? "Pending" : data[i].reqApprovedStatus == 1 ? "Completed" : "Rejected";
			j++;

		}
		if ( dataTableArr.length !== data.length ){
			dataTableArr.splice(dataTableArr.length -1, 1);
		}
//		$('#example >thead >tr').css("display","block");
		if ( $.fn.dataTable.isDataTable( '#example' ) ) {
			console.log("is data table");
			table = $('#example').DataTable();
			table.destroy();
			$('#example').DataTable( {
				"data": dataTableArr,
				"scrollX": false,
				"order": [[ 0, "desc" ]],
				"aLengthMenu": [[10, 25, 50, 75, -1], [10, 25, 50, 75, "All"]],
				"iDisplayLength": 10        } );
		}
		else {
			console.log("is not data table1");
			table = $('#example').DataTable( {
				"data": dataTableArr,
				"scrollX": false,
				"order": [[ 0, "desc" ]],
				"aLengthMenu": [[10, 25, 50, 75, -1], [10, 25, 50, 75, "All"]],
				"iDisplayLength": 10	
			} );
		}

	} 
}