package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservice-gps", url = "localhost:9001")
public interface GpsProxy {

  @GetMapping(value = "/getLocation")
  String getLocationThroughMS(@RequestParam String userName);

}
