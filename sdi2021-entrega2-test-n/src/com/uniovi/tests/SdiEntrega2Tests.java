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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_DataBase;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.PO_View;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdiEntrega2Tests {
	
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox65\\firefox.exe";
	static String Geckdriver024 = "C:\\geckodriver024win64.exe";
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";

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
	}
	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() {
		// COnfiguramos las pruebas.
		// Fijamos el timeout en cada opción de carga de una vista. 2 segundos.
		PO_View.setTimeout(3);

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
		System.out.println("adsdasasdsad");
		PO_DataBase.InitMongoDB();
//		assertTrue("PR01 sin hacer", false);
	}

	/**
	 * [Prueba2] Registro de Usuario con datos inválidos (email, nombre y apellidos vacíos).

	 */
	@Test
	public void PR02() {
		assertTrue("PR02 sin hacer", false);
	}

	/**
	 * [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).

	 */
	@Test
	public void PR03() {
		assertTrue("PR03 sin hacer", false);
	}

	/**
	 * [Prueba4] Registro de Usuario con datos inválidos (email existente).

	 */
	@Test
	public void PR04() {
		assertTrue("PR04 sin hacer", false);
	}

	/**
	 * [Prueba5] Inicio de sesión con datos válidos.

	 */
	@Test
	public void PR05() {
		assertTrue("PR05 sin hacer", false);
	}

	/**
	 * [Prueba6] Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta).

	 */
	@Test
	public void PR06() {
		assertTrue("PR06 sin hacer", false);
	}

	/**
	 * [Prueba7] Inicio de sesión con datos inválidos (campo email o contraseña vacíos).

	 */
	@Test
	public void PR07() {
		assertTrue("PR07 sin hacer", false);
	}

	/**
	 * [Prueba8] Inicio de sesión con datos inválidos (email no existente en la aplicación).

	 */
	@Test
	public void PR08() {
		assertTrue("PR08 sin hacer", false);
	}

	/**
	 * [Prueba9] Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de
inicio de sesión (Login).
	 */
	@Test
	public void PR09() {
		assertTrue("PR09 sin hacer", false);
	}

	/**
	 * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.

	 */
	@Test
	public void PR10() {
		assertTrue("PR10 sin hacer", false);
	}

	/**
	 * [Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
sistema.
	 */
	@Test
	public void PR11() {
		assertTrue("PR11 sin hacer", false);
	}

	/**
	 * [Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se
actualiza y dicho usuario desaparece.
	 */
	@Test
	public void PR12() {
		assertTrue("PR12 sin hacer", false);
	}

	/**
	 * [Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se
actualiza y dicho usuario desaparece
	 */
	@Test
	public void PR13() {
		assertTrue("PR13 sin hacer", false);
	}

	/**
	 * [Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
usuarios desaparecen.
	 */
	@Test
	public void PR14() {
		assertTrue("PR14 sin hacer", false);
	}

	/**
	 * [Prueba15] Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar el botón Submit.
Comprobar que la oferta sale en el listado de ofertas de dicho usuario
	 */
	@Test
	public void PR15() {
		assertTrue("PR15 sin hacer", false);
	}

	/**
	 * [Prueba16] Ir al formulario de alta de oferta, rellenarla con datos inválidos (campo título vacío y
precio en negativo) y pulsar el botón Submit. Comprobar que se muestra el mensaje de campo
obligatorio.
	 */
	@Test
	public void PR16() {
		assertTrue("PR16 sin hacer", false);
	}

	/**
	 * [Prueba17] Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas las
que existen para este usuario
	 */
	@Test
	public void PR17() {
		assertTrue("PR17 sin hacer", false);
	}

	/**
	 * [Prueba18] Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar que la lista se
actualiza y que la oferta desaparece.
	 */
	@Test
	public void PR18() {
		assertTrue("PR18 sin hacer", false);
	}

	/**
	 * [Prueba19] Ir a la lista de ofertas, borrar la última oferta de la lista, comprobar que la lista se actualiza
y que la oferta desaparece
	 */
	@Test
	public void PR19() {
		assertTrue("PR19 sin hacer", false);
	}

	/**
	 * [Prueba20] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
corresponde con el listado de las ofertas existentes en el sistema
	 */
	@Test
	public void PR20() {
		assertTrue("PR20 sin hacer", false);
	}

	/**
	 * [Prueba21] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
muestra la página que corresponde, con la lista de ofertas vacía.
	 */
	@Test
	public void PR21() {
		assertTrue("PR21 sin hacer", false);
	}

	/**
	 * [Prueba22] Hacer una búsqueda escribiendo en el campo un texto en minúscula o mayúscula y
comprobar que se muestra la página que corresponde, con la lista de ofertas que contengan
dicho texto, independientemente que el título esté almacenado en minúsculas o mayúscula.
	 */
	@Test
	public void PR22() {
		assertTrue("PR22 sin hacer", false);
	}

	/**
	 * [Prueba23] Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que
deja un saldo positivo en el contador del comprobador. Y comprobar que el contador se
actualiza correctamente en la vista del comprador.
	 */
	@Test
	public void PR23() {
		assertTrue("PR23 sin hacer", false);
	}

	/**
	 * [Prueba24] Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que
deja un saldo 0 en el contador del comprobador. Y comprobar que el contador se actualiza
correctamente en la vista del comprador.
	 */
	@Test
	public void PR24() {
		assertTrue("PR24 sin hacer", false);
	}

	/**
	 * [Prueba25] Sobre una búsqueda determinada (a elección de desarrollador), intentar comprar una
oferta que esté por encima de saldo disponible del comprador. Y comprobar que se muestra el
mensaje de saldo no suficiente.
	 */
	@Test
	public void PR25() {
		assertTrue("PR25 sin hacer", false);
	}

	/**
	 * [Prueba26] Ir a la opción de ofertas compradas del usuario y mostrar la lista. Comprobar que
aparecen las ofertas que deben aparecer.
	 */
	@Test
	public void PR26() {
		assertTrue("PR26 sin hacer", false);
	}

	/**
	 * [Prueba27] Al crear una oferta marcar dicha oferta como destacada y a continuación comprobar: i)
que aparece en el listado de ofertas destacadas para los usuarios y que el saldo del usuario se
actualiza adecuadamente en la vista del ofertante (-20).
	 */
	@Test
	public void PR27() {
		assertTrue("PR27 sin hacer", false);
	}

	/**
	 * [Prueba28] Sobre el listado de ofertas de un usuario con más de 20 euros de saldo, pinchar en el
enlace Destacada y a continuación comprobar: i) que aparece en el listado de ofertas destacadas
para los usuarios y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (-
20).
	 */
	@Test
	public void PR28() {
		assertTrue("PR29 sin hacer", false);
	}

	/**
	 * [Prueba29] Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo, pinchar en el
enlace Destacada y a continuación comprobar que se muestra el mensaje de saldo no suficiente.
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
	 * [Prueba31] Inicio de sesión con datos inválidos (email existente, pero contraseña incorrecta).

	 */
	@Test
	public void PR31() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba32] Inicio de sesión con datos válidos (campo email o contraseña vacíos).

	 */
	@Test
	public void PR32() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba33] Mostrar el listado de ofertas disponibles y comprobar que se muestran todas las que
