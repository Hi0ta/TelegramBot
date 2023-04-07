package sky.pro.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sky.pro.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private final NotificationService notificationService;

    public TelegramBotUpdatesListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            SendMessage messageStart = new SendMessage(update.message().chat().id(),
                    "персональная напоминалка приветствует тебя! приступим? введи сообщение о том ЧТО напомнить / КОГДА это сделать в формате:  ДД.ММ.ГГГГ ЧЧ:ММ текст напоминалки");
            SendMessage messageOk = new SendMessage(update.message().chat().id(),
                    "записано - напомню! будь спокоен ;-)");
            SendMessage messageEx = new SendMessage(update.message().chat().id(),
                    "введенное сообщение не соответствует заданному формату, попробуй еще разок ;-)");

            if (update.message().text().equals("/start")) {
                telegramBot.execute(messageStart);
            } else if (update.message().text() == null) {
                telegramBot.execute(messageEx);
            } else if (update.message().text().matches("([0-9.:\s]{16})(\s)([А-Яа-яA-Za-z0-9\\p{P}\s]+)")) {
                String reminder = update.message().text();
                Integer chatId = Math.toIntExact(update.message().chat().id());

                notificationService.createNotificationTask(reminder, chatId);
                telegramBot.execute(messageOk);
            } else
                telegramBot.execute(messageEx);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
