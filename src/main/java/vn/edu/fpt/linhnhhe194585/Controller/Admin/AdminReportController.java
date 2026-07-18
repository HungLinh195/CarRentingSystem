package vn.edu.fpt.linhnhhe194585.Controller.Admin;

import java.math.BigDecimal;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.CarRental;
import vn.edu.fpt.linhnhhe194585.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminReportController {
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

    @GetMapping("/report")
    public String report(HttpSession session,
                         @RequestParam(value = "startDate", required = false) String startDateStr,
                         @RequestParam(value = "endDate", required = false) String endDateStr,
                         Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : LocalDate.now().minusMonths(1);
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : LocalDate.now();

        List<CarRental> rentals = rentalService.getReport(startDate, endDate);
        BigDecimal totalRevenue = rentals.stream()
                .map(CarRental::getRentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("rentals", rentals);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalRevenue", totalRevenue);
        return "admin/report";
    }
}
