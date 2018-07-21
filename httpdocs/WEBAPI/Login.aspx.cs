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

public partial class WISAAPI_Login : System.Web.UI.Page
{
	public struct LoginRequest
	{
		public string login, password;
	}

	public struct LoginResponse
	{
		public int EmployeeID, StoreID, JobType, Status;
		public string FirstName, LastName, Email, Phone;
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		LoginRequest req;
		LoginResponse res = new LoginResponse();
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

			string sql = "SELECT EmployeeID,UserName,Password,FirstName,LastName,Email,Phone,StoreID,JobType,Status FROM Employee WHERE UserName = @userName AND Password = @Password";
			SqlCommand command = new SqlCommand(sql, connection);
			command.Parameters.Add("@userName", SqlDbType.NVarChar);
			command.Parameters.Add("@Password", SqlDbType.NVarChar);
			command.Parameters["@userName"].Value = req.login;
			command.Parameters["@password"].Value = req.password;
			
			SqlDataReader reader = command.ExecuteReader();
			if(reader.HasRows)
			{
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
			}
			else
				res.error = "User name and/or password not found";
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
	
	LoginRequest GetRequestInfo()
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
		LoginRequest req = JsonConvert.DeserializeObject<LoginRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(LoginResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
