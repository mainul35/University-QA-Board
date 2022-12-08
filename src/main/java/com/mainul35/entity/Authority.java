package com.mainul35.entity;

import jakarta.persistence.GeneratedValue;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ewsd_tbl_authority")
public class Authority implements GrantedAuthority, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2068750034683931763L;
	@GeneratedValue
	@Id
    @Column(name = "role_uuid", nullable = false)
    private Long id;
    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    public Authority() {
    }

    public Authority(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String role){
        this.authority = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority)) return false;
        Authority authority = (Authority) o;
        return Objects.equals(id, authority.id) &&
                Objects.equals(authority, authority.authority);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, authority);
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", roleName='" + authority + '\'' +
                '}';
    }
}
