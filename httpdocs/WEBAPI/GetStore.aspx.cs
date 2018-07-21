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

public partial class WISAAPI_GetStore : System.Web.UI.Page
{
	public struct GetStoreRequest
	{
		public int StoreID;
	}

	public struct GetStoreResponse
	{
		public int StoreID, StoreNumber;
		public string Address, State, City, Zip;
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		GetStoreRequest req;
		GetStoreResponse res = new GetStoreResponse();
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

			string getStoreInfo = "SELECT StoreID,StoreNumber,Address,State,City,Zip FROM Store WHERE StoreID = @StoreID";
			SqlCommand getStoreInfoCommand = new SqlCommand(getStoreInfo, connection);
			getStoreInfoCommand.Parameters.Add("@StoreID", SqlDbType.Int);
			getStoreInfoCommand.Parameters["@StoreID"].Value = req.StoreID;
			
			SqlDataReader reader = getStoreInfoCommand.ExecuteReader();
			if(!reader.HasRows)
			{
				res.error = "Store not found";
				SendResultInfoAsJson(res);
				return;
			}
			else
			{
				if(reader.Read())
				{
					res.Address = Convert.ToString(reader["Address"]);
					res.State = Convert.ToString(reader["State"]);
					res.City = Convert.ToString(reader["City"]);
					res.Zip = Convert.ToString(reader["Zip"]);
					res.StoreNumber = Convert.ToInt32(reader["StoreNumber"]);
					res.StoreID = Convert.ToInt32(reader["StoreID"]);
				}
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
	
	GetStoreRequest GetRequestInfo()
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
		GetStoreRequest req = JsonConvert.DeserializeObject<GetStoreRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(GetStoreResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
