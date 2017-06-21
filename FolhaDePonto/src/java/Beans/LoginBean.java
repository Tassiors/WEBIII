/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controlador.FuncionarioJpaController;
import Entidades.Funcionario;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Tássio
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{

    @NotNull
    String login;
    @NotNull
    String senha;
    
    private FuncionarioJpaController funcCtrl;
    
    public LoginBean() {
    }
    
    public String logar(){
        String mensagem = null;
        Funcionario f = funcCtrl.findLogin(login, senha);
        //cria uma sessão caso o usuário exista
        if(f != null){
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.setAttribute("usuarioLogado", f);
            mensagem = "Usuário encontrado";
        }else {
            mensagem = "usuário não encontrado";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", mensagem));
        return "index.xhtml";
    }
    
    
}
