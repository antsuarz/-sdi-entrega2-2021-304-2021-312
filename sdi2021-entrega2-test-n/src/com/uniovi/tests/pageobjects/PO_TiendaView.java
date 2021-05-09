package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_TiendaView {
	
	public static void accesoTiendaView(WebDriver driver) {
//		nos loggeamos
		PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");

	}
	
	static public void checkUserCanSeeAllOffers(WebDriver driver, PO_DataBase db) {

				// sacamos la cantidad de ofertas que tiene ese usuario en la base de datos
				int numeroDeOfertasUsuario = PO_Publicaciones.getOfertas(db);
				// calculamos las ofertas para ese usuario
				int numeroOfertasParaElUsuario = db.getOfertas().size() - numeroDeOfertasUsuario;
//				System.out.println(numeroDeOfertasUsuario);
//				System.out.println(numeroOfertasParaElUsuario);
				// iniciamos un contador para ir contando ofertas
				int contador = 0;
		
				// recojemos la cantidad de paginas que hay en la tienda
				List<WebElement> pagination = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@class=\"pagination\"]",
						PO_View.getTimeout());
				int paginasTienda =  (int) Double.parseDouble(pagination.get(0).getAttribute("id"));
				
		
				for (int i = 1; i <= paginasTienda; i++) {
					try {
						List<WebElement> ofertasPagina = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@id=\"oferta\"]",
								PO_View.getTimeout());
						contador += ofertasPagina.size();
					} catch (Exception e) {
//						System.out.println("Sin ofertas en la pagina " + i);
					}
					PO_NavView.clickOption(driver, "/tienda?pg=" + i, "id", "testVistaTienda");
				}
//				System.out.println(contador);
				//TODO arreglar porque siempre cuenta 3 de mas
				assertEquals(numeroOfertasParaElUsuario, contador);
	}
	
	public static   void busquedaOferta(WebDriver driver,String busquedap) {
		WebElement busqueda = driver.findElement(By.name("busqueda"));
		busqueda.click();
		busqueda.clear();
		busqueda.sendKeys(busquedap);
		//pulsamos el boton de busqueda
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//*[@id=\"search\"]", PO_View.getTimeout());
		elementos.get(0).click();
		// assert que estamos en la pagina correcta
		PO_NavView.checkIdOnView(driver, "testVistaTienda");
		

	}

	public static void checkEmptyOffers(WebDriver driver, PO_DataBase db) {

		Boolean resultado;

		resultado = (new WebDriverWait(driver, 2))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath( "//*[@id=\"oferta\"]")));

		assertTrue(resultado);
		
	}
	
	public static void checkOffersMoreThan(WebDriver driver, PO_DataBase db, int expectedValue) {
		List<WebElement> ofertasPagina = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@id=\"oferta\"]",
				PO_View.getTimeout());
		assertTrue(ofertasPagina.size()>expectedValue);
		
	}

	public static void pulsarOferta(WebDriver driver, String nombreOferta) {
		busquedaOferta(driver, nombreOferta);
		List<WebElement> oferta = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@id=\"oferta\"]",
				PO_View.getTimeout());
		oferta.get(0).click();
		
		
	}
	
	public static void comprarOferta(WebDriver driver) {
		PO_NavView.checkIdOnView(driver, "testVistaOfertaConcreta");
		
		List<WebElement> botonCompra = SeleniumUtils.EsperaCargaPagina(driver, "free", "//*[@id=\"btnCompra\"]",
				PO_View.getTimeout());
		botonCompra.get(0).click();
		
		
	}
}
