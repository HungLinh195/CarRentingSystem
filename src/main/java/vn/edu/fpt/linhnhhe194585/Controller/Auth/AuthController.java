package vn.edu.fpt.linhnhhe194585.Controller.Auth;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home(HttpSession session) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login";
        }
        if ("Admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/cars";
        }
        return "redirect:/customer/dashboard";
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        return accountService.login(email, password)
                .map(account -> {
                    session.setAttribute("currentUser", account);
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Email hoặc mật khẩu không chính xác.");
                    return "login";
                });
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("mobile") String mobile,
                           @RequestParam("birthday") String birthday,
                           @RequestParam("identityCard") String identityCard,
                           @RequestParam("licenceNumber") String licenceNumber,
                           @RequestParam("licenceDate") String licenceDate,
                           Model model) {
        try {
            accountService.register(email, password, name, fullName, mobile, birthday, identityCard, licenceNumber, licenceDate);
            model.addAttribute("success", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
