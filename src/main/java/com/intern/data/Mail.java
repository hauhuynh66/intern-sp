package com.intern.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {
    private String to;
    private String subject;
    private String content;

    protected Mail() {

    }

    public static class Builder {
        private final Mail mail;
        public Builder() {
            this.mail = new Mail();
        }

        public Builder to(String address) {
            mail.to = address;
            return this;
        }

        public Builder subject(String subject) {
            mail.subject = subject;
            return this;
        }

        public Builder content(String content) {
            mail.content = content;
            return this;
        }

        public Mail build() {
            return mail;
        }

    }

    @Override
    public String toString() {
        return "Mail{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
