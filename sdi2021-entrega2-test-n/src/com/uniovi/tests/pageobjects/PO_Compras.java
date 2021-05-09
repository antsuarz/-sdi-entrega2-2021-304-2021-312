package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_Compras {

	public static void checkItemOnList(WebDriver driver, String nombre) {
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaCompras");
		List<WebElement> ofertasPagina = SeleniumUtils.EsperaCargaPagina(driver, "text", nombre, PO_View.getTimeout());
		assertTrue(ofertasPagina.size() == 1);

	}

	public static void checkEmptyList(WebDriver driver) {
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaCompras");
		Boolean resultado;

		resultado = (new WebDriverWait(driver, 2)).until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//*[contains(text(),'" + "precio105" + "')]")));

		assertTrue(resultado);

	}

	public static void accesoComprasView(WebDriver driver) {
//		nos loggeamos
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");
		// vamos a la vista de las compras
		PO_NavView.clickOption(driver, "/compras", "text", "Nombre");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaCompras");
	}

	public static void checkComprasOnList(WebDriver driver) {

		PO_NavView.checkIdOnView(driver, "testVistaCompras");

		List<WebElement> compras = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertTrue(compras.size() > 0);

	}

}
