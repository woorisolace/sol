package com.example.clean.Controller;

import com.example.clean.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;


}
