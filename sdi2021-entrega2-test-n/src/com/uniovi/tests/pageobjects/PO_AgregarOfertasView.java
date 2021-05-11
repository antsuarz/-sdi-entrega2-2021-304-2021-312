package com.uniovi.tests.pageobjects;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_AgregarOfertasView {

	public static void accesoAgregarOfertasView(WebDriver driver) {
//		nos loggeamos
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");
		// vamos a la pagina de ofertas
		PO_NavView.clickOption(driver, "ofertas/agregar", "text", "Nombre:");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaOfertasAgregar");
	}

	static public void fillForm(WebDriver driver, String nombrep, String detallesp, int preciop, boolean destacadap) {
		fillFormCaratula(driver, nombrep, detallesp, preciop, null, destacadap);
	}

	/**
	 * EJEMPLO LLAMADA PO_AgregarOfertasView.fillFormCaratula(driver, nameOferta,
	 * "Cancion de prueba para que sea destacada", precio, ".\\img\\caratula.jpg",
	 * true);
	 * 
	 * @param driver
	 * @param nombrep
	 * @param detallesp
	 * @param preciop
	 * @param fotopath
	 * @param destacadap
	 */
	static public void fillFormCaratula(WebDriver driver, String nombrep, String detallesp, int preciop,
			String fotopath, boolean destacadap) {
		WebElement nombre = driver.findElement(By.name("nombre"));
		nombre.click();
		nombre.clear();
		nombre.sendKeys(nombrep);
		WebElement detalles = driver.findElement(By.name("detalles"));
		detalles.click();
		detalles.clear();
		detalles.sendKeys(detallesp);
		WebElement precio = driver.findElement(By.name("precio"));
		precio.click();
		precio.clear();
		precio.sendKeys(String.valueOf(preciop));
		if (fotopath != null) {
			// find the input element
			WebElement elem = driver.findElement(By.xpath("//input[@type='file']"));
			// 'type' the file location to it as it were a usual <input type='text' />
			// element
			File f = new File(fotopath);
			if (f.exists()) {
				elem.sendKeys(f.getAbsolutePath());
			}

		}

		if (destacadap) {
			WebElement destacada = driver.findElement(By.name("destacada"));
			destacada.click();
//			destacada.clear();
//			destacada.click();
		}

		// Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}

}
