package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutDossier;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_statuts")
public class HistoriqueStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    private Dossier dossier;

    @Enumerated(EnumType.STRING)
    private StatutDossier ancienStatut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDossier nouveauStatut;

    @Column(nullable = false)
    private String action;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private LocalDateTime dateAction;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    public HistoriqueStatut() {}

    public HistoriqueStatut(Dossier dossier, StatutDossier ancienStatut, StatutDossier nouveauStatut, String action, Utilisateur utilisateur, String commentaire) {
        this.dossier = dossier;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.action = action;
        this.utilisateur = utilisateur;
        this.commentaire = commentaire;
        this.dateAction = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public StatutDossier getAncienStatut() { return ancienStatut; }
    public void setAncienStatut(StatutDossier ancienStatut) { this.ancienStatut = ancienStatut; }

    public StatutDossier getNouveauStatut() { return nouveauStatut; }
    public void setNouveauStatut(StatutDossier nouveauStatut) { this.nouveauStatut = nouveauStatut; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
