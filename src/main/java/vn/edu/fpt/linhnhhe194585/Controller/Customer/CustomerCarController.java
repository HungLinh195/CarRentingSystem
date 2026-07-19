package vn.edu.fpt.linhnhhe194585.Controller.Customer;

import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Service.CarService;
import vn.edu.fpt.linhnhhe194585.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerCarController {
    @Autowired
    private CarService carService;

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

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "color", required = false) String color,
                            @RequestParam(value = "capacity", required = false) Integer capacity,
                            Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        List<Car> cars = carService.getCars(name, color, capacity);

        model.addAttribute("cars", cars);
        model.addAttribute("name", name);
        model.addAttribute("color", color);
        model.addAttribute("capacity", capacity);
        return "customer/dashboard";
    }

    @GetMapping("/cars/{id}")
    public String carDetail(@PathVariable("id") Integer id, HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        return carService.getCarById(id)
                .map(car -> {
                    model.addAttribute("car", car);
                    model.addAttribute("reviews", rentalService.getReviewsForCar(id));
                    return "customer/car-detail";
                })
                .orElse("redirect:/customer/dashboard");
    }
}
