package ru.otus.spring.testing.students.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.testing.students.config.LocalizationConfig;
import ru.otus.spring.testing.students.service.L10nMessageService;

import java.util.Locale;

import static ru.otus.spring.testing.students.config.MessageBundle.*;

@Service
@RequiredArgsConstructor
public class LocaleEvent implements Event {


    private final L10nMessageService l10nMessageService;
    private final LocalizationConfig localizationConfig;

    public String viewDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(l10nMessageService.getMessage(MESSAGE_GET_LOCALE)).append(NEXT_LINE);
        localizationConfig.getLocalMapping()
                .forEach((k, v) -> stringBuilder.append(l10nMessageService.getMessage(MESSAGE_GET_LOCALE_BTW, k, v)).append(NEXT_LINE));

        return stringBuilder.toString();
    }

    public String action(Object... args) {
        return setLocale(args);
    }

    private String setLocale(Object... args){
        if (args.length == 1) {
            String localeKey = (String) args[0];
            if (localizationConfig.getLocalMapping().containsKey(localeKey)) {
                Locale.setDefault(Locale.forLanguageTag(localizationConfig.getLocalMapping().get(localeKey)));
                return l10nMessageService.getMessage(MESSAGE_SUCCESS);
            } else {
                return l10nMessageService.getMessage(MESSAGE_NOT_CORRECT_PARAMETER, localeKey);
            }
        } else {
            return l10nMessageService.getMessage(MESSAGE_INVALID_PARAMETERS, "local");
        }
    }

}
