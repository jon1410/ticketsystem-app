package de.iubh.fernstudium.ticketsystem.services.mockups;

import de.iubh.fernstudium.ticketsystem.domain.exception.InvalidPasswordException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.PasswordUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanj on 04.07.2017.
 *
 * Dieser Mock ist ApplicationScoped,
 * damit die PostConstructMethode nicht ständig aufgerufen wird (simuliert einen Datenbestand).
 * Das tatsächliche Bean wird dann RequestScope.
 */
@ApplicationScoped
@Alternative
public class UserServiceMockup implements UserService {
	
	private String password;
	private String userId;
	
	private UserDTO user;
	

    @Inject
    private PasswordUtil passwordUtil;

    private Map<String, UserDTO> users;

    @PostConstruct
    private void initUser() {
        if (users == null) {
            users = new HashMap<>();
        }
        users.put("admin", createUserDTO("admin", "admin", "admin", passwordUtil.hashPw("admin"), UserRole.AD));
    }

    @Override
    public UserDTO getUserByUserId(String userId) throws UserNotExistsException {
        return users.get(userId);
    }

    @Override
    public boolean createUser(String userId, String firstName, String lastName, String password, UserRole role) throws UserAlreadyExistsException {
        if(users.containsKey(userId)){
            throw new UserAlreadyExistsException(String.format("User mit UserID: %s existiert bereits", userId));
        }
        String hashedPw = passwordUtil.hashPw(password);
        users.put(userId, createUserDTO(userId, firstName, lastName, hashedPw, role));
        return true;
    }

    @Override
    public boolean login(String userId, String password) throws UserNotExistsException, InvalidPasswordException {
        if(!users.containsKey(userId)){
            throw new UserNotExistsException(String.format("User mit UserID: %s existiert nicht", userId));
        }

        user = users.get(userId);
		
		
		boolean userCheck = passwordUtil.authentificate(password, user.getPassword());
		
		if(user)
		{
			return "main.xhtml?faces-redirect=true";
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARNING, "Anmeldefehler", "EMail/-Passwortkombination wurde nicht gefunden"));
			return null;
			
		}
    }

    @Override
    public boolean changePassword(String userId, String altesPw, String neuesPw) throws UserNotExistsException, InvalidPasswordException {

        if(!users.containsKey(userId)){
            throw new UserNotExistsException(String.format("User mit UserID: %s existiert nicht", userId));
        }
        UserDTO user = users.get(userId);

        if(passwordUtil.authentificate(altesPw, user.getPassword())){
            String hashedPwNew = passwordUtil.hashPw(neuesPw);
            user.setPassword(hashedPwNew);
            users.put(userId, user);
            return true;
        }

        return false;
    }

    @Override
    public boolean userIdExists(String userId) {
        UserDTO user = users.get(userId);
        return user != null;
    }

    private UserDTO createUserDTO(String admin, String firstName, String lastName, String passwort, UserRole role) {
        return new UserDTO(admin, firstName, lastName, passwort, role);
    }
	
	
	
	/**Getter and Setter Methoden*/
	
	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
