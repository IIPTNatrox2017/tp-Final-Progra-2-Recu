package Controllers;

import Enums.MenuConst;
import Exceptions.GetItemsInPageException;
import Exceptions.InvalidOptionMenuException;
import Exceptions.MenuDoNotExistsException;
import Models.Menu;
import Views.MenuView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuController
{
    private final Scanner scanner;
    private final Map<String, Menu> menuMap;
    
    public MenuController(Scanner scanner)
    {
        this.scanner = scanner;
        this.menuMap = new HashMap<>();
    }
    
    public Boolean addMenu(String key, Menu menu)
    {
        if(this.menuMap.containsKey(key) == true)
        {
            return false;
        }
        
        this.menuMap.put(key, menu);
        
        return true;
    }
    
    public Menu updateMenu(String key, Menu menu)
    {
        return this.menuMap.put(key, menu);
    }
    
    public Menu getMenu(String key)
    {
        return this.menuMap.get(key);
    }
    
    public Boolean removeMenu(String key)
    {
        if (this.menuMap.containsKey(key) == false)
        {
            return false;
        }
        
        this.menuMap.remove(key);
        
        return true;
    }
    
    public Integer showMenu(String key) throws MenuDoNotExistsException, GetItemsInPageException
    {
        Menu menu = this.getMenu(key);
        
        if (menu == null)
        {
            throw new MenuDoNotExistsException("No such menu exists!");
        }
        
        MenuView menuView = new MenuView(scanner);
        Integer opcion = MenuConst.INVALID_OPTION.getValue();
        
        do
        {
            String title = menu.getTitle();
            List<String> list = menu.getItemsInPage();
            Integer page = menu.getCurrentPage();
            
            try
            {
                opcion = menuView.showMenu(title, list, page, menu.hasPreviousPage(), menu.hasNextPage());
                
                menu.checkValidOption(opcion);
                
                menu.resolve(opcion);
            }
            catch (InvalidOptionMenuException e)
            {
                menuView.showOpcionError(e.getMessage());
                
                opcion = MenuConst.INVALID_OPTION.getValue();
            }
        }
        while (opcion == MenuConst.PREV.getValue() ||
                opcion == MenuConst.NEXT.getValue() ||
                opcion == MenuConst.INVALID_OPTION.getValue()
        );
        
        return menu.getLastIndexSelected();
    }
    
    public Integer showMenuTemp(Menu menu)
    {
        String key = "__temporary_menu__";
        this.addMenu(key, menu);
        
        try
        {
            return this.showMenu(key);
        }
        catch (MenuDoNotExistsException | GetItemsInPageException e)
        {
            new MenuView(scanner).showOpcionError("No se pudo mostrar el menú: " + e.getMessage());
        }
        finally
        {
            this.removeMenu(key);
        }
        
        return MenuConst.INVALID_OPTION.getValue();
    }
}
