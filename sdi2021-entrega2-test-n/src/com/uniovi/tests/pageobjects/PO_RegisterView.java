package com.uniovi.tests.pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_RegisterView extends PO_NavView {	
	
	static public void fillForm(WebDriver driver, String dnip, String namep, String lastnamep, String passwordp, String passwordconfp) {
		WebElement dni = driver.findElement(By.name("email"));
		dni.click();
		dni.clear();
		dni.sendKeys(dnip);
		WebElement name = driver.findElement(By.name("nombre"));
		name.click();
		name.clear();
		name.sendKeys(namep);
		WebElement lastname = driver.findElement(By.name("apellido"));
		lastname.click();
		lastname.clear();
		lastname.sendKeys(lastnamep);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		WebElement passwordConfirm = driver.findElement(By.name("rePassword"));
		passwordConfirm.click();
		passwordConfirm.clear();
		passwordConfirm.sendKeys(passwordconfp);
		//Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}
	
	/**
	 * @param email
	 * @param password
	 */
	public static  void registerUser(WebDriver driver, String email, String password) {
		registerUser(driver, email, password, "Nombre", "Apellido");
	}

	public static void registerUser(WebDriver driver, String email, String password, String nombre, String apellido) {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, email , nombre, apellido, password , password);
		// Comprobamos que entramos en la sección privada
//		PO_View.checkElement(driver, "text", "testInicioSesion");
		
		
		
	}
}
