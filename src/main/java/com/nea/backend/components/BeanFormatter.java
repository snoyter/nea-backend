package com.nea.backend.components;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class BeanFormatter {
    private final String template;
    private final Matcher matcher;
    private static final Pattern pattern = Pattern.compile("\\{(.+?)\\}");

    public BeanFormatter(String template) {
        this.template = template;
        this.matcher = pattern.matcher(template);
    }

    public String format(Object bean) {
        var buffer = new StringBuilder();

        matcher.reset();
        while (matcher.find()) {
            String token = matcher.group(1);
            try {
                String value = getProperty(bean, token);
                matcher.appendReplacement(buffer, value);
            } catch (Exception e) {
                log.error("Не найдено поле \"" + token + "\" в " + bean.getClass().getName() + " для шаблона \""
                        + template + "\"");
                matcher.appendReplacement(buffer, "{" + token + "}");
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private String getProperty(Object bean, String token) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = bean.getClass().getDeclaredField(token);
        return String.valueOf(field.get(bean));
    }
}
