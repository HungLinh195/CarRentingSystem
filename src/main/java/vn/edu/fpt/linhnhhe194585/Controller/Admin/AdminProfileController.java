package vn.edu.fpt.linhnhhe194585.Controller.Admin;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminProfileController {

    @Autowired
    private AccountService accountService;

    private boolean checkAuth(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            return false;
        }
        model.addAttribute("currentUser", user);
        return true;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";
        return "admin/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("name") String name,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            Account updatedAccount = accountService.updateProfile(user.getId(), name);
            user.setName(updatedAccount.getName());
            session.setAttribute("currentUser", user);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmNewPassword") String confirmNewPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            accountService.changePassword(user.getId(), currentPassword, newPassword, confirmNewPassword);
            user.setPassword(newPassword);
            session.setAttribute("currentUser", user);
            redirectAttributes.addFlashAttribute("success", "Thay đổi mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thay đổi mật khẩu thất bại: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
}
