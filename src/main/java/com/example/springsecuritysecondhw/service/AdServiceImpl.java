package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.dto.AdDto;
import com.example.springsecuritysecondhw.model.Ad;
import com.example.springsecuritysecondhw.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;


    @Override
    public AdDto getAdInfo(Long id) {
        if (existById(id)) {
            return convertAdToAdDto(adRepository.findById(id).get());
        }
        return null;
    }

    @Override
    public List<AdDto> getAllAds() {
        if (adRepository.findAll() != null) {
            List<Ad> adsAll = adRepository.findAll();
            return adsAll.stream().map(this::convertAdToAdDto).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean createAd(AdDto adDto) {
        if(adRepository.findByTitle(adDto.getTitle()).isEmpty() && adRepository.findByPrice(adDto.getPrice()).isEmpty()){
            adRepository.save(convertAdDtoToAd(adDto));
            return true;
        }
        return false;
    }

    @Override
    public boolean editAd(Long id, AdDto adDto) {
        if (existById(id)) {
            Ad ad = adRepository.findById(id).get();
            ad.setTitle(adDto.getTitle());
            ad.setPrice(adDto.getPrice());
            adRepository.save(ad);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDto(Long id) {
        if (existById(id)) {
            adRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AdDto convertAdToAdDto(Ad ad) {
        AdDto adDto = new AdDto();
        adDto.setTitle(ad.getTitle());
        adDto.setPrice(ad.getPrice());
        return adDto;
    }

    private Ad convertAdDtoToAd(AdDto adDto) {
        Ad ad = new Ad();
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        return ad;
    }



    private boolean existById(Long id) {
        return adRepository.existsById(id);
    }
}
