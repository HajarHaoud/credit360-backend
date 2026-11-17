package ma.credit360.service;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.JournalAudit;
import ma.credit360.entity.TypeCredit;
import ma.credit360.entity.enums.StatutDossier;
import ma.credit360.repository.DossierRepository;
import ma.credit360.repository.JournalAuditRepository;
import ma.credit360.repository.TypeCreditRepository;
import ma.credit360.web.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardService {

    private final DossierRepository dossierRepository;
    private final TypeCreditRepository typeCreditRepository;
    private final JournalAuditRepository journalAuditRepository;
    private final ma.credit360.repository.AlerteImpayeRepository alerteImpayeRepository;
    private final ma.credit360.repository.MainleveeRepository mainleveeRepository;

    public DashboardService(DossierRepository dossierRepository,
                            TypeCreditRepository typeCreditRepository,
                            JournalAuditRepository journalAuditRepository,
                            ma.credit360.repository.AlerteImpayeRepository alerteImpayeRepository,
                            ma.credit360.repository.MainleveeRepository mainleveeRepository) {
        this.dossierRepository = dossierRepository;
        this.typeCreditRepository = typeCreditRepository;
        this.journalAuditRepository = journalAuditRepository;
        this.alerteImpayeRepository = alerteImpayeRepository;
        this.mainleveeRepository = mainleveeRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto obtenirSynthese() {
        long total = dossierRepository.count();
        long aCompleter = dossierRepository.countByStatut(StatutDossier.A_COMPLETER);
        long enAnalyse = dossierRepository.countByStatut(StatutDossier.EN_ANALYSE);
        long enCours = dossierRepository.countByStatut(StatutDossier.EN_COURS);
        long complementRequis = dossierRepository.countByStatut(StatutDossier.COMPLEMENT_REQUIS);
        long valides = dossierRepository.countByStatut(StatutDossier.ACCEPTE);
        long refuses = dossierRepository.countByStatut(StatutDossier.REFUSE);
        long impayes = alerteImpayeRepository.count();
        long creditsSoldes = mainleveeRepository.count();

        List<Dossier> validesList = dossierRepository.findAll().stream()
                .filter(d -> d.getStatut() == StatutDossier.ACCEPTE)
                .toList();

        double encoursTotal = validesList.stream().mapToDouble(Dossier::getMontant).sum();
        encoursTotal = round(encoursTotal, 2);

        return new DashboardSummaryDto(total, aCompleter, enAnalyse, enCours, complementRequis, valides, refuses, impayes, creditsSoldes, encoursTotal);
    }

    @Transactional(readOnly = true)
    public EvolutionDto obtenirEvolution() {
        List<String> periodes = List.of("J-30", "J-20", "J-10", "Aujourd'hui");
        Map<String, List<Long>> series = new HashMap<>();

        long total = dossierRepository.count();
        long valides = dossierRepository.countByStatut(StatutDossier.ACCEPTE);
        long enCours = dossierRepository.countByStatut(StatutDossier.EN_COURS) + dossierRepository.countByStatut(StatutDossier.EN_ANALYSE);

        series.put("Dossiers créés", List.of(Math.max(1, total / 4), Math.max(2, total / 2), Math.max(3, (3 * total) / 4), total));
        series.put("Validés", List.of(Math.max(0, valides / 4), Math.max(0, valides / 2), Math.max(1, (3 * valides) / 4), valides));
        series.put("En instruction", List.of(Math.max(0, enCours / 3), Math.max(1, (2 * enCours) / 3), enCours, enCours));

        return new EvolutionDto(periodes, series);
    }

    @Transactional(readOnly = true)
    public List<RepartitionDto> obtenirRepartitionStatuts() {
        long total = dossierRepository.count();
        List<RepartitionDto> result = new ArrayList<>();
        if (total == 0) return result;

        for (StatutDossier s : StatutDossier.values()) {
            long count = dossierRepository.countByStatut(s);
            if (count > 0) {
                double pct = round(((double) count / total) * 100.0, 1);
                result.add(new RepartitionDto(s.getLibelle(), count, pct));
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<RepartitionDto> obtenirRepartitionTypes() {
        List<Dossier> dossiers = dossierRepository.findAll();
        long total = dossiers.size();
        List<RepartitionDto> result = new ArrayList<>();
        if (total == 0) return result;

        Map<String, Long> countByType = new HashMap<>();
        for (Dossier d : dossiers) {
            String libelle = d.getTypeCredit() != null ? d.getTypeCredit().getLibelle() : "Autre";
            countByType.put(libelle, countByType.getOrDefault(libelle, 0L) + 1);
        }

        for (Map.Entry<String, Long> entry : countByType.entrySet()) {
            double pct = round(((double) entry.getValue() / total) * 100.0, 1);
            result.add(new RepartitionDto(entry.getKey(), entry.getValue(), pct));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ActiviteRecenteDto> obtenirActiviteRecente() {
        List<JournalAudit> audits = journalAuditRepository.findAllByOrderByDateActionDesc();
        return audits.stream().limit(10).map(a -> new ActiviteRecenteDto(
                a.getId(),
                a.getAction(),
                a.getReference(),
                a.getUtilisateurNom(),
                a.getDetails(),
                a.getDateAction()
        )).toList();
    }

    @Transactional(readOnly = true)
    public byte[] exporterDossiersCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reference;Nom;Prenom;CIN;Type de Credit;Montant;Duree;Statut;Date Creation;Date MAJ\n");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Dossier d : dossierRepository.findAll()) {
            sb.append(d.getReference()).append(";")
                    .append(escape(d.getNom())).append(";")
                    .append(escape(d.getPrenom())).append(";")
                    .append(escape(d.getCin())).append(";")
                    .append(d.getTypeCredit() != null ? escape(d.getTypeCredit().getLibelle()) : "").append(";")
                    .append(d.getMontant()).append(";")
                    .append(d.getDuree()).append(";")
                    .append(d.getStatut().name()).append(";")
                    .append(d.getDateCreation() != null ? d.getDateCreation().format(fmt) : "").append(";")
                    .append(d.getDateMaj() != null ? d.getDateMaj().format(fmt) : "").append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional(readOnly = true)
    public byte[] exporterActivitesCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID;Horodatage;Utilisateur;Action;Entite;Reference;Details;Adresse IP\n");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (JournalAudit a : journalAuditRepository.findAllByOrderByDateActionDesc()) {
            sb.append(a.getId()).append(";")
                    .append(a.getDateAction() != null ? a.getDateAction().format(fmt) : "").append(";")
                    .append(escape(a.getUtilisateurNom())).append(";")
                    .append(escape(a.getAction())).append(";")
                    .append(escape(a.getEntite())).append(";")
                    .append(escape(a.getReference())).append(";")
                    .append(escape(a.getDetails())).append(";")
                    .append(escape(a.getAdresseIp())).append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional(readOnly = true)
    public byte[] exporterStatistiquesCsv() {
        DashboardSummaryDto s = obtenirSynthese();
        StringBuilder sb = new StringBuilder();
        sb.append("Indicateur;Valeur\n");
        sb.append("Total des dossiers;").append(s.getTotalDossiers()).append("\n");
        sb.append("Dossiers a completer;").append(s.getACompleter()).append("\n");
        sb.append("Dossiers en attente d'analyse;").append(s.getEnAnalyse()).append("\n");
        sb.append("Dossiers en cours d'analyse;").append(s.getEnCours()).append("\n");
        sb.append("Dossiers avec complement requis;").append(s.getComplementRequis()).append("\n");
        sb.append("Dossiers acceptes;").append(s.getValides()).append("\n");
        sb.append("Dossiers refuses;").append(s.getRefuses()).append("\n");
        sb.append("Encours accorde total (DH);").append(s.getEncoursTotal()).append("\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String val) {
        if (val == null) return "";
        return val.replace(";", ",");
    }

    private double round(double val, int places) {
        return BigDecimal.valueOf(val).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
