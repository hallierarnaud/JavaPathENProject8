package tourGuide.user;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class VisitedLocationDTOResponse {

  public UUID userId;
  //Replace locationDTOResponse attribute name by locationsRequest to enable json mapping
  public LocationDTOResponse locationRequest;
  public Date timeVisited;

}
