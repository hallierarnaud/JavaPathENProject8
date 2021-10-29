package tourGuide.user;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDTOFromGpsService {

  private UUID userId;
  private String userName;
  private String phoneNumber;
  private String emailAddress;
  private Date latestLocationTimestamp;
  private List<VisitedLocationDTOFromGpsService> visitedLocationResponseToMainServiceList;

}
