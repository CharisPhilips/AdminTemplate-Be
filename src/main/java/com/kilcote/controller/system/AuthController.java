package com.kilcote.controller.system;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kilcote.common.Global;
import com.kilcote.common.data.ConfirmationToken;
import com.kilcote.common.exceptions.RestException;
import com.kilcote.controller._base.BaseController;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.controller._base.jwt.CurrentUser;
import com.kilcote.entity.system.LoginLog;
import com.kilcote.entity.system.User;
import com.kilcote.service.system.AuthService;
import com.kilcote.service.system.EmailService;
import com.kilcote.service.system.LoginLogService;
import com.kilcote.utils.HttpUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@RestController
@RequestMapping({"/api/oauth"})
@RequiredArgsConstructor
//@Transactional
public class AuthController extends BaseController {

	@Autowired
	private AuthService service;
	//	@Autowired
	//	private UserService userService;
	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private EmailService emailService;
	//	@Secured("ROLE_ADMIN")
	//	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	//	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	//	@RolesAllowed("ROLE_ADMIN")
	//	@PreAuthorize("isAnonymous()")
	//	@PreAuthorize("hasRole('USER')")
	//	@CurrentUser User currentUser,

	@PostMapping("/token")
	public ResponseEntity<User> login(@RequestBody User user, @CurrentUser User currentUser) throws Exception {
		String email = user.getEmail();
		String password = user.getPassword();
		User userLogin = null;
		if (email != null) {
			userLogin = service.login(email, password, authenticationManager, request.getHeader("user-agent"));
			if (userLogin == null) {
				throw new RestException("error.auth.login.failed", null);
			}
			String currentUsername = HttpUtil.getCurrentUsername();
			// save login log
			LoginLog loginLog = new LoginLog();
			loginLog.setEmail(currentUsername);
			loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
			this.loginLogService.addLoginLog(loginLog);
		} else {
			userLogin = currentUser;
		}
		return new ResponseEntity<User>(userLogin, HttpStatus.OK);
	}

	@PostMapping("/signup")
	@ControllerEndpoint(operation="Signup", exceptionMessage="Signup failed")
	public ResponseEntity<Boolean> signup(@RequestBody User user) throws Exception {
		if (!this.service.signup(user)) {
			throw new Exception("Signup is failed");
		}
		ConfirmationToken confirmationToken = new ConfirmationToken(user);
		Global.g_signupConfirmToken.put(confirmationToken.getConfirmationToken(), confirmationToken);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Complete Registration!");
		mailMessage.setFrom(Global.g_mailForVerification);
		mailMessage.setText(String.format("To confirm your account, please click here : %s/#/%s/%s", Global.FRONTEND_DOMAIN_URL, "confirm-signup", confirmationToken.getConfirmationToken()));
		emailService.sendEmail(mailMessage);
		return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
	}

	@GetMapping("/confirm-signup")
	@ControllerEndpoint(operation="confirmSignup", exceptionMessage="Confirm account failed")
	public ResponseEntity<Boolean> confirmSignup(@RequestParam Map<String, String> params) throws Exception {
		String confirmationToken = params.get("confirmationToken");
		ConfirmationToken token = Global.g_signupConfirmToken.get(confirmationToken);
		if (token != null && token.getUser() != null) {
			boolean result = this.service.setEnableUser(token.getUser().getEmail());
			Global.g_signupConfirmToken.remove(confirmationToken);
			return new ResponseEntity<Boolean>(result, HttpStatus.OK);
		}
		else {
			throw new RestException("error.auth.confirm.failed", null);
		}
	}

	@PostMapping("/reset")
	@ControllerEndpoint(operation="Reset", exceptionMessage="Reset failed")
	public ResponseEntity<Boolean> reset(@RequestBody User user) throws Exception {
		ConfirmationToken confirmationToken = new ConfirmationToken(user);
		Global.g_resetConfirmToken.put(confirmationToken.getConfirmationToken(), confirmationToken);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Reset request accepted!");
		mailMessage.setFrom(Global.g_mailForVerification);
		mailMessage.setText(String.format("To reset your account, please click here : %s/#/%s/%s", 
				Global.FRONTEND_DOMAIN_URL, "confirm-reset", confirmationToken.getConfirmationToken()));
		emailService.sendEmail(mailMessage);
		return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
	}

	@GetMapping("/confirm-reset")
	@ControllerEndpoint(operation="confirmReset", exceptionMessage="Reset account failed")
	public ResponseEntity<Boolean> confirmReset(@RequestParam Map<String, String> params ) throws Exception {
		String confirmationToken = params.get("confirmationToken");
		ConfirmationToken token = Global.g_resetConfirmToken.get(confirmationToken);
		if (token != null && token.getUser() != null) {
			User user = token.getUser();
			String password = params.get("password");
			if (password == null || password.trim().length() <= 0) {
				throw new RestException("error.auth.reset.password.empty", null);
			}
			user.setPassword(password);
			boolean result = this.service.resetPassword(user, authenticationManager);
			Global.g_resetConfirmToken.remove(confirmationToken);
			return new ResponseEntity<Boolean>(result, HttpStatus.OK);
		}
		else {
			throw new RestException("error.auth.confirm.failed", null);
		}
	}
	
	@PostMapping("/changepwd")
	@ControllerEndpoint(operation="Change password", exceptionMessage="Change password failed")
	public ResponseEntity<User> changepwd(@RequestBody User user) throws Exception {
		User userNew = service.changePassword(user, authenticationManager);
		if (userNew == null) {
			throw new Exception("Change Password is failed");
		}
		return new ResponseEntity<User>(userNew, HttpStatus.CREATED);
	}
}