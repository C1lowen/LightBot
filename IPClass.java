package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPClass extends TelegramLongPollingBot {

    private static Pattern pattern = Pattern.compile("\\d{3}\\.\\d{3}.\\d{1,2}\\.\\d{3}");
    public IPClass() {
        super("6641089538:AAG_KJdGzma8iOl_u7OkWELhLaQfS-X3-g4");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().getText().equals("/start")){
            long chatId = update.getMessage().getChatId();
            sendTextMessage(chatId, "Привіт, я бот, який допоможе тобі визначити, чи є в тебе світло чи ні!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendTextMessage(chatId, "Напиши мені, будь ласка, свою IP-адресу (наприклад: 192.168.0.000) ");
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (isValidIpAddress(messageText)) {
                boolean isLightOn = checkLightStatus(messageText);
                String response = isLightOn ? "✅ Світло є" : "❌ Світла нема";

                sendTextMessage(chatId, response);
            } else {
                sendTextMessage(chatId, "Будь ласка, введіть коректну IP-адресу.");
            }
        }
    }

    private boolean checkLightStatus(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return address.isReachable(5000);
        } catch (IOException e) {
            System.err.println("Помилка при перевірці доступності пристрою: " + e.getMessage());
            return false;
        }
    }

    private void sendTextMessage(long chatId, String response) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(response);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidIpAddress(String messageText) {
        Matcher matcher = pattern.matcher(messageText);
        if(matcher.find()){
            return true;
        } else { return false; }

    }


    @Override
    public String getBotUsername() {
        return "Birthday_SophiaBot";
    }


}
