package tourGuide.user;

import java.util.UUID;

import lombok.Data;

@Data
public class AttractionDTOResponse {

  public UUID attractionId;
  public String attractionName;
  public String city;
  public String state;
  public double longitude;
  public double latitude;

}
