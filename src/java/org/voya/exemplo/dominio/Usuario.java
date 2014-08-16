package org.voya.exemplo.dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.voya.core.ClasseDominio;

@Entity
@Table(name = "USUARIO")
public class Usuario extends ClasseDominio implements Serializable, org.voya.core.security.Usuario 
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    
    @Basic(optional = false)
    @Column(name = "LOGIN")
    private String login;
    
    @Basic(optional = false)
    @Column(name = "SENHA")
    private String senha;
    
    @Basic(optional = false)
    @Column(name = "DATA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    
    @Column(name = "PERFIL")
    private String perfil;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String nome, String login, String senha, Date dataHora) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.dataHora = dataHora;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.voya.exemplo.dominio.Usuario[ id=" + id + " ]";
    }

    public static Usuario findById(Integer id) throws Exception
    {
        return (Usuario) ClasseDominio.findById(Usuario.class, id);
    }    
    
    public static List<Usuario> findAll() throws Exception
    {
        return ClasseDominio.findAll(Usuario.class);
    } 

}
