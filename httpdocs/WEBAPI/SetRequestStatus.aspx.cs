using System;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Newtonsoft.Json;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.IO;

public partial class WISAAPI_SetRequestStatus : System.Web.UI.Page
{
	public struct SetRequestStatusRequest
	{
		public int RequestID, RequestStatus;
	}

	public struct SetRequestStatusResponse
	{
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		SetRequestStatusRequest req;
		SetRequestStatusResponse res = new SetRequestStatusResponse();
		res.error = String.Empty;
		
		// 1. Deserialize the incoming Json.
		try
		{
			req = GetRequestInfo();
		}
		catch(Exception ex)
		{
			res.error = ex.Message.ToString();

			// Return the results as Json.
			SendResultInfoAsJson(res);
			return;
		}

		SqlConnection connection = new SqlConnection(ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString);
		try
		{
			connection.Open();

			string getRequestInfo = "SELECT EmployeeID, RequestStatus, RequestType, ScheduleID1, ScheduleID2 FROM Request WHERE RequestID = @RequestID";
			SqlCommand getRequestInfoCommand = new SqlCommand(getRequestInfo, connection);
			getRequestInfoCommand.Parameters.Add("@RequestID", SqlDbType.Int);
			getRequestInfoCommand.Parameters["@RequestID"].Value = req.RequestID;
			
			SqlDataReader reader = getRequestInfoCommand.ExecuteReader();
			if(!reader.HasRows)
			{
				res.error = "Store not found";
				SendResultInfoAsJson(res);
				return;
			}
			else
			{
				reader.Read();
				if(Convert.ToInt32(reader["RequestStatus"]) != 0)
				{
					res.error = "Request already set";
					SendResultInfoAsJson(res);
					return;
				}
			}
			int scheduleID = Convert.ToInt32(reader["ScheduleID1"]);
			int empId = Convert.ToInt32(reader["EmployeeID"]);
			int requestType = Convert.ToInt32(reader["RequestType"]);
			int scheduleID1 = Convert.ToInt32(reader["ScheduleID1"]);
			int scheduleID2 = Convert.ToInt32(reader["ScheduleID2"]);
			int emp1 = -1, emp2 = -1;
			reader.Close();

			string sql = "UPDATE Request SET RequestStatus = @RequestStatus WHERE RequestID = @RequestID";
			SqlCommand setRequestStatusCMD = new SqlCommand(sql, connection);
			setRequestStatusCMD.Parameters.Add("@RequestID", SqlDbType.Int);
			setRequestStatusCMD.Parameters.Add("@RequestStatus", SqlDbType.Int);
			setRequestStatusCMD.Parameters["@RequestID"].Value = req.RequestID;
			setRequestStatusCMD.Parameters["@RequestStatus"].Value = req.RequestStatus;
			setRequestStatusCMD.ExecuteNonQuery();
			
			if(req.RequestStatus == 1)
			{
				
				if(requestType == 1) //drop shift
				{
					string updateSql = "UPDATE Schedule SET ShiftStatus = 0 WHERE ScheduleID = @ScheduleID";
					SqlCommand doRequest = new SqlCommand(updateSql, connection);
					doRequest.Parameters.Add("@ScheduleID", SqlDbType.Int);
					doRequest.Parameters["@ScheduleID"].Value = scheduleID;
					doRequest.ExecuteNonQuery();
				}
				else if(requestType == 2) //pickup shift
				{
					string updateSql = "UPDATE Schedule SET ShiftStatus = 1, EmployeeID = @EmployeeID WHERE ScheduleID = @ScheduleID";
					SqlCommand doRequest = new SqlCommand(updateSql, connection);
					doRequest.Parameters.Add("@ScheduleID", SqlDbType.Int);
					doRequest.Parameters.Add("@EmployeeID", SqlDbType.Int);
					doRequest.Parameters["@ScheduleID"].Value = scheduleID;
					doRequest.Parameters["@EmployeeID"].Value = empId;
					doRequest.ExecuteNonQuery();
				}
				else if(requestType == 3) //swap shift
				{
					string getSchedules = "SELECT EmployeeID, ScheduleID FROM Schedule WHERE ScheduleID IN (@ScheduleID1, @ScheduleID2)";
					SqlCommand getSchedulesCmd = new SqlCommand(getSchedules, connection);
					getSchedulesCmd.Parameters.Add("@ScheduleID1", SqlDbType.Int);
					getSchedulesCmd.Parameters.Add("@ScheduleID2", SqlDbType.Int);
					getSchedulesCmd.Parameters["@ScheduleID1"].Value = scheduleID1;
					getSchedulesCmd.Parameters["@ScheduleID2"].Value = scheduleID2;
					
					SqlDataReader schReader = getSchedulesCmd.ExecuteReader();
					while(schReader.Read())
					{
						int cSchID = Convert.ToInt32(schReader["ScheduleID"]);
						if(cSchID == scheduleID1)
							emp1 = Convert.ToInt32(schReader["EmployeeID"]);
						else
							emp2 = Convert.ToInt32(schReader["EmployeeID"]);
					}
					schReader.Close();
					
					string updateSql = "UPDATE Schedule SET EmployeeID = @EmployeeID2 WHERE ScheduleID = @ScheduleID1;UPDATE Schedule SET EmployeeID = @EmployeeID1 WHERE ScheduleID = @ScheduleID2;";
					SqlCommand doRequest = new SqlCommand(updateSql, connection);
					doRequest.Parameters.Add("@ScheduleID1", SqlDbType.Int);
					doRequest.Parameters.Add("@ScheduleID2", SqlDbType.Int);
					doRequest.Parameters.Add("@EmployeeID1", SqlDbType.Int);
					doRequest.Parameters.Add("@EmployeeID2", SqlDbType.Int);
					doRequest.Parameters["@ScheduleID1"].Value = scheduleID1;
					doRequest.Parameters["@ScheduleID2"].Value = scheduleID2;
					doRequest.Parameters["@EmployeeID1"].Value = emp1;
					doRequest.Parameters["@EmployeeID2"].Value = emp2;
					doRequest.ExecuteNonQuery();
				}
				else if(requestType == 4) //accept availability
				{
					
				}
			}
		}
		catch(Exception ex)
		{
			res.error = ex.Message.ToString();
		}
		finally
		{
			if( connection.State == ConnectionState.Open )
			{
				connection.Close();
			}
		}
		
		// Return the results as Json.
		SendResultInfoAsJson(res);
	}
	
	SetRequestStatusRequest GetRequestInfo()
	{
		// Get the Json from the POST.
		string strJson = String.Empty;
		HttpContext context = HttpContext.Current;
		context.Request.InputStream.Position = 0;
		using (StreamReader inputStream = new StreamReader(context.Request.InputStream))
		{
			strJson = inputStream.ReadToEnd();
		}

		// Deserialize the Json.
		SetRequestStatusRequest req = JsonConvert.DeserializeObject<SetRequestStatusRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(SetRequestStatusResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
