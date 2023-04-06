package sky.pro.telegrambot.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import sky.pro.telegrambot.model.NotificationTask;
import sky.pro.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public void createNotificationTask(String reminder, Integer chatId) {

        NotificationTask notificationTask = new NotificationTask(
                chatId,
                reminder.substring(17),
                parse(reminder));

        notificationRepository.save(notificationTask);
    }

    @Nullable
    public LocalDateTime parse(String reminder) {
        try{
            return LocalDateTime.parse(reminder.substring(0, 16), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        } catch (DateTimeParseException e){
            return null;  // в этом месте должно быть сообщение для пользователя о том что он неверно ввел дату и время

        }
    }
}
