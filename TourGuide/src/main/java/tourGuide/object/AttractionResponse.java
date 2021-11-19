package tourGuide.object;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttractionResponse {

  public UUID attractionId;
  public String attractionName;
  public String city;
  public String state;
  public double latitude;
  public double longitude;

}
