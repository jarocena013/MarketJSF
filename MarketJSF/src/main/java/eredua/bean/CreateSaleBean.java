package eredua.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import businessLogic.BLFacade;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.model.file.UploadedFile;

@Named("createSale")
@SessionScoped
public class CreateSaleBean implements Serializable {
	private static final long serialVersionUID = 1;

	private String izena;
	private String deskripzioa;
	private int prezioa;
	private Date date;
	private List<String> egoerak;
	private String egoera;
	private UploadedFile uploadedFile; // Irudia jasotzeko aldagaia
	private static final String IMAGE_PATH = "src/main/resources/images/";

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getDeskripzioa() {
		return deskripzioa;
	}

	public void setDeskripzioa(String deskripzioa) {
		this.deskripzioa = deskripzioa;
	}

	public int getPrezioa() {
		return prezioa;
	}

	public void setPrezioa(int prezioa) {
		this.prezioa = prezioa;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date data) {
		this.date = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getEgoerak() {
		egoerak = new ArrayList<String>();
		egoerak.add("Berria");
		egoerak.add("Oso ona");
		egoerak.add("Egokia");
		egoerak.add("Zaharra");
		return egoerak;
	}

	public void setEgoerak(List<String> egoerak) {
		this.egoerak = egoerak;
	}

	public String getEgoera() {
		return egoera;
	}

	public void setEgoera(String egoera) {
		this.egoera = egoera;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	private File saveImage() throws IOException {

		if (uploadedFile == null || uploadedFile.getSize() == 0) {
			return null;
		}

		String originalName = uploadedFile.getFileName();

		if (originalName == null || !originalName.contains(".")) {
			throw new IOException("Fitxategiaren formatua ez da zuzena");
		}

		String extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();

		if (!extension.matches("\\.(jpg|jpeg|png|gif)")) {
			throw new IOException("Irudi formatua ez da onartzen");
		}

		String fileName = UUID.randomUUID().toString() + extension;

		File directory = new File(IMAGE_PATH);

		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Ezin izan da irudien karpeta sortu");
		}

		File targetFile = new File(directory, fileName);

		try (InputStream input = uploadedFile.getInputStream()) {

			Files.copy(input, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		System.out.println("Irudia gordeta: " + targetFile.getAbsolutePath());

		return targetFile;
	}

	/**
	 * Salmenta berria sortzen du.
	 */
	public void create() {

		String email = "seller1@gmail.com";

		File targetFile = null;

		try {

			int ezenb;

			if (egoera.equals("Berria")) {
				ezenb = 1;
			} else if (egoera.equals("Oso ona")) {
				ezenb = 2;
			} else if (egoera.equals("Egokia")) {
				ezenb = 3;
			} else {
				ezenb = 4;
			}

			// Irudia gorde
			targetFile = saveImage();

			// Salmenta sortu
			BLFacade facade = FacadeBean.getBusinessLogic();

			facade.createSale(izena, deskripzioa, ezenb, prezioa, date, email, targetFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Salmenta berria arazorik gabe sortu da", null));

			// Formularioa garbitu
			izena = null;
			deskripzioa = null;
			prezioa = 0;
			date = null;
			egoera = null;
			uploadedFile = null;

		} catch (Exception e) {

			e.printStackTrace();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Errorea salmenta sortzean: " + e.getMessage(), null));
		}
	}
}