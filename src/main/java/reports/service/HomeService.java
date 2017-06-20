package reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reports.domain.AppUser;
import reports.repository.AppUserRepository;

@Service
public class HomeService {

    @Autowired
    private AppUserRepository appUserRepository;

    public ResponseEntity<AppUser> createUser(AppUser appUser) {
        if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
            throw new RuntimeException("Taki username już istnieje");
        }
        else if (appUser.getPassword().length()<5) {
            throw new RuntimeException("Hasło musi mieć co najmniej 5 znaków");
        }
        appUser.setRole("USER");
        return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
    }


}
