import java.util.Scanner;
import java.util.List;

public class ConsoleController
{
   private Scanner keyboard;
   private WardenRepository repo;
   private InputValidator validator;
   private boolean running;

   public ConsoleController()
   {
      keyboard = new Scanner(System.in);
      repo = new WardenRepository("data/wardens.csv");
      validator = new InputValidator();
      running = true;
   }

   public void runConsole()
   {
      repo.loadWardens();

      while (running)
      {
         displayMenu();
         handleChoice(keyboard.nextLine().trim());
      }

      keyboard.close();
   }

   private void displayMenu()
   {
      System.out.println("\n=========================================================");
      System.out.println(" NEON ARK — ADMIN WARDEN ONBOARDING CONSOLE");
      System.out.println("=========================================================");
      System.out.println("1. Add New Warden");
      System.out.println("2. View Wardens");
      System.out.println("3. Update Warden");
      System.out.println("4. Manage Certifications");
      System.out.println("5. Deactivate / Terminate Warden");
      System.out.println("6. Exit");
      System.out.print("Enter choice: ");
   }

   private void handleChoice(String choice)
   {
      if (choice.equals("1")) addWarden();
      else if (choice.equals("2")) viewWardens();
      else if (choice.equals("3")) simulateUpdate();
      else if (choice.equals("4")) simulateCertifications();
      else if (choice.equals("5")) simulateDeactivate();
      else if (choice.equals("6")) running = false;
      else System.out.println("Invalid option.");
   }

   private void addWarden()
   {
      System.out.println("\n[ ADD NEW WARDEN ]");

      String id = readRequired("Warden ID");
      String given = readRequired("Given Name");
      String family = readOptional("Family Name");
      String dimension = readRequired("Dimension Origin");
      String identity = readUniqueIdentity();
      String role = readRole();
      String status = readStatus();
      String clearance = readRequired("Clearance Level");
      String date = readDate();

      Warden w = new Warden(id, given, family, dimension,
         identity, role, status, clearance, date);

      System.out.println("\nWOULD SEND: POST /api/wardens");
      System.out.println("PAYLOAD:");
      System.out.println("{");
      System.out.println("  \"warden_id\": \"" + id + "\",");
      System.out.println("  \"given_name\": \"" + given + "\",");
      System.out.println("  \"family_name\": \"" + family + "\",");
      System.out.println("  \"dimension_origin\": \"" + dimension + "\",");
      System.out.println("  \"identity_code\": \"" + identity + "\",");
      System.out.println("  \"role_name\": \"" + role + "\",");
      System.out.println("  \"employment_status\": \"" + status + "\",");
      System.out.println("  \"clearance_level\": \"" + clearance + "\",");
      System.out.println("  \"hire_date\": \"" + date + "\"");
      System.out.println("}");

      System.out.println("RESULT: SUCCESS (simulated)");
      pause();
   }

   private void viewWardens()
   {
      List<Warden> list = repo.getWardens();

      System.out.println("\n[ ALL WARDENS ]");

      System.out.printf("%-8s %-12s %-12s %-16s %-14s %-10s %-12s %-10s %-12s\n",
         "ID","First","Last","Dimension","Identity","Role","Status","Clear","HireDate");

      for (Warden w : list)
      {
         System.out.printf("%-8s %-12s %-12s %-16s %-14s %-10s %-12s %-10s %-12s\n",
            w.getId(), w.getGiven(), w.getFamily(), w.getDimension(),
            w.getIdentity(), w.getRole(), w.getStatus(),
            w.getClearance(), w.getDate());
      }

      pause();
   }

   private void simulateUpdate()
   {
      System.out.println("\nAction: Update Warden");
      System.out.println("Inputs: warden_id, field, new_value");
      System.out.println("WOULD SEND: PUT /api/wardens/{id}");
      System.out.println("RESULT: SUCCESS (simulated)");
      pause();
   }

   private void simulateCertifications()
   {
      System.out.println("\nAction: Manage Certifications");
      System.out.println("WOULD SEND: POST /api/wardens/{id}/certifications");
      System.out.println("RESULT: SUCCESS (simulated)");
      pause();
   }

   private void simulateDeactivate()
   {
      System.out.println("\nAction: Deactivate Warden");
      System.out.println("WOULD SEND: PUT /api/wardens/{id}/status");
      System.out.println("RESULT: SUCCESS (simulated)");
      pause();
   }

   private String readRequired(String field)
   {
      String val;

      do
      {
         System.out.print(field + ": ");
         val = keyboard.nextLine().trim();
         if (val.equals("")) System.out.println("Cannot be blank.");
      } while (val.equals(""));

      return val;
   }

   private String readOptional(String field)
   {
      System.out.print(field + ": ");
      return keyboard.nextLine().trim();
   }

   private String readUniqueIdentity()
   {
      String id;

      do
      {
         System.out.print("Identity Code: ");
         id = keyboard.nextLine().trim();

         if (repo.identityExists(id))
            System.out.println("Identity already exists.");

      } while (id.equals("") || repo.identityExists(id));

      return id;
   }

   private String readRole()
   {
      String r;

      do
      {
         System.out.print("Role (Admin/Field/Rift/Trainer/Astral): ");
         r = keyboard.nextLine().trim();
      } while (!validator.validRole(r));

      return r;
   }

   private String readStatus()
   {
      String s;

      do
      {
         System.out.print("Status (Active/Inactive/Suspended/Terminated): ");
         s = keyboard.nextLine().trim();
      } while (!validator.validStatus(s));

      return s;
   }

   private String readDate()
   {
      String d;

      do
      {
         System.out.print("Hire Date (YYYY-MM-DD): ");
         d = keyboard.nextLine().trim();
      } while (!validator.validDate(d));

      return d;
   }

   private void pause()
   {
      System.out.print("\nPress ENTER...");
      keyboard.nextLine();
   }
}