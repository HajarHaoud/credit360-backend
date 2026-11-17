package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.credit360.service.DashboardService;
import ma.credit360.web.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "9. Dashboard et Reporting", description = "KPIs, statistiques, répartition de portefeuille et exports de données")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/api/dashboard")
    @Operation(summary = "Vue d'ensemble complète du tableau de bord")
    public ResponseEntity<DashboardSummaryDto> vueEnsemble() {
        return ResponseEntity.ok(dashboardService.obtenirSynthese());
    }

    @GetMapping("/api/dashboard/stats")
    @Operation(summary = "Indicateurs principaux et compteurs globaux")
    public ResponseEntity<DashboardSummaryDto> stats() {
        return ResponseEntity.ok(dashboardService.obtenirSynthese());
    }

    @GetMapping("/api/dashboard/evolution")
    @Operation(summary = "Évolution temporelle des demandes et des octrois")
    public ResponseEntity<EvolutionDto> evolution() {
        return ResponseEntity.ok(dashboardService.obtenirEvolution());
    }

    @GetMapping("/api/dashboard/repartition-statuts")
    @Operation(summary = "Répartition des dossiers par statut")
    public ResponseEntity<List<RepartitionDto>> repartitionStatuts() {
        return ResponseEntity.ok(dashboardService.obtenirRepartitionStatuts());
    }

    @GetMapping("/api/dashboard/repartition-types")
    @Operation(summary = "Répartition des dossiers par produit de crédit")
    public ResponseEntity<List<RepartitionDto>> repartitionTypes() {
        return ResponseEntity.ok(dashboardService.obtenirRepartitionTypes());
    }

    @GetMapping("/api/dashboard/activite-recente")
    @Operation(summary = "Dernières activités et opérations réalisées")
    public ResponseEntity<List<ActiviteRecenteDto>> activiteRecente() {
        return ResponseEntity.ok(dashboardService.obtenirActiviteRecente());
    }

    @GetMapping("/api/rapports/dossiers/export")
    @Operation(summary = "Exporter les dossiers au format CSV")
    public ResponseEntity<byte[]> exporterDossiers() {
        byte[] csv = dashboardService.exporterDossiersCsv();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dossiers_export.csv\"")
                .body(csv);
    }

    @GetMapping("/api/rapports/activite/export")
    @Operation(summary = "Exporter le journal d'activité au format CSV")
    public ResponseEntity<byte[]> exporterActivite() {
        byte[] csv = dashboardService.exporterActivitesCsv();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"activite_export.csv\"")
                .body(csv);
    }

    @GetMapping("/api/rapports/statistiques/export")
    @Operation(summary = "Exporter le récapitulatif des statistiques au format CSV")
    public ResponseEntity<byte[]> exporterStatistiques() {
        byte[] csv = dashboardService.exporterStatistiquesCsv();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statistiques_export.csv\"")
                .body(csv);
    }
}
