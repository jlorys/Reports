package reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reports.domain.AppUser;
import reports.repository.AppUserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppUserService {

    private AppUserRepository appUserRepository;
    private SessionRegistry sessionRegistry;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, SessionRegistry sessionRegistry, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.sessionRegistry = sessionRegistry;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<AppUser> appUsers(){
        return appUserRepository.findAll();
    }

    public Set<AppUser> loggedInUsers() {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        Set<AppUser> allAppUsers = new HashSet<>();
        allAppUsers.clear();

        for(final Object principal : allPrincipals) {
            if(principal instanceof AppUser) {
                AppUser user = (AppUser) principal;

                List<SessionInformation> activeUserSessions =
                        sessionRegistry.getAllSessions(principal,
                                /* includeExpiredSessions */ false); // Should not return null;

                if (!activeUserSessions.isEmpty()) {
                    System.out.println(user);
                    allAppUsers.add(user);}
            }
        }
        return allAppUsers;
    }

    public ResponseEntity<AppUser> userById(Long id) {
        AppUser appUser = appUserRepository.findOne(id);
        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }
    }

    public ResponseEntity<AppUser> deleteUser(Long id) {
        AppUser appUser = appUserRepository.findOne(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (appUser == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (appUser.getUsername().equalsIgnoreCase(loggedUsername)) {
            throw new RuntimeException("Nie możesz usunąć siebie samego!");
        } else {
            appUserRepository.delete(appUser);
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }
    }

    public ResponseEntity<AppUser> createUser(AppUser appUser) {
        if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
            throw new RuntimeException("Taki username już istnieje");
        }
        else if (appUser.getPassword().length()<5) {
            throw new RuntimeException("Hasło musi mieć co najmniej 5 znaków");
        }
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return new ResponseEntity<>(appUserRepository.save(appUser), HttpStatus.CREATED);
    }

    public AppUser updateUser(AppUser appUser) {
        if (appUserRepository.findOneByUsername(appUser.getUsername()) != null
                && appUserRepository.findOneByUsername(appUser.getUsername()).getId() != appUser.getId()) {
            throw new RuntimeException("Taki username już istnieje");
        }
        else if (appUser.getPassword().length()<5) {
            throw new RuntimeException("Hasło musi mieć co najmniej 5 znaków");
        }
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }
}
