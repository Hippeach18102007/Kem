package com.example.kem;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class KemController {

    // Giả lập database lưu comment trong bộ nhớ (dùng CopyOnWriteArrayList để tránh lỗi khi nhiều người chat cùng lúc)
    private List<Comment> commentList = new CopyOnWriteArrayList<>();

    @GetMapping("/")
    public String home(Model model) {
        // 1. Thông tin cơ bản
        model.addAttribute("name", "Kem");
        model.addAttribute("desc", "Chuyên gia nếm Pate & Phá hoại đồ đạc");

        // 2. Chức năng Random Trạng Thái
        List<String> statuses = Arrays.asList(
                "Đang ngủ chổng vó 💤",
                "Đang rình con thạch sùng 🦎",
                "Đang đòi pate 🐟",
                "Đang chạy parkour lúc 3h sáng 🏃",
                "Đang liếm lông sang chảnh 💅"
        );
        String currentStatus = statuses.get(new Random().nextInt(statuses.size()));
        model.addAttribute("status", currentStatus);

        // 3. Danh sách ảnh (Giữ nguyên của bạn)
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

        // 4. Đẩy danh sách comment và object form rỗng xuống view
        model.addAttribute("comments", commentList);
        model.addAttribute("newComment", new Comment());

        return "index";
    }

    // Xử lý khi người dùng bấm nút Gửi lời nhắn
    @PostMapping("/add-comment")
    public String addComment(@ModelAttribute Comment comment) {
        // Gán thời gian hiện tại
        comment.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM")));
        // Thêm vào đầu danh sách để comment mới nhất lên trên
        commentList.add(0, comment);

        // Redirect về trang chủ để tránh lỗi gửi lại form khi F5
        return "redirect:/";
    }

    // Class nội bộ để chứa dữ liệu Comment
    public static class Comment {
        private String author;
        private String content;
        private String time;

        // Getters & Setters bắt buộc phải có
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
    }
    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // Sẽ trỏ vào file chat.html
    }

    // 2. Nhận tin nhắn từ user và gửi cho tất cả (/topic/public)
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    // 3. Nhận thông báo có người mới vào phòng chat
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Lưu tên user vào session của websocket để dùng khi họ thoát
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}