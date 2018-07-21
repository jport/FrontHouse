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

public partial class WISAAPI_UpdateStore : System.Web.UI.Page
{
	public struct UpdateStoreRequest
	{
		public int StoreID, StoreNumber;
		public string Address, State, City, Zip;
	}

	public struct UpdateStoreResponse
	{
		public string error;
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		UpdateStoreRequest req;
		UpdateStoreResponse res = new UpdateStoreResponse();
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
				reader.Read();
				if(req.Address == null)
					req.Address = Convert.ToString(reader["Address"]);
				if(req.State == null)
					req.State = Convert.ToString(reader["State"]);
				if(req.City == null)
					req.City = Convert.ToString(reader["City"]);
				if(req.Zip == null)
					req.Zip = Convert.ToString(reader["Zip"]);
				if(req.StoreNumber == -1)
					req.StoreNumber = Convert.ToInt32(reader["StoreNumber"]);
			}
			
			reader.Close();

			string sql = "UPDATE Store SET StoreNumber = @StoreNumber, Address = @Address, State = @State, City = @City, Zip = @Zip WHERE StoreID = @StoreID";
			SqlCommand updateStore = new SqlCommand(sql, connection);
			updateStore.Parameters.Add("@StoreID", SqlDbType.Int);
			updateStore.Parameters.Add("@StoreNumber", SqlDbType.NVarChar);
			updateStore.Parameters.Add("@Address", SqlDbType.NVarChar);
			updateStore.Parameters.Add("@State", SqlDbType.NVarChar);
			updateStore.Parameters.Add("@City", SqlDbType.NVarChar);
			updateStore.Parameters.Add("@Zip", SqlDbType.NVarChar);
			updateStore.Parameters["@StoreID"].Value = req.StoreID;
			updateStore.Parameters["@StoreNumber"].Value = req.StoreNumber;
			updateStore.Parameters["@Address"].Value = req.Address;
			updateStore.Parameters["@State"].Value = req.State;
			updateStore.Parameters["@City"].Value = req.City;
			updateStore.Parameters["@Zip"].Value = req.Zip;
			updateStore.ExecuteNonQuery();
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
	
	UpdateStoreRequest GetRequestInfo()
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
		UpdateStoreRequest req = JsonConvert.DeserializeObject<UpdateStoreRequest>(strJson);

		return (req);
	}
	
	void SendResultInfoAsJson(UpdateStoreResponse res)
	{
		string strJson = JsonConvert.SerializeObject(res);
		Response.ContentType = "application/json; charset=utf-8";
		Response.AppendHeader("Access-Control-Allow-Origin", "*");
		Response.Write(strJson);
		Response.End();
	}

}
