package ma.credit360.web.dto;

public class UserDto {
    private Long id;
    private String nom;
    private String fonction;
    private String email;
    private String role;
    private boolean actif;

    public UserDto() {}

    public UserDto(Long id, String nom, String fonction, String email, String role, boolean actif) {
        this.id = id;
        this.nom = nom;
        this.fonction = fonction;
        this.email = email;
        this.role = role;
        this.actif = actif;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}
