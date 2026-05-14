public class Warden
{
   private String id, given, family, dimension, identity, role, status, clearance, date;

   public Warden(String id, String given, String family, String dimension,
      String identity, String role, String status, String clearance, String date)
   {
      this.id = id;
      this.given = given;
      this.family = family;
      this.dimension = dimension;
      this.identity = identity;
      this.role = role;
      this.status = status;
      this.clearance = clearance;
      this.date = date;
   }

   public String getId(){ return id; }
   public String getGiven(){ return given; }
   public String getFamily(){ return family; }
   public String getDimension(){ return dimension; }
   public String getIdentity(){ return identity; }
   public String getRole(){ return role; }
   public String getStatus(){ return status; }
   public String getClearance(){ return clearance; }
   public String getDate(){ return date; }
}