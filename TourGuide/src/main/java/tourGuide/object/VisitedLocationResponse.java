package tourGuide.object;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitedLocationResponse {

  public UUID userId;
  public LocationResponse locationResponse;
  public Date timeVisited;

}
