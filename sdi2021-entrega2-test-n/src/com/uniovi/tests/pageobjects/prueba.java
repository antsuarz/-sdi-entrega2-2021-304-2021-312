package com.uniovi.tests.pageobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

public class prueba {
	
	private static String connectionString = "mongodb://sdi:sdi@cluster0-shard-00-00.p4zvd.mongodb.net:27017,cluster0-shard-00-01.p4zvd.mongodb.net:27017,cluster0-shard-00-02.p4zvd.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-12gia1-shard-0&authSource=admin&retryWrites=true&w=majority";
	
	public static void initialice() {
		List<Document> usuarios = new ArrayList<Document>();
		
		usuarios.add(new Document("email","PacoGonzalez@gmail.com").append("name", "Paco").append("surname", "Test").append("password","6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a").append("rol", "estandar").append("dinero", 100).append("test",true));
		usuarios.add(new Document("email","MariaDelagado@gmail.com").append("name", "Maria").append("surname", "Test").append("password","6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a").append("rol", "estandar").append("dinero", 100).append("test",true));
		usuarios.add(new Document("email","RaulSuarez@gmail.com").append("name", "Raul").append("surname", "Test").append("password","6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a").append("rol", "estandar").append("dinero", 100).append("test",true));
		usuarios.add(new Document("email","EmilioFernandez@gmail.com").append("name", "Emilio").append("surname", "Test").append("password","6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a").append("rol", "estandar").append("dinero", 100).append("test",true));
		usuarios.add(new Document("email","SaraOrtiz@gmail.com").append("name", "Sara").append("surname", "Test").append("password","6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a").append("rol", "estandar").append("dinero", 100).append("test",true));
		usuarios.add(new Document("email","SalomonParedes@gmail.com").append("name", "Salomon").append("surname", "Test").append("password","6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a").append("rol", "estandar").append("dinero", 100).append("test",true));
		
		Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
		
		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			//Conectarse al servidor Mongo
			MongoCollection<Document> users = mongoClient.getDatabase("myFirstDatabase").getCollection("usuariospb");
			users.insertMany(usuarios);
		}
	}
}