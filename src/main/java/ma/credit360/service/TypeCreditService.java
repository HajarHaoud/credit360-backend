package ma.credit360.service;

import ma.credit360.entity.PieceRequise;
import ma.credit360.entity.TypeCredit;
import ma.credit360.repository.PieceRequiseRepository;
import ma.credit360.repository.TypeCreditRepository;
import ma.credit360.web.dto.*;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeCreditService {

    private final TypeCreditRepository typeCreditRepository;
    private final PieceRequiseRepository pieceRequiseRepository;
    private final AuditService auditService;

    public TypeCreditService(TypeCreditRepository typeCreditRepository,
                             PieceRequiseRepository pieceRequiseRepository,
                             AuditService auditService) {
        this.typeCreditRepository = typeCreditRepository;
        this.pieceRequiseRepository = pieceRequiseRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<TypeCreditDto> listerTous() {
        return typeCreditRepository.findAll().stream()
                .map(TypeCreditDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TypeCreditDto> listerActifs() {
        return typeCreditRepository.findAllByActifTrue().stream()
                .map(TypeCreditDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TypeCreditDto obtenirParId(String id) {
        TypeCredit tc = typeCreditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type de crédit introuvable: " + id));
        return new TypeCreditDto(tc);
    }

    @Transactional
    public TypeCreditDto creer(CreateTypeCreditRequest request) {
        if (typeCreditRepository.existsById(request.getId())) {
            throw new BusinessException("Un type de crédit avec l'identifiant '" + request.getId() + "' existe déjà");
        }
        if (request.getMontantMin() > request.getMontantMax()) {
            throw new BusinessException("Le montant minimum ne peut pas dépasser le montant maximum");
        }
        if (request.getDureeMin() > request.getDureeMax()) {
            throw new BusinessException("La durée minimale ne peut pas dépasser la durée maximale");
        }

        TypeCredit tc = new TypeCredit(
                request.getId(),
                request.getLibelle(),
                request.getFamille(),
                request.getTaux(),
                request.getMontantMin(),
                request.getMontantMax(),
                request.getDureeMin(),
                request.getDureeMax()
        );
        tc = typeCreditRepository.save(tc);

        auditService.tracer("CREATION_TYPE_CREDIT", "REFERENTIEL", tc.getId(), "Création du type de crédit: " + tc.getLibelle());
        return new TypeCreditDto(tc);
    }

    @Transactional
    public TypeCreditDto modifier(String id, UpdateTypeCreditRequest request) {
        TypeCredit tc = typeCreditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type de crédit introuvable: " + id));

        if (request.getMontantMin() > request.getMontantMax()) {
            throw new BusinessException("Le montant minimum ne peut pas dépasser le montant maximum");
        }
        if (request.getDureeMin() > request.getDureeMax()) {
            throw new BusinessException("La durée minimale ne peut pas dépasser la durée maximale");
        }

        tc.setLibelle(request.getLibelle());
        tc.setFamille(request.getFamille());
        tc.setTaux(request.getTaux());
        tc.setMontantMin(request.getMontantMin());
        tc.setMontantMax(request.getMontantMax());
        tc.setDureeMin(request.getDureeMin());
        tc.setDureeMax(request.getDureeMax());

        tc = typeCreditRepository.save(tc);
        auditService.tracer("MODIF_TYPE_CREDIT", "REFERENTIEL", tc.getId(), "Mise à jour du type de crédit: " + tc.getLibelle());
        return new TypeCreditDto(tc);
    }

    @Transactional
    public TypeCreditDto modifierStatut(String id, boolean actif) {
        TypeCredit tc = typeCreditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type de crédit introuvable: " + id));
        tc.setActif(actif);
        tc = typeCreditRepository.save(tc);

        auditService.tracer("MODIF_STATUT_TYPE_CREDIT", "REFERENTIEL", tc.getId(), "Statut: " + (actif ? "ACTIF" : "INACTIF"));
        return new TypeCreditDto(tc);
    }

    @Transactional(readOnly = true)
    public List<PieceRequiseDto> listerPiecesRequises(String typeId) {
        return pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(typeId).stream()
                .map(PieceRequiseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PieceRequiseDto ajouterPieceRequise(String typeId, CreatePieceRequiseRequest request) {
        TypeCredit tc = typeCreditRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type de crédit introuvable: " + typeId));

        PieceRequise pr = new PieceRequise(request.getLibelle(), request.isObligatoire(), request.getOrdre(), tc);
        pr = pieceRequiseRepository.save(pr);

        auditService.tracer("AJOUT_PIECE_REQUISE", "REFERENTIEL", typeId, "Ajout de la pièce: " + pr.getLibelle());
        return new PieceRequiseDto(pr);
    }

    @Transactional
    public PieceRequiseDto modifierPieceRequise(Long pieceId, UpdatePieceRequiseRequest request) {
        PieceRequise pr = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable: " + pieceId));

        pr.setLibelle(request.getLibelle());
        pr.setObligatoire(request.isObligatoire());
        pr.setOrdre(request.getOrdre());
        pr = pieceRequiseRepository.save(pr);

        auditService.tracer("MODIF_PIECE_REQUISE", "REFERENTIEL", String.valueOf(pieceId), "Modification de la pièce requise: " + pr.getLibelle());
        return new PieceRequiseDto(pr);
    }

    @Transactional
    public void supprimerPieceRequise(Long pieceId) {
        PieceRequise pr = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable: " + pieceId));

        auditService.tracer("SUPPR_PIECE_REQUISE", "REFERENTIEL", String.valueOf(pieceId), "Suppression de la pièce requise: " + pr.getLibelle());
        pieceRequiseRepository.delete(pr);
    }

    @Transactional
    public PieceRequiseDto modifierObligatoire(Long pieceId, boolean obligatoire) {
        PieceRequise pr = pieceRequiseRepository.findById(pieceId)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce requise introuvable: " + pieceId));

        pr.setObligatoire(obligatoire);
        pr = pieceRequiseRepository.save(pr);

        auditService.tracer("MODIF_OBLIGATOIRE_PIECE", "REFERENTIEL", String.valueOf(pieceId), "Caractère obligatoire: " + obligatoire);
        return new PieceRequiseDto(pr);
    }

    @Transactional
    public List<PieceRequiseDto> reordonnerPieces(String typeId, List<Long> piecesOrdre) {
        List<PieceRequise> pieces = pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(typeId);
        int ordre = 1;
        for (Long id : piecesOrdre) {
            for (PieceRequise p : pieces) {
                if (p.getId().equals(id)) {
                    p.setOrdre(ordre++);
                    pieceRequiseRepository.save(p);
                }
            }
        }
        return pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(typeId).stream()
                .map(PieceRequiseDto::new)
                .collect(Collectors.toList());
    }
}
