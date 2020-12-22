package com.example.emailAppWS.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.example.emailAppWS.model.EmailData;
import com.example.emailAppWS.model.RequestTracking;
import com.example.emailAppWS.model.ResponseData;
import com.example.emailAppWS.model.UserRole;

@Service
public class RequestTrackingService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmailService emailService;

	public ResponseData userRequestSubmit(RequestTracking data ) {
		ResponseData resp = new ResponseData();
		boolean insert = true;
		String sql = "select Get_Next_ID('request') id";
		try {
			String requestId = jdbcTemplate.queryForObject(sql, String.class);
			String sw_name="";
			sql = "select req_approved,sw_name from request r inner join software s on r.sw_id=s.sw_id where user_id= ? and r.sw_id = ?;";
			PreparedStatement statement;
			Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			statement = con.prepareStatement(sql);
			statement.setString(1, data.getUserId());
			statement.setString(2, data.getSoftwareId());
			ResultSet rs = statement.executeQuery();

			System.out.println("Select query ran");
			while(rs.next()) {
				if(rs.getInt("req_approved") == 0 || rs.getInt("req_approved") == 1) {
					sw_name = rs.getString("sw_name");
					insert = false;
				}
			}
			System.out.println("insert : "+insert);
			if(insert) {
				String sql1 = "INSERT INTO email_app.request (request_id, user_id, manager_id, it_team_id, software_team_id, sw_id, req_date,req_status,req_approved) "
						+ "VALUES (?, ?, ?, ?,?, ?, now(),0, 0) ";

				int result = jdbcTemplate.update(sql1, requestId, data.getUserId(), data.getManagerId(),data.getItTeamId(),data.getSoftwareTeamId(),
						data.getSoftwareId());
				if (result > 0) {
					System.out.println("Insert successfully.");
					resp.setResponse("Success");
					resp.setMessage("Request created successfully. Request ID: " + requestId);
					sendApprovalMail(requestId+"-u");
				} 
				else
				{
					resp.setResponse("Failure");
					resp.setMessage("Failed to insert data.");
				}
			}
			
			if(!insert) {
				resp.setResponse("Failure");
				resp.setMessage("Request is already raised for the software " +sw_name);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			resp.setResponse("Failure");
			resp.setMessage("Something went wrong!");
			return resp;
		}
		return resp;
	}

	
	public ResponseData sendApprovalMail(String requestId) {
		System.out.println("Enter sendApprovalMail");
		String[] spl = requestId.split("-");
		int approvedStatus = 0;
		int flag = 0;
		if(spl.length == 3)
		{
			approvedStatus = 1;
		}
		requestId = spl[0];
		String approverId = spl[1];
		

		System.out.println("requestId : " + requestId);
		ResponseData resp = new ResponseData();
		RequestTracking rt = new RequestTracking();
		String sql = "SELECT r.request_id, r.user_id, r.manager_id, r.it_team_id, r.software_team_id, r.sw_id, r.req_date, r.req_status, r.req_approved, sw.sw_name sw_name, " + 
				"	u1.user_emailId user_emailId, u2.user_emailId manager_emailId, u3.user_emailId it_emailId, u4.user_emailId software_emailId," +  
				"	u1.user_name user_name, u2.user_name manager_name, u3.user_name it_name, u4.user_name software_name" + 
				"	FROM email_app.request r" + 
				"    inner join email_app.user u1 on u1.user_id = r.user_id" + 
				"    inner join email_app.user u2 on u2.user_id = r.manager_id" + 
				"    inner join email_app.user u3 on u3.user_id = r.it_team_id" + 
				"    inner join email_app.user u4 on u4.user_id = r.software_team_id" + 
				"    inner join email_app.software sw on sw.sw_id = r.sw_id" +
				"	where request_id=?";
		try {
			PreparedStatement statement;
			Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			statement = con.prepareStatement(sql);
			statement.setString(1, requestId);
			ResultSet rs = statement.executeQuery();

			System.out.println("Select query ran");
			if(rs.next())
			{
				rt.setRequestId(rs.getString("request_id"));
				rt.setUserId(rs.getString("user_id"));
				rt.setManagerId(rs.getString("manager_id"));
				rt.setItTeamId(rs.getString("it_team_id"));
				rt.setSoftwareTeamId(rs.getString("software_team_id"));
				rt.setSoftwareId(rs.getString("sw_id"));
				rt.setReqDate(rs.getString("req_date"));
				rt.setReqStatus(rs.getInt("req_status"));
				rt.setReqApprovedStatus(rs.getInt("req_approved"));
				rt.setSwName(rs.getString("sw_name"));
				rt.setUserMail(rs.getString("user_emailId"));
				rt.setManagerMail(rs.getString("manager_emailId"));
				rt.setItMail(rs.getString("it_emailId"));
				rt.setSoftwareMail(rs.getString("software_emailId"));
				rt.setUserName(rs.getString("user_name"));
				rt.setManagerName(rs.getString("manager_name"));
				rt.setItName(rs.getString("it_name"));
				rt.setSoftwareName(rs.getString("software_name"));

				String recepient = "";
				String message = "";
				int reqStatus = rt.getReqStatus();
				if(approvedStatus == 1)
				{
					reqStatus = 4;
				}

				System.out.println("reqStatus : " + reqStatus);
				
				switch(reqStatus)
				{
				case 0:
					if(approverId.equals("u"))
					{
						if(rs.getInt("req_approved") == 0)
						{
							recepient = rs.getString("manager_emailId");
							message = "<p>Hi " + rs.getString("manager_name") + ",</p><br>"
									+ "<p><a href='mailto:"+rs.getString("user_emailId")+"'>"+ rs.getString("user_name") +"</a> is requesting approval for <b>"+ rt.getRequestId() +"</b>.</p>"
									+ "<p>Software : " + rs.getString("sw_name") + "</p>"
									+ "<p>Request Date : " + rt.getReqDate() + "</p><br>"
									+ "<p>Approvers:<br>"
									+ "		<i><b>Manager: <a href='mailto:"+rs.getString("manager_emailId")+"'>"+ rs.getString("manager_name") +"</a></b></i><br>"
									+ "		IT Team: <a href='mailto:"+rs.getString("it_emailId")+"'>"+ rs.getString("it_name") +"</a><br>"
									+ "		Software Team: <a href='mailto:"+rs.getString("software_emailId")+"'>"+ rs.getString("software_name") +"</a></p><br><br>"
									+ "<a target='_blank' style='text-decoration: none; background:#5cb85c; color:white; padding: 10px;' href='http://localhost:8080/emailAppWS/request/sendApprovalMail/"+rt.getRequestId()+"-m'>Approve</a>"
									+ "<a target='_blank' style='text-decoration: none; background:#d9534f; color:white; padding: 10px; margin-left: 30px;' href='http://localhost:8080/emailAppWS/request/sendApprovalMail/"+rt.getRequestId()+"-m-1'>Reject</a>"
									+ "<br><br>"
									+ "<p>Regards,</p>"
									+ "<p><i>Email App Team</i></p>";
						}
						else
						{
							flag = 4;
						}
					}
					else
					{
						flag = 1;
					}
					break;
				case 1:
					if(approverId.equals("m"))
					{
						if(rs.getInt("req_approved") == 0)
						{
							recepient = rs.getString("it_emailId");
							message = "<p>Hi " + rs.getString("it_name") + ",</p><br>"
									+ "<p><a href='mailto:"+rs.getString("user_emailId")+"'>"+ rs.getString("user_name") +"</a> is requesting approval for <b>"+ rt.getRequestId() +"</b>.</p>"
									+ "<p>Software : " + rs.getString("sw_name") + "</p>"
									+ "<p>Request Date : " + rt.getReqDate() + "</p><br>"
									+ "<p>Approvers:<br>"
									+ "		Manager: <a href='mailto:"+rs.getString("manager_emailId")+"'>"+ rs.getString("manager_name") +"</a><br>"
									+ "		<i><b>IT Team: <a href='mailto:"+rs.getString("it_emailId")+"'>"+ rs.getString("it_name") +"</a></b></i><br>"
									+ "		Software Team: <a href='mailto:"+rs.getString("software_emailId")+"'>"+ rs.getString("software_name") +"</a></p><br><br>"
									+ "<a target='_blank' style='text-decoration: none; background:#5cb85c; color:white; padding: 10px;' href='http://localhost:8080/emailAppWS/request/sendApprovalMail/"+rt.getRequestId()+"-i'>Approve</a>"
									+ "<a target='_blank' style='text-decoration: none; background:#d9534f; color:white; padding: 10px; margin-left: 30px;' href='http://localhost:8080/emailAppWS/request/sendApprovalMail/"+rt.getRequestId()+"-i-1'>Reject</a>"
									+ "<br><br>"
									+ "<p>Regards,</p>"
									+ "<p><i>Email App Team</i></p>";
						}
						else
						{
							flag = 4;
						}
					}
					else
					{
						flag = 1;
					}
					break;
				case 2:
					if(approverId.equals("i"))
					{
						if(rs.getInt("req_approved") == 0)
						{
							recepient = rs.getString("software_emailId");
							message = "<p>Hi " + rs.getString("software_name") + ",</p><br>"
									+ "<p><a href='mailto:"+rs.getString("user_emailId")+"'>"+ rs.getString("user_name") +"</a> is requesting approval for <b>"+ rt.getRequestId() +"</b>.</p>"
									+ "<p>Software : " + rs.getString("sw_name") + "</p>"
									+ "<p>Request Date : " + rt.getReqDate() + "</p><br>"
									+ "<p>Approvers:<br>"
									+ "		Manager: <a href='mailto:"+rs.getString("manager_emailId")+"'>"+ rs.getString("manager_name") +"</a><br>"
									+ "		IT Team: <a href='mailto:"+rs.getString("it_emailId")+"'>"+ rs.getString("it_name") +"</a><br>"
									+ "		<i><b>Software Team: <a href='mailto:"+rs.getString("software_emailId")+"'>"+ rs.getString("software_name") +"</a></b></i></p><br><br>"
									+ "<a target='_blank' style='text-decoration: none; background:#5cb85c; color:white; padding: 10px;' href='http://localhost:8080/emailAppWS/request/sendApprovalMail/"+rt.getRequestId()+"-s'>Approve</a>"
									+ "<a target='_blank' style='text-decoration: none; background:#d9534f; color:white; padding: 10px; margin-left: 30px;' href='http://localhost:8080/emailAppWS/request/sendApprovalMail/"+rt.getRequestId()+"-s-1'>Reject</a>"
									+ "<br><br>"
									+ "<p>Regards,</p>"
									+ "<p><i>Email App Team</i></p>";
						}
						else
						{
							flag = 4;
						}
					}
					else
					{
						flag = 1;
					}
					break;
				case 3:
					if(approverId.equals("s"))
					{
						if(rs.getInt("req_approved") == 0)
						{
							recepient = rs.getString("user_emailId");
							message = "<p>Hi " + rs.getString("user_name") + ",</p><br>"
									+ "<p>Your request for "+ rs.getString("sw_name") +"(<b>"+ rt.getRequestId() +"</b>) is approved.</p>"
									+ "<br><br>"
									+ "<p>Regards,</p>"
									+ "<p><i>Email App Team</i></p>";
						}
						else
						{
							flag = 4;
						}
					}
					else
					{
						flag = 1;
					}
					break;
				case 4:
					if(rs.getInt("req_approved") == 0)
					{
						if((approverId.equals("m") && rs.getInt("req_status") == 1) || (approverId.equals("i") && rs.getInt("req_status") == 2) || (approverId.equals("s") && rs.getInt("req_status") == 3))
						{
							recepient = rs.getString("user_emailId");
							String rejector = "";
							switch(rt.getReqStatus()) {
							case 1:
								rejector = "<a href='mailto:"+rs.getString("manager_emailId")+"'>"+ rs.getString("manager_name") +"</a>";
								break;
							case 2:
								rejector = "<a href='mailto:"+rs.getString("it_emailId")+"'>"+ rs.getString("it_name") +"</a>";
								break;
							case 3:
								rejector = "<a href='mailto:"+rs.getString("software_emailId")+"'>"+ rs.getString("software_name") +"</a>";
								break;
							}
							message = "<p>Hi " + rs.getString("user_name") + ",</p><br>"
									+ "<p>Your request for "+ rs.getString("sw_name") +"(<b>"+ rt.getRequestId() +"</b>) is rejected by " + rejector + ".</p>"
									+ "<br><br>"
									+ "<p>Regards,</p>"
									+ "<p><i>Email App Team</i></p>";
						}
						else
						{
							flag = 3;
						}
					}
					else
					{
						flag = 2;
					}
					break;
				default:
					System.out.println("Request Status is invalid : " + rt.getReqStatus());
					return null;
				}

				if(flag == 0)
				{
					EmailData emailData = new EmailData();
					emailData.setSubject("Approval for " + rt.getRequestId());
					emailData.setRecepient(recepient);
					emailData.setMessage(message);
					
					System.out.println("Sending Email");
					
					String res = emailService.sendEmail(emailData);
					
					System.out.println("Email status : " + res);
					
					if(res.equalsIgnoreCase("success"))
					{
						int req_approved = 0;
						if(rt.getReqStatus() == 3)
						{
							req_approved = 1;
						}
						if(reqStatus == 4)
						{
							req_approved = 2;
							rt.setReqStatus(rt.getReqStatus() - 1);
						}
						sql = "UPDATE email_app.request SET req_status=?, req_approved=? WHERE request_id=?";
						int result = jdbcTemplate.update(sql, rt.getReqStatus()+1, req_approved, rt.getRequestId());
						if(result > 0)
						{
							if(req_approved == 2)
							{
								System.out.println("Request Rejected successfully.");
								resp.setResponse("Success");
								resp.setMessage("Request rejected successfully. Request ID: " + requestId);
							}
							else
							{
								System.out.println("Request Approved successfully.");
								resp.setResponse("Success");
								resp.setMessage("Request approved successfully. Request ID: " + requestId);
							}
						}
						else
						{
							System.out.println("Failed to Update");
							resp.setResponse("Success");
							resp.setMessage("Failed to update request.");
						}
					}
					else
					{
						System.out.println("Send Email Failed");
						resp.setResponse("Failure");
						resp.setMessage("Failed to send email.");
					}
				}
				else
				{
					if(flag == 1)
					{
						System.out.println("Request is already approved");
						resp.setResponse("Failure");
						resp.setMessage("Request is already approved.");
					}
					else if(flag == 2)
					{
						System.out.println("Request is already rejected");
						resp.setResponse("Failure");
						resp.setMessage("Request is already rejected.");
					}
					else if(flag == 3)
					{
						System.out.println("Request is already approved by you");
						resp.setResponse("Failure");
						resp.setMessage("Request is already approved by you.");
					}
					else if(flag == 4)
					{
						System.out.println("Request is already rejected by you");
						resp.setResponse("Failure");
						resp.setMessage("Request is already rejected by you.");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setResponse("Failure");
			resp.setMessage("sendApprovalMail : Something went wrong!");
			return resp;
		}
		return resp;
	}
	
	public List<RequestTracking> requestTrack(String userId){
		List<RequestTracking> result = new ArrayList<>();
		
		String sql = "SELECT r.request_id, r.user_id, r.manager_id, r.it_team_id, r.software_team_id, r.sw_id, r.req_date, r.req_status, " + 
				" r.req_approved, sw.sw_name sw_name,  u1.user_emailId user_emailId, u2.user_emailId manager_emailId, u3.user_emailId   " + 
				" it_emailId, u4.user_emailId software_emailId,  u1.user_name user_name, u2.user_name manager_name, u3.user_name it_name,  " + 
				" u4.user_name software_name  " + 
				"FROM email_app.request r   " + 
				"inner join email_app.user u1 on u1.user_id = r.user_id  " + 
				"inner join email_app.user u2 on u2.user_id = r.manager_id  " + 
				"inner join email_app.user u3 on u3.user_id = r.it_team_id  " + 
				"inner join email_app.user u4 on u4.user_id = r.software_team_id  " + 
				"inner join email_app.software sw on sw.sw_id = r.sw_id  " + 
				"where u1.user_id= ?";
		try {
			PreparedStatement statement;
			Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			statement = con.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();

			System.out.println("Select query ran");
			while(rs.next())
			{
				RequestTracking rt = new RequestTracking();
				rt.setRequestId(rs.getString("request_id"));
				rt.setUserId(rs.getString("user_id"));
				rt.setManagerId(rs.getString("manager_id"));
				rt.setItTeamId(rs.getString("it_team_id"));
				rt.setSoftwareTeamId(rs.getString("software_team_id"));
				rt.setSoftwareId(rs.getString("sw_id"));
				rt.setReqDate(rs.getString("req_date"));
				rt.setReqStatus(rs.getInt("req_status"));
				rt.setReqApprovedStatus(rs.getInt("req_approved"));
				rt.setSwName(rs.getString("sw_name"));
				rt.setUserMail(rs.getString("user_emailId"));
				rt.setManagerMail(rs.getString("manager_emailId"));
				rt.setItMail(rs.getString("it_emailId"));
				rt.setSoftwareMail(rs.getString("software_emailId"));
				rt.setUserName(rs.getString("user_name"));
				rt.setManagerName(rs.getString("manager_name"));
				rt.setItName(rs.getString("it_name"));
				rt.setSoftwareName(rs.getString("software_name"));
				result.add(rt);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("result "+ result);
		return result;
	} 
}
