package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PO_ClienteView {

	private static void navigateUrl(WebDriver driver, String URL, String pag) {
		driver.navigate().to(URL + pag);
	}
	public static void login(WebDriver driver, String email, String password, String uRL) {
		//navego a la URL de acceso a la parte 2
		navigateUrl(driver, uRL, "/cliente.html");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testClienteView");
		// Rellenamos el formulario
		fillForm(driver, email, password);

	}
	private static void fillForm(WebDriver driver, String emailp, String passwordp) {
		WebElement email = driver.findElement(By.name("email"));
		email.click();
		email.clear();
		email.sendKeys(emailp);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		//Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}

}
