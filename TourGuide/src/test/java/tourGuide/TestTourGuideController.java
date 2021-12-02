package tourGuide;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import tourGuide.object.LocationResponse;
import tourGuide.object.VisitedLocationResponse;
import tourGuide.proxies.GpsProxy;
import tourGuide.proxies.RewardsProxy;
import tourGuide.service.TourGuideService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestTourGuideController {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TourGuideService tourGuideService;

  @MockBean
  private GpsProxy gpsProxy;

  @MockBean
  private RewardsProxy rewardsProxy;

  @Test
  public void getAllCurrentLocations_shouldReturnOk() throws Exception {
    when(tourGuideService.getAllCurrentLocations()).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/getAllCurrentLocations")).andExpect(status().isOk());
  }

  @Test
  public void getLocation_shouldReturnOk() throws Exception {
    when(tourGuideService.getUserLocationResponse(any())).thenReturn(new VisitedLocationResponse(UUID.randomUUID(), new LocationResponse(0.0, 0.0), new Date()));
    mockMvc.perform(get("/getLocation")
            .contentType(MediaType.APPLICATION_JSON)
            .param("userName", "userTest"))
            .andExpect(status().isOk());
  }

  @Test
  public void getNearbyAttractions_shouldReturnOk() throws Exception {
    when(tourGuideService.getNearByAttractions(any())).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/getNearbyAttractions")
            .contentType(MediaType.APPLICATION_JSON)
            .param("userName", "userTest"))
            .andExpect(status().isOk());
  }

  @Test
  public void getRewards_shouldReturnOk() throws Exception {
    when(tourGuideService.getUserRewards(any())).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/getRewards")
            .contentType(MediaType.APPLICATION_JSON)
            .param("userName", "userTest"))
            .andExpect(status().isOk());
  }

  @Test
  public void getTripDeals_shouldReturnOk() throws Exception {
    when(tourGuideService.getTripDeals(any())).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/getTripDeals")
            .contentType(MediaType.APPLICATION_JSON)
            .param("userName", "userTest"))
            .andExpect(status().isOk());
  }

  // My endpoints

  @Test
  public void getUserLocation_shouldReturnOk() throws Exception {
    when(gpsProxy.getUserLocation(any(UUID.class))).thenReturn(new VisitedLocationResponse(UUID.randomUUID(), new LocationResponse(0.0, 0.0), new Date()));
    mockMvc.perform(get("/trackUserLocations")
            .contentType(MediaType.APPLICATION_JSON)
            .param("userId", "6ba7b810-9dad-11d1-80b4-00c04fd430c8"))
            .andExpect(status().isOk());
  }

  @Test
  public void getAttractions_shouldReturnOk() throws Exception {
    when(gpsProxy.getAttractions()).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/attractions"))
            .andExpect(status().isOk());
  }

  @Test
  public void getRewardsBis_shouldReturnOk() throws Exception {
    when(rewardsProxy.getRewards(any(UUID.class), any(UUID.class))).thenReturn(100);
    mockMvc.perform(get("/rewards")
            .contentType(MediaType.APPLICATION_JSON)
            .param("attractionId", "6ba7b810-9dad-11d1-80b4-00c04fd430c8")
            .param("userId", "6ba7b810-9dad-11d1-80b4-00c04fd430c8"))
            .andExpect(status().isOk());
  }

}
