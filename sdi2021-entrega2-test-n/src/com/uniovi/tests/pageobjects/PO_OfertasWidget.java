package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_OfertasWidget {

	public static void accesoOfertasWidget(WebDriver driver) {
		accesoOfertasWidget(driver, "testprueba1@gmail.com", "123456");
	}
	public static void accesoOfertasWidget(WebDriver driver, String email , String password) {
		 String URL = "https://localhost:8081";
		// testprueba1@gmail.com ya es anyadido antes de cada prueba
		PO_ClienteView.login(driver, email, password, URL);
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
	
	public static void clickChatOferta(WebDriver driver,  String nombre) {

		PO_NavView.checkIdOnView(driver, "testClienteOfertasView");

		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

		for (WebElement webElement : ofertas) {
			List<WebElement> hijos = webElement.findElements(By.xpath("./child::*"));
			if (hijos.get(0).getText().equals(nombre)) {
				hijos.get(4).findElements(By.xpath("./child::*")).get(0).click();
				return;
			}
			
		}
		
	
	}
}
