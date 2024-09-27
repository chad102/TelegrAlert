package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class TelegramBotClient {
    private Logger logger = LoggerFactory.getLogger(TelegramBotClient.class);
    private final TelegramBot telegramBot;

    public TelegramBotClient(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage (Long chatId, String text) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, text));
        if (!sendResponse.isOk()) {
            logger.error("Message was not sent");
        }
    }
}
