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

public partial class WISAAPI_GetSchedule : System.Web.UI.Page
{
	public struct GetScheduleRequest
	{
		public int StoreID;
	}

	public struct GetScheduleResponse
	{
		public List<Schedule> schedules;
		public string error;
	}

	public struct Schedule
	{
		public int ScheduleID, EmployeeID, ShiftStatus;
		public String EmpFirstName, EmpLastName;
		public DateTime StartOfShift, EndOfShift;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		GetScheduleRequest req;
		GetScheduleResponse res = new GetScheduleResponse();
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

			string sql = "SELECT s.ScheduleID,s.StartOfShift,s.EndOfShift,s.EmployeeID,e.FirstName EmpFirstName,e.LastName EmpLastName,s.ShiftStatus FROM Schedule s LEFT JOIN Employee e ON s.EmployeeID = e.EmployeeID WHERE s.StoreID = @StoreID AND DATEPART(WEEK, s.StartOfShift) = DATEPART(WEEK, getdate()) AND DATEPART(YEAR, s.StartOfShift) = DATEPART(YEAR, getdate())";
			SqlCommand command = new SqlCommand(sql, connection);
			command.Parameters.Add("@StoreID", SqlDbType.Int);
			command.Parameters["@StoreID"].Value = req.StoreID;
			
			res.schedules = new List<Schedule>();
			SqlDataReader reader = command.ExecuteReader();
			if(reader.HasRows)
			{
				while(reader.Read())
				{
					Schedule s = new Schedule();
					s.ScheduleID = Convert.ToInt32(reader["ScheduleID"]);
					s.EmployeeID = Convert.ToInt32(reader["EmployeeID"]);
					s.ShiftStatus = Convert.ToInt32(reader["ShiftStatus"]);
					s.EmpFirstName = Convert.ToString(reader["EmpFirstName"]);
					s.EmpLastName = Convert.ToString(reader["EmpLastName"]);
					DateTime.TryParse(Convert.ToString(reader["StartOfShift"]), out s.StartOfShift);
					DateTime.TryParse(Convert.ToString(reader["EndOfShift"]), out s.EndOfShift);
					
					res.schedules.Add(s);
				}
			}
			else
				res.error = "No records found";
			reader.Close();
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
	
	GetScheduleRequest GetRequestInfo()
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
		GetScheduleRequest req = JsonConvert.DeserializeObject<GetScheduleRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(GetScheduleResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
