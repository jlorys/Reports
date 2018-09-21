package reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reports.domain.AppUser;
import reports.domain.Authority;
import reports.repository.AppUserRepository;
import reports.repository.AuthorityRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HomeService {

    private AppUserRepository appUserRepository;
    private AuthorityRepository authorityRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public HomeService(AppUserRepository appUserRepository, AuthorityRepository authorityRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.authorityRepository = authorityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<AppUser> createUser(AppUser appUser) {
        if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
            throw new RuntimeException("Taki username już istnieje");
        }
        else if (appUser.getPassword().length()<5) {
            throw new RuntimeException("Hasło musi mieć co najmniej 5 znaków");
        }

        Set<Authority> authorityList = new HashSet<>();
        Authority authority = authorityRepository.findOneByName(Authority.UserRoleName.ROLE_USER);
        authorityList.add(authority);
        appUser.setAuthorities(authorityList);

        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return new ResponseEntity<>(appUserRepository.save(appUser), HttpStatus.CREATED);
    }

}
