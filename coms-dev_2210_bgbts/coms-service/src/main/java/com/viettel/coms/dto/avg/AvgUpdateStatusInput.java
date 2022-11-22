package com.viettel.coms.dto.avg;

public class AvgUpdateStatusInput {
    String token;
    String reference_number;
    String life_cycle_state;
    String note;
    String installation_date;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getLife_cycle_state() {
        return life_cycle_state;
    }

    public void setLife_cycle_state(String life_cycle_state) {
        this.life_cycle_state = life_cycle_state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInstallation_date() {
        return installation_date;
    }

    public void setInstallation_date(String installation_date) {
        this.installation_date = installation_date;
    }
}
