package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_UserListView {
	
	public static void accesoUserList(WebDriver driver) {
		// INICIO SESION PRIMERO
		// como admin para poder ver la lista de usuarios
		PO_PrivateView.login(driver, "admin@email.com", "admin");
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaAdministrador");
		PO_NavView.clickOption(driver, "listaUsuarios", "text", "Lista de usuarios");
	}
	
	/**
	 * @param expectedSize
	 */
	public static void checkNumberOfUsersOnList(WebDriver driver,int expectedSize) {
		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
//		CUIDADO AL AÑADIR MAS USUARIOS EN PRUEBAS ANTERIORES
		assertTrue(elementos.size() == expectedSize);
	}

	/**
	 * @param expectedSize
	 */
	public static int getNumberOfUsersOnList(WebDriver driver) {
		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
//		CUIDADO AL AÑADIR MAS USUARIOS EN PRUEBAS ANTERIORES
		return elementos.size() ;
	}
	
	public static void deleteUser(WebDriver driver,int... numsBorrar) {
		for (int i = 0; i < numsBorrar.length; i++) {
			List<WebElement> filaBorrar = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@class=\"deleteCheckbox\"]",PO_View.getTimeout());
			filaBorrar.get(numsBorrar[i]).click();
		}
		
		
		
		List<WebElement> borrarBoton = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@id=\"deleteButton\"]",PO_View.getTimeout());
		borrarBoton.get(0).click();
//		PO_NavView.clickOption(driver, "deleteButton", "id", " Eliminar Usuarios ");
	}

	
}
