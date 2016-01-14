package uk.andrewgorton.HtmlValidatorWithSelenium;

import java.util.List;

public class ValidationResults {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
