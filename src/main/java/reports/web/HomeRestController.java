package reports.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reports.domain.AppUser;
import reports.service.HomeService;

@RestController
public class HomeRestController {

	@Autowired
	private HomeService homeService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
		return homeService.createUser(appUser);
	}

	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

}
