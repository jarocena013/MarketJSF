package eredua.bean;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import configuration.UtilDate;
import domain.Sale;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("querySale")
@SessionScoped
public class QuerySaleBean implements Serializable {
	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	private static final long serialVersionUID = 1L;

	private String izena;
	private List<Sale> results;
	private Sale sale;

	@PostConstruct
	public void init() {

	}

	public void searchSales() {
		if (FacadeBean.getBusinessLogic() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "DB not available."));
			return;
		}
		Date today = UtilDate.trim(new Date());
		this.results = FacadeBean.getBusinessLogic().getSales(izena);

		if (this.results == null || this.results.isEmpty()) {
			// SOLUCIÓN: Eliminamos el ResourceBundle y usamos un texto fijo sin
			// dependencias externas
			String msgBody = "Ez dago salmentarik baldintza horiekin";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
		}
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public List<Sale> getResults() {
		return results;
	}

	public void setResults(List<Sale> results) {
		this.results = results;
	}

	public String xehetasunakIkusi() {
		if (this.sale == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Abisua", "Mesedez, hautatu sale bat taulatik."));
			return null; // Ez da orriz aldatuko hautapenik ez badago
		}

		return "SaleZehatz.xhtml";
	}

	public StreamedContent getImage() {
		if (sale == null || sale.getFile() == null) {
			return null;
		}

		try {
			File file = new File("src/main/resources/images/" + sale.getFile());

			if (!file.exists()) {
				System.out.println("No se encuentra la imagen: " + file.getAbsolutePath());
				return null;
			}

			BufferedImage image = ImageIO.read(file);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", output);

			byte[] bytes = output.toByteArray();

			return DefaultStreamedContent.builder().contentType("image/jpeg")
					.stream(() -> new ByteArrayInputStream(bytes)).build();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
