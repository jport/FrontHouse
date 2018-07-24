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

public partial class WISAAPI_CreateStore : System.Web.UI.Page
{
	public struct CreateStoreRequest
	{
		public int StoreNumber;
		public string StoreName, Address, State, City, Zip;
	}

	public struct CreateStoreResponse
	{
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		CreateStoreRequest req;
		CreateStoreResponse res = new CreateStoreResponse();
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

			string sql = "INSERT INTO Store (StoreName, StoreNumber, Address, State, City, Zip) VALUES (@StoreName, @StoreNumber, @Address, @State, @City, @Zip)";
			SqlCommand createStore = new SqlCommand(sql, connection);
			createStore.Parameters.Add("@StoreName", SqlDbType.NVarChar);
			createStore.Parameters.Add("@StoreNumber", SqlDbType.NVarChar);
			createStore.Parameters.Add("@Address", SqlDbType.NVarChar);
			createStore.Parameters.Add("@State", SqlDbType.NVarChar);
			createStore.Parameters.Add("@City", SqlDbType.NVarChar);
			createStore.Parameters.Add("@Zip", SqlDbType.NVarChar);
			createStore.Parameters["@StoreName"].Value = req.StoreName;
			createStore.Parameters["@StoreNumber"].Value = req.StoreNumber;
			createStore.Parameters["@Address"].Value = req.Address;
			createStore.Parameters["@State"].Value = req.State;
			createStore.Parameters["@City"].Value = req.City;
			createStore.Parameters["@Zip"].Value = req.Zip;
			createStore.ExecuteNonQuery();
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
	
	CreateStoreRequest GetRequestInfo()
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
		CreateStoreRequest req = JsonConvert.DeserializeObject<CreateStoreRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(CreateStoreResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
