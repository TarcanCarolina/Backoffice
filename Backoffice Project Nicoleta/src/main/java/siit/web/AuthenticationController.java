package siit.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class AuthenticationController {

	@GetMapping("/login")
	public String displayLogin() {
		return "login";
	}

	@PostMapping("/login")
	public ModelAndView doLogin(HttpSession session,
			@RequestParam String user, @RequestParam String password) {

		ModelAndView mav = new ModelAndView();

		if (user.equals(password)) {
			session.setAttribute("logged_user", user);
			mav.setViewName("redirect:/customers");
		} else {
			mav.addObject("error", "User and password do not match!");
			mav.setViewName("login");
		}

		return mav;
	}

	@GetMapping("/logout")
	public String doLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

}
