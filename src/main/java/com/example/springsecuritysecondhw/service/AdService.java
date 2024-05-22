package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.dto.AdDto;

import java.util.List;

public interface AdService {

    AdDto getAdInfo(Long id);

    List<AdDto> getAllAds();

    boolean createAd(AdDto adDto);

    boolean editAd(Long id, AdDto adDto);

    boolean deleteDto(Long id);

}
