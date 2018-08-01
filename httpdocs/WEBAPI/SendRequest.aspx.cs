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

public partial class WISAAPI_SendRequest : System.Web.UI.Page
{
	public struct SendRequestRequest
	{
		public int StoreID, EmployeeID, RequestType, ScheduleID1, ScheduleID2;
		public String RequestText;
	}

	public struct SendRequestResponse
	{
		public int RequestID;
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		SendRequestRequest req;
		SendRequestResponse res = new SendRequestResponse();
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
		
		if(req.RequestType <= 0 || req.RequestType > 4)
		{
			res.error = "Invalid request type";
			SendResultInfoAsJson(res);
			return;
		}

		SqlConnection connection = new SqlConnection(ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString);
		try
		{
			connection.Open();

			string sql = "INSERT INTO Request(StoreID,EmployeeID,RequestType,RequestStatus,RequestText,ScheduleID1,ScheduleID2) Values(@StoreID,@EmployeeID,@RequestType,0,@RequestText,@ScheduleID1,@ScheduleID2)";
			SqlCommand createReq = new SqlCommand(sql, connection);
			createReq.Parameters.Add("@StoreID", SqlDbType.Int);
			createReq.Parameters.Add("@EmployeeID", SqlDbType.Int);
			createReq.Parameters.Add("@RequestType", SqlDbType.Int);
			createReq.Parameters.Add("@RequestText", SqlDbType.NVarChar);
			createReq.Parameters.Add("@ScheduleID1", SqlDbType.Int);
			createReq.Parameters.Add("@ScheduleID2", SqlDbType.Int);
			createReq.Parameters["@StoreID"].Value = req.StoreID;
			createReq.Parameters["@EmployeeID"].Value = req.EmployeeID;
			createReq.Parameters["@RequestType"].Value = req.RequestType;
			createReq.Parameters["@RequestText"].Value = req.RequestText;
			createReq.Parameters["@ScheduleID1"].Value = req.ScheduleID1;
			createReq.Parameters["@ScheduleID2"].Value = req.ScheduleID2 == 0 ? req.ScheduleID1 : req.ScheduleID2;
			createReq.ExecuteNonQuery();
			
			string getSql = "SELECT TOP 1 RequestID FROM Request WHERE StoreID = @StoreID AND EmployeeID = @EmployeeID AND RequestType = @RequestType AND RequestText = @RequestText AND ScheduleID1 = @ScheduleID1 AND ScheduleID2 = @ScheduleID2 ORDER BY RequestID desc";
			SqlCommand getReq = new SqlCommand(getSql, connection);
			getReq.Parameters.Add("@StoreID", SqlDbType.Int);
			getReq.Parameters.Add("@EmployeeID", SqlDbType.Int);
			getReq.Parameters.Add("@RequestType", SqlDbType.Int);
			getReq.Parameters.Add("@RequestText", SqlDbType.NVarChar);
			getReq.Parameters.Add("@ScheduleID1", SqlDbType.Int);
			getReq.Parameters.Add("@ScheduleID2", SqlDbType.Int);
			getReq.Parameters["@StoreID"].Value = req.StoreID;
			getReq.Parameters["@EmployeeID"].Value = req.EmployeeID;
			getReq.Parameters["@RequestType"].Value = req.RequestType;
			getReq.Parameters["@RequestText"].Value = req.RequestText;
			getReq.Parameters["@ScheduleID1"].Value = req.ScheduleID1;
			getReq.Parameters["@ScheduleID2"].Value = req.ScheduleID2 == 0 ? req.ScheduleID1 : req.ScheduleID2;
			
			SqlDataReader reader = getReq.ExecuteReader();
			if(reader.Read())
				res.RequestID = Convert.ToInt32(reader["RequestID"]);
			else
				res.error = "Something went wrong";
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
	
	SendRequestRequest GetRequestInfo()
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
		SendRequestRequest req = JsonConvert.DeserializeObject<SendRequestRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(SendRequestResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
