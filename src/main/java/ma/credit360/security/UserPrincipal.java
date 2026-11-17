package ma.credit360.security;

import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String nom;
    private final String email;
    private final String motDePasse;
    private final Role role;
    private final String fonction;
    private final boolean actif;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.nom = utilisateur.getNom();
        this.email = utilisateur.getEmail();
        this.motDePasse = utilisateur.getMotDePasse();
        this.role = utilisateur.getRole();
        this.fonction = utilisateur.getFonction();
        this.actif = utilisateur.isActif();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public Role getRole() { return role; }
    public String getFonction() { return fonction; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return motDePasse; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return actif; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return actif; }
}
