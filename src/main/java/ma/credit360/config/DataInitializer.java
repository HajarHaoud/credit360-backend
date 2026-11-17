package ma.credit360.config;

import ma.credit360.entity.*;
import ma.credit360.entity.enums.*;
import ma.credit360.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final TypeCreditRepository typeCreditRepository;
    private final PieceRequiseRepository pieceRequiseRepository;
    private final DossierRepository dossierRepository;
    private final SeuilHabilitationRepository seuilHabilitationRepository;
    private final ParametreSystemeRepository parametreSystemeRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;
    private final NotificationRepository notificationRepository;
    private final AlerteImpayeRepository alerteImpayeRepository;
    private final RechargeCreditRepository rechargeCreditRepository;
    private final MainleveeRepository mainleveeRepository;
    private final AssuranceCreditRepository assuranceCreditRepository;
    private final SignatureDigitaleRepository signatureDigitaleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UtilisateurRepository utilisateurRepository,
                           TypeCreditRepository typeCreditRepository,
                           PieceRequiseRepository pieceRequiseRepository,
                           DossierRepository dossierRepository,
                           SeuilHabilitationRepository seuilHabilitationRepository,
                           ParametreSystemeRepository parametreSystemeRepository,
                           HistoriqueStatutRepository historiqueStatutRepository,
                           NotificationRepository notificationRepository,
                           AlerteImpayeRepository alerteImpayeRepository,
                           RechargeCreditRepository rechargeCreditRepository,
                           MainleveeRepository mainleveeRepository,
                           AssuranceCreditRepository assuranceCreditRepository,
                           SignatureDigitaleRepository signatureDigitaleRepository,
                           PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.typeCreditRepository = typeCreditRepository;
        this.pieceRequiseRepository = pieceRequiseRepository;
        this.dossierRepository = dossierRepository;
        this.seuilHabilitationRepository = seuilHabilitationRepository;
        this.parametreSystemeRepository = parametreSystemeRepository;
        this.historiqueStatutRepository = historiqueStatutRepository;
        this.notificationRepository = notificationRepository;
        this.alerteImpayeRepository = alerteImpayeRepository;
        this.rechargeCreditRepository = rechargeCreditRepository;
        this.mainleveeRepository = mainleveeRepository;
        this.assuranceCreditRepository = assuranceCreditRepository;
        this.signatureDigitaleRepository = signatureDigitaleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (utilisateurRepository.count() > 0) {
            return;
        }

        // 1. Initialisation des Utilisateurs Démo
        String passHash = passwordEncoder.encode("demo1234");

        Utilisateur conseiller = new Utilisateur("Asmaa El Habachi", "conseiller@credit360.ma", passHash, Role.CONSEILLER, "Conseillère Commerciale");
        Utilisateur analyste = new Utilisateur("Youssef Amrani", "analyste@credit360.ma", passHash, Role.ANALYSTE, "Analyste Crédit");
        Utilisateur responsable = new Utilisateur("Karim Tazi", "responsable@credit360.ma", passHash, Role.RESPONSABLE, "Responsable Engagements");
        Utilisateur admin = new Utilisateur("Admin Système", "admin@credit360.ma", passHash, Role.ADMINISTRATEUR, "Administrateur Système");

        utilisateurRepository.saveAll(List.of(conseiller, analyste, responsable, admin));

        // 2. Initialisation des Types de Crédit et Pièces Requises
        TypeCredit immo = new TypeCredit("immobilier", "Crédit Immobilier (Salaf Sakane)", FamilleCredit.IMMOBILIER, 4.5, 50000.0, 3000000.0, 12, 300);
        TypeCredit conso = new TypeCredit("consommation", "Prêt Personnel (Al Ousra)", FamilleCredit.CONSOMMATION, 6.5, 10000.0, 300000.0, 6, 84);
        TypeCredit auto = new TypeCredit("auto", "Crédit Auto (Salaf Sayara)", FamilleCredit.AUTO, 5.2, 30000.0, 500000.0, 12, 72);
        TypeCredit pro = new TypeCredit("professionnel", "Équipement Professionnel", FamilleCredit.PROFESSIONNEL, 5.8, 50000.0, 1500000.0, 12, 120);

        typeCreditRepository.saveAll(List.of(immo, conso, auto, pro));

        // Pièces requises Immobilier
        pieceRequiseRepository.save(new PieceRequise("Copie de la CIN (recto-verso)", true, 1, immo));
        pieceRequiseRepository.save(new PieceRequise("Attestation de travail récente", true, 2, immo));
        pieceRequiseRepository.save(new PieceRequise("3 derniers bulletins de paie", true, 3, immo));
        pieceRequiseRepository.save(new PieceRequise("3 derniers relevés bancaires", true, 4, immo));
        pieceRequiseRepository.save(new PieceRequise("Compromis de vente ou certificat de propriété", true, 5, immo));
        pieceRequiseRepository.save(new PieceRequise("Devis estimatif des travaux", false, 6, immo));

        // Pièces requises Consommation
        pieceRequiseRepository.save(new PieceRequise("Copie de la CIN (recto-verso)", true, 1, conso));
        pieceRequiseRepository.save(new PieceRequise("Attestation de travail récente", true, 2, conso));
        pieceRequiseRepository.save(new PieceRequise("3 derniers bulletins de paie", true, 3, conso));
        pieceRequiseRepository.save(new PieceRequise("Relevé d'identité bancaire (RIB)", true, 4, conso));

        // Pièces requises Auto
        pieceRequiseRepository.save(new PieceRequise("Copie de la CIN (recto-verso)", true, 1, auto));
        pieceRequiseRepository.save(new PieceRequise("Devis proforma ou bon de commande véhicule", true, 2, auto));
        pieceRequiseRepository.save(new PieceRequise("3 derniers bulletins de paie", true, 3, auto));
        pieceRequiseRepository.save(new PieceRequise("3 derniers relevés bancaires", true, 4, auto));

        // Pièces requises Professionnel
        pieceRequiseRepository.save(new PieceRequise("Registre de commerce (Modèle J)", true, 1, pro));
        pieceRequiseRepository.save(new PieceRequise("Bilans comptables certifiés des 2 derniers exercices", true, 2, pro));
        pieceRequiseRepository.save(new PieceRequise("Relevés bancaires des 6 derniers mois", true, 3, pro));
        pieceRequiseRepository.save(new PieceRequise("Devis du matériel à acquérir", true, 4, pro));

        // 3. Initialisation des Seuils d'Habilitation
        seuilHabilitationRepository.save(new SeuilHabilitation(Role.ANALYSTE, 0.0, 500000.0, true));
        seuilHabilitationRepository.save(new SeuilHabilitation(Role.RESPONSABLE, 0.0, 2000000.0, true));
        seuilHabilitationRepository.save(new SeuilHabilitation(Role.ADMINISTRATEUR, 0.0, 999999999.0, true));

        // 4. Initialisation des Paramètres Système
        parametreSystemeRepository.save(new ParametreSysteme("UPLOAD_MAX_SIZE_MB", "5", "Taille maximale par pièce justificative en Mo", "NUMBER"));
        parametreSystemeRepository.save(new ParametreSysteme("UPLOAD_ALLOWED_EXTENSIONS", "pdf,jpg,jpeg,png", "Formats acceptés", "STRING"));
        parametreSystemeRepository.save(new ParametreSysteme("TAUX_ENDETTEMENT_MAX_POURCENT", "45", "Seuil d'endettement maximal réglementaire", "NUMBER"));
        parametreSystemeRepository.save(new ParametreSysteme("SIMULATION_VALIDITE_JOURS", "30", "Durée de validité des simulations en jours", "NUMBER"));
        parametreSystemeRepository.save(new ParametreSysteme("APP_VERSION", "1.0.0", "Version du système", "STRING"));

        // 5. Initialisation des Dossiers Démo
        Dossier d1 = creerDossierDemo("CRD00013", "Bennani", "Karim", "A123456", "0612345678", "karim.bennani@email.ma",
                "Ingénieur IT", "Casablanca Anfa", 22000.0, 2500.0, immo, 1000000.0, 120, StatutDossier.A_COMPLETER, conseiller, null);

        Dossier d2 = creerDossierDemo("CRD00014", "Salhi", "Amine", "BK789012", "0661223344", "amine.salhi@email.ma",
                "Cadre Financier", "Rabat Agdal", 16000.0, 1800.0, conso, 120000.0, 36, StatutDossier.EN_ANALYSE, conseiller, null);

        Dossier d3 = creerDossierDemo("CRD00015", "Laraki", "Nadia", "C456789", "0677889900", "nadia.laraki@email.ma",
                "Architecte", "Marrakech Guéliz", 19500.0, 2200.0, auto, 280000.0, 60, StatutDossier.EN_COURS, conseiller, analyste);

        Dossier d4 = creerDossierDemo("CRD00016", "Mansouri", "Youssef", "D321654", "0655443322", "youssef.mansouri@email.ma",
                "Chef d'Entreprise", "Tanger City", 45000.0, 6000.0, pro, 750000.0, 84, StatutDossier.ACCEPTE, conseiller, analyste);

        Dossier d5 = creerDossierDemo("CRD00017", "Razi", "Salma", "E987654", "0666778899", "salma.razi@email.ma",
                "Médecin", "Fès Narjiss", 32000.0, 4500.0, immo, 1800000.0, 240, StatutDossier.COMPLEMENT_REQUIS, conseiller, analyste);

        // Notifications démo pour Conseiller et Analyste
        notificationRepository.save(new Notification(
                conseiller,
                "Complément requis sur dossier",
                "Le dossier CRD00017 nécessite une copie lisible de l'attestation de travail.",
                TypeNotification.COMPLEMENT_DEMANDE,
                "CRD00017"
        ));
        notificationRepository.save(new Notification(
                analyste,
                "Nouveau dossier en attente",
                "Le dossier CRD00014 a été transmis pour analyse de faisabilité.",
                TypeNotification.DOSSIER_SOUMIS,
                "CRD00014"
        ));

        // 6. Initialisation des Impayés Démo (Visibles sur la maquette Banque Populaire)
        alerteImpayeRepository.save(new AlerteImpaye(
                "E. Rachid", "C123987", "0611223344", "rachid.e@email.ma", "CONSO",
                "CRD00010", 30, StatutImpaye.PREMIER_IMPAYE, ActionImpaye.RELANCE, 4200.0, 58000.0
        ));
        alerteImpayeRepository.save(new AlerteImpaye(
                "K. Fatima", "D654321", "0622334455", "fatima.k@email.ma", "AUTO",
                "CRD00011", 60, StatutImpaye.DEUXIEME_IMPAYE, ActionImpaye.MISE_EN_DEMEURE, 7850.0, 142000.0
        ));
        alerteImpayeRepository.save(new AlerteImpaye(
                "M. Hassan", "A789456", "0633445566", "hassan.m@email.ma", "IMMO",
                "CRD00012", 90, StatutImpaye.TROISIEME_IMPAYE, ActionImpaye.PROCEDURE, 18400.0, 680000.0
        ));

        // 7. Initialisation de la Recharge Démo (M. Ahmed sur la maquette)
        RechargeCredit rechargeAhmed = new RechargeCredit(
                "RCH00001", "M. Ahmed", "B543210", "0644556677",
                120000.0, 60000.0, 200000.0, 10000.0, 84, 5.5
        );
        rechargeAhmed.setNouvelleMensualite(2878.45);
        rechargeCreditRepository.save(rechargeAhmed);

        // 8. Initialisation de la Mainlevée Démo (Crédit soldé sur la maquette)
        Mainlevee mlv1 = new Mainlevee(
                "MLV00001", "CRD00008", "S. Karim", "A456123",
                "Hypothèque immobilière 1er rang", "Titre foncier N° 45892/05 - Résidence Al Majd Casablanca",
                850000.0, java.time.LocalDate.now().minusYears(7), java.time.LocalDate.now().minusDays(15)
        );
        mainleveeRepository.save(mlv1);

        // 9. Initialisation des Assurances Démo
        AssuranceCredit ass1 = new AssuranceCredit(
                "ASS00001", "CRD00013", "Bennani Karim", "A123456",
                TypeAssurance.DECES_INVALIDITE, 1000000.0, 0.35
        );
        ass1.setStatut(StatutAssurance.SOUSCRITE);
        ass1.setDateSouscription(LocalDateTime.now().minusDays(4));
        ass1.setDateEffet(java.time.LocalDate.now());
        assuranceCreditRepository.save(ass1);

        // 10. Initialisation du parcours de signature sur le dossier d4 (CRD00016 - Validé)
        SignatureDigitale sigCl = new SignatureDigitale(d4, TypeSignataire.CLIENT, "Youssef Mansouri", "D321654", "0655443322");
        SignatureDigitale sigCo = new SignatureDigitale(d4, TypeSignataire.CONSEILLER, "Asmaa El Habachi", "CP-CASABLANCA", "conseiller@credit360.ma");
        SignatureDigitale sigRe = new SignatureDigitale(d4, TypeSignataire.RESPONSABLE, "Karim Tazi", "DIR-ENGAGEMENTS", "responsable@credit360.ma");
        signatureDigitaleRepository.saveAll(List.of(sigCl, sigCo, sigRe));
    }

    private Dossier creerDossierDemo(String ref, String nom, String prenom, String cin, String tel, String email,
                                    String prof, String adr, Double rev, Double chg, TypeCredit tc, Double mnt,
                                    Integer dur, StatutDossier stat, Utilisateur creePar, Utilisateur analyste) {
        Dossier d = new Dossier();
        d.setReference(ref);
        d.setNom(nom);
        d.setPrenom(prenom);
        d.setCin(cin);
        d.setTelephone(tel);
        d.setEmail(email);
        d.setProfession(prof);
        d.setAdresse(adr);
        d.setRevenuMensuel(rev);
        d.setChargesMensuelles(chg);
        d.setTypeCredit(tc);
        d.setMontant(mnt);
        d.setDuree(dur);
        d.setStatut(stat);
        d.setCreePar(creePar);
        d.setAnalysteAffecte(analyste);
        d.setDateCreation(LocalDateTime.now().minusDays(5));
        d.setDateMaj(LocalDateTime.now().minusHours(2));

        Dossier savedDossier = dossierRepository.save(d);

        HistoriqueStatut hist = new HistoriqueStatut(
                savedDossier, null, stat, "INITIALISATION", creePar, "Dossier initialisé pour la démonstration"
        );
        historiqueStatutRepository.save(hist);

        return savedDossier;
    }
}
