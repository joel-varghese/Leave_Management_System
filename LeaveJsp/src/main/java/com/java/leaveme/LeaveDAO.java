package com.java.leaveme;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class LeaveDAO {
	
	Connection connection;
	PreparedStatement pst;
		public String approveDeny(int mgrId, int leaveId, String status, String managerComments) throws ClassNotFoundException, SQLException {
			LeaveDetails leaveDetails = searchLeave(leaveId);
			if (leaveDetails != null) {
				int empId = leaveDetails.getEmpId();
				EmployDAO dao = new EmployDAO();
				Employee employee = dao.searchEmploy(empId);
				int mgr = employee.getMgrId();
				System.out.println(mgr);
				System.out.println(mgrId);
				if (mgr==mgrId) {
					if (status.toUpperCase().equals("YES")) {
						connection = ConnectionHelper.getConnection();
						String cmd = "Update leave_history set LEAVE_STATUS=?, "
								+ " LEAVE_MNGR_COMMENTS=? where "
								+ "Leave_ID=?";
						pst = connection.prepareStatement(cmd);
						pst.setString(1, LeaveStatus.APPROVED.toString());
						pst.setString(2, managerComments);
						pst.setInt(3, leaveId);
						pst.executeUpdate();
						return "Leave Approved...";
					} else {
						connection = ConnectionHelper.getConnection();
						String cmd = "Update leave_history set LEAVE_STATUS='DENIED', "
								+ " LEAVE_MNGR_COMMENTS=? where Leave_ID=?";
						pst = connection.prepareStatement(cmd);
						pst.setString(1, managerComments);
						pst.setInt(2, leaveId);
					
						pst.executeUpdate();
						cmd = "Update Employee set EMP_AVAIL_LEAVE_BAL=EMP_AVAIL_LEAVE_BAL+? "
								+ " where Emp_id=?";
						pst = connection.prepareStatement(cmd);
						pst.setInt(1, leaveDetails.getNoOfDays());
						pst.setInt(2, leaveDetails.getEmpId());
						pst.executeUpdate();
						return "Your Leave Denied Successfully... BT";
					}
				} else {
					return "You are unauthorized Manager...";
				}
			}
			return "Invalid LeaveId...";
		}
	
		public LeaveDetails[] leaveHistory(int empId) throws ClassNotFoundException, SQLException {
		
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from LEAVE_HISTORY Where Emp_ID=?";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, empId);
		ResultSet rs = pst.executeQuery();
		List<LeaveDetails> leaveList = new ArrayList<LeaveDetails>();
		LeaveDetails leave = null;
		
		while(rs.next()) {
			leave = new LeaveDetails();
			leave.setLeaveId(rs.getInt("LEAVE_ID"));
			leave.setNoOfDays(rs.getInt("LEAVE_NO_OF_DAYS"));
			leave.setManagerComments(rs.getString("LEAVE_MNGR_COMMENTS"));
			leave.setEmpId(rs.getInt("EMP_ID"));
			leave.setLeaveStartDate(rs.getDate("LEAVE_START_DATE"));
			leave.setLeaveEndDate(rs.getDate("LEAVE_END_DATE"));
			leave.setLeaveStatus(LeaveStatus.valueOf(rs.getString("LEAVE_STATUS")));
			leave.setLeaveType(LeaveType.valueOf(rs.getString("LEAVE_TYPE")));
			leave.setLeaveReason(rs.getString("LEAVE_REASON"));
			
			leaveList.add(leave);
		}
		
		return leaveList.toArray(new LeaveDetails[leaveList.size()]);
		
	}

	
	
public LeaveDetails[] pendingLeaves(int empId) throws ClassNotFoundException, SQLException {
		
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from LEAVE_HISTORY Where Emp_ID=? and Leave_Status='PENDING'";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, empId);
		ResultSet rs = pst.executeQuery();
		List<LeaveDetails> leaveList = new ArrayList<LeaveDetails>();
		LeaveDetails leave = null;
		
		while(rs.next()) {
			leave = new LeaveDetails();
			leave.setLeaveId(rs.getInt("LEAVE_ID"));
			leave.setNoOfDays(rs.getInt("LEAVE_NO_OF_DAYS"));
			leave.setManagerComments(rs.getString("LEAVE_MNGR_COMMENTS"));
			leave.setEmpId(rs.getInt("EMP_ID"));
			leave.setLeaveStartDate(rs.getDate("LEAVE_START_DATE"));
			leave.setLeaveEndDate(rs.getDate("LEAVE_END_DATE"));
			leave.setLeaveStatus(LeaveStatus.valueOf(rs.getString("LEAVE_STATUS")));
			leave.setLeaveType(LeaveType.valueOf(rs.getString("LEAVE_TYPE")));
			leave.setLeaveReason(rs.getString("LEAVE_REASON"));
			
			leaveList.add(leave);
		}
		
		return leaveList.toArray(new LeaveDetails[leaveList.size()]);
		
	}
	
	
	//SHOW LEAVE
	public LeaveDetails[] showLeave() throws ClassNotFoundException, SQLException {
		
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from LEAVE_HISTORY ";
		pst = connection.prepareStatement(cmd);
		ResultSet rs = pst.executeQuery();
		List<LeaveDetails> leaveList = new ArrayList<LeaveDetails>();
		LeaveDetails leave = null;
		
		while(rs.next()) {
			leave = new LeaveDetails();
			leave.setLeaveId(rs.getInt("LEAVE_ID"));
			leave.setNoOfDays(rs.getInt("LEAVE_NO_OF_DAYS"));
			leave.setManagerComments(rs.getString("LEAVE_MNGR_COMMENTS"));
			leave.setEmpId(rs.getInt("EMP_ID"));
			leave.setLeaveStartDate(rs.getDate("LEAVE_START_DATE"));
			leave.setLeaveEndDate(rs.getDate("LEAVE_END_DATE"));
			leave.setLeaveStatus(LeaveStatus.valueOf(rs.getString("LEAVE_STATUS")));
			leave.setLeaveType(LeaveType.valueOf(rs.getString("LEAVE_TYPE")));
			leave.setLeaveReason(rs.getString("LEAVE_REASON"));
			
			leaveList.add(leave);
		}
		
		return leaveList.toArray(new LeaveDetails[leaveList.size()]);
		
	}
	
	
	//ADD LEAVE
	public String applyLeave(LeaveDetails leaveDetails) throws ClassNotFoundException, SQLException {
		
		long ms = leaveDetails.getLeaveEndDate().getTime() - leaveDetails.getLeaveStartDate().getTime();
	    long m = ms / (1000 * 24 * 60 * 60);
	    int days = (int) m;
	    days = days + 1;
	    java.util.Date d1 = leaveDetails.getLeaveStartDate();
	    leaveDetails.setLeaveType(LeaveType.EL);
	    leaveDetails.setLeaveStatus(LeaveStatus.PENDING);
	    
	    java.util.Date today = new java.util.Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        int sun = 0;
        int sat = 0;
        int d3=days;
        while (d3>=0) {
            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                sun++;
            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY )
                sat++; 
            c1.add(Calendar.DATE, 1);
            d3--;
        }
	    days = days-(sat+sun);
	    long ms1 = leaveDetails.getLeaveEndDate().getTime() - today.getTime();
	    long m1 = ms1/(1000*24*60*60);
	    int edays1 = (int)m1;
	    edays1++;
	    
	    long ms2 = leaveDetails.getLeaveEndDate().getTime() - today.getTime();
	    long m2 = ms2/(1000*24*60*60);
	    int edays2 = (int)m2;
	    edays2++;
	    Employee employee = new EmployDAO().searchEmploy(leaveDetails.getEmpId());
	    int leaveBalance = employee.getLeaveAvail();
	    if (days < 0) {
	    	return "LeaveStartDate cannot be greater than LeaveEndDate...";
	    } else if (leaveBalance - days < 0) {
	    	return "Insufficient Leave Balance...";
	    } else if (edays1 < 0) {
	    	return "Leave End Date Cannot be Yesterday's Date...";
	    } else if (edays2 < 0) {
	    	return "Leave StartDate Cannot be Yesterday's Date...";
	    }
	    else {
	    	//int diff = leaveBalance - days;
	    	leaveDetails.setNoOfDays(days);
	    	connection = ConnectionHelper.getConnection();
	    	
	    	String cmd = "Insert into LEAVE_HISTORY(Emp_ID,Leave_Start_Date, "
	    			+ "Leave_End_Date,Leave_Type,Leave_Status,Leave_Reason,LEAVE_NO_OF_DAYS) "
	    			+ "VALUES(?,?,?,?,?,?,?)";
	    	pst = connection.prepareStatement(cmd);
	    	
	    	pst.setInt(1, leaveDetails.getEmpId());
	    	pst.setDate(2, leaveDetails.getLeaveStartDate());
	    	pst.setDate(3, leaveDetails.getLeaveEndDate());
	    	pst.setString(4, leaveDetails.getLeaveType().toString());
	    	pst.setString(5, leaveDetails.getLeaveStatus().toString());
	    	pst.setString(6, leaveDetails.getLeaveReason());
	    	pst.setInt(7, leaveDetails.getNoOfDays());
	    	pst.executeUpdate();
	    	
	    	cmd = "Update Employee set EMP_AVAIL_LEAVE_BAL=EMP_AVAIL_LEAVE_BAL-? WHERE "
	    			+ " EMP_ID=?";
	    	pst = connection.prepareStatement(cmd);
	    	pst.setInt(1, days);
	    	pst.setInt(2, leaveDetails.getEmpId());
	    	pst.executeUpdate();
	    	if(employee.getMgrId() == 0) {
				connection = ConnectionHelper.getConnection();
				cmd = "Update leave_history set LEAVE_STATUS=?, "
						+ " LEAVE_MNGR_COMMENTS=? where "
						+ "Emp_ID=?";
				pst = connection.prepareStatement(cmd);
				pst.setString(1,LeaveStatus.APPROVED.toString());
				pst.setString(2,LeaveStatus.APPROVED.toString());
				pst.setInt(3,employee.getEmpId());
				pst.executeUpdate();
	    	}
	    	return "Leave Applied Successfully...And LeaveBalance Updated...";
	    	
	    }
	    
	}
	
	
	public LeaveDetails searchLeave(int leaveId) throws ClassNotFoundException, SQLException {
		
		connection = ConnectionHelper.getConnection();
		
		String cmd = "select * from leave_history where LEAVE_ID=?";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, leaveId);
		ResultSet rs = pst.executeQuery();
		LeaveDetails leave = null;
		
		if (rs.next()) {
			leave = new LeaveDetails();
			leave.setLeaveId(rs.getInt("LEAVE_ID"));
			leave.setEmpId(rs.getInt("EMP_ID"));
			leave.setNoOfDays(rs.getInt("LEAVE_NO_OF_DAYS"));
			leave.setLeaveStartDate(rs.getDate("LEAVE_START_DATE"));
			leave.setLeaveEndDate(rs.getDate("LEAVE_END_DATE"));
			leave.setLeaveType(LeaveType.valueOf(rs.getString("LEAVE_TYPE")));
			leave.setLeaveStatus(LeaveStatus.valueOf(rs.getString("LEAVE_STATUS")));
			leave.setLeaveReason(rs.getString("LEAVE_REASON"));
			
		}
		return leave;
	}

}
