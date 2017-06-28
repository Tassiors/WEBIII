/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controlador.FuncionarioJpaController;
import Controlador.exceptions.NonexistentEntityException;
import Entidades.Funcionario;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tássio
 */
@ManagedBean(name = "CrudBean")
@ViewScoped
public class CrudBean implements Serializable {

    private EntityManagerFactory emf;
    private FuncionarioJpaController controllerJpaFunc;
    private Boolean editando;
    private List<Funcionario> listaFuncionarios;
    private Funcionario objeto;

    @NotNull
    @Size(min = 1, max = 200)
    private String nome;

    @NotNull
    @Size(min = 1, max = 200)
    private String setor;

    @NotNull
    private int tipo;

    @NotNull
    @Size(min = 1, max = 45)
    private String login;

    @NotNull
    @Size(min = 1, max = 45)
    private String senha;

    public CrudBean() {
        editando = false;
    }

    @PostConstruct
    public void init() {
        controllerJpaFunc = new FuncionarioJpaController(emf);
        //listaFuncionarios = controllerJpaFunc.findFuncionarioEntities();
    }

    public void novo() {
        objeto = new Funcionario();
        editando = true;
    }

    public void salvar() {
        Funcionario func = new Funcionario();
        func.setNome(nome);
        func.setSetor(setor);
        func.setLogin(login);
        func.setSenha(senha);
        func.setTipo(tipo);

        try {
            controllerJpaFunc.create(func);
        } catch (Exception ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        editando = false;
    }

    public String listar() {
        return "gerenciarFuncionarios2?faces-redirect=true";
    }

    public void editar(Funcionario func) {
        objeto = func;
        editando = true;
    }

    public void excluir(Funcionario func) {
        try {
            controllerJpaFunc.destroy(func.getFuncionarioPK());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelar() {
        editando = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Funcionario> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public Funcionario getObjeto() {
        return objeto;
    }

    public void setObjeto(Funcionario objeto) {
        this.objeto = objeto;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }
}
