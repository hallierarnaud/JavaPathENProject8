package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import gpsUtil.location.VisitedLocation;
import tourGuide.user.EntityBean;

@FeignClient(name = "microservice-gps", url = "localhost:9001")
public interface GpsProxy {

  @GetMapping(value = "/getLocation")
  VisitedLocation getLocationThroughMS(@RequestParam String userName);

  @GetMapping(value = "/locations")
  List<EntityBean> entityBeanList();

}
