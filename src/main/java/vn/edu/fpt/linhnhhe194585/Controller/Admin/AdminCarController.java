package vn.edu.fpt.linhnhhe194585.Controller.Admin;

import java.math.BigDecimal;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarProducer;
import vn.edu.fpt.linhnhhe194585.Service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminCarController {
    @Autowired
    private CarService carService;

    private boolean checkAuth(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            return false;
        }
        model.addAttribute("currentUser", user);
        return true;
    }

    @GetMapping("/cars")
    public String manageCars(HttpSession session, Model model) {
        if (!checkAuth(session, model)) return "redirect:/login";

        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("producers", carService.getAllProducers());
        model.addAttribute("newCar", new Car());
        return "admin/cars";
    }

    @PostMapping("/cars/save")
    public String saveCar(@RequestParam(value = "id", required = false) Integer id,
                          @RequestParam("carName") String carName,
                          @RequestParam("carModelYear") Integer carModelYear,
                          @RequestParam("color") String color,
                          @RequestParam("capacity") Integer capacity,
                          @RequestParam("description") String description,
                          @RequestParam("importDate") String importDateStr,
                          @RequestParam("producerId") Integer producerId,
                          @RequestParam("rentPrice") BigDecimal rentPrice,
                          @RequestParam("status") String status,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            CarProducer producer = carService.getProducerById(producerId)
                    .orElseThrow(() -> new IllegalArgumentException("Nhà sản xuất không hợp lệ."));

            Car car = Car.builder()
                    .id(id)
                    .carName(carName)
                    .carModelYear(carModelYear)
                    .color(color)
                    .capacity(capacity)
                    .description(description)
                    .importDate(LocalDate.parse(importDateStr))
                    .carProducer(producer)
                    .rentPrice(rentPrice)
                    .status(status)
                    .build();

            carService.saveCar(car);
            redirectAttributes.addFlashAttribute("success", "Lưu thông tin xe thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lưu thất bại: " + e.getMessage());
        }
        return "redirect:/admin/cars";
    }

    @PostMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Account user = (Account) session.getAttribute("currentUser");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        try {
            boolean physicallyDeleted = carService.deleteCar(id);
            if (physicallyDeleted) {
                redirectAttributes.addFlashAttribute("success", "Đã xóa xe khỏi hệ thống.");
            } else {
                redirectAttributes.addFlashAttribute("success", "Xe đã có giao dịch thuê. Trạng thái xe được cập nhật thành Bảo trì (Maintenance).");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa xe thất bại: " + e.getMessage());
        }
        return "redirect:/admin/cars";
    }
}
