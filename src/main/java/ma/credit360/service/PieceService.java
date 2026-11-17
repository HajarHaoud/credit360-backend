package ma.credit360.service;

import ma.credit360.entity.*;
import ma.credit360.entity.enums.Role;
import ma.credit360.entity.enums.StatutDossier;
import ma.credit360.entity.enums.TypeNotification;
import ma.credit360.repository.*;
import ma.credit360.web.dto.ChecklistDto;
import ma.credit360.web.dto.FichierPieceDto;
import ma.credit360.web.dto.PieceChecklistItemDto;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PieceService {

    private final DossierRepository dossierRepository;
    private final PieceRequiseRepository pieceRequiseRepository;
    private final PieceDeposeeRepository pieceDeposeeRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DossierService dossierService;
    private final AuthService authService;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final Path uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 Mo

    public PieceService(DossierRepository dossierRepository,
                        PieceRequiseRepository pieceRequiseRepository,
                        PieceDeposeeRepository pieceDeposeeRepository,
                        HistoriqueStatutRepository historiqueStatutRepository,
                        UtilisateurRepository utilisateurRepository,
                        DossierService dossierService,
                        AuthService authService,
                        AuditService auditService,
                        NotificationService notificationService,
                        @Value("${credit360.storage.upload-dir:./uploads}") String uploadDirPath) {
        this.dossierRepository = dossierRepository;
        this.pieceRequiseRepository = pieceRequiseRepository;
        this.pieceDeposeeRepository = pieceDeposeeRepository;
        this.historiqueStatutRepository = historiqueStatutRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.dossierService = dossierService;
        this.authService = authService;
        this.auditService = auditService;
        this.notificationService = notificationService;
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'initialiser le répertoire de stockage", e);
        }
    }

    @Transactional(readOnly = true)
    public ChecklistDto obtenirChecklist(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        List<PieceRequise> piecesRequises = pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(dossier.getTypeCredit().getId());
        List<PieceDeposee> piecesDeposees = pieceDeposeeRepository.findByDossier(dossier);

        Map<Long, PieceDeposee> mapDeposees = piecesDeposees.stream()
                .collect(Collectors.toMap(p -> p.getPieceRequise().getId(), p -> p, (p1, p2) -> p1));

        List<PieceChecklistItemDto> items = new ArrayList<>();
        int nbDeposees = 0;
        int nbObligatoiresManquantes = 0;

        for (PieceRequise pr : piecesRequises) {
            PieceDeposee pd = mapDeposees.get(pr.getId());
            FichierPieceDto fichierDto = (pd != null) ? new FichierPieceDto(pd) : null;
            if (pd != null) {
                nbDeposees++;
            } else if (pr.isObligatoire()) {
                nbObligatoiresManquantes++;
            }
            items.add(new PieceChecklistItemDto(pr.getId(), pr.getLibelle(), pr.isObligatoire(), pr.getOrdre(), fichierDto));
        }

        boolean modifiable = (dossier.getStatut() == StatutDossier.A_COMPLETER || dossier.getStatut() == StatutDossier.COMPLEMENT_REQUIS);
        boolean complete = (nbObligatoiresManquantes == 0);

        return new ChecklistDto(
                piecesRequises.size(),
                nbDeposees,
                piecesRequises.size() - nbDeposees,
                complete,
                modifiable,
                items
        );
    }

    @Transactional
    public FichierPieceDto deposerPiece(String reference, Long pieceId, MultipartFile file) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        if (dossier.getStatut() != StatutDossier.A_COMPLETER && dossier.getStatut() != StatutDossier.COMPLEMENT_REQUIS) {
            throw new BusinessException("Le dossier n'est plus modifiable dans son statut actuel (" + dossier.getStatut().getLibelle() + ")");
        }

        PieceRequise pieceRequise = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable avec l'ID: " + pieceId));

        if (!pieceRequise.getTypeCredit().getId().equals(dossier.getTypeCredit().getId())) {
            throw new BusinessException("Cette pièce ne fait pas partie du référentiel du crédit sélectionné");
        }

        if (file.isEmpty()) {
            throw new BusinessException("Le fichier envoyé est vide");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("La taille du fichier dépasse la limite maximale autorisée de 5 Mo");
        }

        // Valider le format réel du fichier par analyse des octets magiques
        String detectedContentType = validerEtDetecterTypeFichier(file);

        try {
            Path dossierDir = this.uploadDir.resolve(dossier.getReference());
            Files.createDirectories(dossierDir);

            String originalName = (file.getOriginalFilename() != null) ? file.getOriginalFilename() : "piece_" + pieceId;
            String sanitizedName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String storageFilename = pieceId + "_" + System.currentTimeMillis() + "_" + sanitizedName;
            Path targetLocation = dossierDir.resolve(storageFilename);

            // Supprimer l'ancien fichier s'il existait déjà
            Optional<PieceDeposee> existante = pieceDeposeeRepository.findByDossierAndPieceRequise(dossier, pieceRequise);
            if (existante.isPresent()) {
                PieceDeposee old = existante.get();
                try {
                    Files.deleteIfExists(Paths.get(old.getCheminFichier()));
                } catch (Exception ignored) {}
                pieceDeposeeRepository.delete(old);
                pieceDeposeeRepository.flush();
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            PieceDeposee pieceDeposee = new PieceDeposee(
                    dossier,
                    pieceRequise,
                    originalName,
                    file.getSize(),
                    detectedContentType,
                    targetLocation.toString()
            );

            pieceDeposee = pieceDeposeeRepository.save(pieceDeposee);

            auditService.tracer("DEPOT_PIECE", "PIECE", dossier.getReference(), "Dépôt de la pièce: " + pieceRequise.getLibelle() + " (" + originalName + ")");
            return new FichierPieceDto(pieceDeposee);

        } catch (IOException ex) {
            throw new BusinessException("Erreur lors de l'enregistrement physique du fichier: " + ex.getMessage());
        }
    }

    @Transactional
    public void supprimerPiece(String reference, Long pieceId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        if (dossier.getStatut() != StatutDossier.A_COMPLETER && dossier.getStatut() != StatutDossier.COMPLEMENT_REQUIS) {
            throw new BusinessException("Le dossier n'est plus modifiable dans son statut actuel (" + dossier.getStatut().getLibelle() + ")");
        }

        PieceRequise pieceRequise = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable avec l'ID: " + pieceId));

        PieceDeposee pieceDeposee = pieceDeposeeRepository.findByDossierAndPieceRequise(dossier, pieceRequise)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun fichier déposé pour cette pièce requise"));

        try {
            Files.deleteIfExists(Paths.get(pieceDeposee.getCheminFichier()));
        } catch (Exception ignored) {}

        pieceDeposeeRepository.delete(pieceDeposee);
        auditService.tracer("SUPPRESSION_PIECE", "PIECE", dossier.getReference(), "Suppression de la pièce: " + pieceRequise.getLibelle());
    }

    @Transactional(readOnly = true)
    public Resource telechargerFichier(String reference, Long pieceId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        PieceRequise pieceRequise = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable: " + pieceId));

        PieceDeposee pieceDeposee = pieceDeposeeRepository.findByDossierAndPieceRequise(dossier, pieceRequise)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun fichier n'a été déposé pour cette pièce"));

        try {
            Path filePath = Paths.get(pieceDeposee.getCheminFichier());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Le fichier physique est introuvable sur le disque");
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Chemin de fichier invalide");
        }
    }

    @Transactional(readOnly = true)
    public PieceDeposee obtenirPieceDeposee(String reference, Long pieceId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        PieceRequise pieceRequise = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable"));
        return pieceDeposeeRepository.findByDossierAndPieceRequise(dossier, pieceRequise)
                .orElseThrow(() -> new ResourceNotFoundException("Aucune pièce déposée"));
    }

    @Transactional
    public void soumettreDossier(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        if (dossier.getStatut() != StatutDossier.A_COMPLETER) {
            throw new BusinessException("Seul un dossier à l'état 'À compléter' peut être soumis à l'analyse (statut actuel: " + dossier.getStatut().getLibelle() + ")");
        }

        // Vérifier que toutes les pièces obligatoires sont présentes
        List<PieceRequise> obligatoires = pieceRequiseRepository.findByTypeCreditIdAndObligatoireTrue(dossier.getTypeCredit().getId());
        List<PieceDeposee> deposees = pieceDeposeeRepository.findByDossier(dossier);
        Set<Long> idsDeposees = deposees.stream().map(p -> p.getPieceRequise().getId()).collect(Collectors.toSet());

        List<String> manquantes = new ArrayList<>();
        for (PieceRequise pr : obligatoires) {
            if (!idsDeposees.contains(pr.getId())) {
                manquantes.add(pr.getLibelle());
            }
        }

        if (!manquantes.isEmpty()) {
            throw new BusinessException("Impossible de soumettre le dossier : pièces obligatoires manquantes : " + String.join(", ", manquantes));
        }

        StatutDossier ancienStatut = dossier.getStatut();
        dossier.setStatut(StatutDossier.EN_ANALYSE);
        dossierRepository.save(dossier);

        Utilisateur currentUser = authService.getCurrentUser();
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancienStatut,
                StatutDossier.EN_ANALYSE,
                "SOUMISSION",
                currentUser,
                "Dossier complet transmis pour analyse financière"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("SOUMISSION_DOSSIER", "WORKFLOW", dossier.getReference(), "Dossier soumis à l'analyse par " + currentUser.getNom());

        // Notifier les analystes
        List<Utilisateur> analystes = utilisateurRepository.findByRole(Role.ANALYSTE);
        for (Utilisateur analyste : analystes) {
            notificationService.envoyerNotification(
                    analyste,
                    "Nouveau dossier à traiter",
                    "Le dossier " + dossier.getReference() + " (" + dossier.getNom() + " " + dossier.getPrenom() + ") est prêt pour analyse.",
                    TypeNotification.DOSSIER_SOUMIS,
                    dossier.getReference()
            );
        }
    }

    private String validerEtDetecterTypeFichier(MultipartFile file) {
        byte[] magic = new byte[8];
        try (InputStream is = file.getInputStream()) {
            int read = is.read(magic);
            if (read < 4) {
                throw new BusinessException("Fichier non reconnu ou corrompu");
            }
        } catch (IOException e) {
            throw new BusinessException("Erreur de lecture du fichier");
        }

        // PDF: %PDF -> 0x25 0x50 0x44 0x46
        if (magic[0] == 0x25 && magic[1] == 0x50 && magic[2] == 0x44 && magic[3] == 0x46) {
            return "application/pdf";
        }
        // JPEG: FF D8 FF
        if ((magic[0] & 0xFF) == 0xFF && (magic[1] & 0xFF) == 0xD8 && (magic[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }
        // PNG: 89 50 4E 47
        if ((magic[0] & 0xFF) == 0x89 && magic[1] == 0x50 && magic[2] == 0x4E && magic[3] == 0x47) {
            return "image/png";
        }

        throw new BusinessException("Format de fichier non supporté. Seuls les formats PDF, JPG et PNG sont acceptés.");
    }
}
