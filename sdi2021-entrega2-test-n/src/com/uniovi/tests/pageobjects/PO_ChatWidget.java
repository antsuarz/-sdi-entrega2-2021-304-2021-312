package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_ChatWidget {

	public static void sendMessage(WebDriver driver, String mensaje) {
		// compruebo que estoy en la pagina que toca
		PO_NavView.checkIdOnView(driver, "testWidgetChatView");
		// relleno el formulario
		fillForm(driver, mensaje);
		// compruebo que se actualiza la pagina con el mensaje
		PO_View.checkElement(driver, "text", mensaje);

	}

	private static void fillForm(WebDriver driver, String mensajep) {
		WebElement mensaje = driver.findElement(By.name("contenido"));
		mensaje.click();
		mensaje.clear();
		mensaje.sendKeys(mensajep);

		By boton = By.className("btn");
		driver.findElement(boton).click();
	}

	public static void accesoChatView(WebDriver driver, String email, String password) {
		PO_OfertasWidget.accesoOfertasWidget(driver, email, password);
		// vamos a la pagina de ofertas
		WebElement elm = driver.findElement(By.id("testEnlaceChatCliente"));
		elm.click();
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testWidgetConversacionesView");
	}

	public static void checkNumberOfChatsInView(WebDriver driver, int expectedValue) {
		List<WebElement> chats = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		System.out.println(chats.size());
//		System.out.println(expectedValue);
		assertTrue(chats.size() >= expectedValue);
	}

	public static void clickChatOferta(WebDriver driver, String nombre) {
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

		for (WebElement webElement : ofertas) {
			List<WebElement> hijos = webElement.findElements(By.xpath("./child::*"));
			if (hijos.get(0).getText().equals(nombre)) {
				hijos.get(2).findElements(By.xpath("./child::*")).get(0).click();
				return;
			}

		}

		PO_View.checkElement(driver, "text", nombre);
	}

	public static String getFirstChat(WebDriver driver) {
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

		return ofertas.get(0).findElements(By.xpath("./child::*")).get(0).getText();

	}

	public static void deleteChat(WebDriver driver, String nombreOferta) {
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

		for (WebElement webElement : ofertas) {
			List<WebElement> hijos = webElement.findElements(By.xpath("./child::*"));
			if (hijos.get(0).getText().equals(nombreOferta)) {
				hijos.get(3).findElements(By.xpath("./child::*")).get(0).click();
				return;
			}

		}

	}

	public static void checkInvisibilityOf(WebDriver driver, String nombreOferta) {
		String text = nombreOferta;
		String busqueda = "//*[contains(text(),'" + text + "')]";

		Boolean resultado;

		resultado = (new WebDriverWait(driver, PO_View.getTimeout()))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(busqueda)));

		assertTrue(resultado);

	}

	public static String getLastChat(WebDriver driver) {
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());

		return ofertas.get(ofertas.size()-1).findElements(By.xpath("./child::*")).get(0).getText();

	}

}
