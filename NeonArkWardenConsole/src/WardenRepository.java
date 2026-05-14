import java.io.*;
import java.util.*;

public class WardenRepository
{
   private ArrayList<Warden> list;
   private String file;

   public WardenRepository(String file)
   {
      this.file = file;
      list = new ArrayList<>();
   }

   public void loadWardens()
   {
      try
      {
         Scanner sc = new Scanner(new File(file));

         if (sc.hasNextLine()) sc.nextLine();

         while (sc.hasNextLine())
         {
            String[] v = sc.nextLine().split(",", -1);

            list.add(new Warden(v[0], v[1], v[2], v[3],
               v[4], v[5], v[6], v[7], v[8]));
         }

         sc.close();
      }
      catch (Exception e)
      {
         System.out.println("CSV not found.");
      }
   }

   public ArrayList<Warden> getWardens()
   {
      return list;
   }

   public boolean identityExists(String id)
   {
      for (Warden w : list)
      {
         if (w.getIdentity().equalsIgnoreCase(id))
            return true;
      }
      return false;
   }
}