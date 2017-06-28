package Beans;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * @author Tássio
 */
@ManagedBean(name="idiomaBean", eager = true)
@SessionScoped
public class IdiomaBean implements Serializable{
    private String linguagem="";
    private String pais="";
    private Locale CurrentLocal;
    
    public String mudarIdioma() {
        this.mudarLocalidade(new Locale (linguagem,pais));
        return null;
    }
    
    private void mudarLocalidade(Locale locale){
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        CurrentLocal = locale;
    }
    public IdiomaBean() {
        CurrentLocal = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Locale getCurrentLocal() {
        return CurrentLocal;
    }

    public void setCurrentLocal(Locale CurrentLocal) {
        this.CurrentLocal = CurrentLocal;
    }

}
