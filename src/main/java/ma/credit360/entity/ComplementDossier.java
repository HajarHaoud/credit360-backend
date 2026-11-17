package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutComplement;
import java.time.LocalDateTime;

@Entity
@Table(name = "complements_dossiers")
public class ComplementDossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    private Dossier dossier;

    @Column(nullable = false)
    private String motif;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    private String piecesConcernees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutComplement statut = StatutComplement.EN_ATTENTE;

    @Column(nullable = false)
    private LocalDateTime dateDemande;

    private LocalDateTime dateReponse;
    private LocalDateTime dateCloture;

    @Column(columnDefinition = "TEXT")
    private String reponseCommentaire;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "demande_par_id")
    private Utilisateur demandePar;

    public ComplementDossier() {}

    @PrePersist
    public void onPrePersist() {
        this.dateDemande = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutComplement.EN_ATTENTE;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getPiecesConcernees() { return piecesConcernees; }
    public void setPiecesConcernees(String piecesConcernees) { this.piecesConcernees = piecesConcernees; }

    public StatutComplement getStatut() { return statut; }
    public void setStatut(StatutComplement statut) { this.statut = statut; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }

    public LocalDateTime getDateCloture() { return dateCloture; }
    public void setDateCloture(LocalDateTime dateCloture) { this.dateCloture = dateCloture; }

    public String getReponseCommentaire() { return reponseCommentaire; }
    public void setReponseCommentaire(String reponseCommentaire) { this.reponseCommentaire = reponseCommentaire; }

    public Utilisateur getDemandePar() { return demandePar; }
    public void setDemandePar(Utilisateur demandePar) { this.demandePar = demandePar; }
}
