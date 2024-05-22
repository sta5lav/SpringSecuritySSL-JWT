package com.example.springsecuritysecondhw.controller;

import com.example.springsecuritysecondhw.dto.AdDto;
import com.example.springsecuritysecondhw.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @GetMapping("/{id}")
    public ResponseEntity<AdDto> getAdInfo(@PathVariable Long id) {
        AdDto adDto = adService.getAdInfo(id);
        if (adDto != null) {
            return ResponseEntity.ok(adDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AdDto>> getAllAds() {
        List<AdDto> ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @PostMapping
    public ResponseEntity<Void> createAd(@RequestBody AdDto adDto) {
        if (adService.createAd(adDto)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editAd(@PathVariable Long id, @RequestBody AdDto adDto) {
        if (adService.editAd(id, adDto)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        if (adService.deleteDto(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
