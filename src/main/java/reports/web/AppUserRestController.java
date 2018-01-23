package reports.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reports.domain.AppUser;
import reports.service.AppUserService;

@RestController
@RequestMapping(value = "/api")
public class AppUserRestController {

	@Autowired
	private AppUserService appUserService;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<AppUser> appUsers() {
		return appUserService.appUsers();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public void userById(@PathVariable Long id) {
		appUserService.userById(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable Long id) { appUserService.deleteUser(id); }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public void createUser(@RequestBody AppUser appUser) {
		appUserService.createUser(appUser);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public void updateUser(@RequestBody AppUser appUser) {
		appUserService.updateUser(appUser);
	}

}
