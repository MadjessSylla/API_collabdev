package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDao extends JpaRepository<Notification,Integer> {
}
