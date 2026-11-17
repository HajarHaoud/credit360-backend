package ma.credit360.service;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.Simulation;
import ma.credit360.entity.TypeCredit;
import ma.credit360.repository.DossierRepository;
import ma.credit360.repository.SimulationRepository;
import ma.credit360.repository.TypeCreditRepository;
import ma.credit360.web.dto.SimulationParamsDto;
import ma.credit360.web.dto.SimulationRequest;
import ma.credit360.web.dto.SimulationResponse;
import ma.credit360.web.dto.TypeCreditDto;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationService {

    private final TypeCreditRepository typeCreditRepository;
    private final SimulationRepository simulationRepository;
    private final DossierRepository dossierRepository;
    private static final double SEUIL_ENDETTEMENT_MAX = 45.0;

    public SimulationService(TypeCreditRepository typeCreditRepository,
                             SimulationRepository simulationRepository,
                             DossierRepository dossierRepository) {
        this.typeCreditRepository = typeCreditRepository;
        this.simulationRepository = simulationRepository;
        this.dossierRepository = dossierRepository;
    }

    public SimulationParamsDto obtenirParametres() {
        List<TypeCreditDto> types = typeCreditRepository.findAllByActifTrue().stream()
                .map(TypeCreditDto::new)
                .collect(Collectors.toList());
        return new SimulationParamsDto(SEUIL_ENDETTEMENT_MAX, types);
    }

    public SimulationResponse calculer(SimulationRequest request) {
        TypeCredit tc = typeCreditRepository.findById(request.getTypeCreditId())
                .orElseThrow(() -> new ResourceNotFoundException("Type de crédit introuvable: " + request.getTypeCreditId()));

        if (!tc.isActif()) {
            throw new BusinessException("Ce type de crédit n'est pas actif pour de nouvelles simulations");
        }

        double taux = request.getTauxAnnuel() != null ? request.getTauxAnnuel() : tc.getTaux();
        double montant = request.getMontant();
        int dureeMois = request.getDureeMois();
        double revenu = request.getRevenuMensuel();
        double charges = request.getChargesMensuelles() != null ? request.getChargesMensuelles() : 0.0;

        // Calcul de la mensualité constante: M = P * [r / (1 - (1+r)^(-n))]
        double r = (taux / 100.0) / 12.0;
        double mensualiteBrute;
        if (r > 0) {
            mensualiteBrute = (montant * r) / (1 - Math.pow(1 + r, -dureeMois));
        } else {
            mensualiteBrute = montant / dureeMois;
        }

        double mensualite = round(mensualiteBrute, 2);
        double coutTotal = round(mensualite * dureeMois, 2);
        double coutInterets = round(coutTotal - montant, 2);

        // Taux d'endettement: ((mensualité + charges) / revenu) * 100
        double tauxEndettement = round(((mensualite + charges) / revenu) * 100.0, 2);

        // Capacité de remboursement estimée: (revenu * 45%) - charges
        double capaciteRemboursement = round((revenu * (SEUIL_ENDETTEMENT_MAX / 100.0)) - charges, 2);

        boolean eligible = true;
        String motif = "Dossier conforme aux critères d'octroi";

        if (montant < tc.getMontantMin() || montant > tc.getMontantMax()) {
            eligible = false;
            motif = "Le montant doit être compris entre " + tc.getMontantMin() + " et " + tc.getMontantMax() + " DH";
        } else if (dureeMois < tc.getDureeMin() || dureeMois > tc.getDureeMax()) {
            eligible = false;
            motif = "La durée doit être comprise entre " + tc.getDureeMin() + " et " + tc.getDureeMax() + " mois";
        } else if (tauxEndettement > SEUIL_ENDETTEMENT_MAX) {
            eligible = false;
            motif = "Taux d'endettement trop élevé (" + tauxEndettement + "% > " + SEUIL_ENDETTEMENT_MAX + "%)";
        } else if (mensualite > capaciteRemboursement) {
            eligible = false;
            motif = "Mensualité supérieure à la capacité de remboursement estimée";
        }

        Simulation sim = new Simulation();
        sim.setTypeCreditId(tc.getId());
        sim.setTypeCreditLibelle(tc.getLibelle());
        sim.setMontant(montant);
        sim.setDureeMois(dureeMois);
        sim.setTauxAnnuel(taux);
        sim.setRevenuMensuel(revenu);
        sim.setChargesMensuelles(charges);
        sim.setMensualite(mensualite);
        sim.setCoutTotal(coutTotal);
        sim.setCoutInterets(coutInterets);
        sim.setTauxEndettement(tauxEndettement);
        sim.setCapaciteRemboursement(capaciteRemboursement);
        sim.setEligible(eligible);
        sim.setMotif(motif);

        return new SimulationResponse(sim);
    }

    @Transactional(readOnly = true)
    public SimulationResponse obtenirParId(Long id) {
        Simulation sim = simulationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Simulation introuvable avec l'ID: " + id));
        return new SimulationResponse(sim);
    }

    @Transactional
    public SimulationResponse enregistrerPourDossier(String reference, SimulationRequest request) {
        Dossier dossier = dossierRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier introuvable avec la référence: " + reference));

        SimulationResponse resp = calculer(request);
        Simulation sim = new Simulation();
        sim.setDossier(dossier);
        sim.setTypeCreditId(request.getTypeCreditId());
        sim.setTypeCreditLibelle(dossier.getTypeCredit() != null ? dossier.getTypeCredit().getLibelle() : request.getTypeCreditId());
        sim.setMontant(resp.getMontant());
        sim.setDureeMois(resp.getDureeMois());
        sim.setTauxAnnuel(resp.getTauxAnnuel());
        sim.setRevenuMensuel(request.getRevenuMensuel());
        sim.setChargesMensuelles(request.getChargesMensuelles());
        sim.setMensualite(resp.getMensualite());
        sim.setCoutTotal(resp.getCoutTotal());
        sim.setCoutInterets(resp.getCoutInterets());
        sim.setTauxEndettement(resp.getTauxEndettement());
        sim.setCapaciteRemboursement(resp.getCapaciteRemboursement());
        sim.setEligible(resp.isEligible());
        sim.setMotif(resp.getMotif());

        sim = simulationRepository.save(sim);
        return new SimulationResponse(sim);
    }

    @Transactional(readOnly = true)
    public List<SimulationResponse> listerSimulationsDossier(String reference) {
        return simulationRepository.findByDossierReferenceOrderByDateSimulationDesc(reference).stream()
                .map(SimulationResponse::new)
                .collect(Collectors.toList());
    }

    private double round(double val, int places) {
        return BigDecimal.valueOf(val).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
