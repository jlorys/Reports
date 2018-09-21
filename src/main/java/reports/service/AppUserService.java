package reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reports.domain.AppUser;
import reports.domain.Authority;
import reports.repository.AppUserRepository;
import reports.repository.AuthorityRepository;
import java.util.*;

@Service
public class AppUserService {

    private AppUserRepository appUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthorityRepository authorityRepository;
    private AuthenticationService authenticationService;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthorityRepository authorityRepository,
                          AuthenticationService authenticationService) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authorityRepository = authorityRepository;
        this.authenticationService = authenticationService;
    }

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

        int authorities = appUser.getAuthorities().size();
        HashSet<Authority> set = new HashSet();
        appUser.setAuthorities(Collections.emptySet());
        appUserRepository.save(appUser);
        if(authorities==1){
            set.add(authorityRepository.findOneByName(Authority.UserRoleName.ROLE_USER));
            appUser.setAuthorities(set);
        }else{
            set.add(authorityRepository.findOneByName(Authority.UserRoleName.ROLE_USER));
            set.add(authorityRepository.findOneByName(Authority.UserRoleName.ROLE_ADMIN));
            appUser.setAuthorities(set);
        }
        return appUserRepository.save(appUser);
    }

}
