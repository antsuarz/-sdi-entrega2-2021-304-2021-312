package com.uniovi.tests.pageobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class PO_DataBase {

	private static String connectionString = "mongodb://admin:sdi@tiendamusica-shard-00-00.jxgw2.mongodb.net:27017,tiendamusica-shard-00-01.jxgw2.mongodb.net:27017,tiendamusica-shard-00-02.jxgw2.mongodb.net:27017/myWallapop?ssl=true&replicaSet=atlas-od6pn1-shard-0&authSource=admin&retryWrites=true&w=majority";
	private static String AppDBname = "myWallapop";
	private static List<Document> usuarios = new ArrayList<Document>();
	private static List<Document> ofertas = new ArrayList<Document>();
	private static List<Document> compras = new ArrayList<Document>();
	private static List<Document> mensajes = new ArrayList<Document>();

	public PO_DataBase() {
		Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
		insertUsers();
		insertOfertas();
//		insertCompras();
//		insertMensajes(getRandomUserEmail(), getRandomUserId());
		
	}

	public static void main(String[] args) {
//Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
////new PO_DataBase().deleteUser("pablo2@email.com");
////try (MongoClient mongoclient = MongoClients.create(connectionString)) {
////	
////	insertUser(mongoclient, "pablo2@email.com", null, "Pablo2", "Apellido", 100);
////} catch (Exception e) {
////}
////System.out.println("COMPLETADO");
//new PO_DataBase().InitDummyData();
new PO_DataBase().showDataOfDB();
}

	@SuppressWarnings("unused")
	private static void insertCompra(MongoClient mongoclient) {
		mongoclient.getDatabase(AppDBname).getCollection("compras").insertMany(compras);
	}

	@SuppressWarnings("unused")
	private static void insertOferta(MongoClient mongoclient) {
		mongoclient.getDatabase(AppDBname).getCollection("ofertas").insertMany(ofertas);

	}

	public void insertUser(String email, String password, String nombre,
			String apellido, Integer dinero) {
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			insertUser(mongoclient, email, password, nombre, apellido, dinero);
		}
	}
	
	@SuppressWarnings("unused")
	private void insertUser(MongoClient mongoclient, String email, String password, String nombre,
			String apellido, Integer dinero) {

		String defaultEmail = "aaaaaaa@gmail.com";
		String defaultPassword = "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a";// 123456
		String defaultNombre = "aaaaaaa";
		String defaultApellido = "test";
		Integer defaultDinero = 100;

		if (email == null)
			email = defaultEmail;
		if (password == null)
			password = defaultPassword;
		if (nombre == null)
			nombre = defaultNombre;
		if (apellido == null)
			apellido = defaultApellido;
		if (dinero == null)
			dinero = defaultDinero;

		Document usuario = (new Document("email", email).append("password", password).append("nombre", nombre)
				.append("apellido", apellido).append("dinero", dinero).append("test", false));

		mongoclient.getDatabase(AppDBname).getCollection("usuarios").insertOne(usuario);

	}

	private String getRandomUserEmail() {
		String id = "";

		List<Document> usrs = getUsers();
		id = (String) usrs.get(new Random().nextInt(usrs.size())).get("email");

		return id;
	}

	private String getRandomUserId() {

		String id = "";

		List<Document> usrs = usuarios;
		id = (String) usrs.get(new Random().nextInt(usrs.size())).get("_id");

		return id;
	}

	@SuppressWarnings("deprecation")
	private void insertMensajes(String emisor, String conversacion) {
		for (int i = 0; i < 5; i++) {
			// Document{{_id=609a0d487c436568a8bb7816, contenido=hola muy buenas,
			// fecha=11/5/2021 Hora: 6:51, emisor=pablo2@email.com,
			// conversacion=609a0d487c436568a8bb7815, leido=false}}
			mensajes.add(new Document().append("contenido", "contenido de un mensaje random")
					.append("fecha", new Date().toGMTString()).append("emisor", emisor)
					.append("conversacion", conversacion).append("leido", false).append("test", true));
		}
	}

	public void deleteUser(String email) {
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			mongoclient.getDatabase(AppDBname).getCollection("ofertas").deleteMany(new Document("autor", email));
			mongoclient.getDatabase(AppDBname).getCollection("compras").deleteMany(new Document("usuario", email));
			mongoclient.getDatabase(AppDBname).getCollection("usuarios").deleteOne(new Document("email", email));
			mongoclient.getDatabase(AppDBname).getCollection("mensajes").deleteOne(new Document("emisor", email));
		} catch (Exception e) {
		}
	}

	private void insertUsers() {
//		Document{{_id=6091801ae6336f0da8899d61, email=admin@email.com, password=ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11, nombre=admin, apellido=admin, dinero=100, tipo=admin}}
//		Document{{_id=6094538b2b28a92948a41acd, email=user1@email.com, password=155f8c121a9a84a039e70ed7e31c2be23125202e77c478b1a329b8e436b28df9, nombre=pepe, apellido=perez, dinero=63, tipo=noadmin}}
		// 57420b1f0b1e2d07e407a04ff8bbc205a57b3055b34ed94658c04ed38f62daa7 == prueba1
		// 6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a == 123456
		usuarios.add(new Document("email", "aaaaaaa@gmail.com")
				.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")// 123456
				.append("nombre", "aaaaaaa").append("apellido", "test").append("dinero", 100).append("test", true));

		usuarios.add(new Document("email", "zzzzzzzz@gmail.com")
				.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")// 123456
				.append("nombre", "zzzzzzzz").append("apellido", "test").append("dinero", 100).append("test", true));
		usuarios.add(new Document("email", "aaaaaaa2@gmail.com")
				.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")// 123456
				.append("nombre", "aaaaaaa2").append("apellido", "test").append("dinero", 100).append("test", true));
		usuarios.add(new Document("email", "zzzzzzzz2@gmail.com")
				.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")// 123456
				.append("nombre", "zzzzzzzz2").append("apellido", "test").append("dinero", 100).append("test", true));
		for (int i = 0; i < 4; i++) {
			usuarios.add(new Document("email", "testprueba" + i + "@gmail.com")
					.append("password", "6fabd6ea6f1518592b7348d84a51ce97b87e67902aa5a9f86beea34cd39a6b4a")// 123456
					.append("nombre", "testprueba" + i).append("apellido", "test").append("dinero", 100)
					.append("test", true));
		}
	}

	public void insertOferta(String nombre,String detalles, Date fecha, String autor, int precio ,boolean comprado,boolean destacada ) {
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			Document oferta = new Document().append("nombre", nombre)
					.append("detalles", detalles)
					.append("fecha", fecha.toGMTString())
					.append("autor",autor)
					.append("precio", precio)
					.append("comprado", comprado)
					.append("destacada", destacada)
					.append("test", true);
			mongoclient.getDatabase(AppDBname).getCollection("ofertas").insertOne(oferta);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void insertOfertas() {
		// Document{{_id=609479cc4b44d8342879cc99, nombre=Oferta 1, detalles=Esta es la
		// oferta n�mero 1, fecha=4/4/2021 Hora: 1:20, autor=user1@email.com, precio=6,
		// comprado=true, destacada=null}}
		for (int i = 0; i < 4; i++) { // usuario
			for (int j = 0; j < 5; j++) { // oferta
				ofertas.add(new Document().append("nombre", "Oferta" + j)
						.append("detalles",
								"Esta es la oferta numero" + j + " del usuario testprueba" + i + "@gmail.com")
						.append("fecha", new Date().toGMTString()).append("autor", "testprueba" + i + "@gmail.com")
						.append("precio", new Random().nextInt(10)).append("comprado", false)
						.append("destacada", new Random().nextBoolean() ? "on" : null).append("test", true));
			}
			ofertas.add(new Document().append("nombre", "oferta" + 6)
					.append("detalles", "Esta es la oferta numero" + 6 + " del usuario testprueba" + i + "@gmail.com")
					.append("fecha", new Date().toGMTString()).append("autor", "testprueba" + i + "@gmail.com")
					.append("precio", new Random().nextInt(10)).append("comprado", false)
					.append("destacada", new Random().nextBoolean() ? "on" : null).append("test", true));

		}
		ofertas.add(new Document().append("nombre", "precio5")
				.append("detalles", "Esta es la oferta numero" + 7 + " del usuario testprueba" + 5 + "@gmail.com")
				.append("fecha", new Date().toGMTString()).append("autor", "testprueba" + 5 + "@gmail.com")
				.append("precio", 5).append("comprado", false).append("destacada", null).append("test", true));
		ofertas.add(new Document().append("nombre", "precio100")
				.append("detalles", "Esta es la oferta numero" + 8 + " del usuario testprueba" + 5 + "@gmail.com")
				.append("fecha", new Date().toGMTString()).append("autor", "testprueba" + 5 + "@gmail.com")
				.append("precio", 100).append("comprado", false).append("destacada", null).append("test", true));
		ofertas.add(new Document().append("nombre", "precio105")
				.append("detalles", "Esta es la oferta numero" + 9 + " del usuario testprueba" + 5 + "@gmail.com")
				.append("fecha", new Date().toGMTString()).append("autor", "testprueba" + 5 + "@gmail.com")
				.append("precio", 105).append("comprado", false).append("destacada", null).append("test", true));
		ofertas.add(new Document().append("nombre", "precio96")
				.append("detalles", "Esta es la oferta numero" + 10 + " del usuario testprueba" + 5 + "@gmail.com")
				.append("fecha", new Date().toGMTString()).append("autor", "testprueba" + 5 + "@gmail.com")
				.append("precio", 96).append("comprado", false).append("destacada", null).append("test", true));
	}

	public String getIDOferta(String nombre) {
		// TODO Auto-generated method stub
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			FindIterable<Document> it2 = mongoclient.getDatabase(AppDBname).getCollection("ofertas")
					.find(new Document("nombre", nombre));
			MongoCursor<Document> cursor2 = it2.cursor();
			while (cursor2.hasNext()) {
				Document item2 = cursor2.next();
				return item2.get("_id").toString();
			}
		}
		return null;
	}

	private void insertCompras() {

		List<Document> userIds = getUsersIds();

//		Document{{_id=6094f8584df7411384240f48, usuario=user3@email.com, ofertaId=Document{{_id=6094899335fe6a188c4bc076}}}}
		for (int i = 0; i < 5; i++) {
			compras.add(new Document().append("usuario", "testprueba" + i + "@gmail.com")
					.append("ofertaId", userIds.get(new Random().nextInt(userIds.size()))).append("test", true));
		}
	}

	private List<Document> getUsersIds() {
		List<Document> userIds = new ArrayList<Document>();
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			FindIterable<Document> it2 = mongoclient.getDatabase(AppDBname).getCollection("usuarios").find();
			MongoCursor<Document> cursor2 = it2.cursor();
			while (cursor2.hasNext()) {
				Document item2 = cursor2.next();
				userIds.add(new Document("_id", item2.get("_id")));
			}
		}
		return userIds;
	}

	public void InitDummyData() {

		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			mongoclient.getDatabase(AppDBname).getCollection("usuarios").insertMany(usuarios);
			mongoclient.getDatabase(AppDBname).getCollection("ofertas").insertMany(ofertas);
			mongoclient.getDatabase(AppDBname).getCollection("compras").insertMany(compras);

		} catch (Exception e) {
		}

	}

	public void ResetDummyData() {
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			mongoclient.getDatabase(AppDBname).getCollection("usuarios").deleteMany(new Document("test", true));
			mongoclient.getDatabase(AppDBname).getCollection("ofertas").deleteMany(new Document("test", true));
			mongoclient.getDatabase(AppDBname).getCollection("compras").deleteMany(new Document("test", true));
			mongoclient.getDatabase(AppDBname).getCollection("mensajes").deleteMany(new Document("test", true));
			String email;
			for (int i = 0; i < 10; i++) {
				email = "testprueba" + i + "@gmail.com";// "autor" usuario
				mongoclient.getDatabase(AppDBname).getCollection("usuarios").deleteMany(new Document("email", email));
				mongoclient.getDatabase(AppDBname).getCollection("ofertas").deleteMany(new Document("autor", email));
				mongoclient.getDatabase(AppDBname).getCollection("compras").deleteMany(new Document("usuario", email));
				mongoclient.getDatabase(AppDBname).getCollection("mensajes").deleteMany(new Document("emisor", email));
			}
			email = "prueba1@prueba1.com";
			mongoclient.getDatabase(AppDBname).getCollection("usuarios").deleteMany(new Document("email", email));
			mongoclient.getDatabase(AppDBname).getCollection("ofertas").deleteMany(new Document("autor", email));
			mongoclient.getDatabase(AppDBname).getCollection("compras").deleteMany(new Document("usuario", email));
			mongoclient.getDatabase(AppDBname).getCollection("mensajes").deleteMany(new Document("emisor", email));
			
		} catch (Exception e) {
		}
	}

	public void showDataOfDB() {
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
				String item = cursor.next();
				System.out.println(item);
				FindIterable<Document> it2 = mongoclient.getDatabase(AppDBname).getCollection(item).find();
				MongoCursor<Document> cursor2 = it2.cursor();
				while (cursor2.hasNext()) {
					Document item2 = cursor2.next();
					System.out.println(item2);
				}
			}

		} catch (Exception e) {
		}
	}

	private static void prueba() {

		/* Step-1 > Connect to MongoDB */
		System.out.println("Step-1 > Connect to MongoDB");
		MongoClient mongoClient = MongoClients.create(connectionString);
		/* Step-2 > Connect to DATABASE */
		// If in case database doesn't exists, it will be created at runtime
		System.out.println("Step-2 > Connect to DATABASE");
		MongoDatabase db = mongoClient.getDatabase(AppDBname);

		/* Step-3 > Get the COLLECTION (TABLE) */
		// If in case collection (Table) doesn't exists, it will be created at runtime
		System.out.println("Step-3 > Get the COLLECTION (TABLE)");
		DBCollection dbCollection = (DBCollection) db.getCollection("usuarios");

		/*
		 * Step-4 > Not Mandatory If collection already exists, you may remove
		 * everything from collection for perfect OUTPUT of program
		 */
		System.out.println("Step-4 > Not Mandatory");
		dbCollection.remove(new BasicDBObject());

		BasicDBObject basicDBObject1 = new BasicDBObject();
		basicDBObject1.put("id", 1); // Id is the column name, 1 is the value of column
		basicDBObject1.put("name", "Ankit");

		/* Step5 > INSERT document1/record1 in COLLECTION in MongoDB */
		System.out.println("Step-5 > INSERT document1/record1 in COLLECTION in MongoDB");
		dbCollection.insert(basicDBObject1);

		BasicDBObject basicDBObject2 = new BasicDBObject();
		basicDBObject2.put("id", 2); // Id is the column name, 2 is the value of column
		basicDBObject2.put("name", "Sam");

		/* Step-6 > INSERT document2/record2 in COLLECTION in MongoDB */
		System.out.println("Step-6 > INSERT document1/record1 in COLLECTION in MongoDB");
		dbCollection.insert(basicDBObject2);

		/** Step-7 > Display documents of COLLECTION in MongoDB */
		System.out.println("Step-7 > Display documents of COLLECTION in MongoDB");
		DBCursor dbCursor = dbCollection.find();
		while (dbCursor.hasNext()) {
			System.out.println(dbCursor.next());
		}

	}

	public List<Document> getUsers() {
		return getCollection("usuarios");
	}

	public List<Document> getOfertas() {
		return getCollection("ofertas");
	}

	public List<Document> getCompras() {
		return getCollection("compras");
	}
	
	public List<Document> getMensajes() {
		return getCollection("mensajes");
	}

	public List<Document> getCollection(String collection) {
		List<Document> tmp = new ArrayList<Document>();
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			FindIterable<Document> it2 = mongoclient.getDatabase(AppDBname).getCollection(collection).find();
			MongoCursor<Document> cursor2 = it2.cursor();
			while (cursor2.hasNext()) {
				Document item2 = cursor2.next();
				tmp.add(item2);
			}
		}
		return tmp;
	}

	public List<Document> getOfertasUser(String email) {

		List<Document> ofertas = getOfertas();
		List<Document> ofertasUsuario = new ArrayList<Document>();
		for (Document document : ofertas) {
			if (email.equals(document.get("autor"))) {
				ofertasUsuario.add(document);
			}
		}

		return ofertasUsuario;

	}

	public String getIdOfertaPrecioMenorQue(int precio) {
		List<Document> ofertas = getOfertas();

		for (Document document : ofertas) {
			int valor = (int) Double.parseDouble((String) document.get("precio"));
			if (precio < valor) {
				return document.get("_id").toString();
			}
		}

		return null;
	}

	public void addRandomComprasTo(String email) {
		List<Document> comprasRandom = new ArrayList<Document>();
		String idUser = getIdUser(email);
		for (int i = 0; i < 5; i++) {

			compras.add(new Document().append("usuario", email).append("ofertaId", idUser).append("test", true));
		}
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			mongoclient.getDatabase(AppDBname).getCollection("compras").insertMany(comprasRandom);

		} catch (Exception e) {
		}

	}

	public String getIdUser(String email) {
		List<Document> users = getUsers();
		for (Document document : users) {
			if (document.get("email").equals(email)) {
				return document.get("_id").toString();
			}
		}
		throw new InvalidArgumentException("No existe ese email");
	}

	public void deleteMessageByContenido(String contenido) {
		try (MongoClient mongoclient = MongoClients.create(connectionString)) {
			mongoclient.getDatabase(AppDBname).getCollection("compras").deleteOne(new Document("contenido",contenido));

		} catch (Exception e) {
		}
		
	}

	public List<Document> getMessagesUser(String emailUser) {
		String id = getIdUser(emailUser);
		// Document{{_id=609a0d487c436568a8bb7816, contenido=hola muy buenas,
		// fecha=11/5/2021 Hora: 6:51, emisor=pablo2@email.com,
		// conversacion=609a0d487c436568a8bb7815, leido=false}}
		List<Document>mensajes = getMensajes();
		List<Document>mensajesUser = new ArrayList<Document>();
		for (Document document : mensajes) {
			if (document.get("conversacion").toString().equals(id)) {
				mensajesUser.add(document);
			}
		}
		return mensajesUser;
	}

	public int getDineroUsuario(String email) {
		List<Document> users = getUsers();
		for (Document document : users) {
			if (document.get("email").equals(email)) {
				int tmp = Integer.parseInt(document.get("dinero").toString());
				return tmp;
			}
		}
		throw new InvalidArgumentException("No existe ese email");
	}



}
