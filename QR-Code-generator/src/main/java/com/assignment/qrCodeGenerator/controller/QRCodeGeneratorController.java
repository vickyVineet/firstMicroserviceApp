package com.assignment.qrCodeGenerator.controller;

import com.assignment.qrCodeGenerator.service.QRCodeGeneratorService;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Base64;

@Controller
public class QRCodeGeneratorController {

    @GetMapping("/")
    public String home(Model model) {

        return "index";
    }

    @PostMapping("/generate")
    public String getQRCode(Model model,
                            @RequestParam(value = "text",defaultValue = "Knf") String text) {

        byte[] image = new byte[0];

        try {
            // Generate and Return Qr Code in Byte Array
            image = QRCodeGeneratorService.getQRCode(text, 250, 250);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder()
                .encodeToString(image);
        model.addAttribute("qrcode", qrcode);

        return "index";
    }
}
