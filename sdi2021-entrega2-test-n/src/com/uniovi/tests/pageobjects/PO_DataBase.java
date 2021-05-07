package com.uniovi.tests.pageobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class PO_DataBase {


	public static void InitMongoDB() {
		Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
		String connectionString = "mongodb://admin:sdi@tiendamusica-shard-00-00.jxgw2.mongodb.net:27017,tiendamusica-shard-00-01.jxgw2.mongodb.net:27017,tiendamusica-shard-00-02.jxgw2.mongodb.net:27017/myWallapop?ssl=true&replicaSet=atlas-od6pn1-shard-0&authSource=admin&retryWrites=true&w=majority";
		try(MongoClient mongoclient = MongoClients.create(connectionString)) {
			List<String> databases = mongoclient.listDatabaseNames().into(new ArrayList<>());
			System.out.println(databases);
			
			//		MongoClient mongo = new MongoClient("127.0.0.1", 27017);
//		System.out.println("Connection Established");
//			
//			MongoIterable<String> strings = mongoclient.listDatabaseNames();
//			MongoCursor<String> cursor = strings.cursor();
//			while (cursor.hasNext()) {
//				System.out.println(cursor.next());
//			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void InitDummyData() {

	}

	public static void ResetDummyData() {

	}

}
