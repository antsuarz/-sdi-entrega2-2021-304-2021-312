package com.uniovi.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Paquetes JUnit 
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
//Paquetes Selenium 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uniovi.tests.pageobjects.PO_AgregarOfertasView;
import com.uniovi.tests.pageobjects.PO_Compras;
import com.uniovi.tests.pageobjects.PO_DataBase;
import com.uniovi.tests.pageobjects.PO_Destacadas;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_PrivateView;
import com.uniovi.tests.pageobjects.PO_Publicaciones;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_TiendaView;
import com.uniovi.tests.pageobjects.PO_UserListView;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.PO_View;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdiEntrega2Tests {

	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox65\\firefox.exe";
	static String Geckdriver024 = "C:\\geckodriver024win64.exe";
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";
	private static PO_DataBase db;

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	private void navigateUrl(String URL, String pag) {
		driver.navigate().to(URL + pag);
		// new WebDriverWait(driver, 2);
	}

	// Antes de cada prueba se navega al URL home de la aplicación
	@Before
	public void setUp() {
		navigateUrl(URL, "");
		db.InitDummyData();
//		 db.showDataOfDB();
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
		db.ResetDummyData();
//		 db.showDataOfDB();
	}

	@BeforeClass
	static public void begin() {
		// COnfiguramos las pruebas.
		// Fijamos el timeout en cada opción de carga de una vista. 2 segundos.
		PO_View.setTimeout(3);
		db = new PO_DataBase();
		// DEBUG mostrar los datos de la Base de datos
//		db.showDataOfDB();

	}

	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

//	@Test
//	public void Prueba00Conexion() {
//
//		System.out.println(driver.getCurrentUrl());
//		System.out.println();
//		WebElement prueba = driver.findElement(By.id("prueba"));
//		System.out.println("-------------------");
//		System.out.println(prueba.getText());
//		assertEquals(prueba.getText(), "Wallapop App");
//
////	WebElement we = driver.getTitle();
//
//	}

	/**
	 * [Prueba1] Registro de Usuario con datos válidos.
	 */
	@Test
	public void PR01() {

		String email = "usarioPrueba@prueba.com";
		String password = "123456";
		db.deleteUser(email);
		PO_RegisterView.registerUser(driver, email, password);
		// assert
		PO_NavView.checkIdOnView(driver, "testVistaTienda");

	}

	/**
	 * [Prueba2] Registro de Usuario con datos inválidos (email, nombre y apellidos
	 * vacíos).
	 * 
	 */
	@Test
	public void PR02() {

		PO_RegisterView.registerUser(driver, "", "", "", "");
		// assert
		PO_NavView.checkIdOnView(driver, "testVistaRegistro");
	}

	/**
	 * [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña
	 * inválida).
	 * 
	 */
	@Test
	public void PR03() {
		PO_RegisterView.registerUser(driver, "usarioPrueba@prueba.com", "123456", "aaaa", "Nombre", "Apellido");
		// assert
		PO_NavView.checkIdOnView(driver, "testVistaRegistro");
		// mensaje conraseña invalida
		String errMsg = "La contraseña no se ha repetido correctamente ";
		PO_View.checkElement(driver, "text", errMsg);
	}

	/**
	 * [Prueba4] Registro de Usuario con datos inválidos (email existente).
	 * 
	 */
	@Test
	public void PR04() {
		// testprueba1@gmail.com ya es anyadido antes de cada prueba
		PO_RegisterView.registerUser(driver, "testprueba1@gmail.com", "123456");
		// assert
		PO_NavView.checkIdOnView(driver, "testVistaRegistro");
		// assert
		String errMsg = "El usuario ya está registrado en la base de datos ";
		PO_View.checkElement(driver, "text", errMsg);

	}

	/**
	 * [Prueba5] Inicio de sesión con datos válidos.
	 * 
	 */
	@Test
	public void PR05() {
		// testprueba1@gmail.com ya es anyadido antes de cada prueba
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");

	}

	/**
	 * [Prueba6] Inicio de sesión con datos inválidos (email existente, pero
	 * contraseña incorrecta).
	 * 
	 */
	@Test
	public void PR06() {
		// testprueba1@gmail.com ya es anyadido antes de cada prueba contrasenya 123456
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "invalida");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaIdentificacion");
		// assert 
		String errMsg = "El usuario no se ha encontrado en la base de datos";
		PO_View.checkElement(driver, "text", errMsg);

	}

	/**
	 * [Prueba7] Inicio de sesión con datos inválidos (campo email o contraseña
	 * vacíos).
	 * 
	 */
	@Test
	public void PR07() {
		// login vacio
		PO_PrivateView.login(driver, "", "");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaIdentificacion");
	}

	/**
	 * [Prueba8] Inicio de sesión con datos inválidos (email no existente en la
	 * aplicación).
	 * 
	 */
	@Test
	public void PR08() {
		PO_PrivateView.login(driver, "testpruebainvalido@gmail.com", "123456");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaIdentificacion");
		// TODO igual poner algun mensaje de que email no existe
		String errMsg = "El usuario no se ha encontrado en la base de datos";
		PO_View.checkElement(driver, "text", errMsg);
	}

	/**
	 * [Prueba9] Hacer click en la opción de salir de sesión y comprobar que se
	 * redirige a la página de inicio de sesión (Login).
	 */
	@Test
	public void PR09() {
		// INICIO SESION PRIMERO
		// testprueba1@gmail.com ya es anyadido antes de cada prueba
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");
		// Clikamos en desconectarse
		PO_HomeView.clickOption(driver, "desconectarse", "class", "btn btn-primary");
		// comprobamos que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaIdentificacion");

	}

	/**
	 * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario
	 * no está autenticado.
	 * 
	 */
	@Test
	public void PR10() {
		String text = "Inicio";
		String busqueda = "//*[contains(text(),'" + text + "')]";

		Boolean resultado;

		resultado = (new WebDriverWait(driver, 2))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(busqueda)));

		assertTrue(resultado);
	}

	/**
	 * [Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos
	 * los que existen en el sistema.
	 */
	@Test
	public void PR11() {
		// nos metemos en la vista adecuada
		PO_UserListView.accesoUserList(driver);
		// comprobamos que el numero de usuarios coincida con los que hay registrados en
		// la base de datos
		int usersDB = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDB--;
		// realizo el asserto
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDB);

	}

	/**
	 * [Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista,
	 * comprobar que la lista se actualiza y dicho usuario desaparece.
	 */
	@Test
	public void PR12() {

		// vamos a la lista de usuarios
		PO_UserListView.accesoUserList(driver);
		// comprobamos que el numero de usuarios coincida con los que hay registrados en
		// la base de datos
		int usersDBpreBorrado = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDBpreBorrado--;
		// estado inicial correcto
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDBpreBorrado);
		// borro el usuario colocado arriba del todo en la lista
		PO_UserListView.deleteUser(driver, 0);
		// vuelvo a sacar en este momento los usuarios de la base de datos
		int usersDBpostBorrado = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDBpostBorrado--;
		// asserto de la base de datos
		assertEquals(usersDBpreBorrado - 1, usersDBpostBorrado);
		// asserto de la web
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDBpostBorrado);
	}

	/**
	 * [Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista,
	 * comprobar que la lista se actualiza y dicho usuario desaparece
	 */
	@Test
	public void PR13() {
		// vamos a la lista de usuarios
		PO_UserListView.accesoUserList(driver);
		// comprobamos que el numero de usuarios coincida con los que hay registrados en
		// la base de datos
		int usersDBpreBorrado = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDBpreBorrado--;
		// estado inicial correcto
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDBpreBorrado);
		// borro el usuario colocado arriba del todo en la lista
		PO_UserListView.deleteUser(driver, usersDBpreBorrado);
		// vuelvo a sacar en este momento los usuarios de la base de datos
		int usersDBpostBorrado = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDBpostBorrado--;
		// asserto de la base de datos
		assertEquals(usersDBpreBorrado - 1, usersDBpostBorrado);
		// asserto de la web
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDBpostBorrado);
	}

	/**
	 * [Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la
	 * lista se actualiza y dichos usuarios desaparecen.
	 */
	@Test
	public void PR14() {
		// vamos a la lista de usuarios
		PO_UserListView.accesoUserList(driver);
		// comprobamos que el numero de usuarios coincida con los que hay registrados en
		// la base de datos
		int usersDBpreBorrado = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDBpreBorrado--;
		// estado inicial correcto
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDBpreBorrado);
		// borro el usuario colocado arriba del todo en la lista
		PO_UserListView.deleteUser(driver, 0, 1, usersDBpreBorrado);
		// vuelvo a sacar en este momento los usuarios de la base de datos
		int usersDBpostBorrado = db.getUsers().size();
		// le resto 1 por que el admin no se encuentra en esa lista
		usersDBpostBorrado--;
		// asserto de la base de datos
		assertEquals(usersDBpreBorrado - 1, usersDBpostBorrado);
		// asserto de la web
		PO_UserListView.checkNumberOfUsersOnList(driver, usersDBpostBorrado);
	}

	/**
	 * [Prueba15] Ir al formulario de alta de oferta, rellenarla con datos válidos y
	 * pulsar el botón Submit. Comprobar que la oferta sale en el listado de ofertas
	 * de dicho usuario
	 */
	@Test
	public void PR15() {
		// entramos en la pagina adecuada
		PO_AgregarOfertasView.accesoAgregarOfertasView(driver);
		// rellenamos el formulario
		PO_AgregarOfertasView.fillForm(driver, "OfertaTestNombre", "OfertaTestDetalles", 1, false);
		// nos desconectamos
		PO_LoginView.desconectarse(driver);
		// nos metemos en /publicaciones
		PO_Publicaciones.accesoPublicacionesView(driver);
		// sacamos la cantidad de ofertas que tiene ese usuario en la base de datos
		int numeroDeOfertas = PO_Publicaciones.getOfertas(db);
		// comprobamos el numero de ofertas que se ven en la vista
		PO_Publicaciones.checkNumberOfPublicacionesOnList(driver, numeroDeOfertas);

	}

	/**
	 * [Prueba16] Ir al formulario de alta de oferta, rellenarla con datos inválidos
	 * (campo título vacío y precio en negativo) y pulsar el botón Submit. Comprobar
	 * que se muestra el mensaje de campo obligatorio.
	 */
	@Test
	public void PR16() {
		// entramos en la pagina adecuada
		PO_AgregarOfertasView.accesoAgregarOfertasView(driver);
		// rellenamos el formulario
		PO_AgregarOfertasView.fillForm(driver, "", "OfertaTestDetalles", -1, false);
		// comprobamos que seguimos en la misma pagina
		PO_NavView.checkIdOnView(driver, "testVistaOfertasAgregar");

	}

	/**
	 * [Prueba17] Mostrar el listado de ofertas para dicho usuario y comprobar que
	 * se muestran todas las que existen para este usuario
	 */
	@Test
	public void PR17() {
		// TODO comprobar que sea esto lo que pide
		PO_Publicaciones.accesoPublicacionesView(driver);
		// sacamos la cantidad de ofertas que tiene ese usuario en la base de datos
		int numeroDeOfertas = PO_Publicaciones.getOfertas(db);
		// comprobamos el numero de ofertas que se ven en la vista
		PO_Publicaciones.checkNumberOfPublicacionesOnList(driver, numeroDeOfertas);

	}

	/**
	 * [Prueba18] Ir a la lista de ofertas, borrar la primera oferta de la lista,
	 * comprobar que la lista se actualiza y que la oferta desaparece.
	 */
	@Test
	public void PR18() {
		PO_Publicaciones.accesoPublicacionesView(driver);
		// sacamos la cantidad de ofertas que tiene ese usuario en la base de datos
		int numeroDeOfertas = PO_Publicaciones.getOfertas(db);
		// comprobamos el numero de ofertas que se ven en la vista
		PO_Publicaciones.checkNumberOfPublicacionesOnList(driver, numeroDeOfertas);
		// establecemos el elemento a borrar
		int numeroElemento = 0;
		// borramos la primera oferta
		PO_Publicaciones.eliminarElemento(driver, numeroElemento);

	}

	/**
	 * [Prueba19] Ir a la lista de ofertas, borrar la última oferta de la lista,
	 * comprobar que la lista se actualiza y que la oferta desaparece
	 */
	@Test
	public void PR19() {
		PO_Publicaciones.accesoPublicacionesView(driver);
		// sacamos la cantidad de ofertas que tiene ese usuario en la base de datos
		int numeroDeOfertas = PO_Publicaciones.getOfertas(db);
		// comprobamos el numero de ofertas que se ven en la vista
		PO_Publicaciones.checkNumberOfPublicacionesOnList(driver, numeroDeOfertas);
		// borramos la ultima oferta -1 ya q el array empieza en 0
		PO_Publicaciones.eliminarElemento(driver, numeroDeOfertas - 1);
	}

	/**
	 * [Prueba20] Hacer una búsqueda con el campo vacío y comprobar que se muestra
	 * la página que corresponde con el listado de las ofertas existentes en el
	 * sistema
	 */
	@Test
	public void PR20() {

		// vamos a la pagina de la tienda
		PO_TiendaView.accesoTiendaView(driver);
		// realizamos la busqueda
		PO_TiendaView.busquedaOferta(driver, "");
		// Comprobamos que el usuario puede ver todas las ofertas
		PO_TiendaView.checkUserCanSeeAllOffers(driver, db);

	}

	/**
	 * [Prueba21] Hacer una búsqueda escribiendo en el campo un texto que no exista
	 * y comprobar que se muestra la página que corresponde, con la lista de ofertas
	 * vacía.
	 */
	@Test
	public void PR21() {
		// vamos a la pagina de la tienda
		PO_TiendaView.accesoTiendaView(driver);
		// realizamos la busqueda
		PO_TiendaView.busquedaOferta(driver, "dsfuyudfyfuyefiuepoo");
		// Comprobamos que el usuario no puede ver las ofertas
		PO_TiendaView.checkEmptyOffers(driver, db);

	}

	/**
	 * [Prueba22] Hacer una búsqueda escribiendo en el campo un texto en minúscula o
	 * mayúscula y comprobar que se muestra la página que corresponde, con la lista
	 * de ofertas que contengan dicho texto, independientemente que el título esté
	 * almacenado en minúsculas o mayúscula.
	 */
	@Test
	public void PR22() {
		// vamos a la pagina de la tienda
		PO_TiendaView.accesoTiendaView(driver);
		// realizamos la busqueda mayuscula y minuscula
		PO_TiendaView.busquedaOferta(driver, "Oferta");
//		comprobamos que existan ofertas
		PO_TiendaView.checkOffersMoreThan(driver, db, 0);
		// realizamos la busqueda mayuscula y minuscula
		PO_TiendaView.busquedaOferta(driver, "oferta");
//		comprobamos que existan ofertas
		PO_TiendaView.checkOffersMoreThan(driver, db, 0);
		// realizamos la busqueda mayuscula y minuscula
		PO_TiendaView.busquedaOferta(driver, "OFERTA");
		// Comprobamos que el usuario no puede ver las ofertas
		PO_TiendaView.checkEmptyOffers(driver, db);

	}

	/**
	 * [Prueba23] Sobre una búsqueda determinada (a elección de desarrollador),
	 * comprar una oferta que deja un saldo positivo en el contador del comprobador.
	 * Y comprobar que el contador se actualiza correctamente en la vista del
	 * comprador.
	 */
	@Test
	public void PR23() {
		// vamos a la pagina de la tienda
		PO_TiendaView.accesoTiendaView(driver);
		// pulsamos la oferta precio5
		PO_TiendaView.pulsarOferta(driver, "precio5");
//		--compramos la oferta precio5
		PO_TiendaView.comprarOferta(driver);
		// vamos a la vista de las compras
		PO_NavView.clickOption(driver, "/compras", "text", "Nombre");
		// comprobamos que se haya comprado
		PO_Compras.checkItemOnList(driver, "precio5");

	}

	/**
	 * [Prueba24] Sobre una búsqueda determinada (a elección de desarrollador),
	 * comprar una oferta que deja un saldo 0 en el contador del comprobador. Y
	 * comprobar que el contador se actualiza correctamente en la vista del
	 * comprador.
	 */
	@Test
	public void PR24() {
		// vamos a la pagina de la tienda
		PO_TiendaView.accesoTiendaView(driver);
		// pulsamos la oferta precio100
		PO_TiendaView.pulsarOferta(driver, "precio100");
//		--compramos la oferta precio100
		PO_TiendaView.comprarOferta(driver);
		// vamos a la vista de las compras
		PO_NavView.clickOption(driver, "/compras", "text", "Nombre");
		// comprobamos que se haya comprado
		PO_Compras.checkItemOnList(driver, "precio100");
	}

	/**
	 * [Prueba25] Sobre una búsqueda determinada (a elección de desarrollador),
	 * intentar comprar una oferta que esté por encima de saldo disponible del
	 * comprador. Y comprobar que se muestra el mensaje de saldo no suficiente.
	 */
	@Test
	public void PR25() {
		// vamos a la pagina de la tienda
		PO_TiendaView.accesoTiendaView(driver);
		// pulsamos la oferta precio105
		PO_TiendaView.pulsarOferta(driver, "precio105");
//		--compramos la oferta precio105
		PO_TiendaView.comprarOferta(driver);
		// compruebo que sale el mensaje de error
		String errMsg = "No tienes suficiente dinero para comprar esta oferta ";
		PO_View.checkElement(driver, "text", errMsg);
		// vamos a la vista de las compras
		PO_NavView.clickOption(driver, "/compras", "text", "Nombre");
		// comprobamos que se haya comprado
		PO_Compras.checkEmptyList(driver);
	}

	/**
	 * [Prueba26] Ir a la opción de ofertas compradas del usuario y mostrar la
	 * lista. Comprobar que aparecen las ofertas que deben aparecer.
	 */
	@Test
	public void PR26() {
		// vamos a la vista correspondiente
		PO_Compras.accesoComprasView(driver);
		// comprobamos que se haya comprado
		PO_Compras.checkEmptyList(driver);
		// creamos datos dummy para comprobar que salgan cosas
		db.addRandomComprasTo("testprueba1@gmail.com");
		db.showDataOfDB();
		// nos desconectamos
		PO_LoginView.desconectarse(driver);
		// nos volvemos a loggear y entrar a la pagina correcta
		PO_Compras.accesoComprasView(driver);
		// comprobamos que la lista no este vacía
		PO_Compras.checkComprasOnList(driver);

	}

	/**
	 * [Prueba27] Al crear una oferta marcar dicha oferta como destacada y a
	 * continuación comprobar: i) que aparece en el listado de ofertas destacadas
	 * para los usuarios y que el saldo del usuario se actualiza adecuadamente en la
	 * vista del ofertante (-20).
	 */
	@Test
	public void PR27() {
		// accedemos a la vista asociada
		PO_AgregarOfertasView.accesoAgregarOfertasView(driver);
		// rellenamos el formulario
		String nameOferta = "Cancion prueba";
		int precio = 3;
		PO_AgregarOfertasView.fillForm(driver, nameOferta, "Cancion de prueba para que sea destacada", precio, true);

		// redireccion publicaciones: comprobamos que estamos en publicaciones
		PO_NavView.checkIdOnView(driver, "testVistaPublicaciones");
		// vamos a la vista de ofertas destacadas
		PO_NavView.clickOption(driver, "/ofertas/destacadas", "free", "Nombre");
		// comprobamos que la oferta aparece en la lista
		PO_Destacadas.checkOfertaByName(driver, nameOferta);

		// comprobamos que el precio es el correcto
		PO_Destacadas.checkPrecioOfOferta(driver, nameOferta, precio);
		// comprobamos tener el saldo correspondiente 100 - 20 = 80
		PO_NavView.checkSaldo(driver, 80);

	}

	/**
	 * [Prueba28] Sobre el listado de ofertas de un usuario con más de 20 euros de
	 * saldo, pinchar en el enlace Destacada y a continuación comprobar: i) que
	 * aparece en el listado de ofertas destacadas para los usuarios y que el saldo
	 * del usuario se actualiza adecuadamente en la vista del ofertante (- 20).
	 */
	@Test
	public void PR28() {
		assertTrue("PR29 sin hacer", false);
	}

	/**
	 * [Prueba29] Sobre el listado de ofertas de un usuario con menos de 20 euros de
	 * saldo, pinchar en el enlace Destacada y a continuación comprobar que se
	 * muestra el mensaje de saldo no suficiente.
	 */
	@Test
	public void PR29() {
		assertTrue("PR29 sin hacer", false);
	}

	/**
	 * [Prueba30] Inicio de sesión con datos válidos.
	 */
	@Test
	public void PR30() {
		assertTrue("PR30 sin hacer", false);
	}

	/**
	 * [Prueba31] Inicio de sesión con datos inválidos (email existente, pero
	 * contraseña incorrecta).
	 * 
	 */
	@Test
	public void PR31() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba32] Inicio de sesión con datos válidos (campo email o contraseña
	 * vacíos).
	 * 
	 */
	@Test
	public void PR32() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba33] Mostrar el listado de ofertas disponibles y comprobar que se
	 * muestran todas las que existen, menos las del usuario identificado
	 */
	@Test
	public void PR33() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba34] Sobre una búsqueda determinada de ofertas (a elección de
	 * desarrollador), enviar un mensaje a una oferta concreta. Se abriría dicha
	 * conversación por primera vez. Comprobar que el mensaje aparece en el listado
	 * de mensajes.
	 */
	@Test
	public void PR34() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba35] Sobre el listado de conversaciones enviar un mensaje a una
	 * conversación ya abierta. Comprobar que el mensaje aparece en el listado de
	 * mensajes.
	 */
	@Test
	public void PR35() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba36] Mostrar el listado de conversaciones ya abiertas. Comprobar que el
	 * listado contiene las conversaciones que deben ser.
	 */
	@Test
	public void PR36() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba37] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	 * Eliminar de la primera y comprobar que el listado se actualiza correctamente.
	 */
	@Test
	public void PR37() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba38] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	 * Eliminar de la última y comprobar que el listado se actualiza correctamente.
	 */
	@Test
	public void PR38() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba39] Identificarse en la aplicación y enviar un mensaje a una oferta,
	 * validar que el mensaje enviado aparece en el chat. Identificarse después con
	 * el usuario propietario de la oferta y validar que tiene un mensaje sin leer,
	 * entrar en el chat y comprobar que el mensaje pasa a tener el estado leído.
	 */
	@Test
	public void PR39() {
		assertTrue("PR31 sin hacer", false);
	}

	/**
	 * [Prueba40] Identificarse en la aplicación y enviar tres mensajes a una
	 * oferta, validar que los mensajes enviados aparecen en el chat. Identificarse
	 * después con el usuario propietario de la oferta y validar que el número de
	 * mensajes sin leer aparece en su oferta.
	 */
	@Test
	public void PR40() {
		assertTrue("PR31 sin hacer", false);
	}

}
