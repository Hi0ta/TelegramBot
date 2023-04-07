package sky.pro.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface NotificationRepository extends JpaRepository<NotificationTask, Long> {
    Collection<NotificationTask> findNotificationTaskByDateTimeEquals(LocalDateTime dateTime);
}