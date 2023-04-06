package sky.pro.telegrambot.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sky.pro.telegrambot.model.NotificationTask;
import sky.pro.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;


@Component
public class NotificationTimer {

    private final NotificationRepository notificationRepository;
    private final TelegramBot telegramBot;

    public NotificationTimer(NotificationRepository notificationRepository, TelegramBot telegramBot) {
        this.notificationRepository = notificationRepository;
        this.telegramBot = telegramBot;
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        if (!notificationRepository.findNotificationTaskByDateTimeEquals(dateTime).isEmpty()) {// условие что напоминание в БД найдено

            Collection<NotificationTask> notifications = notificationRepository.findNotificationTaskByDateTimeEquals(dateTime);

            notifications.forEach(notificationTask -> {

                SendMessage messageTask = new SendMessage(notificationTask.getChatId(), notificationTask.getNotification());
                telegramBot.execute(messageTask);

                notificationRepository.deleteById(notificationTask.getId());
            });
        }
    }
}
