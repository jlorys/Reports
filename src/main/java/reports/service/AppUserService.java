package reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reports.domain.AppUser;
import reports.repository.AppUserRepository;

import java.util.List;

@Service
public class AppUserService {

    @Autowired
    AppUserRepository appUserRepository;

    public List<AppUser> appUsers(){
        return appUserRepository.findAll();
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
        return appUserRepository.save(appUser);
    }
}
