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

public partial class WISAAPI_UpdateEmployee : System.Web.UI.Page
{
	public struct UpdateEmployeeRequest
	{
		public int EmployeeID, JobType, Status;
		public string FirstName, LastName, Email, Phone, Password;
	}

	public struct UpdateEmployeeResponse
	{
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		UpdateEmployeeRequest req;
		UpdateEmployeeResponse res = new UpdateEmployeeResponse();
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

			string getUserInfo = "SELECT EmployeeID,UserName,Password,FirstName,LastName,Email,Phone,StoreID,JobType,Status FROM Employee WHERE EmployeeID = @EmployeeID";
			SqlCommand getUserInfoCommand = new SqlCommand(getUserInfo, connection);
			getUserInfoCommand.Parameters.Add("@EmployeeID", SqlDbType.Int);
			getUserInfoCommand.Parameters["@EmployeeID"].Value = req.EmployeeID;
			
			SqlDataReader reader = getUserInfoCommand.ExecuteReader();
			if(!reader.HasRows)
			{
				res.error = "Employee not found";
				SendResultInfoAsJson(res);
				return;
			}
			else
			{
				reader.Read();
				if(req.Password == null)
					req.Password = Convert.ToString(reader["Password"]);
				if(req.FirstName == null)
					req.FirstName = Convert.ToString(reader["FirstName"]);
				if(req.LastName == null)
					req.LastName = Convert.ToString(reader["LastName"]);
				if(req.Email == null)
					req.Email = Convert.ToString(reader["Email"]);
				if(req.Phone == null)
					req.Phone = Convert.ToString(reader["Phone"]);
				if(req.JobType == -1)
					req.JobType = Convert.ToInt32(reader["JobType"]);
				if(req.Status == -1)
					req.Status = Convert.ToInt32(reader["Status"]);
			}
			
			reader.Close();

			string sql = "UPDATE Employee SET Password = @Password, FirstName = @FirstName, LastName = @LastName, Email = @Email, Phone = @Phone, JobType = @JobType, Status = @Status WHERE EmployeeID = @EmployeeID";
			SqlCommand updateEmp = new SqlCommand(sql, connection);
			updateEmp.Parameters.Add("@EmployeeID", SqlDbType.Int);
			updateEmp.Parameters.Add("@Password", SqlDbType.NVarChar);
			updateEmp.Parameters.Add("@FirstName", SqlDbType.NVarChar);
			updateEmp.Parameters.Add("@LastName", SqlDbType.NVarChar);
			updateEmp.Parameters.Add("@Email", SqlDbType.NVarChar);
			updateEmp.Parameters.Add("@Phone", SqlDbType.NVarChar);
			updateEmp.Parameters.Add("@JobType", SqlDbType.Int);
			updateEmp.Parameters.Add("@Status", SqlDbType.Int);
			updateEmp.Parameters["@EmployeeID"].Value = req.EmployeeID;
			updateEmp.Parameters["@Password"].Value = req.Password;
			updateEmp.Parameters["@FirstName"].Value = req.FirstName;
			updateEmp.Parameters["@LastName"].Value = req.LastName;
			updateEmp.Parameters["@Email"].Value = req.Email;
			updateEmp.Parameters["@Phone"].Value = req.Phone;
			updateEmp.Parameters["@JobType"].Value = req.JobType;
			updateEmp.Parameters["@Status"].Value = req.Status;
			updateEmp.ExecuteNonQuery();
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
	
	UpdateEmployeeRequest GetRequestInfo()
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
		UpdateEmployeeRequest req = JsonConvert.DeserializeObject<UpdateEmployeeRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(UpdateEmployeeResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
