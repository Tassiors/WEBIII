/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tássio
 */
@Entity
@Table(name = "funcionario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funcionario.findByLogin", query = "SELECT f FROM Funcionario f WHERE f.login = :login and f.senha = :senha"),
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f"),
    @NamedQuery(name = "Funcionario.findById", query = "SELECT f FROM Funcionario f WHERE f.funcionarioPK.id = :id"),
    @NamedQuery(name = "Funcionario.findByNome", query = "SELECT f FROM Funcionario f WHERE f.nome = :nome"),
    @NamedQuery(name = "Funcionario.findBySetor", query = "SELECT f FROM Funcionario f WHERE f.setor = :setor"),
    @NamedQuery(name = "Funcionario.findByTipo", query = "SELECT f FROM Funcionario f WHERE f.tipo = :tipo"),
    @NamedQuery(name = "Funcionario.findBySenha", query = "SELECT f FROM Funcionario f WHERE f.senha = :senha"),
    @NamedQuery(name = "Funcionario.findByFrequenciaId", query = "SELECT f FROM Funcionario f WHERE f.funcionarioPK.frequenciaId = :frequenciaId"),
    @NamedQuery(name = "Funcionario.findByLogin", query = "SELECT f FROM Funcionario f WHERE f.login = :login")})
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FuncionarioPK funcionarioPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "setor")
    private String setor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private int tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "senha")
    private String senha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "login")
    private String login;
    @JoinColumn(name = "frequencia_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Frequencia frequencia;

    public Funcionario() {
    }

    public Funcionario(FuncionarioPK funcionarioPK) {
        this.funcionarioPK = funcionarioPK;
    }

    public Funcionario(FuncionarioPK funcionarioPK, String nome, String setor, int tipo, String senha, String login) {
        this.funcionarioPK = funcionarioPK;
        this.nome = nome;
        this.setor = setor;
        this.tipo = tipo;
        this.senha = senha;
        this.login = login;
    }

    public Funcionario(int id, int frequenciaId) {
        this.funcionarioPK = new FuncionarioPK(id, frequenciaId);
    }

    public FuncionarioPK getFuncionarioPK() {
        return funcionarioPK;
    }

    public void setFuncionarioPK(FuncionarioPK funcionarioPK) {
        this.funcionarioPK = funcionarioPK;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Frequencia getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Frequencia frequencia) {
        this.frequencia = frequencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funcionarioPK != null ? funcionarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.funcionarioPK == null && other.funcionarioPK != null) || (this.funcionarioPK != null && !this.funcionarioPK.equals(other.funcionarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Funcionario[ funcionarioPK=" + funcionarioPK + " ]";
    }
    
}
