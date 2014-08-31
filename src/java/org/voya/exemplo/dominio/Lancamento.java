package org.voya.exemplo.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.voya.core.ClasseDominio;

@Entity
@Table(name = "LANCAMENTO")

public class Lancamento extends ClasseDominio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "DESCRICAO")
    private String descricao;
    
    @Basic(optional = false)
    @Column(name = "DATA")
    @Temporal(TemporalType.DATE)
    private Date data;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigDecimal valor;
    
    @JoinColumn(name = "CATEGORIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Categoria categoria;
    
    @JoinColumn(name = "CONTA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Conta conta;

    public Lancamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoriaId) {
        this.categoria = categoriaId;
    }
    
    /*public void setCategoria(String id) {
        this.categoria = new Categoria(Integer.parseInt(id));
    }*/

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta contaId) {
        this.conta = contaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lancamento)) {
            return false;
        }
        Lancamento other = (Lancamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.voya.exemplo.dominio.Lancamento[ id=" + id + " ]";
    }
    
    
    public static Lancamento findById(Long id) throws Exception
    {
        return (Lancamento) ClasseDominio.findById(Lancamento.class, id);
    }    
    
    public static List<Lancamento> findAll() throws Exception
    {
        return ClasseDominio.findAll(Lancamento.class);
    } 
    
}
