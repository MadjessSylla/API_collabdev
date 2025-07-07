package odk.groupe4.ApiCollabDev.dao;

import odk.groupe4.ApiCollabDev.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDao extends JpaRepository<Notifications,Integer> {
}
