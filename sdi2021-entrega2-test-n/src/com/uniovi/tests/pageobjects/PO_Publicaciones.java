package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

}