existen, menos las del usuario identificado
	 */
	@Test
	public void PR33() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba34] Sobre una búsqueda determinada de ofertas (a elección de desarrollador), enviar un
mensaje a una oferta concreta. Se abriría dicha conversación por primera vez. Comprobar que el
mensaje aparece en el listado de mensajes.
	 */
	@Test
	public void PR34() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba35] Sobre el listado de conversaciones enviar un mensaje a una conversación ya abierta.
Comprobar que el mensaje aparece en el listado de mensajes.
	 */
	@Test
	public void PR35() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba36] Mostrar el listado de conversaciones ya abiertas. Comprobar que el listado contiene las
conversaciones que deben ser.
	 */
	@Test
	public void PR36() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba37] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace Eliminar de la primera y
comprobar que el listado se actualiza correctamente.
	 */
	@Test
	public void PR37() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba38] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace Eliminar de la última y
comprobar que el listado se actualiza correctamente.
	 */
	@Test
	public void PR38() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba39] Identificarse en la aplicación y enviar un mensaje a una oferta, validar que el mensaje
enviado aparece en el chat. Identificarse después con el usuario propietario de la oferta y validar
que tiene un mensaje sin leer, entrar en el chat y comprobar que el mensaje pasa a tener el estado
leído.
	 */
	@Test
	public void PR39() {
		assertTrue("PR31 sin hacer", false);
	}
	
	/**
	 * [Prueba40] Identificarse en la aplicación y enviar tres mensajes a una oferta, validar que los mensajes
enviados aparecen en el chat. Identificarse después con el usuario propietario de la oferta y
validar que el número de mensajes sin leer aparece en su oferta.
	 */
	@Test
	public void PR40() {
		assertTrue("PR31 sin hacer", false);
	}
	

}
