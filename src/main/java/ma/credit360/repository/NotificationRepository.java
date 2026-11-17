package ma.credit360.repository;

import ma.credit360.entity.Notification;
import ma.credit360.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataireOrderByDateCreationDesc(Utilisateur destinataire);
    long countByDestinataireAndLueFalse(Utilisateur destinataire);
}
