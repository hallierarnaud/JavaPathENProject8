package tourGuide.object;

import lombok.Data;

@Data
public class NearbyAttraction implements Comparable<NearbyAttraction> {

  public String attractionName;
  public double attractionLatitude;
  public double attractionLongitude;
  public double touristLatitude;
  public double touristLongitude;
  public int distanceBetweenAttractionAndTourist;
  public int rewardsPoint;

  @Override
  public int compareTo(NearbyAttraction nearbyAttraction) {
    return this.distanceBetweenAttractionAndTourist - nearbyAttraction.distanceBetweenAttractionAndTourist;
  }

}
