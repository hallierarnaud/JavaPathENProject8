package tourGuide.object;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitedLocationResponse implements Comparable<VisitedLocationResponse> {

  public UUID userId;
  public LocationResponse locationResponse;
  public Date timeVisited;

  @Override
  public int compareTo(VisitedLocationResponse visitedLocationResponse) {
    return getTimeVisited().compareTo(visitedLocationResponse.getTimeVisited());
  }

}
