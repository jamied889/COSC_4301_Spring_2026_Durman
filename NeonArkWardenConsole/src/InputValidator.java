public class InputValidator
{
   public boolean validDate(String d)
   {
      return d.matches("\\d{4}-\\d{2}-\\d{2}");
   }

   public boolean validRole(String r)
   {
      return r.equalsIgnoreCase("Admin") ||
             r.equalsIgnoreCase("Field") ||
             r.equalsIgnoreCase("Rift") ||
             r.equalsIgnoreCase("Trainer") ||
             r.equalsIgnoreCase("Astral");
   }

   public boolean validStatus(String s)
   {
      return s.equalsIgnoreCase("Active") ||
             s.equalsIgnoreCase("Inactive") ||
             s.equalsIgnoreCase("Suspended") ||
             s.equalsIgnoreCase("Terminated");
   }
}