package ma.credit360.service;

import ma.credit360.entity.Notification;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.TypeNotification;
import ma.credit360.repository.NotificationRepository;
import ma.credit360.web.dto.CountDto;
import ma.credit360.web.dto.NotificationDto;
import ma.credit360.web.exception.ResourceNotFoundException;
import ma.credit360.web.exception.UnauthorizedOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public NotificationService(NotificationRepository notificationRepository, AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> listerMesNotifications() {
        Utilisateur currentUser = authService.getCurrentUser();
        return notificationRepository.findByDestinataireOrderByDateCreationDesc(currentUser).stream()
                .map(NotificationDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CountDto compterNonLues() {
        Utilisateur currentUser = authService.getCurrentUser();
        return new CountDto(notificationRepository.countByDestinataireAndLueFalse(currentUser));
    }

    @Transactional
    public void marquerCommeLue(Long id) {
        Utilisateur currentUser = authService.getCurrentUser();
        Notification notif = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable: " + id));

        if (!notif.getDestinataire().getId().equals(currentUser.getId())) {
            throw new UnauthorizedOperationException("Cette notification ne vous est pas destinée");
        }

        notif.setLue(true);
        notificationRepository.save(notif);
    }

    @Transactional
    public void marquerToutCommeLu() {
        Utilisateur currentUser = authService.getCurrentUser();
        List<Notification> notifs = notificationRepository.findByDestinataireOrderByDateCreationDesc(currentUser);
        for (Notification n : notifs) {
            if (!n.isLue()) {
                n.setLue(true);
                notificationRepository.save(n);
            }
        }
    }

    @Transactional
    public void supprimer(Long id) {
        Utilisateur currentUser = authService.getCurrentUser();
        Notification notif = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable: " + id));

        if (!notif.getDestinataire().getId().equals(currentUser.getId())) {
            throw new UnauthorizedOperationException("Cette notification ne vous est pas destinée");
        }

        notificationRepository.delete(notif);
    }

    @Transactional
    public void envoyerNotification(Utilisateur destinataire, String titre, String message, TypeNotification type, String referenceDossier) {
        if (destinataire == null) return;
        Notification notif = new Notification(destinataire, titre, message, type, referenceDossier);
        notificationRepository.save(notif);
    }
}
