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

    public static UserRole fromString(String text){
        for(UserRole u : UserRole.values()){
            if(u.resolvedRoleText.equalsIgnoreCase(text)){
                return u;
            }
        }
        return null;
    }
}
