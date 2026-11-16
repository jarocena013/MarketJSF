package eredua.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

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
	public String onEventSelect(SelectEvent event) {
		sale=(Sale) event.getObject();
		return "Salezehatz";
	}

}
