package it.ddbdev.ticketsystem.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String mailFrom;

    private String mailTo;

    private String mailSubject;

    private String mailContent;

    private String mailMimeType;

}
