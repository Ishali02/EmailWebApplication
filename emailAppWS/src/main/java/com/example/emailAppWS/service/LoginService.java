package com.example.emailAppWS.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.example.emailAppWS.model.EmailData;
import com.example.emailAppWS.model.Login;
import com.example.emailAppWS.model.RaiseRequestData;
import com.example.emailAppWS.model.ResponseData;
import com.example.emailAppWS.model.Software;
import com.example.emailAppWS.model.UserRole;;

@Service
public class LoginService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmailService emailService;

	@SuppressWarnings("deprecation")
	public RaiseRequestData loginAuth(Login login)
	{
		String user_id = "";
		RaiseRequestData rrData = new RaiseRequestData();
		List<UserRole> urList = null;
		List<Software> swList = null;
		try {
			String sql = "select user_id from email_app.user  where user_emailId = ? and user_password = ? ";

			user_id = jdbcTemplate.queryForObject(sql, new Object[] { login.getEmailId(), login.getUserPwd() },String.class);
			System.out.println(user_id);
			if(user_id != null && !user_id.isEmpty())
			{
				String sql2 = "SELECT u.user_emailId, u.user_id, r.role_id, r.role_name, u.user_name from user as u  " + 
						"inner join user_role as ur on u.user_id = ur.user_id  " + 
						"inner join role as r on ur.role_id = r.role_id  " + 
						"where r.role_id IN('R001','R002','R003'); ";

				urList = jdbcTemplate.query(sql2, new RowMapper<UserRole>() {
					public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
						UserRole obj = new UserRole();
						obj.setRoleId(rs.getString("role_id"));
						obj.setUserId(rs.getString("user_id"));
						obj.setRoleName(rs.getString("role_name"));
						obj.setUserEmailId(rs.getString("user_emailId"));
						obj.setUserName(rs.getString("user_name"));
						return obj;
					}
				});
				
				
				sql2 = "SELECT sw_id, sw_name, sw_description from software";

				swList = jdbcTemplate.query(sql2, new RowMapper<Software>() {
					public Software mapRow(ResultSet rs, int rowNum) throws SQLException {
						Software obj = new Software();
						obj.setSwId(rs.getString("sw_id"));
						obj.setSwName(rs.getString("sw_name"));
						obj.setSwDesc(rs.getString("sw_description"));
						return obj;
					}
				});
				
				rrData.setUserId(user_id);
				rrData.setUrList(urList);
				rrData.setSwList(swList);
			}

		}catch(Exception e) {
			e.printStackTrace();
			rrData.setUserId("Failure");
			return rrData;
		}
		return rrData;

	}

	public ResponseData signUpAuth(Login login)
	{
		ResponseData resp = new ResponseData();
		String response = "";
		//String sql = "Select get_next_id(user_staging);";
		String sql = "select Get_Next_ID('user_staging') id";
		String stagingID = jdbcTemplate.queryForObject(sql, String.class);

		String sql1 = "INSERT INTO email_app.user_staging (user_staging_id, user_name, user_password, user_emailId) VALUES (?, ?, ?, ?) ";
		try {
			int result = jdbcTemplate.update(sql1, stagingID, login.getUserName(), login.getUserPwd(),login.getEmailId());
			if (result > 0) {
				System.out.println("Insert successfully.");
			} 
			EmailData emailData = new EmailData();
			emailData.setRecepient(login.getEmailId());
			emailData.setSubject("Verification Email");
			String htmlBody = "<p>Click the below link to verify the email</p><br/><br/>"
					+ "<a target='_blank' href='http://localhost:8080/emailAppWS/login/signUpComplete/"+stagingID+"'>Click here </a>";
			emailData.setMessage(htmlBody);
			response= emailService.sendEmail(emailData);
			resp.setResponse(response);
		}catch(DataIntegrityViolationException ex) {
			response = "Account already exists for this emailId.";
			resp.setResponse(response);
			return resp;
		}
		
		return resp;
	}

	public ResponseData signUpComplete(String stagingID) {
		String response = "";
		ResponseData resp = new ResponseData();
		String sql = "select Get_Next_ID('user') id";
		String user_id = jdbcTemplate.queryForObject(sql, String.class);

		String sql1 = " INSERT INTO email_app.user (user_id,user_name, user_password, user_emailId)  "
				+ "(SELECT  '"+user_id+"' as user_id, user_name, user_password, user_emailId FROM email_app.user_staging WHERE user_staging_id = ?)";
		try {
			int result = jdbcTemplate.update(sql1, stagingID);
			if (result > 0) {
				System.out.println("Insert successfully.");
				response = "Verified successfully";
				resp.setResponse(response);
			} 
		}
		catch(DataIntegrityViolationException ex) {
			response = "Account is already created.";
			resp.setResponse(response);
			return resp;
		}
		return resp;
	}

	
}
