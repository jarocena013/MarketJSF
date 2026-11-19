package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	public void create() {
		String email = "seller1@gmail.com";
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

			// UploadedFile fitxategitik java.io.File bat sortu
			java.io.File targetFile = null;
			if (uploadedFile != null && uploadedFile.getFileName() != null && !uploadedFile.getFileName().isEmpty()) {
				// Sortu behin-behineko fitxategi bat zerbitzarian
				targetFile = java.io.File.createTempFile("upload_", "_" + uploadedFile.getFileName());

				// Igotako fitxategiaren edukiak transferitu java.io.File horretara
				try (java.io.InputStream input = uploadedFile.getInputStream();
						java.io.OutputStream out = new java.io.FileOutputStream(targetFile)) {
					byte[] buffer = new byte[1024];
					int length;
					while ((length = input.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
				}
			}

			// Orain java.io.File (targetFile) pasatzen diogu createSale metodoari
			FacadeBean.getBusinessLogic().createSale(izena, deskripzioa, ezenb, prezioa, date, email, targetFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sale berri bat sortu da arazorik gabe", null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Errorea salmenta sortzean: " + e.getMessage(), null));
		}
	}
}