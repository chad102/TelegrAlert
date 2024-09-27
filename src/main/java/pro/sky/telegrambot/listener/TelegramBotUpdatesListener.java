package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;
import pro.sky.telegrambot.service.TelegramBotClient;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.LocalDateTime.parse;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBotClient telegramBotClient;
    private final Pattern NOTIFICATION_TASK_PATTERN = Pattern.compile("(\\d{2}\\d{2}\\d{4} \\d{2}\\d{2})(\\s)([А-яA-z\\s\\d]+)");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private final NotificationTaskService notificationTaskService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBotClient telegramBotClient, NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.telegramBotClient = telegramBotClient;
        this.notificationTaskService = notificationTaskService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                // Process your updates here
                String text = update.message().text();
                Long chatId = update.message().chat().id();

                if ("/start".equals(text)) {
                    telegramBotClient.sendMessage(chatId, "Введите текст напоминания в формате:\\n*01.01.2025 15:00 Сделать домашку*");
                } else if (text != null) {
                    Matcher matcher = NOTIFICATION_TASK_PATTERN.matcher(text);
                    if (!matcher.find()) {
                        telegramBotClient.sendMessage(chatId, "Введенный текст не соответствует шаблону");
                    } else {
                        LocalDateTime notificationDateTime = parse(matcher.group(1));
                        String message = matcher.group(3);
                        if ((notificationDateTime = parse(matcher.group(1))) != null) {
                            notificationTaskService.save(message, chatId, notificationDateTime);
                        } else {
                            telegramBotClient.sendMessage(chatId, "Неверный формат даты/времени");
                        }
                    }
                } else {
                    telegramBotClient.sendMessage(chatId, "Я понимаю только текст");
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
@Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
