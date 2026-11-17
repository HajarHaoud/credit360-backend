package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutAssurance;
import ma.credit360.entity.enums.TypeAssurance;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assurances_credits")
public class AssuranceCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String referenceContrat;

    private String referenceDossier;

    @Column(nullable = false)
    private String client;

    private String cin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAssurance typeAssurance;

    @Column(nullable = false)
    private Double capitalAssure;

    @Column(nullable = false)
    private Double tauxPrimePourcent; // ex: 0.35%

    @Column(nullable = false)
    private Double primeMensuelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAssurance statut = StatutAssurance.PROPOSEE;

    @Column(nullable = false)
    private String compagnieAssurance = "Banque Populaire Assurances";

    private LocalDate dateEffet;
    private LocalDateTime dateSouscription;

    public AssuranceCredit() {}

    public AssuranceCredit(String referenceContrat, String referenceDossier, String client, String cin,
                           TypeAssurance typeAssurance, Double capitalAssure, Double tauxPrimePourcent) {
        this.referenceContrat = referenceContrat;
        this.referenceDossier = referenceDossier;
        this.client = client;
        this.cin = cin;
        this.typeAssurance = typeAssurance;
        this.capitalAssure = capitalAssure;
        this.tauxPrimePourcent = tauxPrimePourcent;
        this.primeMensuelle = Math.round((capitalAssure * (tauxPrimePourcent / 100.0) / 12.0) * 100.0) / 100.0;
        this.statut = StatutAssurance.PROPOSEE;
        this.compagnieAssurance = "Banque Populaire Assurances";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceContrat() { return referenceContrat; }
    public void setReferenceContrat(String referenceContrat) { this.referenceContrat = referenceContrat; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public TypeAssurance getTypeAssurance() { return typeAssurance; }
    public void setTypeAssurance(TypeAssurance typeAssurance) { this.typeAssurance = typeAssurance; }

    public Double getCapitalAssure() { return capitalAssure; }
    public void setCapitalAssure(Double capitalAssure) { this.capitalAssure = capitalAssure; }

    public Double getTauxPrimePourcent() { return tauxPrimePourcent; }
    public void setTauxPrimePourcent(Double tauxPrimePourcent) { this.tauxPrimePourcent = tauxPrimePourcent; }

    public Double getPrimeMensuelle() { return primeMensuelle; }
    public void setPrimeMensuelle(Double primeMensuelle) { this.primeMensuelle = primeMensuelle; }

    public StatutAssurance getStatut() { return statut; }
    public void setStatut(StatutAssurance statut) { this.statut = statut; }

    public String getCompagnieAssurance() { return compagnieAssurance; }
    public void setCompagnieAssurance(String compagnieAssurance) { this.compagnieAssurance = compagnieAssurance; }

    public LocalDate getDateEffet() { return dateEffet; }
    public void setDateEffet(LocalDate dateEffet) { this.dateEffet = dateEffet; }

    public LocalDateTime getDateSouscription() { return dateSouscription; }
    public void setDateSouscription(LocalDateTime dateSouscription) { this.dateSouscription = dateSouscription; }
}
