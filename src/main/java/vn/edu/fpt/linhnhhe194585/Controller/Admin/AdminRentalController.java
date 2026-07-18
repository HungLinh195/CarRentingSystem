package vn.edu.fpt.linhnhhe194585.Controller.Admin;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminRentalController {
    @Autowired
    private RentalService rentalService;

    private boolean checkAuth(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            return false;
        }
        model.addAttribute("currentUser", user);
        return true;
    }

    @GetMapping("/rentals")
    public String manageRentals(HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        model.addAttribute("rentals", rentalService.getAllRentals());
        return "admin/rentals";
    }

    @PostMapping("/rentals/complete/{id}")
    public String completeRental(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            rentalService.updateRentalStatus(id, "Completed");
            redirectAttributes.addFlashAttribute("success", "Giao dịch thuê xe đã được đánh dấu là Hoàn thành!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/rentals";
    }
}
