/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Tássio
 */
@Embeddable
public class FuncionarioPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frequencia_id")
    private int frequenciaId;

    public FuncionarioPK() {
    }

    public FuncionarioPK(int id, int frequenciaId) {
        this.id = id;
        this.frequenciaId = frequenciaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrequenciaId() {
        return frequenciaId;
    }

    public void setFrequenciaId(int frequenciaId) {
        this.frequenciaId = frequenciaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) frequenciaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioPK)) {
            return false;
        }
        FuncionarioPK other = (FuncionarioPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.frequenciaId != other.frequenciaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.FuncionarioPK[ id=" + id + ", frequenciaId=" + frequenciaId + " ]";
    }
    
}
