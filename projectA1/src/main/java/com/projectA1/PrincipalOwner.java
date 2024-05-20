package com.projectA1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.projectA1.model.Owner;

public class PrincipalOwner implements UserDetails {

    private Owner owner;

    public PrincipalOwner(Owner owner) {
        this.owner = owner;
    }
	
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
    	List<GrantedAuthority> authorities = new ArrayList<>();
        for (String ownerRole : owner.getRole()) {
            authorities.add(new SimpleGrantedAuthority(ownerRole));
        }
        return authorities;
    }

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return owner.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return owner.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
