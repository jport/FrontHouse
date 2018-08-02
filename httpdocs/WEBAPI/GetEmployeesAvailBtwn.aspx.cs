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

//Example JSON Payload:
//{StoreID:1, StartDate:"2018-07-22 12:00:00.00", EndDate:"2018-07-22 23:00:00.00"}

public partial class WISAAPI_GetEmployeesAvailBtwn : System.Web.UI.Page
{
	public struct GetEmployeesAvailBtwnRequest
	{
		public int StoreID;
		public DateTime StartDate, EndDate;
	}

	public struct GetEmployeesAvailBtwnResponse
	{
		public List<Employee> employees;
		public String error;
	}

	public struct Employee
	{
		public int EmployeeID;
		public String FirstName, LastName;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		GetEmployeesAvailBtwnRequest req;
		GetEmployeesAvailBtwnResponse res = new GetEmployeesAvailBtwnResponse();
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

			string getAvailEmps = @"SELECT EmployeeID, FirstName, LastName
				FROM Employee
				WHERE EmployeeID IN
				    (
					SELECT DISTINCT e.EmployeeID
					FROM Employee e
					LEFT JOIN AvailabilityTbl a ON e.EmployeeID = a.EmployeeID
					    AND a.DayOfWeek = DATEPART(WEEKDAY, @StartDate)
					WHERE
					    Cast(a.StartTime as time) >= Cast(@StartDate as time)
					    AND Cast(a.EndTime as time) >= Cast(@EndDate as time)
				    )
				    AND EmployeeID NOT IN
				    (
					SELECT DISTINCT e.EmployeeID
					FROM Employee e
					LEFT JOIN Schedule s ON e.EmployeeID = s.EmployeeID
					WHERE
					    s.StartOfShift >= @StartDate
					    AND s.EndOfShift >= @EndDate
				    )
				    AND StoreID = @StoreID
				    AND JobType = 1";
			
			SqlCommand getAvailEmpsCmd = new SqlCommand(getAvailEmps, connection);
			getAvailEmpsCmd.Parameters.Add("@StoreID", SqlDbType.Int);
			getAvailEmpsCmd.Parameters.Add("@StartDate", SqlDbType.DateTime);
			getAvailEmpsCmd.Parameters.Add("@EndDate", SqlDbType.DateTime);
			getAvailEmpsCmd.Parameters["@StoreID"].Value = req.StoreID;
			getAvailEmpsCmd.Parameters["@StartDate"].Value = req.StartDate;
			getAvailEmpsCmd.Parameters["@EndDate"].Value = req.EndDate;
			
			SqlDataReader reader = getAvailEmpsCmd.ExecuteReader();
			res.employees = new List<Employee>();
			while(reader.Read())
			{
				Employee curE = new Employee();
				curE.EmployeeID = Convert.ToInt32(reader["EmployeeID"]);
				curE.FirstName = Convert.ToString(reader["FirstName"]);
				curE.LastName = Convert.ToString(reader["LastName"]);

				res.employees.Add(curE);
			}
			
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
	
	GetEmployeesAvailBtwnRequest GetRequestInfo()
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
		GetEmployeesAvailBtwnRequest req = JsonConvert.DeserializeObject<GetEmployeesAvailBtwnRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(GetEmployeesAvailBtwnResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
