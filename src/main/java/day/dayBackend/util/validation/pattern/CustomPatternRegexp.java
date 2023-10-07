package day.dayBackend.util.validation.pattern;

import lombok.Getter;

@Getter
public enum CustomPatternRegexp {
    PASSWORD("(?=.*\\d)(?=.*\\w).*"),
//    PHONE_NUMBER("0(\\d){1,2}-(\\d){3,4}-(\\d){4}");
    EMAIL("^(?=.{1,256}$)(?=.{1,64}@.{1,255}$)[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*(\\.[a-zA-Z]{2,})$");

    private final String value;

    CustomPatternRegexp(String value) {
        this.value = value;
    }
}
