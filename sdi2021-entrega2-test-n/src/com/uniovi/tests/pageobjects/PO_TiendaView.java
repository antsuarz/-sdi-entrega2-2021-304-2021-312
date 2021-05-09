package com.uniovi.tests.pageobjects;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_TiendaView {
	
	static public void checkUserCanSeeAllOffers(WebDriver driver, PO_DataBase db) {
		//		nos loggeamos
				PO_PrivateView.login(driver, "testprueba1@gmail.com", "123456");
				// COmprobamos que entramos en la pagina privada de Alumno
				// assert que estamos en la pagina correcta
				PO_NavView.checkIdOnView(driver, "testVistaTienda");
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
				assertEquals(numeroOfertasParaElUsuario, contador);
	}
}
