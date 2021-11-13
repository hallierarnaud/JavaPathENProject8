package tourGuide.object;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class VisitedLocationResponse {

  public UUID userId;
  public LocationResponse locationResponse;
  public Date timeVisited;

}
