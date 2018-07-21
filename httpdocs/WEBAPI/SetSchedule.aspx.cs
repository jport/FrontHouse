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

public partial class WISAAPI_SetSchedule : System.Web.UI.Page
{
	public struct SetScheduleRequest
	{
		public int StoreID;
		public List<Schedule> schedules;
	}

	public struct SetScheduleResponse
	{
		public string error;
	}

	public struct Schedule
	{
		public int EmployeeID;
		public DateTime StartOfShift, EndOfShift;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		SetScheduleRequest req;
		SetScheduleResponse res = new SetScheduleResponse();
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

			string sql = "INSERT INTO Schedule (StoreID, StartOfShift, EndOfShift, EmployeeID, ShiftStatus) VALUES (@StoreID, @StartOfShift, @EndOfShift, @EmployeeID, 1)";
			SqlCommand cmd = new SqlCommand(sql, connection);
			cmd.Parameters.Add("@StoreID", SqlDbType.Int);
			cmd.Parameters.Add("@StartOfShift", SqlDbType.DateTime);
			cmd.Parameters.Add("@EndOfShift", SqlDbType.DateTime);
			cmd.Parameters.Add("@EmployeeID", SqlDbType.Int);
			cmd.Parameters["@StoreID"].Value = req.StoreID;
			
			foreach(Schedule s in req.schedules)
			{
				cmd.Parameters["@StartOfShift"].Value = s.StartOfShift;
				cmd.Parameters["@EndOfShift"].Value = s.EndOfShift;
				cmd.Parameters["@EmployeeID"].Value = s.EmployeeID;

				cmd.ExecuteNonQuery();
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
	
	SetScheduleRequest GetRequestInfo()
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
		SetScheduleRequest req = JsonConvert.DeserializeObject<SetScheduleRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(SetScheduleResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
