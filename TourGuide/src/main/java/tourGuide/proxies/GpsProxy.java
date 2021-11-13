package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import tourGuide.object.AttractionResponse;
import tourGuide.object.VisitedLocationResponse;

@FeignClient(name = "microservice-gps", url = "localhost:9001")
public interface GpsProxy {

  @GetMapping(value = "/trackUserLocations")
  VisitedLocationResponse getUserLocation(@RequestParam UUID userId);

  @GetMapping(value = "/attractions")
  List<AttractionResponse> getAttractions();

}
