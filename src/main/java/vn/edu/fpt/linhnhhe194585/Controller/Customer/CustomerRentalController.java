package vn.edu.fpt.linhnhhe194585.Controller.Customer;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.CarRental;
import vn.edu.fpt.linhnhhe194585.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerRentalController {
    @Autowired
    private RentalService rentalService;

    private boolean checkAuth(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Customer".equalsIgnoreCase(user.getRole())) {
            return false;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("customer", user.getCustomer());
        return true;
    }

    @PostMapping("/rent")
    public String rentCars(@RequestParam("carIds") String carIdsStr,
                           @RequestParam("pickupDate") String pickupDateStr,
                           @RequestParam("returnDate") String returnDateStr,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Customer".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            rentalService.createRentals(user.getCustomer().getId(), carIdsStr, pickupDateStr, returnDateStr);
            redirectAttributes.addFlashAttribute("success", "Đặt xe thành công!");
            return "redirect:/customer/history";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thuê xe thất bại: " + e.getMessage());
            return "redirect:/customer/dashboard";
        }
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        Account user = (Account) session.getAttribute("currentUser");
        List<CarRental> rentals = rentalService.getRentalsByCustomer(user.getCustomer().getId());
        List<Integer> reviewedRentalIds = rentalService.getReviewedRentalIds(user.getCustomer().getId());
        model.addAttribute("rentals", rentals);
        model.addAttribute("reviewedRentalIds", reviewedRentalIds);
        return "customer/history";
    }

    @PostMapping("/review")
    public String submitReview(@RequestParam("rentalId") Integer rentalId,
                               @RequestParam("stars") Integer stars,
                               @RequestParam("comment") String comment,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Customer".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            rentalService.addReview(rentalId, stars, comment);
            redirectAttributes.addFlashAttribute("success", "Đã gửi đánh giá thành công! Cảm ơn bạn.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đánh giá thất bại: " + e.getMessage());
        }
        return "redirect:/customer/history";
    }
}
