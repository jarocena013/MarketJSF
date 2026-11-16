package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import businessLogic.BLFacade;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
 

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

	public void create() {
		String email = "seller1@gmail.com";
		try {
			try {
				int ezenb;
				if(egoera.equals("Berria")) {
					ezenb=1;
				}else if(egoera.equals("Oso ona")) {
					ezenb=2;
				}else if(egoera.equals("Egokia")) {
					ezenb=3;
				}else {
					ezenb=4;
				}
				FacadeBean.getBusinessLogic().createSale(izena, deskripzioa, ezenb, prezioa, date, email, null);
			} catch (ParamNullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sale berri bat sortu da arazorik gabe", null));
		} catch (SaleAlreadyExistException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sale hau dagoeneko existitzen da", null));
		} catch (MustBeLaterThanTodayException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Salearen publikazio datak ezin du gaurkoa baino lehenagokoa izan", null));
		}
	}

}
