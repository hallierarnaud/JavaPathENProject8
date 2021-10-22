package tourGuide.user;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class VisitedLocationDTOResponse {

  public UUID userId;
  public LocationDTOResponse locationDTOResponse;
  public Date timeVisited;

}
