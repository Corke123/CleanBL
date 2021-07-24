package org.unibl.etf.ps.cleanbl.fixtures;

import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;

import java.time.LocalDate;

public class ContactUsMessageFixture {
    public static ContactUsMessage createContactUsMessage() {
        return new ContactUsMessage(1L,
                "Registracija na sistem",
                "Ne mogu se registrovati",
                "marko123@gmail.com",
                true,
                "Nalog sa datim email-om vec postoji",
                LocalDate.now());
    }

   public static ContactUsMessage createContactUsMessageOne() {
        return new ContactUsMessage(1L,
                "Registracija na sistem",
                "Ne mogu se registrovati",
                "petar123@gmail.com",
                true,
                "Nalog sa datim email-om vec postoji",
                LocalDate.now());
   }
}
