package com.example.kem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;

@Controller
public class KemController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Kem");
        model.addAttribute("desc", "Chuyên gia nếm Pate & Phá hoại đồ đạc");

        // Danh sách link ảnh (Demo dùng ảnh mèo online, bạn thay bằng ảnh thật sau nhé)
        List<String> photos = Arrays.asList(
                "1db5c905-b82e-4a82-8c00-c4c080c00d1c.jpg",
                "4e7dbd6a-129c-4845-a786-1181eec00b26.jpg",
                "eb348b49-27e4-4b2e-a0e5-99c19ebe52cf.jpg",
                "6a1fcd89-806a-4085-a932-dd613d760602.jpg",
                "9c4ef853-e251-4068-b853-57c29fd666e5.jpg",
                "adab0656-85d7-4c6a-ad82-b8c051cecfcd (1).jpg",
                "download.jpg",
                "bec12c13-afcb-4b68-ba6f-3d17797b5ef3.jpg",
                "2fd05019-a441-4a7f-90b8-ad7d451da11c.jpg"
        );
        model.addAttribute("gallery", photos);

        return "index";
    }
}