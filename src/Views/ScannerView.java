package Views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScannerView
{
    private final Scanner scanner;
    
    public ScannerView(Scanner scanner)
    {
        this.scanner = scanner;
    }
    
    private void clearScreen()
    {
        System.out.print("\033c");
        System.out.flush();
    }
    
    private void showScannerError(String message)
    {
        System.out.println("Error: " + message);
    }
    
    public Integer getInteger(String text)
    {
        Integer value = null;
        
        do
        {
            this.clearScreen();
            
            System.out.print(text + ": ");
            
            try
            {
                value = this.scanner.nextInt();
                
                this.scanner.nextLine();
            }
            catch (InputMismatchException e)
            {
                this.scanner.nextLine();
                
                this.showScannerError("No ingresaste un numero entero");
            }
            catch (NoSuchElementException | IllegalStateException e)
            {
                this.scanner.nextLine();
                
                this.showScannerError(e.getMessage());
            }
        }
        while(value == null);
        
        return value;
    }
    
    public Double getDouble(String text)
    {
        Double value = null;
        
        do
        {
            this.clearScreen();
            
            System.out.print(text + ": ");
            
            try
            {
                value = this.scanner.nextDouble();
                
                this.scanner.nextLine();
            }
            catch (InputMismatchException e)
            {
                this.scanner.nextLine();
                
                this.showScannerError("No ingresaste un numero decimal");
            }
            catch (NoSuchElementException | IllegalStateException e)
            {
                this.scanner.nextLine();
                
                this.showScannerError(e.getMessage());
            }
        }
        while(value == null);
        
        return value;
    }
    
    public String getString(String text)
    {
        String value = null;
        
        do
        {
            this.clearScreen();
            
            System.out.print(text + ": ");
            
            try
            {
                value = this.scanner.nextLine().trim();
            }
            catch (NoSuchElementException | IllegalStateException e)
            {
                this.scanner.nextLine();
                
                this.showScannerError(e.getMessage());
            }
        }
        while(value == null);
        
        return value;
    }
    
    public LocalDate getLocalDate(String text)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate value = null;
        
        while (value == null)
        {
            this.clearScreen();
            
            System.out.print(text + ": ");
            
            String input = this.scanner.nextLine().trim();
            
            try {
                value = LocalDate.parse(input, formatter);
            }
            catch (NoSuchElementException | IllegalStateException e)
            {
                this.scanner.nextLine();
                
                this.showScannerError(e.getMessage());
            }
            catch (DateTimeParseException e) {
                this.showScannerError("Formato invalido. Por favor usa dd-MM-yyyy.");
            }
        }
        
        return value;
    }
}
