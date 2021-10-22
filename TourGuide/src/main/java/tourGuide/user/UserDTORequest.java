package tourGuide.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import lombok.Data;
import tripPricer.Provider;

@Data
public class UserDTORequest {

  private UUID userId;
  private String userName;
  private String phoneNumber;
  private String emailAddress;
  private Date latestLocationTimestamp;
  private List<VisitedLocation> visitedLocations = new ArrayList<>();
  private List<UserReward> userRewards = new ArrayList<>();
  private UserPreferences userPreferences = new UserPreferences();
  private List<Provider> tripDeals = new ArrayList<>();

}
