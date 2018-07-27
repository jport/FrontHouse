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

public partial class WISAAPI_GetAllEmployees : System.Web.UI.Page
{
	public struct GetAllEmployeesRequest
	{
		public int StoreID;
	}

	public struct GetAllEmployeesResponse
	{
		public List<Employee> employees;
		public String error;
	}

	public struct Employee
	{
		public int JobType;
		public String FirstName, LastName, Phone;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		GetAllEmployeesRequest req;
		GetAllEmployeesResponse res = new GetAllEmployeesResponse();
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

			string getAllEmps = @"SELECT FirstName, LastName, Phone, JobType FROM Employee WHERE StoreID = @StoreID AND Status = 0";
			SqlCommand getAllEmpsCmd = new SqlCommand(getAllEmps, connection);
			getAllEmpsCmd.Parameters.Add("@StoreID", SqlDbType.Int);
			getAllEmpsCmd.Parameters["@StoreID"].Value = req.StoreID;
			
			SqlDataReader reader = getAllEmpsCmd.ExecuteReader();
			res.employees = new List<Employee>();
			while(reader.Read())
			{
				Employee curE = new Employee();
				curE.FirstName = Convert.ToString(reader["FirstName"]);
				curE.LastName = Convert.ToString(reader["LastName"]);
				curE.Phone = Convert.ToString(reader["Phone"]);
				curE.JobType = Convert.ToInt32(reader["JobType"]);

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
	
	GetAllEmployeesRequest GetRequestInfo()
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
		GetAllEmployeesRequest req = JsonConvert.DeserializeObject<GetAllEmployeesRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(GetAllEmployeesResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
