package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_Publicaciones {

	public static void accesoPublicacionesView(WebDriver driver) {
		accesoPublicacionesView(driver, "testprueba1@gmail.com", "123456");
	}

	public static void accesoPublicacionesView(WebDriver driver, String email, String password) {
//		nos loggeamos
		PO_PrivateView.login(driver, email, password);
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");
		// vamos a la pagina de ofertas
		PO_NavView.clickOption(driver, "publicaciones", "id", "testVistaPublicaciones");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaPublicaciones");
		
	}

	public static void checkNumberOfPublicacionesOnList(WebDriver driver, int expectedSize) {
		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
//		CUIDADO AL AÑADIR MAS USUARIOS EN PRUEBAS ANTERIORES
		assertTrue(elementos.size() == expectedSize);
	}

	public static int getOfertas(PO_DataBase db) {
		return db.getOfertasUser("testprueba1@gmail.com").size();

	}

	public static void eliminarElemento(WebDriver driver, int numeroElemento) {
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//*[@id=\"btnEliminar\"]",
				PO_View.getTimeout());

		WebElement primeraOfertaBtnEliminar = elementos.get(numeroElemento);
		String href = primeraOfertaBtnEliminar.getAttribute("href");
		primeraOfertaBtnEliminar.click();

		Boolean resultado = (new WebDriverWait(driver, PO_View.getTimeout()))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@href=\"" + href + "\"]")));

		assertTrue(resultado);

		List<WebElement> elementosactualizados = SeleniumUtils.EsperaCargaPaginaxpath(driver,
				"//*[@id=\"btnEliminar\"]", PO_View.getTimeout());

		assertTrue(elementos.size() > elementosactualizados.size());

	}

	public static void destacarOferta(WebDriver driver, String nombreOferta) {
		// compruebo que estoy en la vista que toca
		PO_NavView.checkIdOnView(driver, "testVistaPublicaciones");
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//*[@class=\""+nombreOferta+"\"]",
				PO_View.getTimeout());
		elementos.get(0).click();
		
		
//		// busco la oferta con el nombre del parametro
//		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//
//		WebElement oferta = null;
//		// busco la oferta
//		for (WebElement o : ofertas) {
////			de entre las ofertas busco el hijo con el nombre adecuado
//			List<WebElement> hijos = o.findElements(By.xpath("./child::*"));
//			System.out.println(hijos.get(0).getText());
//			if (hijos.get(0).getText().equals(nombreOferta)) {
//				// si el nombre es el correcto guardo la oferta
//				
//				
//				oferta = o;
//
//			}
//
//		}
//		// selecciono el boton de destacar para esa oferta
//		int columnaBoton = 2;
//		WebElement botonDestacar = oferta.findElements(By.xpath("./child::*")).get(columnaBoton);
//
//		// pulso el boton de destacar
//		botonDestacar.click();

	}

//	public static void checkDestacada(WebDriver driver, String nombreOferta, boolean destacada) {
//		// compruebo que estoy en la vista que toca
//		PO_NavView.checkIdOnView(driver, "testVistaPublicaciones");
//		// busco la oferta con el nombre del parametro
//		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//
//		WebElement oferta = null;
//		// busco la oferta
//		for (WebElement o : ofertas) {
////					de entre las ofertas busco el hijo con el nombre adecuado
//			List<WebElement> hijos = o.findElements(By.xpath("./child::*"));
//			if (hijos.get(0).getText().equals(nombreOferta)) {
//				// si el nombre es el correcto guardo la oferta
//				oferta = o;
//
//			}
//
//		}
//		// selecciono la columna donde aparece el estado
//		int columnaBoton = 2;
//		WebElement elm = oferta.findElements(By.xpath("./child::*")).get(columnaBoton);
//		//compruebo que esta o no destacada
//		if (destacada) {
//			assertTrue(elm.getText().equals("Sí"));
//			
//		}else {
//			assertTrue(elm.getText().equals("No"));
//			
//		}
//	}

}
