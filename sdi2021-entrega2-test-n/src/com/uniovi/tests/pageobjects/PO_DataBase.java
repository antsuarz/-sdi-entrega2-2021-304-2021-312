package com.uniovi.tests.pageobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;

public class PO_DataBase {

//	private static int port = 8081;
	// antonio
	private static String connectionString = "mongodb://admin:sdi@tiendamusica-shard-00-00.jxgw2.mongodb.net:27017,tiendamusica-shard-00-01.jxgw2.mongodb.net:27017,tiendamusica-shard-00-02.jxgw2.mongodb.net:27017/myWallapop?ssl=true&replicaSet=atlas-od6pn1-shard-0&authSource=admin&retryWrites=true&w=majority";
//	private static String clave = "abcdefg";
//	private static String crypto = "crypto";
	private static String AppDBname = "myWallapop";
	private static List<Document> usuarios = new ArrayList<Document>();
	private static List<Document> ofertas = new ArrayList<Document>();
	private static List<Document> compras = new ArrayList<Document>();
	// pablo
//	private static String connectionString = "mongodb://admin:sdi@tiendamusica-shard-00-00.any5v.mongodb.net:27017,tiendamusica-shard-00-01.any5v.mongodb.net:27017,tiendamusica-shard-00-02.any5v.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-exyqcf-shard-0&authSource=admin&retryWrites=true&w=majority";

	private void insertUsers() {
		for (int i = 0; i < 10; i++) {
			usuarios.add(new Document("email", "testprueba" + i + "@gmail.com").append("name", "testprueba" + i)
					.append("surname", "test").append("password", "testprueba").append("rol", "estandar")
					.append("dinero", 100).append("test", true));
		}
	}

	private void insertOfertas() {
		for (int i = 0; i < 10; i++) {
//			usuarios.add(new Document("email","testprueba"+i+"@gmail.com").append("name", "testprueba"+i).append("surname", "test").append("password","testprueba").append("rol", "estandar").append("dinero", 100).append("test",true));
		}
	}

	private void insertCompras() {
		for (int i = 0; i < 10; i++) {
//			usuarios.add(new Document("email","testprueba"+i+"@gmail.com").append("name", "testprueba"+i).append("surname", "test").append("password","testprueba").append("rol", "estandar").append("dinero", 100).append("test",true));
		}
	}

	public static void showDataOfDB() {
		Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			List<String> databases = mongoclient.listDatabaseNames().into(new ArrayList<>());
			System.out.println("databases" + databases);

			List<String> collectionNames = mongoclient.getDatabase(AppDBname).listCollectionNames()
					.into(new ArrayList<>());
			System.out.println("collectionNames" + collectionNames);
			MongoIterable<String> it = mongoclient.getDatabase(AppDBname).listCollectionNames();
			MongoCursor<String> cursor = it.cursor();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}

//			List<String> elements = mongoclient.

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void InitDummyData() {

		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			mongoclient.getDatabase(AppDBname).getCollection("usuarios").insertMany(usuarios);
			mongoclient.getDatabase(AppDBname).getCollection("ofertas").insertMany(ofertas);
			mongoclient.getDatabase(AppDBname).getCollection("compras").insertMany(compras);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void ResetDummyData() {
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			MongoCollection<Document> collectionMongo = mongoclient.getDatabase(AppDBname).getCollection("");
			collectionMongo.deleteMany(new Document("surname", "test"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
