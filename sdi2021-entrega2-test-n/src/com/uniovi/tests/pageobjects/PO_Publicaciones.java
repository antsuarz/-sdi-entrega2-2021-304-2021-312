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
//		nos loggeamos
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");
		// vamos a la pagina de ofertas
		PO_NavView.clickOption(driver, "publicaciones", "id", "testVistaPublicaciones");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaPublicaciones");
	}
	
	public static void checkNumberOfPublicacionesOnList(WebDriver driver,int expectedSize) {
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
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@href=\""+href+"\"]")));

		assertTrue(resultado);
		
		List<WebElement> elementosactualizados = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//*[@id=\"btnEliminar\"]",
				PO_View.getTimeout());
		
		assertTrue(elementos.size() > elementosactualizados.size());
		
		
	}
	
}
