package de.iubh.fernstudium.ticketsystem.domain;

/**
 * Created by ivanj on 04.07.2017.
 */
public enum UserRole {

    AD("Administrator"),
    TU("Tutor"),
    ST("Student");

    UserRole(String resolvedRoleText) {
        this.resolvedRoleText = resolvedRoleText;
    }

    private String resolvedRoleText;

    public String getResolvedRoleText() {
        return resolvedRoleText;
    }

    public UserRole fromString(String shortText){
        return valueOf(shortText);
    }
}
