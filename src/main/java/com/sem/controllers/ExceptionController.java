package com.sem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ExceptionController {

    @GetMapping("/error")
    public String error(Model model, HttpServletRequest request) {
        // Получаем исключение из атрибутов запроса
        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");

        if (exception == null) {
            exception = (Throwable) request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }

        // Получаем статус ошибки
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (exception != null) {
            model.addAttribute("exception", exception);
        } else if (statusCode != null) {
            String message = switch (statusCode) {
                case 404 -> "Страница не найдена";
                case 403 -> "Доступ запрещен";
                case 500 -> "Внутренняя ошибка сервера";
                default -> "Произошла ошибка";
            };
            model.addAttribute("exception", new Exception(message));
        } else {
            model.addAttribute("exception", new Exception("Неизвестная ошибка"));
        }

        return "error";
    }
}
