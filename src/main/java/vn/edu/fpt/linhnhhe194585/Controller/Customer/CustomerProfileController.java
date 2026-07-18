package vn.edu.fpt.linhnhhe194585.Controller.Customer;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import vn.edu.fpt.linhnhhe194585.Service.CustomerService;
import vn.edu.fpt.linhnhhe194585.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/customer")
public class CustomerProfileController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    private boolean checkAuth(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Customer".equalsIgnoreCase(user.getRole())) {
            return false;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("customer", user.getCustomer());
        return true;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";
        return "customer/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("mobile") String mobile,
                                @RequestParam("birthday") String birthdayStr,
                                @RequestParam("identityCard") String identityCard,
                                @RequestParam("licenceNumber") String licenceNumber,
                                @RequestParam("licenceDate") String licenceDateStr,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Customer".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            Customer customer = user.getCustomer();
            customer.setFullName(fullName);
            customer.setMobile(mobile);
            customer.setBirthday(LocalDate.parse(birthdayStr));
            customer.setIdentityCard(identityCard);
            customer.setLicenceNumber(licenceNumber);
            customer.setLicenceDate(LocalDate.parse(licenceDateStr));

            customerService.saveCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmNewPassword") String confirmNewPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Customer".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu mới không khớp!");
            return "redirect:/customer/profile";
        }

        try {
            accountService.changePassword(user.getId(), currentPassword, newPassword);
            user.setPassword(newPassword);
            session.setAttribute("currentUser", user);
            redirectAttributes.addFlashAttribute("success", "Thay đổi mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thay đổi mật khẩu thất bại: " + e.getMessage());
        }
        return "redirect:/customer/profile";
    }
}
