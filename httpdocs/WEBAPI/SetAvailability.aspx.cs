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

public partial class WISAAPI_SetAvailability : System.Web.UI.Page
{
	public struct SetAvailabilityRequest
	{
		public int EmployeeID;
		public List<Availability> days;
	}

	public struct SetAvailabilityResponse
	{
		public string error;
	}

	public struct Availability
	{
		public int Day;
		public DateTime StartTime, EndTime;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		SetAvailabilityRequest req;
		SetAvailabilityResponse res = new SetAvailabilityResponse();
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
			
			string delSql = "DELETE FROM AvailabilityTbl WHERE EmployeeID = @EmployeeID AND Status = 0";
			SqlCommand delCmd = new SqlCommand(delSql, connection);
			delCmd.Parameters.Add("@EmployeeID", SqlDbType.Int);
			delCmd.Parameters["@EmployeeID"].Value = req.EmployeeID;
			delCmd.ExecuteNonQuery();

			string insSql = "INSERT INTO AvailabilityTbl (EmployeeID, DayOfWeek, StartTime, EndTime, Status) VALUES (@EmployeeID, @DayOfWeek, @StartTime, @EndTime, 0)";
			SqlCommand insCmd = new SqlCommand(insSql, connection);
			insCmd.Parameters.Add("@EmployeeID", SqlDbType.Int);
			insCmd.Parameters.Add("@DayOfWeek", SqlDbType.Int);
			insCmd.Parameters.Add("@StartTime", SqlDbType.DateTime);
			insCmd.Parameters.Add("@EndTime", SqlDbType.DateTime);
			insCmd.Parameters["@EmployeeID"].Value = req.EmployeeID;

			foreach(Availability a in req.days)
			{
				insCmd.Parameters["@DayOfWeek"].Value = a.Day;
				insCmd.Parameters["@StartTime"].Value = a.StartTime;
				insCmd.Parameters["@EndTime"].Value = a.EndTime;

				insCmd.ExecuteNonQuery();
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
	
	SetAvailabilityRequest GetRequestInfo()
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
		SetAvailabilityRequest req = JsonConvert.DeserializeObject<SetAvailabilityRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(SetAvailabilityResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
