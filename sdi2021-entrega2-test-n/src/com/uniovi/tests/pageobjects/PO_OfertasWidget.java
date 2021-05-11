package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_OfertasWidget {

	public static void accesoOfertasWidget(WebDriver driver) {
		 String URL = "https://localhost:8081";
		// testprueba1@gmail.com ya es anyadido antes de cada prueba
		PO_ClienteView.login(driver, "testprueba1@gmail.com", "123456", URL);
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testClienteOfertasView");
	}

	public static void checkSizeOfOfertas(WebDriver driver, int total) {
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testClienteOfertasView");
		//sacamos los items
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		System.out.println(total);
		System.out.println(ofertas.size());
		//asserto
		assertEquals(total, ofertas.size());
	}

}
