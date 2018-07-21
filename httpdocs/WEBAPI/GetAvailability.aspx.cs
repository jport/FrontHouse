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

public partial class WISAAPI_GetAvailability : System.Web.UI.Page
{
	public struct GetAvailabilityRequest
	{
                public int EmployeeID;
	}

	public struct GetAvailabilityResponse
	{
		public List<Availability> days;
		public string error;
	}

        public struct Availability
        {
		public int AvailabilityID, Day;
		public DateTime StartTime, EndTime;
        }

	protected void Page_Load(object sender, EventArgs e)
	{
		GetAvailabilityRequest req;
		GetAvailabilityResponse res = new GetAvailabilityResponse();
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

			string sql = "SELECT AvailID, EmployeeID, DayOfWeek, StartTime, EndTime FROM AvailabilityTbl WHERE EmployeeID = @EmployeeID AND Status = 1 ORDER BY DayOfWeek";
			SqlCommand command = new SqlCommand(sql, connection);
			command.Parameters.Add("@EmployeeID", SqlDbType.Int);
			command.Parameters["@EmployeeID"].Value = req.EmployeeID;
			
			res.days = new List<Availability>();
			SqlDataReader reader = command.ExecuteReader();
			if(reader.HasRows)
			{
				while(reader.Read())
				{
					Availability a = new Availability();
					a.AvailabilityID = Convert.ToInt32(reader["AvailID"]);
					a.Day = Convert.ToInt32(reader["DayOfWeek"]);
					DateTime.TryParse(Convert.ToString(reader["StartTime"]), out a.StartTime);
					DateTime.TryParse(Convert.ToString(reader["EndTime"]), out a.EndTime);
					
					res.days.Add(a);
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
	
	GetAvailabilityRequest GetRequestInfo()
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
		GetAvailabilityRequest req = JsonConvert.DeserializeObject<GetAvailabilityRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(GetAvailabilityResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
