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

public partial class WISAAPI_CreateEmployee : System.Web.UI.Page
{
	public struct CreateEmployeeRequest
	{
		public int StoreID, JobType;
		public string UserName, Password, FirstName, LastName, Email, Phone;
	}

	public struct CreateEmployeeResponse
	{
		public int EmployeeID, StoreID, JobType, Status;
		public string FirstName, LastName, Email, Phone;
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		CreateEmployeeRequest req;
		CreateEmployeeResponse res = new CreateEmployeeResponse();
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

			string getUserInfo = "SELECT EmployeeID,UserName,Password,FirstName,LastName,Email,Phone,StoreID,JobType,Status FROM Employee WHERE UserName = @UserName AND (Password = @Password OR 1 = @Flag)";
			SqlCommand getUserInfoCommand = new SqlCommand(getUserInfo, connection);
			getUserInfoCommand.Parameters.Add("@UserName", SqlDbType.NVarChar);
			getUserInfoCommand.Parameters.Add("@Password", SqlDbType.NVarChar);
			getUserInfoCommand.Parameters.Add("@Flag", SqlDbType.NVarChar);
			getUserInfoCommand.Parameters["@UserName"].Value = req.UserName;
			getUserInfoCommand.Parameters["@Password"].Value = req.Password;
			getUserInfoCommand.Parameters["@Flag"].Value = 1;
			
			SqlDataReader reader = getUserInfoCommand.ExecuteReader();
			if(reader.HasRows)
			{
				res.error = "Username already in use";
				SendResultInfoAsJson(res);
				return;
			}
			
			reader.Close();
			string sql = "INSERT INTO Employee(UserName,Password,FirstName,LastName,Email,Phone,StoreID,JobType,Status) Values(@UserName,@Password,@FirstName,@LastName,@Email,@Phone,@StoreID,@JobType,0)";
			SqlCommand createEmp = new SqlCommand(sql, connection);
			createEmp.Parameters.Add("@UserName", SqlDbType.NVarChar);
			createEmp.Parameters.Add("@Password", SqlDbType.NVarChar);
			createEmp.Parameters.Add("@FirstName", SqlDbType.NVarChar);
			createEmp.Parameters.Add("@LastName", SqlDbType.NVarChar);
			createEmp.Parameters.Add("@Email", SqlDbType.NVarChar);
			createEmp.Parameters.Add("@Phone", SqlDbType.NVarChar);
			createEmp.Parameters.Add("@StoreID", SqlDbType.Int);
			createEmp.Parameters.Add("@JobType", SqlDbType.Int);
			createEmp.Parameters["@UserName"].Value = req.UserName;
			createEmp.Parameters["@Password"].Value = req.Password;
			createEmp.Parameters["@FirstName"].Value = req.FirstName;
			createEmp.Parameters["@LastName"].Value = req.LastName;
			createEmp.Parameters["@Email"].Value = req.Email;
			createEmp.Parameters["@Phone"].Value = req.Phone;
			createEmp.Parameters["@StoreID"].Value = req.StoreID;
			createEmp.Parameters["@JobType"].Value = req.JobType;
			createEmp.ExecuteNonQuery();
			
			getUserInfoCommand.Parameters["@Flag"].Value = 0;
			reader = getUserInfoCommand.ExecuteReader();
			if(reader.HasRows)
				if(reader.Read())
				{
					res.EmployeeID = Convert.ToInt32(reader["EmployeeID"]);
					res.StoreID = Convert.ToInt32(reader["StoreID"]);
					res.JobType = Convert.ToInt32(reader["JobType"]);
					res.Status = Convert.ToInt32(reader["Status"]);
					res.FirstName = Convert.ToString( reader["FirstName"] );
					res.LastName = Convert.ToString( reader["LastName"] );
					res.Email = Convert.ToString( reader["Email"] );
					res.Phone = Convert.ToString( reader["Phone"] );
				}
			reader.Close();

			string createAvl = "INSERT INTO AvailabilityTbl (EmployeeID, DayOfWeek, StartTime, EndTime, Status) VALUES (@EmployeeID, 1, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1), (@EmployeeID, 2, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1), (@EmployeeID, 3, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1), (@EmployeeID, 4, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1), (@EmployeeID, 5, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1), (@EmployeeID, 6, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1), (@EmployeeID, 7, '1900-01-01T08:00:00', '1900-01-01T23:59:00', 1)";
			SqlCommand createAvailCmd = new SqlCommand(createAvl, connection);
			createAvailCmd.Parameters.Add("@EmployeeID", SqlDbType.Int);
			createAvailCmd.Parameters["@EmployeeID"].Value = res.EmployeeID;
			createAvailCmd.ExecuteNonQuery();
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
	
	CreateEmployeeRequest GetRequestInfo()
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
		CreateEmployeeRequest req = JsonConvert.DeserializeObject<CreateEmployeeRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(CreateEmployeeResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
