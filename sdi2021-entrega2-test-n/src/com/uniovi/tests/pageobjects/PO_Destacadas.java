package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_Destacadas {

	public static void checkOfertaByName(WebDriver driver, String nombre) {
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testViewOfertasDestacadas");
		
		List<WebElement> ofertasPagina = SeleniumUtils.EsperaCargaPagina(driver, "text", nombre, PO_View.getTimeout());
		assertTrue(ofertasPagina.size() >= 1);
		
	}
	
	public static void checkComprasNotEmptyList(WebDriver driver) {

		PO_NavView.checkIdOnView(driver, "testVistaCompras");

		List<WebElement> compras = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(compras.size() > 0);

	}

	public static void checkExpectedComprasOnList(WebDriver driver, int expectedValue) {

		PO_NavView.checkIdOnView(driver, "testVistaCompras");

		List<WebElement> compras = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(compras.size() == expectedValue);

	}
	
	
	public static void checkPrecioOfOferta(WebDriver driver,  String nombre, int expectedPrecio) {

		PO_NavView.checkIdOnView(driver, "testVistaCompras");

		List<WebElement> compras = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		int precio = 0;
		for (WebElement webElement : compras) {
			System.out.println(webElement.getText());
		}
		
		
		assertTrue(precio == expectedPrecio);

	}
	
}
