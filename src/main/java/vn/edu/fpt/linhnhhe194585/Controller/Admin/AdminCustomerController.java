package vn.edu.fpt.linhnhhe194585.Controller.Admin;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminCustomerController {
    @Autowired
    private CustomerService customerService;

    private boolean checkAuth(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            return false;
        }
        model.addAttribute("currentUser", user);
        return true;
    }

    @GetMapping("/customers")
    public String manageCustomers(HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        model.addAttribute("customers", customerService.getAllCustomers());
        return "admin/customers";
    }

    @PostMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa khách hàng thất bại: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }
}
